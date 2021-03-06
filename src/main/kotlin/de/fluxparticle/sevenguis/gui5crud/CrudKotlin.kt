package de.fluxparticle.sevenguis.gui5crud

import de.fluxparticle.fenja.FenjaSystem.Companion.build
import de.fluxparticle.fenja.FenjaSystem.FilterListExpr
import de.fluxparticle.fenja.FenjaSystem.ListExpr
import de.fluxparticle.fenja.bind
import de.fluxparticle.fenja.expr.bind
import de.fluxparticle.fenja.expr.buildAddOperation
import de.fluxparticle.fenja.expr.buildRemoveOperation
import de.fluxparticle.fenja.expr.buildSetOperation
import de.fluxparticle.fenja.logger.PrintFenjaSystemLogger
import javafx.collections.ListChangeListener
import javafx.event.ActionEvent.ACTION

/**
 * Created by sreinck on 05.08.18.
 */
class CrudKotlin : CrudBase() {

    private val logger = PrintFenjaSystemLogger(System.out)

    override fun bind() {
        build(logger) {
            val sClickCreate by eventsOf(btCreate, ACTION)
            val sClickUpdate by eventsOf(btUpdate, ACTION)
            val sClickDelete by eventsOf(btDelete, ACTION)

            val vPrefix         by  tfPrefix.textProperty()
            val vName           by  tfName.textProperty()
            val vSurname        by  tfSurname.textProperty()
            val vSelectedIndex     by  lvEntries.selectionModel.selectedIndexProperty()

            // -----

            val vFullName       =  (vName combine vSurname) { name, surname -> Name(name, surname) }

            var vlNames: ListExpr<Name> by loop()
            var vlFilterNames: FilterListExpr<Name> by loop()

            val sChangeCreate   by  sClickCreate map { _ -> vlNames.buildAddOperation(vFullName.sample()) }
            val sChangeUpdate   by  sClickUpdate map { _ -> vlFilterNames.buildSetOperation(vSelectedIndex.sample().toInt(), vFullName.sample()) }
            val sChangeDelete   by  sClickDelete map { _ -> vlFilterNames.buildRemoveOperation(vSelectedIndex.sample().toInt()) }

            val sFilterChanges  by  sChangeUpdate orElse sChangeDelete

            val sChanges        by  sChangeCreate orElse (sFilterChanges map { vlFilterNames.reverseTransform(it) })

            vlNames         =  sChanges hold emptyList()

            val vPredicate      by  vPrefix map { prefix -> { name: Name -> name.startsWith(prefix) } }

            vlFilterNames   =  vlNames filter vPredicate

            val vDisableUpdateDelete  by  vSelectedIndex.map { n -> n < 0 }

            // -----

            lvEntries.items             bind  vlFilterNames
            btUpdate.disableProperty()  bind  vDisableUpdateDelete
            btDelete.disableProperty()  bind  vDisableUpdateDelete

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
        }
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            launch(CrudKotlin::class.java, *args)
        }

    }

}
