package inaugural.soliloquy.graphics.rendering;

import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderableStackImpl implements RenderableStack {
    private final HashMap<Integer, ArrayList<Renderable>> STACK;
    private final HashMap<Renderable, Integer> Z_INDICES_OF_INTEGERS;
    private final static RenderableArchetype RENDERABLE_ARCHETYPE = new RenderableArchetype();

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

    private static class RenderableArchetype implements Renderable {
        @Override
        public int getZ() {
            return 0;
        }

        @Override
        public void setZ(int i) {

        }

        @Override
        public void delete() {

        }

        @Override
        public EntityUuid uuid() {
            return null;
        }

        @Override
        public String getInterfaceName() {
            return Renderable.class.getCanonicalName();
        }
    }
}
