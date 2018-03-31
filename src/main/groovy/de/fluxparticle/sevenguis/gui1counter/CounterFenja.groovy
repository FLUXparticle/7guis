package de.fluxparticle.sevenguis.gui1counter

import de.fluxparticle.fenja.FenjaBuilder
import javafx.event.ActionEvent

import static de.fluxparticle.fenja.EventStream.streamOf

/**
 * Created by sreinck on 15.06.16.
 */
class CounterFenja extends CounterBase {

    @Override
    protected void bind() {
        new FenjaBuilder().build {
            sClick = streamOf(btCountUp, ActionEvent.ACTION)

            // -----

            sNextCount = sClick.snapshot(vCount).map { count -> count + 1 }
            vCount = sNextCount.hold(0)

            // -----

            tfCount.textProperty() << vCount.map(Integer.&toString)
        }
    }

    static void main(String... args) {
        launch(CounterFenja.class, args)
    }

}
