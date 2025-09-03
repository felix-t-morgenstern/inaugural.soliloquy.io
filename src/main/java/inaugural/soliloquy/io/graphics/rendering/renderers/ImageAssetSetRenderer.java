package inaugural.soliloquy.io.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.io.graphics.assets.Animation;
import soliloquy.specs.io.graphics.assets.AssetSnippet;
import soliloquy.specs.io.graphics.assets.GlobalLoopingAnimation;
import soliloquy.specs.io.graphics.assets.Sprite;
import soliloquy.specs.io.graphics.renderables.ImageAssetSetRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShiftStackAggregator;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;

import java.util.function.Supplier;

public class ImageAssetSetRenderer extends AbstractImageAssetRenderer<AssetSnippet, ImageAssetSetRenderable> {
    public ImageAssetSetRenderer(
            RenderingBoundaries renderingBoundaries,
            Supplier<Float> getScreenWToHRatio,
            ColorShiftStackAggregator shiftAggregator,
            TimestampValidator timestampValidator) {
        super(renderingBoundaries, getScreenWToHRatio, shiftAggregator, timestampValidator);
    }

    @Override
    public void render(ImageAssetSetRenderable renderable, long timestamp)
            throws IllegalArgumentException {
        super.render(renderable, (r, t) -> {
            var imageAssetSet = r.getImageAssetSet();
            Check.ifNull(imageAssetSet, "imageAssetSet produced by imageAssetSetRenderable");
            var imageAsset = imageAssetSet.getImageAssetWithDisplayParams(r.displayParams());
            Check.ifNull(imageAsset, "imageAsset retrieved from imageAssetSet");
            return switch (imageAsset) {
                case Sprite s -> s;
                case Animation a -> a.snippetAtFrame((int) (t - r.getAnimationStart()));
                case GlobalLoopingAnimation g -> g.provide(t);
                default -> null;
            };
        }, timestamp);
    }
}
