package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.renderables.providers.GlobalLoopingAnimation;

public class GlobalLoopingAnimationImpl
        extends LoopingProviderAbstract<AnimationFrameSnippet>
        implements GlobalLoopingAnimation {
    private final Animation ANIMATION;

    /** @noinspection ConstantConditions*/
    public GlobalLoopingAnimationImpl(EntityUuid uuid, Animation animation,
                                      int periodModuloOffset, Long mostRecentTimestamp) {
        super(uuid, Check.ifNull(animation, "animation").msDuration(), periodModuloOffset,
                mostRecentTimestamp);
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
    public AnimationFrameSnippet getArchetype() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getInterfaceName() {
        return GlobalLoopingAnimation.class.getCanonicalName();
    }
}
