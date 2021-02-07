package inaugural.soliloquy.graphics.test.fakes;

import inaugural.soliloquy.common.test.fakes.FakeCollection;
import inaugural.soliloquy.common.test.fakes.FakeMap;
import soliloquy.specs.common.infrastructure.Collection;
import soliloquy.specs.common.infrastructure.Map;
import soliloquy.specs.common.infrastructure.ReadableCollection;
import soliloquy.specs.common.infrastructure.ReadableMap;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.rendering.RenderableStack;

public class FakeRenderableStack implements RenderableStack {
    public Map<Integer, Collection<Renderable>> RENDERABLES = new FakeMap<>();

    @Override
    public void clear() {
        RENDERABLES.clear();
    }

    @Override
    public void add(Renderable renderable) throws IllegalArgumentException {
        if (!RENDERABLES.containsKey(renderable.z())) {
            Collection<Renderable> renderablesAtZ = new FakeCollection<>();
            renderablesAtZ.add(renderable);
            RENDERABLES.put(renderable.z(), renderablesAtZ);
            return;
        }

        RENDERABLES.get(renderable.z()).add(renderable);
    }

    @Override
    public ReadableMap<Integer, ReadableCollection<Renderable>> snapshot() {
        Map<Integer, ReadableCollection<Renderable>> snapshot = new FakeMap<>();

        RENDERABLES.forEach(kv -> snapshot.put(kv.getItem1(), kv.getItem2()));

        return snapshot;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
