package xyz.berrystudios.easyvillagers.database.table;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import lombok.Getter;
import lombok.Setter;
import xyz.berrystudios.easyvillagers.database.table.annotations.Column;
import xyz.berrystudios.easyvillagers.database.table.annotations.Table;
import xyz.berrystudios.easyvillagers.util.misc.LogUtil;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.*;
import java.util.*;

public class TableManager {
    private final DataSource dataSource;

    public TableManager(DataSource dataSource) {
        this.dataSource = dataSource;

        String migrationTable = "CREATE TABLE IF NOT EXISTS `migrations` (" +
                "`id` INTEGER AUTO_INCREMENT NOT NULL, " +
                "`table_name` TEXT NOT NULL, " +
                "`version` DOUBLE NOT NULL, " +
                "`fields` VARCHAR(2000) NOT NULL," +
                "`date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "PRIMARY KEY (`id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;";

        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()) {
                statement.execute(migrationTable);
            } catch (SQLException e) {
                LogUtil.error("Failed to create migrations table" + e);
            }
    }

    public void persist(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = clazz.getAnnotation(Table.class);
            Field[] fields = clazz.getDeclaredFields();

            if (fields.length == 0) {
                throw new IllegalArgumentException("Class " + clazz.getName() + " has no fields!");
            }

            Migration migration = getLatestMigration(table.name());
            LogUtil.console("Checking table " + table.name() + " for migration...");
            if (migration != null && migration.getVersion() == table.version()) {
                LogUtil.console("Table " + table.name() + " is up-to-date!");
                return; // Table is up-to-date
            }

            Migration newMigration = new Migration(table.name(), table.version(), new Timestamp(System.currentTimeMillis()));
            StringBuilder queryBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                    .append(table.name()).append(" (");

            Column primaryColumn = null;
            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    String sqlType = getSqlTypeFromField(field);

                    if (sqlType == null) {
                        throw new IllegalArgumentException("Field " + field.getName() + " in class " + clazz.getName() + " has an invalid type!");
                    }

                    MigrationField migrationField = new MigrationField();
                    // Column name & type
                    queryBuilder.append("`").append(column.name()).append("`").append(" ");
                    migrationField.setName(column.name());
                    migrationField.setType(sqlType);

                    if (column.primary()) {
                        queryBuilder.append("INTEGER AUTO_INCREMENT NOT NULL");
                        primaryColumn = column;
                    } else {
                        queryBuilder.append(sqlType);
                        if (column.length() > 0) {
                            queryBuilder.append("(").append(column.length()).append(")");
                            migrationField.setLength(column.length());
                        } else migrationField.setLength(-1);

                        if (!column.defaultValue().isEmpty()) {
                            queryBuilder.append(" DEFAULT ").append(column.defaultValue());
                            migrationField.setDefaultValue(column.defaultValue());
                        }
                        if (column.unique()) {
                            queryBuilder.append(" UNIQUE");
                            migrationField.setUnique(true);
                        }
                        if (column.autoIncrement()) {
                            queryBuilder.append(" AUTO_INCREMENT");
                            migrationField.setAutoIncrement(true);
                        }
                        if (column.nullable()) {
                            queryBuilder.append(" NULL");
                            migrationField.setNullable(true);
                        } else {
                            queryBuilder.append(" NOT NULL");
                        }
                    }
                    queryBuilder.append(", ");
                    newMigration.addField(migrationField);
                }
            }
            queryBuilder
                    .append(primaryColumn != null ? "primary key(" + primaryColumn.name() + ")" : "")
                    .append(") ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;");

            try(Connection con = dataSource.getConnection()) {
                con.prepareStatement(queryBuilder.toString()).execute();

                if (migration == null || migration.getVersion() != table.version()) {
                    if (migration != null && !migration.equals(newMigration)) {
                        newMigration.migrate(con, migration);
                        LogUtil.info("Migrated table " + table.name() + " to version " + table.version() + "!");
                    }

                    PreparedStatement statement = con.prepareStatement("INSERT INTO `migrations` (`table_name`, `version`, `fields`) VALUES (?, ?, ?)");
                    statement.setString(1, table.name());
                    statement.setDouble(2, table.version());
                    statement.setString(3, newMigration.serializeFields());
                    statement.execute();
                    LogUtil.info("Inserting table " + table.name() + " migration version " + table.version() + " to database");
                }
            } catch (SQLException err) {
                LogUtil.error("Failed to persist table " + table.name() + "!" + err);
            }
        }
    }

    private Migration getLatestMigration(String name) {
        try(Connection con = dataSource.getConnection()) {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM `migrations` WHERE `table_name` = ? ORDER BY `version` DESC LIMIT 1;");
            statement.setString(1, name);
            ResultSet set = statement.executeQuery();

            if (set.next()) {
                Migration migration = new Migration(set.getString("table_name"),
                        set.getDouble("version"),
                        set.getTimestamp("date"));

                migration.deserializeFields(set.getString("fields"));

                return migration;
            }
        } catch (SQLException err) {
            LogUtil.error("Failed to get latest migration for table " + name + "!" + err);
        }

        return null;
    }

    private String getSqlTypeFromField(Field field) {
        if (String.class.equals(field.getType())) {
            return "TEXT";
        } else if (Integer.class.equals(field.getType()) || int.class.equals(field.getType())) {
            return "INTEGER";
        } else if (Long.class.equals(field.getType()) || long.class.equals(field.getType())) {
            return "BIGINT";
        } else if (Double.class.equals(field.getType()) || double.class.equals(field.getType())) {
            return "DOUBLE";
        } else if (Float.class.equals(field.getType()) || float.class.equals(field.getType())) {
            return "FLOAT";
        } else if (Boolean.class.equals(field.getType()) || boolean.class.equals(field.getType())) {
            return "BOOLEAN";
        } else if (Byte.class.equals(field.getType()) || byte.class.equals(field.getType())) {
            return "TINYINT";
        } else if (Short.class.equals(field.getType()) || short.class.equals(field.getType())) {
            return "SMALLINT";
        } else if (Character.class.equals(field.getType()) || char.class.equals(field.getType())) {
            return "CHAR";
        } else if (Byte[].class.equals(field.getType())) {
            return "BLOB";
        } else if (Date.class.equals(field.getType()) ||
                Time.class.equals(field.getType()) ||
                Timestamp.class.equals(field.getType())) {
            return "TIMESTAMP";
        }

        return null;
    }

    public static Set<Class<?>> getTableClasses() {
        Set<Class<?>> classes = new HashSet<>();
        try (ScanResult scanResult = new ClassGraph()
                .enableClassInfo()
                .enableAnnotationInfo()
                .whitelistPackages("xyz.berrystudios.essentials.database.model")
                .scan()) {
            scanResult.getClassesWithAnnotation(Table.class.getName()).forEach(classInfo -> {
                try {
                    classes.add(Class.forName(classInfo.getName()));
                } catch (ClassNotFoundException e) {
                    LogUtil.console(e.getMessage());
                }
            });
        }
        return classes;
    }
/*
    public static List<Class<?>> getTableClasses() {
        String packageName = "xyz.berrystudios.essentials.database.model";
        List<Class<?>> tableClasses = new ArrayList<>();
        String packagePath = packageName.replace('.', '/');

        // Check if the resource exists
        java.net.URL resource = TableManager.class.getClassLoader().getResource(packagePath);
        if (resource == null) {
            LogUtil.error("Resource not found for path: " + packagePath);
            return tableClasses; // return empty list or handle accordingly
        }

        File packageDirectory = new File(resource.getFile());
        if (packageDirectory.isDirectory()) {
            File[] files = packageDirectory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".class")) {
                        String className = packageName + '.' + file.getName().substring(0, file.getName().lastIndexOf('.'));
                        try {
                            Class<?> clazz = Class.forName(className);
                            if (clazz.isAnnotationPresent(Table.class)) {
                                tableClasses.add(clazz);
                            }
                        } catch (ClassNotFoundException e) {
                            LogUtil.error("There was an error while checking for class: " + e);
                        }
                    }
                }
            }
        }
        return tableClasses;
    }
*/
    @Getter
    private static class Migration {
        private final String tableName;
        private final double version;
        private final List<MigrationField> fields;
        private final Timestamp date;

        public Migration(String tableName, double version, Timestamp date) {
            this.tableName = tableName;
            this.version = version;
            this.fields = new ArrayList<>();
            this.date = date;
        }

        public void addField(MigrationField field) {
            fields.add(field);
        }

        public boolean hasField(String tableName) {
            return fields.stream().anyMatch(f -> f.getName().equals(tableName));
        }

        public MigrationField getField(String tableName) {
            return fields.stream().filter(f -> f.getName().equals(tableName)).findFirst().orElse(null);
        }

        public void deserializeFields(String fields) {
            ObjectMapper mapper = new ObjectMapper();

            try {
                String[] split = fields.split(",,,--,,,");
                for (String field : split) {
                    MigrationField migrationField = mapper.readValue(field, MigrationField.class);
                    this.fields.add(migrationField);
                }
            } catch (JsonProcessingException err) {
                LogUtil.error("Failed to deserialize fields for migration!" + err);
            }
        }

        public String serializeFields() {
            ObjectMapper mapper = new ObjectMapper();
            StringBuilder stringBuilder = new StringBuilder();

            try {
                for (MigrationField field : this.fields) {
                    stringBuilder.append(mapper.writeValueAsString(field)).append(",,,--,,,");
                }
            } catch (JsonProcessingException err) {
                LogUtil.error("Failed to serialize fields for migration!" + err);
            }

            return stringBuilder.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Migration migration = (Migration) o;
            return Double.compare(migration.version, version) == 0 &&
                    tableName.equals(migration.tableName) &&
                    fields.stream().allMatch(f -> {
                        MigrationField field = migration.getField(f.getName());
                        return field != null && field.equals(f);
                    });
        }

        public void migrate(Connection con, Migration oldMigration) throws SQLException {
            con.setAutoCommit(false);
            Statement statement = con.createStatement();

            oldMigration.fields.stream().filter(f -> !this.hasField(f.getName())).forEach(f -> {
                try {
                    statement.executeUpdate("ALTER TABLE `" + tableName + "` DROP COLUMN `" + f.getName() + "`");
                } catch (SQLException err) {
                    LogUtil.error("Failed to drop column " + f.getName() + " from table " + tableName + "!" + err);
                }
            });

            for (MigrationField field : fields) {
                MigrationField oldField = oldMigration.getField(field.getName());
                if (oldField == null) {
                    StringBuilder queryBuilder = new StringBuilder("ALTER TABLE `")
                            .append(tableName).append("` ADD COLUMN `")
                            .append(field.getName()).append("` ")
                            .append(field.getType());

                    if (field.getLength() > 0) {
                        queryBuilder.append("(").append(field.getLength()).append(")");
                    }

                    if (field.getDefaultValue() != null && !field.getDefaultValue().equals("")) {
                        queryBuilder.append(" DEFAULT ").append(field.getDefaultValue());
                    }

                    if (field.isUnique()) {
                        queryBuilder.append(" UNIQUE");
                    }

                    if (field.isAutoIncrement()) {
                        queryBuilder.append(" AUTO_INCREMENT");
                    }

                    if (field.isNullable()) {
                        queryBuilder.append(" NULL");
                    } else {
                        queryBuilder.append(" NOT NULL");
                    }

                    try {
                        statement.executeUpdate(queryBuilder.toString());
                    } catch (SQLException err) {
                        LogUtil.error("Failed to add column " + field.getName() + " to table " + tableName + "!" + err);
                    }
                } else if (!oldField.equals(field)) {
                    StringBuilder queryBuilder = new StringBuilder("ALTER TABLE `")
                            .append(tableName).append("` ALTER COLUMN `")
                            .append(field.getName()).append("` ")
                            .append(field.getType());

                    if (field.getLength() > 0) {
                        queryBuilder.append("(").append(field.getLength()).append(")");
                    }

                    if (field.getDefaultValue() != null && !field.getDefaultValue().equals("")) {
                        queryBuilder.append(" DEFAULT ").append(field.getDefaultValue());
                    }

                    if (field.isUnique()) {
                        queryBuilder.append(" UNIQUE");
                    }

                    if (field.isAutoIncrement()) {
                        queryBuilder.append(" AUTO_INCREMENT");
                    }

                    if (field.isNullable()) {
                        queryBuilder.append(" NULL");
                    } else {
                        queryBuilder.append(" NOT NULL");
                    }

                    try {
                        statement.executeUpdate(queryBuilder.toString());
                    } catch (SQLException err) {
                        LogUtil.error("Failed to modify column " + field.getName() + " in table " + tableName + "!" + err);
                    }
                }
            }

            con.commit();
            con.setAutoCommit(true);
        }
    }

    @Getter
    @Setter
    private static class MigrationField {
        private String name;
        private String type;
        private int length;
        private boolean nullable;
        private boolean unique;
        private boolean autoIncrement;
        private String defaultValue;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MigrationField that = (MigrationField) o;
            return length == that.length &&
                    nullable == that.nullable &&
                    unique == that.unique &&
                    autoIncrement == that.autoIncrement &&
                    name.equals(that.name) &&
                    type.equals(that.type) &&
                    defaultValue.equals(that.defaultValue);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, type, length, nullable, unique, autoIncrement, defaultValue);
        }
    }
}
