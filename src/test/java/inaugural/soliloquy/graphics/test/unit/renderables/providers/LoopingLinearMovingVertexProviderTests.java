package inaugural.soliloquy.graphics.test.unit.renderables.providers;

import inaugural.soliloquy.graphics.renderables.providers.LoopingLinearMovingVertexProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.providers.LoopingLinearMovingProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class LoopingLinearMovingVertexProviderTests {
    private final int TIME_1 = 0;
    private final float LOCATION_1_X = 0.1f;
    private final float LOCATION_1_Y = 0.11f;
    private final Vertex LOCATION_1 = Vertex.of(LOCATION_1_X, LOCATION_1_Y);

    private final int TIME_2 = 100;
    private final float LOCATION_2_X = 0.2f;
    private final float LOCATION_2_Y = 0.22f;
    private final Vertex LOCATION_2 = Vertex.of(LOCATION_2_X, LOCATION_2_Y);

    private final int TIME_3 = 300;
    private final float LOCATION_3_X = 0.3f;
    private final float LOCATION_3_Y = 0.33f;
    private final Vertex LOCATION_3 = Vertex.of(LOCATION_3_X, LOCATION_3_Y);

    private final int PERIOD_DURATION = 600;
    private final int MODULO_OFFSET = 123;

    private final HashMap<Integer, Vertex> VALUES_AT_TIMES =
            new HashMap<Integer, Vertex>() {{
                put(TIME_1, LOCATION_1);
                put(TIME_2, LOCATION_2);
                put(TIME_3, LOCATION_3);
            }};

    private final UUID UUID = java.util.UUID.randomUUID();

    private LoopingLinearMovingProvider<Vertex> _loopingLinearMovingLocationProvider;

    @BeforeEach
    void setUp() {
        _loopingLinearMovingLocationProvider = new LoopingLinearMovingVertexProvider(UUID,
                VALUES_AT_TIMES, PERIOD_DURATION, MODULO_OFFSET, null, null);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingVertexProvider(null, VALUES_AT_TIMES, PERIOD_DURATION,
                        MODULO_OFFSET, null, null));

        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingVertexProvider(UUID, null, PERIOD_DURATION,
                        MODULO_OFFSET, null, null));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingVertexProvider(UUID, new HashMap<>(), PERIOD_DURATION,
                        MODULO_OFFSET, null, null));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingVertexProvider(UUID,
                        new HashMap<Integer, Vertex>() {{
                            put(null, LOCATION_1);
                        }},
                        PERIOD_DURATION, MODULO_OFFSET, null, null));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingVertexProvider(UUID,
                        new HashMap<Integer, Vertex>() {{
                            put(TIME_1, null);
                        }}, PERIOD_DURATION,
                        MODULO_OFFSET, null, null));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingVertexProvider(UUID,
                        new HashMap<Integer, Vertex>() {{
                            put(TIME_2, LOCATION_2);
                        }},
                        PERIOD_DURATION, MODULO_OFFSET, null, null));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingVertexProvider(UUID,
                        new HashMap<Integer, Vertex>() {{
                            put(PERIOD_DURATION + 1, LOCATION_1);
                        }}, PERIOD_DURATION,
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
    void testUuid() {
        assertEquals(UUID, _loopingLinearMovingLocationProvider.uuid());
    }

    @Test
    void testMostRecentTimestampAndPausedTimestamp() {
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
    void testGetArchetype() {
        assertNotNull(_loopingLinearMovingLocationProvider.getArchetype());
    }

    @Test
    void testValuesWithinPeriod() {
        Map<Integer, Vertex> valuesWithinPeriod =
                _loopingLinearMovingLocationProvider.valuesWithinPeriod();

        assertNotNull(valuesWithinPeriod);
        assertNotSame(_loopingLinearMovingLocationProvider.valuesWithinPeriod(),
                valuesWithinPeriod);
        assertEquals(VALUES_AT_TIMES.size(), valuesWithinPeriod.size());
        VALUES_AT_TIMES.keySet().forEach(key ->
                assertEquals(VALUES_AT_TIMES.get(key), valuesWithinPeriod.get((int) (long) key)));
    }

    @Test
    void testPeriodDuration() {
        assertEquals(PERIOD_DURATION, _loopingLinearMovingLocationProvider.periodDuration());
    }

    @Test
    void testProvideAtKey() {
        assertEquals(LOCATION_1,
                _loopingLinearMovingLocationProvider.provide(TIME_1 - MODULO_OFFSET));
        assertEquals(LOCATION_2,
                _loopingLinearMovingLocationProvider.provide(TIME_2 - MODULO_OFFSET));
        assertEquals(LOCATION_3,
                _loopingLinearMovingLocationProvider.provide(TIME_3 - MODULO_OFFSET));
    }

    @Test
    void testProvideWithinProvidedValues() {
        long timeAfterValue1 = 50L;
        long timestamp = TIME_1 - MODULO_OFFSET + timeAfterValue1;
        long timeInterval = TIME_2 - TIME_1;
        float value1Weight = (timeInterval - timeAfterValue1) / (float) timeInterval;
        float value2Weight = 1f - value1Weight;

        float expectedX = (LOCATION_1_X * value1Weight) + (LOCATION_2_X * value2Weight);
        float expectedY = (LOCATION_1_Y * value1Weight) + (LOCATION_2_Y * value2Weight);

        Vertex expected = Vertex.of(expectedX, expectedY);

        Vertex provided = _loopingLinearMovingLocationProvider.provide(timestamp);

        assertEquals(expected.x, provided.x);
        assertEquals(expected.y, provided.y);
    }

    @Test
    void testProvidePastProvidedValues() {
        long timeAfterValue3 = 50L;
        long timestamp = TIME_3 - MODULO_OFFSET + timeAfterValue3;
        long timeInterval = PERIOD_DURATION - TIME_3;
        float value3Weight = (timeInterval - timeAfterValue3) / (float) timeInterval;
        float value1Weight = 1f - value3Weight;

        float expectedX = (LOCATION_3_X * value3Weight) + (LOCATION_1_X * value1Weight);
        float expectedY = (LOCATION_3_Y * value3Weight) + (LOCATION_1_Y * value1Weight);

        Vertex expected = Vertex.of(expectedX, expectedY);

        Vertex provided = _loopingLinearMovingLocationProvider.provide(timestamp);

        assertEquals(expected.x, provided.x);
        assertEquals(expected.y, provided.y);
    }

    @Test
    void testProvideWhenPaused() {
        _loopingLinearMovingLocationProvider.reportPause(TIME_1 - MODULO_OFFSET);

        assertEquals(LOCATION_1, _loopingLinearMovingLocationProvider.provide(123123123L));
    }

    @Test
    void testReset() {
        long resetTimestamp = 123123L;

        _loopingLinearMovingLocationProvider.reset(resetTimestamp);

        assertEquals(LOCATION_1, _loopingLinearMovingLocationProvider.provide(resetTimestamp));
    }

    @Test
    void testReportPauseOrProvideWithOutdatedTimestamp() {
        long timestamp = 123123L;

        _loopingLinearMovingLocationProvider.provide(timestamp);

        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingLocationProvider.provide(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingLocationProvider.reportPause(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingLocationProvider.reportUnpause(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingLocationProvider.reset(timestamp - 1));

        _loopingLinearMovingLocationProvider.reportPause(timestamp + 1);

        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingLocationProvider.provide(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingLocationProvider.reportUnpause(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingLocationProvider.reset(timestamp));

        _loopingLinearMovingLocationProvider.reportUnpause(timestamp + 2);

        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingLocationProvider.provide(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingLocationProvider.reportPause(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingLocationProvider.reset(timestamp + 1));

        _loopingLinearMovingLocationProvider.provide(timestamp + 3);

        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingLocationProvider.provide(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingLocationProvider.reportPause(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingLocationProvider.reportUnpause(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingLocationProvider.reset(timestamp + 2));
    }

    @Test
    void testReportPauseWhilePausedOrViceVersa() {
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingLocationProvider.reportUnpause(0L));

        _loopingLinearMovingLocationProvider.reportPause(0L);

        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingLocationProvider.reportPause(0L));
    }

    @Test
    void testReportPauseAndUnpauseUpdatesOffset() {
        long pauseDuration = 123123L;

        long timeAfterValue1 = 50L;
        long timestamp = TIME_1 - MODULO_OFFSET + PERIOD_DURATION + timeAfterValue1;
        long timeInterval = TIME_2 - TIME_1;
        float value1Weight = (timeInterval - timeAfterValue1) / (float) timeInterval;
        float value2Weight = 1f - value1Weight;

        float expectedX = (LOCATION_1_X * value1Weight) + (LOCATION_2_X * value2Weight);
        float expectedY = (LOCATION_1_Y * value1Weight) + (LOCATION_2_Y * value2Weight);

        Vertex expected = Vertex.of(expectedX, expectedY);

        _loopingLinearMovingLocationProvider.reportPause(0L);
        _loopingLinearMovingLocationProvider.reportUnpause(pauseDuration);

        Vertex provided =
                _loopingLinearMovingLocationProvider.provide(timestamp + pauseDuration);

        assertEquals(expected.x, provided.x);
        assertEquals(expected.y, provided.y);
    }

    @Test
    void testRepresentation() {
        assertEquals(VALUES_AT_TIMES, _loopingLinearMovingLocationProvider.representation());
        assertNotSame(VALUES_AT_TIMES, _loopingLinearMovingLocationProvider.representation());
    }
}
