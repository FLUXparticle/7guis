package de.fluxparticle.sevenguis.gui6circledrawer;

import com.ajjpj.afoundation.collection.immutable.AMapEntry;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by sreinck on 21.06.16.
 */
abstract class CircleDrawerCanvasBase extends Canvas {

    CircleDrawerCanvasBase() {
        super(400, 400);
        bind();
    }

    protected abstract void bind();

    void draw(Iterable<Circle> circles, Circle hovered) {
        GraphicsContext g = getGraphicsContext2D();
        g.setFill(Color.WHITE);
        g.setStroke(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.strokeRect(0, 0, getWidth(), getHeight());

        for (Circle c : circles) {
            int offset = c.getDiameter() / 2;
            if (c.equals(hovered)) {
                g.setFill(Color.LIGHTGRAY);
                g.fillOval(c.getX() - offset, c.getY() - offset, c.getDiameter(), c.getDiameter());
            }
            g.strokeOval(c.getX() - offset, c.getY() - offset, c.getDiameter(), c.getDiameter());
        }
    }

    static AMapEntry<Integer, Circle> getNearestCircleAt(Iterable<AMapEntry<Integer, Circle>> circles, double x, double y) {
        AMapEntry<Integer, Circle> min = null;

        double minDist = Double.MAX_VALUE;
        for (AMapEntry<Integer, Circle> entry : circles) {
            Circle c = entry.getValue();
            double d = Math.sqrt(Math.pow(x - c.getX(), 2) + Math.pow(y - c.getY(), 2));
            if (d <= c.getDiameter()/2 && d < minDist) {
                min = entry;
                minDist = d;
            }
        }

        return min;
    }

}
