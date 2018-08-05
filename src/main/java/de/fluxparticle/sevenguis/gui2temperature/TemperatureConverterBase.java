package de.fluxparticle.sevenguis.gui2temperature;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.text.NumberFormat;
import java.text.ParsePosition;

public abstract class TemperatureConverterBase extends Application {

    private static final NumberFormat FORMAT = NumberFormat.getNumberInstance();

    private static final StringConverter<Number> CONVERTER = new StringConverter<Number>() {
        @Override
        public String toString(Number number) {
            return (number != null) ? FORMAT.format(number) : "";
        }

        @Override
        public Number fromString(String string) {
            ParsePosition position = new ParsePosition(0);
            Number number = FORMAT.parse(string, position);
            if (position.getIndex() < string.length()) {
                return null;
            } else {
                return number;
            }
        }
    };

    protected final ObjectProperty<Number> pCelsius = new SimpleObjectProperty<>(null);

    protected final ObjectProperty<Number> pFahrenheit = new SimpleObjectProperty<>(null);

    private TextField tfCelsius;

    private TextField tfFahrenheit;

    public final void start(Stage stage) {
        tfCelsius = new TextField("---");
        tfFahrenheit = new TextField("---");

        tfCelsius.textProperty().bindBidirectional(pCelsius, CONVERTER);
        tfFahrenheit.textProperty().bindBidirectional(pFahrenheit, CONVERTER);

        bind();

        HBox root = new HBox(10, tfCelsius, new Label("Celsius ="), tfFahrenheit, new Label("Fahrenheit"));
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10));

        stage.setScene(new Scene(root));
        stage.setTitle("Temperature Converter");
        stage.show();
    }

    protected abstract void bind();

    protected static double cToF(double celsius) {
        return (9/5d * celsius) + 32;
    }

    protected static double fToC(double fahrenheit) {
        return 5/9d * (fahrenheit - 32);
    }

}
