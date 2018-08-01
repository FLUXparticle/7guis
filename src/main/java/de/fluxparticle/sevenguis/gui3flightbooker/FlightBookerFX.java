package de.fluxparticle.sevenguis.gui3flightbooker;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.StringProperty;
import org.fxmisc.easybind.EasyBind;
import org.fxmisc.easybind.monadic.MonadicBinding;

import java.time.LocalDate;

public class FlightBookerFX extends FlightBookerBase {

    protected void bind() {
        BooleanBinding isOneWayFlight = flightType.valueProperty().isEqualTo(FlightType.ONE_WAY_FLIGHT);

        MonadicBinding<Boolean> isNotBookable = EasyBind.combine(isOneWayFlight, startDate.textProperty(), returnDate.textProperty(), (oneWayFlight, startDate, returnDate) -> {
            LocalDate localStartDate = stringToDate(startDate);
            if (oneWayFlight) {
                return localStartDate == null;
            } else {
                LocalDate localReturnDate = stringToDate(returnDate);
                return localStartDate == null || localReturnDate == null || localStartDate.compareTo(localReturnDate) > 0;
            }
        });

        returnDate.disableProperty().bind( isOneWayFlight );
        startDate.styleProperty().bind( errorBinding(startDate.textProperty()) );
        returnDate.styleProperty().bind( errorBinding(returnDate.textProperty()) );
        book.disableProperty().bind( isNotBookable );
    }

    private static MonadicBinding<String> errorBinding(StringProperty stringProperty) {
        return EasyBind.map(stringProperty, s -> (stringToDate(s) != null) ? STYLE_NORMAL : STYLE_ERROR);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
