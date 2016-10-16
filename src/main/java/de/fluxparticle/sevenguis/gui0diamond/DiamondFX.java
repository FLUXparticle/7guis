package de.fluxparticle.sevenguis.gui0diamond;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class DiamondFX extends DiamondBase {

    private final IntegerProperty counter = new SimpleIntegerProperty(0);

    private final IntegerProperty left = new SimpleIntegerProperty(0);

    private final IntegerProperty right = new SimpleIntegerProperty(0);

    private final IntegerProperty result = new SimpleIntegerProperty(0);

    protected void bind() {
        btCountUp.setOnAction(e -> counter.set(counter.get() + 1));

        // -----

        left.bind( counter.add(1) );
        right.bind( counter.add(2) );

        result.bind( left.multiply(right) );

        // -----

        tfCount.textProperty().bind( counter.asString() );
        tfLeft.textProperty().bind( left.asString() );
        tfRight.textProperty().bind( right.asString() );
        result.addListener((observable, oldValue, newValue) -> {
            lvResults.getItems().add((Integer) newValue);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}
