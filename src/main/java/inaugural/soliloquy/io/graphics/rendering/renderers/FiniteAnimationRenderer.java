package inaugural.soliloquy.io.graphics.rendering.renderers;

import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.io.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.io.graphics.renderables.FiniteAnimationRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShiftStackAggregator;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.io.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.util.function.Supplier;

public class FiniteAnimationRenderer
        extends AbstractImageAssetRenderer<AnimationFrameSnippet, FiniteAnimationRenderable>
        implements Renderer<FiniteAnimationRenderable> {
    public FiniteAnimationRenderer(RenderingBoundaries renderingBoundaries,
                                   Supplier<Float> getScreenWToHRatio,
                                   ColorShiftStackAggregator shiftAggregator,
                                   TimestampValidator timestampValidator) {
        super(renderingBoundaries, getScreenWToHRatio, shiftAggregator, timestampValidator);
    }

    @Override
    public void render(FiniteAnimationRenderable renderable, long timestamp)
            throws IllegalArgumentException {
        checkRenderableAndTimestamp(renderable, timestamp);

        if (timestamp < renderable.startTimestamp()) {
            return;
        }

        super.render(renderable, ProviderAtTime::provide, timestamp, false);

        if (timestamp >= renderable.endTimestamp()) {
            renderable.delete();
        }
    }
}
