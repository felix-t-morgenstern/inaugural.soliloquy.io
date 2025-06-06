package inaugural.soliloquy.graphics.renderables.factories;

import inaugural.soliloquy.graphics.renderables.GlobalLoopingAnimationRenderableImpl;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.graphics.assets.GlobalLoopingAnimation;
import soliloquy.specs.graphics.renderables.GlobalLoopingAnimationRenderable;
import soliloquy.specs.graphics.renderables.RenderableWithMouseEvents.MouseEventInputs;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.factories.GlobalLoopingAnimationRenderableFactory;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GlobalLoopingAnimationRenderableFactoryImpl
        implements GlobalLoopingAnimationRenderableFactory {
    private final RenderingBoundaries RENDERING_BOUNDARIES;

    public GlobalLoopingAnimationRenderableFactoryImpl(RenderingBoundaries renderingBoundaries) {
        RENDERING_BOUNDARIES = Check.ifNull(renderingBoundaries, "renderingBoundaries");
    }

    @Override
    public GlobalLoopingAnimationRenderable make(GlobalLoopingAnimation globalLoopingAnimation,
                                                 ProviderAtTime<Float> borderThicknessProvider,
                                                 ProviderAtTime<Color> borderColorProvider,
                                                 List<ProviderAtTime<ColorShift>>
                                                         colorShiftProviders,
                                                 ProviderAtTime<FloatBox> renderingAreaProvider,
                                                 int z, UUID uuid,
                                                 RenderableStack containingStack)
            throws IllegalArgumentException {
        return new GlobalLoopingAnimationRenderableImpl(globalLoopingAnimation,
                borderThicknessProvider, borderColorProvider, colorShiftProviders,
                renderingAreaProvider, z, uuid, containingStack, RENDERING_BOUNDARIES);
    }

    @Override
    public GlobalLoopingAnimationRenderable make(GlobalLoopingAnimation globalLoopingAnimation,
                                                 ProviderAtTime<Float> borderThicknessProvider,
                                                 ProviderAtTime<Color> borderColorProvider,
                                                 Map<Integer, Action<MouseEventInputs>> onPress,
                                                 Map<Integer, Action<MouseEventInputs>> onRelease,
                                                 Action<MouseEventInputs> onMouseOver,
                                                 Action<MouseEventInputs> onMouseLeave,
                                                 List<ProviderAtTime<ColorShift>>
                                                         colorShiftProviders,
                                                 ProviderAtTime<FloatBox> renderingAreaProvider,
                                                 int z, UUID uuid,
                                                 RenderableStack containingStack)
            throws IllegalArgumentException {
        return new GlobalLoopingAnimationRenderableImpl(globalLoopingAnimation,
                borderThicknessProvider, borderColorProvider, onPress, onRelease, onMouseOver,
                onMouseLeave, colorShiftProviders, renderingAreaProvider, z, uuid,
                containingStack, RENDERING_BOUNDARIES);
    }
}
