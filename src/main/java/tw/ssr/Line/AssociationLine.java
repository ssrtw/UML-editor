package tw.ssr.Line;

import tw.ssr.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class AssociationLine extends Line {

    public AssociationLine() {
        super();
    }

    public AssociationLine(Vector... vectors) {
        super(vectors);
    }

    public void render(Graphics g) {
        for (int i = 0; i < points.size() - 1; i++) {
            g.drawLine(points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y);
        }
    }
}
