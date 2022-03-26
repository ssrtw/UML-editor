package tw.ssr.Object;

import tw.ssr.Vector;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

abstract public class Object2D {
    protected static int portCnt = 4;
    protected static int zIndexNxt = 0;
    protected int zIndex;
    protected Vector pos;
    protected Vector size;
    protected Material mat;
    protected String name;
    /* Todo: 目前把ports的位置用世界座標儲存
        可能有必要重新設計?
    */
    protected Vector ports[];
    protected Object2D parent;
    protected boolean selected;
    protected boolean isGroup;
    protected ArrayList<Object2D> children;

    public Object2D() {
        zIndex = zIndexNxt++;
        pos = new Vector();
        size = new Vector();
        mat = new Material();
        name = "";
        ports = new Vector[portCnt];
        for (int i = 0; i < ports.length; i++)
            ports[i] = new Vector();
        parent = null;
        isGroup = false;
        selected = false;
        children = new ArrayList<>();
    }

    public Object2D(Object2D parent, Material mat) {
        this();
        this.parent = parent;
        this.mat = mat;
    }

    public Object2D(int x, int y, int w, int h, Object2D parent, Material mat) {
        this(parent, mat);
        this.size.setVec(w, h);
        this.pos.setVec(x, y);
        calcPortPos();
    }

    public Object2D(MouseEvent e, int w, int h, Object2D parent, Material mat) {
        this(parent, mat);
        this.pos.setVec(e);
        this.size.setVec(w, h);
        calcPortPos();
    }

    public void calcPortPos() {
        // Todo: port是世界座標，要想想怎麼改成local的
        Vector objWorldPos = sumParentPosition();
        Vector mid = objWorldPos.clone().add(size.clone().scale(0.5));
        this.ports[0].setVec(mid.x, objWorldPos.y);// 上
        this.ports[1].setVec(mid.x, objWorldPos.y + size.y);// 下
        this.ports[2].setVec(objWorldPos.x, mid.y);// 左
        this.ports[3].setVec(objWorldPos.x + size.x, mid.y);// 右
    }

    public void setSelect(boolean isSelect) {
        selected = isSelect;
    }

    public boolean getIsGroup() {
        return isGroup;
    }

    public void move(int x, int y) {
        this.pos.add(x, y);
        calcPortPos();
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Object2D> getChildren() {
        return children;
    }

    public void setParent(Object2D parent) {
        this.parent = parent;
    }

    public Vector sumParentPosition() {
        Vector position = pos.clone();
        Object2D ancestor = parent;
        while (ancestor != null) {
            position.add(ancestor.pos);
            ancestor = ancestor.parent;
        }
        return position;
    }

    // 將一個場景中的向量轉至當前坐標系
    public Vector reCoordinateVec(Vector worldVec) {
        Vector position = worldVec.clone();
        Object2D ancestor = parent;
        while (ancestor != null) {
            position.sub(ancestor.pos);
            ancestor = ancestor.parent;
        }
        return position;
    }

    // Object2D help to draw port when selected
    public void render(Graphics g) {
        // draw port
        if (selected) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(pos.x + size.x / 2 - 5, pos.y - 10, 10, 10); // top
            g2.drawRect(pos.x + size.x / 2 - 5, pos.y + size.y, 10, 10); // down
            g2.drawRect(pos.x - 10, pos.y + size.y / 2 - 5, 10, 10); // left
            g2.drawRect(pos.x + size.x, pos.y + size.y / 2 - 5, 10, 10); // right
        }
        calcPortPos();
    }

    public abstract boolean intersect(Vector mouse);

    // 全數的框選都適用這個case
    public boolean intersect(Vector v1, Vector v2) {
        // 完全包含
        return v1.x <= pos.x && v1.y <= pos.y && v2.x >= (pos.x + size.x) && v2.y >= (pos.y + size.y);
    }

    public Vector getNearPort(Vector mousePos) {
        Vector nearPoint = ports[0];
        double distance = mousePos.distance(nearPoint);
        // 找四個點距離最近的
        for (int i = 1; i < ports.length; i++) {
            double check = mousePos.distance(ports[i]);
            if (check < distance) {
                nearPoint = ports[i];
                distance = check;
            }
        }
        return nearPoint;
    }
}