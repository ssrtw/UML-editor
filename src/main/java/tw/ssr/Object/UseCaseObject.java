package tw.ssr.Object;

import tw.ssr.Vector;

import java.awt.*;

public class UseCaseObject extends Object2D {
    private static final int width = 120, height = 80;

    public UseCaseObject() {
        super();
    }

    public UseCaseObject(int x, int y, Object2D parent, Material mat) {
        super(x, y, width, height, parent, mat);
        name = "Use Case Name";
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        Vector position = sumParentPosition();
        g.setColor(mat.fill);
        g.fillOval(position.getX(), position.getY(), size.x, size.y);
        Graphics2D g2 = (Graphics2D) g;
        if (selected)
            g2.setColor(Color.RED);
        else
            g2.setColor(mat.edge);
        g2.setStroke(new BasicStroke(4));
        g2.drawOval(position.getX(), position.getY(), size.x, size.y);
        g.setColor(Color.BLACK);
        g.drawString(name, position.x + 15, position.y + 40);
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
