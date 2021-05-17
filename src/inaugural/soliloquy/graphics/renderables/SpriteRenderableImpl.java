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
    private final Sprite SPRITE;
    private final ProviderAtTime<Float> BORDER_THICKNESS_PROVIDER;
    private final ProviderAtTime<Color> BORDER_COLOR_PROVIDER;

    public SpriteRenderableImpl(Sprite sprite, ProviderAtTime<Float> borderThicknessProvider,
                                ProviderAtTime<Color> borderColorProvider,
                                List<ColorShift> colorShifts,
                                ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                EntityUuid uuid, Consumer<Renderable> deleteConsumer) {
        super(colorShifts, renderingAreaProvider, z, uuid, deleteConsumer);
        SPRITE = Check.ifNull(sprite, "sprite");
        BORDER_THICKNESS_PROVIDER = Check.ifNull(borderThicknessProvider,
                "borderThicknessProvider");
        BORDER_COLOR_PROVIDER = Check.ifNull(borderColorProvider, "borderColorProvider");
    }

    /** @noinspection rawtypes*/
    public SpriteRenderableImpl(Sprite sprite, ProviderAtTime<Float> borderThicknessProvider,
                                ProviderAtTime<Color> borderColorProvider, Action clickAction,
                                Action mouseOverAction, Action mouseLeaveAction,
                                List<ColorShift> colorShifts,
                                ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                EntityUuid uuid, Consumer<Renderable> deleteConsumer) {
        super(clickAction, mouseOverAction, mouseLeaveAction, colorShifts,  renderingAreaProvider,
                z, uuid, deleteConsumer);
        SPRITE = Check.ifNull(sprite, "sprite");
        BORDER_THICKNESS_PROVIDER = Check.ifNull(borderThicknessProvider,
                "borderThicknessProvider");
        BORDER_COLOR_PROVIDER = Check.ifNull(borderColorProvider, "borderColorProvider");
    }

    @Override
    public Sprite sprite() {
        return SPRITE;
    }

    @Override
    public ProviderAtTime<Float> borderThicknessProvider() {
        return BORDER_THICKNESS_PROVIDER;
    }

    @Override
    public ProviderAtTime<Color> borderColorProvider() {
        return BORDER_COLOR_PROVIDER;
    }

    @Override
    public String getInterfaceName() {
        return SpriteRenderable.class.getCanonicalName();
    }

    @Override
    protected String className() {
        return "SpriteRenderableImpl";
    }
}
