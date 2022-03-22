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
        this.x = x;
        this.y = y;
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
        x += other.x;
        y += other.y;
        return this;
    }

    public Vector add(int x, int y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector sub(Vector other) {
        x -= other.x;
        y -= other.y;
        return this;
    }

    public Vector sub(int x, int y) {
        this.x -= x;
        this.y -= y;
        return this;
    }
}