package tw.ssr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class ModelPanel extends JPanel implements MouseMotionListener {
    private Canvas canvas;
    private int mouseX, mouseY;

    public ModelPanel() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                mouseX = me.getX();
                mouseY = me.getY();
            }
        });
        this.addMouseMotionListener(this);
    }

    public ModelPanel(Canvas canvas,int posX,int posY) {
        this();
        this.canvas = canvas;
        this.setBounds(posX,posY,50,80);
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        if (canvas.getUe().getMode() != Mode.SELECT) return;
        me.translatePoint(me.getComponent().getLocation().x - mouseX, me.getComponent().getLocation().y - mouseY);
        this.setLocation(me.getX(), me.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
