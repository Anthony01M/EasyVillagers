package xyz.berrystudios.easyvillagers.util.misc;

import com.google.common.util.concurrent.MoreExecutors;
import lombok.Getter;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AsyncScheduler {
    @Getter
    private static final Executor executor = MoreExecutors.directExecutor();
    @Getter
    private static final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(3);

    public static void runAsync(Runnable runnable) {
        executor.execute(runnable);
    }

    public static void scheduleAsync(Runnable runnable, long delay, TimeUnit unit) {
        scheduledExecutor.schedule(runnable, delay, unit);
    }

    public static void runTaskLater(Runnable runnable, long l) {
        executor.execute(() -> {
            try {
                Thread.sleep(l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            executor.execute(runnable);
        });
    }
}
