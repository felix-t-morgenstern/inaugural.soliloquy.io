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
import java.util.function.Function;

public class ComponentFactoryImpl implements ComponentFactory {
    private final Consumer<Component> REGISTER_COMPONENT;
    private final Consumer<Component> DEREGISTER_COMPONENT;
    private final BiConsumer<Component, Integer> ADD_TO_KEY_CAPTURING;
    private final Consumer<Component> REMOVE_FROM_KEY_CAPTURING;
    private final Consumer<RenderableWithMouseEvents> ADD_TO_MOUSE_CAPTURING;
    private final Consumer<RenderableWithMouseEvents> REMOVE_FROM_MOUSE_CAPTURING;
    @SuppressWarnings("rawtypes")
    private final Function<String, soliloquy.specs.common.entities.BiConsumer> GET_BICONSUMER;

    public ComponentFactoryImpl(Consumer<Component> registerComponent,
                                Consumer<Component> deregisterComponent,
                                BiConsumer<Component, Integer> addToKeyCapturing,
                                Consumer<Component> removeFromKeyCapturing,
                                Consumer<RenderableWithMouseEvents> addToCapturing,
                                Consumer<RenderableWithMouseEvents> removeFromCapturing,
                                @SuppressWarnings("rawtypes")
                                Function<String, soliloquy.specs.common.entities.BiConsumer> getBiConsumer) {
        REGISTER_COMPONENT = Check.ifNull(registerComponent, "registerComponent");
        DEREGISTER_COMPONENT = Check.ifNull(deregisterComponent, "deregisterComponent");
        ADD_TO_KEY_CAPTURING = Check.ifNull(addToKeyCapturing, "addToKeyCapturing");
        REMOVE_FROM_KEY_CAPTURING = Check.ifNull(removeFromKeyCapturing, "removeFromKeyCapturing");
        ADD_TO_MOUSE_CAPTURING = Check.ifNull(addToCapturing, "addToCapturing");
        REMOVE_FROM_MOUSE_CAPTURING = Check.ifNull(removeFromCapturing, "removeFromCapturing");
        GET_BICONSUMER = Check.ifNull(getBiConsumer, "getBiConsumer");
    }

    @Override
    public Component make(
            UUID uuid,
            int z,
            Set<KeyBinding> keyBindings,
            boolean blocksLowerKeyBindings,
            int keyBindingPriority,
            ProviderAtTime<FloatBox> dimensionsProvider,
            ProviderAtTime<FloatBox> renderingBoundariesProvider,
            String prerenderHookId,
            String addActionHookId,
            Component containingComponent,
            Map<String, Object> data
    ) throws IllegalArgumentException {
        @SuppressWarnings("unchecked") soliloquy.specs.common.entities.BiConsumer<Component, Component.Addend>
                addHook = GET_BICONSUMER.apply(addActionHookId);
        @SuppressWarnings("unchecked") soliloquy.specs.common.entities.BiConsumer<Component, Long>
                prerenderHook = GET_BICONSUMER.apply(prerenderHookId);
        var component = new ComponentImpl(
                Check.ifNull(uuid, "uuid"),
                z,
                keyBindings,
                blocksLowerKeyBindings,
                containingComponent,
                dimensionsProvider,
                renderingBoundariesProvider,
                data,
                REGISTER_COMPONENT,
                DEREGISTER_COMPONENT,
                REMOVE_FROM_KEY_CAPTURING,
                ADD_TO_MOUSE_CAPTURING,
                REMOVE_FROM_MOUSE_CAPTURING,
                prerenderHook,
                addHook
        );
        ADD_TO_KEY_CAPTURING.accept(component, keyBindingPriority);
        return component;
    }
}
