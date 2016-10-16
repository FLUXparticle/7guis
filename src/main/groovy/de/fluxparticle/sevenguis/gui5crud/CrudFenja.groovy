package de.fluxparticle.sevenguis.gui5crud

import de.fluxparticle.fenja.FenjaBuilder
import de.fluxparticle.fenja.TreeMapValue
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import org.fxmisc.easybind.EasyBind

import static de.fluxparticle.fenja.EventStream.streamOf
import static de.fluxparticle.fenja.TreeMapValue.nextIndex
import static de.fluxparticle.fenja.Value.constValue
import static de.fluxparticle.fenja.Value.valueOf
import static java.util.Arrays.asList

/**
 * Created by sreinck on 16.06.16.
 */
class CrudFenja extends CrudBase {

    private static <T> Map<Integer, T> mapOf(T... values) {
        Map<Integer, T> map = new HashMap<>();

        asList(values).eachWithIndex { T entry, int index -> map.put(index, entry) }

        return map
    }

    @Override
    protected void bind() {
        new FenjaBuilder().build {
            vPrefix         =  valueOf(tfPrefix.textProperty())
            vName           =  valueOf(tfName.textProperty())
            vSurname        =  valueOf(tfSurname.textProperty())
            vSelectedName   =  valueOf(lvEntries.getSelectionModel().selectedItemProperty())
            vSelectedIndex  =  valueOf(lvEntries.getSelectionModel().selectedIndexProperty())

            sClickCreate    =  streamOf(btCreate, ActionEvent.ACTION)
            sClickUpdate    =  streamOf(btUpdate, ActionEvent.ACTION)
            sClickDelete    =  streamOf(btDelete, ActionEvent.ACTION)

            // -----
            
            tmvNames = new TreeMapValue<Integer, Name>(sChanges, mapOf(NAMES))

            vPredicate             =  vPrefix.map { prefix -> { item -> item.startsWith(prefix) } }
            vFullname              =  (vName ** vSurname) { name, surname -> new Name(name, surname) }

            vTreeMapNames          =  constValue(tmvNames);

            vTreeMapFilteredNames  =  (vTreeMapNames ** vPredicate) { names, predicate -> names.filterByValue(predicate) }

            vListFilteredNames     =  vTreeMapFilteredNames.map { tmv -> tmv.entriesAsList() }

            sChangeCreate          =  sClickCreate.map { e -> [ (nextIndex(tmvNames)) : vFullname ] }
            sChangeUpdate          =  sClickUpdate.map { e -> [ (vListFilteredNames[vSelectedIndex].key) : vFullname ] }
            sChangeDelete          =  sClickDelete.map { e -> [ (vListFilteredNames[vSelectedIndex].key) : null ] }

            sChanges               =  sChangeCreate * sChangeUpdate * sChangeDelete

            vDisableEdit           =  vSelectedName.map { n -> n == null }

            // -----

            lvEntries.itemsProperty()   <<  vListFilteredNames.map { ObservableList ol -> EasyBind.map(ol) { e -> e?.value } }
            btDelete.disableProperty()  <<  vDisableEdit
            btUpdate.disableProperty()  <<  vDisableEdit
        }
        System.gc()
    }

    static void main(String... args) {
        launch(CrudFenja.class, args)
    }

}
