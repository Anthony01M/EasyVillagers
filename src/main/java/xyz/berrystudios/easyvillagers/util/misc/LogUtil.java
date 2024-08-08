package xyz.berrystudios.easyvillagers.util.misc;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import xyz.berrystudios.easyvillagers.EasyVillagers;

import java.util.logging.Logger;

@UtilityClass
public class LogUtil {
    public void info(final String info) {
        getLogger().info(info);
    }
    public void warn(final String warn) {
        getLogger().info(warn);
    }
    public void error(final String error) {
        getLogger().info(error);
    }
    public Logger getLogger() {
        return EasyVillagers.getInstance().getLogger();
    }
    public void console(final String info) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', info));
    }
}
