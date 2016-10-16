package de.fluxparticle.sevenguis.gui1counter;

import javafx.event.ActionEvent;
import org.reactfx.EventStream;
import org.reactfx.EventStreams;

// https://gist.github.com/TomasMikula/d0c5bd845b2a5db32cd2
public class CounterReactFX extends CounterBase {

    protected void bind() {
        EventStream<ActionEvent> sClicks = EventStreams.eventsOf(btCountUp, ActionEvent.ACTION);

        EventStream<Integer> summed = sClicks.accumulate(0, (n, x) -> n + 1);

        summed.map(Object::toString).feedTo(tfCount.textProperty());
    }

    public static void main(String[] args) {
        launch(args);
    }

}
