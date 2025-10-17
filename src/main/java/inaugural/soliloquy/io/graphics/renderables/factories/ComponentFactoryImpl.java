package inaugural.soliloquy.io.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.ComponentImpl;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.RenderableWithMouseEvents;
import soliloquy.specs.io.graphics.renderables.factories.ComponentFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.input.keyboard.KeyBinding;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static inaugural.soliloquy.tools.collections.Collections.listOf;

public class ComponentFactoryImpl implements ComponentFactory {
    private final BiConsumer<Component, Integer> ADD_TO_KEY_CAPTURING;
    private final Consumer<Component> REMOVE_FROM_KEY_CAPTURING;
    private final Consumer<RenderableWithMouseEvents> ADD_TO_MOUSE_CAPTURING;
    private final Consumer<RenderableWithMouseEvents> REMOVE_FROM_MOUSE_CAPTURING;

    public ComponentFactoryImpl(BiConsumer<Component, Integer> addToKeyCapturing,
                                Consumer<Component> removeFromKeyCapturing,
                                Consumer<RenderableWithMouseEvents> addToCapturing,
                                Consumer<RenderableWithMouseEvents> removeFromCapturing) {
        ADD_TO_KEY_CAPTURING = Check.ifNull(addToKeyCapturing, "addToKeyCapturing");
        REMOVE_FROM_KEY_CAPTURING = Check.ifNull(removeFromKeyCapturing, "removeFromKeyCapturing");
        ADD_TO_MOUSE_CAPTURING = Check.ifNull(addToCapturing, "addToCapturing");
        REMOVE_FROM_MOUSE_CAPTURING = Check.ifNull(removeFromCapturing, "removeFromCapturing");
    }

    @Override
    public Component make(
            UUID uuid,
            int z,
            Set<KeyBinding> keyBindings,
            boolean blocksLowerKeyBindings,
            int keyBindingPriority,
            ProviderAtTime<FloatBox> renderingBoundariesProvider,
            Component containingComponent,
            Map<String, Object> data
    ) throws IllegalArgumentException {
        var component = new ComponentImpl(
                Check.ifNull(uuid, "uuid"),
                z,
                keyBindings,
                blocksLowerKeyBindings,
                containingComponent,
                renderingBoundariesProvider,
                data,
                REMOVE_FROM_KEY_CAPTURING,
                ADD_TO_MOUSE_CAPTURING,
                REMOVE_FROM_MOUSE_CAPTURING
        );
        ADD_TO_KEY_CAPTURING.accept(component, keyBindingPriority);
        return component;
    }
}
