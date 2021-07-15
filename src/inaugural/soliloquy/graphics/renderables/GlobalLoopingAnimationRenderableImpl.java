package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.*;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.providers.GlobalLoopingAnimation;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class GlobalLoopingAnimationRenderableImpl
        extends AbstractRenderableWithArea
        implements GlobalLoopingAnimationRenderable {
    private GlobalLoopingAnimation _globalLoopingAnimation;

    public GlobalLoopingAnimationRenderableImpl(GlobalLoopingAnimation globalLoopingAnimation,
                                                ProviderAtTime<Float> borderThicknessProvider,
                                                ProviderAtTime<Color> borderColorProvider,
                                                List<ColorShift> colorShifts,
                                                ProviderAtTime<FloatBox> renderingAreaProvider,
                                                int z, EntityUuid uuid,
                                                Consumer<Renderable> updateZIndexInContainer,
                                                Consumer<Renderable> removeFromContainer) {
        super(colorShifts, borderThicknessProvider, borderColorProvider, renderingAreaProvider, z,
                uuid, updateZIndexInContainer, removeFromContainer);
        setGlobalLoopingAnimation(globalLoopingAnimation);
    }

    public GlobalLoopingAnimationRenderableImpl(GlobalLoopingAnimation globalLoopingAnimation,
                                                ProviderAtTime<Float> borderThicknessProvider,
                                                ProviderAtTime<Color> borderColorProvider,
                                                Map<Integer, Action<Long>> onClick,
                                                Map<Integer, Action<Long>> onRelease,
                                                Action<Long> onMouseOver,
                                                Action<Long> onMouseLeave,
                                                List<ColorShift> colorShifts,
                                                ProviderAtTime<FloatBox> renderingAreaProvider,
                                                int z, EntityUuid uuid,
                                                Consumer<Renderable> updateZIndexInContainer,
                                                Consumer<Renderable> removeFromContainer) {
        super(onClick, onRelease, onMouseOver, onMouseLeave, colorShifts, borderThicknessProvider,
                borderColorProvider, renderingAreaProvider, z, uuid, updateZIndexInContainer,
                removeFromContainer);
        setGlobalLoopingAnimation(globalLoopingAnimation);
    }

    @Override
    protected boolean underlyingAssetSupportsMouseEvents() {
        return _globalLoopingAnimation.supportsMouseEvents();
    }

    @Override
    protected String className() {
        return "GlobalLoopingAnimationRenderableImpl";
    }

    @Override
    public String getInterfaceName() {
        return GlobalLoopingAnimationRenderable.class.getCanonicalName();
    }

    @Override
    public GlobalLoopingAnimation getGlobalLoopingAnimation() {
        return _globalLoopingAnimation;
    }

    @Override
    public void setGlobalLoopingAnimation(GlobalLoopingAnimation globalLoopingAnimation)
            throws IllegalArgumentException {
        Check.ifNull(globalLoopingAnimation, "globalLoopingAnimation");
        if (_capturesMouseEvents && !globalLoopingAnimation.supportsMouseEvents()) {
            throw new IllegalArgumentException(
                    "GlobalLoopingAnimationRenderableImpl.setGlobalLoopingAnimation: cannot " +
                            "assign GlobalLoopingAnimation which does not support mouse events " +
                            "to a setGlobalLoopingAnimationRenderable which does support mouse " +
                            "events");
        }
        _globalLoopingAnimation = globalLoopingAnimation;
    }
}
