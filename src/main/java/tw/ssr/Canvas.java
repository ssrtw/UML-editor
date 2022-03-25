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
    private boolean singleSelect;
    private boolean isDragging;

    public Canvas() {
        super();
        singleSelect = false;
        isDragging = false;
        // TODO: 先用override的，之後可能得重想這邊的繼承關係
        // scene不應該畫出Group的邊界
        scene = new Group() {
            @Override
            public void render(Graphics g) {
                for (Object2D child : children) {
                    child.render(g);
                }
            }
        };
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

    public void renameSelected(String name) {
        if (singleSelect) {
            selected.get(0).setName(name);
            repaint();
        }
    }

    public void groupSelected() {
        if (selected.size() > 1) {
            Group group = new Group();
            for (Object2D obj : selected) {
                // 更改物件的父節點
                group.getChildren().add(obj);
                scene.getChildren().remove(obj);
                // 移除select狀態
                obj.setSelect(false);
            }
            group.grouping();
            group.setParent(scene);
            scene.getChildren().add(group);
            selected.clear();
        }
        repaint();
    }

    public void ungroupSelected() {
        for (Object2D obj : selected)
            if (obj.getIsGroup()) {
                Group group = (Group) obj;
                group.ungrouping();
            }
        repaint();
    }

    public ArrayList<Object2D> getSelected() {
        return selected;
    }

    public UMLEditor getUe() {
        return ue;
    }

    class CanvasMouseAdapter extends MouseAdapter {
        private Canvas canvas;

        public CanvasMouseAdapter(Canvas canvas) {
            this.canvas = canvas;
        }

        private void clearSelected() {
            selected.clear();
            singleSelect = false;
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
                singleSelect = true;
                intersectObj.setSelect(true);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            isDragging = false;
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (canvas.getUe().getMode() == Mode.CREATE_CLASS) {
                    scene.getChildren().add(new ClassObject(e.getX(), e.getY(), scene, new Material(Color.BLACK, Color.WHITE)));
                } else if (canvas.getUe().getMode() == Mode.CREATE_USECASE) {
                    scene.getChildren().add(new UseCaseObject(e.getX(), e.getY(), scene, new Material(Color.BLACK, Color.WHITE)));
                }
                repaint();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            clearSelected();
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (canvas.getUe().getMode() == Mode.SELECT) {
                    startPos.setVec(e);
                    rayCasting();
                } else {
                    isDragging = false;
                    singleSelect = false;
                }
                repaint();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (canvas.getUe().getMode() == Mode.SELECT) {
                    if (singleSelect) {
                        isDragging = false;
                        int mx = e.getX() - startPos.getX();
                        int my = e.getY() - startPos.getY();
                        selected.get(0).move(mx, my);
                        startPos.setVec(e);
                        repaint();
                        return;
                    }
                    selected.clear();
                    // 圈選
                    isDragging = true;
                    clearSelected();
                    currPos.setVec(e);
                    Vector leftTop = Vector.minPoint(startPos, currPos);
                    Vector rightDown = Vector.maxPoint(startPos, currPos);
                    for (Object2D obj : scene.getChildren()) {
                        if (obj.intersect(leftTop, rightDown)) {
                            obj.setSelect(true);
                            selected.add(obj);
                        } else {
                            obj.setSelect(false);
                        }
                    }
                    repaint();
                }
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
