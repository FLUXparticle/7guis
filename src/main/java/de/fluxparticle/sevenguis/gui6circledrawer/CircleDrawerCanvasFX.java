package de.fluxparticle.sevenguis.gui6circledrawer;

import com.ajjpj.afoundation.collection.immutable.AMapEntry;
import de.fluxparticle.sevenguis.gui6circledrawer.undo.UndoManager;
import de.fluxparticle.sevenguis.gui6circledrawer.undo.UndoableEdit;
import javafx.event.EventHandler;
import javafx.scene.control.Slider;
import javafx.stage.WindowEvent;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;

/**
 * Created by sreinck on 17.06.16.
 */
abstract class CircleDrawerCanvasFX extends CircleDrawerCanvasBase {

    private List<Circle> circles = new ArrayList<>();

    private UndoManager undoManager = new UndoManager();

    CircleDrawerCanvasFX() {
        super();
        draw();
    }

    private void draw() { draw(null); }

    void draw(Circle hovered) { draw(circles, hovered); }

    void addCircle(Circle circle) {
        undoManager.addEdit(new CreateCircleEdit(circle));
        circles.add(circle);
    }

    Circle getNearestCircleAt(double x, double y) {
        Stream<AMapEntry<Integer, Circle>> stream = range(0, circles.size())
                .mapToObj(i -> new AMapEntry<Integer, Circle>() {
                    @Override
                    public Integer getKey() {
                        return i;
                    }

                    @Override
                    public Circle getValue() {
                        return circles.get(i);
                    }
                });

        AMapEntry<Integer, Circle> entry = getNearestCircleAt(stream::iterator, x, y);

        return entry != null ? entry.getValue() : null;
    }

    void showDialog(Circle selected) {
        Dialog dialog = new Dialog(selected);

        dialog.getSlider().valueProperty().addListener((v, o, n) -> {
            selected.setDiameter(n.intValue());
            draw();
        });

        int oldDiameter = selected.getDiameter();
        dialog.getStage().setOnCloseRequest(e -> undoManager.addEdit(
                new ChangeDiameterEdit(selected, oldDiameter, selected.getDiameter())));

        dialog.getStage().show();
    }

    void undo() {
        undoManager.undo();
        draw();
    }

    void redo() {
        undoManager.redo();
        draw();
    }

    private class CreateCircleEdit extends UndoableEdit {

        private Circle circle;

        CreateCircleEdit(Circle circle) {
            this.circle = circle;
        }

        public void undo() {
            circles.remove(circle);
        }

        public void redo() {
            circles.add(circle);
        }

    }

    private class ChangeDiameterEdit extends UndoableEdit {

        private Circle circle;
        private int oldDiameter, newDiameter;

        ChangeDiameterEdit(Circle circle, int oldDiameter, int newDiameter) {
            this.circle = circle;
            this.oldDiameter = oldDiameter;
            this.newDiameter = newDiameter;
        }

        public void undo() {
            circle.setDiameter(oldDiameter);
        }

        public void redo() {
            circle.setDiameter(newDiameter);
        }

    }

}
