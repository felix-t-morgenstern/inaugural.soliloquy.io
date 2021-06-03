package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.renderables.providers.GlobalLoopingAnimation;

public class GlobalLoopingAnimationImpl implements GlobalLoopingAnimation {
    private final Animation ANIMATION;
    private int _periodModuloOffset;
    private Long _pausedTimestamp;
    private Long _mostRecentReportedTimestamp;

    private long _timestampSentToProviderMostRecently;
    private int _periodModuloOffsetAtMostRecentProvision;
    private AnimationFrameSnippet _mostRecentlyProvidedFrameSnippet;

    /** @noinspection ConstantConditions*/
    public GlobalLoopingAnimationImpl(Animation animation, int periodModuloOffset) {
        ANIMATION = Check.ifNull(animation, "animation");
        if (periodModuloOffset >= ANIMATION.msDuration()) {
            throw new IllegalArgumentException("GlobalLoopingAnimationImpl: periodModuloOffset " +
                    "cannot be greater than duration of animation");
        }
        _periodModuloOffset = Check.throwOnLtValue(periodModuloOffset, 0, "periodModuloOffset");
    }

    @Override
    public boolean supportsMouseEvents() {
        return ANIMATION.supportsMouseEventCapturing();
    }

    @Override
    public int periodModuloOffset() {
        return _periodModuloOffset;
    }

    // TODO: Remember to clear the debugging shit out of here
    @Override
    public AnimationFrameSnippet provide(long timestamp) throws IllegalArgumentException {
        long timestampForProvider;
        if (_pausedTimestamp == null) {
            timestampForProvider = timestamp;
        }
        else {
            timestampForProvider = _pausedTimestamp;
        }
        if (_timestampSentToProviderMostRecently == timestampForProvider &&
                _periodModuloOffset == _periodModuloOffsetAtMostRecentProvision) {
            System.out.println("(Returning prev snippet)");
            return _mostRecentlyProvidedFrameSnippet;
        }
        _timestampSentToProviderMostRecently = timestampForProvider;
        _periodModuloOffsetAtMostRecentProvision = _periodModuloOffset;
        int msForProvider = (int) ((timestampForProvider + _periodModuloOffset) % ANIMATION.msDuration());
        System.out.println("timestamp = " + timestamp);
        System.out.println("timestampForProvider = " + timestampForProvider);

        System.out.println("msForProvider = " + msForProvider);
        return _mostRecentlyProvidedFrameSnippet = ANIMATION.snippetAtFrame(
                msForProvider);
    }

    @Override
    public AnimationFrameSnippet getArchetype() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reportPause(long timestamp) throws IllegalArgumentException {
        if (_pausedTimestamp != null) {
            throw new IllegalArgumentException("GlobalLoopingAnimationImpl.reportPause: " +
                    "cannot pause if already paused");
        }
        if (_mostRecentReportedTimestamp != null && timestamp < _mostRecentReportedTimestamp) {
            throw new IllegalArgumentException("GlobalLoopingAnimationImpl.reportPause: " +
                    "cannot pause at timestamp prior to most recent unpausing");
        }
        _mostRecentReportedTimestamp = _pausedTimestamp = timestamp;
    }

    @Override
    public void reportUnpause(long timestamp) throws IllegalArgumentException {
        if (_pausedTimestamp == null) {
            throw new IllegalArgumentException("GlobalLoopingAnimationImpl.reportUnpause: " +
                    "cannot unpause if already unpaused");
        }
        if (_mostRecentReportedTimestamp != null && timestamp < _mostRecentReportedTimestamp) {
            throw new IllegalArgumentException("GlobalLoopingAnimationImpl.reportUnpause: " +
                    "cannot unpause at timestamp prior to most recent pausing");
        }
        _periodModuloOffset = (int)((_periodModuloOffset - (timestamp - _pausedTimestamp)
                + ANIMATION.msDuration()) % ANIMATION.msDuration());
        while (_periodModuloOffset < 0) {
            _periodModuloOffset += ANIMATION.msDuration();
        }
        _mostRecentReportedTimestamp = timestamp;
        _pausedTimestamp = null;
    }

    @Override
    public Long pausedTimestamp() {
        return _pausedTimestamp;
    }

    @Override
    public String getInterfaceName() {
        return GlobalLoopingAnimation.class.getCanonicalName();
    }
}
