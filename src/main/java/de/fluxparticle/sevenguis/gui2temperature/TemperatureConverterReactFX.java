package de.fluxparticle.sevenguis.gui2temperature;

import org.reactfx.EventStream;
import org.reactfx.EventStreams;
import org.reactfx.InterceptableEventStream;


/*
A version of Temperature Converter where the conversion back and forth
is not stable (i.e. fToC(cToF(v)) != v). Here, interceptable streams
are employed to prevent feedback loops.

This is what happens when the value of fahrenheit text field is changed:

 fahrenheit.text emits
 iFahrenheit emits the same value
 iFahernheit.guardedBy(...) emits the same value like this:
     iCelsius is muted first
     value is emitted
         celsius text is updated
         celsius.text emits (but not iCelsius, which is muted)
     iCelsius is unmuted

The explanation and implementation idea are by Tomas Mikula.
I merely put it all into an exectuable file.
 */
public class TemperatureConverterReactFX extends TemperatureConverterBase {

    protected void bind() {
        InterceptableEventStream<Number> sCelsiusInput = EventStreams.valuesOf(pCelsius).interceptable();
        InterceptableEventStream<Number> sFahrenheitInput = EventStreams.valuesOf(pFahrenheit).interceptable();

        EventStream<Double> sFahrenheitOutput = sCelsiusInput
                .filter(n -> n != null)
                .map(n -> cToF(n.doubleValue()))
                .guardedBy(sFahrenheitInput::mute);
        EventStream<Double> sCelsiusOutput = sFahrenheitInput
                .filter(n -> n != null)
                .map(n -> fToC(n.doubleValue()))
                .guardedBy(sCelsiusInput::mute);

        sCelsiusOutput.feedTo(pCelsius);
        sFahrenheitOutput.feedTo(pFahrenheit);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
