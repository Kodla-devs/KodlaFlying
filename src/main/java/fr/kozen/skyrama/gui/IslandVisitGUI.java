package fr.kozen.skyrama.gui;

import fr.kozen.skyrama.Skyrama;
import fr.kozen.skyrama.objects.islands.Island;
import fr.kozen.skyrama.utils.NotificationManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IslandVisitGUI implements Listener {

    public static void openVisitGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§b§lAda Ziyareti");
        
        List<Island> allIslands = new ArrayList<>();
        // Tüm adaları al (IslandManager'dan)
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            Island island = Skyrama.getIslandManager().getPlayerIsland(offlinePlayer.getPlayer());
            if (island != null && !island.getOwner().equals(player)) {
                allIslands.add(island);
            }
        }
        
        int slot = 0;
        for (Island island : allIslands) {
            if (slot >= 45) break; // Maksimum 45 ada göster
            
            ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();
            skullMeta.setOwningPlayer(island.getOwner());
            skullMeta.setDisplayName("§a§l" + island.getOwner().getName() + "'nin Adası");
            
            List<String> lore = Arrays.asList(
                "§7Ada Sahibi: §e" + island.getOwner().getName(),
                "§7Üye Sayısı: §e" + island.getPlayers().size(),
                "§7Durum: §a" + (island.getOwner().isOnline() ? "Çevrimiçi" : "Çevrimdışı"),
                "",
                "§e▸ Ziyaret etmek için tıklayın!"
            );
            skullMeta.setLore(lore);
            playerHead.setItemMeta(skullMeta);
            
            gui.setItem(slot, playerHead);
            slot++;
        }
        
        // Geri dön butonu
        ItemStack backButton = new ItemStack(Material.ARROW);
        ItemMeta backMeta = backButton.getItemMeta();
        backMeta.setDisplayName("§c§lGeri Dön");
        backMeta.setLore(Arrays.asList("§7Ana menüye dön"));
        backButton.setItemMeta(backMeta);
        gui.setItem(49, backButton);
        
        // Boş slotları doldur
        fillEmptySlots(gui);
        
        player.openInventory(gui);
        NotificationManager.playNotificationSound(player);
    }
    
    private static void fillEmptySlots(Inventory gui) {
        ItemStack glass = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
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
        if (!event.getView().getTitle().equals("§b§lAda Ziyareti")) return;
        
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
                String ownerName = skullMeta.getOwningPlayer().getName();
                player.closeInventory();
                player.performCommand("ada git " + ownerName);
            }
        }
    }
}
