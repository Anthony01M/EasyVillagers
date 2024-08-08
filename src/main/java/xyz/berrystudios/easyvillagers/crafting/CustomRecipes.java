package xyz.berrystudios.easyvillagers.crafting;

import xyz.berrystudios.easyvillagers.EasyVillagers;
import xyz.berrystudios.easyvillagers.crafting.recipes.FarmerRecipe;
import xyz.berrystudios.easyvillagers.crafting.recipes.IronFarmRecipe;

import java.util.Arrays;
import java.util.List;

public class CustomRecipes {
    private final List<CustomRecipe> recipes;

    public CustomRecipes() {
        this.recipes = Arrays.asList(
                new FarmerRecipe(),
                new IronFarmRecipe()
        );
    }
    public void registerAll(EasyVillagers easyVillagers) {
        for (CustomRecipe recipe : recipes) recipe.register(easyVillagers);
    }
}
