package de.fluxparticle.sevenguis.gui2temperature

import de.fluxparticle.fenja.FenjaSystem
import de.fluxparticle.fenja.logger.PrintFenjaSystemLogger
import de.fluxparticle.fenja.stream.EventStream
import de.fluxparticle.fenja.stream.EventStreamSource
import de.fluxparticle.fenja.stream.bind
import de.fluxparticle.fenja.stream.mapNotNull
import de.fluxparticle.sevenguis.gui1counter.CounterBase

/**
 * Created by sreinck on 20.05.18.
 */
class TemperatureConverterKotlin : TemperatureConverterBase() {

    private val system = FenjaSystem(PrintFenjaSystemLogger(System.out))

    private val sCelsiusInput: EventStreamSource<Number?> by system.EventStreamSourceDelegate()

    private val sFahrenheitInput: EventStreamSource<Number?> by system.EventStreamSourceDelegate()

    private var sFahrenheitOutput: EventStream<Number?> by system.EventStreamRelayDelegate()

    private var sCelsiusOutput: EventStream<Number?> by system.EventStreamRelayDelegate()

    override fun bind() {
        sCelsiusInput     bind  pCelsius
        sFahrenheitInput  bind  pFahrenheit

        // -----

        sFahrenheitOutput = sCelsiusInput mapNotNull { cToF(it.toDouble()) }
        sCelsiusOutput = sFahrenheitInput mapNotNull { fToC(it.toDouble()) }

        // -----

        pFahrenheit  bind  sFahrenheitOutput
        pCelsius     bind  sCelsiusOutput

        system.finish()
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            CounterBase.launch(TemperatureConverterKotlin::class.java, *args)
        }

    }

}
