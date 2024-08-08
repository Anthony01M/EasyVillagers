package xyz.berrystudios.easyvillagers.gui.types.type;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.berrystudios.easyvillagers.EasyVillagers;
import xyz.berrystudios.easyvillagers.gui.types.craft.FarmerCraft;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class FarmerType extends AbstractItem {

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.DIRT)
                .setDisplayName("§a§lFarmer")
                .addLoreLines(
                        "§7",
                        "§fThe farmer block contains a whole",
                        "§fcrop farm in a single block.",
                        "§7",
                        EasyVillagers.getInstance().getCfile().craftable_farmer ? "§eClick to see how to craft and required materials" : "§cYou can't craft this villager type."
                );
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        if (EasyVillagers.getInstance().getCfile().craftable_farmer) {
            FarmerCraft windows = new FarmerCraft();
            windows.open(player);
        }
    }
}
