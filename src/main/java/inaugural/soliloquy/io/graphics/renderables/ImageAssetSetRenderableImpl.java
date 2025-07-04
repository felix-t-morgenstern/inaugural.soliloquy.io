package inaugural.soliloquy.io.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.assets.Animation;
import soliloquy.specs.io.graphics.assets.GlobalLoopingAnimation;
import soliloquy.specs.io.graphics.assets.ImageAssetSet;
import soliloquy.specs.io.graphics.assets.Sprite;
import soliloquy.specs.io.graphics.renderables.ImageAssetSetRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderableStack;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ImageAssetSetRenderableImpl extends AbstractImageAssetRenderable
        implements ImageAssetSetRenderable {
    private final Map<String, String> DISPLAY_PARAMS;

    private ImageAssetSet imageAssetSet;

    public ImageAssetSetRenderableImpl(ImageAssetSet imageAssetSet,
                                       Map<String, String> displayParams,
                                       List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                       ProviderAtTime<Float> borderThicknessProvider,
                                       ProviderAtTime<Color> borderColorProvider,
                                       ProviderAtTime<FloatBox> renderingDimensionsProvider,
                                       int z,
                                       UUID uuid,
                                       RenderableStack containingStack,
                                       RenderingBoundaries renderingBoundaries) {
        super(colorShiftProviders, borderThicknessProvider, borderColorProvider,
                renderingDimensionsProvider, z, uuid, containingStack, renderingBoundaries);
        setImageAssetSet(imageAssetSet);
        DISPLAY_PARAMS = displayParams;
    }

    public ImageAssetSetRenderableImpl(ImageAssetSet imageAssetSet,
                                       Map<String, String> displayParams,
                                       Map<Integer, Action<MouseEventInputs>> onPress,
                                       Map<Integer, Action<MouseEventInputs>> onRelease,
                                       Action<MouseEventInputs> onMouseOver,
                                       Action<MouseEventInputs> onMouseLeave,
                                       List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                       ProviderAtTime<Float> borderThicknessProvider,
                                       ProviderAtTime<Color> borderColorProvider,
                                       ProviderAtTime<FloatBox> renderingDimensionsProvider,
                                       int z,
                                       UUID uuid,
                                       RenderableStack containingStack,
                                       RenderingBoundaries renderingBoundaries) {
        super(onPress, onRelease, onMouseOver, onMouseLeave, colorShiftProviders,
                borderThicknessProvider, borderColorProvider, renderingDimensionsProvider, z, uuid,
                containingStack, renderingBoundaries);
        setImageAssetSet(imageAssetSet);
        DISPLAY_PARAMS = displayParams;
        throwInConstructorIfFedUnderlyingAssetThatDoesNotSupport();
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
