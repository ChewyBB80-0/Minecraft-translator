package com.chewy.translator.plugin;

import com.chewy.translator.command.LanguageCommand;
import com.chewy.translator.command.TranslateCommand;
import com.chewy.translator.config.ConfigManager;
import com.chewy.translator.listener.ChatListener;
import com.chewy.translator.translation.TranslationService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MinecraftTranslator extends JavaPlugin {

    private static MinecraftTranslator instance;
    private ConfigManager configManager;
    private TranslationService translationService;

    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize configuration
        configManager = new ConfigManager(this);
        configManager.loadConfig();
        
        // Initialize translation service
        translationService = new TranslationService(configManager);
        translationService.startCacheCleanup();
        
        // Register events
        getServer().getPluginManager().registerEvents(new ChatListener(translationService), this);
        
        // Register commands
        getCommand("language").setExecutor(new LanguageCommand(this));
        getCommand("lang").setExecutor(new LanguageCommand(this));
        getCommand("translate").setExecutor(new TranslateCommand(this));
        
        getLogger().info("Minecraft Universal Translator v1.0 enabled!");
    }

    @Override
    public void onDisable() {
        if (translationService != null) {
            translationService.stopCacheCleanup();
        }
        getLogger().info("Minecraft Universal Translator v1.0 disabled!");
    }

    public static MinecraftTranslator getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public TranslationService getTranslationService() {
        return translationService;
    }
}
