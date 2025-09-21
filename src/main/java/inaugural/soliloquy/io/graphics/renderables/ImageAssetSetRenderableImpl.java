package inaugural.soliloquy.io.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.assets.Animation;
import soliloquy.specs.io.graphics.assets.GlobalLoopingAnimation;
import soliloquy.specs.io.graphics.assets.ImageAssetSet;
import soliloquy.specs.io.graphics.assets.Sprite;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.ImageAssetSetRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.ui.EventInputs;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ImageAssetSetRenderableImpl extends AbstractImageAssetRenderable
        implements ImageAssetSetRenderable {
    private final Map<String, String> DISPLAY_PARAMS;

    private ImageAssetSet imageAssetSet;
    private Long animationStart;

    public ImageAssetSetRenderableImpl(ImageAssetSet imageAssetSet,
                                       Map<String, String> displayParams,
                                       List<ColorShift> colorShifts,
                                       ProviderAtTime<Float> borderThicknessProvider,
                                       ProviderAtTime<Color> borderColorProvider,
                                       ProviderAtTime<FloatBox> renderingDimensionsProvider,
                                       int z,
                                       UUID uuid,
                                       Component component,
                                       RenderingBoundaries renderingBoundaries,
                                       TimestampValidator timestampValidator) {
        super(colorShifts, borderThicknessProvider, borderColorProvider,
                renderingDimensionsProvider, z, uuid, component, renderingBoundaries,
                timestampValidator);
        setImageAssetSet(imageAssetSet);
        DISPLAY_PARAMS = displayParams;
    }

    public ImageAssetSetRenderableImpl(ImageAssetSet imageAssetSet,
                                       Map<String, String> displayParams,
                                       Map<Integer, Action<EventInputs>> onPress,
                                       Map<Integer, Action<EventInputs>> onRelease,
                                       Action<EventInputs> onMouseOver,
                                       Action<EventInputs> onMouseLeave,
                                       List<ColorShift> colorShifts,
                                       ProviderAtTime<Float> borderThicknessProvider,
                                       ProviderAtTime<Color> borderColorProvider,
                                       ProviderAtTime<FloatBox> renderingDimensionsProvider,
                                       int z,
                                       UUID uuid,
                                       Component component,
                                       RenderingBoundaries renderingBoundaries,
                                       TimestampValidator timestampValidator) {
        super(onPress, onRelease, onMouseOver, onMouseLeave, colorShifts, borderThicknessProvider,
                borderColorProvider, renderingDimensionsProvider, z, uuid, component,
                renderingBoundaries, timestampValidator);
        setImageAssetSet(imageAssetSet);
        DISPLAY_PARAMS = displayParams;
    }

    @Override
    public ImageAssetSet getImageAssetSet() {
        return imageAssetSet;
    }

    @Override
    public void setImageAssetSet(ImageAssetSet imageAssetSet) throws IllegalArgumentException {
        Check.ifNull(imageAssetSet, "imageAssetSet");
        if (capturesMouseEvents && !imageAssetSet.supportsMouseEventCapturing()) {
            throw new IllegalArgumentException("ImageAssetSetRenderableImpl.setImageAssetSet: " +
                    "cannot assign ImageAssetSet which does not support mouse events to an " +
                    "ImageAssetSetRenderable which does support mouse events");
        }
        this.imageAssetSet = imageAssetSet;
    }

    @Override
    public Long getAnimationStart() {
        return animationStart;
    }

    @Override
    public void setAnimationStart(Long animationStart) {
        this.animationStart = animationStart;
    }

    @Override
    public Map<String, String> displayParams() {
        return DISPLAY_PARAMS;
    }

    @Override
    protected boolean underlyingAssetSupportsMouseEvents() {
        return imageAssetSet.supportsMouseEventCapturing();
    }

    @Override
    protected String className() {
        return "ImageAssetSetRenderableImpl";
    }

    @Override
    public boolean capturesMouseEventAtPoint(Vertex point, long timestamp)
            throws UnsupportedOperationException, IllegalArgumentException {
        return capturesMouseEventAtPoint(point, timestamp, () -> {
            var imageAsset = imageAssetSet.getImageAssetWithDisplayParams(DISPLAY_PARAMS);
            return switch (imageAsset) {
                case Sprite sprite -> sprite;
                case GlobalLoopingAnimation globalLoopingAnimation ->
                        globalLoopingAnimation.provide(timestamp);
                case Animation animation ->
                        animation.snippetAtFrame((int) (timestamp % animation.msDuration()));
                case null, default ->
                    // throw exception
                        null;
            };
        });
    }
}
