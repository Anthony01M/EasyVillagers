package xyz.berrystudios.easyvillagers.manager.init.start;

import xyz.berrystudios.easyvillagers.manager.init.Initable;
import xyz.berrystudios.easyvillagers.util.misc.LogUtil;

public class PacketManager implements Initable {
    @Override
    public void start() {
        LogUtil.info("Registering packets...");

        //PacketEvents.getAPI().getEventManager().registerListener();

    }
}
