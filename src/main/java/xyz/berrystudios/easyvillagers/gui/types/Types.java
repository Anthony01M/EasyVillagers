package xyz.berrystudios.easyvillagers.gui.types;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.berrystudios.easyvillagers.gui.types.type.FarmerType;
import xyz.berrystudios.easyvillagers.gui.types.type.IronFarmType;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

public class Types {
    public void open(Player player) {
        Gui gui = Gui.normal()
                .setStructure(
                        "# # # # # # # # #",
                        "# . 1 . 2 . 3 . #",
                        "# . . 4 . 5 . . #",
                        "# # # # # # # # #")
                .addIngredient('#', new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)
                        .setDisplayName("§7"))
                )
                .addIngredient('1', new FarmerType())
                .addIngredient('2', new IronFarmType())
                .build();

        Window window = Window.single()
                .setViewer(player)
                .setTitle("§6EasyVillager §aTypes")
                .setGui(gui)
                .build();

        window.open();
    }
}
