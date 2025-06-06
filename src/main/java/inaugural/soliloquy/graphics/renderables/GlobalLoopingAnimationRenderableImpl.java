package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.assets.GlobalLoopingAnimation;
import soliloquy.specs.graphics.renderables.GlobalLoopingAnimationRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GlobalLoopingAnimationRenderableImpl
        extends AbstractImageAssetRenderable
        implements GlobalLoopingAnimationRenderable {
    private GlobalLoopingAnimation globalLoopingAnimation;

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
                                                Map<Integer, Action<MouseEventInputs>> onPress,
                                                Map<Integer, Action<MouseEventInputs>> onRelease,
                                                Action<MouseEventInputs> onMouseOver,
                                                Action<MouseEventInputs> onMouseLeave,
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
        return globalLoopingAnimation.supportsMouseEvents();
    }

    @Override
    protected String className() {
        return "GlobalLoopingAnimationRenderableImpl";
    }

    @Override
    public GlobalLoopingAnimation getGlobalLoopingAnimation() {
        return globalLoopingAnimation;
    }

    @Override
    public void setGlobalLoopingAnimation(GlobalLoopingAnimation globalLoopingAnimation)
            throws IllegalArgumentException {
        Check.ifNull(globalLoopingAnimation, "globalLoopingAnimation");
        if (capturesMouseEvents && !globalLoopingAnimation.supportsMouseEvents()) {
            throw new IllegalArgumentException(
                    "GlobalLoopingAnimationRenderableImpl.setGlobalLoopingAnimation: cannot " +
                            "assign GlobalLoopingAnimation which does not support mouse events " +
                            "to a setGlobalLoopingAnimationRenderable which does support mouse " +
                            "events");
        }
        this.globalLoopingAnimation = globalLoopingAnimation;
    }

    @Override
    public boolean capturesMouseEventAtPoint(Vertex point, long timestamp)
            throws UnsupportedOperationException, IllegalArgumentException {
        return capturesMouseEventAtPoint(point, timestamp,
                () -> globalLoopingAnimation.provide(timestamp));
    }
}
