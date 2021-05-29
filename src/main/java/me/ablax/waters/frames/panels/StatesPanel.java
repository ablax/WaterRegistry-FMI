package me.ablax.waters.frames.panels;

import me.ablax.waters.db.DBHelper;
import me.ablax.waters.frames.GenericTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Murad Hamza on 29.5.2021 г.
 */
public class StatesPanel extends JPanel{

    Connection conn = null;
    PreparedStatement state = null;
    JTable table = new JTable();
    JScrollPane scroller = new JScrollPane(table);
    int id = -1;
    ResultSet result = null;

    JPanel upPanel = new JPanel();
    JPanel midPanel = new JPanel();
    JPanel downPanel = new JPanel();

    JLabel fnameL = new JLabel("Име:");
    JLabel lnameL = new JLabel("Фамилия:");
    JLabel sexL = new JLabel("Пол:");
    JLabel ageL = new JLabel("Години:");
    JLabel salaryL = new JLabel("Заплата:");

    JTextField fnameTF = new JTextField();
    JTextField lnameTF = new JTextField();
    JTextField ageTF = new JTextField();
    JTextField salaryTF = new JTextField();

    String[] item = {"Мъж", "Жена"};
    JComboBox<String> sexCombo = new JComboBox<String>(item);

    JButton addBtn = new JButton("Добавяне");
    JButton deleteBtn = new JButton("Изтриване");
    JButton editBtn = new JButton("Редактиране");
    JButton searchBtn = new JButton("Търси");
    JComboBox<String> searchCombo = new JComboBox<>();

    private final ActionListener addAction = arg0 -> {
        conn = DBHelper.getConnection();
        String sql = "insert into person values(null,?,?,?,?,?)";
        try {
            state = conn.prepareStatement(sql);
            state.setString(1, fnameTF.getText());
            state.setString(2, lnameTF.getText());
            state.setString(3, sexCombo.getSelectedItem().toString());
            state.setByte(4, Byte.parseByte(ageTF.getText()));
            state.setFloat(5, Float.parseFloat(salaryTF.getText()));

            state.execute();
            table.setModel(DBHelper.getAllData("WATER_BODY"));
            DBHelper.fillCombo(searchCombo);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                state.close();
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        clearForm();
    };
    private final ActionListener searchAction = e -> {
        String selectedItem = searchCombo.getSelectedItem().toString();
        String[] items = selectedItem.split(" ");
        int itemID = Integer.parseInt(items[0]);

        conn = DBHelper.getConnection();
        String sql = "select * from person where id=?";
        try {
            state = conn.prepareStatement(sql);
            state.setInt(1, itemID);
            result = state.executeQuery();
            table.setModel(new GenericTable(result));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    };
    private final ActionListener deleteAction = e -> {
        conn = DBHelper.getConnection();
        String sql = "delete from person where id=?";
        try {
            state = conn.prepareStatement(sql);
            state.setInt(1, id);
            state.execute();
            id = -1;
            table.setModel(DBHelper.getAllData("WATER_BODY"));
            DBHelper.fillCombo(searchCombo);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    };
    private MouseListener tableListener = new MouseListener() {

        @Override
        public void mouseClicked(MouseEvent e) {
            int row = table.getSelectedRow();
            id = Integer.parseInt(table.getValueAt(row, 0).toString());

        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

    };

    public StatesPanel() {

        //upPanel
        upPanel.setLayout(new GridLayout(5, 2));

        upPanel.add(fnameL);
        upPanel.add(fnameTF);
        upPanel.add(lnameL);
        upPanel.add(lnameTF);
        upPanel.add(sexL);
        upPanel.add(sexCombo);
        upPanel.add(ageL);
        upPanel.add(ageTF);
        upPanel.add(salaryL);
        upPanel.add(salaryTF);

        this.add(upPanel);

        //midPanel
        midPanel.add(addBtn);
        midPanel.add(deleteBtn);
        midPanel.add(editBtn);
        midPanel.add(searchCombo);
        midPanel.add(searchBtn);


        this.add(midPanel);

        addBtn.addActionListener(addAction);
        deleteBtn.addActionListener(deleteAction);
        DBHelper.fillCombo(searchCombo);
        searchBtn.addActionListener(searchAction);

        //downPanel
        this.add(downPanel);
        downPanel.add(scroller);
        scroller.setPreferredSize(new Dimension(450, 160));
        table.setModel(DBHelper.getAllData("WATER_BODY"));
        table.addMouseListener(tableListener);


    }//end constructor

    public void clearForm() {
        fnameTF.setText("");
        lnameTF.setText("");
        ageTF.setText("");
        salaryTF.setText("");
    }// end clearForm


}// end class MyFrame