package me.ablax.waters.frames.panels;

import me.ablax.waters.db.DBHelper;
import me.ablax.waters.db.repositories.StateRepository;
import me.ablax.waters.frames.GenericTable;
import me.ablax.waters.utils.Pair;
import me.ablax.waters.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Murad Hamza on 29.5.2021 г.
 */
public class StatesPanel extends UpdateableJPanel {

    private static final List<Pair<String, String>> fields = new ArrayList<>();
    private static final String TABLE_NAME = "STATES";

    static {
        fields.add(new Pair<>("STATE", "Област"));
        fields.add(new Pair<>("AREA", "Площ"));
        fields.add(new Pair<>("POPULATION", "Население"));
    }

    private final Map<String, JTextField> textFields = new HashMap<>();
    JTable table = new JTable();
    JScrollPane scroller = new JScrollPane(table);
    long selectedId = -1;
    final JComboBox<String> searchCombo = new JComboBox<>();
    final JTextField searchTextField = new JTextField();
    final JComboBox<String> searchCombo2 = new JComboBox<>();
    final JTextField searchTextField2 = new JTextField();

    private final ActionListener searchAction = e -> {
        final String selectedItem = searchCombo.getSelectedItem().toString();
        final String searchFor = searchTextField.getText();

        final String selectedItem2 = searchCombo2.getSelectedItem().toString();
        final String searchFor2 = searchTextField2.getText();

        final GenericTable selectedTable = StateRepository.search(selectedItem, searchFor, selectedItem2, searchFor2);
        this.table.setModel(selectedTable);
    };
    private final ActionListener addAction = args -> {
        final String stateName = textFields.get("STATE").getText();
        final int area = Integer.parseInt(textFields.get("AREA").getText());
        final int population = Integer.parseInt(textFields.get("POPULATION").getText());
        StateRepository.addState(stateName, area, population);
        reloadTable();
        clearForm();
    };
    private final ActionListener editAction = args -> {
        if (selectedId != 1) {
            final String stateName = textFields.get("STATE").getText();
            final int area = Integer.parseInt(textFields.get("AREA").getText());
            final int population = Integer.parseInt(textFields.get("POPULATION").getText());
            StateRepository.editState(selectedId, stateName, area, population);
            selectedId = -1;
            reloadTable();
            clearForm();
        }
    };
    private final ActionListener deleteAction = e -> {
        if (selectedId != -1) {
            try {
                StateRepository.delete(selectedId);
            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null, "Sorry but you can't delete this entry", "Cannot delete", JOptionPane.ERROR_MESSAGE);
                ;
            }
            selectedId = -1;
            reloadTable();
        }
    };
    private MouseListener tableListener = new MouseListener() {

        @Override
        public void mouseClicked(MouseEvent e) {
            int row = table.getSelectedRow();
            final GenericTable tableModel = (GenericTable) table.getModel();
            selectedId = tableModel.getId(row);

            Object[] objects = tableModel.getObject(row);
            for (int i = 0; i < objects.length; i++) {
                final Object object = objects[i];
                final String columnName = tableModel.getColumnName(i);

                textFields.get(columnName).setText(object.toString());
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {/**/}

        @Override
        public void mouseExited(MouseEvent e) {/**/}

        @Override
        public void mousePressed(MouseEvent e) {/**/}

        @Override
        public void mouseReleased(MouseEvent e) {/**/}

    };

    public StatesPanel() {
        final JButton addBtn = new JButton("Добавяне");
        final JButton deleteBtn = new JButton("Изтриване");
        final JButton editBtn = new JButton("Редактиране");
        final JButton searchBtn = new JButton("Търси");

        //Upanel
        final JPanel upPanel = new JPanel();
        upPanel.setLayout(new GridLayout(3, 2));

        for (final Pair<String, String> field : fields) {
            final String key = field.getA();
            final String value = field.getB();

            final JLabel label = new JLabel(value);
            final JTextField textField = new JTextField();
            upPanel.add(label);
            upPanel.add(textField);

            textFields.put(key, textField);
        }

        this.add(upPanel);//end Upanel

        //control buttons
        final JPanel controlButtons = new JPanel();
        controlButtons.add(addBtn);
        controlButtons.add(deleteBtn);
        controlButtons.add(editBtn);

        this.add(controlButtons);//control butons

        final JPanel searchOptions = new JPanel();
        searchOptions.setLayout(new GridLayout(1, 3));
        textFields.keySet().forEach(searchCombo::addItem);
        textFields.keySet().forEach(searchCombo2::addItem);
        searchOptions.add(searchCombo);
        searchOptions.add(searchTextField);
        searchOptions.add(searchCombo2);
        searchOptions.add(searchTextField2);
        searchOptions.add(searchBtn);

        this.add(searchOptions);

        addBtn.addActionListener(addAction);
        deleteBtn.addActionListener(deleteAction);
        editBtn.addActionListener(editAction);
        searchBtn.addActionListener(searchAction);

        this.add(scroller);
        scroller.setPreferredSize(new Dimension(450, 160));
        table.addMouseListener(tableListener);

        reloadTable();
    }

    public void clearForm() {
        textFields.values().forEach(jTextField -> jTextField.setText(""));
    }

    public void reloadTable() {
        Utils.runAsync(() -> table.setModel(DBHelper.getAllData(TABLE_NAME)));
    }

}