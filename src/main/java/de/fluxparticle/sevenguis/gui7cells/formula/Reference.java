package de.fluxparticle.sevenguis.gui7cells.formula;

/**
 * Created by sreinck on 24.02.16.
 */
public final class Reference extends Formula {

    private final int row;

    private final int column;

    public Reference(String name) {
        row = Integer.parseInt(name.substring(1))-1;
        column = name.charAt(0) - 'A';
    }

    public Reference(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public String toString() {
        return Character.toString((char) ('A' + column)) + (row + 1);
    }

    @Override
    public <R, D> R accept(ContentVisitor<R, D> visitor, D data) {
        return visitor.visitReference(row, column, data);
    }

}
