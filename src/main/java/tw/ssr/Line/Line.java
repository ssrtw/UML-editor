package tw.ssr.Line;

import tw.ssr.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Line {
    ArrayList<Vector> points;

    public Line() {
        points = new ArrayList<>();
    }

    public Line(Vector... vectors) {
        this();
        points.addAll(Arrays.asList(vectors));
    }

    public void render(Graphics g) {
        for (int i = 0; i < points.size() - 1; i++) {
            g.drawLine(points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y);
        }
    }
}
