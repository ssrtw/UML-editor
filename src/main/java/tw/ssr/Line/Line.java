package tw.ssr.Line;

import tw.ssr.IRenderObj;
import tw.ssr.Vector;

public abstract class Line implements IRenderObj {
    protected Vector p, q;

    public Line() {
        this.p = null;
        this.q = null;
    }

    public Line(Vector p, Vector q) {
        this.p = p;
        this.q = q;
    }
}
