package de.fluxparticle.sevenguis.gui4timer

import de.fluxparticle.fenja.FenjaSystem
import de.fluxparticle.fenja.expr.Expr
import de.fluxparticle.fenja.expr.InputExpr
import de.fluxparticle.fenja.expr.bind
import de.fluxparticle.fenja.logger.PrintFenjaSystemLogger
import de.fluxparticle.fenja.stream.EventStream
import de.fluxparticle.fenja.stream.EventStreamSource
import de.fluxparticle.fenja.stream.bind
import de.fluxparticle.fenja.stream.ticker
import javafx.event.ActionEvent

/**
 * Created by sreinck on 05.08.18.
 */
class TimerKotlin : TimerBase() {

    private val system = FenjaSystem(PrintFenjaSystemLogger(System.out))

    private val sTicker: EventStreamSource<Unit> by system.EventStreamSourceDelegate()

    private val vSlider: InputExpr<Number> by system.InputExprDelegate()

    private val sReset: EventStreamSource<ActionEvent> by system.EventStreamSourceDelegate()

    private var sTicks: EventStream<Unit> by system.EventStreamRelayDelegate()

    private var sResetElapsed: EventStream<Double> by system.EventStreamRelayDelegate()

    private var sTickElapsed: EventStream<Double> by system.EventStreamRelayDelegate()

    private var vElapsed: Expr<Double> by system.OutputExprDelegate()

    private var vTicking: Expr<Boolean> by system.OutputExprDelegate()

    private var vElapsedStr: Expr<String> by system.OutputExprDelegate()

    private var vProgress: Expr<Number> by system.OutputExprDelegate()

    override fun bind() {
        sTicker  ticker  DURATION
        vSlider  bind    sSlider.valueProperty()
        sReset.bind(btReset, ActionEvent.ACTION)

        // -----

        sTicks         =  sTicker gate vTicking

        sResetElapsed  =  sReset map { 0.0 }
        sTickElapsed   =  (sTicks snapshot vElapsed) { _, elapsed -> elapsed + DURATION.toSeconds() }

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
