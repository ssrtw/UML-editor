package tw.ssr.Line;

import tw.ssr.Vector;

import java.awt.*;

public class AssociationLine extends Line {

    public AssociationLine() {
        super();
    }

    public AssociationLine(Vector p, Vector q) {
        super(p, q);
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(p.x, p.y, q.x, q.y);
        Vector vec = p.clone().sub(q);
        // 箭頭兩撇的長度
        int len = 20;
        // 算距離
        double d = p.distance(q);
        double ux, uy;
        // 求線段長度的向量
        ux = vec.x / d * len;
        uy = vec.y / d * len;
        // 對該向量轉30度角
        Vector r1 = Vector.rotate(Math.PI / 6, ux, uy).add(q);
        Vector r2 = Vector.rotate(-Math.PI / 6, ux, uy).add(q);
        g2.drawLine(q.x, q.y, r1.x, r1.y);
        g2.drawLine(q.x, q.y, r2.x, r2.y);
    }

}
