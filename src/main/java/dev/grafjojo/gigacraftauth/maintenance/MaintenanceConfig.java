package dev.grafjojo.gigacraftauth.maintenance;

import dev.grafjojo.gigacraftcore.config.GigaConfig;
import dev.grafjojo.gigacraftcore.config.annotations.ConfigValue;
import dev.grafjojo.gigacraftcore.config.annotations.YamlConfig;

@YamlConfig(name = "maintenance")
public class MaintenanceConfig implements GigaConfig {

    @ConfigValue(key = "maintenance.enabled")
    public static boolean maintenance = true;

    @ConfigValue(key = "maintenance.reason")
    public static String maintenanceReason = "";
}
