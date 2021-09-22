package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.assets.Sprite;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.SpriteRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SpriteRenderableImpl extends AbstractRenderableWithArea implements SpriteRenderable {
    private Sprite _sprite;

    public SpriteRenderableImpl(Sprite sprite, ProviderAtTime<Float> borderThicknessProvider,
                                ProviderAtTime<Color> borderColorProvider,
                                List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                ProviderAtTime<FloatBox> renderingDimensionsProvider, int z,
                                EntityUuid uuid, Consumer<Renderable> updateZIndexInContainer,
                                Consumer<Renderable> removeFromContainer) {
        super(colorShiftProviders, borderThicknessProvider, borderColorProvider,
                renderingDimensionsProvider, z, uuid, updateZIndexInContainer,
                removeFromContainer);
        setSprite(sprite);
    }

    public SpriteRenderableImpl(Sprite sprite, ProviderAtTime<Float> borderThicknessProvider,
                                ProviderAtTime<Color> borderColorProvider,
                                Map<Integer, Action<Long>> onPress,
                                Map<Integer, Action<Long>> onRelease,
                                Action<Long> onMouseOver, Action<Long> onMouseLeave,
                                List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                ProviderAtTime<FloatBox> renderingDimensionsProvider, int z,
                                EntityUuid uuid, Consumer<Renderable> updateZIndexInContainer,
                                Consumer<Renderable> removeFromContainer) {
        super(onPress, onRelease, onMouseOver, onMouseLeave, colorShiftProviders,
                borderThicknessProvider, borderColorProvider, renderingDimensionsProvider, z, uuid,
                updateZIndexInContainer, removeFromContainer);
        setSprite(sprite);
        throwInConstructorIfFedUnderlyingAssetThatDoesNotSupport();
    }

    @Override
    public Sprite getSprite() {
        return _sprite;
    }

    @Override
    public void setSprite(Sprite sprite) throws IllegalArgumentException {
        Check.ifNull(sprite, "sprite");
        if (_capturesMouseEvents && !sprite.image().supportsMouseEventCapturing()) {
            throw new IllegalArgumentException("SpriteRenderableImpl.setSprite: cannot assign " +
                    "Sprite whose Image does not support mouse events to a SpriteRenderable " +
                    "which does support mouse events");
        }
        _sprite = sprite;
    }

    @Override
    public String getInterfaceName() {
        return SpriteRenderable.class.getCanonicalName();
    }

    @Override
    protected boolean underlyingAssetSupportsMouseEvents() {
        return _sprite.image().supportsMouseEventCapturing();
    }

    @Override
    protected String className() {
        return "SpriteRenderableImpl";
    }

    @Override
    public boolean capturesMouseEventAtPoint(float x, float y, long timestamp)
            throws UnsupportedOperationException, IllegalArgumentException {
        return capturesMouseEventAtPoint(x, y, timestamp, () -> _sprite);
    }
}
