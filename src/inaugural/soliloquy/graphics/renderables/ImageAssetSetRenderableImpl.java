package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.assets.ImageAssetSet;
import soliloquy.specs.graphics.renderables.ImageAssetSetRenderable;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static inaugural.soliloquy.tools.Tools.nullIfEmpty;

public class ImageAssetSetRenderableImpl extends AbstractRenderableWithArea
        implements ImageAssetSetRenderable {
    private ImageAssetSet _imageAssetSet;
    private String _type;
    private String _direction;

    public ImageAssetSetRenderableImpl(ImageAssetSet imageAssetSet, String type, String direction,
                                       List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                       ProviderAtTime<Float> borderThicknessProvider,
                                       ProviderAtTime<Color> borderColorProvider,
                                       ProviderAtTime<FloatBox> renderingDimensionsProvider, int z,
                                       EntityUuid uuid,
                                       Consumer<Renderable> updateZIndexInContainer,
                                       Consumer<Renderable> removeFromContainer) {
        super(colorShiftProviders, borderThicknessProvider, borderColorProvider,
                renderingDimensionsProvider, z, uuid, updateZIndexInContainer,
                removeFromContainer);
        setImageAssetSet(imageAssetSet);
        setType(type);
        setDirection(direction);
    }

    public ImageAssetSetRenderableImpl(ImageAssetSet imageAssetSet, String type, String direction,
                                       Map<Integer, Action<Long>> onPress,
                                       Map<Integer, Action<Long>> onRelease,
                                       Action<Long> onMouseOver, Action<Long> onMouseLeave,
                                       List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                       ProviderAtTime<Float> borderThicknessProvider,
                                       ProviderAtTime<Color> borderColorProvider,
                                       ProviderAtTime<FloatBox> renderingDimensionsProvider, int z,
                                       EntityUuid uuid,
                                       Consumer<Renderable> updateZIndexInContainer,
                                       Consumer<Renderable> removeFromContainer) {
        super(onPress, onRelease, onMouseOver, onMouseLeave, colorShiftProviders,
                borderThicknessProvider, borderColorProvider, renderingDimensionsProvider, z, uuid,
                updateZIndexInContainer, removeFromContainer);
        setImageAssetSet(imageAssetSet);
        setType(type);
        setDirection(direction);
        throwInConstructorIfFedUnderlyingAssetThatDoesNotSupport();
    }

    @Override
    public ImageAssetSet getImageAssetSet() {
        return _imageAssetSet;
    }

    @Override
    public void setImageAssetSet(ImageAssetSet imageAssetSet) throws IllegalArgumentException {
        Check.ifNull(imageAssetSet, "imageAssetSet");
        if (_capturesMouseEvents && !imageAssetSet.supportsMouseEventCapturing()) {
            throw new IllegalArgumentException("ImageAssetSetRenderableImpl.setImageAssetSet: " +
                    "cannot assign ImageAssetSet which does not support mouse events to an " +
                    "ImageAssetSetRenderable which does support mouse events");
        }
        _imageAssetSet = imageAssetSet;
    }

    @Override
    public String getType() {
        return _type;
    }

    @Override
    public void setType(String type) {
        _type = nullIfEmpty(type);
    }

    @Override
    public String getDirection() {
        return _direction;
    }

    @Override
    public void setDirection(String direction) {
        _direction = nullIfEmpty(direction);
    }

    @Override
    protected boolean underlyingAssetSupportsMouseEvents() {
        return _imageAssetSet.supportsMouseEventCapturing();
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
    public boolean capturesMouseEventAtPoint(float x, float y, long timestamp)
            throws UnsupportedOperationException, IllegalArgumentException {
        return false;
    }
}
