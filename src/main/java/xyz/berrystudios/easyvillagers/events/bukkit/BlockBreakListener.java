package xyz.berrystudios.easyvillagers.events.bukkit;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import xyz.berrystudios.easyvillagers.EasyVillagers;
import xyz.berrystudios.easyvillagers.database.SQL;

import java.util.Objects;

public class BlockBreakListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Location location = block.getLocation();
        String customBlockType = SQL.getInstance().getCustomBlocksService().isCustomBlock(locationToString(location));
        if (customBlockType != null) {
            ItemStack customItem = createCustomBlock(customBlockType);
            event.setDropItems(false);
            block.getWorld().dropItemNaturally(location, customItem);
            SQL.getInstance().getCustomBlocksService().removeCustomBlock(locationToString(location));
        }
    }
    private ItemStack createCustomBlock(String key) {
        ItemStack customBlock = new ItemStack(Material.GLASS);
        ItemMeta meta = customBlock.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(getDisplayNameForKey(key));
            NamespacedKey namespacedKey = new NamespacedKey(EasyVillagers.getInstance(), "custom_block_key");
            meta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, key);
            customBlock.setItemMeta(meta);
        }
        return customBlock;
    }
    private String getDisplayNameForKey(String key) {
        return switch (key) {
            case "farmer_block" -> "§fFarmer";
            case "iron_farm_block" -> "§fIron Farm";
            default -> "§fUnknown Block";
        };
    }
    private String locationToString(Location location) {
        return Objects.requireNonNull(location.getWorld()).getName() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ();
    }
}
