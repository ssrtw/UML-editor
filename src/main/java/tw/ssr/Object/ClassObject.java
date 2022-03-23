package tw.ssr.Object;

import java.awt.*;

public class ClassObject extends Object2D {
    private static final int width = 100, height = 150;

    public ClassObject() {
        super();
    }

    public ClassObject(int x, int y, Object2D parent, Material mat) {
        super(x, y, width, height, parent, mat);
        name = "Class Name";
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        Vector position = parent.pos.clone().add(pos);
        g.setColor(mat.fill);
        g.fillRect(position.x, position.y, size.x, size.y);
        Graphics2D g2 = (Graphics2D) g;
        if (selected)
            g2.setColor(Color.RED);
        else
            g2.setColor(mat.edge);
        g2.setStroke(new BasicStroke(4));
        g2.drawRect(position.x, position.y, size.x, size.y);
        g2.drawLine(position.x, position.y + 30, position.x + size.x, position.y + 30);
        g2.setStroke(new BasicStroke(2));
        for (int i = 2; i <= 4; i++)
            g2.drawLine(position.x, position.y + 30 * i, position.x + size.x, position.y + 30 * i);
        // name
        g.setColor(Color.BLACK);
        g.drawString(name, position.x + 5, position.y + 20);
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
        Vector position = parent.pos.clone().add(pos);
        if (mouse.x >= position.x && mouse.x <= position.x + size.x && mouse.y >= position.y && mouse.y <= position.y + size.y)
            return true;
        return false;
    }
}
