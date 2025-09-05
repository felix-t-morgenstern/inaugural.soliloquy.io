package inaugural.soliloquy.io.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.ComponentImpl;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.RenderableWithMouseEvents;
import soliloquy.specs.io.graphics.renderables.factories.ComponentFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.util.UUID;
import java.util.function.Consumer;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static soliloquy.specs.io.input.keyboard.entities.KeyBindingContext.bindingContext;

public class ComponentFactoryImpl implements ComponentFactory {
    private final Consumer<RenderableWithMouseEvents> ADD_TO_CAPTURING;
    private final Consumer<RenderableWithMouseEvents> REMOVE_FROM_CAPTURING;

    public ComponentFactoryImpl(Consumer<RenderableWithMouseEvents> addToCapturing,
                                Consumer<RenderableWithMouseEvents> removeFromCapturing) {
        ADD_TO_CAPTURING = Check.ifNull(addToCapturing, "addToCapturing");
        REMOVE_FROM_CAPTURING = Check.ifNull(removeFromCapturing, "removeFromCapturing");
    }

    @Override
    public Component make(
            UUID uuid,
            int z,
            ProviderAtTime<FloatBox> renderingBoundariesProvider,
            Component containingComponent
    ) throws IllegalArgumentException {
        return new ComponentImpl(
                Check.ifNull(uuid, "uuid"),
                z,
                bindingContext(listOf(), false),
                containingComponent,
                renderingBoundariesProvider,
                ADD_TO_CAPTURING,
                REMOVE_FROM_CAPTURING
        );
    }
}
