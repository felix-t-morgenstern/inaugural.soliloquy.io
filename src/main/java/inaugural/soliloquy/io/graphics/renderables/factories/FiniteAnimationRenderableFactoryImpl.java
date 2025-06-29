package inaugural.soliloquy.io.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.FiniteAnimationRenderableImpl;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.assets.Animation;
import soliloquy.specs.io.graphics.renderables.FiniteAnimationRenderable;
import soliloquy.specs.io.graphics.renderables.RenderableWithMouseEvents.MouseEventInputs;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.factories.FiniteAnimationRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderableStack;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FiniteAnimationRenderableFactoryImpl implements FiniteAnimationRenderableFactory {
    private final RenderingBoundaries RENDERING_BOUNDARIES;

    public FiniteAnimationRenderableFactoryImpl(RenderingBoundaries renderingBoundaries) {
        RENDERING_BOUNDARIES = Check.ifNull(renderingBoundaries, "renderingBoundaries");
    }

    @Override
    public FiniteAnimationRenderable make(Animation animation,
                                          ProviderAtTime<Float> borderThicknessProvider,
                                          ProviderAtTime<Color> borderColorProvider,
                                          List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                          ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                          UUID uuid,
                                          RenderableStack containingStack,
                                          long startTimestamp, Long pausedTimestamp,
                                          Long mostRecentTimestamp)
            throws IllegalArgumentException {
        return new FiniteAnimationRenderableImpl(animation, borderThicknessProvider,
                borderColorProvider, colorShiftProviders, renderingAreaProvider, z, uuid,
                containingStack, RENDERING_BOUNDARIES, startTimestamp, pausedTimestamp,
                mostRecentTimestamp);
    }

    @Override
    public FiniteAnimationRenderable make(Animation animation,
                                          ProviderAtTime<Float> borderThicknessProvider,
                                          ProviderAtTime<Color> borderColorProvider,
                                          Map<Integer, Action<MouseEventInputs>> onPress,
                                          Map<Integer, Action<MouseEventInputs>> onRelease,
                                          Action<MouseEventInputs> onMouseOver, Action<MouseEventInputs> onMouseLeave,
                                          List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                          ProviderAtTime<FloatBox> renderingAreaProvider,
                                          int z, UUID uuid,
                                          RenderableStack containingStack,
                                          long startTimestamp, Long pausedTimestamp,
                                          Long mostRecentTimestamp)
            throws IllegalArgumentException {
        return new FiniteAnimationRenderableImpl(animation, borderThicknessProvider,
                borderColorProvider, onPress, onRelease, onMouseOver, onMouseLeave,
                colorShiftProviders, renderingAreaProvider, z, uuid, containingStack,
                RENDERING_BOUNDARIES, startTimestamp, pausedTimestamp, mostRecentTimestamp);
    }
}
