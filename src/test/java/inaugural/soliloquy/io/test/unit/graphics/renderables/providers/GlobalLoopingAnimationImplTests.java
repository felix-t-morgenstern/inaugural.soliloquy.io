package inaugural.soliloquy.io.test.unit.graphics.renderables.providers;

import inaugural.soliloquy.io.graphics.renderables.providers.GlobalLoopingAnimationImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeAnimation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.io.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.io.graphics.assets.GlobalLoopingAnimation;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalLoopingAnimationImplTests {
    private final String ID = "globalLoopingAnimationId";
    private final int MS_DURATION = 456;
    private final int PERIOD_MODULO_OFFSET = 123;
    private final String ANIMATION_ID = "animationId";
    private final FakeAnimation ANIMATION = new FakeAnimation(ANIMATION_ID, MS_DURATION, true);
    private final long MOST_RECENT_TIMESTAMP = 123L;

    private GlobalLoopingAnimation globalLoopingAnimation;

    @BeforeEach
    public void setUp() {
        globalLoopingAnimation = new GlobalLoopingAnimationImpl(ID, ANIMATION,
                PERIOD_MODULO_OFFSET, null);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
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
    public void testId() {
        assertEquals(ID, globalLoopingAnimation.id());
    }

    @Test
    public void testUuid() {
        assertThrows(UnsupportedOperationException.class, globalLoopingAnimation::uuid);
    }

    @Test
    public void testAnimationId() {
        assertEquals(ANIMATION_ID, globalLoopingAnimation.animationId());
    }

    @Test
    public void testSupportsMouseEvents() {
        assertTrue(globalLoopingAnimation.supportsMouseEvents());

        ANIMATION.SupportsMouseEventCapturing = false;

        assertFalse(globalLoopingAnimation.supportsMouseEvents());
    }

    @Test
    public void testPeriodModuloOffset() {
        assertEquals(PERIOD_MODULO_OFFSET, globalLoopingAnimation.periodModuloOffset());
    }

    @Test
    public void testPauseTimestamp() {
        long pauseTimestamp = 111L;
        assertEquals(pauseTimestamp, (long) new GlobalLoopingAnimationImpl(ID, ANIMATION,
                PERIOD_MODULO_OFFSET, pauseTimestamp).pausedTimestamp());
    }

    @Test
    public void testProvideAndMostRecentTimestamp() {
        long timestamp = 789789L;
        int expectedFrame = (int) ((timestamp + PERIOD_MODULO_OFFSET) % MS_DURATION);

        AnimationFrameSnippet providedSnippet = globalLoopingAnimation.provide(timestamp);

        assertEquals(timestamp, (long) globalLoopingAnimation.mostRecentTimestamp());
        assertEquals(1, ANIMATION.SnippetsProvided.size());
        Pair<Integer, AnimationFrameSnippet> providedSnippetWithFrame =
                ANIMATION.SnippetsProvided.getFirst();
        assertEquals(expectedFrame, (int) providedSnippetWithFrame.FIRST);
        assertSame(providedSnippetWithFrame.SECOND, providedSnippet);
    }

    @Test
    public void testReportPauseAndUnpause() {
        long pauseTimestamp = 10000L;
        long unpauseTimestamp = 10001L;
        int expectedPeriodModuloOffset =
                (PERIOD_MODULO_OFFSET - (int) (unpauseTimestamp - pauseTimestamp) + MS_DURATION)
                        % MS_DURATION;

        assertNull(globalLoopingAnimation.pausedTimestamp());

        globalLoopingAnimation.reportPause(pauseTimestamp);

        assertEquals(pauseTimestamp, (long) globalLoopingAnimation.pausedTimestamp());

        globalLoopingAnimation.reportUnpause(unpauseTimestamp);

        assertNull(globalLoopingAnimation.pausedTimestamp());
        assertEquals(expectedPeriodModuloOffset, globalLoopingAnimation.periodModuloOffset());
    }

    @Test
    public void testReportPauseWhilePausedOrViceVersa() {
        assertThrows(UnsupportedOperationException.class, () ->
                globalLoopingAnimation.reportUnpause(0L));

        globalLoopingAnimation.reportPause(0L);

        assertThrows(UnsupportedOperationException.class, () ->
                globalLoopingAnimation.reportPause(0L));
    }

    @Test
    public void testTimestampSentToProviderWhenPausedAndUnpaused() {
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

        globalLoopingAnimation.reportPause(pauseTimestamp);

        assertEquals(PERIOD_MODULO_OFFSET, globalLoopingAnimation.periodModuloOffset());

        AnimationFrameSnippet providedSnippetWhilePaused =
                globalLoopingAnimation.provide(pauseTimestamp + 1);

        assertEquals(1, ANIMATION.SnippetsProvided.size());
        Pair<Integer, AnimationFrameSnippet> providedSnippetAndMsPositionWhilePaused =
                ANIMATION.SnippetsProvided.getFirst();
        assertEquals(expectedMsSentToProviderWhilePaused,
                (int) providedSnippetAndMsPositionWhilePaused.FIRST);
        assertSame(providedSnippetWhilePaused, providedSnippetAndMsPositionWhilePaused.SECOND);

        globalLoopingAnimation.reportUnpause(unpauseTimestamp);

        assertEquals(periodModuloOffsetAfterUnpaused,
                globalLoopingAnimation.periodModuloOffset());

        AnimationFrameSnippet providedSnippetAfterUnpaused =
                globalLoopingAnimation.provide(providedTimestampAfterUnpaused);

        assertEquals(2, ANIMATION.SnippetsProvided.size());
        Pair<Integer, AnimationFrameSnippet> providedSnippetAndMsPositionAfterUnpaused =
                ANIMATION.SnippetsProvided.get(1);
        assertEquals(expectedMsSentToProviderAfterUnpaused,
                (int) providedSnippetAndMsPositionAfterUnpaused.FIRST);
        assertSame(providedSnippetAfterUnpaused,
                providedSnippetAndMsPositionAfterUnpaused.SECOND);
    }

    @Test
    public void testReset() {
        long resetTimestamp = 123123L;

        globalLoopingAnimation.reset(resetTimestamp);

        assertEquals(MS_DURATION - (resetTimestamp % MS_DURATION),
                globalLoopingAnimation.periodModuloOffset());
    }

    @Test
    public void testReportPauseOrProvideWithOutdatedTimestamp() {
        long timestamp = 123123L;

        globalLoopingAnimation.provide(timestamp);

        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimation.provide(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimation.reportPause(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimation.reportUnpause(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimation.reset(timestamp - 1));

        globalLoopingAnimation.reportPause(timestamp + 1);

        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimation.provide(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimation.reportUnpause(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimation.reset(timestamp));

        globalLoopingAnimation.reportUnpause(timestamp + 2);

        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimation.provide(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimation.reportPause(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimation.reset(timestamp + 1));

        globalLoopingAnimation.provide(timestamp + 3);

        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimation.provide(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimation.reportPause(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimation.reportUnpause(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimation.reset(timestamp + 2));
    }

    @Test
    public void testRepresentation() {
        assertEquals(ANIMATION_ID, globalLoopingAnimation.representation());
    }
}
