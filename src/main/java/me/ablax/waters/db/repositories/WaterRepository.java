package me.ablax.waters.db.repositories;

import me.ablax.waters.db.DBHelper;
import me.ablax.waters.frames.GenericTable;
import me.ablax.waters.utils.Resolvable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * @author Murad Hamza on 30.5.2021 г.
 */
public class WaterRepository {

    public static void addWater(final String name, final Double waterArea, final Double waterDepth, final Long stateId) {
        final String sql = "insert into WATER_BODY (NAME, WATER_AREA, WATER_DEPTH, STATE_ID) values(?,?,?,?)";
        try (final Connection connection = DBHelper.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, waterArea);
            preparedStatement.setDouble(3, waterDepth);
            preparedStatement.setLong(4, stateId);

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void editWater(final long selectedId, final String name, final Double waterArea, final Double waterDepth, final Long stateId) {
        final String sql = "UPDATE WATER_BODY SET NAME=?, WATER_AREA=?, WATER_DEPTH=?, STATE_ID=? WHERE WATER_ID=?";
        try (final Connection connection = DBHelper.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, waterArea);
            preparedStatement.setDouble(3, waterDepth);
            preparedStatement.setLong(4, stateId);
            preparedStatement.setLong(5, selectedId);

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(final long stateId) {
        final String sql = "DELETE FROM WATER_BODY WHERE WATER_ID = ?";
        try (final Connection connection = DBHelper.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, stateId);

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static GenericTable search(final String selectedItem, final String searchFor, final Resolvable... resolvables) {
        final String searchWord;
        final boolean isString = selectedItem.equalsIgnoreCase("NAME");
        if (isString) {
            searchWord = " LIKE ";
        } else {
            searchWord = " = ";
        }

        final String sql = "select * from WATER_BODY where " + selectedItem + searchWord + "?";
        try (final Connection conn = DBHelper.getConnection();
        ) {
            final PreparedStatement state = conn.prepareStatement(sql);
            if (isString) {
                state.setString(1, "%" + searchFor + "%");
            } else {
                state.setInt(1, Integer.parseInt(searchFor));
            }
            final ResultSet result = state.executeQuery();

            return new GenericTable(result, Arrays.asList(resolvables));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;
    }


}
