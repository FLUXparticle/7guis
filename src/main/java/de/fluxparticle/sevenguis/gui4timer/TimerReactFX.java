package de.fluxparticle.sevenguis.gui4timer;

import javafx.event.ActionEvent;
import org.reactfx.EventStream;
import org.reactfx.EventStreams;
import org.reactfx.StateMachine;
import org.reactfx.util.Tuples;

import java.time.Duration;

import static org.reactfx.EventStreams.combine;
import static org.reactfx.EventStreams.valuesOf;

public class TimerReactFX extends TimerBase {

    protected void bind() {
        EventStream<ActionEvent> resets = EventStreams.eventsOf(btReset, ActionEvent.ACTION);
        EventStream<?> ticks = EventStreams.ticks(Duration.ofMillis(100));

        EventStream<Double> elapsed = StateMachine.init(Tuples.t(0.0, sSlider.getValue()))
                .on(resets).transition((tup_es, r) -> Tuples.t(0.0, tup_es._2))
                .on(ticks).transition((tup_es, t) -> Tuples.t(tup_es._1 + (tup_es._1 < tup_es._2 ? 0.1 : 0.0), tup_es._2))
                .on(EventStreams.valuesOf(sSlider.valueProperty())).transition((tup_es, s1) -> Tuples.t(tup_es._1, s1.doubleValue()))
                .toStateStream().map(tup_es -> tup_es._1);

        combine(elapsed, valuesOf(sSlider.valueProperty())).map((e, s) -> e / s.doubleValue()).subscribe(pbProgress::setProgress);

        elapsed
                .map(e -> String.format("%.1fs", e))
                .feedTo(lNumericProgress.textProperty());
    }

    public static void main(String[] args) {
        launch(args);
    }

}
