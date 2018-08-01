package de.fluxparticle.sevenguis.gui3flightbooker

import de.fluxparticle.fenja.FenjaBuilder

import static de.fluxparticle.fenja.Value.valueOf
import static de.fluxparticle.sevenguis.gui3flightbooker.FlightBookerBase.FlightType.ONE_WAY_FLIGHT

/**
 * Created by sreinck on 16.06.16.
 */
class FlightBookerGroovy extends FlightBookerBase {

    @Override
    protected void bind() {
        new FenjaBuilder().build {
            vFlightType  =  valueOf(flightType.valueProperty())
            vStartDate   =  valueOf(startDate.textProperty())
            vReturnDate  =  valueOf(returnDate.textProperty())

            // -----
            
            vOneWay             =  vFlightType.map { v -> v == ONE_WAY_FLIGHT }

            vStartDateAsDate    =  vStartDate.map { txt -> stringToDate(txt) }
            vReturnDateAsDate   =  vReturnDate.map { txt -> stringToDate(txt) }

            vStartDateIsValid   =  vStartDateAsDate.map { v -> v != null }
            vReturnDateIsValid  =  vReturnDateAsDate.map { v -> v != null }

            vDateRangeIsValid   =  (vStartDateAsDate ** vReturnDateAsDate) { s, r -> s != null && r != null && s.compareTo(r) <= 0 }

            vDatesValid         =  (vOneWay ** vStartDateIsValid ** vDateRangeIsValid) { o, s, r -> (o && s) || r }
            
            // -----

            returnDate.disableProperty()  <<  vOneWay
            startDate.styleProperty()     <<  vStartDateIsValid.map { v -> v ? STYLE_NORMAL : STYLE_ERROR }
            returnDate.styleProperty()    <<  vReturnDateIsValid.map { v -> v ? STYLE_NORMAL : STYLE_ERROR }
            book.disableProperty()        <<  vDatesValid.map { v -> !v }
        }
    }

    static void main(String... args) {
        launch(FlightBookerGroovy.class, args)
    }

}
