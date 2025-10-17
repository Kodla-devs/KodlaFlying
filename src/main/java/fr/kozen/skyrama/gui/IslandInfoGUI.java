package fr.kozen.skyrama.gui;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.objects.islands.Island;
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

import java.util.Arrays;

public class IslandInfoGUI implements Listener {

    public static void openInfoGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, "§e§lAda Bilgileri");
        
        Island island = Skyrama.getIslandManager().getPlayerIsland(player);
        if (island == null) {
            player.closeInventory();
            NotificationManager.sendErrorNotification(player, "Adanız bulunamadı!");
            return;
        }
        
        // Ada sahibi bilgisi
        gui.setItem(10, createItem(Material.GOLDEN_HELMET, "§6§lAda Sahibi", 
            Arrays.asList("§7Sahip: §e" + island.getOwner().getName(), 
                         "§7Durum: " + (island.getOwner().isOnline() ? "§aÇevrimiçi" : "§cÇevrimdışı"))));
        
        // Üye sayısı
        gui.setItem(12, createItem(Material.PLAYER_HEAD, "§b§lÜye Sayısı", 
            Arrays.asList("§7Toplam üye: §e" + island.getPlayers().size(),
                         "§7Maksimum: §e10", // Sabit değer, config'den alınabilir
                         "",
                         "§7Üye listesini görmek için",
                         "§7ayarlar menüsünü kullanın")));
        
        // Ada konumu
        gui.setItem(14, createItem(Material.COMPASS, "§a§lAda Konumu", 
            Arrays.asList("§7X: §e" + (int) island.getSpawn().getX(),
                         "§7Y: §e" + (int) island.getSpawn().getY(),
                         "§7Z: §e" + (int) island.getSpawn().getZ(),
                         "§7Dünya: §e" + island.getSpawn().getWorld().getName())));
        
        // Ada seviyesi (placeholder)
        gui.setItem(16, createItem(Material.EXPERIENCE_BOTTLE, "§d§lAda Seviyesi", 
            Arrays.asList("§7Seviye: §e1", // Placeholder
                         "§7EXP: §e0/100",
                         "",
                         "§7Seviye sistemi yakında",
                         "§7eklenecek!")));
        
        // Geri dön
        gui.setItem(22, createItem(Material.ARROW, "§c§lGeri Dön", 
            Arrays.asList("§7Ana menüye dön")));
        
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
        ItemStack glass = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);
        
        for (int i = 0; i < gui.getSize(); i++) {
            if (gui.getItem(i) == null) {
                gui.setItem(i, glass);
            }
        }
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("§e§lAda Bilgileri")) return;
        
        event.setCancelled(true);
        
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        
        if (clickedItem == null || clickedItem.getType() == Material.YELLOW_STAINED_GLASS_PANE) return;
        
        String itemName = clickedItem.getItemMeta().getDisplayName();
        
        if (itemName.equals("§c§lGeri Dön")) {
            player.closeInventory();
            IslandMainGUI.openMainGUI(player);
        }
    }
}
