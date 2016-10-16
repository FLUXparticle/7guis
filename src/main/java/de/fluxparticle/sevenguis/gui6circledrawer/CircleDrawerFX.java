package de.fluxparticle.sevenguis.gui6circledrawer;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;

public class CircleDrawerFX extends CircleDrawerBase {

    CircleDrawerCanvasBase createCanvas() {
        return new CircleDrawerCanvasFX() {

            private Circle hovered;

            @Override
            protected void bind() {
                hovered = null;

                btUndo.setOnAction(e -> undo());
                btRedo.setOnAction(e -> redo());

                {
                    Popup popup = new Popup();
                    {
                        Button diameterEntry = new Button("Diameter...");
                        popup.getContent().add(diameterEntry);

                        diameterEntry.setOnAction(e -> {
                            popup.hide();
                            showDialog(hovered);
                        });
                    }

                    setOnMousePressed((MouseEvent e) -> {
                        if (e.isPrimaryButtonDown() && hovered == null) {
                            addCircle(new Circle((int) e.getX(), (int) e.getY()));
                            getOnMouseMoved().handle(e);
                        }
                        if (popup.isShowing()) popup.hide();
                        if (e.isPopupTrigger() && hovered != null)
                            popup.show(this, e.getScreenX(), e.getScreenY());
                    });
                }

                setOnMouseMoved((MouseEvent e) -> {
                    hovered = getNearestCircleAt(e.getX(), e.getY());
                    draw(hovered);
                });
            }

        };
    }

    public static void main(String[] args) {
        launch(args);
    }

}
