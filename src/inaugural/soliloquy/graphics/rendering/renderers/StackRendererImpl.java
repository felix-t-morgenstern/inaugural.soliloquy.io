package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.infrastructure.List;
import soliloquy.specs.common.infrastructure.Map;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.renderers.Renderer;
import soliloquy.specs.graphics.rendering.renderers.StackRenderer;

import java.util.ArrayList;
import java.util.Collections;

public class StackRendererImpl implements StackRenderer {
    private final RenderableStack RENDERABLE_STACK;
    private final Renderer<Renderable> RENDERER;
    private final TimestampValidator TIMESTAMP_VALIDATOR;

    public StackRendererImpl(RenderableStack renderableStack, Renderer<Renderable> renderer,
                             Long mostRecentTimestamp) {
        RENDERABLE_STACK = Check.ifNull(renderableStack, "renderableStack");
        RENDERER = Check.ifNull(renderer, "renderer");
        TIMESTAMP_VALIDATOR = new TimestampValidator(mostRecentTimestamp);
    }

    @Override
    public void render(long timestamp) {
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);

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
