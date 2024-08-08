package xyz.berrystudios.easyvillagers.util.misc;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;

public class YamlLoader extends YamlConfiguration {
    private final File configFile;
    public YamlLoader(File configFile) {
        this.configFile = configFile;
        loadConfiguration();
    }
    private YamlLoader() {
        this.configFile = null;
    }
    private void loadConfiguration() {
        try {
            load(new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8));
        } catch (FileNotFoundException ignored) {
        } catch (IOException | InvalidConfigurationException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + configFile, e);
        }
    }
    public static YamlLoader loadConfiguration(final InputStream stream) {
        YamlLoader config = new YamlLoader();
        try {
            config.load(new InputStreamReader(stream, StandardCharsets.UTF_8));
        } catch (IOException | InvalidConfigurationException var3) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", var3);
        }
        return config;
    }
    public String getString(String path, String def) {
        return super.getString(path, def);
    }
    public List<String> getStringList(String path) {
        return super.getStringList(path);
    }
    public void saveConfig() {
        if (configFile != null) {
            try {
                save(configFile);
            } catch (IOException e) {
                Bukkit.getLogger().log(Level.SEVERE, "Cannot save " + configFile, e);
            }
        }
    }
}
