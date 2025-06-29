package inaugural.soliloquy.io.test.unit.graphics.renderables.providers;

import inaugural.soliloquy.io.graphics.renderables.providers.RectangleAnimatedBackgroundTextureIdProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.io.graphics.renderables.providers.LoopingLinearMovingProvider;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
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
    private final Map<Integer, Integer> VALUES_WITHIN_PERIOD = mapOf();

    private final UUID UUID = java.util.UUID.randomUUID();

    private LoopingLinearMovingProvider<Integer> rectangleAnimatedBackgroundTextureIdProvider;

    @BeforeEach
    public void setUp() {
        VALUES_WITHIN_PERIOD.put(MS_1, MS_1_VALUE);
        VALUES_WITHIN_PERIOD.put(MS_2, MS_2_VALUE);
        VALUES_WITHIN_PERIOD.put(MS_3, MS_3_VALUE);
        VALUES_WITHIN_PERIOD.put(MS_4, MS_4_VALUE);
        VALUES_WITHIN_PERIOD.put(MS_5, MS_5_VALUE);
        VALUES_WITHIN_PERIOD.put(MS_6, MS_6_VALUE);
        VALUES_WITHIN_PERIOD.put(MS_7, MS_7_VALUE);
        VALUES_WITHIN_PERIOD.put(MS_8, MS_8_VALUE);
        VALUES_WITHIN_PERIOD.put(MS_9, MS_9_VALUE);

        rectangleAnimatedBackgroundTextureIdProvider =
                new RectangleAnimatedBackgroundTextureIdProvider(UUID, PERIOD_DURATION,
                        PERIOD_MODULO_OFFSET, VALUES_WITHIN_PERIOD, null,
                        MOST_RECENT_TIMESTAMP);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
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
    public void testUuid() {
        assertSame(UUID, rectangleAnimatedBackgroundTextureIdProvider.uuid());
    }

    @Test
    public void testValuesWithinPeriod() {
        Map<Integer, Integer> valuesWithinPeriod =
                rectangleAnimatedBackgroundTextureIdProvider.valuesWithinPeriod();

        assertNotNull(valuesWithinPeriod);
        assertNotSame(VALUES_WITHIN_PERIOD, valuesWithinPeriod);
        assertEquals(VALUES_WITHIN_PERIOD.size(), valuesWithinPeriod.size());
        VALUES_WITHIN_PERIOD.forEach((k, v) -> assertEquals(v, valuesWithinPeriod.get(k)));
    }

    @Test
    public void testPeriodDuration() {
        assertEquals(PERIOD_DURATION,
                rectangleAnimatedBackgroundTextureIdProvider.periodDuration());
    }

    @Test
    public void testPeriodModuloOffset() {
        assertEquals(PERIOD_MODULO_OFFSET,
                rectangleAnimatedBackgroundTextureIdProvider.periodModuloOffset());
    }

    @Test
    public void testReset() {
        long resetTimestamp = 123123L;

        rectangleAnimatedBackgroundTextureIdProvider.reset(resetTimestamp);

        assertEquals(PERIOD_DURATION - (resetTimestamp % PERIOD_DURATION),
                rectangleAnimatedBackgroundTextureIdProvider.periodModuloOffset());
    }

    @Test
    public void testReportPauseOrProvideWithOutdatedTimestamp() {
        long timestamp = 123123L;

        rectangleAnimatedBackgroundTextureIdProvider.provide(timestamp);

        assertThrows(IllegalArgumentException.class, () ->
                rectangleAnimatedBackgroundTextureIdProvider.provide(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleAnimatedBackgroundTextureIdProvider.reportPause(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleAnimatedBackgroundTextureIdProvider.reportUnpause(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleAnimatedBackgroundTextureIdProvider.reset(timestamp - 1));

        rectangleAnimatedBackgroundTextureIdProvider.reportPause(timestamp + 1);

        assertThrows(IllegalArgumentException.class, () ->
                rectangleAnimatedBackgroundTextureIdProvider.provide(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleAnimatedBackgroundTextureIdProvider.reportUnpause(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleAnimatedBackgroundTextureIdProvider.reset(timestamp));

        rectangleAnimatedBackgroundTextureIdProvider.reportUnpause(timestamp + 2);

        assertThrows(IllegalArgumentException.class, () ->
                rectangleAnimatedBackgroundTextureIdProvider.provide(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleAnimatedBackgroundTextureIdProvider.reportPause(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleAnimatedBackgroundTextureIdProvider.reset(timestamp + 1));

        rectangleAnimatedBackgroundTextureIdProvider.provide(timestamp + 3);

        assertThrows(IllegalArgumentException.class, () ->
                rectangleAnimatedBackgroundTextureIdProvider.provide(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleAnimatedBackgroundTextureIdProvider.reportPause(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleAnimatedBackgroundTextureIdProvider.reportUnpause(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleAnimatedBackgroundTextureIdProvider.reset(timestamp + 2));
    }

    @Test
    public void testProvide() {
        long timestamp1 = MS_3 - PERIOD_MODULO_OFFSET;
        long timestamp2 = timestamp1 - 1;

        int providedValue2 = rectangleAnimatedBackgroundTextureIdProvider.provide(timestamp2);
        int providedValue1 = rectangleAnimatedBackgroundTextureIdProvider.provide(timestamp1);

        assertEquals(MS_3_VALUE, providedValue1);
        assertEquals(MS_2_VALUE, providedValue2);
    }

    @Test
    public void testProvideWhenPaused() {
        long pauseTimestamp = MS_3 - PERIOD_MODULO_OFFSET;
        long provideTimestamp = pauseTimestamp + 123456L;

        rectangleAnimatedBackgroundTextureIdProvider.reportPause(pauseTimestamp);

        int providedValue =
                rectangleAnimatedBackgroundTextureIdProvider.provide(provideTimestamp);

        assertEquals(MS_3_VALUE, providedValue);
    }

    @Test
    public void testProvideWhenResumed() {
        long pauseTimestamp = MS_3 - PERIOD_MODULO_OFFSET - 1;
        long unpauseTimestamp = pauseTimestamp + 123456L;

        rectangleAnimatedBackgroundTextureIdProvider.reportPause(pauseTimestamp);
        rectangleAnimatedBackgroundTextureIdProvider.reportUnpause(unpauseTimestamp);

        int providedValue1 =
                rectangleAnimatedBackgroundTextureIdProvider.provide(unpauseTimestamp);
        int providedValue2 =
                rectangleAnimatedBackgroundTextureIdProvider.provide(unpauseTimestamp + 1);

        assertEquals(MS_2_VALUE, providedValue1);
        assertEquals(MS_3_VALUE, providedValue2);
    }

    @Test
    public void testReportPauseWhilePausedOrViceVersa() {
        assertThrows(UnsupportedOperationException.class, () ->
                rectangleAnimatedBackgroundTextureIdProvider.reportUnpause(MOST_RECENT_TIMESTAMP));

        rectangleAnimatedBackgroundTextureIdProvider.reportPause(MOST_RECENT_TIMESTAMP);

        assertThrows(UnsupportedOperationException.class, () ->
                rectangleAnimatedBackgroundTextureIdProvider.reportPause(MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testMostRecentTimestamp() {
        assertEquals(MOST_RECENT_TIMESTAMP,
                (long) rectangleAnimatedBackgroundTextureIdProvider.mostRecentTimestamp());
    }

    @Test
    public void testPauseTimestamp() {
        rectangleAnimatedBackgroundTextureIdProvider.reportPause(MOST_RECENT_TIMESTAMP + 1);

        assertEquals(MOST_RECENT_TIMESTAMP + 1,
                (long) rectangleAnimatedBackgroundTextureIdProvider.pausedTimestamp());
    }

    @Test
    public void testRepresentation() {
        assertEquals(VALUES_WITHIN_PERIOD,
                rectangleAnimatedBackgroundTextureIdProvider.representation());
        assertNotSame(VALUES_WITHIN_PERIOD,
                rectangleAnimatedBackgroundTextureIdProvider.representation());
    }
}
