package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.ImageAsset;
import soliloquy.specs.graphics.assets.ImageAssetSet;
import soliloquy.specs.graphics.assets.Sprite;
import soliloquy.specs.graphics.renderables.ImageAssetSetRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;
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
    private String direction;

    public ImageAssetSetRenderableImpl(ImageAssetSet imageAssetSet, String type, String direction,
                                       List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                       ProviderAtTime<Float> borderThicknessProvider,
                                       ProviderAtTime<Color> borderColorProvider,
                                       ProviderAtTime<FloatBox> renderingDimensionsProvider, int z,
                                       UUID uuid, RenderableStack containingStack,
                                       RenderingBoundaries renderingBoundaries) {
        super(colorShiftProviders, borderThicknessProvider, borderColorProvider,
                renderingDimensionsProvider, z, uuid, containingStack, renderingBoundaries);
        setImageAssetSet(imageAssetSet);
        setType(type);
        setDirection(direction);
    }

    public ImageAssetSetRenderableImpl(ImageAssetSet imageAssetSet, String type, String direction,
                                       Map<Integer,
                                               Action<MouseEventInputs>> onPress,
                                       Map<Integer,
                                               Action<MouseEventInputs>> onRelease,
                                       Action<MouseEventInputs> onMouseOver,
                                       Action<MouseEventInputs> onMouseLeave,
                                       List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                       ProviderAtTime<Float> borderThicknessProvider,
                                       ProviderAtTime<Color> borderColorProvider,
                                       ProviderAtTime<FloatBox> renderingDimensionsProvider, int z,
                                       UUID uuid, RenderableStack containingStack,
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
    public String getDirection() {
        return direction;
    }

    @Override
    public void setDirection(String direction) {
        this.direction = nullIfEmpty(direction);
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
    public String getInterfaceName() {
        return ImageAssetSetRenderable.class.getCanonicalName();
    }

    @Override
    public boolean capturesMouseEventAtPoint(Vertex point, long timestamp)
            throws UnsupportedOperationException, IllegalArgumentException {
        return capturesMouseEventAtPoint(point, timestamp, () -> {
            ImageAsset imageAsset =
                    imageAssetSet.getImageAssetForTypeAndDirection(type, direction);
            if (imageAsset instanceof Sprite) {
                return (Sprite) imageAsset;
            }
            // TODO: Refactor ImageAssetSet to support GlobalLoopingAnimation
            else if (imageAsset instanceof Animation) {
                Animation animation = (Animation) imageAsset;
                return animation.snippetAtFrame((int) (timestamp % animation.msDuration()));
            }
            else {
                // throw exception
                return null;
            }
        });
    }
}
