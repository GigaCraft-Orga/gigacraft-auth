package dev.grafjojo.gigacraftauth.utils;

import dev.grafjojo.gigacraftcore.color.Coloration;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.*;

public class Settings {

    public static final Component RAW_PREFIX = Coloration.gradient("GigaAuth",
            TextColor.fromHexString("#19C30F"),
            TextColor.fromHexString("#9FFF00"))
            .decorate(TextDecoration.BOLD);

    public static final Component PREFIX =  RAW_PREFIX
            .append(Component.text(" » ", NamedTextColor.DARK_GRAY));

    public static final Component CONSOLE_PREFIX = Component.text("[", NamedTextColor.DARK_GRAY)
            .append(RAW_PREFIX.color(NamedTextColor.GREEN))
            .append(Component.text("] ", NamedTextColor.DARK_GRAY));
}