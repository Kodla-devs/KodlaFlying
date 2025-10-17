package fr.kozen.skyrama.commands.subcommands;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.interfaces.ISubCommand;
import fr.kozen.skyrama.utils.NotificationManager;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CreateCommand implements ISubCommand {

    @Override
    public String getName() {
        return "olustur";
    }

    @Override
    public String getDescription() {
        return "Bir ada oluştur";
    }

    @Override
    public String getPermission() { return "skyrama.command.create"; }

    @Override
    public String getSyntax() {
        return "/ada olustur";
    }

    @Override
    public List<String> getArgs() { return Arrays.asList(); }

    @Override
    public void perform(Player player, String[] args) {
        if(Skyrama.getIslandManager().getPlayerIsland(player) == null) {
            NotificationManager.sendTitle(player, "§a§lAda Oluşturuluyor...", "§7Lütfen bekleyin");
            NotificationManager.sendActionBar(player, "§a⚡ Ada oluşturuluyor...");
            NotificationManager.playNotificationSound(player);
            
            Skyrama.getIslandManager().create(player);
            
            NotificationManager.sendSuccessNotification(player, "Ada başarıyla oluşturuldu!");
            NotificationManager.sendTitle(player, "§6§lHoş Geldiniz!", "§eAdanız hazır!");
        } else {
            player.sendMessage(Skyrama.getLocaleManager().getString("player-already-have-island"));
            NotificationManager.sendErrorNotification(player, "Zaten bir adanız var!");
        }
    }
}
