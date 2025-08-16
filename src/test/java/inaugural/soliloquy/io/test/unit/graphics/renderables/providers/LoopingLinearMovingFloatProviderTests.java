package inaugural.soliloquy.io.test.unit.graphics.renderables.providers;

import inaugural.soliloquy.io.graphics.renderables.providers.LoopingLinearMovingFloatProvider;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.graphics.renderables.providers.LoopingLinearMovingProvider;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomLong;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

@ExtendWith(MockitoExtension.class)
public class LoopingLinearMovingFloatProviderTests {
    private final Map<Integer, Float> VALUES_AT_TIMES = mapOf();
    private final int TIME_1 = 0;
    private final float VALUE_1 = 0.2f;
    private final int TIME_2 = 100;
    private final float VALUE_2 = 0.4f;
    private final int TIME_3 = 300;
    private final float VALUE_3 = 0.6f;
    private final int PERIOD_DURATION = 600;
    private final int MODULO_OFFSET = 123;

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private TimestampValidator mockTimestampValidator;

    private LoopingLinearMovingProvider<Float> provider;

    @BeforeEach
    public void setUp() {
        VALUES_AT_TIMES.put(TIME_1, VALUE_1);
        VALUES_AT_TIMES.put(TIME_2, VALUE_2);
        VALUES_AT_TIMES.put(TIME_3, VALUE_3);

        provider = new LoopingLinearMovingFloatProvider(UUID, VALUES_AT_TIMES, PERIOD_DURATION,
                MODULO_OFFSET, null, mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatProvider(null, VALUES_AT_TIMES, PERIOD_DURATION,
                        MODULO_OFFSET, null, mockTimestampValidator));

        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatProvider(UUID, null, PERIOD_DURATION,
                        MODULO_OFFSET, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatProvider(UUID, mapOf(), PERIOD_DURATION,
                        MODULO_OFFSET, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatProvider(UUID, mapOf(pairOf(null, VALUE_1)),
                        PERIOD_DURATION,
                        MODULO_OFFSET, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatProvider(UUID, mapOf(pairOf(TIME_1, null)),
                        PERIOD_DURATION,
                        MODULO_OFFSET, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatProvider(UUID, mapOf(pairOf(TIME_2, VALUE_2)),
                        PERIOD_DURATION,
                        MODULO_OFFSET, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatProvider(UUID,
                        mapOf(pairOf(PERIOD_DURATION + 1, VALUE_1)), PERIOD_DURATION,
                        MODULO_OFFSET, null, mockTimestampValidator));

        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatProvider(UUID, VALUES_AT_TIMES, PERIOD_DURATION,
                        -1, null, mockTimestampValidator));

        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatProvider(UUID, VALUES_AT_TIMES, PERIOD_DURATION,
                        PERIOD_DURATION, null, null));
    }

    @Test
    public void testUuid() {
        assertEquals(UUID, provider.uuid());
    }

    @Test
    public void testPausedTimestamp() {
        long pausedTimestamp = randomLong();
        when(mockTimestampValidator.mostRecentTimestamp()).thenReturn(pausedTimestamp);

        var provider =
                new LoopingLinearMovingFloatProvider(UUID, VALUES_AT_TIMES, PERIOD_DURATION,
                        MODULO_OFFSET, null, mockTimestampValidator);
        provider.reportPause(pausedTimestamp);

        assertEquals(pausedTimestamp, (long) provider.pausedTimestamp());
        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(pausedTimestamp);
    }

    @Test
    public void testValuesWithinPeriod() {
        Map<Integer, Float> valuesWithinPeriod =
                provider.valuesWithinPeriod();

        assertNotNull(valuesWithinPeriod);
        assertNotSame(provider.valuesWithinPeriod(), valuesWithinPeriod);
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
        assertEquals(VALUE_1,
                (float) provider.provide(TIME_1 - MODULO_OFFSET));
        assertEquals(VALUE_2,
                (float) provider.provide(TIME_2 - MODULO_OFFSET));
        assertEquals(VALUE_3,
                (float) provider.provide(TIME_3 - MODULO_OFFSET));
    }

    @Test
    public void testProvideWithinProvidedValues() {
        long timeAfterValue1 = 50L;
        long timestamp = TIME_1 - MODULO_OFFSET + timeAfterValue1;
        long timeInterval = TIME_2 - TIME_1;
        float value1Weight = (timeInterval - timeAfterValue1) / (float) timeInterval;
        float value2Weight = 1f - value1Weight;

        float expected = (VALUE_1 * value1Weight) + (VALUE_2 * value2Weight);

        assertEquals(expected, (float) provider.provide(timestamp));
    }

    @Test
    public void testProvidePastProvidedValues() {
        long timeAfterValue3 = 50L;
        long timestamp = TIME_3 - MODULO_OFFSET + timeAfterValue3;
        long timeInterval = PERIOD_DURATION - TIME_3;
        float value3Weight = (timeInterval - timeAfterValue3) / (float) timeInterval;
        float value1Weight = 1f - value3Weight;

        float expected = (VALUE_3 * value3Weight) + (VALUE_1 * value1Weight);

        assertEquals(expected, (float) provider.provide(timestamp));
    }

    @Test
    public void testProvideWhenPaused() {
        when(mockTimestampValidator.mostRecentTimestamp())
                .thenReturn((long) TIME_1 - MODULO_OFFSET);

        provider.reportPause(TIME_1 - MODULO_OFFSET);

        assertEquals(VALUE_1, (float) provider.provide(123123123L));
    }

    @Test
    public void testReset() {
        long resetTimestamp = 123123L;

        provider.reset(resetTimestamp);

        assertEquals(VALUE_1, (float) provider.provide(resetTimestamp));
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

        float expected = (VALUE_1 * value1Weight) + (VALUE_2 * value2Weight);

        provider.reportPause(0L);
        provider.reportUnpause(pauseDuration);

        assertEquals(expected,
                (float) provider.provide(timestamp + pauseDuration));
    }

    @Test
    public void testRepresentation() {
        assertEquals(VALUES_AT_TIMES, provider.representation());
        assertNotSame(VALUES_AT_TIMES, provider.representation());
    }
}
