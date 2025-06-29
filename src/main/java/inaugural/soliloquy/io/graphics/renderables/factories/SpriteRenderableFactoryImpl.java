package inaugural.soliloquy.io.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.SpriteRenderableImpl;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.assets.Sprite;
import soliloquy.specs.io.graphics.renderables.RenderableWithMouseEvents.MouseEventInputs;
import soliloquy.specs.io.graphics.renderables.SpriteRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.factories.SpriteRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderableStack;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SpriteRenderableFactoryImpl implements SpriteRenderableFactory {
    private final RenderingBoundaries RENDERING_BOUNDARIES;

    public SpriteRenderableFactoryImpl(RenderingBoundaries renderingBoundaries) {
        RENDERING_BOUNDARIES = Check.ifNull(renderingBoundaries, "renderingBoundaries");
    }

    @Override
    public SpriteRenderable make(Sprite sprite, ProviderAtTime<Float> borderThicknessProvider,
                                 ProviderAtTime<Color> borderColorProvider,
                                 List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                 ProviderAtTime<FloatBox> renderingDimensionsProvider, int z,
                                 UUID uuid, RenderableStack containingStack)
            throws IllegalArgumentException {
        return new SpriteRenderableImpl(sprite, borderThicknessProvider, borderColorProvider,
                colorShiftProviders, renderingDimensionsProvider, z, uuid, containingStack,
                RENDERING_BOUNDARIES);
    }

    @Override
    public SpriteRenderable make(Sprite sprite, ProviderAtTime<Float> borderThicknessProvider,
                                 ProviderAtTime<Color> borderColorProvider,
                                 Map<Integer, Action<MouseEventInputs>> onPress,
                                 Map<Integer, Action<MouseEventInputs>> onRelease,
                                 Action<MouseEventInputs> onMouseOver,
                                 Action<MouseEventInputs> onMouseLeave,
                                 List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                 ProviderAtTime<FloatBox> renderingDimensionsProvider, int z,
                                 UUID uuid, RenderableStack containingStack)
            throws IllegalArgumentException {
        return new SpriteRenderableImpl(sprite, borderThicknessProvider, borderColorProvider,
                onPress, onRelease, onMouseOver, onMouseLeave, colorShiftProviders,
                renderingDimensionsProvider, z, uuid, containingStack, RENDERING_BOUNDARIES);
    }
}
