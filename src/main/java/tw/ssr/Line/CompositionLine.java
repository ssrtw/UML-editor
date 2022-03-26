package tw.ssr.Line;

import tw.ssr.Vector;

import java.awt.*;

public class CompositionLine extends Line {

    public CompositionLine() {
        super();
    }

    public CompositionLine(Vector p, Vector q) {
        super(p, q);
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        Vector vec = p.clone().sub(q);
        // 箭頭兩側的長度
        int len = 16;
        // 算距離
        double d = p.distance(q);
        double ux, uy;
        // 求線段長度的向量
        ux = vec.x / d * len;
        uy = vec.y / d * len;
        // 對該向量轉45度角
        Vector r1 = Vector.rotate(Math.PI / 4, ux, uy).add(q);
        Vector r2 = Vector.rotate(-Math.PI / 4, ux, uy).add(q);
        // 要畫菱形的點
        Vector r3 = q.clone().add((int) (ux * Math.sqrt(2)), (int) (uy * Math.sqrt(2)));
        g2.drawLine(q.x, q.y, r1.x, r1.y);
        g2.drawLine(q.x, q.y, r2.x, r2.y);
        g2.drawLine(r1.x, r1.y, r3.x, r3.y);
        g2.drawLine(r2.x, r2.y, r3.x, r3.y);
        g2.drawLine(p.x, p.y, r3.x, r3.y);
    }

}
