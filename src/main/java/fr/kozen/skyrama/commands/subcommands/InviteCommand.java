package fr.kozen.skyrama.commands.subcommands;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.interfaces.ISubCommand;
import fr.kozen.skyrama.objects.islands.Island;
import fr.kozen.skyrama.types.Rank;
import fr.kozen.skyrama.utils.NotificationManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class InviteCommand implements ISubCommand {

    @Override
    public String getName() {
        return "davet";
    }

    @Override
    public String getDescription() {
        return "Oyuncuyu adanıza davet et";
    }

    @Override
    public String getPermission() { return "skyrama.command.invite"; }

    @Override
    public String getSyntax() {
        return "/ada davet {ekle | kabul | reddet} <oyuncu>";
    }

    @Override
    public List<String> getArgs() { return Arrays.asList("ekle", "kabul", "reddet"); }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage(Skyrama.getLocaleManager().getString("invalid-syntax").replace("{0}", "/ada davet {ekle | kabul | reddet} <oyuncu>"));
            return;
        }

        Island island = Skyrama.getIslandManager().getPlayerIsland(player);
        Player target = Bukkit.getPlayer(args[2]);

        if(target == null) {
            player.sendMessage(Skyrama.getLocaleManager().getString("player-offline").replace("{0}", args[1]));
            return;
        }

        if (Objects.equals(args[1], "ekle")) {
            if(island != null) {
                if(island.getRank(player) == Rank.OWNER) {
                    if (Skyrama.getIslandManager().getPlayerIsland(target) != null && Skyrama.getIslandManager().getPlayerIsland(target) == island) {
                        player.sendMessage(Skyrama.getLocaleManager().getString("player-already-on-island").replace("{0}", target.getName()));
                        return;
                    }

                    if (island.getInvites() != null && island.getInvites().get(target) == null) {
                        NotificationManager.sendSuccessNotification(player, target.getName() + " adlı oyuncuya davet gönderildi!");
                        NotificationManager.sendInviteNotification(target, player.getName());
                        target.sendMessage(ChatColor.GREEN + " ");
                        target.sendMessage(ChatColor.GRAY + player.getName() + " sizi adasında oynamaya davet etti. Kabul ederseniz adanız silinecek.");
                        target.sendMessage(ChatColor.GREEN + " ");

                        TextComponent messageYes = new TextComponent(ChatColor.GREEN + "[KABUL ET]");
                        messageYes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ada davet kabul " + player.getName()));

                        TextComponent messageNo = new TextComponent(ChatColor.RED + "[REDDET] ");
                        messageNo.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ada davet reddet " + player.getName()));

                        messageYes.addExtra(" ");
                        messageYes.addExtra(messageNo);

                        target.spigot().sendMessage(messageYes);
                        target.sendMessage(ChatColor.GREEN + " ");

                        island.getInvites().put(target, player);
                    } else {
                        player.sendMessage(Skyrama.getLocaleManager().getString("player-already-invited").replace("{0}", island.getInvites().get(target).getName()).replace("{1}", target.getName()));
                    }
                } else {
                    player.sendMessage(Skyrama.getLocaleManager().getString("player-no-owner"));
                }
            } else {
                player.sendMessage(Skyrama.getLocaleManager().getString("player-no-island"));
            }
        } else if (Objects.equals(args[1], "kabul")) {
            Island newIsland = Skyrama.getIslandManager().getPlayerIsland(target);

            if(!newIsland.getInvites().isEmpty() && newIsland.getInvites().get(player) != null) {

                if(island != null) {
                    island.removePlayer(player);
                    newIsland.addPlayer(player, Rank.fromInt(1));
                }

                newIsland.addPlayer(player, Rank.fromInt(1));

                target.sendMessage(Skyrama.getLocaleManager().getString("player-join-island").replace("{0}", player.getName()));

                player.sendMessage(Skyrama.getLocaleManager().getString("player-join-island-success").replace("{0}", target.getName()));
                player.performCommand("ada ev");

            } else {
                player.sendMessage(Skyrama.getLocaleManager().getString("player-no-invited").replace("{0}", args[1]));
            }
        } else if (Objects.equals(args[1], "reddet")) {
            if(!island.getInvites().isEmpty() && island.getInvites().get(player) != null) {

                island.getInvites().remove(player);
                player.sendMessage(Skyrama.getLocaleManager().getString("player-decline-invitation").replace("{0}", target.getName()));
                target.sendMessage(Skyrama.getLocaleManager().getString("player-decline-your-invitation").replace("{0}", player.getName()));

            } else {
                player.sendMessage(Skyrama.getLocaleManager().getString("player-no-invited").replace("{0}", args[1]));
            }
        }
    }
}
