package xyz.berrystudios.easyvillagers.gui.types.craft;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.berrystudios.easyvillagers.EasyVillagers;
import xyz.berrystudios.easyvillagers.gui.types.Craft;

public class IronFarmCraft {
    public void open(Player player) {
        String title = "§f§lIron Farm";
        Material material = Material.DIRT;
        String[] loreLines = new String[]{
                "§7",
                "§fIron Farms are the most §c§ncommon§f§r§f",
                "§fvillager type. They can §b§nproduce§f§r§f",
                "§firon ingots.",
                "§7"
        };

        Craft craft = new Craft(title, material, loreLines, EasyVillagers.getInstance().getCfile().craftable_ironfarm_recipes);
        craft.open(player);
    }
}
