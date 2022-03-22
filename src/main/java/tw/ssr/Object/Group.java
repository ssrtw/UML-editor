package tw.ssr.Object;

import java.awt.*;

public class Group extends Object2D {
    public Group() {
        super();
    }

    @Override
    public void render(Graphics g) {
        for (Object2D obj : children) {
            obj.render(g);
        }
    }

    @Override
    public boolean intersect(Vector mouse) {
        return false;
    }
}
