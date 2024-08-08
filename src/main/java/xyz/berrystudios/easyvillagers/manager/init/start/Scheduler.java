package xyz.berrystudios.easyvillagers.manager.init.start;

import xyz.berrystudios.easyvillagers.database.SQL;
import xyz.berrystudios.easyvillagers.database.model.CustomBlocks;
import xyz.berrystudios.easyvillagers.manager.init.Initable;
import xyz.berrystudios.easyvillagers.util.thread.ThreadManager;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler implements Initable {
    private ScheduledExecutorService executorService;
    @Override
    public void start() {
        executorService = ThreadManager.createNewScheduledExecutor("EasyVillagers-Schedule-Thread");
        schedulePeriodicTasks();
    }
    private void schedulePeriodicTasks() {
        executorService.scheduleAtFixedRate(this::updateCustomBlocks, 0, 5, TimeUnit.SECONDS);
    }
    private void updateCustomBlocks() {
        List<CustomBlocks> customBlocks = SQL.getInstance().getCustomBlocksService().getAllCustomBlocks();
        for (CustomBlocks block : customBlocks) {
            long currentTime = System.currentTimeMillis();
            long elapsed = currentTime - block.getUpdatedOn();
            if (needsUpdate(block, elapsed)) {
                String newData = generateRandomData(block);
                block.setData(newData);
                block.setUpdatedOn(currentTime);
                SQL.getInstance().getCustomBlocksService().updateCustomBlock(block);
            }
        }
    }
    private String generateRandomData(CustomBlocks block) {
        return switch (block.getType()) {
            case "iron_farm_block" ->
                    "{ \"iron\": " + (int) (Math.random() * 5) + ", \"roses\": " + (int) (Math.random() * 3) + " }";
            case "wheat_farm_block" ->
                    "{ \"wheat\": " + (int) (Math.random() * 5) + ", \"seeds\": " + (int) (Math.random() * 3) + " }";
            case "carrot_farm_block" ->
                    "{ \"carrots\": " + (int) (Math.random() * 5) + ", \"seeds\": " + (int) (Math.random() * 3) + " }";
            case "potato_farm_block" ->
                    "{ \"potatoes\": " + (int) (Math.random() * 5) + ", \"seeds\": " + (int) (Math.random() * 3) + " }";
            case "pumpkin_farm_block" ->
                    "{ \"pumpkins\": " + (int) (Math.random() * 5) + ", \"seeds\": " + (int) (Math.random() * 3) + " }";
            case "melon_farm_block" ->
                    "{ \"melons\": " + (int) (Math.random() * 5) + ", \"seeds\": " + (int) (Math.random() * 3) + " }";
            case "sugar_cane_farm_block" ->
                    "{ \"sugar_cane\": " + (int) (Math.random() * 5) + ", \"seeds\": " + (int) (Math.random() * 3) + " }";
            case "cactus_farm_block" ->
                    "{ \"cactus\": " + (int) (Math.random() * 5) + ", \"seeds\": " + (int) (Math.random() * 3) + " }";
            case "vine_farm_block" ->
                    "{ \"vines\": " + (int) (Math.random() * 5) + ", \"seeds\": " + (int) (Math.random() * 3) + " }";
            case "nether_wart_farm_block" ->
                    "{ \"nether_wart\": " + (int) (Math.random() * 5) + " }";
            case "tree_farm_block" ->
                    "{ \"logs\": " + (int) (Math.random() * 5) + ", \"saplings\": " + (int) (Math.random() * 3) + " }";
            default -> "";
        };
    }
    private boolean needsUpdate(CustomBlocks block, long elapsed) {

        return elapsed >= 60000; // Example: update every minute
    }
}
