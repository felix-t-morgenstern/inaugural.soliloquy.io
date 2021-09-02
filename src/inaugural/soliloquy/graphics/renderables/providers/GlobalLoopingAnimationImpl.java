package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.renderables.providers.GlobalLoopingAnimation;

public class GlobalLoopingAnimationImpl
        extends AbstractLoopingProvider<AnimationFrameSnippet>
        implements GlobalLoopingAnimation {
    private final String ID;
    private final Animation ANIMATION;

    private static final EntityUuid PLACEHOLDER_UUID = new PlaceholderUuid();

    /** @noinspection ConstantConditions*/
    public GlobalLoopingAnimationImpl(String id, Animation animation,
                                      int periodModuloOffset) {
        super(PLACEHOLDER_UUID, Check.ifNull(animation, "animation").msDuration(), periodModuloOffset, null);
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
    public AnimationFrameSnippet getArchetype() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getInterfaceName() {
        return GlobalLoopingAnimation.class.getCanonicalName();
    }

    @Override
    public String id() throws IllegalStateException {
        return ID;
    }

    @Override
    public EntityUuid uuid() throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "GlobalLoopingAnimationImpl: uuid is not supported");
    }

    private static class PlaceholderUuid implements EntityUuid {
        @Override
        public long getMostSignificantBits() {
            return 0;
        }

        @Override
        public long getLeastSignificantBits() {
            return 0;
        }

        @Override
        public String getInterfaceName() {
            return null;
        }
    }
}
