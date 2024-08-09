package xyz.berrystudios.easyvillagers.gui.types.craft;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.berrystudios.easyvillagers.EasyVillagers;
import xyz.berrystudios.easyvillagers.gui.types.Craft;

public class FarmerCraft {
    public void open(Player player) {
        String title = "§a§lFarmer";
        Material material = Material.DIRT;
        String[] loreLines = new String[]{
                "§7",
                "§fFarmers are the most §c§ncommon§f§r§f",
                "§fvillager type. They can §c§nplant§f§r§f",
                "§fand §c§nharvest§f§r§f crops.",
                "§7"
        };

        Craft craft = new Craft(title, material, loreLines, EasyVillagers.getInstance().getCfile().craftable_farmer_recipes);
        craft.open(player);
    }
}
