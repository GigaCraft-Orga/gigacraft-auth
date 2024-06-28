package dev.grafjojo.gigacraftauth.configs;

import dev.grafjojo.gigacraftcore.config.GigaConfig;
import dev.grafjojo.gigacraftcore.config.annotations.ConfigValue;
import dev.grafjojo.gigacraftcore.config.annotations.YamlConfig;
import dev.grafjojo.gigacraftcore.database.DatabaseCredentials;

@YamlConfig(name = "credentials")
public class CredentialsConfig implements GigaConfig {

    @ConfigValue(key = "postgres.host")
    public static String postgresHost = "localhost";

    @ConfigValue(key = "postgres.port")
    public static int postgresPort = 5432;

    @ConfigValue(key = "postgres.user")
    public static String postgresUser = "postgres";

    @ConfigValue(key = "postgres.password")
    public static String postgresPassword = " ";

    @ConfigValue(key = "postgres.database")
    public static String postgresDatabase = "postgres";

    public static DatabaseCredentials getPostgresCredentials() {
        return new DatabaseCredentials(postgresHost, postgresPort, postgresDatabase, postgresUser, postgresPassword);
    }
}
