package inaugural.soliloquy.io.graphics.renderables.factories;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.io.graphics.renderables.Renderable;
import soliloquy.specs.ui.Component;

import java.util.function.BiConsumer;

public class AbstractRenderableFactory {
    protected final BiConsumer<Component, Renderable> REMOVE_FROM_COMPONENT;

    public AbstractRenderableFactory(BiConsumer<Component, Renderable> removeFromComponent) {
        REMOVE_FROM_COMPONENT = Check.ifNull(removeFromComponent, "removeFromComponent");
    }
}
