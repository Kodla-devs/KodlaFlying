package fr.kozen.skyrama.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class NotificationManager {

    /**
     * Oyuncuya actionbar mesajı gönderir
     */
    public static void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, 
            net.md_5.bungee.api.chat.TextComponent.fromLegacyText(message));
    }

    /**
     * Oyuncuya title ve subtitle gönderir
     */
    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    /**
     * Oyuncuya basit title gönderir (varsayılan zamanlamalar ile)
     */
    public static void sendTitle(Player player, String title, String subtitle) {
        sendTitle(player, title, subtitle, 10, 70, 20);
    }

    /**
     * Oyuncuya ses efekti çalar
     */
    public static void playSound(Player player, Sound sound, float volume, float pitch) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    /**
     * Başarı sesi çalar
     */
    public static void playSuccessSound(Player player) {
        playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
    }

    /**
     * Hata sesi çalar
     */
    public static void playErrorSound(Player player) {
        playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
    }

    /**
     * Bildirim sesi çalar
     */
    public static void playNotificationSound(Player player) {
        playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.5f);
    }

    /**
     * Teleportasyon sesi çalar
     */
    public static void playTeleportSound(Player player) {
        playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
    }

    /**
     * Oyuncunun etrafında particle efekti oluşturur
     */
    public static void spawnParticles(Player player, Particle particle, int count, double offsetX, double offsetY, double offsetZ) {
        Location loc = player.getLocation();
        player.getWorld().spawnParticle(particle, loc, count, offsetX, offsetY, offsetZ);
    }

    /**
     * Başarı particle efekti
     */
    public static void spawnSuccessParticles(Player player) {
        spawnParticles(player, Particle.VILLAGER_HAPPY, 20, 1.0, 1.0, 1.0);
    }

    /**
     * Teleportasyon particle efekti
     */
    public static void spawnTeleportParticles(Player player) {
        spawnParticles(player, Particle.PORTAL, 30, 0.5, 1.0, 0.5);
    }

    /**
     * Hata particle efekti
     */
    public static void spawnErrorParticles(Player player) {
        spawnParticles(player, Particle.SMOKE_NORMAL, 10, 0.5, 0.5, 0.5);
    }

    /**
     * Kalp particle efekti (davet kabul edildiğinde)
     */
    public static void spawnHeartParticles(Player player) {
        spawnParticles(player, Particle.HEART, 15, 1.0, 1.0, 1.0);
    }

    /**
     * Komple başarı bildirimi (ses + particle + actionbar)
     */
    public static void sendSuccessNotification(Player player, String message) {
        sendActionBar(player, "§a✓ " + message);
        playSuccessSound(player);
        spawnSuccessParticles(player);
    }

    /**
     * Komple hata bildirimi (ses + particle + actionbar)
     */
    public static void sendErrorNotification(Player player, String message) {
        sendActionBar(player, "§c✗ " + message);
        playErrorSound(player);
        spawnErrorParticles(player);
    }

    /**
     * Komple teleportasyon bildirimi
     */
    public static void sendTeleportNotification(Player player, String destination) {
        sendTitle(player, "§a§lIşınlanıyor...", "§7" + destination + " adasına");
        sendActionBar(player, "§a→ " + destination + " adasına ışınlanıyor...");
        playTeleportSound(player);
        spawnTeleportParticles(player);
    }

    /**
     * Davet bildirimi
     */
    public static void sendInviteNotification(Player player, String inviterName) {
        sendTitle(player, "§6§lAda Daveti!", "§e" + inviterName + " §7sizi davet etti");
        playNotificationSound(player);
        spawnHeartParticles(player);
    }
}
