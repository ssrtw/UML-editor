package tw.ssr.Line;

import tw.ssr.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Line {
    protected Vector p, q;

    public Line() {
        this.p = null;
        this.q = null;
    }

    public Line(Vector p, Vector q) {
        this.p = p;
        this.q = q;
    }

    public abstract void render(Graphics g);
}
