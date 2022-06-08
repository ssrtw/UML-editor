package tw.ssr;

import tw.ssr.Line.*;
import tw.ssr.Object.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

public class CanvasController {
    MouseMode mouseMode;
    CanvasView view;
    CanvasModel model;
    ArrayList<Object2D> selected;
    Vector startPos, currPos;
    UMLEditor ue;

    public CanvasController() {
        mouseMode = MouseMode.NONE;
        startPos = new Vector();
        currPos = new Vector();
        selected = new ArrayList<>();
    }

    public CanvasController(UMLEditor ue, CanvasModel model, CanvasView view) {
        this();
        this.ue = ue;
        this.model = model;
        this.view = view;
    }

    public void setUMLEditor(UMLEditor ue) {
        this.ue = ue;
    }

    public void setView(CanvasView view) {
        this.view = view;
    }

    public void setModel(CanvasModel model) {
        this.model = model;
    }

    public UMLEditor getUe() {
        return ue;
    }

    public Vector getStartPos() {
        return startPos;
    }

    public Vector getCurrPos() {
        return currPos;
    }

    public ArrayList<Object2D> getSelected() {
        return selected;
    }

    public MouseMode getMouseMode() {
        return mouseMode;
    }

    public void setMouseMode(MouseMode mouseMode) {
        this.mouseMode = mouseMode;
    }

    private void clearSelected() {
        selected.clear();
        for (Object2D obj : model.scene.getChildren()) {
            obj.setSelect(false);
        }
    }

    public void renameSelected(String name) {
        if (selected.size() == 1 && !selected.get(0).getIsGroup()) {
            selected.get(0).setName(name);
        }
        view.repaint();
    }

    public void groupSelected() {
        if (selected.size() > 1) {
            Group group = new Group();
            for (Object2D obj : selected) {
                // 更改物件的父節點
                group.getChildren().add(obj);
                model.scene.getChildren().remove(obj);
                // 移除select狀態
                obj.setSelect(false);
            }
            group.grouping();
            group.setParent(model.scene);
            model.scene.getChildren().add(group);
            selected.clear();
        }
        view.repaint();
    }

    public void ungroupSelected() {
        for (Object2D obj : selected)
            if (obj.getIsGroup()) {
                Group group = (Group) obj;
                group.ungrouping();
            }
        view.repaint();
    }

    // https://www.itread01.com/content/1548889398.html
    public void paint(Graphics g) {
        model.scene.render(g);
        for (Line line : model.lines) {
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
            default -> {
            }
        }
    }

    void doMousePressed(MouseEvent e) {
        clearSelected();
        if (SwingUtilities.isLeftMouseButton(e)) {
            Object2D selectedObj;
            switch (ue.getMode()) {
                case SELECT -> {
                    startPos.setVec(e);
                    selectedObj = rayCasting(startPos);
                    // 如果有選到東西
                    if (selectedObj != null) {
                        mouseMode = MouseMode.SINGLE_SELECT;
                        selectedObj.setSelect(true);
                        selected.add(selectedObj);
                    } else {
                        // 如果沒有選到東西，代表應該會做框選
                        setMouseMode(MouseMode.BOX_SELECT);
                        getCurrPos().setVec(getStartPos());
                    }
                }
                case ASSOCIATION_LINE, GENERALIZATION_LINE, COMPOSITION_LINE -> {
                    startPos.setVec(e);
                    currPos.setVec(e);
                    selectedObj = rayCasting(startPos);
                    if (selectedObj != null) {
                        // 選到了物件，要開始畫線
                        mouseMode = MouseMode.DRAW_LINE;
                        selected.add(selectedObj);
                    } else {
                        // 沒有選到物件就不做事
                        mouseMode = MouseMode.NONE;
                    }
                }
                default -> {
                }
            }
            view.repaint();
        }
    }

    void doMouseDragged(MouseEvent e) {
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
                    for (Object2D obj : model.scene.getChildren()) {
                        if (obj.intersect(leftTop, rightDown)) {
                            obj.setSelect(true);
                            selected.add(obj);
                        } else {
                            obj.setSelect(false);
                        }
                    }
                }
                default -> {
                }
            }
            view.repaint();
        }
    }

    void doMouseReleased(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            Mode mode = ue.getMode();
            if (mouseMode == MouseMode.NONE) {
                if (mode == Mode.CREATE_CLASS) {
                    model.scene.getChildren()
                            .add(new ClassObject(e, model.scene, new Material(Color.BLACK, Color.WHITE)));
                } else if (mode == Mode.CREATE_USECASE) {
                    model.scene.getChildren()
                            .add(new UseCaseObject(e, model.scene, new Material(Color.BLACK, Color.WHITE)));
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
                        if (mode == Mode.ASSOCIATION_LINE) {
                            model.lines.add(new AssociationLine(firstNearPort, secondNearPort));
                        } else if (mode == Mode.GENERALIZATION_LINE) {
                            model.lines.add(new GeneralizationLine(firstNearPort, secondNearPort));
                        } else if (mode == Mode.COMPOSITION_LINE) {
                            model.lines.add(new CompositionLine(firstNearPort, secondNearPort));
                        }
                    }
                }
            }
        }
        mouseMode = MouseMode.NONE;
        startPos.setVec(0, 0);
        currPos.setVec(0, 0);
        view.repaint();
    }

    private Object2D rayCasting(Vector mousePos) {
        Object2D intersectObj = null;
        for (int i = model.scene.getChildren().size() - 1; i >= 0; i--) {
            if (model.scene.getChildren().get(i).intersect(mousePos))
                intersectObj = model.scene.getChildren().get(i);
        }
        return intersectObj;
    }
}
