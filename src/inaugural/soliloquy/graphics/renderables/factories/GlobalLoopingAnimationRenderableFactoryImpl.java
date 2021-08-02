package inaugural.soliloquy.graphics.renderables.factories;

import inaugural.soliloquy.graphics.renderables.GlobalLoopingAnimationRenderableImpl;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.GlobalLoopingAnimationRenderable;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.factories.GlobalLoopingAnimationRenderableFactory;
import soliloquy.specs.graphics.renderables.providers.GlobalLoopingAnimation;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class GlobalLoopingAnimationRenderableFactoryImpl
        implements GlobalLoopingAnimationRenderableFactory {
    @Override
    public GlobalLoopingAnimationRenderable make(GlobalLoopingAnimation globalLoopingAnimation,
                                                 ProviderAtTime<Float> borderThicknessProvider,
                                                 ProviderAtTime<Color> borderColorProvider,
                                                 List<ColorShift> colorShifts,
                                                 ProviderAtTime<FloatBox> renderingAreaProvider,
                                                 int z, EntityUuid uuid,
                                                 Consumer<Renderable> updateZIndexInContainer,
                                                 Consumer<Renderable> removeFromContainer)
            throws IllegalArgumentException {
        return new GlobalLoopingAnimationRenderableImpl(globalLoopingAnimation,
                borderThicknessProvider, borderColorProvider, colorShifts, renderingAreaProvider,
                z, uuid, updateZIndexInContainer, removeFromContainer);
    }

    @Override
    public GlobalLoopingAnimationRenderable make(GlobalLoopingAnimation globalLoopingAnimation,
                                                 ProviderAtTime<Float> borderThicknessProvider,
                                                 ProviderAtTime<Color> borderColorProvider,
                                                 Map<Integer, Action<Long>> onPress,
                                                 Map<Integer, Action<Long>> onRelease,
                                                 Action<Long> onMouseOver,
                                                 Action<Long> onMouseLeave,
                                                 List<ColorShift> colorShifts,
                                                 ProviderAtTime<FloatBox> renderingAreaProvider,
                                                 int z, EntityUuid uuid,
                                                 Consumer<Renderable> updateZIndexInContainer,
                                                 Consumer<Renderable> removeFromContainer)
            throws IllegalArgumentException {
        return new GlobalLoopingAnimationRenderableImpl(globalLoopingAnimation,
                borderThicknessProvider, borderColorProvider, onPress, onRelease, onMouseOver,
                onMouseLeave, colorShifts, renderingAreaProvider, z, uuid, updateZIndexInContainer,
                removeFromContainer);
    }

    @Override
    public String getInterfaceName() {
        return GlobalLoopingAnimationRenderableFactory.class.getCanonicalName();
    }
}
