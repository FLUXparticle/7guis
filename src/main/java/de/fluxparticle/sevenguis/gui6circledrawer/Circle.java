package de.fluxparticle.sevenguis.gui6circledrawer;

public class Circle {

    private int x;

    private int y;

    private int diameter;

    public Circle() {
        // empty
    }

    Circle(int x, int y) {
        this.x = x;
        this.y = y;
        this.diameter = 30;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDiameter() {
        return diameter;
    }

    public void setDiameter(int d) {
        this.diameter = d;
    }

    @Override
    public String toString() {
        return "Circle{x=" + x + ", y=" + y + ", diameter=" + diameter + '}';
    }

}
