package xyz.berrystudios.easyvillagers.crafting;

import xyz.berrystudios.easyvillagers.EasyVillagers;
import xyz.berrystudios.easyvillagers.crafting.recipes.FarmerRecipe;
import xyz.berrystudios.easyvillagers.crafting.recipes.IronFarmRecipe;

import java.util.ArrayList;
import java.util.List;

public class CustomRecipes {
    private final List<CustomRecipe> recipes = new ArrayList<>();;

    public CustomRecipes() {
        if (EasyVillagers.getInstance().getCfile().craftable_farmer) recipes.add(new FarmerRecipe());
        if (EasyVillagers.getInstance().getCfile().craftable_ironfarm) recipes.add(new IronFarmRecipe());
    }
    public void registerAll(EasyVillagers easyVillagers) {
        for (CustomRecipe recipe : recipes) recipe.register(easyVillagers);
    }
}
