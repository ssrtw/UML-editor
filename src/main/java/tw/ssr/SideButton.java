package tw.ssr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SideButton extends JButton {
    private Mode mode;
    UMLEditor ue;

    public SideButton(String text) {
        super(text);
        this.setBackground(Color.LIGHT_GRAY);
        this.setForeground(Color.BLACK);
        this.addMouseListener(new SideButtonMouseAdapter(this));
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

    public void changeColor(Mode mode) {
        if (this.mode == mode) {
            setBackground(Color.GRAY);
            setForeground(Color.WHITE);
        } else {
            setBackground(Color.LIGHT_GRAY);
            setForeground(Color.BLACK);
        }
    }
}

class SideButtonMouseAdapter extends MouseAdapter {
    private SideButton sb;

    public SideButtonMouseAdapter() {
        super();
    }

    public SideButtonMouseAdapter(SideButton sb) {
        super();
        this.sb=sb;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        if (e.getButton() == MouseEvent.BUTTON1) {
            sb.ue.setMode(sb.getMode());
            sb.ue.setOtherBtnColor();
        }
    }
}
