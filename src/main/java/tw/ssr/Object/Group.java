package tw.ssr.Object;

import tw.ssr.Vector;

import java.awt.*;
import java.util.ArrayList;

public class Group extends Object2D {
    public Group() {
        super();
        children = new ArrayList<>();
        isGroup = true;
    }

    public void grouping() {
        pos.setVec(Integer.MAX_VALUE, Integer.MAX_VALUE);
        size.setVec(0, 0);
        for (Object2D obj : children) {
            // 找完所有的children，看應該讓group的位置在哪
            pos.setMin(obj.pos);
            // 找完所有的children，看應該要框多少的範圍
            size.setMax(obj.pos.clone().add(obj.size.x, obj.size.y));
            // change parent
            obj.parent = this;
        }
        // 算出長寬，設定長寬
        size.sub(pos);
        pos.sub(10, 10);
        size.add(20, 20);
        // 所有物件重設座標，基底座標是當前group的
        for (Object2D obj : children) {
            obj.pos.sub(pos);
        }
    }

    public void ungrouping() {
        for (Object2D obj : children) {
            // 把所有的children都變回這個group的parent節點
            obj.parent = parent;
            parent.getChildren().add(obj);
            // 把position都加上當前group的offset
            obj.pos.add(pos);
        }
        // 把自己從parent的列表中移除
        parent.getChildren().remove(this);
    }

    @Override
    public void render(Graphics g) {
        Vector position = sumParentPosition();
        // 先渲染group出來
        // 設定背景為半透明
        g.setColor(new Color(64, 64, 64, 128));
        g.fillRect(position.x, position.y, size.x, size.y);
        Graphics2D g2 = (Graphics2D) g;
        if (selected) {
            g2.setColor(Color.RED);
        }
        g2.setStroke(new BasicStroke(4));
        g2.drawRect(position.x, position.y, size.x, size.y);
        // 完成group的渲染，接著渲染children的
        for (Object2D child : children) {
            child.render(g);
        }
    }

    @Override
    public boolean intersect(Vector mouse) {
        Vector position = parent.pos.clone().add(pos);
        return mouse.x >= position.x && mouse.x <= position.x + size.x && mouse.y >= position.y && mouse.y <= position.y + size.y;
    }
}
