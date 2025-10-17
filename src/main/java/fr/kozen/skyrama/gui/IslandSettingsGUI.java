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

import java.util.Arrays;

public class IslandSettingsGUI implements Listener {

    public static void openSettingsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, "§6§lAda Ayarları");
        
        // Doğum noktası ayarla
        gui.setItem(11, createItem(Material.RED_BED, "§a§lDoğum Noktası Ayarla", 
            Arrays.asList("§7Bulunduğunuz konumu", "§7ada doğum noktası yapın", "", "§e▸ Tıklayın!")));
        
        // Üye yönetimi
        gui.setItem(13, createItem(Material.PLAYER_HEAD, "§b§lÜye Yönetimi", 
            Arrays.asList("§7Ada üyelerinizi yönetin", "§7• Üyeleri görüntüle", "§7• Yetki ver/al", "", "§e▸ Tıklayın!")));
        
        // Ada koruması
        gui.setItem(15, createItem(Material.SHIELD, "§c§lAda Koruması", 
            Arrays.asList("§7Ada koruma ayarlarınız", "§7• PvP ayarları", "§7• Blok koruma", "", "§e▸ Tıklayın!")));
        
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
        ItemStack glass = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
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
        if (!event.getView().getTitle().equals("§6§lAda Ayarları")) return;
        
        event.setCancelled(true);
        
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        
        if (clickedItem == null || clickedItem.getType() == Material.ORANGE_STAINED_GLASS_PANE) return;
        
        String itemName = clickedItem.getItemMeta().getDisplayName();
        
        switch (itemName) {
            case "§a§lDoğum Noktası Ayarla":
                player.closeInventory();
                player.performCommand("ada dogumnoktasi");
                break;
                
            case "§b§lÜye Yönetimi":
                player.closeInventory();
                NotificationManager.sendActionBar(player, "§e Bu özellik yakında eklenecek!");
                break;
                
            case "§c§lAda Koruması":
                player.closeInventory();
                NotificationManager.sendActionBar(player, "§e Bu özellik yakında eklenecek!");
                break;
                
            case "§c§lGeri Dön":
                player.closeInventory();
                IslandMainGUI.openMainGUI(player);
                break;
        }
    }
}
