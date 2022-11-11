package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.renderables.FiniteAnimationRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;

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
                                         List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                         ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                         UUID uuid,
                                         RenderableStack containingStack,
                                         RenderingBoundaries renderingBoundaries,
                                         long startTimestamp, Long pausedTimestamp,
                                         Long mostRecentTimestamp) {
        super(colorShiftProviders, borderThicknessProvider, borderColorProvider,
                renderingAreaProvider, z, uuid, containingStack, renderingBoundaries);
        ANIMATION = Check.ifNull(animation, "animation");
        this.startTimestamp = startTimestamp;
        checkPausedTimestampAndMostRecentTimestamp(pausedTimestamp, mostRecentTimestamp);
        this.pausedTimestamp = pausedTimestamp;
        if (mostRecentTimestamp != null) {
            TIMESTAMP_VALIDATOR.validateTimestamp(mostRecentTimestamp);
        }
    }

    public FiniteAnimationRenderableImpl(Animation animation,
                                         ProviderAtTime<Float> borderThicknessProvider,
                                         ProviderAtTime<Color> borderColorProvider,
                                         Map<Integer, Action<MouseEventInputs>> onPress,
                                         Map<Integer, Action<MouseEventInputs>> onRelease,
                                         Action<MouseEventInputs> onMouseOver,
                                         Action<MouseEventInputs> onMouseLeave,
                                         List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                         ProviderAtTime<FloatBox> renderingAreaProvider,
                                         int z, UUID uuid,
                                         RenderableStack containingStack,
                                         RenderingBoundaries renderingBoundaries,
                                         long startTimestamp, Long pausedTimestamp,
                                         Long mostRecentTimestamp) {
        super(onPress, onRelease, onMouseOver, onMouseLeave, colorShiftProviders,
                borderThicknessProvider, borderColorProvider, renderingAreaProvider, z, uuid,
                containingStack, renderingBoundaries);
        ANIMATION = Check.ifNull(animation, "animation");
        this.startTimestamp = startTimestamp;
        checkPausedTimestampAndMostRecentTimestamp(pausedTimestamp, mostRecentTimestamp);
        this.pausedTimestamp = pausedTimestamp;
        if (mostRecentTimestamp != null) {
            TIMESTAMP_VALIDATOR.validateTimestamp(mostRecentTimestamp);
        }
    }

    private void checkPausedTimestampAndMostRecentTimestamp(Long pausedTimestamp,
                                                            Long mostRecentTimestamp) {
        if (pausedTimestamp != null) {
            if (mostRecentTimestamp == null) {
                throw new IllegalArgumentException("FiniteAnimationRenderableImpl: " +
                        "pausedTimestamp cannot be non-null if mostRecentTimestamp is null");
            }
            else {
                Check.throwOnSecondGt(mostRecentTimestamp, pausedTimestamp,
                        "mostRecentTimestamp", "pausedTimestamp");
            }
        }
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
    public AnimationFrameSnippet getArchetype() {
        throw new UnsupportedOperationException();
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

    @Override
    public Long mostRecentTimestamp() {
        return TIMESTAMP_VALIDATOR.mostRecentTimestamp();
    }

    @Override
    public String getInterfaceName() {
        return FiniteAnimationRenderable.class.getCanonicalName();
    }
}
