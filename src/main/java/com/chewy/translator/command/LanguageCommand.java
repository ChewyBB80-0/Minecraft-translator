package com.chewy.translator.command;

import com.chewy.translator.plugin.MinecraftTranslator;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LanguageCommand implements CommandExecutor {
    private final MinecraftTranslator plugin;

    public LanguageCommand(MinecraftTranslator plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        // Open language selection GUI
        // Note: In a full implementation, this would open a custom inventory
        // For now, show available languages
        player.sendMessage(ChatColor.GOLD + "=== Language Selection ===");
        player.sendMessage(ChatColor.WHITE + "Available languages:");
        
        for (String langCode : plugin.getConfigManager().getAvailableLanguages()) {
            String langName = plugin.getConfigManager().getLanguageName(langCode);
            String flag = plugin.getConfigManager().getLanguageFlag(langCode);
            player.sendMessage(ChatColor.AQUA + flag + " " + langName + " (" + langCode + ")");
        }

        player.sendMessage(ChatColor.GOLD + "Use /language <code> to set your language");
        player.sendMessage(ChatColor.GOLD + "Example: /language es");

        return true;
    }
}
