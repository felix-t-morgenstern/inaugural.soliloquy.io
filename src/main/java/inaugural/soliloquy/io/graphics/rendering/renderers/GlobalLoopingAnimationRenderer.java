package inaugural.soliloquy.io.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.io.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.io.graphics.renderables.GlobalLoopingAnimationRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShiftStackAggregator;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.io.graphics.rendering.WindowResolutionManager;

import java.util.function.Supplier;

public class GlobalLoopingAnimationRenderer
        extends
        AbstractImageAssetRenderer<AnimationFrameSnippet, GlobalLoopingAnimationRenderable> {
    public GlobalLoopingAnimationRenderer(
            RenderingBoundaries renderingBoundaries,
            Supplier<Float> getScreenWToHRatio,
            ColorShiftStackAggregator shiftAggregator,
            TimestampValidator timestampValidator
    ) {
        super(renderingBoundaries, getScreenWToHRatio, shiftAggregator, timestampValidator);
    }

    @Override
    public void render(GlobalLoopingAnimationRenderable renderable, long timestamp)
            throws IllegalArgumentException {
        checkRenderableAndTimestamp(renderable, timestamp);

        super.render(renderable, (r, t) -> {
                    var animation = r.getGlobalLoopingAnimation();
                    return Check.ifNull(animation,
                                    "animation within GlobalLoopingAnimationRenderableImpl.render")
                            .provide(t);
                }, timestamp,
                false);
    }
}
