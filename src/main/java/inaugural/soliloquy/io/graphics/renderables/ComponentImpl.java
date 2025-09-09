package inaugural.soliloquy.io.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.Renderable;
import soliloquy.specs.io.graphics.renderables.RenderableWithMouseEvents;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.input.keyboard.entities.KeyBindingContext;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.collections.Collections.setOf;

public class ComponentImpl  extends AbstractRenderable implements Component {
    private final KeyBindingContext BINDING_CONTEXT;
    private final Set<Renderable> RENDERABLES;
    private final Consumer<RenderableWithMouseEvents> ADD_TO_CAPTURING;
    private final Consumer<RenderableWithMouseEvents> REMOVE_FROM_CAPTURING;
    private final Map<String, Object> DATA;

    private int tier;
    private ProviderAtTime<FloatBox> renderingBoundariesProvider;

    @SuppressWarnings("ConstantConditions")
    public ComponentImpl(UUID uuid,
                         int z,
                         KeyBindingContext bindingContext,
                         Component containingComponent,
                         ProviderAtTime<FloatBox> renderingBoundariesProvider,
                         Map<String, Object> data,
                         Consumer<RenderableWithMouseEvents> addToCapturing,
                         Consumer<RenderableWithMouseEvents> removeFromCapturing) {
        super(z, uuid);
        this.containingComponent = containingComponent;
        if (containingComponent != null) {
            this.tier = containingComponent.tier() + 1;
            containingComponent.add(this);
        }
        this.renderingBoundariesProvider =
                Check.ifNull(renderingBoundariesProvider, "renderingBoundariesProvider");
        BINDING_CONTEXT = Check.ifNull(bindingContext, "bindingContext");
        RENDERABLES = setOf();
        DATA = mapOf(Check.ifNull(data, "data"));
        ADD_TO_CAPTURING = Check.ifNull(addToCapturing, "addToCapturing");
        REMOVE_FROM_CAPTURING = Check.ifNull(removeFromCapturing, "removeFromCapturing");
    }

    @Override
    public KeyBindingContext keyBindingContext() {
        return BINDING_CONTEXT;
    }

    @Override
    public void add(Renderable renderable) throws IllegalArgumentException {
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
        RENDERABLES.add(renderable);
        if (renderable instanceof RenderableWithMouseEvents) {
            ADD_TO_CAPTURING.accept((RenderableWithMouseEvents) renderable);
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
            REMOVE_FROM_CAPTURING.accept((RenderableWithMouseEvents) renderable);
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
        super.delete();
    }

    @Override
    public Map<String, Object> data() throws IllegalStateException {
        return DATA;
    }
}
