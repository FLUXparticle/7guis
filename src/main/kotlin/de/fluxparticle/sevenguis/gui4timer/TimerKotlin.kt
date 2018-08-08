package de.fluxparticle.sevenguis.gui4timer

import de.fluxparticle.fenja.FenjaSystem
import de.fluxparticle.fenja.expr.InputExpr
import de.fluxparticle.fenja.expr.UpdateExpr
import de.fluxparticle.fenja.expr.bind
import de.fluxparticle.fenja.logger.PrintFenjaSystemLogger
import de.fluxparticle.fenja.stream.InputEventStream
import de.fluxparticle.fenja.stream.UpdateEventStream
import de.fluxparticle.fenja.stream.bind
import de.fluxparticle.fenja.stream.ticker
import javafx.event.ActionEvent

/**
 * Created by sreinck on 05.08.18.
 */
class TimerKotlin : TimerBase() {

    private val system = FenjaSystem(PrintFenjaSystemLogger(System.out))

    private val sTicker:       InputEventStream<Unit>        by system.InputEventStreamDelegate()

    private val vSlider:       InputExpr<Number>             by system.InputExprDelegate()

    private val sReset:        InputEventStream<ActionEvent> by system.InputEventStreamDelegate()

    private var sTicks:        UpdateEventStream<Unit>       by system.UpdateEventStreamDelegate()

    private var sResetElapsed: UpdateEventStream<Double>     by system.UpdateEventStreamDelegate()

    private var sTickElapsed:  UpdateEventStream<Double>     by system.UpdateEventStreamDelegate()

    private var vElapsed:      UpdateExpr<Double>            by system.UpdateExprDelegate()

    private var vTicking:      UpdateExpr<Boolean>           by system.UpdateExprDelegate()

    private var vElapsedStr:   UpdateExpr<String>            by system.UpdateExprDelegate()

    private var vProgress:     UpdateExpr<Number>            by system.UpdateExprDelegate()

    override fun bind() {
        sTicker  ticker  DURATION
        vSlider  bind    sSlider.valueProperty()
        sReset.bind(btReset, ActionEvent.ACTION)

        // -----

        sTicks         =  sTicker filter { vTicking.sample() }

        sResetElapsed  =  sReset map { 0.0 }
        sTickElapsed   =  sTicks map { _ -> vElapsed.sample() + DURATION.toSeconds() }

        vElapsed       =  (sResetElapsed orElse sTickElapsed).hold(0.0)

        vTicking       =  (vElapsed combine vSlider) { e, s -> e < s.toDouble() }

        vElapsedStr    =  vElapsed map { String.format("%.1fs", it)}

        vProgress      =  (vElapsed combine vSlider) { e, s -> e / s.toDouble() }

        // -----

        lNumericProgress.textProperty()  bind  vElapsedStr
        pbProgress.progressProperty()    bind  vProgress

        system.finish()
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            launch(TimerKotlin::class.java, *args)
        }

    }

}
