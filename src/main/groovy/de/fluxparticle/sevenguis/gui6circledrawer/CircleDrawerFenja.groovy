package de.fluxparticle.sevenguis.gui6circledrawer

import com.ajjpj.afoundation.collection.immutable.ARedBlackTreeMap
import de.fluxparticle.fenja.FenjaBuilder
import javafx.event.ActionEvent
import javafx.scene.input.MouseEvent
import javafx.stage.Stage
import javafx.stage.WindowEvent
import nz.sodium.Unit

import static com.ajjpj.afoundation.collection.immutable.AList.nil
import static de.fluxparticle.fenja.EventStream.never
import static de.fluxparticle.fenja.EventStream.streamOf
import static de.fluxparticle.fenja.TreeMapValue.apply
import static de.fluxparticle.fenja.TreeMapValue.nextIndex
import static de.fluxparticle.fenja.Value.switchS
import static de.fluxparticle.fenja.Value.valueOf
import static de.fluxparticle.sevenguis.gui6circledrawer.CircleDrawerCanvasBase.getNearestCircleAt
import static java.util.Comparator.naturalOrder

/**
 * Created by sreinck on 20.06.16.
 */
class CircleDrawerFenja extends CircleDrawerBase {

    private static def EMPTY = ARedBlackTreeMap.empty(naturalOrder())

    @Override
    def CircleDrawerCanvasBase createCanvas() {
        return new CircleDrawerCanvasBase() {
            @Override
            protected void bind() {
                new FenjaBuilder().build {
                    sClickUndo = streamOf(btUndo, ActionEvent.ACTION)
                    sClickRedo = streamOf(btRedo, ActionEvent.ACTION)
                    sMousePressed = streamOf(this, MouseEvent.MOUSE_PRESSED)
                    sMouseMoved = streamOf(this, MouseEvent.MOUSE_MOVED)

                    // -----

                    // Mouse Move

                    sMouseOverCircle = sMouseMoved
                            .map { e -> getNearestCircleAt(vCircles, e.x, e.y)?.value }

                    vHoveredCircle = (sMouseOverCircle * sAddCircle)
                            .filter { circle -> circle != vHoveredCircle }
                            .hold(null)

                    // Left Click

                    sMousePrimaryPressed = sMousePressed
                            .filter { it.isPrimaryButtonDown() }

                    sAddCircle = sMousePrimaryPressed
                            .map { e -> new Circle((int) e.getX(), (int) e.getY()) }

                    // Right Click

                    sSelectedEntry = sMousePressed
                            .filter { it.isPopupTrigger() }
                            .map { e -> getNearestCircleAt(vCircles, e.x, e.y) }
                            .filter { it != null }

                    sNewDialog = sSelectedEntry
                            .map { entry -> [entry.key, vDialogs.get(entry.key).getOrElse(null) ?: new Dialog(entry.value)] }

                    sAddDialog = sNewDialog
                            .filter { key, dialog -> !vDialogs.containsKey(key) }
                            .map { key, dialog -> [ (key) : dialog ] }

                    sDialogChanges = sAddDialog * sCloseDialog * sHideDialog

                    vDialogs = sDialogChanges
                            .map { changes -> apply(changes, vDialogs) }
                            .hold(EMPTY)

                    vDialogs.listen { println "dialogs = $it" }


                    vsCircleUpdateChanges = vDialogs.map { map ->
                        def sCircleUpdateChanges = never()

                        map.forEach { entry ->
                            sCircleUpdateChanges *= valueOf(entry.value.slider.valueProperty()).updates()
                                    .map { newDiameter -> [ (entry.key) : [ diameter : newDiameter ] ] }
                        }

                        return sCircleUpdateChanges
                    }

                    vsCircleUpdate = vDialogs.map { map ->
                        def sCircleUpdateChanges = never()

                        map.forEach { entry ->
                            sCircleUpdateChanges *= valueOf(entry.value.slider.valueChangingProperty()).updates()
                        }

                        return sCircleUpdateChanges
                    }

                    sCircleUpdate = switchS(vsCircleUpdate)

                    sCircleUpdateBegin = sCircleUpdate.filter { (Boolean) it }.map { Unit.UNIT }
                    sCircleUpdateEnd = sCircleUpdate.filter { !it }.map { Unit.UNIT }

                    vsCloseDialog = vDialogs.map { map ->
                        def sCloseDialog = never()

                        map.forEach { entry ->
                            sCloseDialog *= streamOf(entry.value.stage as Stage, WindowEvent.WINDOW_CLOSE_REQUEST)
                                    .map { [ (entry.key) : null ] }
                        }

                        return sCloseDialog
                    }

                    sCloseDialog = switchS(vsCloseDialog)

                    // Add Circles

                    sCircleAddChanges = sAddCircle
                            .map { circle -> [ (nextIndex(vCircles)) : circle ] }

                    sCircleChanges = (sCircleAddChanges * switchS(vsCircleUpdateChanges))

                    sCircleChanges.listen { println "circleChange = $it" }

                    sEditCircles = sCircleChanges
                            .map { changes -> apply(changes, vCircles) }

                    // Undo

                    sDoUndo = sClickUndo.filter { vUndoStack != nil }

                    sPushUndoStack = (sAddCircle * sDoRedo * sCircleUpdateBegin).map { circles -> vUndoStack.cons(vCircles) }
                    sPopUndoStack = sDoUndo.map { vUndoStack.tail() }
                    vUndoStack = (sPushUndoStack * sPopUndoStack).hold(nil)

                    // Redo

                    sDoRedo = sClickRedo.filter { vRedoStack != nil }

                    sPushRedoStack = sDoUndo.map { vRedoStack.cons(vCircles) }
                    sPopRedoStack = sDoRedo.map { vRedoStack.tail() }
                    sClearRedoStack = (sAddCircle * sCircleUpdateEnd).map { nil }

                    vRedoStack = (sPushRedoStack * sPopRedoStack * sClearRedoStack).hold(nil)

                    // Undo + Redo

                    sUndoCircles = sDoUndo.map { vUndoStack.head() }
                    sRedoCircles = sDoRedo.map { vRedoStack.head() }
                    sUndoRedoCircles = (sUndoCircles * sRedoCircles)

                    // Circles

                    sNextCircles = sEditCircles * sUndoRedoCircles

                    sUnusedDialog = sNextCircles.map { circles ->
                        def changeDialogs = [:]
                        vDialogs.forEach { entry ->
                            if (!circles.containsKey(entry.key)) {
                                changeDialogs[entry.key] = entry.updateValue
                            }
                        }
                        return changeDialogs
                    }

                    sHideDialog = sUnusedDialog.map { map ->
                        def hideDialogs = [:]
                        map.forEach { key, dialog ->
                            hideDialogs[key] = null
                        }
                        return hideDialogs
                    }

                    vCircles = sNextCircles.hold(EMPTY)

                    vDocWithContext = (vHoveredCircle ** vCircles) { hovered, circles -> [circles, hovered] }

                    // -----

                    vDocWithContext.listen { circles, hovered ->
                        println "draw!"
                        draw(circles.values(), hovered)
                    }

                    sNewDialog.listen { key, dialog ->
                        dialog.stage.show()
                        dialog.stage.requestFocus()
                    }

                    sUnusedDialog.listen { map ->
                        map*.value*.stage*.hide()
                    }
                }
            }
        }
    }

    public static void main(String... args) {
        launch(CircleDrawerFenja, args)
    }

}
