package fr.kozen.skyrama.commands.subcommands;

import fr.kozen.skyrama.gui.IslandMainGUI;
import fr.kozen.skyrama.interfaces.ISubCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class MenuCommand implements ISubCommand {

    @Override
    public String getName() {
        return "menu";
    }

    @Override
    public String getDescription() {
        return "Ada yönetim menüsünü aç";
    }

    @Override
    public String getPermission() { 
        return "skyrama.command.menu"; 
    }

    @Override
    public String getSyntax() {
        return "/ada menu";
    }

    @Override
    public List<String> getArgs() { 
        return Arrays.asList(); 
    }

    @Override
    public void perform(Player player, String[] args) {
        IslandMainGUI.openMainGUI(player);
    }
}
