package xyz.berrystudios.easyvillagers.manager.init.start;

import co.aikar.commands.PaperCommandManager;
import xyz.berrystudios.easyvillagers.EasyVillagers;
import xyz.berrystudios.easyvillagers.commands.*;
import xyz.berrystudios.easyvillagers.manager.init.Initable;

public class CommandRegister implements Initable {
    @Override public void start() {
        PaperCommandManager commandManager = new PaperCommandManager(EasyVillagers.getInstance());

        // EasyVillagers commands
        commandManager.registerCommand(new GiveAllCommand());
        commandManager.registerCommand(new GiveCommand());
    }
}
