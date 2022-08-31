package inaugural.soliloquy.graphics.rendering;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.util.*;

import static inaugural.soliloquy.graphics.api.Constants.WHOLE_SCREEN;

public class RenderableStackImpl implements RenderableStack {
    private final UUID UUID;
    private final FloatBox RENDERING_DIMENSIONS;
    private final RenderableStack CONTAINING_STACK;
    private final HashMap<Integer, ArrayList<Renderable>> STACK;
    private final HashMap<Renderable, Integer> Z_INDICES_OF_INTEGERS;

    private int _z;

    public RenderableStackImpl() {
        UUID = null;
        _z = 0;
        RENDERING_DIMENSIONS = WHOLE_SCREEN;
        CONTAINING_STACK = null;
        STACK = new HashMap<>();
        Z_INDICES_OF_INTEGERS = new HashMap<>();
    }

    @SuppressWarnings("ConstantConditions")
    public RenderableStackImpl(UUID uuid, int z, FloatBox renderingDimensions,
                               RenderableStack containingStack) {
        UUID = Check.ifNull(uuid, "uuid");
        _z = z;
        RENDERING_DIMENSIONS = Check.ifNull(renderingDimensions, "renderingDimensions");
        CONTAINING_STACK = Check.ifNull(containingStack, "containingStack");
        STACK = new HashMap<>();
        Z_INDICES_OF_INTEGERS = new HashMap<>();
    }

    @Override
    public void clearContainedRenderables() {
        STACK.clear();
    }

    @Override
    public void add(Renderable renderable) throws IllegalArgumentException {
        if (Z_INDICES_OF_INTEGERS.containsKey(renderable)) {
            int previousZIndex = Z_INDICES_OF_INTEGERS.get(renderable);
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
            STACK.put(renderable.getZ(), new ArrayList<Renderable>() {{
                add(renderable);
            }});
        }
        else {
            STACK.get(renderable.getZ()).add(renderable);
        }
    }

    @Override
    public void remove(Renderable renderable) throws IllegalArgumentException {
        int z = renderable.getZ();
        STACK.get(z).remove(renderable);
        if (STACK.get(z).isEmpty()) {
            STACK.remove(z);
        }
    }

    @Override
    public FloatBox renderingDimensions() {
        return RENDERING_DIMENSIONS;
    }

    @Override
    public Map<Integer, List<Renderable>> renderablesByZIndexRepresentation() {
        return new HashMap<>(STACK);
    }

    @Override
    public String getInterfaceName() {
        return RenderableStack.class.getCanonicalName();
    }

    @Override
    public int getZ() {
        return _z;
    }

    @Override
    public void setZ(int z) {
        if (CONTAINING_STACK == null) {
            throw new UnsupportedOperationException(
                    "RenderableStackImpl.setZ: cannot set z value on top-level stack");
        }
        _z = z;
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
