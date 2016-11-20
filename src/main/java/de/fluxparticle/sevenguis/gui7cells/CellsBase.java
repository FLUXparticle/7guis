package de.fluxparticle.sevenguis.gui7cells;

import de.fluxparticle.sevenguis.gui7cells.cell.Cell;
import de.fluxparticle.sevenguis.gui7cells.cell.CellFactory;
import de.fluxparticle.sevenguis.gui7cells.formula.Content;
import de.fluxparticle.sevenguis.gui7cells.formula.Model;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.controlsfx.control.spreadsheet.*;

import java.util.stream.IntStream;

import static java.util.stream.Collectors.toCollection;

public abstract class CellsBase extends Application {

    private static final SpreadsheetCellType<CellInfo> CELL_TYPE = new CellInfoSpreadsheetCellType();

    private static final int ROWS = 100;

    private static final int COLS = 26;

    private final Model model;

    public CellsBase(CellFactory cellFactory) {
        model = new Model(ROWS, COLS, cellFactory);
        System.gc();
    }

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
        stage.setTitle(getClass().getSimpleName());
        stage.setWidth(400);
        stage.setHeight(400);
        stage.show();
    }

    private static SpreadsheetCell createCell(int row, int col, Cell cell) {
        SpreadsheetCellBase cellBase = new SpreadsheetCellBase(row, col, 1, 1, CELL_TYPE);

        cellBase.setItem(new CellInfo(cell.getContent(), cell.getText()));
        cellBase.itemProperty().addListener((observable, oldValue, newValue) -> {
            CellInfo oldInfo = (CellInfo) oldValue;
            CellInfo newInfo = (CellInfo) newValue;
            Content oldContent = oldInfo.getContent();
            Content newContent = newInfo.getContent();
            String oldText = oldInfo.getText();
            String newText = newInfo.getText();
            if (newContent != oldContent && oldText != newText) {
                cell.setText(newInfo.getText());
                long start = System.currentTimeMillis();
                cell.setContent(newContent);
                long end = System.currentTimeMillis();
                System.out.println((end - start) + " ms");
            }
        });
        cell.textProperty().addListener((observable, oldValue, newValue) -> {
            CellInfo info = (CellInfo) cellBase.getItem();
            cellBase.setItem(new CellInfo(info.getContent(), newValue));
        });

        return cellBase;
    }

}
