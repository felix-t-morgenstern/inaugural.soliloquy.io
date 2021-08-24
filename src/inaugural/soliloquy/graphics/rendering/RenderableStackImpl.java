package inaugural.soliloquy.graphics.rendering;

import inaugural.soliloquy.graphics.archetypes.RenderableArchetype;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.factories.ListFactory;
import soliloquy.specs.common.factories.MapFactory;
import soliloquy.specs.common.infrastructure.List;
import soliloquy.specs.common.infrastructure.Map;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.util.HashMap;

public class RenderableStackImpl implements RenderableStack {
    private final HashMap<Integer, List<Renderable>> STACK;
    private final HashMap<Renderable, Integer> Z_INDICES_OF_INTEGERS;
    private final MapFactory MAP_FACTORY;
    private final ListFactory LIST_FACTORY;
    private final static RenderableArchetype RENDERABLE_ARCHETYPE = new RenderableArchetype();

    public RenderableStackImpl(MapFactory mapFactory, ListFactory listFactory) {
        STACK = new HashMap<>();
        Z_INDICES_OF_INTEGERS = new HashMap<>();
        MAP_FACTORY = Check.ifNull(mapFactory, "mapFactory");
        LIST_FACTORY = Check.ifNull(listFactory, "listFactory");
    }

    @Override
    public void clearContainedRenderables() {
        STACK.clear();
    }

    // TODO: Test whether adding the same Renderable with a different z-index updates that Renderable's z-index in the Stack
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
            List<Renderable> renderables = LIST_FACTORY.make(RENDERABLE_ARCHETYPE);
            renderables.add(renderable);
            STACK.put(renderable.getZ(), renderables);
            return;
        }
        STACK.get(renderable.getZ()).add(renderable);
    }

    @Override
    public Map<Integer, List<Renderable>> snapshot() {
        Map<Integer, List<Renderable>> snapshot =
                MAP_FACTORY.make(0, LIST_FACTORY.make(RENDERABLE_ARCHETYPE));

        STACK.forEach((z, renderables) -> snapshot.put(z, renderables.makeClone()));

        return snapshot;
    }

    @Override
    public String getInterfaceName() {
        return RenderableStack.class.getCanonicalName();
    }
}
