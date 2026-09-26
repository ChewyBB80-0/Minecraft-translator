package com.chewy.translator.command;

import com.chewy.translator.plugin.MinecraftTranslator;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TranslateCommand implements CommandExecutor {
    private final MinecraftTranslator plugin;

    public TranslateCommand(MinecraftTranslator plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /translate <on|off|bypass>");
            player.sendMessage(ChatColor.GOLD + "on - Enable translation (default)");
            player.sendMessage(ChatColor.GOLD + "off - Disable translation");
            player.sendMessage(ChatColor.GOLD + "bypass - See original messages (staff only)");
            return true;
        }

        String action = args[0].toLowerCase();

        switch (action) {
            case "on":
                player.setMetadata("translator_translate_enabled", 
                    org.bukkit.metadata.FixedMetadataValue(plugin, true));
                player.sendMessage(ChatColor.GREEN + "Translation enabled.");
                break;

            case "off":
                player.setMetadata("translator_translate_enabled", 
                    org.bukkit.metadata.FixedMetadataValue(plugin, false));
                player.sendMessage(ChatColor.RED + "Translation disabled.");
                break;

            case "bypass":
                if (player.hasPermission("translator.bypass")) {
                    player.setMetadata("translator_bypass_enabled", 
                        org.bukkit.metadata.FixedMetadataValue(plugin, true));
                    player.sendMessage(ChatColor.YELLOW + "Bypass mode enabled. You will see original messages.");
                } else {
                    player.sendMessage(ChatColor.RED + "You do not have permission to bypass translation.");
                }
                break;

            default:
                player.sendMessage(ChatColor.RED + "Unknown action. Use /translate for help.");
        }

        return true;
    }
}
