package com.chewy.translator.listener;

import com.chewy.translator.plugin.MinecraftTranslator;
import com.chewy.translator.translation.TranslationService;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    private final MinecraftTranslator plugin;
    private final TranslationService translationService;

    public ChatListener(MinecraftTranslator plugin) {
        this.plugin = plugin;
        this.translationService = plugin.getTranslationService();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player sender = event.getPlayer();
        String originalMessage = event.getMessage();
        String senderLang = getPlayerLanguage(sender);
        
        // Remove original message from broadcast
        event.setCancelled(true);
        
        // Get list of listeners (players who should receive the message)
        for (Player listener : event.getRecipients()) {
            if (listener.equals(sender)) {
                // Sender sees their original message
                listener.sendMessage(getFormattedMessage(sender, originalMessage, senderLang, senderLang));
                continue;
            }
            
            // Check if translation is enabled for this listener
            if (isTranslationDisabled(listener)) {
                // Listener has translation disabled, show original
                listener.sendMessage(getFormattedMessage(sender, originalMessage, senderLang, senderLang));
                continue;
            }
            
            // Check if listener has bypass enabled
            if (hasBypassEnabled(listener)) {
                // Bypass sees original message
                listener.sendMessage(getFormattedMessage(sender, originalMessage, senderLang, senderLang));
                continue;
            }
            
            // Get listener's preferred language
            String listenerLang = getPlayerLanguage(listener);
            
            // Translate the message
            String translated = translationService.translate(
                originalMessage, senderLang, listenerLang
            );
            
            // Send translated message
            listener.sendMessage(getFormattedMessage(sender, originalMessage, senderLang, listenerLang, translated));
        }
    }

    /**
     * Get the language of a player
     */
    private String getPlayerLanguage(Player player) {
        if (player.hasMetadata("translator_language")) {
            return player.getMetadata("translator_language").get(0).asString();
        }
        // Default to server default language
        return plugin.getConfigManager().getDefaultLanguage();
    }

    /**
     * Check if translation is disabled for a player
     */
    private boolean isTranslationDisabled(Player player) {
        return player.hasMetadata("translator_translate_enabled") &&
               !player.getMetadata("translator_translate_enabled").get(0).asBoolean();
    }

    /**
     * Check if bypass is enabled for a player
     */
    private boolean hasBypassEnabled(Player player) {
        return player.hasMetadata("translator_bypass_enabled") &&
               player.getMetadata("translator_bypass_enabled").get(0).asBoolean();
    }

    /**
     * Format message for same-language display
     */
    private String getFormattedMessage(Player sender, String message, 
                                       String sourceLang, String targetLang) {
        return ChatColor.WHITE + "[" + sender.getDisplayName() + "] " + message;
    }

    /**
     * Format translated message with language indicators
     */
    private String getFormattedMessage(Player sender, String original, 
                                       String sourceLang, String targetLang,
                                       String translated) {
        StringBuilder sb = new StringBuilder();
        
        if (sourceLang.equalsIgnoreCase(targetLang)) {
            sb.append(ChatColor.WHITE).append("[").append(sender.getDisplayName()).append("] ").append(translated);
        } else {
            String sourceFlag = plugin.getConfigManager().getLanguageFlag(sourceLang);
            sb.append(ChatColor.GRAY).append("[").append(sourceFlag).append("] ");
            sb.append(ChatColor.WHITE).append("[").append(sender.getDisplayName()).append("] ");
            sb.append(translated);
        }
        
        return sb.toString();
    }
}
