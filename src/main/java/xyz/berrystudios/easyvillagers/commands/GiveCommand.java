package xyz.berrystudios.easyvillagers.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.entity.Player;
import xyz.berrystudios.easyvillagers.EasyVillagers;

@CommandAlias("ev|easyv|evillager|easyvillager|easyvillagers")
public final class GiveCommand extends BaseCommand {

    @Subcommand("give")
    @CommandPermission("easyvillagers.give")
    public void onDefault(final CommandSender sender, final OnlinePlayer target, final String type, final int amount) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return;
        }
        Player player = target.getPlayer();
        if (player == null) {
            sender.sendMessage("Player not found.");
            return;
        }
        ItemStack customBlock = createCustomBlock(type);
        if (customBlock == null) {
            sender.sendMessage("Invalid custom block type.");
            return;
        }
        customBlock.setAmount(amount);
        player.getInventory().addItem(customBlock);
        sender.sendMessage("Gave " + amount + " " + type + " to " + player.getName() + ".");
    }

    private ItemStack createCustomBlock(String type) {
        Material material = Material.GLASS;
        String displayName;
        NamespacedKey key = new NamespacedKey(EasyVillagers.getInstance(), type);
        switch (type) {
            case "farmer_block":
                displayName = "§fFarmer Block";
                break;
            case "iron_farm_block":
                displayName = "§fIron Farm Block";
                break;
            default:
                return null;
        }
        ItemStack customBlock = new ItemStack(material);
        ItemMeta meta = customBlock.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, type);
            customBlock.setItemMeta(meta);
        }
        return customBlock;
    }
}
