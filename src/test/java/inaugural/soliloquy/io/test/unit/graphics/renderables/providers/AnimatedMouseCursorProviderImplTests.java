package inaugural.soliloquy.io.test.unit.graphics.renderables.providers;

import inaugural.soliloquy.io.graphics.renderables.providers.AnimatedMouseCursorProviderImpl;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.graphics.renderables.providers.AnimatedMouseCursorProvider;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;


import java.util.Map;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomString;
import static org.junit.jupiter.api.Assertions.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

@ExtendWith(MockitoExtension.class)
public class AnimatedMouseCursorProviderImplTests {
    private final int MS_1 = 0;
    private final long MOUSE_CURSOR_1 = 123L;
    private final int MS_2 = 111;
    private final long MOUSE_CURSOR_2 = 456L;
    private final int MS_3 = 444;
    private final long MOUSE_CURSOR_3 = 789L;

    private final Map<Integer, Long> CURSORS_AT_MS = mapOf();

    private final String ID = randomString();
    private final int MS_DURATION = 777;
    private final int PERIOD_MODULO_OFFSET = 321;
    private final long MOST_RECENT_TIMESTAMP = 12L;

    @Mock private TimestampValidator mockTimestampValidator;

    private AnimatedMouseCursorProvider animatedMouseCursorProvider;

    @BeforeEach
    public void setUp() {
        CURSORS_AT_MS.put(MS_1, MOUSE_CURSOR_1);
        CURSORS_AT_MS.put(MS_2, MOUSE_CURSOR_2);
        CURSORS_AT_MS.put(MS_3, MOUSE_CURSOR_3);

        animatedMouseCursorProvider = new AnimatedMouseCursorProviderImpl(ID, CURSORS_AT_MS,
                MS_DURATION, PERIOD_MODULO_OFFSET, null, mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new AnimatedMouseCursorProviderImpl(null,
                CURSORS_AT_MS, MS_DURATION, PERIOD_MODULO_OFFSET, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () -> new AnimatedMouseCursorProviderImpl("",
                CURSORS_AT_MS, MS_DURATION, PERIOD_MODULO_OFFSET, null, mockTimestampValidator));

        assertThrows(IllegalArgumentException.class, () -> new AnimatedMouseCursorProviderImpl(ID,
                null, MS_DURATION, PERIOD_MODULO_OFFSET, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () -> new AnimatedMouseCursorProviderImpl(ID,
                mapOf(), MS_DURATION, PERIOD_MODULO_OFFSET, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new AnimatedMouseCursorProviderImpl(ID, mapOf(pairOf(null, MOUSE_CURSOR_1)),
                        MS_DURATION, PERIOD_MODULO_OFFSET, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new AnimatedMouseCursorProviderImpl(ID, mapOf(pairOf(-1, MOUSE_CURSOR_1)),
                        MS_DURATION, PERIOD_MODULO_OFFSET, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new AnimatedMouseCursorProviderImpl(ID, mapOf(pairOf(MS_1, null)),
                        MS_DURATION, PERIOD_MODULO_OFFSET, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new AnimatedMouseCursorProviderImpl(ID, mapOf(pairOf(1, MOUSE_CURSOR_1)),
                        MS_DURATION, PERIOD_MODULO_OFFSET, null, mockTimestampValidator));

        assertThrows(IllegalArgumentException.class, () -> new AnimatedMouseCursorProviderImpl(ID,
                CURSORS_AT_MS, 0, PERIOD_MODULO_OFFSET, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () -> new AnimatedMouseCursorProviderImpl(ID,
                CURSORS_AT_MS, MS_3, PERIOD_MODULO_OFFSET, null, mockTimestampValidator));

        assertThrows(IllegalArgumentException.class, () -> new AnimatedMouseCursorProviderImpl(ID,
                CURSORS_AT_MS, MS_DURATION, MS_DURATION, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () -> new AnimatedMouseCursorProviderImpl(ID,
                CURSORS_AT_MS, MS_DURATION, -1, null, mockTimestampValidator));

        assertThrows(IllegalArgumentException.class, () -> new AnimatedMouseCursorProviderImpl(ID,
                CURSORS_AT_MS, MS_DURATION, PERIOD_MODULO_OFFSET, MOST_RECENT_TIMESTAMP, null));
    }

    @Test
    public void testUuid() {
        assertThrows(UnsupportedOperationException.class, animatedMouseCursorProvider::uuid);
    }

    @Test
    public void testId() {
        assertEquals(ID, animatedMouseCursorProvider.id());
    }

    @Test
    public void testPausedTimestamp() {
        var pausedTimestamp = -234L;

        ProviderAtTime<Long> animatedMouseCursorProvider =
                new AnimatedMouseCursorProviderImpl(ID, CURSORS_AT_MS, MS_DURATION,
                        PERIOD_MODULO_OFFSET, pausedTimestamp, mockTimestampValidator);

        assertEquals(pausedTimestamp, (long) animatedMouseCursorProvider.pausedTimestamp());
    }

    @Test
    public void testProvide() {
        assertEquals(MOUSE_CURSOR_3, (long) animatedMouseCursorProvider
                .provide(MS_DURATION - PERIOD_MODULO_OFFSET - 1));
        assertEquals(MOUSE_CURSOR_1, (long) animatedMouseCursorProvider
                .provide(MS_DURATION - PERIOD_MODULO_OFFSET));
        assertEquals(MOUSE_CURSOR_1, (long) animatedMouseCursorProvider
                .provide(MS_DURATION - PERIOD_MODULO_OFFSET + MS_2 - 1));
        assertEquals(MOUSE_CURSOR_2, (long) animatedMouseCursorProvider
                .provide(MS_DURATION - PERIOD_MODULO_OFFSET + MS_2));
        assertEquals(MOUSE_CURSOR_2, (long) animatedMouseCursorProvider
                .provide(MS_DURATION - PERIOD_MODULO_OFFSET + MS_3 - 1));
        assertEquals(MOUSE_CURSOR_3, (long) animatedMouseCursorProvider
                .provide(MS_DURATION - PERIOD_MODULO_OFFSET + MS_3));
    }

    @Test
    public void testReportPauseAndUnpause() {
        var pauseTimestamp = 10000L;
        var unpauseTimestamp = 10001L;

        assertNull(animatedMouseCursorProvider.pausedTimestamp());

        animatedMouseCursorProvider.reportPause(pauseTimestamp);

        assertEquals(pauseTimestamp, (long) animatedMouseCursorProvider.pausedTimestamp());

        animatedMouseCursorProvider.reportUnpause(unpauseTimestamp);

        assertNull(animatedMouseCursorProvider.pausedTimestamp());
    }

    @Test
    public void testReportPauseWhilePausedOrViceVersa() {
        assertThrows(UnsupportedOperationException.class, () ->
                animatedMouseCursorProvider.reportUnpause(MOST_RECENT_TIMESTAMP));

        animatedMouseCursorProvider.reportPause(MOST_RECENT_TIMESTAMP);

        assertThrows(UnsupportedOperationException.class, () ->
                animatedMouseCursorProvider.reportPause(MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testProvideWhenPaused() {
        animatedMouseCursorProvider.reportPause(MS_2);

        assertEquals(MOUSE_CURSOR_2, (long) animatedMouseCursorProvider
                .provide(MS_DURATION - PERIOD_MODULO_OFFSET + MS_3));
    }

    @Test
    public void testUnpauseUpdatesPeriodModuloOffset() {
        var pauseDuration = 123123L;

        animatedMouseCursorProvider.reportPause(MOST_RECENT_TIMESTAMP);
        animatedMouseCursorProvider.reportUnpause(MOST_RECENT_TIMESTAMP + pauseDuration);

        assertEquals(MOUSE_CURSOR_1, (long) animatedMouseCursorProvider
                .provide(MS_DURATION - PERIOD_MODULO_OFFSET + MS_2 - 1 + pauseDuration));
        assertEquals(MOUSE_CURSOR_2, (long) animatedMouseCursorProvider
                .provide(MS_DURATION - PERIOD_MODULO_OFFSET + MS_2 + pauseDuration));
    }

    @Test
    public void testReset() {
        var resetTimestamp = 123123L;

        animatedMouseCursorProvider.reset(resetTimestamp);

        assertEquals(MOUSE_CURSOR_1,
                (long) animatedMouseCursorProvider.provide(resetTimestamp + MS_2 - 1));
        assertEquals(MOUSE_CURSOR_2,
                (long) animatedMouseCursorProvider.provide(resetTimestamp + MS_2));
    }

    @Test
    public void testRepresentation() {
        assertEquals(CURSORS_AT_MS, animatedMouseCursorProvider.representation());
        assertNotSame(CURSORS_AT_MS, animatedMouseCursorProvider.representation());
    }
}
