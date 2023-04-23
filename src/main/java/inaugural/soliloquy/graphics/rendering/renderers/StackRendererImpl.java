package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.graphics.rendering.renderers.Renderers;
import soliloquy.specs.graphics.rendering.renderers.StackRenderer;

import java.util.Collections;

import static inaugural.soliloquy.tools.collections.Collections.listOf;

public class StackRendererImpl implements StackRenderer {
    private final Renderers RENDERERS;
    private final RenderingBoundaries RENDERING_BOUNDARIES;
    private final TimestampValidator TIMESTAMP_VALIDATOR;

    public StackRendererImpl(Renderers renderers, RenderingBoundaries renderingBoundaries,
                             Long mostRecentTimestamp) {
        RENDERERS = Check.ifNull(renderers, "renderers");
        RENDERING_BOUNDARIES = Check.ifNull(renderingBoundaries, "renderingBoundaries");
        TIMESTAMP_VALIDATOR = new TimestampValidator(mostRecentTimestamp);
    }

    @Override
    public void render(RenderableStack stack, long timestamp) {
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);

        var boundaries = stack.getRenderingBoundariesProvider().provide(timestamp);
        RENDERING_BOUNDARIES.pushNewBoundaries(boundaries);

        var toRender = stack.renderablesByZIndexRepresentation();

        var keys = listOf(toRender.keySet().stream().toList());

        keys.sort(Collections.reverseOrder());

        keys.forEach(z -> toRender.get(z).forEach(renderable ->
                RENDERERS.render(renderable, timestamp)));

        RENDERING_BOUNDARIES.popMostRecentBoundaries();
    }

    @Override
    public String getInterfaceName() {
        return StackRenderer.class.getCanonicalName();
    }
}
