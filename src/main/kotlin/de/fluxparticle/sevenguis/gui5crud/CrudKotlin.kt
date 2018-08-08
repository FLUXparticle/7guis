package de.fluxparticle.sevenguis.gui5crud

import de.fluxparticle.fenja.FenjaSystem
import de.fluxparticle.fenja.expr.*
import de.fluxparticle.fenja.logger.PrintFenjaSystemLogger
import de.fluxparticle.fenja.operation.ListOperation
import de.fluxparticle.fenja.stream.InputEventStream
import de.fluxparticle.fenja.stream.UpdateEventStream
import de.fluxparticle.fenja.stream.bind
import javafx.collections.ListChangeListener
import javafx.event.ActionEvent

/**
 * Created by sreinck on 05.08.18.
 */
class CrudKotlin : CrudBase() {

    private val system = FenjaSystem(PrintFenjaSystemLogger(System.out))

    private val sClickCreate: InputEventStream<ActionEvent> by system.InputEventStreamDelegate()

    private val sClickUpdate: InputEventStream<ActionEvent> by system.InputEventStreamDelegate()

    private val sClickDelete: InputEventStream<ActionEvent> by system.InputEventStreamDelegate()

    private var sChangeCreate: UpdateEventStream<ListOperation<Name>> by system.UpdateEventStreamDelegate()

    private var sChangeUpdate: UpdateEventStream<ListOperation<Name>> by system.UpdateEventStreamDelegate()

    private var sChangeDelete: UpdateEventStream<ListOperation<Name>> by system.UpdateEventStreamDelegate()

    private var sChanges: UpdateEventStream<ListOperation<Name>> by system.UpdateEventStreamDelegate()

    private val vPrefix: InputExpr<String> by system.InputExprDelegate()

    private val vName: InputExpr<String> by system.InputExprDelegate()

    private val vSurname: InputExpr<String> by system.InputExprDelegate()

    private val vSelectedIndex: InputExpr<Number> by system.InputExprDelegate()

    private var vFullName: UpdateExpr<Name> by system.UpdateExprDelegate()

    private var vlNames: ListExpr<Name> by system.UpdateExprDelegate()

    private var vlFilterNames: ListExpr<Name> by system.UpdateExprDelegate()

    private var vPredicate: UpdateExpr<(Name) -> Boolean> by system.UpdateExprDelegate()

    private var vDisableUpdate: UpdateExpr<Boolean> by system.UpdateExprDelegate()

    private var vDisableDelete: UpdateExpr<Boolean> by system.UpdateExprDelegate()

    override fun bind() {
        tfPrefix.text = "P"

        sClickCreate.bind(btCreate, ActionEvent.ACTION)
        sClickUpdate.bind(btUpdate, ActionEvent.ACTION)
        sClickDelete.bind(btDelete, ActionEvent.ACTION)

        vPrefix         bind  tfPrefix.textProperty()
        vName           bind  tfName.textProperty()
        vSurname        bind  tfSurname.textProperty()
        vSelectedIndex  bind  lvEntries.selectionModel.selectedIndexProperty()

        // -----

        sChangeCreate   =  sClickCreate map { _ -> vlNames.buildAddOperation(vFullName.sample()) }
        sChangeUpdate   =  sClickUpdate map { _ -> vlNames.buildSetOperation(vSelectedIndex.sample().toInt(), vFullName.sample()) }
        sChangeDelete   =  sClickDelete map { _ -> vlNames.buildRemoveOperation(vSelectedIndex.sample().toInt()) }

        sChanges        =  sChangeCreate orElse sChangeUpdate orElse sChangeDelete

        vlNames         =  sChanges hold emptyList()

        vPredicate      =  vPrefix map { prefix -> { name: Name -> name.startsWith(prefix) } }

        vlFilterNames   =  vlNames filter vPredicate

        vFullName       =  (vName combine vSurname) { name, surname -> Name(name, surname) }

        vDisableUpdate  =  vSelectedIndex.map { n -> n.toInt() < 0 }
        vDisableDelete  =  vSelectedIndex.map { n -> n.toInt() < 0 }

        // -----

        lvEntries.items             bind  vlFilterNames
        btUpdate.disableProperty()  bind  vDisableUpdate
        btDelete.disableProperty()  bind  vDisableDelete

        lvEntries.items.addListener(ListChangeListener<Name> { c ->
            while (c.next()) {
                when {
                    c.wasPermutated() -> println("permutated ${c.to - c.from} at ${c.from}")
                    c.wasUpdated()    -> println("updated ${c.to - c.from} at ${c.from}")
                    c.wasReplaced()   -> println("replaced ${c.to - c.from} at ${c.from}")
                    c.wasAdded()      -> println("added ${c.to - c.from} at ${c.from}")
                    c.wasRemoved()    -> println("removed ${c.removedSize} at ${c.from}")
                }
            }
        })

        system.finish()
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            launch(CrudKotlin::class.java, *args)
        }

    }

}
