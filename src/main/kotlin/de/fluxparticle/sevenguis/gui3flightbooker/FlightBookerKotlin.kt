package de.fluxparticle.sevenguis.gui3flightbooker

import de.fluxparticle.fenja.FenjaSystem
import de.fluxparticle.fenja.expr.*
import de.fluxparticle.sevenguis.gui1counter.CounterBase
import java.time.LocalDate

/**
 * Created by sreinck on 20.05.18.
 */
class FlightBookerKotlin : FlightBookerBase() {

    private val system = FenjaSystem()

    private val vFlightType: InputExpr<FlightType> by system.InputExprDelegate()
    private val vStartDate: InputExpr<String> by system.InputExprDelegate()
    private val vReturnDate: InputExpr<String> by system.InputExprDelegate()

    private var vOneWay: Expr<Boolean> by system.OutputExprDelegate()

    private var vStartDateAsDate: Expr<LocalDate?> by system.OutputExprDelegate()
    private var vReturnDateAsDate: Expr<LocalDate?> by system.OutputExprDelegate()

    private var vStartDateIsValid: Expr<Boolean> by system.OutputExprDelegate()
    private var vReturnDateIsValid: Expr<Boolean> by system.OutputExprDelegate()

    private var vDateRangeIsValid: Expr<Boolean> by system.OutputExprDelegate()

    private var vDatesValid: Expr<Boolean> by system.OutputExprDelegate()

    private var vStartDateStyle: Expr<String> by system.OutputExprDelegate()

    private var vReturnDateStyle: Expr<String> by system.OutputExprDelegate()

    private var vDisableButton: Expr<Boolean> by system.OutputExprDelegate()

    override fun bind() {
        vFlightType  bind  flightType.valueProperty()
        vStartDate   bind  startDate.textProperty()
        vReturnDate  bind  returnDate.textProperty()

        // -----

        vOneWay             =  vFlightType { v -> v == FlightType.ONE_WAY_FLIGHT }

        vStartDateAsDate    =  vStartDate { stringToDate(it) }
        vReturnDateAsDate   =  vReturnDate { stringToDate(it) }

        vStartDateIsValid   =  vStartDateAsDate { v -> v != null }
        vReturnDateIsValid  =  vReturnDateAsDate { v -> v != null }

        vDateRangeIsValid   =  (vStartDateAsDate..vReturnDateAsDate) { s, r -> s != null && r != null && s <= r }

        vDatesValid         =  (vOneWay and vStartDateIsValid) or vDateRangeIsValid

        vStartDateStyle     =  vStartDateIsValid { style(it) }
        vReturnDateStyle    =  vReturnDateIsValid { style(it) }
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
            CounterBase.launch(FlightBookerKotlin::class.java, *args)
        }

    }

}
