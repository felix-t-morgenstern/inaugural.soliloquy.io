package inaugural.soliloquy.io.test.unit.graphics.renderables.providers;

import inaugural.soliloquy.io.graphics.renderables.providers.LoopingLinearMovingVertexProvider;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.providers.LoopingLinearMovingProvider;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomLong;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

@ExtendWith(MockitoExtension.class)
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

    @Mock private TimestampValidator mockTimestampValidator;

    private LoopingLinearMovingProvider<Vertex> provider;

    @BeforeEach
    public void setUp() {
        provider = new LoopingLinearMovingVertexProvider(UUID, VALUES_AT_TIMES, PERIOD_DURATION,
                MODULO_OFFSET, null, mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingVertexProvider(null, VALUES_AT_TIMES, PERIOD_DURATION,
                        MODULO_OFFSET, null, mockTimestampValidator));

        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingVertexProvider(UUID, null, PERIOD_DURATION,
                        MODULO_OFFSET, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingVertexProvider(UUID, mapOf(), PERIOD_DURATION,
                        MODULO_OFFSET, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new LoopingLinearMovingVertexProvider(UUID, mapOf(pairOf(null, LOCATION_1)),
                        PERIOD_DURATION, MODULO_OFFSET, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new LoopingLinearMovingVertexProvider(UUID, mapOf(pairOf(TIME_1, null)),
                        PERIOD_DURATION, MODULO_OFFSET, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new LoopingLinearMovingVertexProvider(UUID, mapOf(pairOf(TIME_2, LOCATION_2)),
                        PERIOD_DURATION, MODULO_OFFSET, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new LoopingLinearMovingVertexProvider(UUID,
                        mapOf(pairOf(PERIOD_DURATION + 1, LOCATION_1)), PERIOD_DURATION,
                        MODULO_OFFSET, null, mockTimestampValidator));

        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingVertexProvider(UUID, VALUES_AT_TIMES, PERIOD_DURATION,
                        -1, null, mockTimestampValidator));

        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingVertexProvider(UUID, VALUES_AT_TIMES, PERIOD_DURATION,
                        PERIOD_DURATION, null, null));
    }

    @Test
    public void testUuid() {
        assertEquals(UUID, provider.uuid());
    }

    @Test
    public void testPausedTimestamp() {
        var pausedTimestamp = randomLong();
        when(mockTimestampValidator.mostRecentTimestamp()).thenReturn(pausedTimestamp);

        var provider =
                new LoopingLinearMovingVertexProvider(UUID, VALUES_AT_TIMES, PERIOD_DURATION,
                        MODULO_OFFSET, pausedTimestamp, mockTimestampValidator);

        assertEquals(pausedTimestamp, (long) provider.pausedTimestamp());
    }

    @Test
    public void testValuesWithinPeriod() {
        Map<Integer, Vertex> valuesWithinPeriod =
                provider.valuesWithinPeriod();

        assertNotNull(valuesWithinPeriod);
        assertNotSame(provider.valuesWithinPeriod(),
                valuesWithinPeriod);
        assertEquals(VALUES_AT_TIMES.size(), valuesWithinPeriod.size());
        VALUES_AT_TIMES.keySet().forEach(key ->
                assertEquals(VALUES_AT_TIMES.get(key), valuesWithinPeriod.get((int) (long) key)));
    }

    @Test
    public void testPeriodDuration() {
        assertEquals(PERIOD_DURATION, provider.periodDuration());
    }

    @Test
    public void testProvideAtKey() {
        assertEquals(LOCATION_1,
                provider.provide(TIME_1 - MODULO_OFFSET));
        assertEquals(LOCATION_2,
                provider.provide(TIME_2 - MODULO_OFFSET));
        assertEquals(LOCATION_3,
                provider.provide(TIME_3 - MODULO_OFFSET));
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

        var expected = vertexOf(expectedX, expectedY);

        var provided = provider.provide(timestamp);

        assertEquals(expected.X, provided.X);
        assertEquals(expected.Y, provided.Y);
    }

    @Test
    public void testProvidePastProvidedValues() {
        var timeAfterValue3 = 50L;
        var timestamp = TIME_3 - MODULO_OFFSET + timeAfterValue3;
        var timeInterval = PERIOD_DURATION - TIME_3;
        var value3Weight = (timeInterval - timeAfterValue3) / (float) timeInterval;
        var value1Weight = 1f - value3Weight;

        var expectedX = (LOCATION_3_X * value3Weight) + (LOCATION_1_X * value1Weight);
        var expectedY = (LOCATION_3_Y * value3Weight) + (LOCATION_1_Y * value1Weight);

        var expected = vertexOf(expectedX, expectedY);

        var provided = provider.provide(timestamp);

        assertEquals(expected.X, provided.X);
        assertEquals(expected.Y, provided.Y);
    }

    @Test
    public void testProvideWhenPaused() {
        when(mockTimestampValidator.mostRecentTimestamp()).thenReturn((long) TIME_1 - MODULO_OFFSET);

        provider.reportPause(TIME_1 - MODULO_OFFSET);

        assertEquals(LOCATION_1, provider.provide(123123123L));
    }

    @Test
    public void testReset() {
        long resetTimestamp = 123123L;

        provider.reset(resetTimestamp);

        assertEquals(LOCATION_1, provider.provide(resetTimestamp));
    }

    @Test
    public void testReportPauseWhilePausedOrViceVersa() {
        assertThrows(UnsupportedOperationException.class, () ->
                provider.reportUnpause(0L));

        provider.reportPause(0L);

        assertThrows(UnsupportedOperationException.class, () ->
                provider.reportPause(0L));
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

        provider.reportPause(0L);
        provider.reportUnpause(pauseDuration);

        Vertex provided =
                provider.provide(timestamp + pauseDuration);

        assertEquals(expected.X, provided.X);
        assertEquals(expected.Y, provided.Y);
    }

    @Test
    public void testRepresentation() {
        assertEquals(VALUES_AT_TIMES, provider.representation());
        assertNotSame(VALUES_AT_TIMES, provider.representation());
    }
}
