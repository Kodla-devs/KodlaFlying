package fr.kozen.skyrama.commands.subcommands;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.interfaces.ISubCommand;
import fr.kozen.skyrama.objects.islands.Island;
import fr.kozen.skyrama.utils.NotificationManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class GoToCommand implements ISubCommand {

    @Override
    public String getName() {
        return "git";
    }
    
    // Alternatif isim kontrolü için
    public boolean isAlias(String name) {
        return name.equalsIgnoreCase("git") || name.equalsIgnoreCase("ziyaret");
    }

    @Override
    public String getDescription() {
        return "Belirtilen oyuncunun adasına git";
    }

    @Override
    public String getPermission() { 
        return "skyrama.command.goto"; 
    }

    @Override
    public String getSyntax() {
        return "/ada git|ziyaret <oyuncu>";
    }

    @Override
    public List<String> getArgs() { 
        return Arrays.asList(); 
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Skyrama.getLocaleManager().getString("invalid-syntax").replace("{0}", getSyntax()));
            NotificationManager.playErrorSound(player);
            return;
        }

        String targetName = args[1];
        Player targetPlayer = Bukkit.getPlayer(targetName);
        OfflinePlayer offlineTarget = null;

        // Önce çevrimiçi oyuncuları kontrol et
        if (targetPlayer != null) {
            offlineTarget = targetPlayer;
        } else {
            // Çevrimdışı oyuncuları kontrol et
            for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                if (offlinePlayer.getName() != null && offlinePlayer.getName().equalsIgnoreCase(targetName)) {
                    offlineTarget = offlinePlayer;
                    break;
                }
            }
        }

        if (offlineTarget == null) {
            player.sendMessage(Skyrama.getLocaleManager().getString("player-offline").replace("{0}", targetName));
            NotificationManager.playErrorSound(player);
            return;
        }

        Island targetIsland = Skyrama.getIslandManager().getPlayerIsland(offlineTarget.getPlayer());
        
        if (targetIsland == null) {
            player.sendMessage(Skyrama.getLocaleManager().getString("player-offline-island").replace("{0}", targetName));
            NotificationManager.playErrorSound(player);
            return;
        }

        // Kendi adasına gitmeye çalışıyor mu?
        if (targetIsland.getOwner().equals(player)) {
            player.sendMessage("§c Kendi adanıza gitmek için /ada ev komutunu kullanın!");
            NotificationManager.playErrorSound(player);
            return;
        }

        // Teleportasyon işlemi
        try {
            targetIsland.getSpawn().setWorld(Bukkit.getWorld((String) Skyrama.getPlugin(Skyrama.class).getConfig().get("general.world")));
            
            // Bildirim ve efektler
            NotificationManager.sendTeleportNotification(player, offlineTarget.getName());

            // capture values used in the lambda so they are effectively final
            final String offlineTargetName = offlineTarget.getName();
            final Location spawnLocation = targetIsland.getSpawn();

            // 2 saniye sonra ışınla (teleportasyon gecikmesi için)
            Bukkit.getScheduler().runTaskLater(Skyrama.getPlugin(Skyrama.class), () -> {
                player.teleport(spawnLocation);
                NotificationManager.sendSuccessNotification(player, offlineTargetName + "'nin adasına başarıyla ışınlandınız!");
            }, 40L); // 40 tick = 2 saniye
            
        } catch (Exception e) {
            player.sendMessage("§c Işınlanma sırasında bir hata oluştu!");
            NotificationManager.sendErrorNotification(player, "Teleportasyon hatası!");
            e.printStackTrace();
        }
    }
}
