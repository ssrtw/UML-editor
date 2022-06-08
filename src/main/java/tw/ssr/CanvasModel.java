package tw.ssr;

import tw.ssr.Line.*;
import tw.ssr.Object.*;
import java.awt.*;
import java.util.ArrayList;

public class CanvasModel {
    Object2D scene;
    ArrayList<Line> lines;

    public CanvasModel() {
        // TODO: 我覺得scene是特例，因此用override，之後可能得重想這邊的繼承關係
        scene = new Object2D() {
            @Override
            public void render(Graphics g) {
                for (Object2D child : children) {
                    child.render(g);
                }
            }

            @Override
            public boolean intersect(Vector mouse) {
                return false;
            }
        };
        lines = new ArrayList<>();
    }
}
