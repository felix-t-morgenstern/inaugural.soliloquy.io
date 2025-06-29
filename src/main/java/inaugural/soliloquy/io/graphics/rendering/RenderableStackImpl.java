package inaugural.soliloquy.io.graphics.rendering;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.Renderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderableStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.io.api.Constants.WHOLE_SCREEN_PROVIDER;
import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;

public class RenderableStackImpl implements RenderableStack {
    private final UUID UUID;
    private final RenderableStack CONTAINING_STACK;
    private final Map<Integer, List<Renderable>> STACK;
    private final Map<Renderable, Integer> Z_INDICES_OF_INTEGERS;

    private int z;
    private ProviderAtTime<FloatBox> renderingBoundariesProvider;

    public RenderableStackImpl() {
        UUID = null;
        z = 0;
        renderingBoundariesProvider = WHOLE_SCREEN_PROVIDER;
        CONTAINING_STACK = null;
        STACK = mapOf();
        Z_INDICES_OF_INTEGERS = mapOf();
    }

    @SuppressWarnings("ConstantConditions")
    public RenderableStackImpl(UUID uuid, int z,
                               ProviderAtTime<FloatBox> renderingBoundariesProvider,
                               RenderableStack containingStack) {
        UUID = Check.ifNull(uuid, "uuid");
        this.z = z;
        this.renderingBoundariesProvider =
                Check.ifNull(renderingBoundariesProvider, "renderingBoundariesProvider");
        CONTAINING_STACK = Check.ifNull(containingStack, "containingStack");
        CONTAINING_STACK.add(this);
        STACK = mapOf();
        Z_INDICES_OF_INTEGERS = mapOf();
    }

    @Override
    public void clearContainedRenderables() {
        STACK.clear();
    }

    @Override
    public void add(Renderable renderable) throws IllegalArgumentException {
        if (renderable instanceof RenderableStack renderableStack) {
            if (renderableStack.containingStack() != this) {
                throw new IllegalArgumentException(
                        "RenderableStackImpl.add: cannot add a RenderableStack which does not " +
                                "have this class as its containingStack");
            }
        }
        if (Z_INDICES_OF_INTEGERS.containsKey(renderable)) {
            var previousZIndex = Z_INDICES_OF_INTEGERS.get(renderable);
            if (previousZIndex == renderable.getZ()) {
                return;
            }
            STACK.get(previousZIndex).remove(renderable);
            if (STACK.get(previousZIndex).isEmpty()) {
                STACK.remove(previousZIndex);
            }
        }
        Z_INDICES_OF_INTEGERS.put(renderable, renderable.getZ());
        if (!STACK.containsKey(renderable.getZ())) {
            STACK.put(renderable.getZ(), listOf(renderable));
        }
        else {
            STACK.get(renderable.getZ()).add(renderable);
        }
    }

    @Override
    public void remove(Renderable renderable) throws IllegalArgumentException {
        var z = renderable.getZ();
        STACK.get(z).remove(renderable);
        if (STACK.get(z).isEmpty()) {
            STACK.remove(z);
        }
    }

    @Override
    public ProviderAtTime<FloatBox> getRenderingBoundariesProvider() {
        return renderingBoundariesProvider;
    }

    @Override
    public void setRenderingBoundariesProvider(ProviderAtTime<FloatBox> providerAtTime)
            throws IllegalArgumentException, UnsupportedOperationException {
        if (CONTAINING_STACK == null) {
            throw new UnsupportedOperationException(
                    "RenderableStackImpl.setRenderingBoundariesProvider: cannot assign new " +
                            "rendering boundaries for top-level stack");
        }
        renderingBoundariesProvider = Check.ifNull(providerAtTime, "providerAtTime");
    }

    @Override
    public Map<Integer, List<Renderable>> renderablesByZIndexRepresentation() {
        return mapOf(STACK);
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public void setZ(int z) {
        if (CONTAINING_STACK == null) {
            throw new UnsupportedOperationException(
                    "RenderableStackImpl.setZ: cannot set z value on top-level stack");
        }
        this.z = z;
        CONTAINING_STACK.add(this);
    }

    @Override
    public RenderableStack containingStack() {
        return CONTAINING_STACK;
    }

    @Override
    public void delete() {
        STACK.values().forEach(renderables -> renderables.forEach(Renderable::delete));
    }

    @Override
    public UUID uuid() {
        return UUID;
    }
}
