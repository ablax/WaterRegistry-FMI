package me.ablax.waters.db.repositories;

import me.ablax.waters.db.DBHelper;
import me.ablax.waters.frames.GenericTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Murad Hamza on 30.5.2021 г.
 */
public class WaterRepository {

    public static void addState(final String name, final int waterArea, final int waterDepth) {
        final String sql = "insert into WATER_BODY (NAME, WATER_AREA, WATER_DEPTH ) values(?,?,?)";
        try (final Connection connection = DBHelper.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, waterArea);
            preparedStatement.setInt(3, waterDepth);

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(final long stateId) {
        final String sql = "DELETE FROM STATES WHERE STATE_ID = ?";
        try (final Connection connection = DBHelper.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, stateId);

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void editState(final long selectedId, final String stateName, final int area, final int population) {
        final String sql = "UPDATE STATES SET STATE_NAME=?, AREA=?, POPULATION=? WHERE STATE_ID=?";
        try (final Connection connection = DBHelper.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, stateName);
            preparedStatement.setInt(2, area);
            preparedStatement.setInt(3, population);
            preparedStatement.setLong(4, selectedId);

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static GenericTable search(final String selectedItem, final String searchFor) {
        final String searchWord;
        final boolean isString = selectedItem.equalsIgnoreCase("STATE_NAME");
        if (isString) {
            searchWord = " LIKE ";
        } else {
            searchWord = " = ";
        }

        final String sql = "select * from STATES where " + selectedItem + searchWord + "?";
        try (final Connection conn = DBHelper.getConnection();
        ) {
            final PreparedStatement state = conn.prepareStatement(sql);
            if (isString) {
                state.setString(1, "%" + searchFor + "%");
            } else {
                state.setInt(1, Integer.parseInt(searchFor));
            }
            final ResultSet result = state.executeQuery();

            return new GenericTable(result);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;
    }

//    public static void fillCombo(JComboBox<String> combo) {
//
//        final String sql = "select SUPERVISOR_ID, fname from STATES";
//        try (final Connection conn = DBHelper.getConnection();
//             final PreparedStatement preparedStatement = conn.prepareStatement(sql);
//             final ResultSet resultSet = preparedStatement.executeQuery();) {
//
//
//            combo.removeAllItems();
//            while (resultSet.next()) {
//                String item = resultSet.getObject(1).toString() + " " + resultSet.getObject(2);
//                combo.addItem(item);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }

}
