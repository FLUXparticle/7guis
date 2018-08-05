package de.fluxparticle.sevenguis.gui4timer

import de.fluxparticle.fenja.FenjaBuilder
import javafx.event.ActionEvent

import static de.fluxparticle.fenja.EventStream.streamOf
import static de.fluxparticle.fenja.EventStream.ticker
import static de.fluxparticle.fenja.Value.valueOf
/**
 * Created by sreinck on 15.06.16.
 */
class TimerGroovy extends TimerBase {

    @Override
    protected void bind() {
        new FenjaBuilder().build {
            sTicker  =  ticker(DURATION)
            vSlider  =  valueOf(sSlider.valueProperty())
            sReset   =  streamOf(btReset, ActionEvent.ACTION)

            // -----

            sTicks         =  sTicker.filter { vTicking }

            sResetElapsed  =  sReset.map { 0.0 }
            sTickElapsed   =  sTicks.map { vElapsed + DURATION.toSeconds() }

            vElapsed       =  (sResetElapsed * sTickElapsed).hold(0.0)

            vTicking       =  (vElapsed ** vSlider) { e, s -> e < s }

            // -----

            lNumericProgress.textProperty()  <<  vElapsed.map { String.format("%.1fs", it)}
            pbProgress.progressProperty()    <<  (vElapsed ** vSlider) { e, s -> e / s}
        }
    }

    static void main(String... args) {
        launch(TimerGroovy.class, args)
    }

}
