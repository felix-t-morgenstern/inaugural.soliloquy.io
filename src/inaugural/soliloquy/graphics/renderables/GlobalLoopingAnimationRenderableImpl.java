package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.graphics.assets.GlobalLoopingAnimation;
import soliloquy.specs.graphics.renderables.GlobalLoopingAnimationRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GlobalLoopingAnimationRenderableImpl
        extends AbstractImageAssetRenderable
        implements GlobalLoopingAnimationRenderable {
    private GlobalLoopingAnimation _globalLoopingAnimation;

    public GlobalLoopingAnimationRenderableImpl(GlobalLoopingAnimation globalLoopingAnimation,
                                                ProviderAtTime<Float> borderThicknessProvider,
                                                ProviderAtTime<Color> borderColorProvider,
                                                List<ProviderAtTime<ColorShift>>
                                                        colorShiftProviders,
                                                ProviderAtTime<FloatBox> renderingAreaProvider,
                                                int z, UUID uuid,
                                                RenderableStack containingStack,
                                                RenderingBoundaries renderingBoundaries) {
        super(colorShiftProviders, borderThicknessProvider, borderColorProvider,
                renderingAreaProvider, z, uuid, containingStack, renderingBoundaries);
        setGlobalLoopingAnimation(globalLoopingAnimation);
    }

    public GlobalLoopingAnimationRenderableImpl(GlobalLoopingAnimation globalLoopingAnimation,
                                                ProviderAtTime<Float> borderThicknessProvider,
                                                ProviderAtTime<Color> borderColorProvider,
                                                Map<Integer, Action<Long>> onPress,
                                                Map<Integer, Action<Long>> onRelease,
                                                Action<Long> onMouseOver,
                                                Action<Long> onMouseLeave,
                                                List<ProviderAtTime<ColorShift>>
                                                        colorShiftProviders,
                                                ProviderAtTime<FloatBox> renderingAreaProvider,
                                                int z, UUID uuid,
                                                RenderableStack containingStack,
                                                RenderingBoundaries renderingBoundaries) {
        super(onPress, onRelease, onMouseOver, onMouseLeave, colorShiftProviders,
                borderThicknessProvider, borderColorProvider, renderingAreaProvider, z, uuid,
                containingStack, renderingBoundaries);
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

    @Override
    public boolean capturesMouseEventAtPoint(float x, float y, long timestamp)
            throws UnsupportedOperationException, IllegalArgumentException {
        return capturesMouseEventAtPoint(x, y, timestamp,
                () -> _globalLoopingAnimation.provide(timestamp));
    }
}
