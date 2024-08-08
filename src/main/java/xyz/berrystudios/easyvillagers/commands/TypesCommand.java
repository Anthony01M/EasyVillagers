package xyz.berrystudios.easyvillagers.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.berrystudios.easyvillagers.gui.types.Types;

@CommandAlias("ev|easyv|evillager|easyvillager|easyvillagers")
public final class TypesCommand extends BaseCommand {
    @Subcommand("types|type")
    @CommandPermission("easyvillagers.types")
    public void onDefault(final CommandSender sender) {
        if (sender instanceof Player player) {
            Types type = new Types();
            type.open(player);
        } else sender.sendMessage("§cOnly players can use this command.");
    }
}