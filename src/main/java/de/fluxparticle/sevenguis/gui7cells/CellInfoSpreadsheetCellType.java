package de.fluxparticle.sevenguis.gui7cells;

import de.fluxparticle.sevenguis.gui7cells.formula.Content;
import de.fluxparticle.sevenguis.gui7cells.formula.FormulaSyntax;
import de.fluxparticle.sevenguis.gui7cells.formula.Text;
import de.fluxparticle.syntax.config.EnumSyntaxConfig;
import de.fluxparticle.syntax.parser.Lexer;
import de.fluxparticle.syntax.parser.Parser;
import org.controlsfx.control.spreadsheet.SpreadsheetCellEditor;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

import java.io.Reader;
import java.io.StringReader;

import static java.util.Optional.ofNullable;

public class CellInfoSpreadsheetCellType extends SpreadsheetCellType<CellInfo> {

    private static final EnumSyntaxConfig<FormulaSyntax> CONFIG = new EnumSyntaxConfig<>(FormulaSyntax.class);

    private static final Parser PARSER = CONFIG.getParser(FormulaSyntax.FORMULA);

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

}
