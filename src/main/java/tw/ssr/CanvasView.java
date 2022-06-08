package tw.ssr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CanvasView extends JPanel implements MouseListener, MouseMotionListener {
    // 這個屬性必須存在，否則在框選時無法確定選到一個的時候要做什麼處理
    CanvasController controller;

    public CanvasView() {
        super();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setBackground(Color.LIGHT_GRAY);
    }

    public void setController(CanvasController controller) {
        this.controller = controller;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        controller.paint(g);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        controller.doMousePressed(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        controller.doMouseDragged(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        controller.doMouseReleased(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}