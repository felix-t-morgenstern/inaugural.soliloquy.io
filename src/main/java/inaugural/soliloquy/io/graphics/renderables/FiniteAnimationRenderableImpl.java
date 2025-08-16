package inaugural.soliloquy.io.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.assets.Animation;
import soliloquy.specs.io.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.io.graphics.renderables.FiniteAnimationRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.ui.Component;
import soliloquy.specs.ui.EventInputs;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// NB: This class contains a lot of redundant code with AbstractFinitePausableAtTime, since Java
//     does not support multiple inheritance, and it is less hasslesome to reproduce the logic of
//     that class, rather than the logic of AbstractImageAssetRenderable.
public class FiniteAnimationRenderableImpl extends AbstractImageAssetRenderable
        implements FiniteAnimationRenderable {
    private final Animation ANIMATION;

    private long startTimestamp;
    private Long pausedTimestamp;

    public FiniteAnimationRenderableImpl(Animation animation,
                                         ProviderAtTime<Float> borderThicknessProvider,
                                         ProviderAtTime<Color> borderColorProvider,
                                         List<ColorShift> colorShifts,
                                         ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                         UUID uuid,
                                         Component component,
                                         RenderingBoundaries renderingBoundaries,
                                         long startTimestamp, Long pausedTimestamp,
                                         TimestampValidator timestampValidator) {
        super(colorShifts, borderThicknessProvider, borderColorProvider, renderingAreaProvider, z,
                uuid, component, renderingBoundaries, timestampValidator);
        ANIMATION = Check.ifNull(animation, "animation");
        this.startTimestamp = startTimestamp;
        this.pausedTimestamp = pausedTimestamp;
    }

    public FiniteAnimationRenderableImpl(Animation animation,
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
                                         long startTimestamp, Long pausedTimestamp,
                                         TimestampValidator timestampValidator) {
        super(onPress, onRelease, onMouseOver, onMouseLeave, colorShifts, borderThicknessProvider,
                borderColorProvider, renderingAreaProvider, z, uuid, component, renderingBoundaries,
                timestampValidator);
        ANIMATION = Check.ifNull(animation, "animation");
        this.startTimestamp = startTimestamp;
        this.pausedTimestamp = pausedTimestamp;
    }

    @Override
    protected boolean underlyingAssetSupportsMouseEvents() {
        return ANIMATION.supportsMouseEventCapturing();
    }

    @Override
    protected String className() {
        return FiniteAnimationRenderableImpl.class.getCanonicalName();
    }

    @Override
    public long startTimestamp() {
        return startTimestamp;
    }

    @Override
    public long endTimestamp() {
        return startTimestamp + ANIMATION.msDuration();
    }

    @Override
    public String animationId() {
        return ANIMATION.id();
    }

    @Override
    public void reportPause(long timestamp) throws IllegalArgumentException {
        if (pausedTimestamp != null) {
            throw new IllegalArgumentException(
                    "FiniteAnimationRenderableImpl.reportPause: already paused");
        }
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);
        pausedTimestamp = timestamp;
    }

    @Override
    public void reportUnpause(long timestamp) throws IllegalArgumentException {
        if (pausedTimestamp == null) {
            throw new IllegalArgumentException(
                    "FiniteAnimationRenderableImpl.reportUnpause: not yet paused");
        }
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);
        startTimestamp += timestamp - pausedTimestamp;
        pausedTimestamp = null;
    }

    @Override
    public Long pausedTimestamp() {
        return pausedTimestamp;
    }

    @Override
    public boolean capturesMouseEventAtPoint(Vertex point, long timestamp)
            throws UnsupportedOperationException, IllegalArgumentException {
        return capturesMouseEventAtPoint(point, timestamp, () -> provide(timestamp));
    }

    @Override
    public AnimationFrameSnippet provide(long timestamp) throws IllegalArgumentException {
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);
        if (pausedTimestamp != null) {
            timestamp = pausedTimestamp;
        }
        return ANIMATION.snippetAtFrame(
                (int) (Math.min(startTimestamp + ANIMATION.msDuration(),
                        Math.max(startTimestamp, timestamp))
                        - startTimestamp));
    }

    @Override
    public Object representation() {
        return null;
    }
}
