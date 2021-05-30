package me.ablax.waters.db.repositories;

import me.ablax.waters.db.DBHelper;
import me.ablax.waters.frames.GenericTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Murad Hamza on 29.5.2021 г.
 */
public class StateRepository {

    public static void addState(final String stateName, final int area, final int population) {
        final String sql = "insert into STATES (STATE_NAME, AREA, POPULATION ) values(?,?,?)";
        try (final Connection connection = DBHelper.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, stateName);
            preparedStatement.setInt(2, area);
            preparedStatement.setInt(3, population);

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


    public static List<String> findAllNames() {
        final List<String> names = new ArrayList<>();
        final String sql = "select STATE_NAME from STATES";
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

    public static String getNameById(final long id) {
        final String sql = "select STATE_NAME from STATES WHERE STATE_ID = ?";
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
        final String sql = "select STATE_ID from STATES WHERE STATE_NAME = ?";
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


}
