package inaugural.soliloquy.graphics.rendering;

import inaugural.soliloquy.graphics.archetypes.RenderableArchetype;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.factories.CollectionFactory;
import soliloquy.specs.common.factories.MapFactory;
import soliloquy.specs.common.infrastructure.Collection;
import soliloquy.specs.common.infrastructure.Map;
import soliloquy.specs.common.infrastructure.ReadableCollection;
import soliloquy.specs.common.infrastructure.ReadableMap;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.util.HashMap;

public class RenderableStackImpl implements RenderableStack {
    private final java.util.Map<Integer, Collection<Renderable>> STACK;
    private final MapFactory MAP_FACTORY;
    private final CollectionFactory COLLECTION_FACTORY;
    private final RenderableArchetype RENDERABLE_ARCHETYPE;

    public RenderableStackImpl(MapFactory mapFactory, CollectionFactory collectionFactory) {
        STACK = new HashMap<>();
        MAP_FACTORY = Check.ifNull(mapFactory, "mapFactory");
        COLLECTION_FACTORY = Check.ifNull(collectionFactory, "collectionFactory");
        RENDERABLE_ARCHETYPE = new RenderableArchetype();
    }

    @Override
    public void clear() {
        STACK.clear();
    }

    @Override
    public void add(Renderable renderable) throws IllegalArgumentException {
        if (!STACK.containsKey(renderable.z())) {
            Collection<Renderable> renderables = COLLECTION_FACTORY.make(RENDERABLE_ARCHETYPE);
            renderables.add(renderable);
            STACK.put(renderable.z(), renderables);
            return;
        }
        STACK.get(renderable.z()).add(renderable);
    }

    @Override
    public ReadableMap<Integer, ReadableCollection<Renderable>> snapshot() {
        Map<Integer, ReadableCollection<Renderable>> snapshot =
                MAP_FACTORY.make(0, COLLECTION_FACTORY.make(RENDERABLE_ARCHETYPE));

        STACK.forEach((z, renderables) -> snapshot.put(z, renderables.representation()));

        return snapshot.readOnlyRepresentation();
    }

    @Override
    public String getInterfaceName() {
        return RenderableStack.class.getCanonicalName();
    }
}
