package dev.grafjojo.gigacraftauth.command;

import dev.grafjojo.gigacraftauth.GigaAuth;
import dev.grafjojo.gigacraftauth.maintenance.MaintenanceManager;
import dev.grafjojo.gigacraftauth.utils.Settings;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

@Command(value = "maintenance", alias = {"wartung"})
@Permission("gigacraft.auth.maintenance")
public class MaintenanceCommand extends BaseCommand {

    private final MaintenanceManager MAINTENANCE = GigaAuth.get().getMaintenanceManager();

    @Default
    public void execute(CommandSender sender) {
        if (MAINTENANCE.isMaintenanceActive()) {
            MAINTENANCE.setMaintenance(false);
            sender.sendMessage(Settings.PREFIX.append(Component.text("Wartungsarbeiten wurde deaktiviert.",
                    Style.style(NamedTextColor.RED).decoration(TextDecoration.BOLD, false))
            ));
        } else {
            MAINTENANCE.setMaintenance(true);
            sender.sendMessage(Settings.PREFIX.append(Component.text("Wartungsarbeiten wurde aktiviert.",
                    Style.style(NamedTextColor.GRAY).decoration(TextDecoration.BOLD, false))
            ));

            Bukkit.getOnlinePlayers().forEach(player -> {
                if (!player.hasPermission("gigacraft.auth.maintenance.bypass")) {
                    player.kick(Component.text("Der Server befindet sich in Wartungsarbeiten.",
                            NamedTextColor.RED));
                }
            });
        }
    }

}
