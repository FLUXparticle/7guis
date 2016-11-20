package de.fluxparticle.sevenguis.gui7cells.cell

import de.fluxparticle.fenja.ValueSink
import de.fluxparticle.sevenguis.gui7cells.formula.Content
import de.fluxparticle.sevenguis.gui7cells.formula.Text

import static de.fluxparticle.fenja.Value.switchV

/**
 * Created by sreinck on 20.11.16.
 */
class CellFenja extends Cell {

    def binder;

    def content;

    def value;

    CellFenja(model) {
        binder = new ContentFenja(model: model)

        content = new ValueSink(Text.EMPTY)
        value = switchV( content.map { it.accept(binder, null) } )

        value.listen(this.&updateValue)
    }

    @Override
    Content getContent() {
        content.value
    }

    @Override
    void setContent(Content content) {
        this.content.send(content)
    }

}
