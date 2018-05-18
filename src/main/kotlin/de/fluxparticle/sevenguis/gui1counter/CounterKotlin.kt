package de.fluxparticle.sevenguis.gui1counter

import de.fluxparticle.fenja.*
import javafx.event.ActionEvent
import nz.sodium.Transaction

/**
 * Created by sreinck on 18.05.18.
 */
class CounterKotlin : CounterBase() {

    private var sClick: EventStream<ActionEvent> by eventStream()

    private var sNextCount: EventStream<Int> by eventStream()

    private var vCount: Value<Int> by value()

    override fun bind() {
        Transaction.runVoid {
            sClick = EventStream.streamOf(btCountUp, ActionEvent.ACTION)

            // -----

            sNextCount = sClick.snapshot(vCount).map { count -> count + 1 }
            vCount = sNextCount.hold(0)

            // -----

            tfCount.textProperty() bindTo vCount.map(Int::toString)
        }
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            launch(CounterKotlin::class.java, *args)
        }

    }

}
