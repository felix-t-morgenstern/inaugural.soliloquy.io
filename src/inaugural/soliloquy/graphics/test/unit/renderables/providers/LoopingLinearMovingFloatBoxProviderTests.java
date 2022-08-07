package inaugural.soliloquy.graphics.test.unit.renderables.providers;

import inaugural.soliloquy.graphics.renderables.providers.LoopingLinearMovingFloatBoxProvider;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.graphics.renderables.providers.LoopingLinearMovingProvider;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class LoopingLinearMovingFloatBoxProviderTests {
    private final int TIME_1 = 0;
    private final float BOX_1_LEFT_X = 0.1f;
    private final float BOX_1_TOP_Y = 0.11f;
    private final float BOX_1_RIGHT_X = 0.111f;
    private final float BOX_1_BOTTOM_Y = 0.1111f;
    private final FloatBox BOX_1 = 
            new FakeFloatBox(BOX_1_LEFT_X, BOX_1_TOP_Y, BOX_1_RIGHT_X, BOX_1_BOTTOM_Y);

    private final int TIME_2 = 100;
    private final float BOX_2_LEFT_X = 0.2f;
    private final float BOX_2_TOP_Y = 0.22f;
    private final float BOX_2_RIGHT_X = 0.222f;
    private final float BOX_2_BOTTOM_Y = 0.2222f;
    private final FloatBox BOX_2 =
            new FakeFloatBox(BOX_2_LEFT_X, BOX_2_TOP_Y, BOX_2_RIGHT_X, BOX_2_BOTTOM_Y);

    private final int TIME_3 = 300;
    private final float BOX_3_LEFT_X = 0.3f;
    private final float BOX_3_TOP_Y = 0.33f;
    private final float BOX_3_RIGHT_X = 0.333f;
    private final float BOX_3_BOTTOM_Y = 0.3333f;
    private final FloatBox BOX_3 =
            new FakeFloatBox(BOX_3_LEFT_X, BOX_3_TOP_Y, BOX_3_RIGHT_X, BOX_3_BOTTOM_Y);
    
    private final int PERIOD_DURATION = 600;
    private final int MODULO_OFFSET = 123;
    private final FakeFloatBoxFactory FLOAT_BOX_FACTORY = new FakeFloatBoxFactory();

    private final HashMap<Integer, FloatBox> VALUES_AT_TIMES = new HashMap<Integer, FloatBox>() {{
        put(TIME_1, BOX_1);
        put(TIME_2, BOX_2);
        put(TIME_3, BOX_3);
    }};

    private final UUID UUID = java.util.UUID.randomUUID();

    private LoopingLinearMovingProvider<FloatBox> _loopingLinearMovingFloatBoxProvider;
    
    @BeforeEach
    void setUp() {
        _loopingLinearMovingFloatBoxProvider = new LoopingLinearMovingFloatBoxProvider(UUID,
                VALUES_AT_TIMES, PERIOD_DURATION, MODULO_OFFSET, null, null, FLOAT_BOX_FACTORY);
    }
    
    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatBoxProvider(null, VALUES_AT_TIMES, PERIOD_DURATION,
                        MODULO_OFFSET, null, null, FLOAT_BOX_FACTORY));

        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatBoxProvider(UUID, null, PERIOD_DURATION,
                        MODULO_OFFSET, null, null, FLOAT_BOX_FACTORY));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatBoxProvider(UUID, new HashMap<>(), PERIOD_DURATION,
                        MODULO_OFFSET, null, null, FLOAT_BOX_FACTORY));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatBoxProvider(UUID, new HashMap<Integer, FloatBox>() {{
                    put(null, BOX_1);
                }}, PERIOD_DURATION,
                        MODULO_OFFSET, null, null, FLOAT_BOX_FACTORY));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatBoxProvider(UUID, new HashMap<Integer, FloatBox>() {{
                    put(TIME_1, null);
                }}, PERIOD_DURATION,
                        MODULO_OFFSET, null, null, FLOAT_BOX_FACTORY));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatBoxProvider(UUID, new HashMap<Integer, FloatBox>() {{
                    put(TIME_2, BOX_2);
                }}, PERIOD_DURATION,
                        MODULO_OFFSET, null, null, FLOAT_BOX_FACTORY));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatBoxProvider(UUID, new HashMap<Integer, FloatBox>() {{
                    put(PERIOD_DURATION + 1, BOX_1);
                }}, PERIOD_DURATION,
                        MODULO_OFFSET, null, null, FLOAT_BOX_FACTORY));

        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatBoxProvider(UUID, VALUES_AT_TIMES, PERIOD_DURATION,
                        -1, null, null, FLOAT_BOX_FACTORY));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatBoxProvider(UUID, VALUES_AT_TIMES, PERIOD_DURATION,
                        PERIOD_DURATION, null, null, FLOAT_BOX_FACTORY));

        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatBoxProvider(UUID, VALUES_AT_TIMES, PERIOD_DURATION,
                        MODULO_OFFSET, 123123L, null, FLOAT_BOX_FACTORY));
        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatBoxProvider(UUID, VALUES_AT_TIMES, PERIOD_DURATION,
                        MODULO_OFFSET, 1L, 0L, FLOAT_BOX_FACTORY));

        assertThrows(IllegalArgumentException.class, () ->
                new LoopingLinearMovingFloatBoxProvider(UUID, VALUES_AT_TIMES, PERIOD_DURATION,
                        MODULO_OFFSET, null, null, null));
    }

    @Test
    void testUuid() {
        assertEquals(UUID, _loopingLinearMovingFloatBoxProvider.uuid());
    }

    @Test
    void testMostRecentTimestampAndPausedTimestamp() {
        long pausedTimestamp = 123123L;
        long mostRecentTimestamp = 456456L;

        LoopingLinearMovingProvider<FloatBox> loopingLinearMovingFloatBoxProvider =
                new LoopingLinearMovingFloatBoxProvider(UUID, VALUES_AT_TIMES, PERIOD_DURATION,
                        MODULO_OFFSET, pausedTimestamp, mostRecentTimestamp, FLOAT_BOX_FACTORY);

        assertEquals(pausedTimestamp,
                (long)loopingLinearMovingFloatBoxProvider.pausedTimestamp());
        assertEquals(mostRecentTimestamp,
                (long)loopingLinearMovingFloatBoxProvider.mostRecentTimestamp());
    }

    @Test
    void testGetArchetype() {
        assertNotNull(_loopingLinearMovingFloatBoxProvider.getArchetype());
    }

    @Test
    void testValuesWithinPeriod() {
        Map<Integer, FloatBox> valuesWithinPeriod =
                _loopingLinearMovingFloatBoxProvider.valuesWithinPeriod();

        assertNotNull(valuesWithinPeriod);
        assertNotSame(_loopingLinearMovingFloatBoxProvider.valuesWithinPeriod(),
                valuesWithinPeriod);
        assertEquals(VALUES_AT_TIMES.size(), valuesWithinPeriod.size());
        VALUES_AT_TIMES.keySet().forEach(key ->
                assertEquals(VALUES_AT_TIMES.get(key), valuesWithinPeriod.get((int)(long)key)));
    }

    @Test
    void testPeriodDuration() {
        assertEquals(PERIOD_DURATION, _loopingLinearMovingFloatBoxProvider.periodDuration());
    }

    @Test
    void testProvideAtKey() {
        assertEquals(BOX_1, _loopingLinearMovingFloatBoxProvider.provide(TIME_1 - MODULO_OFFSET));
        assertEquals(BOX_2, _loopingLinearMovingFloatBoxProvider.provide(TIME_2 - MODULO_OFFSET));
        assertEquals(BOX_3, _loopingLinearMovingFloatBoxProvider.provide(TIME_3 - MODULO_OFFSET));
    }

    @Test
    void testProvideWithinProvidedValues() {
        long timeAfterValue1 = 50L;
        long timestamp = TIME_1 - MODULO_OFFSET + timeAfterValue1;
        long timeInterval = TIME_2 - TIME_1;
        float value1Weight = (timeInterval - timeAfterValue1) / (float)timeInterval;
        float value2Weight = 1f - value1Weight;

        float expectedLeftX = (BOX_1_LEFT_X * value1Weight) + (BOX_2_LEFT_X * value2Weight);
        float expectedTopY = (BOX_1_TOP_Y * value1Weight) + (BOX_2_TOP_Y * value2Weight);
        float expectedRightX = (BOX_1_RIGHT_X * value1Weight) + (BOX_2_RIGHT_X * value2Weight);
        float expectedBottomY = (BOX_1_BOTTOM_Y * value1Weight) + (BOX_2_BOTTOM_Y * value2Weight);

        FakeFloatBox expected =
                new FakeFloatBox(expectedLeftX, expectedTopY, expectedRightX, expectedBottomY);

        assertEquals(expected, _loopingLinearMovingFloatBoxProvider.provide(timestamp));
    }

    @Test
    void testProvidePastProvidedValues() {
        long timeAfterValue3 = 50L;
        long timestamp = TIME_3 - MODULO_OFFSET + timeAfterValue3;
        long timeInterval = PERIOD_DURATION - TIME_3;
        float value3Weight = (timeInterval - timeAfterValue3) / (float)timeInterval;
        float value1Weight = 1f - value3Weight;

        float expectedLeftX = (BOX_3_LEFT_X * value3Weight) + (BOX_1_LEFT_X * value1Weight);
        float expectedTopY = (BOX_3_TOP_Y * value3Weight) + (BOX_1_TOP_Y * value1Weight);
        float expectedRightX = (BOX_3_RIGHT_X * value3Weight) + (BOX_1_RIGHT_X * value1Weight);
        float expectedBottomY = (BOX_3_BOTTOM_Y * value3Weight) + (BOX_1_BOTTOM_Y * value1Weight);

        FakeFloatBox expected =
                new FakeFloatBox(expectedLeftX, expectedTopY, expectedRightX, expectedBottomY);

        assertEquals(expected, _loopingLinearMovingFloatBoxProvider.provide(timestamp));
    }

    @Test
    void testProvideWhenPaused() {
        _loopingLinearMovingFloatBoxProvider.reportPause(TIME_1 - MODULO_OFFSET);

        assertEquals(BOX_1, _loopingLinearMovingFloatBoxProvider.provide(123123123L));
    }

    @Test
    void testReset() {
        long resetTimestamp = 123123L;

        _loopingLinearMovingFloatBoxProvider.reset(resetTimestamp);

        assertEquals(BOX_1, _loopingLinearMovingFloatBoxProvider.provide(resetTimestamp));
    }

    @Test
    void testReportPauseOrProvideWithOutdatedTimestamp() {
        long timestamp = 123123L;

        _loopingLinearMovingFloatBoxProvider.provide(timestamp);

        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatBoxProvider.provide(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatBoxProvider.reportPause(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatBoxProvider.reportUnpause(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatBoxProvider.reset(timestamp - 1));

        _loopingLinearMovingFloatBoxProvider.reportPause(timestamp + 1);

        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatBoxProvider.provide(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatBoxProvider.reportUnpause(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatBoxProvider.reset(timestamp));

        _loopingLinearMovingFloatBoxProvider.reportUnpause(timestamp + 2);

        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatBoxProvider.provide(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatBoxProvider.reportPause(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatBoxProvider.reset(timestamp + 1));

        _loopingLinearMovingFloatBoxProvider.provide(timestamp + 3);

        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatBoxProvider.provide(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatBoxProvider.reportPause(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatBoxProvider.reportUnpause(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatBoxProvider.reset(timestamp + 2));
    }

    @Test
    void testReportPauseWhilePausedOrViceVersa() {
        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatBoxProvider.reportUnpause(0L));

        _loopingLinearMovingFloatBoxProvider.reportPause(0L);

        assertThrows(IllegalArgumentException.class, () ->
                _loopingLinearMovingFloatBoxProvider.reportPause(0L));
    }

    @Test
    void testReportPauseAndUnpauseUpdatesOffset() {
        long pauseDuration = 123123L;

        long timeAfterValue1 = 50L;
        long timestamp = TIME_1 - MODULO_OFFSET + PERIOD_DURATION + timeAfterValue1;
        long timeInterval = TIME_2 - TIME_1;
        float value1Weight = (timeInterval - timeAfterValue1) / (float)timeInterval;
        float value2Weight = 1f - value1Weight;

        float expectedLeftX = (BOX_1_LEFT_X * value1Weight) + (BOX_2_LEFT_X * value2Weight);
        float expectedTopY = (BOX_1_TOP_Y * value1Weight) + (BOX_2_TOP_Y * value2Weight);
        float expectedRightX = (BOX_1_RIGHT_X * value1Weight) + (BOX_2_RIGHT_X * value2Weight);
        float expectedBottomY = (BOX_1_BOTTOM_Y * value1Weight) + (BOX_2_BOTTOM_Y * value2Weight);

        FakeFloatBox expected =
                new FakeFloatBox(expectedLeftX, expectedTopY, expectedRightX, expectedBottomY);

        _loopingLinearMovingFloatBoxProvider.reportPause(0L);
        _loopingLinearMovingFloatBoxProvider.reportUnpause(pauseDuration);

        assertEquals(expected,
                _loopingLinearMovingFloatBoxProvider.provide(timestamp + pauseDuration));
    }
}
