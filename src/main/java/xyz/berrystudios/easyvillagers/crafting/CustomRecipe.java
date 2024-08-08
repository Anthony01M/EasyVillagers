package xyz.berrystudios.easyvillagers.crafting;

import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import xyz.berrystudios.easyvillagers.EasyVillagers;

import java.util.Objects;

public interface CustomRecipe {
    void register(EasyVillagers easyVillagers);
    default void recipes(EasyVillagers easyVillagers, ShapedRecipe recipe, String recipes) {
        String[] recipeArray = recipes.split(" ");
        for (int i = 0; i < recipeArray.length; i++) {
            char c = (char) (65 + i);
            Material material = Material.getMaterial(recipeArray[i]);
            if (material == null) {
                Objects.requireNonNull(easyVillagers.getLogger()).warning("Invalid material in farmer recipe: " + recipeArray[i]);
                return;
            }
            recipe.setIngredient(c, material);
        }
    }
}