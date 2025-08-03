package inaugural.soliloquy.io.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.SpriteRenderableImpl;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.assets.Sprite;
import soliloquy.specs.io.graphics.renderables.Renderable;
import soliloquy.specs.io.graphics.renderables.SpriteRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.factories.SpriteRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.ui.EventInputs;
import soliloquy.specs.ui.Component;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

public class SpriteRenderableFactoryImpl implements SpriteRenderableFactory {
    private final RenderingBoundaries RENDERING_BOUNDARIES;
    private final TimestampValidator TIMESTAMP_VALIDATOR;

    public SpriteRenderableFactoryImpl(RenderingBoundaries renderingBoundaries,
                                       TimestampValidator timestampValidator) {
        RENDERING_BOUNDARIES = Check.ifNull(renderingBoundaries, "renderingBoundaries");
        TIMESTAMP_VALIDATOR = Check.ifNull(timestampValidator, "timestampValidator");
    }

    @Override
    public SpriteRenderable make(Sprite sprite, ProviderAtTime<Float> borderThicknessProvider,
                                 ProviderAtTime<Color> borderColorProvider,
                                 List<ColorShift> colorShifts,
                                 ProviderAtTime<FloatBox> renderingDimensionsProvider, int z,
                                 UUID uuid,
                                 Component component)
            throws IllegalArgumentException {
        return new SpriteRenderableImpl(sprite, borderThicknessProvider, borderColorProvider,
                colorShifts, renderingDimensionsProvider, z, uuid, component, RENDERING_BOUNDARIES,
                TIMESTAMP_VALIDATOR);
    }

    @Override
    public SpriteRenderable make(Sprite sprite, ProviderAtTime<Float> borderThicknessProvider,
                                 ProviderAtTime<Color> borderColorProvider,
                                 Map<Integer, Action<EventInputs>> onPress,
                                 Map<Integer, Action<EventInputs>> onRelease,
                                 Action<EventInputs> onMouseOver,
                                 Action<EventInputs> onMouseLeave,
                                 List<ColorShift> colorShifts,
                                 ProviderAtTime<FloatBox> renderingDimensionsProvider, int z,
                                 UUID uuid,
                                 Component component)
            throws IllegalArgumentException {
        return new SpriteRenderableImpl(sprite, borderThicknessProvider, borderColorProvider,
                onPress, onRelease, onMouseOver, onMouseLeave, colorShifts,
                renderingDimensionsProvider, z, uuid, component, RENDERING_BOUNDARIES,
                TIMESTAMP_VALIDATOR);
    }
}
