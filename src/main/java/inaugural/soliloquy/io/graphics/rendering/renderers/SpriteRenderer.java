package inaugural.soliloquy.io.graphics.rendering.renderers;

import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.io.graphics.assets.Sprite;
import soliloquy.specs.io.graphics.renderables.SpriteRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShiftStackAggregator;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.io.graphics.rendering.WindowResolutionManager;

import java.util.function.Supplier;

public class SpriteRenderer extends AbstractImageAssetRenderer<Sprite, SpriteRenderable> {
    public SpriteRenderer(RenderingBoundaries renderingBoundaries,
                          Supplier<Float> getScreenWToHRatio,
                          ColorShiftStackAggregator shiftAggregator,
                          TimestampValidator timestampValidator) {
        super(renderingBoundaries, getScreenWToHRatio, shiftAggregator, timestampValidator);
    }

    @Override
    public void render(SpriteRenderable renderable, long timestamp)
            throws IllegalArgumentException {
        super.render(renderable, (r, _) -> r.getSprite(), timestamp);
    }
}
