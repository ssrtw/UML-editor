package tw.ssr;

import java.awt.*;
import javax.swing.*;

public class UMLEditor {
    private static int minWidth = 1000, minHeight = 800;
    private JFrame mainFrame;
    private String[] btnName;
    private SideButton[] sideBarBtns;
    private CanvasView view;
    private CanvasModel model;
    private CanvasController controller;
    private Mode mode;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu editMenu;
    private JMenuItem renameMenuItem, groupMenuItem, ungroupMenuItem;

    public UMLEditor() {
        mainFrame = new JFrame("UML editor");
        btnName = new String[] { "select", "association", "generalization", "composition", "class", "use case" };
        sideBarBtns = new SideButton[btnName.length];
        view = new CanvasView();
        model = new CanvasModel();
        controller = new CanvasController();
        view.setController(controller);
        controller.setUMLEditor(this);
        controller.setView(view);
        controller.setModel(model);

        for (int i = 0; i < btnName.length; i++)
            sideBarBtns[i] = new SideButton(btnName[i], Mode.values()[i], this);

        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");
        renameMenuItem = new JMenuItem("Rename");
        groupMenuItem = new JMenuItem("Group");
        ungroupMenuItem = new JMenuItem("Ungroup");
    }

    public JFrame getMainJFrame() {
        return mainFrame;
    }

    public JPanel getView() {
        return view;
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

        mainFrame.add(view, panelConstraints);

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
        // 初始化時，設為select模式非null
        mode = Mode.SELECT;
        setOtherBtnColor();

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        editMenu.add(renameMenuItem);
        renameMenuItem.addActionListener(e -> {
            if (controller.getSelected().size() == 1) {
                String newName = JOptionPane.showInputDialog(mainFrame, "Input new name", "Rename",
                        JOptionPane.INFORMATION_MESSAGE);
                if (newName != null)
                    controller.renameSelected(newName);
            }
        });
        editMenu.add(groupMenuItem);
        groupMenuItem.addActionListener(e -> {
            controller.groupSelected();
        });
        editMenu.add(ungroupMenuItem);
        ungroupMenuItem.addActionListener(e -> {
            controller.ungroupSelected();
        });
        mainFrame.setJMenuBar(menuBar);

        mainFrame.setVisible(true);
    }

    public void setOtherBtnColor() {
        for (SideButton btn : sideBarBtns) {
            btn.changeColor(mode);
        }
    }
}
