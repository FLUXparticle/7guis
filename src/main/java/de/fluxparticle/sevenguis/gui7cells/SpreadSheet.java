package de.fluxparticle.sevenguis.gui7cells;

import de.fluxparticle.sevenguis.gui7cells.formula.Cell;
import de.fluxparticle.sevenguis.gui7cells.formula.Formula;
import de.fluxparticle.sevenguis.gui7cells.formula.Model;
import de.fluxparticle.sevenguis.gui7cells.formula.Textual;
import de.fluxparticle.sevenguis.gui7cells.parser.Parser;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

// Although Swing is worse in almost every respect compared to JavaFX its TableView
// was easier to customize to a spreadsheet than JavaFX's TableView.
// Caveat: You must never cancel an edit, i.e. always commit with enter. I tried and
// tried to make it work but its seems to me that there is a bug in JavaFX somewhere.
public class SpreadSheet extends ScrollPane {

    public SpreadSheet(int height, int width) {
        Model model = new Model(height, width);

        HBox hBox = new HBox();

        {
            ListView<String> rowHeaders = new ListView<>();
            rowHeaders.getItems().add("");
            for (int i = 0; i < height; i++) {
                rowHeaders.getItems().add(i + "");
            }
//            ScrollPane scrolledRowHeaders = new ScrollPane(rowHeaders);
//            rowHeaders.setHbarPolicy(ScrollBarPolicy.NEVER);
//            rowHeaders.setVbarPolicy(ScrollBarPolicy.NEVER);

/*
            table.getChildrenUnmodifiable().addListener((ListChangeListener<Node>) c -> {
                ScrollBar vbarTable = (ScrollBar) table.lookup(".scroll-bar:vertical");
                ScrollBar vbarRowHeaders = (ScrollBar) scrolledRowHeaders.lookup(".scroll-bar:vertical");
                if (vbarRowHeaders != null && vbarTable != null)
                    vbarTable.valueProperty().bindBidirectional(vbarRowHeaders.valueProperty());
            });
*/

            hBox.getChildren().add(rowHeaders);
        }

        {
            TableView<ObservableList<de.fluxparticle.sevenguis.gui7cells.formula.Cell>> table = new TableView<>();
            table.setEditable(true);
            table.setItems(model.getCellsAsObservableList());

            // The following is very very JavaFX specific.

            for (char w = 'A'; w < 'A' + width; w++) {
                TableColumn<ObservableList<Cell>, String> column = new TableColumn<>(w + "");

                column.setSortable(false);
                column.setMinWidth(50);
                column.setCellFactory(TextFieldTableCell.forTableColumn());
                final char w0 = w;
//                column.setCellValueFactory(param -> param.getValue().get(w0 - 'A').textProperty());
                column.setOnEditStart(event -> {
                    int row = event.getTablePosition().getRow();
                    int col = event.getTablePosition().getColumn();
                    Cell c = model.getCell(row, col);
//                    c.setShowUserData(true);
                });
                // A minefield of weird behavior...
                // Somehow changing the value of the text property of a cell here destroys the table.
/*
            column.setOnEditCancel(event -> {
                Cell c = model.getCells()[ro][co];
                c.setShowUserData(false);
            });
*/
                column.setOnEditCommit(event -> {
                    int row = event.getTablePosition().getRow();
                    int col = event.getTablePosition().getColumn();
                    Cell c = model.getCell(row, col);
//                    c.setUserData(event.getNewValue());
                    Formula formula;
                    try {
                        formula = Parser.parse(event.getNewValue());
                    } catch (Exception e) {
                        formula = new Textual(e.getMessage());
                    }
                    c.setFormula(formula);
                });
                table.getColumns().add(column);

            }

            HBox.setHgrow(table, Priority.ALWAYS);
            hBox.getChildren().add(table);
        }

        setContent(hBox);
    }
}
