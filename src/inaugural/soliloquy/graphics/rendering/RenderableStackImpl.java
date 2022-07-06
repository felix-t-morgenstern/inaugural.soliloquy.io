package inaugural.soliloquy.graphics.rendering;

import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.util.*;

public class RenderableStackImpl implements RenderableStack {
    private final HashMap<Integer, ArrayList<Renderable>> STACK;
    private final HashMap<Renderable, Integer> Z_INDICES_OF_INTEGERS;

    public RenderableStackImpl() {
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
    public Map<Integer, List<Renderable>> representation() {
        return new HashMap<>(STACK);
    }

    @Override
    public String getInterfaceName() {
        return RenderableStack.class.getCanonicalName();
    }
}
