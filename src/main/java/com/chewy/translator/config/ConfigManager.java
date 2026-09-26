package com.chewy.translator.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    private final MinecraftTranslator plugin;
    private FileConfiguration config;
    private File configFile;

    public static final String DEFAULT_LANGUAGE = "en";
    public static final int CACHE_EXPIRY_HOURS = 48;

    private static final List<String> SUPPORTED_LANGUAGES = new ArrayList<>();

    static {
        SUPPORTED_LANGUAGES.add("en");
        SUPPORTED_LANGUAGES.add("es");
        SUPPORTED_LANGUAGES.add("fr");
        SUPPORTED_LANGUAGES.add("de");
        SUPPORTED_LANGUAGES.add("pt");
        SUPPORTED_LANGUAGES.add("ru");
        SUPPORTED_LANGUAGES.add("ja");
        SUPPORTED_LANGUAGES.add("ko");
        SUPPORTED_LANGUAGES.add("zh");
    }

    public ConfigManager(MinecraftTranslator plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveDefaultConfig();
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (Exception e) {
            plugin.getLogger().severe("Could not save config to " + configFile);
        }
    }

    public String getDefaultLanguage() {
        return config.getString("default-language", DEFAULT_LANGUAGE);
    }

    public boolean isShowOriginalHover() {
        return config.getBoolean("show-original-hover", true);
    }

    public boolean isCacheEnabled() {
        return config.getBoolean("cache-enabled", true);
    }

    public int getCacheExpiryHours() {
        return config.getInt("cache-expiry-hours", CACHE_EXPIRY_HOURS);
    }

    public List<String> getSupportedLanguages() {
        return config.getStringList("supported-languages");
    }

    public boolean isLanguageSupported(String lang) {
        return SUPPORTED_LANGUAGES.contains(lang.toLowerCase());
    }

    public List<String> getAvailableLanguages() {
        return new ArrayList<>(SUPPORTED_LANGUAGES);
    }

    public String getLanguageName(String code) {
        switch (code.toLowerCase()) {
            case "en": return "English";
            case "es": return "Spanish";
            case "fr": return "French";
            case "de": return "German";
            case "pt": return "Portuguese";
            case "ru": return "Russian";
            case "ja": return "Japanese";
            case "ko": return "Korean";
            case "zh": return "Chinese";
            default: return code;
        }
    }

    public String getLanguageFlag(String code) {
        switch (code.toLowerCase()) {
            case "en": return "🇺🇸";
            case "es": return "🇪🇸";
            case "fr": return "🇫🇷";
            case "de": return "🇩🇪";
            case "pt": return "🇧🇷";
            case "ru": return "🇷🇺";
            case "ja": return "🇯🇵";
            case "ko": return "🇰🇷";
            case "zh": return "🇨🇳";
            default: return "🏳️";
        }
    }
}
