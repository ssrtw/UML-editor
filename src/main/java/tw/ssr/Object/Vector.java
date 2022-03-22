package tw.ssr.Object;

import java.awt.event.MouseEvent;

public class Vector {
    protected int x, y;

    public Vector() {
        x = y = 0;
    }

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector(MouseEvent e) {
        this.x = e.getX();
        this.y = e.getY();
    }

    public void setVec(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setVec(MouseEvent e) {
        this.x = e.getX();
        this.y = e.getY();
    }

    public void setVec(Vector v) {
        this.x = v.x;
        this.y = v.y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Vector clone() {
        return new Vector(x, y);
    }

    public Vector add(Vector other) {
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    public Vector add(int x, int y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector sub(Vector other) {
        this.x -= other.x;
        this.y -= other.y;
        return this;
    }

    public Vector sub(int x, int y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vector abs() {
        this.x = Math.abs(x);
        this.y = Math.abs(y);
        return this;
    }

    public static Vector minPoint(Vector p, Vector q) {
        Vector res = new Vector();
        res.setVec(Math.min(p.x, q.x), Math.min(p.y, q.y));
        return res;
    }

    public static Vector maxPoint(Vector p, Vector q) {
        Vector res = new Vector();
        res.setVec(Math.max(p.x, q.x), Math.max(p.y, q.y));
        return res;
    }

    @Override
    public String toString() {
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}