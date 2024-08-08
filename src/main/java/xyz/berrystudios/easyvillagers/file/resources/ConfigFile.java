package xyz.berrystudios.easyvillagers.file.resources;

import org.bukkit.ChatColor;
import xyz.berrystudios.easyvillagers.file.AbstractFile;
import xyz.berrystudios.easyvillagers.file.Load;
import xyz.berrystudios.easyvillagers.util.misc.YamlLoader;

public class ConfigFile extends AbstractFile {
    @Load(path = "prefix")
    public String prefix = "§6EasyVillagers §7» §f";

    @Load(path = "database.type")
    public String database_type;
    @Load(path = "database.host")
    public String database_host;
    @Load(path = "database.port")
    public int database_port;
    @Load(path = "database.database")
    public String database_database;
    @Load(path = "database.username")
    public String database_username;
    @Load(path = "database.password")
    public String database_password;

    @Load(path = "craftable.farmer.enabled")
    public boolean craftable_farmer;
    @Load(path = "craftable.farmer.recipes")
    public String craftable_farmer_recipes;
    @Load(path = "craftable.ironfarm.enabled")
    public boolean craftable_ironfarm;
    @Load(path = "craftable.ironfarm.recipes")
    public String craftable_ironfarm_recipes;

    @Override
    public Object getValue(String path, YamlLoader loader, Class<?> type) {
        Object value = loader.get(path);
        return value instanceof String ? ChatColor.translateAlternateColorCodes('&', (String) value) : value;
    }
}
