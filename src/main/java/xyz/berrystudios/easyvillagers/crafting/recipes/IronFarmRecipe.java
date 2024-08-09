package xyz.berrystudios.easyvillagers.crafting.recipes;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import xyz.berrystudios.easyvillagers.EasyVillagers;
import xyz.berrystudios.easyvillagers.crafting.CustomRecipe;

public class IronFarmRecipe implements CustomRecipe {
    @Override
    public void register(EasyVillagers easyVillagers) {
        ItemStack result = new ItemStack(Material.GLASS);
        ItemMeta meta = result.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§fFarmer");
            NamespacedKey key = new NamespacedKey(easyVillagers, "iron_farm_block");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "iron_farm_block");
            result.setItemMeta(meta);
        }
        NamespacedKey key = new NamespacedKey(easyVillagers, "iron_farm_recipe");
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape("ABC", "DEF", "GHI");
        String recipes = EasyVillagers.getInstance().getCfile().craftable_ironfarm_recipes;
        recipes(easyVillagers, recipe, recipes);
    }
}
