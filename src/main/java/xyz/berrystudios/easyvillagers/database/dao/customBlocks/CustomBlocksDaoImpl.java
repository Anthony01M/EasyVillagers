package xyz.berrystudios.easyvillagers.database.dao.customBlocks;

import xyz.berrystudios.easyvillagers.database.model.CustomBlocks;
import xyz.berrystudios.easyvillagers.util.misc.LogUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CustomBlocksDaoImpl implements CustomBlocksDao {
    private final DataSource dataSource;

    public CustomBlocksDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String isCustomBlock(String location) {
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM custom_blocks WHERE location = ?");
            stmt.setString(1, location);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("type");
        } catch (SQLException e) {
            LogUtil.error("An error occurred while checking if a block is a custom block: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void addCustomBlock(String location, String type) {
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement stmt = con.prepareStatement("INSERT INTO custom_blocks (location, type, updated_on, data) VALUES (?, ?, ?, ?)");
            stmt.setString(1, location);
            stmt.setString(2, type);
            stmt.setLong(3, System.currentTimeMillis());
            stmt.setString(4, "{}");
            stmt.executeUpdate();
        } catch (SQLException e) {
            LogUtil.error("An error occurred while adding a custom block: " + e.getMessage());
        }
    }

    @Override
    public void removeCustomBlock(String location) {
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement stmt = con.prepareStatement("DELETE FROM custom_blocks WHERE location = ?");
            stmt.setString(1, location);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LogUtil.error("An error occurred while removing a custom block: " + e.getMessage());
        }
    }

    @Override
    public void updateCustomBlock(CustomBlocks block) {
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement stmt = con.prepareStatement("UPDATE custom_blocks SET updated_on = ?, data = ? WHERE location = ?");
            stmt.setLong(1, block.getUpdatedOn());
            stmt.setString(2, block.getData());
            stmt.setString(3, block.getLocation());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LogUtil.error("An error occurred while updating a custom block: " + e.getMessage());
        }
    }

    @Override
    public void updateIronFarmStatus(String location, boolean isActive) {
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement stmt = con.prepareStatement("UPDATE custom_blocks SET data = ? WHERE location = ? AND type = 'iron_farm_block'");
            String newData = "{ \"active\": " + isActive + " }";
            stmt.setString(1, newData);
            stmt.setString(2, location);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LogUtil.error("An error occurred while updating the iron farm status: " + e.getMessage());
        }
    }

    @Override
    public void updateFarmerBlock(String location, String cropType) {
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement stmt = con.prepareStatement("UPDATE custom_blocks SET data = ? WHERE location = ? AND type = 'farmer_block'");
            String newData = "{ \"cropType\": \"" + cropType + "\" }";
            stmt.setString(1, newData);
            stmt.setString(2, location);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LogUtil.error("An error occurred while updating the farmer block: " + e.getMessage());
        }
    }


    @Override
    public List<CustomBlocks> getAllCustomBlocks() {
        List<CustomBlocks> customBlocks = null;
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM custom_blocks");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) customBlocks.add(new CustomBlocks(rs.getString("location"), rs.getString("type"), rs.getLong("updated_on"), rs.getString("data")));

        } catch (SQLException e) {
            LogUtil.error("An error occurred while getting all custom blocks: " + e.getMessage());
        }
        return customBlocks;
    }
}
