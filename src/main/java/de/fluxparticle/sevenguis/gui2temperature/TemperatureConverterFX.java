package de.fluxparticle.sevenguis.gui2temperature;

public class TemperatureConverterFX extends TemperatureConverterBase {

    protected void bind() {
        pCelsius.addListener((v, o, n) -> {
            if (n != null) {
                pFahrenheit.set(cToF(n.doubleValue()));
            }
        });
        pFahrenheit.addListener((v, o, n) -> {
            if (n != null) {
                pCelsius.set(fToC(n.doubleValue()));
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}
