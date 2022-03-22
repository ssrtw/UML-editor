package tw.ssr;

import java.awt.*;

public class ClassPanel extends ModelPanel {
    public ClassPanel() {

    }

    public ClassPanel(Canvas canvas, int posX, int posY) {
        super(canvas, posX, posY);
        this.setSize(50, 80);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawRoundRect(0, 0, 50, 80, 3, 3);
    }
}
