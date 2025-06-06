package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.shared.Direction;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.GlobalLoopingAnimation;
import soliloquy.specs.graphics.assets.ImageAssetSet;
import soliloquy.specs.graphics.assets.Sprite;
import soliloquy.specs.graphics.renderables.ImageAssetSetRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.Tools.nullIfEmpty;

public class ImageAssetSetRenderableImpl extends AbstractImageAssetRenderable
        implements ImageAssetSetRenderable {
    private ImageAssetSet imageAssetSet;
    private String type;
    private Direction direction;

    public ImageAssetSetRenderableImpl(ImageAssetSet imageAssetSet,
                                       String type,
                                       Direction direction,
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
        setType(type);
        setDirection(direction);
    }

    public ImageAssetSetRenderableImpl(ImageAssetSet imageAssetSet,
                                       String type,
                                       Direction direction,
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
        setType(type);
        setDirection(direction);
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
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = nullIfEmpty(type);
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
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
            var imageAsset = imageAssetSet.getImageAssetForTypeAndDirection(type, direction);
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
