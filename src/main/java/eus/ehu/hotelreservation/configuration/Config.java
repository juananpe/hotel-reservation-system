package eus.ehu.hotelreservation.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton that loads and exposes application configuration from
 * {@code config.properties}.
 * <p>
 * Provides access to database-related settings such as the open mode
 * (initialize vs. reuse existing schema), the database name, and
 * whether the data-access layer is local or remote.
 */
public class Config {

    private Properties prop;

    public static Config getInstance() {
        return instance;
    }

    private static Config instance = new Config();

    private Config() {
        loadConfig();
    }

    private void loadConfig() {
        prop = new Properties();
        try (InputStream input = new FileInputStream("config.properties")) {
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getDataBaseOpenMode() {
        return prop.getProperty("db.openmode");
    }

    public String getDatabaseName() {
        return prop.getProperty("db.name");
    }

    public boolean isDataAccessLocal() {
        return prop.getProperty("db.local").equals("true");
    }

}
