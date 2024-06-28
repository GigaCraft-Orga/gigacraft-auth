package dev.grafjojo.gigacraftauth.registration;

import dev.grafjojo.gigacraftcore.database.DatabaseRegistry;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class RegistrationManager {

    private final RegistrationStorage REGISTRATION_STORAGE = DatabaseRegistry.getStorage(RegistrationStorage.class);

    public CompletableFuture<Boolean> isRegistrationDone(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> REGISTRATION_STORAGE.isProcessed(uuid));
    }
}
