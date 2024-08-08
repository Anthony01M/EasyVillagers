package xyz.berrystudios.easyvillagers.events.bukkit;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import xyz.berrystudios.easyvillagers.EasyVillagers;
import xyz.berrystudios.easyvillagers.database.SQL;

import java.util.Objects;

public class BlockPlaceListener implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (isCustomBlock(item)) {
            Block block = event.getBlock();
            Location location = block.getLocation();
            String blockType = getBlockTypeFromItem(item);

            if (blockType != null) {
                SQL.getInstance().getCustomBlocksService().addCustomBlock(locationToString(location), blockType);

                // Check if a villager is placed adjacent to the block
                for (BlockFace face : BlockFace.values()) {
                    Block adjacentBlock = block.getRelative(face);
                    if (adjacentBlock.getType() == Material.VILLAGER_SPAWN_EGG) {
                        // Update the block to start functionality
                        if (blockType.equals("iron_farm_block")) {
                            // Start Iron Farm functionality
                            SQL.getInstance().getCustomBlocksService().updateIronFarmStatus(locationToString(location), true);
                        } else if (blockType.equals("farmer_block")) {
                            // Update Farmer block with crop type
                            String cropType = getCropTypeFromItem(item);
                            SQL.getInstance().getCustomBlocksService().updateFarmerBlock(locationToString(location), cropType);
                        }
                    }
                }
            }
        }
    }

    private boolean isCustomBlock(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        NamespacedKey key = new NamespacedKey(EasyVillagers.getInstance(), "custom_block_key");
        return meta.getPersistentDataContainer().has(key, PersistentDataType.STRING);
    }

    private String getBlockTypeFromItem(ItemStack item) {
        if (item == null) return null;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        NamespacedKey key = new NamespacedKey(EasyVillagers.getInstance(), "custom_block_key");
        return meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }

    private String getCropTypeFromItem(ItemStack item) {
        // Implement logic to extract crop type from item metadata or name
        return "wheat"; // Placeholder
    }

    private String locationToString(Location location) {
        return Objects.requireNonNull(location.getWorld()).getName() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ();
    }
}
