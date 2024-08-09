package xyz.berrystudios.easyvillagers.gui.types;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

public class Craft {
    private final String title;
    private final Material material;
    private final String[] loreLines;
    private final String recipePattern;

    public Craft(String title, Material material, String[] loreLines, String recipePattern) {
        this.title = title;
        this.material = material;
        this.loreLines = loreLines;
        this.recipePattern = recipePattern;
    }

    public void open(Player player) {
        Item blackStainedGlassPane = new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§7"));
        Item redStainedGlassPane = new SimpleItem(new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName("§7"));
        Item typeItem = new SimpleItem(new ItemBuilder(material)
                .setDisplayName(title)
                .addLoreLines(loreLines)
        );
        Item backButton = new SimpleItem(new ItemBuilder(Material.ARROW).setDisplayName("§cBack"));
        Gui gui = Gui.normal()
                .setStructure(
                        "# # # # # # # # #",
                        "# 1 2 3 # # # # #",
                        "# 4 5 6 # # * # #",
                        "# 7 8 9 # # # # #",
                        "# # # # # # # # #",
                        "0 0 0 0 < 0 0 0 0"
                )
                .addIngredient('#', blackStainedGlassPane)
                .addIngredient('0', redStainedGlassPane)
                .addIngredient('*', typeItem)
                .addIngredient('<', backButton)
                .build();

        populateRecipe(gui);

        Window window = Window.single()
                .setViewer(player)
                .setTitle(title)
                .setGui(gui)
                .build();

        window.open();
    }

    private void populateRecipe(Gui gui) {
        String[] ingredients = recipePattern.split(" ");
        for (int i = 0; i < ingredients.length; i++) {
            Material material = Material.getMaterial(ingredients[i]);
            if (material != null)
                gui.setItem(i + 1, new SimpleItem(new ItemBuilder(material).setDisplayName("§f" + material.name())));
        }
    }
}
