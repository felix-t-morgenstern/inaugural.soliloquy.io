package inaugural.soliloquy.io.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.io.graphics.assets.Animation;
import soliloquy.specs.io.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.io.graphics.assets.GlobalLoopingAnimation;

import java.util.UUID;

public class GlobalLoopingAnimationImpl
        extends AbstractLoopingProvider<AnimationFrameSnippet>
        implements GlobalLoopingAnimation {
    private final String ID;
    private final Animation ANIMATION;

    private static final UUID PLACEHOLDER_UUID = new UUID(0, 0);

    public GlobalLoopingAnimationImpl(String id, Animation animation, int periodModuloOffset,
                                      Long pausedTimestamp) {
        // NB: pausedTimestamp is used for both pausedTimestamp and mostRecentTimestamp in parent
        //     constructor since mostRecentTimestamp must be non-null if pausedTimestamp is
        //     non-null
        super(PLACEHOLDER_UUID, Check.ifNull(animation, "animation").msDuration(),
                periodModuloOffset, pausedTimestamp, pausedTimestamp);
        ID = Check.ifNullOrEmpty(id, "id");
        ANIMATION = animation;
    }

    @Override
    public boolean supportsMouseEvents() {
        return ANIMATION.supportsMouseEventCapturing();
    }

    @Override
    public String animationId() {
        return ANIMATION.id();
    }

    @Override
    protected AnimationFrameSnippet provideValueAtMsWithinPeriod(int msWithinPeriod) {
        return ANIMATION.snippetAtFrame(msWithinPeriod);
    }

    @Override
    public String id() throws IllegalStateException {
        return ID;
    }

    @Override
    public UUID uuid() throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "GlobalLoopingAnimationImpl: uuid is not supported");
    }

    @Override
    public Object representation() {
        return ANIMATION.id();
    }
}
