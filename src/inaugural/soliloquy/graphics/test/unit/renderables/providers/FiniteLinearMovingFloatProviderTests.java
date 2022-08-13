package inaugural.soliloquy.graphics.test.unit.renderables.providers;

import inaugural.soliloquy.graphics.renderables.providers.FiniteLinearMovingFloatProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.providers.FiniteLinearMovingProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FiniteLinearMovingFloatProviderTests {
    private final HashMap<Long, Float> VALUES_AT_TIMES = new HashMap<>();
    private final long TIME_1 = 100L;
    private final float VALUE_1 = 0.2f;
    private final long TIME_2 = 300L;
    private final float VALUE_2 = 0.4f;
    private final long TIME_3 = 500L;
    private final float VALUE_3 = 0.6f;
    private final long MOST_RECENT_TIMESTAMP = 34L;

    private final UUID UUID = java.util.UUID.randomUUID();

    private FiniteLinearMovingProvider<Float> _finiteLinearMovingFloatProvider;

    @BeforeEach
    void setUp() {
        VALUES_AT_TIMES.put(TIME_1, VALUE_1);
        VALUES_AT_TIMES.put(TIME_2, VALUE_2);
        VALUES_AT_TIMES.put(TIME_3, VALUE_3);

        _finiteLinearMovingFloatProvider = new FiniteLinearMovingFloatProvider(UUID,
                VALUES_AT_TIMES, null, MOST_RECENT_TIMESTAMP);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingFloatProvider(null, VALUES_AT_TIMES, null, null));

        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingFloatProvider(UUID, null, null, null));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingFloatProvider(UUID, new HashMap<>(), null, null));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingFloatProvider(UUID, new HashMap<Long, Float>() {{
                    put(null, VALUE_1);
                }}, null, null));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingFloatProvider(UUID, new HashMap<Long, Float>() {{
                    put(TIME_1, null);
                }}, null, null));

        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingFloatProvider(UUID, VALUES_AT_TIMES, 2L, null));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingFloatProvider(UUID, VALUES_AT_TIMES, 2L, 1L));
    }

    @Test
    void testUuid() {
        assertSame(UUID, _finiteLinearMovingFloatProvider.uuid());
    }

    @Test
    void testMostRecentTimestamp() {
        assertEquals(MOST_RECENT_TIMESTAMP,
                (long) _finiteLinearMovingFloatProvider.mostRecentTimestamp());
    }

    @Test
    void testValuesAtTimestampsRepresentation() {
        assertNotNull(_finiteLinearMovingFloatProvider.valuesAtTimestampsRepresentation());
        assertEquals(VALUES_AT_TIMES,
                _finiteLinearMovingFloatProvider.valuesAtTimestampsRepresentation());
        assertNotSame(VALUES_AT_TIMES,
                _finiteLinearMovingFloatProvider.valuesAtTimestampsRepresentation());
    }

    @Test
    void testGetArchetype() {
        assertNotNull(_finiteLinearMovingFloatProvider.getArchetype());
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(FiniteLinearMovingProvider.class.getCanonicalName() + "<" +
                        Float.class.getCanonicalName() + ">",
                _finiteLinearMovingFloatProvider.getInterfaceName());
    }

    @Test
    void testProvideAtExtremes() {
        assertEquals(VALUE_1, (float) _finiteLinearMovingFloatProvider.provide(TIME_1 - 1));
        assertEquals(VALUE_1, (float) _finiteLinearMovingFloatProvider.provide(TIME_1));
        assertEquals(VALUE_3, (float) _finiteLinearMovingFloatProvider.provide(TIME_3));
        assertEquals(VALUE_3, (float) _finiteLinearMovingFloatProvider.provide(TIME_3 + 1));
    }

    @Test
    void testProvideInterpolatedValue() {
        long timeAfterTime1 = 50;
        long timestamp = TIME_1 + timeAfterTime1;
        long distanceBetweenTimes = TIME_2 - TIME_1;
        float time2Weight = timeAfterTime1 / (float) distanceBetweenTimes;
        float time1Weight = 1f - time2Weight;
        float weightedValue1 = VALUE_1 * time1Weight;
        float weightedValue2 = VALUE_2 * time2Weight;

        assertEquals(weightedValue1 + weightedValue2,
                (float) _finiteLinearMovingFloatProvider.provide(timestamp));
    }

    @Test
    void testPausedTimestamp() {
        long pausedTimestamp = 12L;
        FiniteLinearMovingProvider<Float> pausedFiniteLinearMovingFloatProvider =
                new FiniteLinearMovingFloatProvider(UUID, VALUES_AT_TIMES, pausedTimestamp,
                        MOST_RECENT_TIMESTAMP);

        assertEquals(pausedTimestamp,
                (long) pausedFiniteLinearMovingFloatProvider.pausedTimestamp());
    }

    @Test
    void testProvideWhenPaused() {
        _finiteLinearMovingFloatProvider.reportPause(TIME_1);

        assertEquals(VALUE_1, (float) _finiteLinearMovingFloatProvider.provide(123123123L));
    }

    @Test
    void testProvideOrReportPauseOrUnpauseWithInvalidTimestampAndMostRecentTimestamp() {
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingFloatProvider.provide(MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingFloatProvider.reportUnpause(MOST_RECENT_TIMESTAMP - 1));

        _finiteLinearMovingFloatProvider.provide(MOST_RECENT_TIMESTAMP + 1);

        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingFloatProvider.provide(MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingFloatProvider.reportUnpause(MOST_RECENT_TIMESTAMP));
        assertEquals(MOST_RECENT_TIMESTAMP + 1,
                (long) _finiteLinearMovingFloatProvider.mostRecentTimestamp());

        _finiteLinearMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP + 2);

        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingFloatProvider.provide(MOST_RECENT_TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingFloatProvider.reportUnpause(MOST_RECENT_TIMESTAMP + 1));
        assertEquals(MOST_RECENT_TIMESTAMP + 2,
                (long) _finiteLinearMovingFloatProvider.mostRecentTimestamp());
        assertEquals(MOST_RECENT_TIMESTAMP + 2,
                (long) _finiteLinearMovingFloatProvider.pausedTimestamp());

        _finiteLinearMovingFloatProvider.reportUnpause(MOST_RECENT_TIMESTAMP + 3);

        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingFloatProvider.provide(MOST_RECENT_TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingFloatProvider.reportUnpause(MOST_RECENT_TIMESTAMP + 2));
        assertEquals(MOST_RECENT_TIMESTAMP + 3,
                (long) _finiteLinearMovingFloatProvider.mostRecentTimestamp());
        assertNull(_finiteLinearMovingFloatProvider.pausedTimestamp());
    }

    @Test
    void testReportPauseWhilePausedOrViceVersa() {
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingFloatProvider.reportUnpause(MOST_RECENT_TIMESTAMP));

        _finiteLinearMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP);

        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP));
    }

    @Test
    void testReportPauseAndUnpauseUpdatesTimestampsForValues() {
        long pauseDuration = 123123L;
        long timeAfterTime1 = 50;
        long timestamp = TIME_1 + timeAfterTime1;
        long distanceBetweenTimes = TIME_2 - TIME_1;
        float time2Weight = timeAfterTime1 / (float) distanceBetweenTimes;
        float time1Weight = 1f - time2Weight;
        float weightedValue1 = VALUE_1 * time1Weight;
        float weightedValue2 = VALUE_2 * time2Weight;

        _finiteLinearMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP);
        _finiteLinearMovingFloatProvider.reportUnpause(MOST_RECENT_TIMESTAMP + pauseDuration);

        Map<Long, Float> valuesAtTimestampsRepresentation =
                _finiteLinearMovingFloatProvider.valuesAtTimestampsRepresentation();

        assertEquals(VALUE_1, (float) valuesAtTimestampsRepresentation.get(TIME_1 + pauseDuration));
        assertEquals(VALUE_2, (float) valuesAtTimestampsRepresentation.get(TIME_2 + pauseDuration));
        assertEquals(VALUE_3, (float) valuesAtTimestampsRepresentation.get(TIME_3 + pauseDuration));

        assertEquals(weightedValue1 + weightedValue2,
                (float) _finiteLinearMovingFloatProvider.provide(timestamp + pauseDuration));
    }
}
