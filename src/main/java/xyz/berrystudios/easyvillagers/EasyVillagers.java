package xyz.berrystudios.easyvillagers;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.berrystudios.easyvillagers.file.AbstractFile;
import xyz.berrystudios.easyvillagers.file.resources.ConfigFile;
import xyz.berrystudios.easyvillagers.manager.InitManager;
import xyz.berrystudios.easyvillagers.util.misc.YamlLoader;
import xyz.berrystudios.easyvillagers.util.thread.ThreadManager;

import java.io.File;
import java.util.concurrent.ExecutorService;

public final class EasyVillagers extends JavaPlugin {
    @Getter public static EasyVillagers instance;
    @Getter public ConfigFile cfile;
    @Getter private ExecutorService executorService;
    private final InitManager initManager = new InitManager();

    @Override public void onLoad() {
        initManager.load();
    }

    @Override public void onEnable() {
        // Plugin startup logic
        instance = this;
        this.executorService = ThreadManager.createNewExecutor("EasyVillagers-Thread");
        File folder = this.getDataFolder();
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File configFile = new File(folder, "config.yml");
        if (!configFile.exists()) {
            saveResource("config.yml", true);
        }
        YamlLoader configYaml = new YamlLoader(configFile);
        this.cfile = AbstractFile.load(null, new ConfigFile(), configYaml);

        initManager.start();
    }

    @Override public void onDisable() {
        // Plugin shutdown logic
        initManager.stop();
    }
}
