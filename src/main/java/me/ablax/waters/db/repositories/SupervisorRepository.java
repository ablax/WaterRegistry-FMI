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
public class SupervisorRepository {

    public static void addSupervisor(final String firstName, final String lastName, final String comment, final Long waterId) {
        final String sql = "insert into SUPERVISOR (FNAME, LNAME, SUPERVISOR_COMMENT, WATER_ID) values(?,?,?,?)";
        try (final Connection connection = DBHelper.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, comment);
            preparedStatement.setLong(4, waterId);

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void editSupervisor(final long selectedId, final String firstName, final String lastName, final String comment, final Long waterId) {
        final String sql = "UPDATE SUPERVISOR SET FNAME=?, LNAME=?, SUPERVISOR_COMMENT=?, WATER_ID=? WHERE SUPERVISOR_ID=?";
        try (final Connection connection = DBHelper.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, comment);
            preparedStatement.setLong(4, waterId);
            preparedStatement.setLong(5, selectedId);

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(final long stateId) {
        final String sql = "DELETE FROM SUPERVISOR WHERE SUPERVISOR_ID = ?";
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
        final boolean isString = !selectedItem.equalsIgnoreCase("WATER_ID");
        if (isString) {
            searchWord = " LIKE ";
        } else {
            searchWord = " = ";
        }

        final String sql = "select * from SUPERVISOR where " + selectedItem + searchWord + "?";
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
