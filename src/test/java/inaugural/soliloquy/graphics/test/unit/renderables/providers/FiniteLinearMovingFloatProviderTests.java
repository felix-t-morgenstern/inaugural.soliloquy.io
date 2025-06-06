package inaugural.soliloquy.graphics.test.unit.renderables.providers;

import inaugural.soliloquy.graphics.renderables.providers.FiniteLinearMovingFloatProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.providers.FiniteLinearMovingProvider;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static org.junit.jupiter.api.Assertions.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

public class FiniteLinearMovingFloatProviderTests {
    private final long TIME_1 = 100L;
    private final float VALUE_1 = 0.2f;
    private final long TIME_2 = 300L;
    private final float VALUE_2 = 0.4f;
    private final long TIME_3 = 500L;
    private final float VALUE_3 = 0.6f;
    private final Map<Long, Float> VALUES_AT_TIMES = mapOf(
            pairOf(TIME_1, VALUE_1),
            pairOf(TIME_2, VALUE_2),
            pairOf(TIME_3, VALUE_3)
    );

    private final long MOST_RECENT_TIMESTAMP = 34L;

    private final UUID UUID = java.util.UUID.randomUUID();

    private FiniteLinearMovingProvider<Float> finiteLinearMovingFloatProvider;

    @BeforeEach
    public void setUp() {
        finiteLinearMovingFloatProvider = new FiniteLinearMovingFloatProvider(UUID,
                VALUES_AT_TIMES, null, MOST_RECENT_TIMESTAMP);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingFloatProvider(null, VALUES_AT_TIMES, null, null));

        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingFloatProvider(UUID, null, null, null));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingFloatProvider(UUID, mapOf(), null, null));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingFloatProvider(UUID, mapOf(pairOf(null, VALUE_1)), null,
                        null));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingFloatProvider(UUID, mapOf(pairOf(TIME_1, null)), null, null));

        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingFloatProvider(UUID, VALUES_AT_TIMES, 2L, null));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingFloatProvider(UUID, VALUES_AT_TIMES, 2L, 1L));
    }

    @Test
    public void testUuid() {
        assertSame(UUID, finiteLinearMovingFloatProvider.uuid());
    }

    @Test
    public void testMostRecentTimestamp() {
        assertEquals(MOST_RECENT_TIMESTAMP,
                (long) finiteLinearMovingFloatProvider.mostRecentTimestamp());
    }

    @Test
    public void testValuesAtTimestampsRepresentation() {
        assertNotNull(finiteLinearMovingFloatProvider.valuesAtTimestampsRepresentation());
        assertEquals(VALUES_AT_TIMES,
                finiteLinearMovingFloatProvider.valuesAtTimestampsRepresentation());
        assertNotSame(VALUES_AT_TIMES,
                finiteLinearMovingFloatProvider.valuesAtTimestampsRepresentation());
    }

    @Test
    public void testProvideAtExtremes() {
        assertEquals(VALUE_1, (float) finiteLinearMovingFloatProvider.provide(TIME_1 - 1));
        assertEquals(VALUE_1, (float) finiteLinearMovingFloatProvider.provide(TIME_1));
        assertEquals(VALUE_3, (float) finiteLinearMovingFloatProvider.provide(TIME_3));
        assertEquals(VALUE_3, (float) finiteLinearMovingFloatProvider.provide(TIME_3 + 1));
    }

    @Test
    public void testProvideInterpolatedValue() {
        long timeAfterTime1 = 50;
        long timestamp = TIME_1 + timeAfterTime1;
        long distanceBetweenTimes = TIME_2 - TIME_1;
        float time2Weight = timeAfterTime1 / (float) distanceBetweenTimes;
        float time1Weight = 1f - time2Weight;
        float weightedValue1 = VALUE_1 * time1Weight;
        float weightedValue2 = VALUE_2 * time2Weight;

        assertEquals(weightedValue1 + weightedValue2,
                (float) finiteLinearMovingFloatProvider.provide(timestamp));
    }

    @Test
    public void testPausedTimestamp() {
        long pausedTimestamp = 12L;
        FiniteLinearMovingProvider<Float> pausedFiniteLinearMovingFloatProvider =
                new FiniteLinearMovingFloatProvider(UUID, VALUES_AT_TIMES, pausedTimestamp,
                        MOST_RECENT_TIMESTAMP);

        assertEquals(pausedTimestamp,
                (long) pausedFiniteLinearMovingFloatProvider.pausedTimestamp());
    }

    @Test
    public void testProvideWhenPaused() {
        finiteLinearMovingFloatProvider.reportPause(TIME_1);

        assertEquals(VALUE_1, (float) finiteLinearMovingFloatProvider.provide(123123123L));
    }

    @Test
    public void testProvideOrReportPauseOrUnpauseWithInvalidTimestampAndMostRecentTimestamp() {
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingFloatProvider.provide(MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingFloatProvider.reportUnpause(MOST_RECENT_TIMESTAMP - 1));

        finiteLinearMovingFloatProvider.provide(MOST_RECENT_TIMESTAMP + 1);

        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingFloatProvider.provide(MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingFloatProvider.reportUnpause(MOST_RECENT_TIMESTAMP));
        assertEquals(MOST_RECENT_TIMESTAMP + 1,
                (long) finiteLinearMovingFloatProvider.mostRecentTimestamp());

        finiteLinearMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP + 2);

        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingFloatProvider.provide(MOST_RECENT_TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingFloatProvider.reportUnpause(MOST_RECENT_TIMESTAMP + 1));
        assertEquals(MOST_RECENT_TIMESTAMP + 2,
                (long) finiteLinearMovingFloatProvider.mostRecentTimestamp());
        assertEquals(MOST_RECENT_TIMESTAMP + 2,
                (long) finiteLinearMovingFloatProvider.pausedTimestamp());

        finiteLinearMovingFloatProvider.reportUnpause(MOST_RECENT_TIMESTAMP + 3);

        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingFloatProvider.provide(MOST_RECENT_TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingFloatProvider.reportUnpause(MOST_RECENT_TIMESTAMP + 2));
        assertEquals(MOST_RECENT_TIMESTAMP + 3,
                (long) finiteLinearMovingFloatProvider.mostRecentTimestamp());
        assertNull(finiteLinearMovingFloatProvider.pausedTimestamp());
    }

    @Test
    public void testReportPauseWhilePausedOrViceVersa() {
        assertThrows(UnsupportedOperationException.class, () ->
                finiteLinearMovingFloatProvider.reportUnpause(MOST_RECENT_TIMESTAMP));

        finiteLinearMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP);

        assertThrows(UnsupportedOperationException.class, () ->
                finiteLinearMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testReportPauseAndUnpauseUpdatesTimestampsForValues() {
        var pauseDuration = 123123L;
        var timeAfterTime1 = 50;
        var timestamp = TIME_1 + timeAfterTime1;
        var distanceBetweenTimes = TIME_2 - TIME_1;
        var time2Weight = timeAfterTime1 / (float) distanceBetweenTimes;
        var time1Weight = 1f - time2Weight;
        var weightedValue1 = VALUE_1 * time1Weight;
        var weightedValue2 = VALUE_2 * time2Weight;

        finiteLinearMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP);
        finiteLinearMovingFloatProvider.reportUnpause(MOST_RECENT_TIMESTAMP + pauseDuration);

        var valuesAtTimestampsRepresentation =
                finiteLinearMovingFloatProvider.valuesAtTimestampsRepresentation();

        assertEquals(VALUE_1, (float) valuesAtTimestampsRepresentation.get(TIME_1 + pauseDuration));
        assertEquals(VALUE_2, (float) valuesAtTimestampsRepresentation.get(TIME_2 + pauseDuration));
        assertEquals(VALUE_3, (float) valuesAtTimestampsRepresentation.get(TIME_3 + pauseDuration));

        assertEquals(weightedValue1 + weightedValue2,
                (float) finiteLinearMovingFloatProvider.provide(timestamp + pauseDuration));
    }

    @Test
    public void testRepresentation() {
        assertEquals(VALUES_AT_TIMES, finiteLinearMovingFloatProvider.representation());
        assertNotSame(VALUES_AT_TIMES, finiteLinearMovingFloatProvider.representation());
    }
}
