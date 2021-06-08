package me.ablax.waters.db.repositories;

import me.ablax.waters.db.DBHelper;
import me.ablax.waters.frames.GenericTable;
import me.ablax.waters.frames.MainFrame;
import me.ablax.waters.utils.Resolvable;
import me.ablax.waters.utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Murad Hamza on 30.5.2021 г.
 */
public class WaterRepository {

    public static void addWater(final String name, final Double waterArea, final Double waterDepth, final Long stateId) {
        final String sql = "insert into WATER_BODY (NAME, AREA, DEPTH, STATE_ID) values(?,?,?,?)";
        try (final Connection connection = DBHelper.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, waterArea);
            preparedStatement.setDouble(3, waterDepth);
            preparedStatement.setLong(4, stateId);

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MainFrame.updateTables();
        }
    }

    public static void editWater(final long selectedId, final String name, final Double waterArea, final Double waterDepth, final Long stateId) {
        final String sql = "UPDATE WATER_BODY SET NAME=?, AREA=?, DEPTH=?, STATE_ID=? WHERE WATER_ID=?";
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
        } finally {
            MainFrame.updateTables();
        }
    }

    public static void delete(final long stateId) throws SQLException {
        final String sql = "DELETE FROM WATER_BODY WHERE WATER_ID = ?";
        try (final Connection connection = DBHelper.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, stateId);

            preparedStatement.execute();
        } finally {
            MainFrame.updateTables();
        }
    }

    public static String getNameById(final long id) {
        final String sql = "select NAME from WATER_BODY WHERE WATER_ID = ?";
        try (final Connection conn = DBHelper.getConnection();
        ) {
            final PreparedStatement state = conn.prepareStatement(sql);
            state.setLong(1, id);
            final ResultSet result = state.executeQuery();

            if (result.next()) {
                return result.getString(1);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public static Long getIdByName(final String name) {
        final List<String> names = new ArrayList<>();
        final String sql = "select WATER_ID from WATER_BODY WHERE NAME = ?";
        try (final Connection conn = DBHelper.getConnection();
        ) {
            final PreparedStatement state = conn.prepareStatement(sql);
            state.setString(1, name);
            final ResultSet result = state.executeQuery();

            if (result.next()) {
                return result.getLong(1);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return -1L;
    }


    public static List<String> findAllNames() {
        final List<String> names = new ArrayList<>();
        final String sql = "select NAME from WATER_BODY";
        try (final Connection conn = DBHelper.getConnection();
        ) {
            final PreparedStatement state = conn.prepareStatement(sql);
            final ResultSet result = state.executeQuery();

            while (result.next()) {
                names.add(result.getString(1));
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return names;
    }

    public static GenericTable search(final String selectedItem, final String searchFor, final String selectedItem2, final String searchFor2, final Resolvable... resolvables) {
        final String searchWord;
        final boolean isString = selectedItem.equalsIgnoreCase("NAME");
        if (isString) {
            searchWord = " LIKE ";
        } else {
            searchWord = " = ";
        }

        final String searchWord2;
        final boolean isString2 = selectedItem2.equalsIgnoreCase("NAME");
        if (isString2) {
            searchWord2 = " LIKE ";
        } else {
            searchWord2 = " = ";
        }

        final String sql = "select * from WATER_BODY where " + selectedItem + searchWord + "?" + (selectedItem.equalsIgnoreCase(selectedItem2) ? " OR " : " AND ") + selectedItem2 + searchWord2 + "?";
        try (final Connection conn = DBHelper.getConnection();
        ) {
            final PreparedStatement state = conn.prepareStatement(sql);
            if (isString) {
                state.setString(1, "%" + searchFor + "%");
            } else {
                state.setInt(1, Utils.getInt(searchFor));
            }
            if (isString2) {
                state.setString(2, "%" + searchFor2 + "%");
            } else {
                state.setInt(2, Utils.getInt(searchFor2));
            }
            final ResultSet result = state.executeQuery();

            return new GenericTable(result, Arrays.asList(resolvables));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;
    }


}
