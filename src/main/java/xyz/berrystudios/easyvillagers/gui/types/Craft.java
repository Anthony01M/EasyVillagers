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

    public Craft(String title, Material material, String[] loreLines) {
        this.title = title;
        this.material = material;
        this.loreLines = loreLines;
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
                        "# . . . # # # # #",
                        "# . . . # # * # #",
                        "# . . . # # # # #",
                        "# # # # # # # # #",
                        "0 0 0 0 < 0 0 0 0"
                )
                .addIngredient('#', blackStainedGlassPane)
                .addIngredient('0', redStainedGlassPane)
                .addIngredient('*', typeItem)
                .addIngredient('<', backButton)
                .build();

        Window window = Window.single()
                .setViewer(player)
                .setTitle(title)
                .setGui(gui)
                .build();

        window.open();
    }
}
