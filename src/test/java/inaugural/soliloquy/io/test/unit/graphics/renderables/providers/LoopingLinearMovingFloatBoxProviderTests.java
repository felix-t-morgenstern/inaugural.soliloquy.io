package inaugural.soliloquy.io.test.unit.graphics.renderables.providers;

import inaugural.soliloquy.io.graphics.renderables.providers.LoopingLinearMovingFloatBoxProvider;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.providers.LoopingLinearMovingProvider;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomLong;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

@ExtendWith(MockitoExtension.class)
public class LoopingLinearMovingFloatBoxProviderTests {
    private final int TIME_1 = 0;
    private final float BOX_1_LEFT_X = 0.1f;
    private final float BOX_1_TOP_Y = 0.11f;
    private final float BOX_1_RIGHT_X = 0.111f;
    private final float BOX_1_BOTTOM_Y = 0.1111f;
    private final FloatBox BOX_1 =
            floatBoxOf(BOX_1_LEFT_X, BOX_1_TOP_Y, BOX_1_RIGHT_X, BOX_1_BOTTOM_Y);

    private final int TIME_2 = 100;
    private final float BOX_2_LEFT_X = 0.2f;
    private final float BOX_2_TOP_Y = 0.22f;
    private final float BOX_2_RIGHT_X = 0.222f;
    private final float BOX_2_BOTTOM_Y = 0.2222f;
    private final FloatBox BOX_2 =
            floatBoxOf(BOX_2_LEFT_X, BOX_2_TOP_Y, BOX_2_RIGHT_X, BOX_2_BOTTOM_Y);

    private final int TIME_3 = 300;
    private final float BOX_3_LEFT_X = 0.3f;
    private final float BOX_3_TOP_Y = 0.33f;
    private final float BOX_3_RIGHT_X = 0.333f;
    private final float BOX_3_BOTTOM_Y = 0.3333f;
    private final FloatBox BOX_3 =
            floatBoxOf(BOX_3_LEFT_X, BOX_3_TOP_Y, BOX_3_RIGHT_X, BOX_3_BOTTOM_Y);

    private final int PERIOD_DURATION = 600;
    private final int MODULO_OFFSET = 123;

    private final Map<Integer, FloatBox> VALUES_AT_TIMES = mapOf(
            pairOf(TIME_1, BOX_1),
            pairOf(TIME_2, BOX_2),
            pairOf(TIME_3, BOX_3)
    );

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private TimestampValidator mockTimestampValidator;

    private LoopingLinearMovingProvider<FloatBox> provider;

    @BeforeEach
    public void setUp() {
        provider = new LoopingLinearMovingFloatBoxProvider(UUID, VALUES_AT_TIMES, PERIOD_DURATION,
                MODULO_OFFSET, null, mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatBoxProvider(null, VALUES_AT_TIMES, PERIOD_DURATION,
                        MODULO_OFFSET, null, mockTimestampValidator));

        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatBoxProvider(UUID, null, PERIOD_DURATION,
                        MODULO_OFFSET, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatBoxProvider(UUID, mapOf(), PERIOD_DURATION,
                        MODULO_OFFSET, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatBoxProvider(UUID, mapOf(
                        pairOf(null, BOX_1)
                ), PERIOD_DURATION,
                        MODULO_OFFSET, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatBoxProvider(UUID, mapOf(
                        pairOf(TIME_1, null)
                ), PERIOD_DURATION,
                        MODULO_OFFSET, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatBoxProvider(UUID, mapOf(
                        pairOf(TIME_2, BOX_2)
                ), PERIOD_DURATION,
                        MODULO_OFFSET, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatBoxProvider(UUID, mapOf(
                        pairOf(PERIOD_DURATION + 1, BOX_1)
                ), PERIOD_DURATION,
                        MODULO_OFFSET, null, mockTimestampValidator));

        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatBoxProvider(UUID, VALUES_AT_TIMES, PERIOD_DURATION,
                        -1, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatBoxProvider(UUID, VALUES_AT_TIMES, PERIOD_DURATION,
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
                new LoopingLinearMovingFloatBoxProvider(UUID, VALUES_AT_TIMES, PERIOD_DURATION,
                        MODULO_OFFSET, pausedTimestamp, mockTimestampValidator);

        assertEquals(pausedTimestamp, (long) provider.pausedTimestamp());
    }

    @Test
    public void testValuesWithinPeriod() {
        Map<Integer, FloatBox> valuesWithinPeriod = provider.valuesWithinPeriod();

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
        assertEquals(BOX_1, provider.provide(TIME_1 - MODULO_OFFSET));
        assertEquals(BOX_2, provider.provide(TIME_2 - MODULO_OFFSET));
        assertEquals(BOX_3, provider.provide(TIME_3 - MODULO_OFFSET));
    }

    @Test
    public void testProvideWithinProvidedValues() {
        long timeAfterValue1 = 50L;
        long timestamp = TIME_1 - MODULO_OFFSET + timeAfterValue1;
        long timeInterval = TIME_2 - TIME_1;
        float value1Weight = (timeInterval - timeAfterValue1) / (float) timeInterval;
        float value2Weight = 1f - value1Weight;

        float expectedLeftX = (BOX_1_LEFT_X * value1Weight) + (BOX_2_LEFT_X * value2Weight);
        float expectedTopY = (BOX_1_TOP_Y * value1Weight) + (BOX_2_TOP_Y * value2Weight);
        float expectedRightX = (BOX_1_RIGHT_X * value1Weight) + (BOX_2_RIGHT_X * value2Weight);
        float expectedBottomY = (BOX_1_BOTTOM_Y * value1Weight) + (BOX_2_BOTTOM_Y * value2Weight);

        var expected = floatBoxOf(expectedLeftX, expectedTopY, expectedRightX, expectedBottomY);

        assertEquals(expected, provider.provide(timestamp));
    }

    @Test
    public void testProvidePastProvidedValues() {
        long timeAfterValue3 = 50L;
        long timestamp = TIME_3 - MODULO_OFFSET + timeAfterValue3;
        long timeInterval = PERIOD_DURATION - TIME_3;
        float value3Weight = (timeInterval - timeAfterValue3) / (float) timeInterval;
        float value1Weight = 1f - value3Weight;

        float expectedLeftX = (BOX_3_LEFT_X * value3Weight) + (BOX_1_LEFT_X * value1Weight);
        float expectedTopY = (BOX_3_TOP_Y * value3Weight) + (BOX_1_TOP_Y * value1Weight);
        float expectedRightX = (BOX_3_RIGHT_X * value3Weight) + (BOX_1_RIGHT_X * value1Weight);
        float expectedBottomY = (BOX_3_BOTTOM_Y * value3Weight) + (BOX_1_BOTTOM_Y * value1Weight);

        var expected = floatBoxOf(expectedLeftX, expectedTopY, expectedRightX, expectedBottomY);

        assertEquals(expected, provider.provide(timestamp));
    }

    @Test
    public void testProvideWhenPaused() {
        when(mockTimestampValidator.mostRecentTimestamp())
                .thenReturn((long) TIME_1 - MODULO_OFFSET);

        provider.reportPause(TIME_1 - MODULO_OFFSET);

        assertEquals(BOX_1, provider.provide(123123123L));
    }

    @Test
    public void testReset() {
        long resetTimestamp = 123123L;

        provider.reset(resetTimestamp);

        assertEquals(BOX_1, provider.provide(resetTimestamp));
    }

    @Test
    public void testReportPauseWhilePausedOrViceVersa() {
        assertThrows(UnsupportedOperationException.class, () -> provider.reportUnpause(0L));

        provider.reportPause(0L);

        assertThrows(UnsupportedOperationException.class, () -> provider.reportPause(0L));
    }

    @Test
    public void testReportPauseAndUnpauseUpdatesOffset() {
        long pauseDuration = 123123L;

        long timeAfterValue1 = 50L;
        long timestamp = TIME_1 - MODULO_OFFSET + PERIOD_DURATION + timeAfterValue1;
        long timeInterval = TIME_2 - TIME_1;
        float value1Weight = (timeInterval - timeAfterValue1) / (float) timeInterval;
        float value2Weight = 1f - value1Weight;

        float expectedLeftX = (BOX_1_LEFT_X * value1Weight) + (BOX_2_LEFT_X * value2Weight);
        float expectedTopY = (BOX_1_TOP_Y * value1Weight) + (BOX_2_TOP_Y * value2Weight);
        float expectedRightX = (BOX_1_RIGHT_X * value1Weight) + (BOX_2_RIGHT_X * value2Weight);
        float expectedBottomY = (BOX_1_BOTTOM_Y * value1Weight) + (BOX_2_BOTTOM_Y * value2Weight);

        var expected = floatBoxOf(expectedLeftX, expectedTopY, expectedRightX, expectedBottomY);

        provider.reportPause(0L);
        provider.reportUnpause(pauseDuration);

        assertEquals(expected, provider.provide(timestamp + pauseDuration));
    }

    @Test
    public void testRepresentation() {
        assertEquals(VALUES_AT_TIMES, provider.representation());
        assertNotSame(VALUES_AT_TIMES, provider.representation());
    }
}
