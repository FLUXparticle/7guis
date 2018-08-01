package de.fluxparticle.sevenguis.gui3flightbooker;

import org.reactfx.EventStream;

import java.time.LocalDate;
import java.util.Objects;

import static org.reactfx.EventStreams.combine;
import static org.reactfx.EventStreams.valuesOf;

public class FlightBookerReactFX extends FlightBookerBase {

    protected void bind() {
        EventStream<FlightType> vFlightType = valuesOf(flightType.valueProperty());
        EventStream<String> vStartDate = valuesOf(startDate.textProperty());
        EventStream<String> vReturnDate = valuesOf(returnDate.textProperty());

        // -----

        EventStream<Boolean> oneWay = vFlightType.map(v -> v == FlightType.ONE_WAY_FLIGHT);

        EventStream<LocalDate> startDateDate = vStartDate
                .map(FlightBookerBase::stringToDate);
        EventStream<LocalDate> returnDateDate = vReturnDate
                .map(FlightBookerBase::stringToDate);

        EventStream<Boolean> startDateValid = startDateDate.map(Objects::nonNull);
        EventStream<Boolean> returnDateValid = returnDateDate.map(Objects::nonNull);

        EventStream<Boolean> dateRangeValid = combine(startDateDate, returnDateDate)
                .map((s, r) -> s != null && r != null && s.compareTo(r) <= 0);

        EventStream<Boolean> datesValid = combine(oneWay, startDateValid, dateRangeValid)
                .map((o, s, r) -> (o && s) || r);

        // -----

        oneWay.feedTo(returnDate.disableProperty());
        startDateValid.map(v -> v ? STYLE_NORMAL : STYLE_ERROR).feedTo(startDate.styleProperty());
        returnDateValid.map(v -> v ? STYLE_NORMAL : STYLE_ERROR).feedTo(returnDate.styleProperty());
        datesValid.map(v -> !v).feedTo(book.disableProperty());
    }

    public static void main(String[] args) {
        launch(args);
    }

}
