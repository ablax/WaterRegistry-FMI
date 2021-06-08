package me.ablax.waters.frames;

import me.ablax.waters.frames.panels.StatesPanel;
import me.ablax.waters.frames.panels.SupervisorPanel;
import me.ablax.waters.frames.panels.UpdateableJPanel;
import me.ablax.waters.frames.panels.WaterPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Murad Hamza on 29.5.2021 г.
 */
public class MainFrame extends JFrame {

    public static List<UpdateableJPanel> panels = new ArrayList<>();

    public MainFrame() {
        final StatesPanel statesPanel = new StatesPanel();
        final WaterPanel waterPanel = new WaterPanel();
        final SupervisorPanel supervisorPanel = new SupervisorPanel();

        panels.add(statesPanel);
        panels.add(waterPanel);
        panels.add(supervisorPanel);

        this.setSize(475, 325);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);


        final JTabbedPane tab = new JTabbedPane();

        tab.add(statesPanel, "Области");
        tab.add(waterPanel, "Водоеми");
        tab.add(supervisorPanel, "Отговорници");

        this.add(tab);

        this.setVisible(true);

    }

    public static void updateTables() {
        panels.forEach(UpdateableJPanel::reloadTable);
    }


}
