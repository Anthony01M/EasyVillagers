package xyz.berrystudios.easyvillagers.manager;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import xyz.berrystudios.easyvillagers.manager.init.Initable;
import xyz.berrystudios.easyvillagers.manager.init.start.*;
import xyz.berrystudios.easyvillagers.manager.init.stop.*;

public class InitManager {
    ClassToInstanceMap<Initable> initializersOnLoad;
    ClassToInstanceMap<Initable> initializersOnStart;
    ClassToInstanceMap<Initable> initializersOnStop;
    public InitManager() {
        initializersOnLoad = new ImmutableClassToInstanceMap.Builder<Initable>()
                .build();
        initializersOnStart = new ImmutableClassToInstanceMap.Builder<Initable>()
                .put(EventManager.class, new EventManager())
                .put(CommandRegister.class, new CommandRegister())
                .put(Scheduler.class, new Scheduler())
                .build();
        initializersOnStop = new ImmutableClassToInstanceMap.Builder<Initable>()
                .put(TerminateThread.class, new TerminateThread())
                .build();
    }
    public void load() {
        for (Initable initable : initializersOnLoad.values()) initable.start();
    }
    public void start() {
        for (Initable initable : initializersOnStart.values()) initable.start();
    }
    public void stop() {
        for (Initable initable : initializersOnStop.values()) initable.start();
    }
}
