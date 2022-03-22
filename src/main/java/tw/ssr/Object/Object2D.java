package tw.ssr.Object;

import java.awt.*;
import java.util.ArrayList;

abstract public class Object2D {
    private static int portCnt = 4;
    private static int zIndexNxt = 0;
    protected int zIndex;
    protected Vector pos;
    protected Vector size;
    protected Material mat;
    protected String name;
    protected Vector ports[];
    protected Object2D parent;
    protected boolean selected;
    protected ArrayList<Object2D> children;

    public Object2D() {
        zIndex = zIndexNxt++;
        pos = new Vector();
        size = new Vector();
        mat = new Material();
        name = new String();
        ports = new Vector[portCnt];
        parent = null;
        selected = false;
        children = new ArrayList<>();
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

    public void move(int x, int y) {
        this.pos.setVec(pos.getX() + x, pos.getY() + y);
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Object2D> getChildren() {
        return children;
    }

    public abstract void render(Graphics g);

    public abstract boolean intersect(Vector mouse);

    public boolean intersect(Vector v1, Vector v2) {
        // 完全包含
        if (v1.x <= pos.x && v1.y <= pos.y && v2.x >= (pos.x + size.x) && v2.y >= (pos.y + size.y)) {
            return true;
        }
        return false;
    }
}