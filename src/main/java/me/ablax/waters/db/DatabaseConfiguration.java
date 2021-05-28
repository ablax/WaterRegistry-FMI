package me.ablax.waters.db;

import java.util.Properties;

/**
 * @author Murad Hamza on 29.5.2021 г.
 */
public class DatabaseConfiguration {
    private final String connectionUrl;
    private final String driver;
    private final String username;
    private final String password;

    public DatabaseConfiguration(final Properties properties) {
        this.connectionUrl = properties.getProperty("connectionUrl");
        this.driver = properties.getProperty("driver");
        this.username = properties.getProperty("dbusername");
        this.password = properties.getProperty("dbpassword");
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public String getDriver() {
        return driver;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
