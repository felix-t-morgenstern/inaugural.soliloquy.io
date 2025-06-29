package inaugural.soliloquy.io.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.assets.Sprite;
import soliloquy.specs.io.graphics.renderables.SpriteRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderableStack;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SpriteRenderableImpl extends AbstractImageAssetRenderable implements SpriteRenderable {
    private Sprite sprite;

    public SpriteRenderableImpl(Sprite sprite, ProviderAtTime<Float> borderThicknessProvider,
                                ProviderAtTime<Color> borderColorProvider,
                                List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                ProviderAtTime<FloatBox> renderingDimensionsProvider, int z,
                                UUID uuid, RenderableStack containingStack,
                                RenderingBoundaries renderingBoundaries) {
        super(colorShiftProviders, borderThicknessProvider, borderColorProvider,
                renderingDimensionsProvider, z, uuid, containingStack, renderingBoundaries);
        setSprite(sprite);
    }

    public SpriteRenderableImpl(Sprite sprite, ProviderAtTime<Float> borderThicknessProvider,
                                ProviderAtTime<Color> borderColorProvider,
                                Map<Integer, Action<MouseEventInputs>> onPress,
                                Map<Integer, Action<MouseEventInputs>> onRelease,
                                Action<MouseEventInputs> onMouseOver,
                                Action<MouseEventInputs> onMouseLeave,
                                List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                ProviderAtTime<FloatBox> renderingDimensionsProvider, int z,
                                UUID uuid, RenderableStack containingStack,
                                RenderingBoundaries renderingBoundaries) {
        super(onPress, onRelease, onMouseOver, onMouseLeave, colorShiftProviders,
                borderThicknessProvider, borderColorProvider, renderingDimensionsProvider, z, uuid,
                containingStack, renderingBoundaries);
        setSprite(sprite);
        throwInConstructorIfFedUnderlyingAssetThatDoesNotSupport();
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public void setSprite(Sprite sprite) throws IllegalArgumentException {
        Check.ifNull(sprite, "sprite");
        if (capturesMouseEvents && !sprite.image().supportsMouseEventCapturing()) {
            throw new IllegalArgumentException("SpriteRenderableImpl.setSprite: cannot assign " +
                    "Sprite whose Image does not support mouse events to a SpriteRenderable " +
                    "which does support mouse events");
        }
        this.sprite = sprite;
    }

    @Override
    protected boolean underlyingAssetSupportsMouseEvents() {
        return sprite.image().supportsMouseEventCapturing();
    }

    @Override
    protected String className() {
        return "SpriteRenderableImpl";
    }

    @Override
    public boolean capturesMouseEventAtPoint(Vertex point, long timestamp)
            throws UnsupportedOperationException, IllegalArgumentException {
        return capturesMouseEventAtPoint(point, timestamp, this::getSprite);
    }
}
