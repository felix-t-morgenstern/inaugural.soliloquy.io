package inaugural.soliloquy.io.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.GlobalLoopingAnimationRenderableImpl;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.assets.GlobalLoopingAnimation;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.GlobalLoopingAnimationRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.factories.GlobalLoopingAnimationRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.ui.EventInputs;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GlobalLoopingAnimationRenderableFactoryImpl implements GlobalLoopingAnimationRenderableFactory {
    private final RenderingBoundaries RENDERING_BOUNDARIES;
    private final TimestampValidator TIMESTAMP_VALIDATOR;

    public GlobalLoopingAnimationRenderableFactoryImpl(RenderingBoundaries renderingBoundaries,
                                                       TimestampValidator timestampValidator) {
        RENDERING_BOUNDARIES = Check.ifNull(renderingBoundaries, "renderingBoundaries");
        TIMESTAMP_VALIDATOR = Check.ifNull(timestampValidator, "timestampValidator");
    }

    @Override
    public GlobalLoopingAnimationRenderable make(GlobalLoopingAnimation globalLoopingAnimation,
                                                 ProviderAtTime<Float> borderThicknessProvider,
                                                 ProviderAtTime<Color> borderColorProvider,
                                                 List<ColorShift> colorShifts,
                                                 ProviderAtTime<FloatBox> renderingAreaProvider,
                                                 int z, UUID uuid,
                                                 Component component)
            throws IllegalArgumentException {
        return new GlobalLoopingAnimationRenderableImpl(globalLoopingAnimation,
                borderThicknessProvider, borderColorProvider, colorShifts,
                renderingAreaProvider, z, uuid, component,
                RENDERING_BOUNDARIES, TIMESTAMP_VALIDATOR);
    }

    @Override
    public GlobalLoopingAnimationRenderable make(GlobalLoopingAnimation globalLoopingAnimation,
                                                 ProviderAtTime<Float> borderThicknessProvider,
                                                 ProviderAtTime<Color> borderColorProvider,
                                                 Map<Integer, Action<EventInputs>> onPress,
                                                 Map<Integer, Action<EventInputs>> onRelease,
                                                 Action<EventInputs> onMouseOver,
                                                 Action<EventInputs> onMouseLeave,
                                                 List<ColorShift> colorShifts,
                                                 ProviderAtTime<FloatBox> renderingAreaProvider,
                                                 int z, UUID uuid,
                                                 Component component)
            throws IllegalArgumentException {
        return new GlobalLoopingAnimationRenderableImpl(globalLoopingAnimation,
                borderThicknessProvider, borderColorProvider, onPress, onRelease, onMouseOver,
                onMouseLeave, colorShifts, renderingAreaProvider, z, uuid,
                component, RENDERING_BOUNDARIES, TIMESTAMP_VALIDATOR);
    }
}
