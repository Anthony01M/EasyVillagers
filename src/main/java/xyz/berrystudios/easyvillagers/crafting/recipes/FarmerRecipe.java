package xyz.berrystudios.easyvillagers.crafting.recipes;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import xyz.berrystudios.easyvillagers.EasyVillagers;
import xyz.berrystudios.easyvillagers.crafting.CustomRecipe;

public class FarmerRecipe implements CustomRecipe {
    @Override
    public void register(EasyVillagers easyVillagers) {
        ItemStack result = new ItemStack(Material.GLASS);
        ItemMeta meta = result.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§fIron Farm");
            NamespacedKey key = new NamespacedKey(easyVillagers, "farmer_block");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "farmer_block");
            result.setItemMeta(meta);
        }
        NamespacedKey key = new NamespacedKey(easyVillagers, "farmer_recipe");
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape("AAA", "ABA", "CDC");
        recipe.setIngredient('A', Material.GLASS_PANE);
        recipe.setIngredient('B', Material.WATER);
        recipe.setIngredient('C', Material.IRON_INGOT);
        recipe.setIngredient('D', Material.DIRT);
    }
}
