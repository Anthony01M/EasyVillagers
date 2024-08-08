package xyz.berrystudios.easyvillagers.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import xyz.berrystudios.easyvillagers.database.dao.customBlocks.CustomBlocksDaoImpl;
import xyz.berrystudios.easyvillagers.database.service.CustomBlocksService;
import xyz.berrystudios.easyvillagers.database.table.TableManager;
import xyz.berrystudios.easyvillagers.util.misc.LogUtil;
import xyz.berrystudios.easyvillagers.EasyVillagers;

@Getter
public class SQL {
    private static SQL instance;

    @Setter
    private HikariDataSource dataSource;

    // Services
    @Getter
    private CustomBlocksService customBlocksService;

    private SQL() {
        instance = this;

        synchronized (this) {
            if(getDataSource() != null && !getDataSource().isClosed()) return;

            HikariConfig hikariConfig = getHikariConfig();
            dataSource = new HikariDataSource(hikariConfig);
            LogUtil.info("Connected to database");

            // Handle creation of tables
            LogUtil.info("Starting database migration...");
            TableManager tableManager = new TableManager(dataSource);
            TableManager.getTableClasses().forEach(tableManager::persist);
            LogUtil.info("Database migration complete");

            // Setup Services
            customBlocksService = new CustomBlocksService(new CustomBlocksDaoImpl(dataSource));
        }
    }

    private static @NotNull HikariConfig getHikariConfig() {
        HikariConfig hikariConfig = new HikariConfig();
        String jdbcUrl = getJdbcUrl();
        hikariConfig.setJdbcUrl(jdbcUrl);
        if (EasyVillagers.getInstance().getCfile().database_type.equalsIgnoreCase("mysql")) {
            hikariConfig.setUsername(EasyVillagers.getInstance().getCfile().database_username);
            hikariConfig.setPassword(EasyVillagers.getInstance().getCfile().database_password);
            hikariConfig.addDataSourceProperty("useSSL", "false");
            hikariConfig.addDataSourceProperty("serverTimezone", "UTC");
        }
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.setLeakDetectionThreshold(5000); //5 seconds or else warn us about a potential leak
        return hikariConfig;
    }

    private static @NotNull String getJdbcUrl() {
        String dbType = EasyVillagers.getInstance().getCfile().database_type.toLowerCase();
        return switch (dbType) {
            case "mysql" ->
                    "jdbc:mysql://" + EasyVillagers.getInstance().getCfile().database_host + ":" + EasyVillagers.getInstance().getCfile().database_port + "/" + EasyVillagers.getInstance().getCfile().database_database;
            case "sqlite" ->
                    "jdbc:sqlite:" + EasyVillagers.getInstance().getDataFolder().getAbsolutePath() + "/database.db";
            default -> throw new IllegalArgumentException("Invalid database type: " + dbType);
        };
    }

    public static SQL getInstance() {
        if (instance == null) {
            instance = new SQL();
        }
        return instance;
    }
}
