package xyz.berrystudios.easyvillagers.manager.init.stop;

import xyz.berrystudios.easyvillagers.manager.init.Initable;
import xyz.berrystudios.easyvillagers.util.thread.ThreadManager;

public class TerminateThread implements Initable {
    @Override
    public void start() {
        ThreadManager.shutdown();
    }
}
