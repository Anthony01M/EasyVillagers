package xyz.berrystudios.easyvillagers.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.command.CommandSender;
import xyz.berrystudios.easyvillagers.EasyVillagers;

@CommandAlias("ev|easyv|evillager|easyvillager|easyvillagers")
public final class HelpCommand extends BaseCommand {
    @Default
    @Subcommand("help")
    @CommandPermission("easyvillagers.help")
    public void onDefault(final CommandSender sender) {
        sender.sendMessage("""
                §6EasyVillagers §fversion %s §fHelp
                §7- §e/ev types §7- §fOpens the types GUI
                §7- §e/ev give <player> <type> <amount> §7- §fGives a player a farm type
                §7- §e/ev giveall <type> <amount> §7- §fGives all players a farm type
                §7- §e/ev reload §7- §fReloads the plugin configuration
                """, EasyVillagers.getInstance().getDescription().getVersion());
    }
}
