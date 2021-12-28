package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeRenderableStack implements RenderableStack {
    public Map<Integer, List<Renderable>> RENDERABLES = new HashMap<>();

    @Override
    public void clearContainedRenderables() {
        RENDERABLES.clear();
    }

    @Override
    public void add(Renderable renderable) throws IllegalArgumentException {
        if (!RENDERABLES.containsKey(renderable.getZ())) {
            ArrayList<Renderable> renderablesAtZ = new ArrayList<>();
            renderablesAtZ.add(renderable);
            RENDERABLES.put(renderable.getZ(), renderablesAtZ);
            return;
        }

        RENDERABLES.get(renderable.getZ()).add(renderable);
    }

    @Override
    public Map<Integer, List<Renderable>> representation() {
        return new HashMap<>(RENDERABLES);
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
