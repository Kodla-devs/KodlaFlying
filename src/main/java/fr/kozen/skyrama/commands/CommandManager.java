package fr.kozen.skyrama.commands;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.commands.subcommands.*;
import fr.kozen.skyrama.gui.IslandMainGUI;
import fr.kozen.skyrama.interfaces.ISubCommand;
import org.antlr.v4.runtime.misc.NotNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CommandManager implements CommandExecutor {

    private final ArrayList<ISubCommand> subcommands = new ArrayList<>();

    public CommandManager(){

        subcommands.add(new CreateCommand());
        subcommands.add(new HomeCommand());
        subcommands.add(new SetSpawnCommand());
        subcommands.add(new InviteCommand());
        subcommands.add(new LeaveCommand());
        subcommands.add(new GoToCommand());
        subcommands.add(new MenuCommand());

        this.initTabCompleter();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0){
            for (int i = 0; i < getSubcommands().size(); i++){
                ISubCommand subCmd = getSubcommands().get(i);
                boolean matches = args[0].equalsIgnoreCase(subCmd.getName());
                
                // GoToCommand için özel alias kontrolü
                if (subCmd instanceof GoToCommand) {
                    matches = ((GoToCommand) subCmd).isAlias(args[0]);
                }
                
                if (matches){
                    if (sender.hasPermission(subCmd.getPermission())) {
                        subCmd.perform((Player) sender, args);
                    } else {
                        sender.sendMessage(Skyrama.getLocaleManager().getString("player-noperm"));
                    }
                    break;
                }
            }
        }else if(args.length == 0){
            // GUI'yi aç
            if (sender instanceof Player) {
                IslandMainGUI.openMainGUI((Player) sender);
            } else {
                sender.sendMessage("--------------------------------");
                for (int i = 0; i < getSubcommands().size(); i++){
                    sender.sendMessage(getSubcommands().get(i).getSyntax() + " - " + getSubcommands().get(i).getDescription());
                }
                sender.sendMessage("--------------------------------");
            }
        }
        return true;
    }

    public void initTabCompleter() {
        Skyrama.getPlugin(Skyrama.class).getCommand("ada").setTabCompleter(new TabCompleter() {
            @Override
            public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
                if (command.getName().equalsIgnoreCase("ada")) {
                    List<String> l = new ArrayList<>();
                    if(args.length == 1) {
                        getSubcommands().forEach(subCommand -> {
                            l.add(subCommand.getName());
                            // GoToCommand için alias ekle
                            if (subCommand instanceof GoToCommand) {
                                l.add("ziyaret");
                            }
                        });
                    } else if (args.length == 2) {
                        ISubCommand subCommand = getSubcommands().stream().filter(subCommandFilter -> subCommandFilter.getName().equals(args[0])).findAny().orElse(null);
                        if(subCommand != null) {
                            subCommand.getArgs().forEach(arg -> {
                                l.add(arg);
                            });
                        }
                    }
                    return l;
                }
                return null;
            }
        });
    }

    public ArrayList<ISubCommand> getSubcommands(){
        return subcommands;
    }

}
