package xyz.berrystudios.easyvillagers.gui.types.type;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.berrystudios.easyvillagers.EasyVillagers;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class IronFarmType extends AbstractItem {
    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.IRON_INGOT)
                .setDisplayName("§a§lFarmer")
                .addLoreLines(
                        "§7",
                        "§fThe iron farm block contains a complete",
                        "§firon farm in a single block. It produces one iron",
                        "§fgolem every four minutes.",
                        "§fThis averages about one iron ingot per minute.",
                        "§7",
                        EasyVillagers.getInstance().getCfile().craftable_ironfarm ? "§eClick to see how to craft and required materials" : "§cYou can't craft this villager type."
                );
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {

    }
}
