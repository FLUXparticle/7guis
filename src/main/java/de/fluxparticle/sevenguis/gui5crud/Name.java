package de.fluxparticle.sevenguis.gui5crud;

/**
 * Created by sreinck on 16.06.16.
 */
class Name {

    private String name;

    private String surname;

    public Name(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public boolean startsWith(String prefix) {
        return name.startsWith(prefix);
    }

    @Override
    public String toString() {
        return "<" + name + ", " + surname + ">";
    }

}
