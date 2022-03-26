package tw.ssr;

import java.awt.event.MouseEvent;

public class Vector {
    public int x, y;

    public Vector() {
        x = y = 0;
    }

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector(double x, double y) {
        this.x = (int) x;
        this.y = (int) y;
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

    public Vector scale(double s) {
        this.x *= s;
        this.y *= s;
        return this;
    }

    public Vector abs() {
        this.x = Math.abs(x);
        this.y = Math.abs(y);
        return this;
    }

    public void setMin(Vector other) {
        setVec(Math.min(x, other.x), Math.min(y, other.y));
    }

    public void setMax(Vector other) {
        setVec(Math.max(x, other.x), Math.max(y, other.y));
    }

    public static Vector minPoint(Vector p, Vector q) {
        Vector res = p.clone();
        res.setMin(q);
        return res;
    }

    public static Vector midPoint(Vector p, Vector q) {
        Vector res = p.clone();
        res.setVec((p.x + q.x) / 2, (p.y + q.y) / 2);
        return res;
    }

    public static Vector maxPoint(Vector p, Vector q) {
        Vector res = p.clone();
        res.setMax(q);
        return res;
    }

    public double distance(Vector other) {
        return Vector.distance(this, other);
    }

    public static double distance(Vector p, Vector q) {
        return Math.sqrt(Math.pow(p.x - q.x, 2) + Math.pow(p.y - q.y, 2));
    }

    public static Vector rotate(double theta, double ux, double uy) {
        double vx, vy, sin, cos;
        sin = Math.sin(theta);
        cos = Math.cos(theta);
        vx = cos * ux - sin * uy;
        vy = sin * ux + cos * uy;
        return new Vector(vx, vy);
    }

    @Override
    public String toString() {
        return "( " + x + ", " + y + ");";
    }
}