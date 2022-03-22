package tw.ssr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SideButton extends JButton {
    private Mode mode;
    private UMLEditor ue;

    public SideButton(String text) {
        super(text);
        this.setBackground(Color.LIGHT_GRAY);
        this.setForeground(Color.BLACK);
        this.addMouseListener(new MyMouseAdapter());
    }

    public SideButton(String text, Mode mode, UMLEditor ue) {
        this(text);
        this.mode = mode;
        this.ue = ue;
    }

    public Mode getMode() {
        return mode;
    }

    class MyMouseAdapter extends MouseAdapter {
        public MyMouseAdapter() {
            super();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            if (e.getButton() == MouseEvent.BUTTON1) {
                ue.setMode(mode);
                ue.setOtherBtnColor();
            }
        }
    }
}
