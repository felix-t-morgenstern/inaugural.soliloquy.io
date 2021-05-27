package inaugural.soliloquy.graphics.renderables.factories;

import inaugural.soliloquy.graphics.renderables.SpriteRenderableImpl;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.assets.Sprite;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.SpriteRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.factories.SpriteRenderableFactory;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class SpriteRenderableFactoryImpl implements SpriteRenderableFactory {
    @Override
    public SpriteRenderable make(Sprite sprite, ProviderAtTime<Float> borderThicknessProvider,
                                 ProviderAtTime<Color> borderColorProvider,
                                 List<ColorShift> colorShifts,
                                 ProviderAtTime<FloatBox> renderingDimensionsProvider, int z,
                                 EntityUuid uuid, Consumer<Renderable> updateZIndexInContainer,
                                 Consumer<Renderable> removeFromContainer)
            throws IllegalArgumentException {
        return new SpriteRenderableImpl(sprite, borderThicknessProvider, borderColorProvider,
                colorShifts, renderingDimensionsProvider, z, uuid, updateZIndexInContainer,
                removeFromContainer);
    }

    @Override
    public SpriteRenderable make(Sprite sprite, ProviderAtTime<Float> borderThicknessProvider,
                                 ProviderAtTime<Color> borderColorProvider,
                                 Action onClick, Action onMouseOver, Action onMouseLeave,
                                 List<ColorShift> colorShifts,
                                 ProviderAtTime<FloatBox> renderingDimensionsProvider, int z,
                                 EntityUuid uuid, Consumer<Renderable> updateZIndexInContainer,
                                 Consumer<Renderable> removeFromContainer)
            throws IllegalArgumentException {
        return new SpriteRenderableImpl(sprite, borderThicknessProvider, borderColorProvider,
                onClick, onMouseOver, onMouseLeave, colorShifts, renderingDimensionsProvider, z,
                uuid, updateZIndexInContainer, removeFromContainer);
    }

    @Override
    public String getInterfaceName() {
        return SpriteRenderableFactory.class.getCanonicalName();
    }
}
