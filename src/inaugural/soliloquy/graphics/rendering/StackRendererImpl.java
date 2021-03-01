package inaugural.soliloquy.graphics.rendering;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.infrastructure.List;
import soliloquy.specs.common.infrastructure.Map;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.Renderer;
import soliloquy.specs.graphics.rendering.StackRenderer;

import java.util.ArrayList;
import java.util.Collections;

public class StackRendererImpl implements StackRenderer {
    private final RenderableStack RENDERABLE_STACK;
    @SuppressWarnings("rawtypes")
    private final Renderer<Renderable> RENDERER;

    @SuppressWarnings("rawtypes")
    public StackRendererImpl(RenderableStack renderableStack, Renderer<Renderable> renderer) {
        RENDERABLE_STACK = Check.ifNull(renderableStack, "renderableStack");
        RENDERER = Check.ifNull(renderer, "renderer");
    }

    // TODO: Refactor how keys are obtained and sorted after having refactored Collection to either extend List, or implement a method which exposes a properly-typed List or Array
    @SuppressWarnings("rawtypes")
    @Override
    public void render() {
        Map<Integer, List<Renderable>> toRender = RENDERABLE_STACK.snapshot();

        java.util.List<Integer> keys = new ArrayList<>(toRender.keySet());

        keys.sort(Collections.reverseOrder());

        keys.forEach(z -> toRender.get(z).forEach(RENDERER::render));
    }

    @Override
    public String getInterfaceName() {
        return StackRenderer.class.getCanonicalName();
    }
}
