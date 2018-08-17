package de.fluxparticle.sevenguis.gui3flightbooker

import de.fluxparticle.fenja.FenjaSystem
import de.fluxparticle.fenja.logger.PrintFenjaSystemLogger

/**
 * Created by sreinck on 20.05.18.
 */
class FlightBookerKotlin : FlightBookerBase() {

    private val logger = PrintFenjaSystemLogger(System.out)

    override fun bind() {
        FenjaSystem.build(logger) {
            val vFlightType by flightType.valueProperty()
            val vStartDate by startDate.textProperty()
            val vReturnDate by returnDate.textProperty()

            // -----

            val vOneWay by vFlightType map { v -> v == FlightType.ONE_WAY_FLIGHT }

            val vStartDateAsDate by vStartDate map { stringToDate(it) }
            val vReturnDateAsDate by vReturnDate map { stringToDate(it) }

            val vStartDateIsValid by vStartDateAsDate map { v -> v != null }
            val vReturnDateIsValid by vReturnDateAsDate map { v -> v != null }

            val vDateRangeIsValid by (vStartDateAsDate combine vReturnDateAsDate) { s, r -> s != null && r != null && s <= r }

            val vDatesValid by (vOneWay and vStartDateIsValid) or vDateRangeIsValid

            val vStartDateStyle by vStartDateIsValid map { style(it) }
            val vReturnDateStyle by vReturnDateIsValid map { style(it) }

            // -----

            returnDate.disableProperty() bind vOneWay
            startDate.styleProperty() bind vStartDateStyle
            returnDate.styleProperty() bind vReturnDateStyle
            book.disableProperty() bind !vDatesValid
        }
    }

    companion object {

        private fun style(v: Boolean) = if (v) STYLE_NORMAL else STYLE_ERROR

        @JvmStatic
        fun main(args: Array<String>) {
            launch(FlightBookerKotlin::class.java, *args)
        }

    }

}
