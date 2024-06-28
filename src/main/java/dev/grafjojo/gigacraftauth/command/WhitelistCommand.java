package dev.grafjojo.gigacraftauth.command;

import dev.grafjojo.gigacraftauth.GigaAuth;
import dev.grafjojo.gigacraftauth.utils.Settings;
import dev.grafjojo.gigacraftauth.whitelist.WhitelistManager;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.UUID;

@Command(value = "whitelist")
@Permission("giga.auth.whitelist")
public class WhitelistCommand extends BaseCommand {

    private final WhitelistManager WHITELIST = GigaAuth.get().getWhitelistManager();

    @SubCommand("add")
    public void executeAdd(CommandSender sender, String uuid) {
        try {
            UUID uuidObj = UUID.fromString(uuid);
            WHITELIST.isWhitelisted(uuidObj).thenAccept(isWhitelisted -> {
                if (isWhitelisted) {
                    sender.sendMessage(Settings.PREFIX.append(Component.text(uuid, NamedTextColor.BLUE))
                            .append(Component.text(" ist bereits auf der Whitelist.", NamedTextColor.RED))
                            .decoration(TextDecoration.BOLD, false));
                } else {
                    WHITELIST.whitelist(uuidObj).thenRun(() ->
                            sender.sendMessage(Settings.PREFIX.append(Component.text(uuid, NamedTextColor.BLUE))
                                    .append(Component.text(" wurde zur Whitelist hinzugefügt.", NamedTextColor.GRAY))
                                    .decoration(TextDecoration.BOLD, false)));
                }
            });
        } catch (IllegalArgumentException e) {
            sender.sendMessage(Settings.PREFIX
                    .append(Component.text("UUID ist im falschen format.", Style.style(NamedTextColor.RED)
                            .decoration(TextDecoration.BOLD, false))));
        }
    }

    @SubCommand("remove")
    public void executeRemove(CommandSender sender, String uuid) {
        try {
            UUID uuidObj = UUID.fromString(uuid);
            WHITELIST.isWhitelisted(uuidObj).thenAccept(isWhitelisted -> {
                if (isWhitelisted) {
                    WHITELIST.unWhitelist(uuidObj).thenRun(() ->
                            sender.sendMessage(Settings.PREFIX.append(Component.text(String.valueOf(uuidObj), NamedTextColor.BLUE))
                                    .append(Component.text(" wurde von der Whitelist entfernt.", NamedTextColor.GRAY))
                                    .decoration(TextDecoration.BOLD, false)));
                } else {
                    sender.sendMessage(Settings.PREFIX.append(Component.text(String.valueOf(uuidObj), NamedTextColor.BLUE))
                            .append(Component.text(" ist nicht auf der Whitelist.", NamedTextColor.RED))
                            .decoration(TextDecoration.BOLD, false));
                }
            });
        } catch (IllegalArgumentException e) {
            sender.sendMessage(Settings.PREFIX
                    .append(Component.text("UUID ist im falschen format.", Style.style(NamedTextColor.RED)
                            .decoration(TextDecoration.BOLD, false))));
        }
    }

    @SubCommand("cache")
    public void executeCache(CommandSender sender) {
        sender.sendMessage(Settings.PREFIX.append(Component.text("Cached UUIDs:", Style.style(NamedTextColor.GRAY)
                .decoration(TextDecoration.BOLD, false))));

        WHITELIST.getCache().forEach(uuid -> {

            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

            Component coloredName = Component.text(player.getName() != null ? player.getName() : "Unbekannt",
                    player.getName() != null ? NamedTextColor.AQUA : NamedTextColor.RED);

            HoverEvent<Component> hoverEvent = HoverEvent.showText(Component.text("In Zwischenablage speichern", NamedTextColor.GRAY));
            ClickEvent clickEvent = ClickEvent.copyToClipboard(uuid.toString());

            sender.sendMessage(Component.text(" » ", NamedTextColor.DARK_GRAY)
                    .append(Component.text(uuid.toString(), NamedTextColor.BLUE))

                            .append(Component.text(" (", NamedTextColor.DARK_GRAY))
                            .append(coloredName)
                            .append(Component.text(")", NamedTextColor.DARK_GRAY))

                    .clickEvent(clickEvent).hoverEvent(hoverEvent)
            );
        });

    }
}