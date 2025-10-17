package fr.kozen.skyrama.commands.subcommands;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.interfaces.ISubCommand;
import fr.kozen.skyrama.storage.queries.IslandDao;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class SetSpawnCommand implements ISubCommand {

    @Override
    public String getName() {
        return "dogumnoktasi";
    }

    @Override
    public String getDescription() {
        return "Adanızın doğum noktasını bulunduğunuz konuma ayarlayın";
    }

    @Override
    public String getPermission() { return "skyrama.command.setspawn"; }

    @Override
    public String getSyntax() {
        return "/ada dogumnoktasi";
    }

    @Override
    public List<String> getArgs() { return Arrays.asList(); }

    @Override
    public void perform(Player player, String[] args) {
        if(Skyrama.getGridManager().isInPlayerIsland(player, player.getLocation()) != 2) {
            player.sendMessage(Skyrama.getLocaleManager().getString("setspawn-out-island"));
        } else {
            Skyrama.getIslandManager().getPlayerIsland(player).setSpawn(player.getLocation());
            IslandDao.save(Skyrama.getIslandManager().getPlayerIsland(player));
            player.sendMessage(Skyrama.getLocaleManager().getString("setspawn-success"));
        }
    }
}