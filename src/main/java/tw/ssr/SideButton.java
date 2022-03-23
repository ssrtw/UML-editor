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
        this.addMouseListener(new SideButtonMouseAdapter());
        ImageIcon ii = new ImageIcon("src/main/resources/" + text + ".png");
        Image img = ii.getImage();
        Image resizeImg = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        ii = new ImageIcon(resizeImg);  // transform it back
        // https://stackoverflow.com/a/354866
        setVerticalTextPosition(SwingConstants.BOTTOM);
        setHorizontalTextPosition(SwingConstants.CENTER);
        setFocusable(false);
        setBorderPainted(false);
        setRolloverEnabled(true);
        this.setIcon(ii);
    }

    public SideButton(String text, Mode mode, UMLEditor ue) {
        this(text);
        this.mode = mode;
        this.ue = ue;
    }

    public Mode getMode() {
        return mode;
    }

    class SideButtonMouseAdapter extends MouseAdapter {
        public SideButtonMouseAdapter() {
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
