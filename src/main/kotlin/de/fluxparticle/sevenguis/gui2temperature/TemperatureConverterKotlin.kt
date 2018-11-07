package de.fluxparticle.sevenguis.gui2temperature

import de.fluxparticle.fenja.FenjaSystem
import de.fluxparticle.fenja.bind
import de.fluxparticle.fenja.logger.PrintFenjaSystemLogger
import de.fluxparticle.fenja.stream.mapNotNull
import de.fluxparticle.sevenguis.gui1counter.CounterBase

/**
 * Created by sreinck on 20.05.18.
 */
class TemperatureConverterKotlin : TemperatureConverterBase() {

    private val logger = PrintFenjaSystemLogger(System.out)

    override fun bind() {
        FenjaSystem.build(logger) {
            val sCelsiusInput     by  changesOf(pCelsius)
            val sFahrenheitInput  by  changesOf(pFahrenheit)

            // -----

            val sFahrenheitOutput  by  sCelsiusInput mapNotNull { cToF(it.toDouble()) }
            val sCelsiusOutput     by  sFahrenheitInput mapNotNull { fToC(it.toDouble()) }

            // -----

            pFahrenheit  bind  sFahrenheitOutput
            pCelsius     bind  sCelsiusOutput
        }
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            CounterBase.launch(TemperatureConverterKotlin::class.java, *args)
        }

    }

}
