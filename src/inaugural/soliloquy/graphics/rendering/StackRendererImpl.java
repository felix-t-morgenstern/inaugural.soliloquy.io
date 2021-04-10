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
    private final Renderer<Renderable> RENDERER;

    private Long _mostRecentTimestamp;

    public StackRendererImpl(RenderableStack renderableStack, Renderer<Renderable> renderer) {
        RENDERABLE_STACK = Check.ifNull(renderableStack, "renderableStack");
        RENDERER = Check.ifNull(renderer, "renderer");
    }

    // TODO: Refactor how keys are obtained and sorted after having refactored Collection to either extend List, or implement a method which exposes a properly-typed List or Array
    @Override
    public void render(long timestamp) {
        if (_mostRecentTimestamp != null) {
            if (timestamp < _mostRecentTimestamp) {
                throw new IllegalArgumentException(
                        "RasterizedLineSegmentRenderer.render: outdated timestamp provided");
            }
        }
        _mostRecentTimestamp = timestamp;

        Map<Integer, List<Renderable>> toRender = RENDERABLE_STACK.snapshot();

        java.util.List<Integer> keys = new ArrayList<>(toRender.keySet());

        keys.sort(Collections.reverseOrder());

        keys.forEach(z -> toRender.get(z).forEach(renderable ->
                RENDERER.render(renderable, timestamp)));
    }

    @Override
    public String getInterfaceName() {
        return StackRenderer.class.getCanonicalName();
    }
}
