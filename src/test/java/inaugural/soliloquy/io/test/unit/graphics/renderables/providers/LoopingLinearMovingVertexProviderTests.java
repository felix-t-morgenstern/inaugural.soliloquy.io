package inaugural.soliloquy.io.test.unit.graphics.renderables.providers;

import inaugural.soliloquy.io.graphics.renderables.providers.LoopingLinearMovingVertexProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.providers.LoopingLinearMovingProvider;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static org.junit.jupiter.api.Assertions.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

public class LoopingLinearMovingVertexProviderTests {
    private final int TIME_1 = 0;
    private final float LOCATION_1_X = 0.1f;
    private final float LOCATION_1_Y = 0.11f;
    private final Vertex LOCATION_1 = vertexOf(LOCATION_1_X, LOCATION_1_Y);

    private final int TIME_2 = 100;
    private final float LOCATION_2_X = 0.2f;
    private final float LOCATION_2_Y = 0.22f;
    private final Vertex LOCATION_2 = vertexOf(LOCATION_2_X, LOCATION_2_Y);

    private final int TIME_3 = 300;
    private final float LOCATION_3_X = 0.3f;
    private final float LOCATION_3_Y = 0.33f;
    private final Vertex LOCATION_3 = vertexOf(LOCATION_3_X, LOCATION_3_Y);

    private final int PERIOD_DURATION = 600;
    private final int MODULO_OFFSET = 123;

    private final Map<Integer, Vertex> VALUES_AT_TIMES = mapOf(
            pairOf(TIME_1, LOCATION_1),
            pairOf(TIME_2, LOCATION_2),
            pairOf(TIME_3, LOCATION_3)
    );

    private final UUID UUID = java.util.UUID.randomUUID();

    private LoopingLinearMovingProvider<Vertex> loopingLinearMovingLocationProvider;

    @BeforeEach
    public void setUp() {
        loopingLinearMovingLocationProvider = new LoopingLinearMovingVertexProvider(UUID,
                VALUES_AT_TIMES, PERIOD_DURATION, MODULO_OFFSET, null, null);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingVertexProvider(null, VALUES_AT_TIMES, PERIOD_DURATION,
                        MODULO_OFFSET, null, null));

        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingVertexProvider(UUID, null, PERIOD_DURATION,
                        MODULO_OFFSET, null, null));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingVertexProvider(UUID, mapOf(), PERIOD_DURATION,
                        MODULO_OFFSET, null, null));
        assertThrows(IllegalArgumentException.class,
                () -> new LoopingLinearMovingVertexProvider(UUID, mapOf(pairOf(null, LOCATION_1)),
                        PERIOD_DURATION, MODULO_OFFSET, null, null));
        assertThrows(IllegalArgumentException.class,
                () -> new LoopingLinearMovingVertexProvider(UUID, mapOf(pairOf(TIME_1, null)),
                        PERIOD_DURATION, MODULO_OFFSET, null, null));
        assertThrows(IllegalArgumentException.class,
                () -> new LoopingLinearMovingVertexProvider(UUID, mapOf(pairOf(TIME_2, LOCATION_2)),
                        PERIOD_DURATION, MODULO_OFFSET, null, null));
        assertThrows(IllegalArgumentException.class,
                () -> new LoopingLinearMovingVertexProvider(UUID,
                        mapOf(pairOf(PERIOD_DURATION + 1, LOCATION_1)), PERIOD_DURATION,
                        MODULO_OFFSET, null, null));

        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingVertexProvider(UUID, VALUES_AT_TIMES, PERIOD_DURATION,
                        -1, null, null));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingVertexProvider(UUID, VALUES_AT_TIMES, PERIOD_DURATION,
                        PERIOD_DURATION, null, null));

        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingVertexProvider(UUID, VALUES_AT_TIMES, PERIOD_DURATION,
                        MODULO_OFFSET, 123123L, null));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingVertexProvider(UUID, VALUES_AT_TIMES, PERIOD_DURATION,
                        MODULO_OFFSET, 1L, 0L));
    }

    @Test
    public void testUuid() {
        assertEquals(UUID, loopingLinearMovingLocationProvider.uuid());
    }

    @Test
    public void testMostRecentTimestampAndPausedTimestamp() {
        long pausedTimestamp = 123123L;
        long mostRecentTimestamp = 456456L;

        LoopingLinearMovingProvider<Vertex> loopingLinearMovingLocationProvider =
                new LoopingLinearMovingVertexProvider(UUID, VALUES_AT_TIMES, PERIOD_DURATION,
                        MODULO_OFFSET, pausedTimestamp, mostRecentTimestamp);

        assertEquals(pausedTimestamp,
                (long) loopingLinearMovingLocationProvider.pausedTimestamp());
        assertEquals(mostRecentTimestamp,
                (long) loopingLinearMovingLocationProvider.mostRecentTimestamp());
    }

    @Test
    public void testValuesWithinPeriod() {
        Map<Integer, Vertex> valuesWithinPeriod =
                loopingLinearMovingLocationProvider.valuesWithinPeriod();

        assertNotNull(valuesWithinPeriod);
        assertNotSame(loopingLinearMovingLocationProvider.valuesWithinPeriod(),
                valuesWithinPeriod);
        assertEquals(VALUES_AT_TIMES.size(), valuesWithinPeriod.size());
        VALUES_AT_TIMES.keySet().forEach(key ->
                assertEquals(VALUES_AT_TIMES.get(key), valuesWithinPeriod.get((int) (long) key)));
    }

    @Test
    public void testPeriodDuration() {
        assertEquals(PERIOD_DURATION, loopingLinearMovingLocationProvider.periodDuration());
    }

    @Test
    public void testProvideAtKey() {
        assertEquals(LOCATION_1,
                loopingLinearMovingLocationProvider.provide(TIME_1 - MODULO_OFFSET));
        assertEquals(LOCATION_2,
                loopingLinearMovingLocationProvider.provide(TIME_2 - MODULO_OFFSET));
        assertEquals(LOCATION_3,
                loopingLinearMovingLocationProvider.provide(TIME_3 - MODULO_OFFSET));
    }

    @Test
    public void testProvideWithinProvidedValues() {
        long timeAfterValue1 = 50L;
        long timestamp = TIME_1 - MODULO_OFFSET + timeAfterValue1;
        long timeInterval = TIME_2 - TIME_1;
        float value1Weight = (timeInterval - timeAfterValue1) / (float) timeInterval;
        float value2Weight = 1f - value1Weight;

        float expectedX = (LOCATION_1_X * value1Weight) + (LOCATION_2_X * value2Weight);
        float expectedY = (LOCATION_1_Y * value1Weight) + (LOCATION_2_Y * value2Weight);

        Vertex expected = vertexOf(expectedX, expectedY);

        Vertex provided = loopingLinearMovingLocationProvider.provide(timestamp);

        assertEquals(expected.X, provided.X);
        assertEquals(expected.Y, provided.Y);
    }

    @Test
    public void testProvidePastProvidedValues() {
        long timeAfterValue3 = 50L;
        long timestamp = TIME_3 - MODULO_OFFSET + timeAfterValue3;
        long timeInterval = PERIOD_DURATION - TIME_3;
        float value3Weight = (timeInterval - timeAfterValue3) / (float) timeInterval;
        float value1Weight = 1f - value3Weight;

        float expectedX = (LOCATION_3_X * value3Weight) + (LOCATION_1_X * value1Weight);
        float expectedY = (LOCATION_3_Y * value3Weight) + (LOCATION_1_Y * value1Weight);

        Vertex expected = vertexOf(expectedX, expectedY);

        Vertex provided = loopingLinearMovingLocationProvider.provide(timestamp);

        assertEquals(expected.X, provided.X);
        assertEquals(expected.Y, provided.Y);
    }

    @Test
    public void testProvideWhenPaused() {
        loopingLinearMovingLocationProvider.reportPause(TIME_1 - MODULO_OFFSET);

        assertEquals(LOCATION_1, loopingLinearMovingLocationProvider.provide(123123123L));
    }

    @Test
    public void testReset() {
        long resetTimestamp = 123123L;

        loopingLinearMovingLocationProvider.reset(resetTimestamp);

        assertEquals(LOCATION_1, loopingLinearMovingLocationProvider.provide(resetTimestamp));
    }

    @Test
    public void testReportPauseOrProvideWithOutdatedTimestamp() {
        long timestamp = 123123L;

        loopingLinearMovingLocationProvider.provide(timestamp);

        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingLocationProvider.provide(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingLocationProvider.reportPause(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingLocationProvider.reportUnpause(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingLocationProvider.reset(timestamp - 1));

        loopingLinearMovingLocationProvider.reportPause(timestamp + 1);

        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingLocationProvider.provide(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingLocationProvider.reportUnpause(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingLocationProvider.reset(timestamp));

        loopingLinearMovingLocationProvider.reportUnpause(timestamp + 2);

        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingLocationProvider.provide(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingLocationProvider.reportPause(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingLocationProvider.reset(timestamp + 1));

        loopingLinearMovingLocationProvider.provide(timestamp + 3);

        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingLocationProvider.provide(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingLocationProvider.reportPause(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingLocationProvider.reportUnpause(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingLocationProvider.reset(timestamp + 2));
    }

    @Test
    public void testReportPauseWhilePausedOrViceVersa() {
        assertThrows(UnsupportedOperationException.class, () ->
                loopingLinearMovingLocationProvider.reportUnpause(0L));

        loopingLinearMovingLocationProvider.reportPause(0L);

        assertThrows(UnsupportedOperationException.class, () ->
                loopingLinearMovingLocationProvider.reportPause(0L));
    }

    @Test
    public void testReportPauseAndUnpauseUpdatesOffset() {
        long pauseDuration = 123123L;

        long timeAfterValue1 = 50L;
        long timestamp = TIME_1 - MODULO_OFFSET + PERIOD_DURATION + timeAfterValue1;
        long timeInterval = TIME_2 - TIME_1;
        float value1Weight = (timeInterval - timeAfterValue1) / (float) timeInterval;
        float value2Weight = 1f - value1Weight;

        float expectedX = (LOCATION_1_X * value1Weight) + (LOCATION_2_X * value2Weight);
        float expectedY = (LOCATION_1_Y * value1Weight) + (LOCATION_2_Y * value2Weight);

        Vertex expected = vertexOf(expectedX, expectedY);

        loopingLinearMovingLocationProvider.reportPause(0L);
        loopingLinearMovingLocationProvider.reportUnpause(pauseDuration);

        Vertex provided =
                loopingLinearMovingLocationProvider.provide(timestamp + pauseDuration);

        assertEquals(expected.X, provided.X);
        assertEquals(expected.Y, provided.Y);
    }

    @Test
    public void testRepresentation() {
        assertEquals(VALUES_AT_TIMES, loopingLinearMovingLocationProvider.representation());
        assertNotSame(VALUES_AT_TIMES, loopingLinearMovingLocationProvider.representation());
    }
}
