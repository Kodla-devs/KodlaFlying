package fr.kozen.skyrama.commands.subcommands;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.interfaces.ISubCommand;
import fr.kozen.skyrama.utils.NotificationManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class HomeCommand implements ISubCommand {

    @Override
    public String getName() {
        return "ev";
    }

    @Override
    public String getDescription() {
        return "Ada evine ışınlan";
    }

    @Override
    public String getPermission() { return "skyrama.command.home"; }

    @Override
    public String getSyntax() {
        return "/ada ev";
    }

    @Override
    public List<String> getArgs() { return Arrays.asList(); }

    @Override
    public void perform(Player player, String[] args) {
        if(Skyrama.getIslandManager().getPlayerIsland(player) != null) {
            NotificationManager.sendTeleportNotification(player, "Kendi adanız");
            
            Skyrama.getIslandManager().getPlayerIsland(player).getSpawn().setWorld(Bukkit.getWorld((String) Skyrama.getPlugin(Skyrama.class).getConfig().get("general.world")));
            player.teleport(Skyrama.getIslandManager().getPlayerIsland(player).getSpawn());
            
            NotificationManager.sendSuccessNotification(player, "Ada evinize hoş geldiniz!");
        } else {
            player.sendMessage(Skyrama.getLocaleManager().getString("player-no-island"));
            NotificationManager.sendErrorNotification(player, "Adanız yok!");
        }
    }
}