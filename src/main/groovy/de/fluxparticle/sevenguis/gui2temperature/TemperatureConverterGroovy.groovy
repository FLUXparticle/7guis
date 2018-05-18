package de.fluxparticle.sevenguis.gui2temperature

import de.fluxparticle.fenja.FenjaBuilder

import static de.fluxparticle.fenja.Value.valueOfSilent

/**
 * Created by sreinck on 15.06.16.
 */
class TemperatureConverterGroovy extends TemperatureConverterBase {

    @Override
    protected void bind() {
        new FenjaBuilder().build {
            sCelsiusInput     =  valueOfSilent(pCelsius).values();
            sFahrenheitInput  =  valueOfSilent(pFahrenheit).values();

            // -----

            sFahrenheitOutput = sCelsiusInput
                    .filter { it != null }
                    .map { cToF(it.doubleValue()) };

            sCelsiusOutput = sFahrenheitInput
                    .filter { it != null }
                    .map { fToC(it.doubleValue()) };

            // -----

            sFahrenheitOutput.listen(pFahrenheit.&setValue)
            sCelsiusOutput.listen(pCelsius.&setValue)
        }
    }

    static void main(String... args) {
        launch(TemperatureConverterGroovy.class, args)
    }

}
