package inaugural.soliloquy.graphics.test.unit.renderables.providers;

import inaugural.soliloquy.graphics.renderables.providers.AnimatedMouseCursorProviderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.providers.AnimatedMouseCursorProvider;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class AnimatedMouseCursorProviderImplTests {
    private final int MS_1 = 0;
    private final long MOUSE_CURSOR_1 = 123L;
    private final int MS_2 = 111;
    private final long MOUSE_CURSOR_2 = 456L;
    private final int MS_3 = 444;
    private final long MOUSE_CURSOR_3 = 789L;

    private final HashMap<Integer, Long> CURSORS_AT_MS = new HashMap<>();

    private final String ID = "id";
    private final int MS_DURATION = 777;
    private final int PERIOD_MODULO_OFFSET = 321;
    private final long MOST_RECENT_TIMESTAMP = 12L;

    private AnimatedMouseCursorProvider _animatedMouseCursorProvider;

    @BeforeEach
    void setUp() {
        CURSORS_AT_MS.put(MS_1, MOUSE_CURSOR_1);
        CURSORS_AT_MS.put(MS_2, MOUSE_CURSOR_2);
        CURSORS_AT_MS.put(MS_3, MOUSE_CURSOR_3);

        _animatedMouseCursorProvider = new AnimatedMouseCursorProviderImpl(ID, CURSORS_AT_MS,
                MS_DURATION, PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> new AnimatedMouseCursorProviderImpl(null,
                CURSORS_AT_MS, MS_DURATION, PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> new AnimatedMouseCursorProviderImpl("",
                CURSORS_AT_MS, MS_DURATION, PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class, () -> new AnimatedMouseCursorProviderImpl(ID,
                null, MS_DURATION, PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> new AnimatedMouseCursorProviderImpl(ID,
                new HashMap<>(), MS_DURATION, PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> new AnimatedMouseCursorProviderImpl(ID,
                new HashMap<Integer, Long>() {{
                    put(null, MOUSE_CURSOR_1);
                }}, MS_DURATION, PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> new AnimatedMouseCursorProviderImpl(ID,
                new HashMap<Integer, Long>() {{
                    put(-1, MOUSE_CURSOR_1);
                }}, MS_DURATION, PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> new AnimatedMouseCursorProviderImpl(ID,
                new HashMap<Integer, Long>() {{
                    put(MS_1, null);
                }}, MS_DURATION, PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> new AnimatedMouseCursorProviderImpl(ID,
                new HashMap<Integer, Long>() {{
                    put(1, MOUSE_CURSOR_1);
                }}, MS_DURATION, PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class, () -> new AnimatedMouseCursorProviderImpl(ID,
                CURSORS_AT_MS, 0, PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> new AnimatedMouseCursorProviderImpl(ID,
                CURSORS_AT_MS, MS_3, PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class, () -> new AnimatedMouseCursorProviderImpl(ID,
                CURSORS_AT_MS, MS_DURATION, MS_DURATION, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> new AnimatedMouseCursorProviderImpl(ID,
                CURSORS_AT_MS, MS_DURATION, -1, null, MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class, () -> new AnimatedMouseCursorProviderImpl(ID,
                CURSORS_AT_MS, MS_DURATION, PERIOD_MODULO_OFFSET, MOST_RECENT_TIMESTAMP + 1,
                MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> new AnimatedMouseCursorProviderImpl(ID,
                CURSORS_AT_MS, MS_DURATION, PERIOD_MODULO_OFFSET, MOST_RECENT_TIMESTAMP, null));
    }

    @Test
    void testUuid() {
        assertThrows(UnsupportedOperationException.class, _animatedMouseCursorProvider::uuid);
    }

    @Test
    void testId() {
        assertEquals(ID, _animatedMouseCursorProvider.id());
    }

    @Test
    void testMostRecentTimestamp() {
        assertEquals(MOST_RECENT_TIMESTAMP,
                (long)_animatedMouseCursorProvider.mostRecentTimestamp());
    }

    @Test
    void testPausedTimestamp() {
        long pausedTimestamp = -234L;

        ProviderAtTime<Long> animatedMouseCursorProvider =
                new AnimatedMouseCursorProviderImpl(ID, CURSORS_AT_MS, MS_DURATION,
                        PERIOD_MODULO_OFFSET, pausedTimestamp, MOST_RECENT_TIMESTAMP);

        assertEquals(pausedTimestamp, (long)animatedMouseCursorProvider.pausedTimestamp());
    }

    @Test
    void testProvide() {
        assertEquals(MOUSE_CURSOR_3, (long)_animatedMouseCursorProvider
                .provide(MS_DURATION - PERIOD_MODULO_OFFSET - 1));
        assertEquals(MOUSE_CURSOR_1, (long)_animatedMouseCursorProvider
                .provide(MS_DURATION - PERIOD_MODULO_OFFSET));
        assertEquals(MOUSE_CURSOR_1, (long)_animatedMouseCursorProvider
                .provide(MS_DURATION - PERIOD_MODULO_OFFSET + MS_2 - 1));
        assertEquals(MOUSE_CURSOR_2, (long)_animatedMouseCursorProvider
                .provide(MS_DURATION - PERIOD_MODULO_OFFSET + MS_2));
        assertEquals(MOUSE_CURSOR_2, (long)_animatedMouseCursorProvider
                .provide(MS_DURATION - PERIOD_MODULO_OFFSET + MS_3 - 1));
        assertEquals(MOUSE_CURSOR_3, (long)_animatedMouseCursorProvider
                .provide(MS_DURATION - PERIOD_MODULO_OFFSET + MS_3));
    }

    @Test
    void testReportPauseAndUnpause() {
        long pauseTimestamp = 10000L;
        long unpauseTimestamp = 10001L;

        assertNull(_animatedMouseCursorProvider.pausedTimestamp());

        _animatedMouseCursorProvider.reportPause(pauseTimestamp);

        assertEquals(pauseTimestamp, (long) _animatedMouseCursorProvider.pausedTimestamp());

        _animatedMouseCursorProvider.reportUnpause(unpauseTimestamp);

        assertNull(_animatedMouseCursorProvider.pausedTimestamp());
    }

    @Test
    void testReportPauseAndUnpauseWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> _animatedMouseCursorProvider.reportUnpause(MOST_RECENT_TIMESTAMP));

        _animatedMouseCursorProvider.reportPause(MOST_RECENT_TIMESTAMP);

        assertThrows(IllegalArgumentException.class,
                () -> _animatedMouseCursorProvider.reportUnpause(MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class,
                () -> _animatedMouseCursorProvider.reportPause(MOST_RECENT_TIMESTAMP));

        _animatedMouseCursorProvider.reportUnpause(MOST_RECENT_TIMESTAMP);


        assertThrows(IllegalArgumentException.class,
                () -> _animatedMouseCursorProvider.reportPause(MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class,
                () -> _animatedMouseCursorProvider.reportUnpause(MOST_RECENT_TIMESTAMP));
    }

    @Test
    void testProvideWhenPaused() {
        _animatedMouseCursorProvider.reportPause(MS_2);

        assertEquals(MOUSE_CURSOR_2, (long)_animatedMouseCursorProvider
                .provide(MS_DURATION - PERIOD_MODULO_OFFSET + MS_3));
    }

    @Test
    void testUnpauseUpdatesPeriodModuloOffset() {
        long pauseDuration = 123123L;

        _animatedMouseCursorProvider.reportPause(MOST_RECENT_TIMESTAMP);
        _animatedMouseCursorProvider.reportUnpause(MOST_RECENT_TIMESTAMP + pauseDuration);

        assertEquals(MOUSE_CURSOR_1, (long)_animatedMouseCursorProvider
                .provide(MS_DURATION - PERIOD_MODULO_OFFSET + MS_2 - 1 + pauseDuration));
        assertEquals(MOUSE_CURSOR_2, (long)_animatedMouseCursorProvider
                .provide(MS_DURATION - PERIOD_MODULO_OFFSET + MS_2 + pauseDuration));
    }

    @Test
    void testReset() {
        long resetTimestamp = 123123L;

        _animatedMouseCursorProvider.reset(resetTimestamp);

        assertEquals(MOUSE_CURSOR_1,
                (long)_animatedMouseCursorProvider.provide(resetTimestamp + MS_2 - 1));
        assertEquals(MOUSE_CURSOR_2,
                (long)_animatedMouseCursorProvider.provide(resetTimestamp + MS_2));
    }

    @Test
    void testReportPauseOrProvideWithOutdatedTimestamp() {
        long timestamp = 123123L;

        _animatedMouseCursorProvider.provide(timestamp);

        assertThrows(IllegalArgumentException.class, () ->
                _animatedMouseCursorProvider.provide(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _animatedMouseCursorProvider.reportPause(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _animatedMouseCursorProvider.reportUnpause(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _animatedMouseCursorProvider.reset(timestamp - 1));

        _animatedMouseCursorProvider.reportPause(timestamp + 1);

        assertThrows(IllegalArgumentException.class, () ->
                _animatedMouseCursorProvider.provide(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _animatedMouseCursorProvider.reportUnpause(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _animatedMouseCursorProvider.reset(timestamp));

        _animatedMouseCursorProvider.reportUnpause(timestamp + 2);

        assertThrows(IllegalArgumentException.class, () ->
                _animatedMouseCursorProvider.provide(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _animatedMouseCursorProvider.reportPause(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _animatedMouseCursorProvider.reset(timestamp + 1));

        _animatedMouseCursorProvider.provide(timestamp + 3);

        assertThrows(IllegalArgumentException.class, () ->
                _animatedMouseCursorProvider.provide(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _animatedMouseCursorProvider.reportPause(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _animatedMouseCursorProvider.reportUnpause(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _animatedMouseCursorProvider.reset(timestamp + 2));
    }

    @Test
    void testGetArchetype() {
        assertNotNull(_animatedMouseCursorProvider.getArchetype());
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(ProviderAtTime.class.getCanonicalName() + "<" +
                long.class.getCanonicalName() + ">",
                _animatedMouseCursorProvider.getInterfaceName());
    }
}
