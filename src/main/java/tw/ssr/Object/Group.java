package tw.ssr.Object;

import java.awt.*;

public class Group extends Object2D {
    private int width, height;

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
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public boolean intersect(Vector mouse) {
        return false;
    }
}
