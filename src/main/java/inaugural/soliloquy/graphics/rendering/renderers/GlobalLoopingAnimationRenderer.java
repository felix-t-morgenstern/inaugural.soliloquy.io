package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.renderables.GlobalLoopingAnimationRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShiftStackAggregator;
import soliloquy.specs.graphics.renderables.colorshifting.NetColorShifts;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;

import static inaugural.soliloquy.graphics.api.Constants.INTACT_COLOR;
public class GlobalLoopingAnimationRenderer
        extends CanRenderSnippets<GlobalLoopingAnimationRenderable> {
    private final ColorShiftStackAggregator COLOR_SHIFT_STACK_AGGREGATOR;

    public GlobalLoopingAnimationRenderer(RenderingBoundaries renderingBoundaries,
                                          ColorShiftStackAggregator colorShiftStackAggregator,
                                          Long mostRecentTimestamp) {
        super(renderingBoundaries, mostRecentTimestamp);
        COLOR_SHIFT_STACK_AGGREGATOR = Check.ifNull(colorShiftStackAggregator,
                "colorShiftStackAggregator");
    }

    @Override
    public void render(GlobalLoopingAnimationRenderable globalLoopingAnimationRenderable,
                       long timestamp)
            throws IllegalArgumentException {
        Check.ifNull(globalLoopingAnimationRenderable, "globalLoopingAnimationRenderable");

        var renderingArea =
                Check.ifNull(globalLoopingAnimationRenderable.getRenderingDimensionsProvider(),
                                "globalLoopingAnimationRenderable.getRenderingDimensionsProvider()")
                        .provide(timestamp);

        validateRenderableWithDimensionsMembers(renderingArea,
                globalLoopingAnimationRenderable.colorShiftProviders(),
                globalLoopingAnimationRenderable.uuid(), "globalLoopingAnimationRenderable");

        Check.ifNull(globalLoopingAnimationRenderable.getGlobalLoopingAnimation(),
                "globalLoopingAnimationRenderable.getGlobalLoopingAnimation()");

        TIMESTAMP_VALIDATOR.validateTimestamp(this.getClass().getCanonicalName(), timestamp);

        NetColorShifts netColorShifts = netColorShifts(
                globalLoopingAnimationRenderable.colorShiftProviders(),
                COLOR_SHIFT_STACK_AGGREGATOR,
                timestamp);

        super.render(
                renderingArea,
                globalLoopingAnimationRenderable.getGlobalLoopingAnimation().provide(timestamp),
                INTACT_COLOR,
                netColorShifts
        );
    }
}
