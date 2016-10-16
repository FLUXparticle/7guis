package de.fluxparticle.sevenguis.gui3flightbooker;

import javafx.beans.value.ChangeListener;

public class FlightBookerCallback extends FlightBookerBase {

    protected void bind() {
        flightType.setValue(null);

        // A lot of inversion of control but in parts even terser than the "right" solution.
        flightType.valueProperty().addListener((v, o, n) ->
                returnDate.setDisable(n == FlightType.ONE_WAY_FLIGHT)
        );
        startDate.textProperty().addListener((v, o, n) ->
                startDate.setStyle(isDateString(n) ? STYLE_NORMAL : STYLE_ERROR)
        );
        returnDate.textProperty().addListener((v, o, n) ->
            returnDate.setStyle(isDateString(n) ? STYLE_NORMAL : STYLE_ERROR)
        );

        ChangeListener<Object> bookEnabledAction = (v, o, n) -> {
            if (flightType.getValue() == FlightType.ONE_WAY_FLIGHT) {
                book.setDisable(!isDateString(startDate.getText()));
            } else {
                book.setDisable(
                        !isDateString(startDate.getText()) ||
                        !isDateString(returnDate.getText()) ||
                        stringToDate(startDate.getText()).compareTo(stringToDate(returnDate.getText())) > 0);
            }
        };
        flightType.valueProperty().addListener(bookEnabledAction);
        startDate.textProperty().addListener(bookEnabledAction);
        returnDate.textProperty().addListener(bookEnabledAction);

        // It is important to set the value after the initializations of the callbacks.
        flightType.setValue(FlightType.ONE_WAY_FLIGHT);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
