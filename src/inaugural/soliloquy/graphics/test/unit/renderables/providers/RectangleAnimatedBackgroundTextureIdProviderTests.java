package inaugural.soliloquy.graphics.test.unit.renderables.providers;

import inaugural.soliloquy.graphics.renderables.providers.RectangleAnimatedBackgroundTextureIdProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.providers.LoopingLinearMovingProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/** @noinspection FieldCanBeLocal */
public class RectangleAnimatedBackgroundTextureIdProviderTests {
    private final int PERIOD_DURATION = 4000;
    private final int PERIOD_MODULO_OFFSET = 123;
    private final long PAUSED_TIMESTAMP = 45L;
    private final long MOST_RECENT_TIMESTAMP = 67L;

    private final int MS_1 = 0;
    private final int MS_1_VALUE = 111;
    private final int MS_2 = 100;
    private final int MS_2_VALUE = 222;
    private final int MS_3 = 300;
    private final int MS_3_VALUE = 333;
    private final int MS_4 = 600;
    private final int MS_4_VALUE = 444;
    private final int MS_5 = 1000;
    private final int MS_5_VALUE = 555;
    private final int MS_6 = 1500;
    private final int MS_6_VALUE = 666;
    private final int MS_7 = 2100;
    private final int MS_7_VALUE = 777;
    private final int MS_8 = 2800;
    private final int MS_8_VALUE = 888;
    private final int MS_9 = 3600;
    private final int MS_9_VALUE = 999;
    private final HashMap<Integer, Integer> VALUES_WITHIN_PERIOD = new HashMap<>();

    private final UUID UUID = java.util.UUID.randomUUID();

    private LoopingLinearMovingProvider<Integer> _rectangleAnimatedBackgroundTextureIdProvider;

    @BeforeEach
    void setUp() {
        VALUES_WITHIN_PERIOD.put(MS_1, MS_1_VALUE);
        VALUES_WITHIN_PERIOD.put(MS_2, MS_2_VALUE);
        VALUES_WITHIN_PERIOD.put(MS_3, MS_3_VALUE);
        VALUES_WITHIN_PERIOD.put(MS_4, MS_4_VALUE);
        VALUES_WITHIN_PERIOD.put(MS_5, MS_5_VALUE);
        VALUES_WITHIN_PERIOD.put(MS_6, MS_6_VALUE);
        VALUES_WITHIN_PERIOD.put(MS_7, MS_7_VALUE);
        VALUES_WITHIN_PERIOD.put(MS_8, MS_8_VALUE);
        VALUES_WITHIN_PERIOD.put(MS_9, MS_9_VALUE);

        _rectangleAnimatedBackgroundTextureIdProvider =
                new RectangleAnimatedBackgroundTextureIdProvider(UUID, PERIOD_DURATION,
                        PERIOD_MODULO_OFFSET, VALUES_WITHIN_PERIOD, null,
                        MOST_RECENT_TIMESTAMP);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleAnimatedBackgroundTextureIdProvider(null, PERIOD_DURATION,
                        PERIOD_MODULO_OFFSET, VALUES_WITHIN_PERIOD, PAUSED_TIMESTAMP,
                        MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class, () ->
                new RectangleAnimatedBackgroundTextureIdProvider(UUID, 0,
                        0, VALUES_WITHIN_PERIOD, PAUSED_TIMESTAMP,
                        MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class, () ->
                new RectangleAnimatedBackgroundTextureIdProvider(UUID, PERIOD_DURATION,
                        PERIOD_DURATION, VALUES_WITHIN_PERIOD, PAUSED_TIMESTAMP,
                        MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class, () ->
                new RectangleAnimatedBackgroundTextureIdProvider(UUID, PERIOD_DURATION,
                        -1, VALUES_WITHIN_PERIOD, PAUSED_TIMESTAMP,
                        MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class, () ->
                new RectangleAnimatedBackgroundTextureIdProvider(UUID, PERIOD_DURATION,
                        PERIOD_MODULO_OFFSET, null, PAUSED_TIMESTAMP,
                        MOST_RECENT_TIMESTAMP));

        // NB: Constructors being invoked here are simply to test whether no exception is thrown
        //     when no exception is expected
        new RectangleAnimatedBackgroundTextureIdProvider(UUID, PERIOD_DURATION,
                PERIOD_MODULO_OFFSET,
                VALUES_WITHIN_PERIOD, PAUSED_TIMESTAMP,
                MOST_RECENT_TIMESTAMP);

        VALUES_WITHIN_PERIOD.remove(MS_1);
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleAnimatedBackgroundTextureIdProvider(UUID, PERIOD_DURATION,
                        PERIOD_MODULO_OFFSET, VALUES_WITHIN_PERIOD, PAUSED_TIMESTAMP,
                        MOST_RECENT_TIMESTAMP));
        VALUES_WITHIN_PERIOD.put(MS_1, MS_1_VALUE);

        new RectangleAnimatedBackgroundTextureIdProvider(UUID, PERIOD_DURATION,
                PERIOD_MODULO_OFFSET,
                VALUES_WITHIN_PERIOD, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP);

        VALUES_WITHIN_PERIOD.put(PERIOD_DURATION, 123);
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleAnimatedBackgroundTextureIdProvider(UUID, PERIOD_DURATION,
                        PERIOD_MODULO_OFFSET, VALUES_WITHIN_PERIOD, PAUSED_TIMESTAMP,
                        MOST_RECENT_TIMESTAMP));
        VALUES_WITHIN_PERIOD.remove(PERIOD_DURATION);

        new RectangleAnimatedBackgroundTextureIdProvider(UUID, PERIOD_DURATION,
                PERIOD_MODULO_OFFSET,
                VALUES_WITHIN_PERIOD, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP);

        VALUES_WITHIN_PERIOD.put(-1, 123);
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleAnimatedBackgroundTextureIdProvider(UUID, PERIOD_DURATION,
                        PERIOD_MODULO_OFFSET, VALUES_WITHIN_PERIOD, PAUSED_TIMESTAMP,
                        MOST_RECENT_TIMESTAMP));
        VALUES_WITHIN_PERIOD.remove(-1);

        new RectangleAnimatedBackgroundTextureIdProvider(UUID, PERIOD_DURATION,
                PERIOD_MODULO_OFFSET,
                VALUES_WITHIN_PERIOD, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP);

        assertThrows(IllegalArgumentException.class, () ->
                new RectangleAnimatedBackgroundTextureIdProvider(UUID, PERIOD_DURATION,
                        PERIOD_MODULO_OFFSET, VALUES_WITHIN_PERIOD, PAUSED_TIMESTAMP,
                        null));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleAnimatedBackgroundTextureIdProvider(UUID, PERIOD_DURATION,
                        PERIOD_MODULO_OFFSET, VALUES_WITHIN_PERIOD, PAUSED_TIMESTAMP,
                        PAUSED_TIMESTAMP - 1));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(LoopingLinearMovingProvider.class.getCanonicalName() + "<" +
                        Integer.class.getCanonicalName() + ">",
                _rectangleAnimatedBackgroundTextureIdProvider.getInterfaceName());
    }

    @Test
    void testUuid() {
        assertSame(UUID, _rectangleAnimatedBackgroundTextureIdProvider.uuid());
    }

    @Test
    void testValuesWithinPeriod() {
        Map<Integer, Integer> valuesWithinPeriod =
                _rectangleAnimatedBackgroundTextureIdProvider.valuesWithinPeriod();

        assertNotNull(valuesWithinPeriod);
        assertNotSame(VALUES_WITHIN_PERIOD, valuesWithinPeriod);
        assertEquals(VALUES_WITHIN_PERIOD.size(), valuesWithinPeriod.size());
        VALUES_WITHIN_PERIOD.forEach((k, v) -> assertEquals(v, valuesWithinPeriod.get(k)));
    }

    @Test
    void testPeriodDuration() {
        assertEquals(PERIOD_DURATION,
                _rectangleAnimatedBackgroundTextureIdProvider.periodDuration());
    }

    @Test
    void testPeriodModuloOffset() {
        assertEquals(PERIOD_MODULO_OFFSET,
                _rectangleAnimatedBackgroundTextureIdProvider.periodModuloOffset());
    }

    @Test
    void testReset() {
        long resetTimestamp = 123123L;

        _rectangleAnimatedBackgroundTextureIdProvider.reset(resetTimestamp);

        assertEquals(PERIOD_DURATION - (resetTimestamp % PERIOD_DURATION),
                _rectangleAnimatedBackgroundTextureIdProvider.periodModuloOffset());
    }

    @Test
    void testReportPauseOrProvideWithOutdatedTimestamp() {
        long timestamp = 123123L;

        _rectangleAnimatedBackgroundTextureIdProvider.provide(timestamp);

        assertThrows(IllegalArgumentException.class, () ->
                _rectangleAnimatedBackgroundTextureIdProvider.provide(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleAnimatedBackgroundTextureIdProvider.reportPause(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleAnimatedBackgroundTextureIdProvider.reportUnpause(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleAnimatedBackgroundTextureIdProvider.reset(timestamp - 1));

        _rectangleAnimatedBackgroundTextureIdProvider.reportPause(timestamp + 1);

        assertThrows(IllegalArgumentException.class, () ->
                _rectangleAnimatedBackgroundTextureIdProvider.provide(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleAnimatedBackgroundTextureIdProvider.reportUnpause(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleAnimatedBackgroundTextureIdProvider.reset(timestamp));

        _rectangleAnimatedBackgroundTextureIdProvider.reportUnpause(timestamp + 2);

        assertThrows(IllegalArgumentException.class, () ->
                _rectangleAnimatedBackgroundTextureIdProvider.provide(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleAnimatedBackgroundTextureIdProvider.reportPause(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleAnimatedBackgroundTextureIdProvider.reset(timestamp + 1));

        _rectangleAnimatedBackgroundTextureIdProvider.provide(timestamp + 3);

        assertThrows(IllegalArgumentException.class, () ->
                _rectangleAnimatedBackgroundTextureIdProvider.provide(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleAnimatedBackgroundTextureIdProvider.reportPause(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleAnimatedBackgroundTextureIdProvider.reportUnpause(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleAnimatedBackgroundTextureIdProvider.reset(timestamp + 2));
    }

    @Test
    void testProvide() {
        long timestamp1 = MS_3 - PERIOD_MODULO_OFFSET;
        long timestamp2 = timestamp1 - 1;

        int providedValue2 = _rectangleAnimatedBackgroundTextureIdProvider.provide(timestamp2);
        int providedValue1 = _rectangleAnimatedBackgroundTextureIdProvider.provide(timestamp1);

        assertEquals(MS_3_VALUE, providedValue1);
        assertEquals(MS_2_VALUE, providedValue2);
    }

    @Test
    void testProvideWhenPaused() {
        long pauseTimestamp = MS_3 - PERIOD_MODULO_OFFSET;
        long provideTimestamp = pauseTimestamp + 123456L;

        _rectangleAnimatedBackgroundTextureIdProvider.reportPause(pauseTimestamp);

        int providedValue =
                _rectangleAnimatedBackgroundTextureIdProvider.provide(provideTimestamp);

        assertEquals(MS_3_VALUE, providedValue);
    }

    @Test
    void testProvideWhenResumed() {
        long pauseTimestamp = MS_3 - PERIOD_MODULO_OFFSET - 1;
        long unpauseTimestamp = pauseTimestamp + 123456L;

        _rectangleAnimatedBackgroundTextureIdProvider.reportPause(pauseTimestamp);
        _rectangleAnimatedBackgroundTextureIdProvider.reportUnpause(unpauseTimestamp);

        int providedValue1 =
                _rectangleAnimatedBackgroundTextureIdProvider.provide(unpauseTimestamp);
        int providedValue2 =
                _rectangleAnimatedBackgroundTextureIdProvider.provide(unpauseTimestamp + 1);

        assertEquals(MS_2_VALUE, providedValue1);
        assertEquals(MS_3_VALUE, providedValue2);
    }

    @Test
    void testReportPauseAndUnpauseWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> _rectangleAnimatedBackgroundTextureIdProvider
                        .reportUnpause(MOST_RECENT_TIMESTAMP));

        _rectangleAnimatedBackgroundTextureIdProvider.reportPause(MOST_RECENT_TIMESTAMP);

        assertThrows(IllegalArgumentException.class,
                () -> _rectangleAnimatedBackgroundTextureIdProvider
                        .reportUnpause(MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class,
                () -> _rectangleAnimatedBackgroundTextureIdProvider
                        .reportPause(MOST_RECENT_TIMESTAMP));

        _rectangleAnimatedBackgroundTextureIdProvider.reportUnpause(MOST_RECENT_TIMESTAMP);


        assertThrows(IllegalArgumentException.class,
                () -> _rectangleAnimatedBackgroundTextureIdProvider
                        .reportPause(MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class,
                () -> _rectangleAnimatedBackgroundTextureIdProvider
                        .reportUnpause(MOST_RECENT_TIMESTAMP));
    }

    @Test
    void testMostRecentTimestamp() {
        assertEquals(MOST_RECENT_TIMESTAMP,
                (long) _rectangleAnimatedBackgroundTextureIdProvider.mostRecentTimestamp());
    }

    @Test
    void testPauseTimestamp() {
        _rectangleAnimatedBackgroundTextureIdProvider.reportPause(MOST_RECENT_TIMESTAMP + 1);

        assertEquals(MOST_RECENT_TIMESTAMP + 1,
                (long) _rectangleAnimatedBackgroundTextureIdProvider.pausedTimestamp());
    }

    @Test
    void testGetArchetype() {
        assertEquals(0,
                (int) _rectangleAnimatedBackgroundTextureIdProvider.getArchetype());
    }
}
