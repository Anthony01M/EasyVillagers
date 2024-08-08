package xyz.berrystudios.easyvillagers.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.command.CommandSender;


@CommandAlias("ev|easyv|evillager|easyvillager|easyvillagers")
public final class GiveAllCommand extends BaseCommand {
    @Subcommand("giveall")
    @CommandPermission("easyvillagers.giveall")
    public void onDefault(final CommandSender sender, final String type, final int amount) {

    }
}
