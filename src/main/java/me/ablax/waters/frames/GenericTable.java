package me.ablax.waters.frames;

import me.ablax.waters.utils.Resolvable;

import javax.swing.table.AbstractTableModel;
import java.io.Serial;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class GenericTable extends AbstractTableModel {

    @Serial
    private static final long serialVersionUID = 28944513L;
    private static final String EMPTY_STRING = "";
    private final AtomicInteger rowCount = new AtomicInteger(0);
    private final List<String> columnNames = new ArrayList<>();
    private final List<Object[]> data = new ArrayList<>();
    private final List<Object[]> dataWithoutId = new ArrayList<>();

    private final Map<String, Resolvable> resolvables = new HashMap<>();

    public GenericTable(final ResultSet result) throws SQLException {
        final ResultSetMetaData metaData = result.getMetaData();
        final int columns = metaData.getColumnCount();
        for (int i = 1; i <= columns; i++) {
            this.columnNames.add(metaData.getColumnLabel(i));
        }
        this.setRS(result);
    }

    public GenericTable(final ResultSet result, final List<Resolvable> resolvableList) throws SQLException {
        for (final Resolvable resolvable : resolvableList) {
            resolvables.put(resolvable.getName(), resolvable);
        }
        final ResultSetMetaData metaData = result.getMetaData();
        final int columns = metaData.getColumnCount();
        for (int i = 1; i <= columns; i++) {
            this.columnNames.add(metaData.getColumnLabel(i));
        }
        this.setRS(result);
    }

    public Long getId(final int rowIndex) {
        return (Long) data.get(rowIndex)[0];
    }

    public Object[] getObject(final int rowIndex) {
        return dataWithoutId.get(rowIndex);
    }

    public void setRS(final ResultSet result) throws SQLException {
        while (result.next()) {
            final List<Object> values = new ArrayList<>();
            for (final String columnName : this.columnNames) {
                final Object value;
                if (resolvables.containsKey(columnName)) {
                    final Resolvable resolvable = resolvables.get(columnName);
                    value = resolvable.getNameFunction().apply(result.getLong(columnName));
                } else {
                    value = result.getObject(columnName);
                }
                values.add(value);
            }
            this.data.add(values.toArray());
            values.remove(0);
            this.dataWithoutId.add(values.toArray());
            this.rowCount.incrementAndGet();
        }
        columnNames.remove(0);
    }

    @Override
    public int getColumnCount() {
        return this.columnNames.size();
    }

    @Override
    public int getRowCount() {
        return this.rowCount.get();
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        return this.dataWithoutId.get(rowIndex)[columnIndex];
    }

    @Override
    public String getColumnName(final int columnIndex) {
        try {
            final String columnName = this.columnNames.get(columnIndex);
            if (resolvables.containsKey(columnName)) {
                return resolvables.get(columnName).getDisplayName();
            }
            return columnName;
        } catch (Exception e) {
            e.printStackTrace();
            return EMPTY_STRING;
        }
    }
}