package tw.ssr;

import java.awt.*;

public class CasePanel extends ModelPanel {
    public CasePanel() {

    }

    public CasePanel(Canvas canvas,int posX,int posY) {
        super(canvas,posX,posY);
        this.setSize(70, 40);
        this.setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GRAY);
        g.fillOval(0,0,70,40);
    }
}
