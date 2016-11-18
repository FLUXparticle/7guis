package de.fluxparticle.sevenguis.gui7cells;

import de.fluxparticle.sevenguis.gui7cells.formula.Cell;
import de.fluxparticle.sevenguis.gui7cells.formula.Formula;
import de.fluxparticle.sevenguis.gui7cells.formula.Model;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.controlsfx.control.spreadsheet.*;

import java.util.stream.IntStream;

import static java.util.stream.Collectors.toCollection;

public class CellsFX extends Application {

    private static final int ROWS = 100;

    private static final int COLS = 26;

    private Model model = new Model(ROWS, COLS);

    public void start(Stage stage) {
        GridBase grid = new GridBase(ROWS, COLS);

        ObservableList<ObservableList<SpreadsheetCell>> rows = IntStream.range(0, grid.getRowCount())
                .mapToObj(row -> IntStream.range(0, grid.getColumnCount())
                        .mapToObj(col -> createCell(row, col, model.getCell(row, col)))
                        .collect(toCollection(FXCollections::observableArrayList)))
                .collect(toCollection(FXCollections::observableArrayList));

        grid.setRows(rows);

        SpreadsheetView root = new SpreadsheetView(grid);
        root.getColumns().forEach(column -> column.setPrefWidth(50));

        stage.setScene(new Scene(root));
        stage.setTitle("Cells");
        stage.setWidth(400);
        stage.setHeight(400);
        stage.show();
    }

    private static SpreadsheetCell createCell(int row, int col, Cell cell) {
        SpreadsheetCellType<CellInfo> cellType = new SpreadsheetCellType<CellInfo>() {

            @Override
            public SpreadsheetCellEditor createEditor(SpreadsheetView view) {
                return new SpreadsheetCellEditor.StringEditor(view) {
                    @Override
                    public void startEdit(Object value) {
                        CellInfo info = (CellInfo) value;
                        // TODO toString() muss wirklich genau die Formel wiederherstellen
                        String str = info.getFormula().toString();
                        super.startEdit(str);
                    }
                };
            }

            @Override
            public String toString(CellInfo info) {
                return info.getText();
            }

            @Override
            public boolean match(Object o) {
                return true;
            }

            @Override
            public CellInfo convertValue(Object o) {
                if (o instanceof CellInfo) {
                    return (CellInfo) o;
                }

                Formula formula;
                try {
                    formula = null; // Parser.parse(o.toString());
                } catch (Exception e) {
                    formula = null; // new Textual(e.getMessage());
                }
                return new CellInfo(formula, "");
            }
        };

        SpreadsheetCellBase cellBase = new SpreadsheetCellBase(row, col, 1, 1, cellType);

        cellBase.setItem(new CellInfo(cell.getFormula(), ""));
        cellBase.itemProperty().addListener((observable, oldValue, newValue) -> {
            CellInfo info = (CellInfo) newValue;
            Formula oldFormula = cell.getFormula();
            Formula newFormula = info.getFormula();
            if (newFormula != oldFormula) {
                cell.setFormula(newFormula);
            }
        });
        cell.textProperty().addListener((observable, oldValue, newValue) -> {
            CellInfo info = (CellInfo) cellBase.getItem();
            cellBase.setItem(new CellInfo(info.getFormula(), newValue));
        });

        return cellBase;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
