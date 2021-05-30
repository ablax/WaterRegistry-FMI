package me.ablax.waters.frames;

import me.ablax.waters.frames.panels.StatesPanel;
import me.ablax.waters.frames.panels.SupervisorPanel;
import me.ablax.waters.frames.panels.WaterPanel;

import javax.swing.*;



/**
 * @author Murad Hamza on 29.5.2021 г.
 */
public class MainFrame extends JFrame {

    public MainFrame() {
        final StatesPanel statesPanel = new StatesPanel();
        final WaterPanel waterPanel = new WaterPanel();
        final SupervisorPanel supervisorPanel = new SupervisorPanel();

        this.setSize(500, 600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);


        final JTabbedPane tab = new JTabbedPane();

        tab.add(statesPanel, "Области");
        tab.add(waterPanel, "Водоеми");
        tab.add(supervisorPanel, "Отговорници");

        this.add(tab);

        this.setVisible(true);

    }


}
