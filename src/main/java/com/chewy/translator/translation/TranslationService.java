package com.chewy.translator.translation;

import com.chewy.translator.config.ConfigManager;
import com.chewy.translator.cache.TranslationCache;
import org.bukkit.ChatColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TranslationService {
    private static final Logger logger = LoggerFactory.getLogger(TranslationService.class);
    
    private final ConfigManager configManager;
    private final TranslationCache cache;
    private boolean cacheSchedulerRunning = false;
    
    // Track active translation requests for rate limiting
    private final Map<String, Long> requestTimestamps = new ConcurrentHashMap<>();
    
    public TranslationService(ConfigManager configManager) {
        this.configManager = configManager;
        this.cache = new TranslationCache(configManager.getCacheExpiryHours());
    }

    /**
     * Translate a message from source language to target language
     */
    public String translate(String text, String sourceLang, String targetLang) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        // If same language, return as-is
        if (sourceLang.equalsIgnoreCase(targetLang)) {
            return text;
        }
        
        // Check cache first
        String cacheKey = sourceLang + "->" + targetLang + ":" + text.hashCode();
        CachedTranslation cached = cache.get(cacheKey);
        if (cached != null) {
            logger.debug("Cache hit for {} -> {}", sourceLang, targetLang);
            return cached.getTranslation();
        }
        
        // Rate limit check
        if (!checkRateLimit()) {
            logger.warn("Rate limit exceeded, returning original text");
            return text;
        }
        
        // Perform translation
        String translated = performTranslation(text, sourceLang, targetLang);
        
        // Cache the result
        if (translated != null && !translated.isEmpty()) {
            cache.put(cacheKey, new CachedTranslation(sourceLang, targetLang, text, translated));
        }
        
        return translated != null ? translated : text;
    }

    /**
     * Detect the language of a message
     */
    public String detectLanguage(String text) {
        // Simple language detection based on common patterns
        // In production, you'd use a proper language detection library
        
        // For now, default to English if we can't detect
        return "en";
    }

    /**
     * Get a preview of what the translation will look like (for UI purposes)
     */
    public String getTranslationPreview(String text, String sourceLang, String targetLang) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        // Quick check without full translation
        if (sourceLang.equalsIgnoreCase(targetLang)) {
            return text;
        }
        
        // Return a placeholder with language info
        String sourceName = configManager.getLanguageName(sourceLang);
        String targetName = configManager.getLanguageName(targetLang);
        
        return "[Translated from " + sourceName + "] " + text;
    }

    /**
     * Format the translated message for display
     */
    public String formatTranslatedMessage(String original, String translated, 
                                          String sender, String sourceLang, 
                                          String targetLang, boolean showOriginal) {
        StringBuilder sb = new StringBuilder();
        
        // Add original language indicator if configured
        if (showOriginal && !sourceLang.equalsIgnoreCase(targetLang)) {
            String sourceFlag = configManager.getLanguageFlag(sourceLang);
            String targetFlag = configManager.getLanguageFlag(targetLang);
            
            sb.append(ChatColor.GRAY).append("[").append(sourceFlag).append("] ");
            sb.append(sender).append(": ");
            
            // The translated text with original as hover (done via ComponentBuilder)
            sb.append(ChatColor.WHITE).append(translated);
            
            return sb.toString();
        }
        
        return sender + ": " + translated;
    }

    /**
     * Clear the translation cache
     */
    public void clearCache() {
        cache.clear();
        logger.info("Translation cache cleared");
    }

    /**
     * Get cache statistics
     */
    public String getCacheStats() {
        return String.format("Cache size: %d entries, Hit rate: %.1f%%",
                cache.size(), cache.getHitRate() * 100);
    }

    /**
     * Start the cache cleanup scheduler
     */
    public void startCacheCleanup() {
        if (cacheSchedulerRunning) {
            return;
        }
        cacheSchedulerRunning = true;
        
        Bukkit.getScheduler().runTaskTimer(MinecraftTranslator.getInstance(), () -> {
            cache.cleanup();
        }, 20 * 60 * 60, 20 * 60 * 60); // Run every hour
    }

    /**
     * Stop the cache cleanup scheduler
     */
    public void stopCacheCleanup() {
        cacheSchedulerRunning = false;
    }

    /**
     * Perform the actual translation using the configured provider
     */
    private String performTranslation(String text, String sourceLang, String targetLang) {
        try {
            // Use DeepTranslate (free, no API key needed)
            String url = String.format(
                "https://deeptranslate.net/api/translate?text=%s&source=%s&target=%s",
                java.net.URLEncoder.encode(text, "UTF-8"),
                sourceLang,
                targetLang
            );
            
            // Make HTTP request (in production, use a proper HTTP client)
            String response = makeHttpRequest(url);
            
            // Parse response (simplified - in production, parse JSON properly)
            if (response != null && !response.isEmpty()) {
                return response;
            }
        } catch (Exception e) {
            logger.error("Translation failed: {}", e.getMessage());
        }
        
        return null;
    }

    /**
     * Make an HTTP request (simplified - use proper HTTP client in production)
     */
    private String makeHttpRequest(String url) {
        // This is a placeholder - in production, use OkHttp or similar
        try {
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) 
                new java.net.URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            
            java.io.BufferedReader br = new java.io.BufferedReader(
                new java.io.InputStreamReader(conn.getInputStream()));
            
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
            
            return response.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Check if we're within rate limits
     */
    private boolean checkRateLimit() {
        long now = System.currentTimeMillis();
        
        // Simple sliding window rate limit: max 10 requests per second
        long windowStart = now - 1000;
        
        requestTimestamps.entrySet().removeIf(e -> e.getValue() < windowStart);
        
        if (requestTimestamps.size() >= 10) {
            return false;
        }
        
        requestTimestamps.put(String.valueOf(now), now);
        return true;
    }
}
