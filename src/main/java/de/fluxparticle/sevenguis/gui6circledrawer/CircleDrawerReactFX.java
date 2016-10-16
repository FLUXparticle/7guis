package de.fluxparticle.sevenguis.gui6circledrawer;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;
import org.reactfx.EventStream;

import static org.reactfx.EventStreams.eventsOf;
import static org.reactfx.EventStreams.merge;

// https://gist.github.com/TomasMikula/b04dd89da1584597fa14
public class CircleDrawerReactFX extends CircleDrawerBase {

    CircleDrawerCanvasBase createCanvas() {
        return new CircleDrawerCanvasFX() {

            @Override
            protected void bind() {
                btUndo.setOnAction(e -> undo());
                btRedo.setOnAction(e -> redo());

                Button diameterEntry = new Button("Diameter...");
                Popup popup = new Popup();
                popup.getContent().add(diameterEntry);

                EventStream<MouseEvent> leftPressesToVoid = eventsOf(this, MouseEvent.MOUSE_PRESSED)
                        .filter(MouseEvent::isPrimaryButtonDown)
                        .filter(e -> getNearestCircleAt(e.getX(), e.getY()) == null);
                EventStream<Circle> addedCircles = leftPressesToVoid
                        .map(e -> new Circle((int) e.getX(), (int) e.getY()))
                        .hook(this::addCircle);
                EventStream<Circle> hoveredCircles = eventsOf(this, MouseEvent.MOUSE_MOVED)
                        .map(e -> getNearestCircleAt(e.getX(), e.getY()));
                merge(addedCircles, hoveredCircles).subscribe(this::draw);

                eventsOf(this, MouseEvent.MOUSE_PRESSED).subscribe(e -> { if (popup.isShowing()) popup.hide(); });
                EventStream<MouseEvent> rightPressesToCircle = eventsOf(this, MouseEvent.MOUSE_PRESSED)
                        .filter(MouseEvent::isPopupTrigger)
                        .filter(e -> getNearestCircleAt(e.getX(), e.getY()) != null);
                rightPressesToCircle.subscribe(e -> popup.show(this, e.getScreenX(), e.getScreenY()));
                EventStream<Circle> selectedCircles = rightPressesToCircle.map(e -> getNearestCircleAt(e.getX(), e.getY()));
                EventStream<ActionEvent> diameterEntryClicks = eventsOf(diameterEntry, ActionEvent.ACTION);
                selectedCircles.emitOn(diameterEntryClicks).subscribe(c -> {
                    popup.hide();
                    showDialog(c);
                });
            }

        };
    }

    public static void main(String[] args) {
        launch(args);
    }

}
