package tw.ssr;

import tw.ssr.Line.AssociationLine;
import tw.ssr.Line.CompositionLine;
import tw.ssr.Line.GeneralizationLine;
import tw.ssr.Line.Line;
import tw.ssr.Object.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

enum MouseMode {
    NONE, SINGLE_SELECT, BOX_SELECT, DRAW_LINE
}

public class Canvas extends JPanel {
    private Object2D scene;
    private UMLEditor ue;
    private ArrayList<Object2D> selected;
    private ArrayList<Line> lines;
    private Vector startPos, currPos;
    // 這個屬性必須存在，否則在框選時無法確定選到一個的時候要做什麼處理
    private MouseMode mouseMode;

    public Canvas() {
        super();
        mouseMode = MouseMode.NONE;
        // TODO: 我覺得scene是特例，因此用override，之後可能得重想這邊的繼承關係
        scene = new Object2D() {
            @Override
            public void render(Graphics g) {
                for (Object2D child : children) {
                    child.render(g);
                }
            }

            @Override
            public boolean intersect(Vector mouse) {
                return false;
            }
        };
        selected = new ArrayList<>();
        lines = new ArrayList<>();
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
        if (mouseMode == MouseMode.SINGLE_SELECT) {
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
            for (Object2D obj : scene.getChildren()) {
                obj.setSelect(false);
            }
        }

        public Object2D rayCasting(Vector mousePos) {
            Object2D intersectObj = null;
            for (int i = scene.getChildren().size() - 1; i >= 0; i--) {
                if (scene.getChildren().get(i).intersect(mousePos))
                    intersectObj = scene.getChildren().get(i);
            }
            return intersectObj;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            clearSelected();
            if (SwingUtilities.isLeftMouseButton(e)) {
                switch (canvas.getUe().getMode()) {
                    case SELECT -> {
                        startPos.setVec(e);
                        Object2D selectedObj = rayCasting(startPos);
                        // 如果有選到東西
                        if (selectedObj != null) {
                            mouseMode = MouseMode.SINGLE_SELECT;
                            selectedObj.setSelect(true);
                            selected.add(selectedObj);
                        } else {
                            // 如果沒有選到東西，代表應該會做框選
                            mouseMode = MouseMode.BOX_SELECT;
                            currPos.setVec(startPos);
                        }
                    }
                    case ASSOCIATION_LINE, GENERALIZATION_LINE, COMPOSITION_LINE -> {
                        startPos.setVec(e);
                        currPos.setVec(e);
                        Object2D selectedObj = rayCasting(startPos);
                        if (selectedObj != null) {
                            // 選到了物件，要開始畫線
                            mouseMode = MouseMode.DRAW_LINE;
                            selected.add(selectedObj);
                        } else {
                            // 沒有選到物件就不做事
                            mouseMode = MouseMode.NONE;
                        }
                    }
                }
                repaint();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            currPos.setVec(e);
            if (SwingUtilities.isLeftMouseButton(e)) {
                switch (mouseMode) {
                    case SINGLE_SELECT -> {
                        Vector motion = currPos.sub(startPos);
                        selected.get(0).move(motion.x, motion.y);
                        startPos.setVec(e);
                    }
                    case BOX_SELECT -> {
                        // 圈選
                        clearSelected();
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
                    }
                }
                repaint();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            if (SwingUtilities.isLeftMouseButton(e)) {
                Mode mode = canvas.getUe().getMode();
                if (mouseMode == MouseMode.NONE) {
                    if (mode == Mode.CREATE_CLASS) {
                        scene.getChildren().add(new ClassObject(e, scene, new Material(Color.BLACK, Color.WHITE)));
                    } else if (mode == Mode.CREATE_USECASE) {
                        scene.getChildren().add(new UseCaseObject(e, scene, new Material(Color.BLACK, Color.WHITE)));
                    }
                } else if (mouseMode == MouseMode.DRAW_LINE) {
                    Vector currPos = new Vector(e);
                    // 拿第一個物件
                    if (selected.size() == 1 && !selected.get(0).getIsGroup()) {
                        Object2D firstObj = selected.get(0);
                        Object2D secondObj = rayCasting(currPos);
                        // 如果其中一個是group的話就不該被連線，並且第二個物件不該是null
                        if (secondObj != null && !secondObj.getIsGroup()) {
                            Vector firstNearPort = firstObj.getNearPort(startPos);
                            Vector secondNearPort = secondObj.getNearPort(currPos);
                            switch (mode) {
                                case ASSOCIATION_LINE -> lines.add(new AssociationLine(firstNearPort, secondNearPort));
                                case GENERALIZATION_LINE -> lines.add(new GeneralizationLine(firstNearPort, secondNearPort));
                                case COMPOSITION_LINE -> lines.add(new CompositionLine(firstNearPort, secondNearPort));
                            }
                        }
                    }
                }
                mouseMode = MouseMode.NONE;
                startPos.setVec(0, 0);
                currPos.setVec(0, 0);
                repaint();
            }
        }
    }

    // https://www.itread01.com/content/1548889398.html
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        scene.render(g);
        for (Line line : lines) {
            line.render(g);
        }
        switch (mouseMode) {
            case BOX_SELECT -> {
                Vector leftTop = Vector.minPoint(startPos, currPos);
                Vector size = startPos.clone().sub(currPos).abs();
                g.setColor(new Color(0, 128, 192, 128));
                g.fillRect(leftTop.getX(), leftTop.getY(), size.getX(), size.getY());
            }
            case DRAW_LINE -> {
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(3));
                g2.drawLine(startPos.x, startPos.y, currPos.x, currPos.y);
            }
        }
    }
}
