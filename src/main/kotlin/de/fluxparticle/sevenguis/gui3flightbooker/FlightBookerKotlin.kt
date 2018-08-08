package de.fluxparticle.sevenguis.gui3flightbooker

import de.fluxparticle.fenja.FenjaSystem
import de.fluxparticle.fenja.expr.*
import de.fluxparticle.fenja.logger.PrintFenjaSystemLogger
import java.time.LocalDate

/**
 * Created by sreinck on 20.05.18.
 */
class FlightBookerKotlin : FlightBookerBase() {

    private val system = FenjaSystem(PrintFenjaSystemLogger(System.out))

    private val vFlightType: InputExpr<FlightType> by system.InputExprDelegate()
    private val vStartDate: InputExpr<String> by system.InputExprDelegate()
    private val vReturnDate: InputExpr<String> by system.InputExprDelegate()

    private var vOneWay: UpdateExpr<Boolean> by system.UpdateExprDelegate()

    private var vStartDateAsDate: UpdateExpr<LocalDate?> by system.UpdateExprDelegate()
    private var vReturnDateAsDate: UpdateExpr<LocalDate?> by system.UpdateExprDelegate()

    private var vStartDateIsValid: UpdateExpr<Boolean> by system.UpdateExprDelegate()
    private var vReturnDateIsValid: UpdateExpr<Boolean> by system.UpdateExprDelegate()

    private var vDateRangeIsValid: UpdateExpr<Boolean> by system.UpdateExprDelegate()

    private var vDatesValid: UpdateExpr<Boolean> by system.UpdateExprDelegate()

    private var vStartDateStyle: UpdateExpr<String> by system.UpdateExprDelegate()

    private var vReturnDateStyle: UpdateExpr<String> by system.UpdateExprDelegate()

    private var vDisableButton: UpdateExpr<Boolean> by system.UpdateExprDelegate()

    override fun bind() {
        vFlightType  bind  flightType.valueProperty()
        vStartDate   bind  startDate.textProperty()
        vReturnDate  bind  returnDate.textProperty()

        // -----

        vOneWay             =  vFlightType map { v -> v == FlightType.ONE_WAY_FLIGHT }

        vStartDateAsDate    =  vStartDate map { stringToDate(it) }
        vReturnDateAsDate   =  vReturnDate map { stringToDate(it) }

        vStartDateIsValid   =  vStartDateAsDate map { v -> v != null }
        vReturnDateIsValid  =  vReturnDateAsDate map { v -> v != null }

        vDateRangeIsValid   =  (vStartDateAsDate combine vReturnDateAsDate) { s, r -> s != null && r != null && s <= r }

        vDatesValid         =  (vOneWay and vStartDateIsValid) or vDateRangeIsValid

        vStartDateStyle     =  vStartDateIsValid map { style(it) }
        vReturnDateStyle    =  vReturnDateIsValid map { style(it) }
        vDisableButton      =  !vDatesValid

        // -----

        returnDate.disableProperty()  bind  vOneWay
        startDate.styleProperty()     bind  vStartDateStyle
        returnDate.styleProperty()    bind  vReturnDateStyle
        book.disableProperty()        bind  vDisableButton

        system.finish()
    }

    companion object {

        private fun style(v: Boolean) = if (v) STYLE_NORMAL else STYLE_ERROR

        @JvmStatic
        fun main(args: Array<String>) {
            launch(FlightBookerKotlin::class.java, *args)
        }

    }

}
