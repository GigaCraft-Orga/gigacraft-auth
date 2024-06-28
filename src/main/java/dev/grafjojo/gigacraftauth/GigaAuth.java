package dev.grafjojo.gigacraftauth;

import dev.grafjojo.gigacraftauth.command.MaintenanceCommand;
import dev.grafjojo.gigacraftauth.command.WhitelistCommand;
import dev.grafjojo.gigacraftauth.configs.CredentialsConfig;
import dev.grafjojo.gigacraftauth.listener.LoginListener;
import dev.grafjojo.gigacraftauth.maintenance.MaintenanceConfig;
import dev.grafjojo.gigacraftauth.maintenance.MaintenanceManager;
import dev.grafjojo.gigacraftauth.registration.RegistrationManager;
import dev.grafjojo.gigacraftauth.registration.RegistrationStorage;
import dev.grafjojo.gigacraftauth.utils.Settings;
import dev.grafjojo.gigacraftauth.whitelist.WhitelistManager;
import dev.grafjojo.gigacraftauth.whitelist.WhitelistStorage;
import dev.grafjojo.gigacraftcore.concurrency.ThreadExecutor;
import dev.grafjojo.gigacraftcore.config.ConfigFactory;
import dev.grafjojo.gigacraftcore.database.DatabaseRegistry;
import dev.grafjojo.gigacraftcore.database.postgres.PostgresDatabase;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.bukkit.message.BukkitMessageKey;
import dev.triumphteam.cmd.core.message.MessageKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class GigaAuth extends JavaPlugin {

    private static GigaAuth instance;
    private ConfigFactory configFactory;
    private PostgresDatabase database;
    private WhitelistManager whitelistManager;
    private MaintenanceManager maintenanceManager;
    private RegistrationManager registrationManager;

    @Override
    public void onEnable() {
        instance = this;
        initConfig();
        initDatabase();
        whitelistManager = new WhitelistManager();
        maintenanceManager = new MaintenanceManager();
        registrationManager = new RegistrationManager();

        BukkitCommandManager<CommandSender> manager = BukkitCommandManager.create(this);
        manager.registerMessage(BukkitMessageKey.NO_PERMISSION, (sender, context) ->
                sender.sendMessage(Settings.PREFIX.append(Component.text("Du hast keine Rechte!",
                        NamedTextColor.RED).decoration(TextDecoration.BOLD, false))));
        manager.registerMessage(MessageKey.UNKNOWN_COMMAND, (sender, context) -> sender.sendMessage(Settings.PREFIX.append(Component.text("Kommando ist unvollständig!",
                NamedTextColor.RED).decoration(TextDecoration.BOLD, false))));

        manager.registerCommand(new WhitelistCommand());
        manager.registerCommand(new MaintenanceCommand());

        Bukkit.getPluginManager().registerEvents(new LoginListener(), this);

        Bukkit.getConsoleSender().sendMessage(Settings.CONSOLE_PREFIX.append(Component.text("Plugin has been enabled!", NamedTextColor.GRAY)));
    }

    private void initConfig() {
        configFactory = new ConfigFactory(getDataFolder().getPath());
        configFactory.loadConfig(CredentialsConfig.class);
        configFactory.loadConfig(MaintenanceConfig.class);

        Bukkit.getConsoleSender().sendMessage(Settings.CONSOLE_PREFIX.append(Component.text("Config(s) has been initialized!", NamedTextColor.GRAY)));
    }

    private void initDatabase() {
        database = new PostgresDatabase(CredentialsConfig.getPostgresCredentials());

        DatabaseRegistry.registerStorage(new WhitelistStorage(database));
        DatabaseRegistry.registerStorage(new RegistrationStorage(database));
    }

    @Override
    public void onDisable() {
        database.disconnect();
        ThreadExecutor.shutdown();

        Bukkit.getConsoleSender().sendMessage(Settings.CONSOLE_PREFIX.append(Component.text("Plugin has been disabled!", NamedTextColor.GRAY)));
    }

    public static GigaAuth get() {
        return instance;
    }

    public ConfigFactory getConfigFactory() {
        return configFactory;
    }

    public WhitelistManager getWhitelistManager() {
        return whitelistManager;
    }

    public MaintenanceManager getMaintenanceManager() {
        return maintenanceManager;
    }

    public RegistrationManager getRegistrationManager() {
        return registrationManager;
    }
}
