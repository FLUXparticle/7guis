package de.fluxparticle.sevenguis.gui5crud

import de.fluxparticle.fenja.FenjaSystem
import de.fluxparticle.fenja.expr.*
import de.fluxparticle.fenja.logger.PrintFenjaSystemLogger
import de.fluxparticle.fenja.operation.ListOperation
import de.fluxparticle.fenja.stream.EventStream
import de.fluxparticle.fenja.stream.EventStreamSource
import de.fluxparticle.fenja.stream.bind
import javafx.event.ActionEvent

/**
 * Created by sreinck on 05.08.18.
 */
class CrudKotlin : CrudBase() {

    private val system = FenjaSystem(PrintFenjaSystemLogger(System.out))

    private val sClickCreate: EventStreamSource<ActionEvent> by system.EventStreamSourceDelegate()

    private val sClickUpdate: EventStreamSource<ActionEvent> by system.EventStreamSourceDelegate()

    private val sClickDelete: EventStreamSource<ActionEvent> by system.EventStreamSourceDelegate()

    private var sChangeCreate: EventStream<ListOperation<Name>> by system.EventStreamRelayDelegate()

    private var sChangeUpdate: EventStream<ListOperation<Name>> by system.EventStreamRelayDelegate()

    private var sChangeDelete: EventStream<ListOperation<Name>> by system.EventStreamRelayDelegate()

    private var sChanges: EventStream<ListOperation<Name>> by system.EventStreamRelayDelegate()

    private val vName: InputExpr<String> by system.InputExprDelegate()

    private val vSurname: InputExpr<String> by system.InputExprDelegate()

    private val vSelectedIndex: InputExpr<Number> by system.InputExprDelegate()

    private var vFullName: Expr<Name> by system.OutputExprDelegate()

    private var vlNames: Expr<List<Name>> by system.OutputExprDelegate()

    private var vDebug: Expr<String> by system.OutputExprDelegate()

    private var vDisableEdit: Expr<Boolean> by system.OutputExprDelegate()

    override fun bind() {
        sClickCreate.bind(btCreate, ActionEvent.ACTION)
        sClickUpdate.bind(btUpdate, ActionEvent.ACTION)
        sClickDelete.bind(btDelete, ActionEvent.ACTION)
        vName           bind  tfName.textProperty()
        vSurname        bind  tfSurname.textProperty()
        vSelectedIndex  bind lvEntries.selectionModel.selectedIndexProperty()

        // -----

        vlNames         =  sChanges hold emptyList()

        vFullName       =  (vName combine vSurname) { name, surname -> Name(name, surname) }

        sChangeCreate   =  (sClickCreate snapshot vFullName) { _, name -> vlNames.buildAddOperation(name) }
        sChangeUpdate   =  (sClickUpdate snapshot vSelectedIndex and vFullName) { _, index, name -> vlNames.buildSetOperation(index.toInt(), name) }
        sChangeDelete   =  (sClickDelete snapshot vSelectedIndex) { _, index -> vlNames.buildRemoveOperation(index.toInt()) }

        sChanges        =  sChangeCreate orElse sChangeUpdate orElse sChangeDelete

        vDebug          =  vlNames map { it.toString() }

        vDisableEdit    =  vSelectedIndex.map { n -> n.toInt() < 0 }

        // -----

        lvEntries.items             bind  vlNames
        tfPrefix.textProperty()     bind  vDebug
        btUpdate.disableProperty()  bind  vDisableEdit
        btDelete.disableProperty()  bind  vDisableEdit

        system.finish()
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            launch(CrudKotlin::class.java, *args)
        }

    }

}
