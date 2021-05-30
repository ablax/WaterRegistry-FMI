package me.ablax.waters.frames.panels;

import me.ablax.waters.db.DBHelper;
import me.ablax.waters.db.repositories.StateRepository;
import me.ablax.waters.db.repositories.WaterRepository;
import me.ablax.waters.frames.GenericTable;
import me.ablax.waters.utils.Pair;
import me.ablax.waters.utils.Resolvable;
import me.ablax.waters.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Murad Hamza on 30.5.2021 г.
 */
public class WaterPanel extends JPanel {

    private static final List<Pair<String, String>> fields = new ArrayList<>();
    private static final String TABLE_NAME = "WATER_BODY";
    private static final Resolvable[] resolvables = new Resolvable[]{new Resolvable("STATE_ID", "STATE", StateRepository::getNameById, StateRepository::getIdByName)};

    static {
        fields.add(new Pair<>("NAME", "Водоем"));
        fields.add(new Pair<>("WATER_AREA", "Площ"));
        fields.add(new Pair<>("WATER_DEPTH", "Дълбочина"));
    }

    final JComboBox<String> stateCombo = new JComboBox<>();
    final JComboBox<String> searchCombo = new JComboBox<>();
    final JTextField searchTextField = new JTextField();
    private final Map<String, JTextField> textFields = new HashMap<>();
    JTable table = new JTable();
    private final ActionListener searchAction = e -> {
        final String selectedItem = searchCombo.getSelectedItem().toString();
        final String searchItem = Arrays.stream(resolvables)
                .filter(resolvable -> resolvable.getDisplayName().equalsIgnoreCase(selectedItem))
                .findFirst()
                .map(Resolvable::getName)
                .orElse(selectedItem);
        final String searchtext = searchTextField.getText();
        final String searchFor = Arrays.stream(resolvables)
                .filter(resolvable -> resolvable.getName().equalsIgnoreCase(searchItem))
                .findFirst().map(resolvable -> resolvable.getIdFunction().apply(searchtext).toString())
                .orElse(searchtext);

        final GenericTable selectedTable = WaterRepository.search(searchItem, searchFor, resolvables);
        this.table.setModel(selectedTable);
    };
    private final ActionListener addAction = args -> {
        final Long stateId = StateRepository.getIdByName((String) stateCombo.getSelectedItem());
        final String name = textFields.get("NAME").getText();
        final double area = Double.parseDouble(textFields.get("WATER_AREA").getText());
        final double depth = Double.parseDouble(textFields.get("WATER_DEPTH").getText());
        WaterRepository.addWater(name, area, depth, stateId);
        reloadTable();
        clearForm();
    };
    JScrollPane scroller = new JScrollPane(table);
    long selectedId = -1;
    private final ActionListener editAction = args -> {
        if (selectedId != 1) {
            final Long stateId = StateRepository.getIdByName((String) stateCombo.getSelectedItem());
            final String name = textFields.get("NAME").getText();
            final double area = Double.parseDouble(textFields.get("WATER_AREA").getText());
            final double depth = Double.parseDouble(textFields.get("WATER_DEPTH").getText());
            WaterRepository.editWater(selectedId, name, area, depth, stateId);
            selectedId = -1;
            reloadTable();
            clearForm();
        }
    };
    private final ActionListener deleteAction = e -> {
        if (selectedId != -1) {
            WaterRepository.delete(selectedId);
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

                final JTextField textField = textFields.get(columnName);
                if (textField != null) {
                    textField.setText(object.toString());
                } else {
                    stateCombo.setSelectedItem(object.toString());
                }
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

    public WaterPanel() {
        final JButton addBtn = new JButton("Добавяне");
        final JButton deleteBtn = new JButton("Изтриване");
        final JButton editBtn = new JButton("Редактиране");
        final JButton searchBtn = new JButton("Търси");

        //Upanel
        final JPanel upPanel = new JPanel();
        upPanel.setLayout(new GridLayout(4, 2));

        StateRepository.findAllNames().forEach(stateCombo::addItem);

        upPanel.add(new JLabel("Област"));
        upPanel.add(stateCombo);
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
        for (final Resolvable resolvable : resolvables) {
            searchCombo.addItem(resolvable.getDisplayName());
        }
        textFields.keySet().forEach(searchCombo::addItem);
        searchOptions.add(searchCombo);
        searchOptions.add(searchTextField);
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

    private void reloadTable() {
        Utils.runAsync(() -> {
            final GenericTable allDataTable = DBHelper.getAllData(TABLE_NAME, resolvables);
            table.setModel(allDataTable);
        });
    }

}