package inaugural.soliloquy.graphics.test.unit.renderables.providers;

import inaugural.soliloquy.graphics.renderables.providers.GlobalLoopingAnimationImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeAnimation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.assets.GlobalLoopingAnimation;

import static org.junit.jupiter.api.Assertions.*;

class GlobalLoopingAnimationImplTests {
    private final String ID = "globalLoopingAnimationId";
    private final int MS_DURATION = 456;
    private final int PERIOD_MODULO_OFFSET = 123;
    private final String ANIMATION_ID = "animationId";
    private final FakeAnimation ANIMATION = new FakeAnimation(ANIMATION_ID, MS_DURATION, true);
    private final long MOST_RECENT_TIMESTAMP = 123L;

    private GlobalLoopingAnimation _globalLoopingAnimation;

    @BeforeEach
    void setUp() {
        _globalLoopingAnimation = new GlobalLoopingAnimationImpl(ID, ANIMATION,
                PERIOD_MODULO_OFFSET, null);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationImpl(null, ANIMATION, PERIOD_MODULO_OFFSET, null));
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationImpl("", ANIMATION, PERIOD_MODULO_OFFSET, null));
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationImpl(ID, null, PERIOD_MODULO_OFFSET, null));
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationImpl(ID, ANIMATION, -1, null));
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationImpl(ID, ANIMATION, MS_DURATION, null));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(GlobalLoopingAnimation.class.getCanonicalName(),
                _globalLoopingAnimation.getInterfaceName());
    }

    @Test
    void testId() {
        assertEquals(ID, _globalLoopingAnimation.id());
    }

    @Test
    void testUuid() {
        assertThrows(UnsupportedOperationException.class, _globalLoopingAnimation::uuid);
    }

    @Test
    void testAnimationId() {
        assertEquals(ANIMATION_ID, _globalLoopingAnimation.animationId());
    }

    @Test
    void testSupportsMouseEvents() {
        assertTrue(_globalLoopingAnimation.supportsMouseEvents());

        ANIMATION.SupportsMouseEventCapturing = false;

        assertFalse(_globalLoopingAnimation.supportsMouseEvents());
    }

    @Test
    void testPeriodModuloOffset() {
        assertEquals(PERIOD_MODULO_OFFSET, _globalLoopingAnimation.periodModuloOffset());
    }

    @Test
    void testPauseTimestamp() {
        long pauseTimestamp = 111L;
        assertEquals(pauseTimestamp, (long) new GlobalLoopingAnimationImpl(ID, ANIMATION,
                PERIOD_MODULO_OFFSET, pauseTimestamp).pausedTimestamp());
    }

    @Test
    void testProvideAndMostRecentTimestamp() {
        long timestamp = 789789L;
        int expectedFrame = (int) ((timestamp + PERIOD_MODULO_OFFSET) % MS_DURATION);

        AnimationFrameSnippet providedSnippet = _globalLoopingAnimation.provide(timestamp);

        assertEquals(timestamp, (long) _globalLoopingAnimation.mostRecentTimestamp());
        assertEquals(1, ANIMATION.SnippetsProvided.size());
        Pair<Integer, AnimationFrameSnippet> providedSnippetWithFrame =
                ANIMATION.SnippetsProvided.get(0);
        assertEquals(expectedFrame, (int) providedSnippetWithFrame.getItem1());
        assertSame(providedSnippetWithFrame.getItem2(), providedSnippet);
    }

    @Test
    void testGetArchetype() {
        assertThrows(UnsupportedOperationException.class, _globalLoopingAnimation::getArchetype);
    }

    @Test
    void testReportPauseAndUnpause() {
        long pauseTimestamp = 10000L;
        long unpauseTimestamp = 10001L;
        int expectedPeriodModuloOffset =
                (PERIOD_MODULO_OFFSET - (int) (unpauseTimestamp - pauseTimestamp) + MS_DURATION)
                        % MS_DURATION;

        assertNull(_globalLoopingAnimation.pausedTimestamp());

        _globalLoopingAnimation.reportPause(pauseTimestamp);

        assertEquals(pauseTimestamp, (long) _globalLoopingAnimation.pausedTimestamp());

        _globalLoopingAnimation.reportUnpause(unpauseTimestamp);

        assertNull(_globalLoopingAnimation.pausedTimestamp());
        assertEquals(expectedPeriodModuloOffset, _globalLoopingAnimation.periodModuloOffset());
    }

    @Test
    void testReportPauseAndUnpauseWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> _globalLoopingAnimation.reportUnpause(MOST_RECENT_TIMESTAMP));

        _globalLoopingAnimation.reportPause(MOST_RECENT_TIMESTAMP);

        assertThrows(IllegalArgumentException.class,
                () -> _globalLoopingAnimation.reportUnpause(MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class,
                () -> _globalLoopingAnimation.reportPause(MOST_RECENT_TIMESTAMP));

        _globalLoopingAnimation.reportUnpause(MOST_RECENT_TIMESTAMP);


        assertThrows(IllegalArgumentException.class,
                () -> _globalLoopingAnimation.reportPause(MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class,
                () -> _globalLoopingAnimation.reportUnpause(MOST_RECENT_TIMESTAMP));
    }

    @Test
    void testTimestampSentToProviderWhenPausedAndUnpaused() {
        long pauseTimestamp = 10000L;
        int expectedMsSentToProviderWhilePaused =
                (int) ((pauseTimestamp + PERIOD_MODULO_OFFSET) % MS_DURATION);
        long unpauseTimestamp = 10500L;
        int periodModuloOffsetAfterUnpaused =
                (PERIOD_MODULO_OFFSET - (int) (unpauseTimestamp - pauseTimestamp) + MS_DURATION)
                        % MS_DURATION;
        long providedTimestampAfterUnpaused = 10525L;
        int expectedMsSentToProviderAfterUnpaused =
                (int) ((providedTimestampAfterUnpaused + periodModuloOffsetAfterUnpaused)
                        % MS_DURATION);

        _globalLoopingAnimation.reportPause(pauseTimestamp);

        assertEquals(PERIOD_MODULO_OFFSET, _globalLoopingAnimation.periodModuloOffset());

        AnimationFrameSnippet providedSnippetWhilePaused =
                _globalLoopingAnimation.provide(pauseTimestamp + 1);

        assertEquals(1, ANIMATION.SnippetsProvided.size());
        Pair<Integer, AnimationFrameSnippet> providedSnippetAndMsPositionWhilePaused =
                ANIMATION.SnippetsProvided.get(0);
        assertEquals(expectedMsSentToProviderWhilePaused,
                (int) providedSnippetAndMsPositionWhilePaused.getItem1());
        assertSame(providedSnippetWhilePaused, providedSnippetAndMsPositionWhilePaused.getItem2());

        _globalLoopingAnimation.reportUnpause(unpauseTimestamp);

        assertEquals(periodModuloOffsetAfterUnpaused,
                _globalLoopingAnimation.periodModuloOffset());

        AnimationFrameSnippet providedSnippetAfterUnpaused =
                _globalLoopingAnimation.provide(providedTimestampAfterUnpaused);

        assertEquals(2, ANIMATION.SnippetsProvided.size());
        Pair<Integer, AnimationFrameSnippet> providedSnippetAndMsPositionAfterUnpaused =
                ANIMATION.SnippetsProvided.get(1);
        assertEquals(expectedMsSentToProviderAfterUnpaused,
                (int) providedSnippetAndMsPositionAfterUnpaused.getItem1());
        assertSame(providedSnippetAfterUnpaused,
                providedSnippetAndMsPositionAfterUnpaused.getItem2());
    }

    @Test
    void testReset() {
        long resetTimestamp = 123123L;

        _globalLoopingAnimation.reset(resetTimestamp);

        assertEquals(MS_DURATION - (resetTimestamp % MS_DURATION),
                _globalLoopingAnimation.periodModuloOffset());
    }

    @Test
    void testReportPauseOrProvideWithOutdatedTimestamp() {
        long timestamp = 123123L;

        _globalLoopingAnimation.provide(timestamp);

        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimation.provide(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimation.reportPause(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimation.reportUnpause(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimation.reset(timestamp - 1));

        _globalLoopingAnimation.reportPause(timestamp + 1);

        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimation.provide(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimation.reportUnpause(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimation.reset(timestamp));

        _globalLoopingAnimation.reportUnpause(timestamp + 2);

        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimation.provide(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimation.reportPause(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimation.reset(timestamp + 1));

        _globalLoopingAnimation.provide(timestamp + 3);

        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimation.provide(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimation.reportPause(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimation.reportUnpause(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimation.reset(timestamp + 2));
    }

    @Test
    void testRepresentation() {
        assertEquals(ANIMATION_ID, _globalLoopingAnimation.representation());
    }
}
