package inaugural.soliloquy.graphics.test.unit.renderables.providers;

import inaugural.soliloquy.graphics.renderables.providers.LoopingLinearMovingFloatProvider;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeEntityUuid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.providers.LoopingMovingProvider;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LoopingLinearMovingFloatProviderTests {
    private final FakeEntityUuid UUID = new FakeEntityUuid();
    private final HashMap<Integer, Float> VALUES_AT_TIMES = new HashMap<>();
    private final int TIME_1 = 0;
    private final float VALUE_1 = 0.2f;
    private final int TIME_2 = 100;
    private final float VALUE_2 = 0.4f;
    private final int TIME_3 = 300;
    private final float VALUE_3 = 0.6f;
    private final int PERIOD_DURATION = 600;
    private final int MODULO_OFFSET = 123;

    private LoopingMovingProvider<Float> _loopingLinearMovingFloatProvider;

    @BeforeEach
    void setUp() {
        VALUES_AT_TIMES.put(TIME_1, VALUE_1);
        VALUES_AT_TIMES.put(TIME_2, VALUE_2);
        VALUES_AT_TIMES.put(TIME_3, VALUE_3);

        _loopingLinearMovingFloatProvider = new LoopingLinearMovingFloatProvider(UUID,
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
        assertEquals(UUID, _loopingLinearMovingFloatProvider.uuid());
    }

    @Test
    void testMostRecentTimestampAndPausedTimestamp() {
        long pausedTimestamp = 123123L;
        long mostRecentTimestamp = 456456L;

        LoopingMovingProvider<Float> loopingLinearMovingFloatProvider =
                new LoopingLinearMovingFloatProvider(UUID, VALUES_AT_TIMES, PERIOD_DURATION,
                        MODULO_OFFSET, pausedTimestamp, mostRecentTimestamp);

        assertEquals(pausedTimestamp,
                (long)loopingLinearMovingFloatProvider.pausedTimestamp());
        assertEquals(mostRecentTimestamp,
                (long)loopingLinearMovingFloatProvider.mostRecentTimestamp());
    }

    @Test
    void testGetArchetype() {
        assertNotNull(_loopingLinearMovingFloatProvider.getArchetype());
    }

    @Test
    void testValuesWithinPeriod() {
        Map<Integer, Float> valuesWithinPeriod =
                _loopingLinearMovingFloatProvider.valuesWithinPeriod();

        assertNotNull(valuesWithinPeriod);
        assertNotSame(_loopingLinearMovingFloatProvider.valuesWithinPeriod(), valuesWithinPeriod);
        assertEquals(VALUES_AT_TIMES.size(), valuesWithinPeriod.size());
        VALUES_AT_TIMES.keySet().forEach(key ->
                assertEquals(VALUES_AT_TIMES.get(key), valuesWithinPeriod.get((int)(long)key)));
    }

    @Test
    void testPeriodDuration() {
        assertEquals(PERIOD_DURATION, _loopingLinearMovingFloatProvider.periodDuration());
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(LoopingMovingProvider.class.getCanonicalName() + "<" +
                Float.class.getCanonicalName() + ">",
                _loopingLinearMovingFloatProvider.getInterfaceName());
    }

    @Test
    void testProvideAtKey() {
        assertEquals(VALUE_1,
                (float)_loopingLinearMovingFloatProvider.provide(TIME_1 - MODULO_OFFSET));
        assertEquals(VALUE_2,
                (float)_loopingLinearMovingFloatProvider.provide(TIME_2 - MODULO_OFFSET));
        assertEquals(VALUE_3,
                (float)_loopingLinearMovingFloatProvider.provide(TIME_3 - MODULO_OFFSET));
    }

    @Test
    void testProvideWithinProvidedValues() {
        long timeAfterValue1 = 50L;
        long timestamp = TIME_1 - MODULO_OFFSET + timeAfterValue1;
        long timeInterval = TIME_2 - TIME_1;
        float value1Weight = (timeInterval - timeAfterValue1) / (float)timeInterval;
        float value2Weight = 1f - value1Weight;

        float expected = (VALUE_1 * value1Weight) + (VALUE_2 * value2Weight);

        assertEquals(expected, (float)_loopingLinearMovingFloatProvider.provide(timestamp));
    }

    @Test
    void testProvidePastProvidedValues() {
        long timeAfterValue3 = 50L;
        long timestamp = TIME_3 - MODULO_OFFSET + timeAfterValue3;
        long timeInterval = PERIOD_DURATION - TIME_3;
        float value3Weight = (timeInterval - timeAfterValue3) / (float)timeInterval;
        float value1Weight = 1f - value3Weight;

        float expected = (VALUE_3 * value3Weight) + (VALUE_1 * value1Weight);

        assertEquals(expected, (float)_loopingLinearMovingFloatProvider.provide(timestamp));
    }

    @Test
    void testProvideWhenPaused() {
        _loopingLinearMovingFloatProvider.reportPause(TIME_1 - MODULO_OFFSET);

        assertEquals(VALUE_1, (float)_loopingLinearMovingFloatProvider.provide(123123123L));
    }

    @Test
    void testReset() {
        long resetTimestamp = 123123L;

        _loopingLinearMovingFloatProvider.reset(resetTimestamp);

        assertEquals(VALUE_1, (float)_loopingLinearMovingFloatProvider.provide(resetTimestamp));
    }

    @Test
    void testReportPauseOrProvideWithOutdatedTimestamp() {
        long timestamp = 123123L;

        _loopingLinearMovingFloatProvider.provide(timestamp);

        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatProvider.provide(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatProvider.reportPause(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatProvider.reportUnpause(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatProvider.reset(timestamp - 1));

        _loopingLinearMovingFloatProvider.reportPause(timestamp + 1);

        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatProvider.provide(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatProvider.reportUnpause(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatProvider.reset(timestamp));

        _loopingLinearMovingFloatProvider.reportUnpause(timestamp + 2);

        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatProvider.provide(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatProvider.reportPause(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatProvider.reset(timestamp + 1));

        _loopingLinearMovingFloatProvider.provide(timestamp + 3);

        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatProvider.provide(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatProvider.reportPause(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatProvider.reportUnpause(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatProvider.reset(timestamp + 2));
    }

    @Test
    void testReportPauseWhilePausedOrViceVersa() {
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatProvider.reportUnpause(0L));

        _loopingLinearMovingFloatProvider.reportPause(0L);

        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatProvider.reportPause(0L));
    }

    @Test
    void testReportPauseAndUnpauseUpdatesOffset() {
        long pauseDuration = 123123L;

        long timeAfterValue1 = 50L;
        long timestamp = TIME_1 - MODULO_OFFSET + PERIOD_DURATION + timeAfterValue1;
        long timeInterval = TIME_2 - TIME_1;
        float value1Weight = (timeInterval - timeAfterValue1) / (float)timeInterval;
        float value2Weight = 1f - value1Weight;

        float expected = (VALUE_1 * value1Weight) + (VALUE_2 * value2Weight);

        _loopingLinearMovingFloatProvider.reportPause(0L);
        _loopingLinearMovingFloatProvider.reportUnpause(pauseDuration);

        assertEquals(expected,
                (float)_loopingLinearMovingFloatProvider.provide(timestamp + pauseDuration));
    }
}
