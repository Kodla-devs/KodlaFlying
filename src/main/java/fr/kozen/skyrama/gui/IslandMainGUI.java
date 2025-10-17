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
import java.util.List;

public class IslandMainGUI implements Listener {

    public static void openMainGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, "§6§lAda Yönetimi");
        
        Island playerIsland = Skyrama.getIslandManager().getPlayerIsland(player);
        
        // Ada Oluştur / Ada Evi
        if (playerIsland == null) {
            gui.setItem(11, createItem(Material.GRASS_BLOCK, "§a§lAda Oluştur", 
                Arrays.asList("§7Yeni bir ada oluşturun", "§7ve maceraya başlayın!", "", "§e▸ Tıklayın!")));
        } else {
            gui.setItem(11, createItem(Material.RED_BED, "§a§lAda Evi", 
                Arrays.asList("§7Adanıza ışınlanın", "", "§e▸ Tıklayın!")));
        }
        
        // Ada Ayarları
        if (playerIsland != null) {
            gui.setItem(13, createItem(Material.REDSTONE, "§6§lAda Ayarları", 
                Arrays.asList("§7Ada ayarlarınızı düzenleyin", "§7• Doğum noktası ayarla", "§7• Üye yönetimi", "", "§e▸ Tıklayın!")));
        }
        
        // Ada Ziyareti
        gui.setItem(15, createItem(Material.ENDER_PEARL, "§b§lAda Ziyareti", 
            Arrays.asList("§7Başka oyuncuların", "§7adalarını ziyaret edin", "", "§e▸ Tıklayın!")));
        
        // Ada Bilgileri
        if (playerIsland != null) {
            gui.setItem(20, createItem(Material.BOOK, "§e§lAda Bilgileri", 
                Arrays.asList("§7Ada seviyenizi ve", "§7istatistiklerinizi görün", "", "§e▸ Tıklayın!")));
        }
        
        // Davet Sistemi
        if (playerIsland != null) {
            gui.setItem(22, createItem(Material.PAPER, "§d§lDavet Sistemi", 
                Arrays.asList("§7Arkadaşlarınızı", "§7adanıza davet edin", "", "§e▸ Tıklayın!")));
        }
        
        // Ada Terk Et
        if (playerIsland != null) {
            gui.setItem(24, createItem(Material.BARRIER, "§c§lAda Terk Et", 
                Arrays.asList("§7Mevcut adanızı terk edin", "§c§lDikkat: Bu işlem geri alınamaz!", "", "§e▸ Tıklayın!")));
        }
        
        // Dekoratif itemler
        fillEmptySlots(gui);
        
        player.openInventory(gui);
        NotificationManager.playNotificationSound(player);
    }
    
    private static ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    
    private static void fillEmptySlots(Inventory gui) {
        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
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
        if (!event.getView().getTitle().equals("§6§lAda Yönetimi")) return;
        
        event.setCancelled(true);
        
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        
        if (clickedItem == null || clickedItem.getType() == Material.GRAY_STAINED_GLASS_PANE) return;
        
        String itemName = clickedItem.getItemMeta().getDisplayName();
        
        switch (itemName) {
            case "§a§lAda Oluştur":
                player.closeInventory();
                player.performCommand("ada olustur");
                break;
                
            case "§a§lAda Evi":
                player.closeInventory();
                player.performCommand("ada ev");
                break;
                
            case "§6§lAda Ayarları":
                player.closeInventory();
                IslandSettingsGUI.openSettingsGUI(player);
                break;
                
            case "§b§lAda Ziyareti":
                player.closeInventory();
                IslandVisitGUI.openVisitGUI(player);
                break;
                
            case "§e§lAda Bilgileri":
                player.closeInventory();
                IslandInfoGUI.openInfoGUI(player);
                break;
                
            case "§d§lDavet Sistemi":
                player.closeInventory();
                IslandInviteGUI.openInviteGUI(player);
                break;
                
            case "§c§lAda Terk Et":
                player.closeInventory();
                player.performCommand("ada ayril");
                break;
        }
    }
}
