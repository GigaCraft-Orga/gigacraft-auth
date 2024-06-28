package dev.grafjojo.gigacraftauth.listener;

import dev.grafjojo.gigacraftauth.GigaAuth;
import dev.grafjojo.gigacraftauth.maintenance.MaintenanceManager;
import dev.grafjojo.gigacraftauth.registration.RegistrationManager;
import dev.grafjojo.gigacraftauth.utils.Settings;
import dev.grafjojo.gigacraftauth.whitelist.WhitelistManager;
import dev.grafjojo.gigacraftcore.color.Coloration;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class LoginListener implements Listener {

    private final WhitelistManager WHITELIST = GigaAuth.get().getWhitelistManager();
    private final MaintenanceManager MAINTENANCE = GigaAuth.get().getMaintenanceManager();
    private final RegistrationManager REGISTRATION = GigaAuth.get().getRegistrationManager();

    @EventHandler(priority = EventPriority.MONITOR)
    public void handleLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();


        if (MAINTENANCE.isMaintenanceActive() && !player.hasPermission("gigacraft.auth.maintenance.bypass")) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER,
                    maintenanceComponent(MAINTENANCE.getMaintenanceReason()));
            return;
        }

        if (WHITELIST.isWhitelisted(player.getUniqueId()).join()) return;

        if (REGISTRATION.isRegistrationDone(player.getUniqueId()).join()) {
            WHITELIST.whitelist(player.getUniqueId()).join();
            return;
        }

        event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST,
                whitelistComponent(Component.text("Du bist nicht auf der Whitelist!", NamedTextColor.RED)));
    }

    private Component whitelistComponent(Component content) {
        return Settings.RAW_PREFIX
                .appendNewline()
                .append(content);
    }

    private Component maintenanceComponent(String reason) {
        return Settings.RAW_PREFIX
                .append(Component.text(" » ", NamedTextColor.DARK_GRAY))
                .append(Component.text("Wartungsarbeiten", NamedTextColor.RED))
                .appendNewline()
                .append(Coloration.translate(reason));
    }
}
