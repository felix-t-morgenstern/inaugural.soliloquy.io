package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.graphics.rendering.renderers.Renderer;
import soliloquy.specs.graphics.rendering.renderers.StackRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StackRendererImpl implements StackRenderer {
    private final Renderer<Renderable> RENDERER;
    private final RenderingBoundaries RENDERING_BOUNDARIES;
    private final TimestampValidator TIMESTAMP_VALIDATOR;

    public StackRendererImpl(Renderer<Renderable> renderer, RenderingBoundaries renderingBoundaries,
                             Long mostRecentTimestamp) {
        RENDERER = Check.ifNull(renderer, "renderer");
        RENDERING_BOUNDARIES = Check.ifNull(renderingBoundaries, "renderingBoundaries");
        TIMESTAMP_VALIDATOR = new TimestampValidator(mostRecentTimestamp);
    }

    @Override
    public void render(RenderableStack stack, long timestamp) {
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);

        FloatBox boundaries = stack.getRenderingBoundariesProvider().provide(timestamp);
        RENDERING_BOUNDARIES.pushNewBoundaries(boundaries);

        Map<Integer, List<Renderable>> toRender = stack.renderablesByZIndexRepresentation();

        List<Integer> keys = new ArrayList<>(toRender.keySet());

        keys.sort(Collections.reverseOrder());

        keys.forEach(z -> toRender.get(z).forEach(renderable ->
                RENDERER.render(renderable, timestamp)));

        RENDERING_BOUNDARIES.popMostRecentBoundaries();
    }

    @Override
    public String getInterfaceName() {
        return StackRenderer.class.getCanonicalName();
    }
}
