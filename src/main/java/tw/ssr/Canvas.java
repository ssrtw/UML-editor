package tw.ssr;

import tw.ssr.Object.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Canvas extends JPanel {
    private Object2D scene;
    private UMLEditor ue;
    private ArrayList<Object2D> selected;
    private Vector startPos, currPos;
    private boolean signleSelect;
    private boolean isDragging;

    public Canvas() {
        super();
        signleSelect = false;
        isDragging = false;
        scene = new Group();
        selected = new ArrayList<>();
        startPos = new Vector();
        currPos = new Vector();
        this.addMouseListener(new CanvasMouseAdapter(this));
        this.addMouseMotionListener(new CanvasMouseAdapter(this));
        this.setBackground(Color.LIGHT_GRAY);
    }

    public Canvas(UMLEditor ue) {
        this();
        this.ue = ue;
    }

    public UMLEditor getUe() {
        return ue;
    }

    class CanvasMouseAdapter extends MouseAdapter {
        private Canvas canvas;

        public CanvasMouseAdapter(Canvas canvas) {
            this.canvas = canvas;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            isDragging = false;
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (canvas.getUe().getMode() == Mode.CREATE_CLASS) {
                    scene.getChildren().add(new ClassObject(e.getX(), e.getY(), scene, new Material(Color.BLACK, Color.WHITE)));
                } else if (canvas.getUe().getMode() == Mode.CREATE_USECASE) {
                    scene.getChildren().add(new UseCaseObject(e.getX(), e.getY(), scene, new Material(Color.BLACK, Color.WHITE)));
                }
                repaint();
            }
        }

        private void clearSelected() {
            selected.clear();
            signleSelect = false;
            for (Object2D obj : scene.getChildren()) {
                obj.setSelect(false);
            }
        }

        public void rayCasting() {
            Object2D intersectObj = null;
            for (int i = scene.getChildren().size() - 1; i >= 0; i--) {
                if (intersectObj == null && scene.getChildren().get(i).intersect(new Vector(startPos.getX(), startPos.getY()))) {
                    intersectObj = scene.getChildren().get(i);
                } else {
                    scene.getChildren().get(i).setSelect(false);
                }
            }
            selected.add(intersectObj);
            if (intersectObj != null) {
                signleSelect = true;
                intersectObj.setSelect(true);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            clearSelected();
            if (e.getButton() == MouseEvent.BUTTON1 && canvas.getUe().getMode() == Mode.SELECT) {
                startPos.setVec(e);
                rayCasting();
            } else {
                isDragging = false;
                signleSelect = false;
            }
            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            if (canvas.getUe().getMode() == Mode.SELECT) {
                if (signleSelect) {
                    isDragging = false;
                    int mx = e.getX() - startPos.getX();
                    int my = e.getY() - startPos.getY();
                    selected.get(0).move(mx, my);
                    startPos.setVec(e);
                    repaint();
                    return;
                }
                // 圈選
                isDragging = true;
                clearSelected();
                currPos.setVec(e);
                Vector leftTop = Vector.minPoint(startPos, currPos);
                Vector rightDown = Vector.maxPoint(startPos, currPos);
                for (Object2D obj : scene.getChildren()) {
                    if (obj.intersect(leftTop, rightDown)) {
                        obj.setSelect(true);
                    } else {
                        obj.setSelect(false);
                    }
                }
                repaint();
            }
        }
    }

    // https://www.itread01.com/content/1548889398.html
    // 應該用paintComponets，但是我覺得好像畫就好，我自己有z-index。(目前先做Array順序就好)
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        scene.render(g);
        if (isDragging) {
            Vector leftTop = Vector.minPoint(startPos, currPos);
            Vector size = startPos.clone().sub(currPos).abs();
            g.setColor(new Color(0, 128, 192, 128));
            g.fillRect(leftTop.getX(), leftTop.getY(), size.getX(), size.getY());
        }
    }
}
