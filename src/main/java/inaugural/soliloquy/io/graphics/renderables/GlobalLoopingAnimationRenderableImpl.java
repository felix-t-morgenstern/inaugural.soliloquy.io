package inaugural.soliloquy.io.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.assets.GlobalLoopingAnimation;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.GlobalLoopingAnimationRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.ui.EventInputs;

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
                                                List<ColorShift> colorShifts,
                                                ProviderAtTime<FloatBox> renderingAreaProvider,
                                                int z, UUID uuid,
                                                Component component,
                                                RenderingBoundaries renderingBoundaries,
                                                TimestampValidator timestampValidator) {
        super(colorShifts, borderThicknessProvider, borderColorProvider, renderingAreaProvider, z,
                uuid, component, renderingBoundaries, timestampValidator);
        setGlobalLoopingAnimation(globalLoopingAnimation);
    }

    public GlobalLoopingAnimationRenderableImpl(GlobalLoopingAnimation globalLoopingAnimation,
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
                                                RenderingBoundaries renderingBoundaries,
                                                TimestampValidator timestampValidator) {
        super(onPress, onRelease, onMouseOver, onMouseLeave, colorShifts, borderThicknessProvider,
                borderColorProvider, renderingAreaProvider, z, uuid, component,
                renderingBoundaries, timestampValidator);
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
