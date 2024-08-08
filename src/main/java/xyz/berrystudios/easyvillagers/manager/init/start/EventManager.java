package xyz.berrystudios.easyvillagers.manager.init.start;

import org.bukkit.Bukkit;
import xyz.berrystudios.easyvillagers.EasyVillagers;
import xyz.berrystudios.easyvillagers.crafting.CustomRecipes;
import xyz.berrystudios.easyvillagers.events.bukkit.BlockBreakListener;
import xyz.berrystudios.easyvillagers.events.bukkit.BlockInteractListener;
import xyz.berrystudios.easyvillagers.events.bukkit.BlockPlaceListener;
import xyz.berrystudios.easyvillagers.manager.init.Initable;
import xyz.berrystudios.easyvillagers.util.misc.LogUtil;

public class EventManager implements Initable {
    @Override
    public void start() {
        LogUtil.info("Registering bukkit events...");

        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), EasyVillagers.getInstance());
        Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(), EasyVillagers.getInstance());
        Bukkit.getPluginManager().registerEvents(new BlockInteractListener(), EasyVillagers.getInstance());

        CustomRecipes customRecipes = new CustomRecipes();
        customRecipes.registerAll(EasyVillagers.getInstance());
    }
}
