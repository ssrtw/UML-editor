package tw.ssr.Object;

import java.awt.*;

public class UseCaseObject extends Object2D {
    private static int width = 120, height = 80;

    public UseCaseObject() {
        super();
    }

    public UseCaseObject(int x, int y, Object2D parent, Material mat) {
        super(x, y, width, height, parent, mat);
        name = "Use Case Name";
    }

    @Override
    public void render(Graphics g) {
        g.setColor(mat.fill);
        g.fillOval(pos.getX(), pos.getY(), size.x, size.y);
        Graphics2D g2 = (Graphics2D) g;
        if (selected)
            g2.setColor(Color.RED);
        else
            g2.setColor(mat.edge);
        g2.setStroke(new BasicStroke(4));
        g2.drawOval(pos.getX(), pos.getY(), size.x, size.y);
        g.setColor(Color.BLACK);
        g.drawString(name, pos.x + 15, pos.y + 40);
    }

    @Override
    public boolean intersect(Vector mouse) {
        double rx = width / 2;
        double ry = height / 2;
        double tx = (mouse.x - (pos.x + rx)) / rx;
        double ty = (mouse.y - (pos.y + ry)) / ry;
        return tx * tx + ty * ty < 1.0;
    }

}
