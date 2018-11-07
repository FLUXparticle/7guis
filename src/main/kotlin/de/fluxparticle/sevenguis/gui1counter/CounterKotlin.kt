package de.fluxparticle.sevenguis.gui1counter

import de.fluxparticle.fenja.FenjaSystem.Companion.build
import de.fluxparticle.fenja.FenjaSystem.UpdateExpr
import de.fluxparticle.fenja.bind
import de.fluxparticle.fenja.logger.PrintFenjaSystemLogger
import javafx.event.ActionEvent

/**
 * Created by sreinck on 18.05.18.
 */
class CounterKotlin : CounterBase() {

    private val logger = PrintFenjaSystemLogger(System.out)

    override fun bind() {
        build(logger) {
            val sClick        by  eventsOf(btCountUp, ActionEvent.ACTION)

            // -----

            var vCount: UpdateExpr<Int> by loop()

            val sNextCount    by  sClick map { _ -> vCount.sample() + 1 }
            vCount            =   sNextCount hold 0
            val vText     by  vCount map { it.toString() }

            // -----

            tfCount.textProperty()  bind  vText

        }
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            launch(CounterKotlin::class.java, *args)
        }

    }

}
