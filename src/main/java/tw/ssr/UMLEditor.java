package tw.ssr;

import java.awt.*;
import javax.swing.*;

enum Mode {
    SELECT, ASSOCIATION, GENERALIZATION_LINE, COMPOSITION_LINE, CREATE_CLASS, CREATE_USECASE
}

public class UMLEditor {
    private static int minWidth = 1000, minHeight = 800;
    private JFrame mainFrame;
    private String[] btnName;
    private SideButton[] sideBarBtns;
    private Canvas canvas;
    private Mode mode;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu editMenu;
    private JMenuItem size;

    public UMLEditor() {
        mainFrame = new JFrame();
        btnName = new String[]{"select", "association", "generalization", "composition", "class", "use case"};
        sideBarBtns = new SideButton[btnName.length];
        canvas = new Canvas(this);

        for (int i = 0; i < btnName.length; i++)
            sideBarBtns[i] = new SideButton(btnName[i], Mode.values()[i], this);

        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");
        size = new JMenuItem("size");
    }

    public JFrame getMainJFrame() {
        return mainFrame;
    }

    public JPanel getCanvas() {
        return canvas;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void run() {
        mainFrame.setSize(minWidth, minHeight);
        mainFrame.setMinimumSize(new Dimension(minWidth, minHeight));
        mainFrame.setLayout(new GridBagLayout());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GridBagConstraints panelConstraints = new GridBagConstraints();
        panelConstraints.gridx = 1;
        panelConstraints.gridy = 0;
        panelConstraints.gridwidth = 7;
        panelConstraints.gridheight = 12;
        panelConstraints.weightx = 1;
        panelConstraints.weighty = 1;
        panelConstraints.fill = GridBagConstraints.BOTH;

        mainFrame.add(canvas, panelConstraints);

        GridBagConstraints sideBarConstraints = new GridBagConstraints();
        for (int i = 0; i < btnName.length; i++) {
            sideBarConstraints.gridx = 0;
            sideBarConstraints.gridy = i * 2;
            sideBarConstraints.gridwidth = 1;
            sideBarConstraints.gridheight = 2;
            sideBarConstraints.weighty = 1;
            sideBarConstraints.fill = GridBagConstraints.VERTICAL;

            mainFrame.add(sideBarBtns[i], sideBarConstraints);
        }
        fileMenu.add(size);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        mainFrame.setJMenuBar(menuBar);

        mainFrame.setVisible(true);
    }

    public void setOtherBtnColor() {
        for (int i = 0; i < sideBarBtns.length; i++) {
            if (sideBarBtns[i].getMode() != mode) {
                sideBarBtns[i].setBackground(Color.LIGHT_GRAY);
                sideBarBtns[i].setForeground(Color.BLACK);
            } else {
                sideBarBtns[i].setBackground(Color.GRAY);
                sideBarBtns[i].setForeground(Color.WHITE);
            }
        }
    }
}
