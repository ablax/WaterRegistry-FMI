package me.ablax.waters.frames;

import javax.swing.table.AbstractTableModel;
import java.io.Serial;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class GenericTable extends AbstractTableModel {

    @Serial
    private static final long serialVersionUID = 28944513L;
    private static final String EMPTY_STRING = "";
    private final AtomicInteger rowCount = new AtomicInteger(0);
    private final List<String> columnNames = new ArrayList<>();
    private final List<Object[]> data = new ArrayList<>();
    private final List<Object[]> dataWithoutId = new ArrayList<>();

    public GenericTable(final ResultSet result) throws SQLException {
        final ResultSetMetaData metaData = result.getMetaData();
        final int columns = metaData.getColumnCount();
        for (int i = 1; i <= columns; i++) {
            this.columnNames.add(metaData.getColumnLabel(i));
        }
        this.setRS(result);
    }

    public void setRS(final ResultSet result) throws SQLException {

        while (result.next()) {
            final List<Object> values = new ArrayList<>();
            for (final String columnName : this.columnNames) {
                values.add(result.getObject(columnName));
            }
            this.data.add(values.toArray());
            values.remove(0);
            this.dataWithoutId.add(values.toArray());
            this.rowCount.incrementAndGet();
        }
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
        return this.data.get(rowIndex)[columnIndex];
    }

    @Override
    public String getColumnName(final int columnIndex) {
        try {
            return this.columnNames.get(columnIndex);
        } catch (Exception e) {
            e.printStackTrace();
            return EMPTY_STRING;
        }
    }
}