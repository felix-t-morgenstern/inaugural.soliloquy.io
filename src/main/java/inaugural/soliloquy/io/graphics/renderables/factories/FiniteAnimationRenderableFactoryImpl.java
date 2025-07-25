package inaugural.soliloquy.io.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.FiniteAnimationRenderableImpl;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.assets.Animation;
import soliloquy.specs.io.graphics.renderables.FiniteAnimationRenderable;
import soliloquy.specs.io.graphics.renderables.Renderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.factories.FiniteAnimationRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.ui.EventInputs;
import soliloquy.specs.ui.Component;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

public class FiniteAnimationRenderableFactoryImpl extends AbstractRenderableFactory
        implements FiniteAnimationRenderableFactory {
    private final RenderingBoundaries RENDERING_BOUNDARIES;

    public FiniteAnimationRenderableFactoryImpl(RenderingBoundaries renderingBoundaries,
                                                BiConsumer<Component, Renderable> removeFromComponent) {
        super(removeFromComponent);
        RENDERING_BOUNDARIES = Check.ifNull(renderingBoundaries, "renderingBoundaries");
    }

    @Override
    public FiniteAnimationRenderable make(Animation animation,
                                          ProviderAtTime<Float> borderThicknessProvider,
                                          ProviderAtTime<Color> borderColorProvider,
                                          List<ColorShift> colorShifts,
                                          ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                          UUID uuid,
                                          Component component,
                                          long startTimestamp, Long pausedTimestamp,
                                          Long mostRecentTimestamp)
            throws IllegalArgumentException {
        return new FiniteAnimationRenderableImpl(animation, borderThicknessProvider,
                borderColorProvider, colorShifts, renderingAreaProvider, z, uuid, component,
                REMOVE_FROM_COMPONENT, RENDERING_BOUNDARIES, startTimestamp, pausedTimestamp,
                mostRecentTimestamp);
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
                                          long startTimestamp, Long pausedTimestamp,
                                          Long mostRecentTimestamp)
            throws IllegalArgumentException {
        return new FiniteAnimationRenderableImpl(animation, borderThicknessProvider,
                borderColorProvider, onPress, onRelease, onMouseOver, onMouseLeave, colorShifts,
                renderingAreaProvider, z, uuid, component, REMOVE_FROM_COMPONENT,
                RENDERING_BOUNDARIES, startTimestamp, pausedTimestamp, mostRecentTimestamp);
    }
}
