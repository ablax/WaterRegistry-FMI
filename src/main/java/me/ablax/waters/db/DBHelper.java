package me.ablax.waters.db;

import me.ablax.waters.frames.GenericTable;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DBHelper {


    private static final DatabaseConfiguration config;
    static GenericTable model = null;

    static {
        final Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("database.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        config = new DatabaseConfiguration(properties);
        initDB();
    }

    public static void initDB() {
        final String dbInitQuery = "CREATE TABLE IF NOT EXISTS STATES(\n" +
                "STATE_ID BIGINT IDENTITY NOT NULL,\n" +
                "STATE_NAME NVARCHAR(20) NOT NULL,\n" +
                "AREA INT NOT NULL,\n" +
                "POPULATION BIGINT CHECK(POPULATION>0), --ооф check навсякъде... или в програмата?\n" +
                "CONSTRAINT PK_STATE_ID PRIMARY KEY(STATE_ID)\n" +
                ");\n" +
                "CREATE TABLE IF NOT EXISTS WATER_BODY(\n" +
                "WATER_ID BIGINT IDENTITY NOT NULL,\n" +
                "STATE_ID BIGINT NOT NULL,\n" +
                "NAME NVARCHAR(20) NOT NULL,\n" +
                "WATER_AREA DECIMAL(5,2) NOT NULL,\n" +
                "WATER_DEPTH DECIMAL(5,2) NOT NULL,\n" +
                "CONSTRAINT PK_WATER_ID PRIMARY KEY(WATER_ID),\n" +
                "CONSTRAINT FK_STATES_STATE_ID FOREIGN KEY (STATE_ID) REFERENCES STATES (STATE_ID)\n" +
                ");\n" +
                "CREATE TABLE IF NOT EXISTS SUPERVISOR(\n" +
                "SUPERVISOR_ID BIGINT IDENTITY NOT NULL,\n" +
                "WATER_ID BIGINT NULL, --null понеже като е нов, не отговаря на никой язовир\n" +
                "FNAME NVARCHAR(20) NOT NULL,\n" +
                "LNAME NVARCHAR(20) NOT NULL,\n" +
                "SUPERVISOR_COMMENT NVARCHAR(200) NOT NULL,\n" +
                "CONSTRAINT PK_SUPERVISOR_ID PRIMARY KEY(SUPERVISOR_ID),\n" +
                "CONSTRAINT FK_WATER_BODY_WATER_ID FOREIGN KEY(WATER_ID) REFERENCES WATER_BODY (WATER_ID)\n" +
                ");";

        try {
            final Connection connection = getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement(dbInitQuery);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public static void fillCombo(JComboBox<String> combo) {

        final Connection conn = getConnection();
        String sql = "select SUPERVISOR_ID ,fname from SUPERVISOR";
        try {
            final PreparedStatement preparedStatement = conn.prepareStatement(sql);
            final ResultSet resultSet = preparedStatement.executeQuery();
            combo.removeAllItems();
            while (resultSet.next()) {
                String item = resultSet.getObject(1).toString() + " " + resultSet.getObject(2);
                combo.addItem(item);
            }//end while
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }//end fillCombo

    public static GenericTable getAllData(String tableName) {

        final Connection conn = getConnection();
        String sql = "select * from " + tableName;
        try {
            final PreparedStatement preparedStatement = conn.prepareStatement(sql);
            final ResultSet resultSet = preparedStatement.executeQuery();
            model = new GenericTable(resultSet);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return model;

    }

    public static Connection getConnection() {

        try {
            Class.forName(config.getDriver());
            return DriverManager.getConnection(config.getConnectionUrl(), config.getUsername(), config.getPassword());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
