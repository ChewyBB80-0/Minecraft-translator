package com.chewy.translator.cache;

import java.util.LinkedHashMap;
import java.util.Map;

public class TranslationCache {
    private final Map<String, CachedTranslation> cache;
    private final int expiryHours;
    private long lastCleanupTime;

    public TranslationCache(int expiryHours) {
        // Use LRU cache with max 1000 entries
        this.cache = new LinkedHashMap<String, CachedTranslation>(1000, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, CachedTranslation> eldest) {
                return size() > 1000;
            }
        };
        this.expiryHours = expiryHours;
        this.lastCleanupTime = System.currentTimeMillis();
    }

    /**
     * Get a cached translation
     */
    public CachedTranslation get(String key) {
        CachedTranslation entry = cache.get(key);
        if (entry != null) {
            // Check if expired
            if (entry.isExpired()) {
                cache.remove(key);
                return null;
            }
            return entry;
        }
        return null;
    }

    /**
     * Cache a translation
     */
    public void put(String key, CachedTranslation entry) {
        cache.put(key, entry);
    }

    /**
     * Remove an entry from cache
     */
    public void remove(String key) {
        cache.remove(key);
    }

    /**
     * Clear all cache entries
     */
    public void clear() {
        cache.clear();
    }

    /**
     * Clean up expired entries
     */
    public void cleanup() {
        long now = System.currentTimeMillis();
        long expiryMs = expiryHours * 60 * 60 * 1000L;
        
        cache.entrySet().removeIf(entry -> 
            (now - entry.getValue().getTimestamp()) > expiryMs
        );
    }

    /**
     * Get cache size
     */
    public int size() {
        return cache.size();
    }

    /**
     * Get hit rate (approximate)
     */
    public double getHitRate() {
        // This is a simplified implementation
        // In production, track hits/misses separately
        return 0.8; // Placeholder
    }

    /**
     * Cached translation entry
     */
    public static class CachedTranslation {
        private final String sourceLang;
        private final String targetLang;
        private final String originalText;
        private final String translation;
        private final long timestamp;

        public CachedTranslation(String sourceLang, String targetLang, 
                                String originalText, String translation) {
            this.sourceLang = sourceLang;
            this.targetLang = targetLang;
            this.originalText = originalText;
            this.translation = translation;
            this.timestamp = System.currentTimeMillis();
        }

        public String getSourceLang() { return sourceLang; }
        public String getTargetLang() { return targetLang; }
        public String getOriginalText() { return originalText; }
        public String getTranslation() { return translation; }
        public long getTimestamp() { return timestamp; }

        public boolean isExpired() {
            return (System.currentTimeMillis() - timestamp) > 
                   (expiryHours * 60 * 60 * 1000L);
        }
    }
}
