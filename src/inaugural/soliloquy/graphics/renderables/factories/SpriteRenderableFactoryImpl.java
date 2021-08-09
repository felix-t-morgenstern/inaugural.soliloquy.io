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
import java.util.Map;
import java.util.function.Consumer;

public class SpriteRenderableFactoryImpl implements SpriteRenderableFactory {
    @Override
    public SpriteRenderable make(Sprite sprite, ProviderAtTime<Float> borderThicknessProvider,
                                 ProviderAtTime<Color> borderColorProvider,
                                 List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                 ProviderAtTime<FloatBox> renderingDimensionsProvider, int z,
                                 EntityUuid uuid, Consumer<Renderable> updateZIndexInContainer,
                                 Consumer<Renderable> removeFromContainer)
            throws IllegalArgumentException {
        return new SpriteRenderableImpl(sprite, borderThicknessProvider, borderColorProvider,
                colorShiftProviders, renderingDimensionsProvider, z, uuid, updateZIndexInContainer,
                removeFromContainer);
    }

    @Override
    public SpriteRenderable make(Sprite sprite, ProviderAtTime<Float> borderThicknessProvider,
                                 ProviderAtTime<Color> borderColorProvider,
                                 Map<Integer, Action<Long>> onPress,
                                 Map<Integer, Action<Long>> onRelease,
                                 Action<Long> onMouseOver, Action<Long> onMouseLeave,
                                 List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                 ProviderAtTime<FloatBox> renderingDimensionsProvider, int z,
                                 EntityUuid uuid, Consumer<Renderable> updateZIndexInContainer,
                                 Consumer<Renderable> removeFromContainer)
            throws IllegalArgumentException {
        return new SpriteRenderableImpl(sprite, borderThicknessProvider, borderColorProvider,
                onPress, onRelease, onMouseOver, onMouseLeave, colorShiftProviders,
                renderingDimensionsProvider, z, uuid, updateZIndexInContainer,
                removeFromContainer);
    }

    @Override
    public String getInterfaceName() {
        return SpriteRenderableFactory.class.getCanonicalName();
    }
}
