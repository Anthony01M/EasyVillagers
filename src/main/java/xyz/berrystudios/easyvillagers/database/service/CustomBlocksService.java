package xyz.berrystudios.easyvillagers.database.service;

import xyz.berrystudios.easyvillagers.database.dao.customBlocks.CustomBlocksDao;
import xyz.berrystudios.easyvillagers.database.model.CustomBlocks;
import xyz.berrystudios.easyvillagers.util.misc.AsyncScheduler;

import java.util.List;

public class CustomBlocksService {
    private final CustomBlocksDao customBlocksDao;

    public CustomBlocksService(CustomBlocksDao customBlocksDao) {
        this.customBlocksDao = customBlocksDao;
    }

    public String isCustomBlock(String location) {
        return customBlocksDao.isCustomBlock(location);
    }

    public void addCustomBlock(String location, String type) {
        AsyncScheduler.getExecutor().execute(() -> customBlocksDao.addCustomBlock(location, type));
    }

    public void removeCustomBlock(String location) {
        AsyncScheduler.getExecutor().execute(() -> customBlocksDao.removeCustomBlock(location));
    }

    public List<CustomBlocks> getAllCustomBlocks() {
        return customBlocksDao.getAllCustomBlocks();
    }

    public void updateCustomBlock(CustomBlocks block) {
        AsyncScheduler.getExecutor().execute(() -> customBlocksDao.updateCustomBlock(block));
    }

    public void updateIronFarmStatus(String location, boolean isActive) {
        AsyncScheduler.getExecutor().execute(() -> customBlocksDao.updateIronFarmStatus(location, isActive));
    }

    public void updateFarmerBlock(String location, String cropType) {
        AsyncScheduler.getExecutor().execute(() -> customBlocksDao.updateFarmerBlock(location, cropType));
    }
}
