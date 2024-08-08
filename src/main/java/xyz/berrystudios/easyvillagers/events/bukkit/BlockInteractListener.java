package xyz.berrystudios.easyvillagers.events.bukkit;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import xyz.berrystudios.easyvillagers.EasyVillagers;
import xyz.berrystudios.easyvillagers.database.SQL;

import java.util.Objects;

public class BlockInteractListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        if (block == null) return;

        Location location = block.getLocation();
        String customBlockType = SQL.getInstance().getCustomBlocksService().isCustomBlock(locationToString(location));

        if (customBlockType != null) {
            if (customBlockType.equals("iron_farm_block")) {
                if (event.getItem() != null && event.getItem().getType() == Material.VILLAGER_SPAWN_EGG)
                    SQL.getInstance().getCustomBlocksService().updateIronFarmStatus(locationToString(location), true);
                else if (event.getItem() == null || event.getItem().getType() == Material.AIR) {
                    ItemStack villager = new ItemStack(Material.VILLAGER_SPAWN_EGG);
                    event.getPlayer().getInventory().addItem(villager);
                    SQL.getInstance().getCustomBlocksService().updateIronFarmStatus(locationToString(location), false);
                }
            } else if (customBlockType.equals("farmer_block")) {
                String cropType = getCropTypeFromItem(event.getItem());
                if (cropType != null)
                    SQL.getInstance().getCustomBlocksService().updateFarmerBlock(locationToString(location), cropType);
            }
        }
    }
    private String getCropTypeFromItem(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return null;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        NamespacedKey cropTypeKey = new NamespacedKey(EasyVillagers.getInstance(), "crop_type_key");
        if (meta.getPersistentDataContainer().has(cropTypeKey, PersistentDataType.STRING))
            return meta.getPersistentDataContainer().get(cropTypeKey, PersistentDataType.STRING);
        return null;
    }
    private String locationToString(Location location) {
        return Objects.requireNonNull(location.getWorld()).getName() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ();
    }
}
