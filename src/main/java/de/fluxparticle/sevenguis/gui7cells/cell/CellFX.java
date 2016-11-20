package de.fluxparticle.sevenguis.gui7cells.cell;

import de.fluxparticle.sevenguis.gui7cells.formula.Content;
import de.fluxparticle.sevenguis.gui7cells.formula.Model;
import de.fluxparticle.sevenguis.gui7cells.formula.Text;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import static org.fxmisc.easybind.EasyBind.subscribe;

/**
 * Created by sreinck on 19.11.16.
 */
public class CellFX extends Cell {

    private final ContentObservable binder;

    private final ObjectProperty<Object> value;

    private Content content;

    public CellFX(Model model) {
        binder = new ContentObservable(model);

        value = new SimpleObjectProperty<>();
        setContent(Text.EMPTY);

        subscribe(value, this::updateValue);
    }

    @Override
    public Content getContent() {
        return content;
    }

    @Override
    public void setContent(Content content) {
        this.content = content;
        value.bind(content.accept(binder, null));
    }

    public ObjectProperty<Object> valueProperty() {
        return value;
    }

}
