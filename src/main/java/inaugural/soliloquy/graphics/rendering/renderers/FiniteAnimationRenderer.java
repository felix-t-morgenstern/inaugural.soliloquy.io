package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.renderables.FiniteAnimationRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShiftStackAggregator;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import static inaugural.soliloquy.graphics.api.Constants.INTACT_COLOR;

public class FiniteAnimationRenderer
        extends CanRenderSnippets<FiniteAnimationRenderable>
        implements Renderer<FiniteAnimationRenderable> {
    private final ColorShiftStackAggregator COLOR_SHIFT_STACK_AGGREGATOR;

    public FiniteAnimationRenderer(RenderingBoundaries renderingBoundaries,
                                   ColorShiftStackAggregator colorShiftStackAggregator,
                                   Long mostRecentTimestamp) {
        super(renderingBoundaries, mostRecentTimestamp);
        COLOR_SHIFT_STACK_AGGREGATOR = Check.ifNull(colorShiftStackAggregator,
                "colorShiftStackAggregator");
    }

    @Override
    public void render(FiniteAnimationRenderable finiteAnimationRenderable, long timestamp)
            throws IllegalArgumentException {
        Check.ifNull(finiteAnimationRenderable, "finiteAnimationRenderable");

        var renderingArea = Check.ifNull(finiteAnimationRenderable.getRenderingDimensionsProvider(),
                "finiteAnimationRenderable.getRenderingDimensionsProvider()").provide(timestamp);

        validateRenderableWithDimensionsMembers(renderingArea,
                finiteAnimationRenderable.colorShiftProviders(),
                finiteAnimationRenderable.uuid(), "finiteAnimationRenderable");

        TIMESTAMP_VALIDATOR.validateTimestamp(this.getClass().getCanonicalName(), timestamp);

        if (timestamp < finiteAnimationRenderable.startTimestamp()) {
            return;
        }

        if (timestamp >= finiteAnimationRenderable.endTimestamp()) {
            finiteAnimationRenderable.delete();
            return;
        }

        var netColorShifts = netColorShifts(
                finiteAnimationRenderable.colorShiftProviders(),
                COLOR_SHIFT_STACK_AGGREGATOR,
                timestamp);

        var snippet = finiteAnimationRenderable.provide(timestamp);

        super.render(
                renderingArea,
                snippet,
                INTACT_COLOR,
                netColorShifts
        );
    }
}
