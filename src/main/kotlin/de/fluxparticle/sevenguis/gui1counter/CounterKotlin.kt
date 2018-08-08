package de.fluxparticle.sevenguis.gui1counter

import de.fluxparticle.fenja.FenjaSystem
import de.fluxparticle.fenja.expr.UpdateExpr
import de.fluxparticle.fenja.expr.bind
import de.fluxparticle.fenja.logger.PrintFenjaSystemLogger
import de.fluxparticle.fenja.stream.InputEventStream
import de.fluxparticle.fenja.stream.UpdateEventStream
import de.fluxparticle.fenja.stream.bind
import javafx.event.ActionEvent

/**
 * Created by sreinck on 18.05.18.
 */
class CounterKotlin : CounterBase() {

    private val system = FenjaSystem(PrintFenjaSystemLogger(System.out))

    private val sClick: InputEventStream<ActionEvent> by system.InputEventStreamDelegate()

    private var sNextCount: UpdateEventStream<Int> by system.UpdateEventStreamDelegate()

    private var vCount: UpdateExpr<Int> by system.UpdateExprDelegate()

    private var vCountStr: UpdateExpr<String> by system.UpdateExprDelegate()

    override fun bind() {
        sClick.bind(btCountUp, ActionEvent.ACTION)

        // -----

        sNextCount  =  sClick map { _ -> vCount.sample() + 1 }
        vCount      =  sNextCount hold 0
        vCountStr   =  vCount map { it.toString() }

        // -----

        tfCount.textProperty() bind vCountStr

        system.finish()
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            launch(CounterKotlin::class.java, *args)
        }

    }

}
