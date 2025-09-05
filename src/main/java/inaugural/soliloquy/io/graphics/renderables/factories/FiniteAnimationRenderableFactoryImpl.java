package inaugural.soliloquy.io.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.FiniteAnimationRenderableImpl;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.assets.Animation;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.FiniteAnimationRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.factories.FiniteAnimationRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.ui.EventInputs;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FiniteAnimationRenderableFactoryImpl implements FiniteAnimationRenderableFactory {
    private final RenderingBoundaries RENDERING_BOUNDARIES;
    private final TimestampValidator TIMESTAMP_VALIDATOR;

    public FiniteAnimationRenderableFactoryImpl(RenderingBoundaries renderingBoundaries,
                                                TimestampValidator timestampValidator) {
        RENDERING_BOUNDARIES = Check.ifNull(renderingBoundaries, "renderingBoundaries");
        TIMESTAMP_VALIDATOR = Check.ifNull(timestampValidator, "timestampValidator");
    }

    @Override
    public FiniteAnimationRenderable make(Animation animation,
                                          ProviderAtTime<Float> borderThicknessProvider,
                                          ProviderAtTime<Color> borderColorProvider,
                                          List<ColorShift> colorShifts,
                                          ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                          UUID uuid,
                                          Component component,
                                          long startTimestamp,
                                          Long pausedTimestamp)
            throws IllegalArgumentException {
        return new FiniteAnimationRenderableImpl(animation, borderThicknessProvider,
                borderColorProvider, colorShifts, renderingAreaProvider, z, uuid, component,
                RENDERING_BOUNDARIES, startTimestamp, pausedTimestamp, TIMESTAMP_VALIDATOR);
    }

    @Override
    public FiniteAnimationRenderable make(Animation animation,
                                          ProviderAtTime<Float> borderThicknessProvider,
                                          ProviderAtTime<Color> borderColorProvider,
                                          Map<Integer, Action<EventInputs>> onPress,
                                          Map<Integer, Action<EventInputs>> onRelease,
                                          Action<EventInputs> onMouseOver,
                                          Action<EventInputs> onMouseLeave,
                                          List<ColorShift> colorShifts,
                                          ProviderAtTime<FloatBox> renderingAreaProvider,
                                          int z, UUID uuid,
                                          Component component,
                                          long startTimestamp,
                                          Long pausedTimestamp)
            throws IllegalArgumentException {
        return new FiniteAnimationRenderableImpl(animation, borderThicknessProvider,
                borderColorProvider, onPress, onRelease, onMouseOver, onMouseLeave, colorShifts,
                renderingAreaProvider, z, uuid, component, RENDERING_BOUNDARIES, startTimestamp,
                pausedTimestamp, TIMESTAMP_VALIDATOR);
    }
}
