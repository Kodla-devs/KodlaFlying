package fr.kozen.skyrama.commands.subcommands;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.interfaces.ISubCommand;
import fr.kozen.skyrama.objects.islands.Island;
import fr.kozen.skyrama.types.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class LeaveCommand implements ISubCommand {

    @Override
    public String getName() {
        return "ayril";
    }

    @Override
    public String getDescription() {
        return "Mevcut adadan ayrıl";
    }

    @Override
    public String getPermission() { return "skyrama.command.leave"; }

    @Override
    public String getSyntax() {
        return "/ada ayril";
    }

    @Override
    public List<String> getArgs() { return Arrays.asList(); }

    @Override
    public void perform(Player player, String[] args) {
        Island island = Skyrama.getIslandManager().getPlayerIsland(player);

        if(island != null) {
            if(island.getRank(player).equals(Rank.OWNER)) {
                island.removePlayer(player);
                player.sendMessage(ChatColor.GREEN + "Adanızı terk ettiniz ve ada silindi.");
                for(OfflinePlayer offlinePlayer : island.getPlayers().keySet()) {
                    if(offlinePlayer.isOnline()) {
                        Player member = offlinePlayer.getPlayer();
                        member.sendMessage(ChatColor.RED + "" + player.getName() + " adayı sildi.");
                        island.removePlayer(member);
                    }
                }
            } else {
                island.removePlayer(player);
                player.sendMessage(ChatColor.GREEN + "Adayı başarıyla terk ettiniz.");
                for(OfflinePlayer offlinePlayer : island.getPlayers().keySet()) {
                    if(offlinePlayer.isOnline()) {
                        Player member = offlinePlayer.getPlayer();
                        member.sendMessage(ChatColor.RED + "" + player.getName() + " adanızı terk etti.");
                    }
                }
            }
        } else {
            player.sendMessage(Skyrama.getLocaleManager().getString("player-no-island"));
        }
    }
}