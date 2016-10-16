package de.fluxparticle.sevenguis.gui3flightbooker;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.StringProperty;
import org.fxmisc.easybind.EasyBind;
import org.fxmisc.easybind.monadic.MonadicBinding;

public class FlightBookerFX extends FlightBookerBase {

    protected void bind() {
        BooleanBinding isOneWayFlight = flightType.valueProperty().isEqualTo(FlightType.ONE_WAY_FLIGHT);

        MonadicBinding<Boolean> isNotBookable = EasyBind.combine(isOneWayFlight, startDate.textProperty(), returnDate.textProperty(), (oneWayFlight, startDate, returnDate) -> {
            if (oneWayFlight) {
                return !isDateString(startDate);
            } else {
                return !isDateString(startDate)
                        || !isDateString(returnDate)
                        || stringToDate(startDate).compareTo(stringToDate(returnDate)) > 0;
            }
        });

        returnDate.disableProperty().bind( isOneWayFlight );
        startDate.styleProperty().bind( errorBinding(startDate.textProperty()) );
        returnDate.styleProperty().bind( errorBinding(returnDate.textProperty()) );
        book.disableProperty().bind( isNotBookable );
    }

    private static MonadicBinding<String> errorBinding(StringProperty stringProperty) {
        return EasyBind.map(stringProperty, s -> isDateString(s) ? STYLE_NORMAL : STYLE_ERROR);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
