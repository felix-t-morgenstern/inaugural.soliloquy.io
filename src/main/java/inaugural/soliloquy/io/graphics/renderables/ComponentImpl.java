package inaugural.soliloquy.io.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.BiConsumer;
import soliloquy.specs.common.shared.HasId;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.Renderable;
import soliloquy.specs.io.graphics.renderables.RenderableWithMouseEvents;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.input.keyboard.KeyBinding;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import static inaugural.soliloquy.tools.Tools.defaultIfNullElseTransform;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.collections.Collections.setOf;
import static soliloquy.specs.io.graphics.renderables.Component.Addend.addend;

public class ComponentImpl extends AbstractRenderable implements Component {
    private final Set<KeyBinding> BINDINGS;
    private final boolean BLOCKS_LOWER_BINDINGS;
    private final Set<Renderable> RENDERABLES;
    private final Map<String, Object> DATA;
    private final BiConsumer<Component, Long> PRERENDER;
    private final soliloquy.specs.common.entities.BiConsumer<Component, Addend>
            ADD_HOOK;

    private final Consumer<Component> DEREGISTER_COMPONENT;
    private final Consumer<Component> REMOVE_FROM_KEY_CAPTURING;
    private final Consumer<RenderableWithMouseEvents> ADD_TO_MOUSE_CAPTURING;
    private final Consumer<RenderableWithMouseEvents> REMOVE_FROM_MOUSE_CAPTURING;

    private int tier;
    private ProviderAtTime<FloatBox> dimensionsProvider;
    private ProviderAtTime<FloatBox> renderingBoundariesProvider;

    public final static java.util.function.BiConsumer<Component, Long> COMPONENT_PRERENDER_HOOK =
            (c, t) -> ((ComponentImpl) c).prerenderHook(t);

    @SuppressWarnings("ConstantConditions")
    public ComponentImpl(UUID uuid,
                         int z,
                         Set<KeyBinding> keyBindings,
                         boolean blocksLowerKeyBindings,
                         Component containingComponent,
                         ProviderAtTime<FloatBox> dimensionsProvider,
                         ProviderAtTime<FloatBox> renderingBoundariesProvider,
                         Map<String, Object> data,
                         Consumer<Component> registerComponent,
                         Consumer<Component> deregisterComponent,
                         Consumer<Component> removeFromKeyCapturing,
                         Consumer<RenderableWithMouseEvents> addToMouseCapturing,
                         Consumer<RenderableWithMouseEvents> removeFromMouseCapturing,
                         BiConsumer<Component, Long> prerender,
                         soliloquy.specs.common.entities.BiConsumer<Component, Addend> addHook) {
        super(z, uuid);
        BINDINGS = Check.ifNull(keyBindings, "keyBindings");
        BLOCKS_LOWER_BINDINGS = blocksLowerKeyBindings;
        this.dimensionsProvider = Check.ifNull(dimensionsProvider, "dimensionsProvider");
        this.containingComponent = containingComponent;
        if (containingComponent != null) {
            this.tier = containingComponent.tier() + 1;
        }
        this.renderingBoundariesProvider =
                Check.ifNull(renderingBoundariesProvider, "renderingBoundariesProvider");
        RENDERABLES = setOf();
        PRERENDER = prerender;
        ADD_HOOK = addHook;
        DATA = mapOf(Check.ifNull(data, "data"));
        DEREGISTER_COMPONENT = Check.ifNull(deregisterComponent, "deregisterComponent");
        REMOVE_FROM_KEY_CAPTURING = Check.ifNull(removeFromKeyCapturing, "removeFromKeyCapturing");
        ADD_TO_MOUSE_CAPTURING = Check.ifNull(addToMouseCapturing, "addToMouseCapturing");
        REMOVE_FROM_MOUSE_CAPTURING =
                Check.ifNull(removeFromMouseCapturing, "removeFromMouseCapturing");

        Check.ifNull(registerComponent, "registerComponent").accept(this);
    }

    @Override
    public Set<KeyBinding> keyBindings() {
        return BINDINGS;
    }

    @Override
    public boolean blocksLowerKeyBindings() {
        return BLOCKS_LOWER_BINDINGS;
    }

    @Override
    public void add(Renderable renderable) throws IllegalArgumentException {
        add(renderable, null);
    }

    @Override
    public void add(Renderable renderable, Map<String, Object> data)
            throws IllegalArgumentException {
        Check.ifNull(renderable, "renderable");
        if (renderable.containingComponent() != this) {
            throw new IllegalArgumentException(
                    "ComponentImpl.add: renderable must have this stored as its Component");
        }
        if (renderable instanceof Component) {
            var newComponentTier = ((Component) renderable).tier();
            if (newComponentTier != tier + 1) {
                throw new IllegalArgumentException(
                        "ComponentImpl.add: renderable is Component whose tier (" +
                                newComponentTier +
                                ") is not one greater than this Component's tier (" + tier + ")");
            }
        }

        if (ADD_HOOK != null) {
            ADD_HOOK.accept(this, addend(renderable, data));
        }

        RENDERABLES.add(renderable);
        if (renderable instanceof RenderableWithMouseEvents) {
            ADD_TO_MOUSE_CAPTURING.accept((RenderableWithMouseEvents) renderable);
        }
    }

    @Override
    public void remove(Renderable renderable) throws IllegalArgumentException {
        Check.ifNull(renderable, "renderable");
        if (!renderable.isDeleted() && renderable.containingComponent() != this) {
            throw new IllegalArgumentException(
                    "ComponentImpl.remove: renderable not in this Component");
        }
        RENDERABLES.remove(renderable);
        if (renderable instanceof RenderableWithMouseEvents) {
            REMOVE_FROM_MOUSE_CAPTURING.accept((RenderableWithMouseEvents) renderable);
        }
    }

    @Override
    public void clear() {
        RENDERABLES.clear();
    }

    @Override
    public Set<Renderable> contentsRepresentation() {
        return setOf(RENDERABLES);
    }

    @Override
    public ProviderAtTime<FloatBox> getDimensionsProvider() {
        return dimensionsProvider;
    }

    @Override
    public void setDimensionsProvider(ProviderAtTime<FloatBox> dimensionsProvider)
            throws IllegalArgumentException {
        this.dimensionsProvider = Check.ifNull(dimensionsProvider, "dimensionsProvider");
    }

    @Override
    public ProviderAtTime<FloatBox> getRenderingBoundariesProvider() {
        return renderingBoundariesProvider;
    }

    @Override
    public void setRenderingBoundariesProvider(ProviderAtTime<FloatBox> provider)
            throws IllegalArgumentException, UnsupportedOperationException {
        if (containingComponent == null) {
            throw new UnsupportedOperationException(
                    "RenderableStackImpl.setRenderingBoundariesProvider: cannot assign new " +
                            "rendering boundaries for top-level Component");
        }
        renderingBoundariesProvider = Check.ifNull(provider, "provider");
    }

    public void prerenderHook(long timestamp) {
        if (PRERENDER != null) {
            PRERENDER.accept(this, timestamp);
        }
    }

    @Override
    public String prerenderHookId() {
        return defaultIfNullElseTransform(PRERENDER, HasId::id, null);
    }

    @Override
    public String addHookId() {
        return defaultIfNullElseTransform(ADD_HOOK, soliloquy.specs.common.entities.BiConsumer::id,
                null);
    }

    @Override
    public int tier() {
        return tier;
    }

    @Override
    public void setZ(int z) {
        if (containingComponent == null) {
            throw new UnsupportedOperationException(
                    "RenderableStackImpl.setZ: cannot set z value on top-level Component");
        }
        super.setZ(z);
    }

    @Override
    public Component containingComponent() {
        return containingComponent;
    }

    public void setContainingComponent(Component containingComponent) {
        this.containingComponent = containingComponent;
        if (containingComponent == null) {
            this.tier = 0;
        }
        else {
            this.tier = containingComponent.tier() + 1;
        }
    }

    @Override
    public void delete() {
        RENDERABLES.forEach(Renderable::delete);
        DEREGISTER_COMPONENT.accept(this);
        REMOVE_FROM_KEY_CAPTURING.accept(this);
        super.delete();
    }

    @Override
    public Map<String, Object> data() throws IllegalStateException {
        return DATA;
    }
}
