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
import java.util.function.Consumer;

public class SpriteRenderableImpl extends AbstractRenderableWithArea implements SpriteRenderable {
    private Sprite _sprite;
    private ProviderAtTime<Float> _borderThicknessProvider;
    private ProviderAtTime<Color> _borderColorProvider;

    public SpriteRenderableImpl(Sprite sprite, ProviderAtTime<Float> borderThicknessProvider,
                                ProviderAtTime<Color> borderColorProvider,
                                List<ColorShift> colorShifts,
                                ProviderAtTime<FloatBox> renderingDimensionsProvider, int z,
                                EntityUuid uuid, Consumer<Renderable> updateZIndexInContainer,
                                Consumer<Renderable> removeFromContainer) {
        super(colorShifts, renderingDimensionsProvider, z, uuid, updateZIndexInContainer,
                removeFromContainer);
        setSprite(sprite);
        setBorderThicknessProvider(borderThicknessProvider);
        setBorderColorProvider(borderColorProvider);
    }

    /** @noinspection rawtypes*/
    public SpriteRenderableImpl(Sprite sprite, ProviderAtTime<Float> borderThicknessProvider,
                                ProviderAtTime<Color> borderColorProvider, Action clickAction,
                                Action mouseOverAction, Action mouseLeaveAction,
                                List<ColorShift> colorShifts,
                                ProviderAtTime<FloatBox> renderingDimensionsProvider, int z,
                                EntityUuid uuid, Consumer<Renderable> updateZIndexInContainer,
                                Consumer<Renderable> removeFromContainer) {
        super(clickAction, mouseOverAction, mouseLeaveAction, colorShifts,
                renderingDimensionsProvider, z, uuid, updateZIndexInContainer,
                removeFromContainer);
        setSprite(sprite);
        setBorderThicknessProvider(borderThicknessProvider);
        setBorderColorProvider(borderColorProvider);
        throwInConstructorIfFedUnderlyingAssetThatDoesNotSupport();
    }

    @Override
    public Sprite getSprite() {
        return _sprite;
    }

    @Override
    public void setSprite(Sprite sprite) throws IllegalArgumentException {
        _sprite = Check.ifNull(sprite, "sprite");
    }

    @Override
    public ProviderAtTime<Float> getBorderThicknessProvider() {
        return _borderThicknessProvider;
    }

    @Override
    public void setBorderThicknessProvider(ProviderAtTime<Float> borderThicknessProvider)
            throws IllegalArgumentException {
        _borderThicknessProvider = Check.ifNull(borderThicknessProvider,
                "borderThicknessProvider");
    }

    @Override
    public ProviderAtTime<Color> getBorderColorProvider() {
        return _borderColorProvider;
    }

    @Override
    public void setBorderColorProvider(ProviderAtTime<Color> borderColorProvider)
            throws IllegalArgumentException {
        _borderColorProvider = Check.ifNull(borderColorProvider, "borderColorProvider");
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
}
