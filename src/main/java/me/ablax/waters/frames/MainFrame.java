package me.ablax.waters.frames;

import me.ablax.waters.frames.panels.StatesPanel;

import javax.swing.*;



/**
 * @author Murad Hamza on 29.5.2021 г.
 */
public class MainFrame extends JFrame {

    public MainFrame() {
        this.setSize(500, 600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);


        final JTabbedPane tab = new JTabbedPane();

        tab.add(new StatesPanel(), "Области");

        this.add(tab);

        this.setVisible(true);

    }


}
