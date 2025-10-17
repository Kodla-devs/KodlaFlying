package fr.kozen.skyrama.gui;

import fr.kozen.skyrama.utils.NotificationManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.Collection;

public class IslandInviteGUI implements Listener {

    public static void openInviteGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§d§lDavet Sistemi");
        
        // Çevrimiçi oyuncuları listele
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        int slot = 0;
        
        for (Player onlinePlayer : onlinePlayers) {
            if (onlinePlayer.equals(player)) continue; // Kendisini dahil etme
            if (slot >= 45) break; // Maksimum 45 oyuncu göster
            
            ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();
            skullMeta.setOwningPlayer(onlinePlayer);
            skullMeta.setDisplayName("§a§l" + onlinePlayer.getName());
            skullMeta.setLore(Arrays.asList(
                "§7Oyuncu: §e" + onlinePlayer.getName(),
                "§7Durum: §aÇevrimiçi",
                "",
                "§e▸ Davet etmek için tıklayın!"
            ));
            playerHead.setItemMeta(skullMeta);
            
            gui.setItem(slot, playerHead);
            slot++;
        }
        
        // Bilgi paneli
        gui.setItem(45, createItem(Material.BOOK, "§6§lBilgi", 
            Arrays.asList("§7Çevrimiçi oyuncuları görüyorsunuz",
                         "§7Davet etmek istediğiniz oyuncuya",
                         "§7tıklayın")));
        
        // Geri dön butonu
        gui.setItem(49, createItem(Material.ARROW, "§c§lGeri Dön", 
            Arrays.asList("§7Ana menüye dön")));
        
        // Boş slotları doldur
        fillEmptySlots(gui);
        
        player.openInventory(gui);
        NotificationManager.playNotificationSound(player);
    }
    
    private static ItemStack createItem(Material material, String name, java.util.List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    
    private static void fillEmptySlots(Inventory gui) {
        ItemStack glass = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);
        
        for (int i = 45; i < 54; i++) {
            if (gui.getItem(i) == null) {
                gui.setItem(i, glass);
            }
        }
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("§d§lDavet Sistemi")) return;
        
        event.setCancelled(true);
        
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        
        if (clickedItem == null) return;
        
        if (clickedItem.getType() == Material.ARROW) {
            player.closeInventory();
            IslandMainGUI.openMainGUI(player);
            return;
        }
        
        if (clickedItem.getType() == Material.PLAYER_HEAD) {
            SkullMeta skullMeta = (SkullMeta) clickedItem.getItemMeta();
            if (skullMeta.getOwningPlayer() != null) {
                String targetName = skullMeta.getOwningPlayer().getName();
                player.closeInventory();
                player.performCommand("ada davet ekle " + targetName);
            }
        }
    }
}
