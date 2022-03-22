package tw.ssr.Object;

import java.awt.*;

public class ClassObject extends Object2D {
    private static int width = 80, height = 120;

    public ClassObject() {
        super();
    }

    public ClassObject(int x, int y, Object2D parent, Material mat) {
        super(x, y, width, height, parent, mat);
        name = "Class Name";
    }

    @Override
    public void render(Graphics g) {
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
        g.setColor(Color.BLACK);
        g.drawString(name, position.x + 5, position.y + 20);
    }

    @Override
    public boolean intersect(Vector mouse) {
        Vector position = parent.pos.clone().add(pos);
        if (mouse.x >= position.x && mouse.x <= position.x + size.x && mouse.y >= position.y && mouse.y <= position.y + size.y)
            return true;
        return false;
    }
}
