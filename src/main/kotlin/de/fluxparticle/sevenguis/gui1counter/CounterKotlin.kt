package de.fluxparticle.sevenguis.gui1counter

import de.fluxparticle.fenja.FenjaSystem
import de.fluxparticle.fenja.expr.Expr
import de.fluxparticle.fenja.expr.bind
import de.fluxparticle.fenja.logger.PrintFenjaSystemLogger
import de.fluxparticle.fenja.stream.EventStream
import de.fluxparticle.fenja.stream.EventStreamSource
import de.fluxparticle.fenja.stream.bind
import javafx.event.ActionEvent

/**
 * Created by sreinck on 18.05.18.
 */
class CounterKotlin : CounterBase() {

    private val system = FenjaSystem(PrintFenjaSystemLogger(System.out))

    private val sClick: EventStreamSource<ActionEvent> by system.EventStreamSourceDelegate()

    private var sNextCount: EventStream<Int> by system.EventStreamRelayDelegate()

    private var vCount: Expr<Int> by system.OutputExprDelegate()

    private var vCountStr: Expr<String> by system.OutputExprDelegate()

    override fun bind() {
        sClick.bind(btCountUp, ActionEvent.ACTION)

        // -----

        sNextCount = (sClick snapshot vCount) { _, count -> count + 1 }
        vCount = sNextCount hold 0
        vCountStr = vCount map { it.toString() }

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
