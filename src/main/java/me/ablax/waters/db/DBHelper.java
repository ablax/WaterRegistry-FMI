package me.ablax.waters.db;

import me.ablax.waters.frames.GenericTable;
import me.ablax.waters.utils.Resolvable;
import me.ablax.waters.utils.Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

public class DBHelper {


    private static final DatabaseConfiguration config;
    private static List<Connection> connections = new CopyOnWriteArrayList<>();

    static {
        final Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("database.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        config = new DatabaseConfiguration(properties);
        loadConnections(20);
        initDB();
    }


    public static void initDB() {
        final String dbInitQuery = "CREATE TABLE IF NOT EXISTS STATES(\n" +
                "STATE_ID BIGINT IDENTITY NOT NULL,\n" +
                "STATE NVARCHAR(20) NOT NULL UNIQUE,\n" +
                "AREA INT NOT NULL,\n" +
                "POPULATION BIGINT CHECK(POPULATION>0),\n" +
                "CONSTRAINT PK_STATE_ID PRIMARY KEY(STATE_ID)\n" +
                ");\n" +
                "CREATE TABLE IF NOT EXISTS WATER_BODY(\n" +
                "WATER_ID BIGINT IDENTITY NOT NULL,\n" +
                "STATE_ID BIGINT NOT NULL,\n" +
                "NAME NVARCHAR(20) NOT NULL,\n" +
                "AREA DECIMAL(5,2) NOT NULL,\n" +
                "DEPTH DECIMAL(5,2) NOT NULL,\n" +
                "CONSTRAINT PK_WATER_ID PRIMARY KEY(WATER_ID),\n" +
                "CONSTRAINT FK_STATES_STATE_ID FOREIGN KEY (STATE_ID) REFERENCES STATES (STATE_ID)\n" +
                ");\n" +
                "CREATE TABLE IF NOT EXISTS SUPERVISOR(\n" +
                "SUPERVISOR_ID BIGINT IDENTITY NOT NULL,\n" +
                "WATER_ID BIGINT NULL,\n" +
                "FNAME NVARCHAR(20) NOT NULL,\n" +
                "LNAME NVARCHAR(20) NOT NULL,\n" +
                "COMMENT NVARCHAR(200) NOT NULL,\n" +
                "CONSTRAINT PK_SUPERVISOR_ID PRIMARY KEY(SUPERVISOR_ID),\n" +
                "CONSTRAINT FK_WATER_BODY_WATER_ID FOREIGN KEY(WATER_ID) REFERENCES WATER_BODY (WATER_ID)\n" +
                ");";

        Utils.runAsync(() -> {
            try (final Connection connection = getConnection();
                 final PreparedStatement preparedStatement = connection.prepareStatement(dbInitQuery)) {
                preparedStatement.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public static GenericTable getAllData(final String tableName, final Resolvable... resolvables) {

        final String sql = "select * from " + tableName;
        try (final Connection conn = getConnection();
             final PreparedStatement preparedStatement = conn.prepareStatement(sql);
             final ResultSet resultSet = preparedStatement.executeQuery();) {
            return new GenericTable(resultSet, Arrays.asList(resolvables));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void loadConnections(int amount) {
        for (int i = 0; i < amount; i++) {
            connections.add(doConnect());
        }
    }

    public static Connection doConnect() {
        try {
            Class.forName(config.getDriver());
            return DriverManager.getConnection(config.getConnectionUrl(), config.getUsername(), config.getPassword());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Connection getConnection() {
        if (connections.size() <= 15) {
            loadConnections(5);
        }
        return connections.remove(0);
    }

}
