package de.fluxparticle.sevenguis.gui7cells;

import de.fluxparticle.sevenguis.gui7cells.formula.*;
import de.fluxparticle.syntax.config.EnumSyntaxConfig;
import de.fluxparticle.syntax.parser.Lexer;
import de.fluxparticle.syntax.parser.Parser;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.controlsfx.control.spreadsheet.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.stream.IntStream;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toCollection;

public class CellsFX extends Application {

    private static final EnumSyntaxConfig<FormulaSyntax> CONFIG = new EnumSyntaxConfig<>(FormulaSyntax.class);

    private static final Parser PARSER = CONFIG.getParser(FormulaSyntax.FORMULA);

    private static final SpreadsheetCellType<CellInfo> CELL_TYPE = new SpreadsheetCellType<CellInfo>() {

        @Override
        public SpreadsheetCellEditor createEditor(SpreadsheetView view) {
            return new SpreadsheetCellEditor.StringEditor(view) {
                @Override
                public void startEdit(Object value) {
                    CellInfo info = (CellInfo) value;
                    String str = info.getContent().toString();
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

            String input = ofNullable((String) o).orElse("");
            Content content;
            try {
                Reader reader = new StringReader(input);
                Lexer lexer = CONFIG.newLexer(reader);
                content = (Content) PARSER.check(lexer);
            } catch (Exception e) {
                content = new Text(input);
            }
            return new CellInfo(content, input);
        }
    };

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
        SpreadsheetCellBase cellBase = new SpreadsheetCellBase(row, col, 1, 1, CELL_TYPE);

        cellBase.setItem(new CellInfo(cell.getContent(), ""));
        cellBase.itemProperty().addListener((observable, oldValue, newValue) -> {
            CellInfo info = (CellInfo) newValue;
            Content oldContent = cell.getContent();
            Content newContent = info.getContent();
            if (newContent != oldContent) {
                cell.setText(info.getText());
                cell.setContent(newContent);
            }
        });
        cell.textProperty().addListener((observable, oldValue, newValue) -> {
            CellInfo info = (CellInfo) cellBase.getItem();
            cellBase.setItem(new CellInfo(info.getContent(), newValue));
        });

        return cellBase;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
