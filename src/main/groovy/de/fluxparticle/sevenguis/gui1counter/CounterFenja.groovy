package de.fluxparticle.sevenguis.gui1counter

import javafx.event.ActionEvent;
import de.fluxparticle.fenja.FenjaBuilder

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

            vCount = sClick.accum(0) { _, s -> s + 1 }

            // -----

            tfCount.textProperty() << vCount.map(Integer.&toString)
        }
    }

    static void main(String... args) {
        launch(CounterFenja.class, args)
    }

}
