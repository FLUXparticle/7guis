package de.fluxparticle.sevenguis.gui0diamond;

import javafx.event.ActionEvent;
import org.reactfx.EventStream;
import org.reactfx.EventStreams;

public class DiamondReactFX extends DiamondBase {

    protected void bind() {
        EventStream<ActionEvent> sClicks = EventStreams.eventsOf(btCountUp, ActionEvent.ACTION);

        // -----

        EventStream<Integer> summed = sClicks.accumulate(0, (n, x) -> n + 1);

        EventStream<Integer> left = summed.map(s -> s + 1);
        EventStream<Integer> right = summed.map(s -> s + 2);

        EventStream<Integer> result = EventStreams.zip(left, right).map((l, r) -> l * r);

        // -----

        summed.map(Object::toString).feedTo(tfCount.textProperty());
        left.map(Object::toString).feedTo(tfLeft.textProperty());
        right.map(Object::toString).feedTo(tfRight.textProperty());
        result.subscribe( r -> lvResults.getItems().add(r) );
    }

    public static void main(String[] args) {
        launch(args);
    }

}
