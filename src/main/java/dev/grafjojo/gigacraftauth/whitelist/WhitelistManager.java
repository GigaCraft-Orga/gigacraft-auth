package dev.grafjojo.gigacraftauth.whitelist;

import dev.grafjojo.gigacraftcore.database.DatabaseRegistry;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class WhitelistManager {

    private final WhitelistStorage WHITELIST_STORAGE;
    private final Set<UUID> WHITELIST_CACHE;

    public WhitelistManager() {
        WHITELIST_STORAGE = DatabaseRegistry.getStorage(WhitelistStorage.class);
        WHITELIST_CACHE = new HashSet<>();
    }

    public CompletableFuture<Boolean> isWhitelisted(UUID uuid) {
        if (WHITELIST_CACHE.contains(uuid)) {
            return CompletableFuture.completedFuture(true);
        }

        return WHITELIST_STORAGE.isWhitelistedAsync(uuid).thenApplyAsync(isWhitelisted -> {
            if (isWhitelisted) {
                synchronized (WHITELIST_CACHE) {
                    WHITELIST_CACHE.add(uuid);
                }
            }
            return isWhitelisted;
        });
    }

    public CompletableFuture<Void> whitelist(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            WHITELIST_STORAGE.whitelist(uuid);
            synchronized (WHITELIST_CACHE) {
                WHITELIST_CACHE.add(uuid);
            }
            return null;
        });
    }

    public CompletableFuture<Void> unWhitelist(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            WHITELIST_STORAGE.removeWhitelist(uuid);
            synchronized (WHITELIST_CACHE) {
                WHITELIST_CACHE.remove(uuid);
            }
            return null;
        });
    }

    public Set<UUID> getCache() {
        return WHITELIST_CACHE;}
}
