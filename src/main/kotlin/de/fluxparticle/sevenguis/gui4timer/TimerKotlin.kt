package de.fluxparticle.sevenguis.gui4timer

import de.fluxparticle.fenja.FenjaSystem.Companion.build
import de.fluxparticle.fenja.FenjaSystem.UpdateExpr
import de.fluxparticle.fenja.bind
import de.fluxparticle.fenja.logger.PrintFenjaSystemLogger
import javafx.event.ActionEvent.ACTION

/**
 * Created by sreinck on 05.08.18.
 */
class TimerKotlin : TimerBase() {

    private val logger = PrintFenjaSystemLogger(System.out)

    override fun bind() {
        build(logger) {
            val sTicker  by  ticker(DURATION)
            val vSlider  by  sSlider.valueProperty()
            val sReset   by  eventsOf(btReset, ACTION)

            // -----

            var vElapsed: UpdateExpr<Double>   by  loop()
            var vTicking: UpdateExpr<Boolean>  by  loop()

            val sTicks         by  sTicker filter { vTicking.sample() }

            val sResetElapsed  by  sReset map { 0.0 }
            val sTickElapsed   by  sTicks map { _ -> vElapsed.sample() + DURATION.toSeconds() }

            vElapsed           =  (sResetElapsed orElse sTickElapsed)  hold  0.0
            vTicking           =  (vElapsed combine vSlider) { e, s -> e < s }

            val vElapsedStr    by  vElapsed map { String.format("%.1fs", it) }

            val vProgress      by  (vElapsed combine vSlider) { e, s -> e / s }

            // -----

            lNumericProgress.textProperty()  bind  vElapsedStr
            pbProgress.progressProperty()    bind  vProgress
        }
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            launch(TimerKotlin::class.java, *args)
        }

    }

}
