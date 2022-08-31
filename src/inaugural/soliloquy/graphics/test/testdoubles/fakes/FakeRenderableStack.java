package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.util.*;

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
    public void remove(Renderable renderable) throws IllegalArgumentException {

    }

    @Override
    public FloatBox renderingDimensions() {
        return null;
    }

    @Override
    public Map<Integer, List<Renderable>> renderablesByZIndexRepresentation() {
        return new HashMap<>(RENDERABLES);
    }

    @Override
    public String getInterfaceName() {
        return null;
    }

    @Override
    public int getZ() {
        return 0;
    }

    @Override
    public void setZ(int i) {

    }

    @Override
    public RenderableStack containingStack() {
        return null;
    }

    @Override
    public void delete() {

    }

    @Override
    public UUID uuid() {
        return null;
    }
}
