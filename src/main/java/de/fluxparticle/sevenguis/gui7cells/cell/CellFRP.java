package de.fluxparticle.sevenguis.gui7cells.cell;

import de.fluxparticle.fenja.Value;
import de.fluxparticle.fenja.ValueSink;
import de.fluxparticle.sevenguis.gui7cells.formula.Content;
import de.fluxparticle.sevenguis.gui7cells.formula.Model;
import de.fluxparticle.sevenguis.gui7cells.formula.Text;

import static de.fluxparticle.fenja.Value.switchV;

/**
 * Created by sreinck on 19.11.16.
 */
public class CellFRP extends Cell {

    private final ContentFRP binder;

    private final ValueSink<Content> content;

    private final Value<Object> value;

    public CellFRP(Model model) {
        binder = new ContentFRP(model);

        content = new ValueSink<>(Text.EMPTY);
        value = switchV(content.map(c -> c.accept(binder, null)));

        value.listen(this::updateValue);
    }

    @Override
    public Content getContent() {
        return content.getValue();
    }

    @Override
    public void setContent(Content content) {
        this.content.setValue(content);
    }

    public Value<Object> valueProperty() {
        return value;
    }

}
