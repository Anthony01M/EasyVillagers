package xyz.berrystudios.easyvillagers.database.dao.customBlocks;

import xyz.berrystudios.easyvillagers.database.model.CustomBlocks;

import java.util.List;

public interface CustomBlocksDao {
    String isCustomBlock(String location);
    void addCustomBlock(String location, String type);
    void removeCustomBlock(String location);
    void updateCustomBlock(CustomBlocks block);
    void updateIronFarmStatus(String s, boolean b);
    void updateFarmerBlock(String s, String cropType);
    List<CustomBlocks> getAllCustomBlocks();
}
