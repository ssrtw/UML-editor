package tw.ssr.Object;

import tw.ssr.Vector;

import java.awt.*;
import java.util.ArrayList;

abstract public class Object2D {
    protected static int portCnt = 4;
    protected static int zIndexNxt = 0;
    protected int zIndex;
    protected Vector pos;
    protected Vector size;
    protected Material mat;
    protected String name;
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
        name = new String();
        ports = new Vector[portCnt];
        parent = null;
        isGroup = false;
        selected = false;
    }

    public Object2D(int x, int y, int w, int h, Object2D parent, Material mat) {
        this();
        this.pos.setVec(x, y);
        this.size.setVec(w, h);
        this.parent = parent;
        this.mat = mat;
    }

    public void setSelect(boolean isSelect) {
        selected = isSelect;
    }

    public boolean getIsGroup() {
        return isGroup;
    }

    public void move(int x, int y) {
        this.pos.setVec(pos.getX() + x, pos.getY() + y);
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
    }

    public boolean intersect(Vector mouse) {
        return false;
    }

    public boolean intersect(Vector v1, Vector v2) {
        // 完全包含
        if (v1.x <= pos.x && v1.y <= pos.y && v2.x >= (pos.x + size.x) && v2.y >= (pos.y + size.y)) {
            return true;
        }
        return false;
    }
}