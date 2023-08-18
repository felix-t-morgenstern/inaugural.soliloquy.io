package inaugural.soliloquy.graphics.test.unit.renderables.providers;

import inaugural.soliloquy.graphics.renderables.providers.LoopingLinearMovingFloatProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.providers.LoopingLinearMovingProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LoopingLinearMovingFloatProviderTests {
    private final HashMap<Integer, Float> VALUES_AT_TIMES = new HashMap<>();
    private final int TIME_1 = 0;
    private final float VALUE_1 = 0.2f;
    private final int TIME_2 = 100;
    private final float VALUE_2 = 0.4f;
    private final int TIME_3 = 300;
    private final float VALUE_3 = 0.6f;
    private final int PERIOD_DURATION = 600;
    private final int MODULO_OFFSET = 123;

    private final UUID UUID = java.util.UUID.randomUUID();

    private LoopingLinearMovingProvider<Float> loopingLinearMovingFloatProvider;

    @BeforeEach
    void setUp() {
        VALUES_AT_TIMES.put(TIME_1, VALUE_1);
        VALUES_AT_TIMES.put(TIME_2, VALUE_2);
        VALUES_AT_TIMES.put(TIME_3, VALUE_3);

        loopingLinearMovingFloatProvider = new LoopingLinearMovingFloatProvider(UUID,
                VALUES_AT_TIMES, PERIOD_DURATION, MODULO_OFFSET, null, null);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatProvider(null, VALUES_AT_TIMES, PERIOD_DURATION,
                        MODULO_OFFSET, null, null));

        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatProvider(UUID, null, PERIOD_DURATION,
                        MODULO_OFFSET, null, null));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatProvider(UUID, new HashMap<>(), PERIOD_DURATION,
                        MODULO_OFFSET, null, null));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatProvider(UUID, new HashMap<Integer, Float>() {{
                    put(null, VALUE_1);
                }}, PERIOD_DURATION,
                        MODULO_OFFSET, null, null));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatProvider(UUID, new HashMap<Integer, Float>() {{
                    put(TIME_1, null);
                }}, PERIOD_DURATION,
                        MODULO_OFFSET, null, null));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatProvider(UUID, new HashMap<Integer, Float>() {{
                    put(TIME_2, VALUE_2);
                }}, PERIOD_DURATION,
                        MODULO_OFFSET, null, null));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatProvider(UUID, new HashMap<Integer, Float>() {{
                    put(PERIOD_DURATION + 1, VALUE_1);
                }}, PERIOD_DURATION,
                        MODULO_OFFSET, null, null));

        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatProvider(UUID, VALUES_AT_TIMES, PERIOD_DURATION,
                        -1, null, null));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatProvider(UUID, VALUES_AT_TIMES, PERIOD_DURATION,
                        PERIOD_DURATION, null, null));

        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatProvider(UUID, VALUES_AT_TIMES, PERIOD_DURATION,
                        MODULO_OFFSET, 123123L, null));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatProvider(UUID, VALUES_AT_TIMES, PERIOD_DURATION,
                        MODULO_OFFSET, 1L, 0L));
    }

    @Test
    void testUuid() {
        assertEquals(UUID, loopingLinearMovingFloatProvider.uuid());
    }

    @Test
    void testMostRecentTimestampAndPausedTimestamp() {
        long pausedTimestamp = 123123L;
        long mostRecentTimestamp = 456456L;

        LoopingLinearMovingProvider<Float> loopingLinearMovingFloatProvider =
                new LoopingLinearMovingFloatProvider(UUID, VALUES_AT_TIMES, PERIOD_DURATION,
                        MODULO_OFFSET, pausedTimestamp, mostRecentTimestamp);

        assertEquals(pausedTimestamp,
                (long) loopingLinearMovingFloatProvider.pausedTimestamp());
        assertEquals(mostRecentTimestamp,
                (long) loopingLinearMovingFloatProvider.mostRecentTimestamp());
    }

    @Test
    void testArchetype() {
        assertNotNull(loopingLinearMovingFloatProvider.archetype());
    }

    @Test
    void testValuesWithinPeriod() {
        Map<Integer, Float> valuesWithinPeriod =
                loopingLinearMovingFloatProvider.valuesWithinPeriod();

        assertNotNull(valuesWithinPeriod);
        assertNotSame(loopingLinearMovingFloatProvider.valuesWithinPeriod(), valuesWithinPeriod);
        assertEquals(VALUES_AT_TIMES.size(), valuesWithinPeriod.size());
        VALUES_AT_TIMES.keySet().forEach(key ->
                assertEquals(VALUES_AT_TIMES.get(key), valuesWithinPeriod.get((int) (long) key)));
    }

    @Test
    void testPeriodDuration() {
        assertEquals(PERIOD_DURATION, loopingLinearMovingFloatProvider.periodDuration());
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(LoopingLinearMovingProvider.class.getCanonicalName() + "<" +
                        Float.class.getCanonicalName() + ">",
                loopingLinearMovingFloatProvider.getInterfaceName());
    }

    @Test
    void testProvideAtKey() {
        assertEquals(VALUE_1,
                (float) loopingLinearMovingFloatProvider.provide(TIME_1 - MODULO_OFFSET));
        assertEquals(VALUE_2,
                (float) loopingLinearMovingFloatProvider.provide(TIME_2 - MODULO_OFFSET));
        assertEquals(VALUE_3,
                (float) loopingLinearMovingFloatProvider.provide(TIME_3 - MODULO_OFFSET));
    }

    @Test
    void testProvideWithinProvidedValues() {
        long timeAfterValue1 = 50L;
        long timestamp = TIME_1 - MODULO_OFFSET + timeAfterValue1;
        long timeInterval = TIME_2 - TIME_1;
        float value1Weight = (timeInterval - timeAfterValue1) / (float) timeInterval;
        float value2Weight = 1f - value1Weight;

        float expected = (VALUE_1 * value1Weight) + (VALUE_2 * value2Weight);

        assertEquals(expected, (float) loopingLinearMovingFloatProvider.provide(timestamp));
    }

    @Test
    void testProvidePastProvidedValues() {
        long timeAfterValue3 = 50L;
        long timestamp = TIME_3 - MODULO_OFFSET + timeAfterValue3;
        long timeInterval = PERIOD_DURATION - TIME_3;
        float value3Weight = (timeInterval - timeAfterValue3) / (float) timeInterval;
        float value1Weight = 1f - value3Weight;

        float expected = (VALUE_3 * value3Weight) + (VALUE_1 * value1Weight);

        assertEquals(expected, (float) loopingLinearMovingFloatProvider.provide(timestamp));
    }

    @Test
    void testProvideWhenPaused() {
        loopingLinearMovingFloatProvider.reportPause(TIME_1 - MODULO_OFFSET);

        assertEquals(VALUE_1, (float) loopingLinearMovingFloatProvider.provide(123123123L));
    }

    @Test
    void testReset() {
        long resetTimestamp = 123123L;

        loopingLinearMovingFloatProvider.reset(resetTimestamp);

        assertEquals(VALUE_1, (float) loopingLinearMovingFloatProvider.provide(resetTimestamp));
    }

    @Test
    void testReportPauseOrProvideWithOutdatedTimestamp() {
        long timestamp = 123123L;

        loopingLinearMovingFloatProvider.provide(timestamp);

        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingFloatProvider.provide(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingFloatProvider.reportPause(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingFloatProvider.reportUnpause(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingFloatProvider.reset(timestamp - 1));

        loopingLinearMovingFloatProvider.reportPause(timestamp + 1);

        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingFloatProvider.provide(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingFloatProvider.reportUnpause(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingFloatProvider.reset(timestamp));

        loopingLinearMovingFloatProvider.reportUnpause(timestamp + 2);

        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingFloatProvider.provide(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingFloatProvider.reportPause(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingFloatProvider.reset(timestamp + 1));

        loopingLinearMovingFloatProvider.provide(timestamp + 3);

        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingFloatProvider.provide(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingFloatProvider.reportPause(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingFloatProvider.reportUnpause(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingFloatProvider.reset(timestamp + 2));
    }

    @Test
    void testReportPauseWhilePausedOrViceVersa() {
        assertThrows(UnsupportedOperationException.class, () ->
                loopingLinearMovingFloatProvider.reportUnpause(0L));

        loopingLinearMovingFloatProvider.reportPause(0L);

        assertThrows(UnsupportedOperationException.class, () ->
                loopingLinearMovingFloatProvider.reportPause(0L));
    }

    @Test
    void testReportPauseAndUnpauseUpdatesOffset() {
        long pauseDuration = 123123L;

        long timeAfterValue1 = 50L;
        long timestamp = TIME_1 - MODULO_OFFSET + PERIOD_DURATION + timeAfterValue1;
        long timeInterval = TIME_2 - TIME_1;
        float value1Weight = (timeInterval - timeAfterValue1) / (float) timeInterval;
        float value2Weight = 1f - value1Weight;

        float expected = (VALUE_1 * value1Weight) + (VALUE_2 * value2Weight);

        loopingLinearMovingFloatProvider.reportPause(0L);
        loopingLinearMovingFloatProvider.reportUnpause(pauseDuration);

        assertEquals(expected,
                (float) loopingLinearMovingFloatProvider.provide(timestamp + pauseDuration));
    }

    @Test
    void testRepresentation() {
        assertEquals(VALUES_AT_TIMES, loopingLinearMovingFloatProvider.representation());
        assertNotSame(VALUES_AT_TIMES, loopingLinearMovingFloatProvider.representation());
    }
}
