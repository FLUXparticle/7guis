package de.fluxparticle.sevenguis.gui2temperature

import de.fluxparticle.sevenguis.gui1counter.CounterBase

/**
 * Created by sreinck on 20.05.18.
 */
class TemperatureConverterKotlin : TemperatureConverterBase() {

/*
    private var sCelsiusInput: EventStream<Number> by eventStream()

    private var sFahrenheitInput: EventStream<Number> by eventStream()

    private var sFahrenheitOutput: EventStream<Number> by eventStream()

    private var sCelsiusOutput: EventStream<Number> by eventStream()
*/

    override fun bind() {
/*
        Transaction.runVoid {
            sCelsiusInput     =  valueOfSilent(pCelsius).values()
            sFahrenheitInput  =  valueOfSilent(pFahrenheit).values()

            // -----

            sFahrenheitOutput = sCelsiusInput
                    .filter { it != null }
                    .map { cToF(it.toDouble()) }

            sCelsiusOutput = sFahrenheitInput
                    .filter { it != null }
                    .map { fToC(it.toDouble()) }

            // -----

            sFahrenheitOutput.listen(pFahrenheit::setValue)
            sCelsiusOutput.listen(pCelsius::setValue)
        }
*/
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            CounterBase.launch(TemperatureConverterKotlin::class.java, *args)
        }

    }

}
