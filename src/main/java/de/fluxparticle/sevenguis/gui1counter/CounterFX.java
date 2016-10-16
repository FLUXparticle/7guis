package de.fluxparticle.sevenguis.gui1counter;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class CounterFX extends CounterBase {

    private final IntegerProperty counter = new SimpleIntegerProperty(0);

    protected void bind() {
        btCountUp.setOnAction(e -> counter.set(counter.get() + 1));

        tfCount.textProperty().bind(counter.asString());
    }

    public static void main(String[] args) {
        launch(args);
    }

}
