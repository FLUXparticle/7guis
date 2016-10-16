package de.fluxparticle.sevenguis.gui3flightbooker;

import org.reactfx.EventStream;

import java.time.LocalDate;

import static org.reactfx.EventStreams.combine;
import static org.reactfx.EventStreams.valuesOf;

public class FlightBookerReactFX extends FlightBookerBase {

    protected void bind() {
        EventStream<Boolean> oneWay = valuesOf(flightType.valueProperty()).map(v -> v == FlightType.ONE_WAY_FLIGHT);

        EventStream<LocalDate> startDateDate = valuesOf(startDate.textProperty())
                .map(txt -> isDateString(txt) ? stringToDate(txt) : null);
        EventStream<LocalDate> returnDateDate = valuesOf(returnDate.textProperty())
                .map(txt -> isDateString(txt) ? stringToDate(txt) : null);

        EventStream<Boolean> startDateValid = startDateDate.map(v -> v != null);
        EventStream<Boolean> returnDateValid = returnDateDate.map(v -> v != null);

        EventStream<Boolean> dateRangeValid = combine(startDateDate, returnDateDate)
                .map((s, r) -> s != null && r != null && s.compareTo(r) <= 0);

        EventStream<Boolean> datesValid = combine(oneWay, startDateValid, dateRangeValid)
                .map((o, s, r) -> (o && s) || r);

        oneWay.feedTo(returnDate.disableProperty());
        startDateValid.map(v -> v ? STYLE_NORMAL : STYLE_ERROR).feedTo(startDate.styleProperty());
        returnDateValid.map(v -> v ? STYLE_NORMAL : STYLE_ERROR).feedTo(returnDate.styleProperty());
        datesValid.map(v -> !v).feedTo(book.disableProperty());
    }

    public static void main(String[] args) {
        launch(args);
    }

}
