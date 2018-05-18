package de.fluxparticle.sevenguis.gui0diamond

import de.fluxparticle.fenja.FenjaBuilder
import javafx.event.ActionEvent

import static de.fluxparticle.fenja.EventStream.streamOf

/**
 * Created by sreinck on 16.06.16.
 */
class DiamondGroovy extends DiamondBase {

    @Override
    protected void bind() {
        new FenjaBuilder().build {
            sClicks  =  streamOf(btCountUp, ActionEvent.ACTION)

            // -----

            vCounter  =  sClicks.accum(0) { e, n -> n + 1 }

            vLeft     =  vCounter.map { c -> c + 1 }
            vRight    =  vCounter.map { c -> c + 2 }

            vResult   =  (vLeft ** vRight) { l, r -> l * r }

            // -----

            tfCount.textProperty()  <<  vCounter.map(Integer.&toString)
            tfLeft.textProperty()   <<  vLeft.map(Integer.&toString)
            tfRight.textProperty()  <<  vRight.map(Integer.&toString)
            vResult.listen { r -> lvResults.getItems().add(r) }
        }
    }

    static void main(String... args) {
        launch(DiamondGroovy.class, args)
    }

}
