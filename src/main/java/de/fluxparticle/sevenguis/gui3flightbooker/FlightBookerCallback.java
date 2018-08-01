package de.fluxparticle.sevenguis.gui3flightbooker;

import javafx.beans.value.ChangeListener;

import java.time.LocalDate;

public class FlightBookerCallback extends FlightBookerBase {

    protected void bind() {
        // This value must be reinitialized so all callback will be called
        flightType.setValue(null);

        flightType.valueProperty().addListener((v, o, n) ->
                returnDate.setDisable(n == FlightType.ONE_WAY_FLIGHT)
        );
        startDate.textProperty().addListener((v, o, n) ->
                startDate.setStyle((stringToDate(n) != null) ? STYLE_NORMAL : STYLE_ERROR)
        );
        returnDate.textProperty().addListener((v, o, n) ->
            returnDate.setStyle((stringToDate(n) != null) ? STYLE_NORMAL : STYLE_ERROR)
        );

        ChangeListener<Object> bookEnabledAction = (v, o, n) -> {
            LocalDate localStartDate = stringToDate(startDate.getText());
            if (flightType.getValue() == FlightType.ONE_WAY_FLIGHT) {
                book.setDisable(localStartDate == null);
            } else {
                LocalDate localReturnDate = stringToDate(returnDate.getText());
                book.setDisable(localStartDate == null || localReturnDate == null || localStartDate.compareTo(localReturnDate) > 0
                );
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
