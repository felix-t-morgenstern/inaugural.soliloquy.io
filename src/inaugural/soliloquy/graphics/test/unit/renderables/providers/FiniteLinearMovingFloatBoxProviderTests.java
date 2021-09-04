package inaugural.soliloquy.graphics.test.unit.renderables.providers;

import inaugural.soliloquy.graphics.renderables.providers.FiniteLinearMovingFloatBoxProvider;
import inaugural.soliloquy.graphics.renderables.providers.FiniteLinearMovingFloatProvider;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.providers.FiniteLinearMovingProvider;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FiniteLinearMovingFloatBoxProviderTests {
    private final FakeFloatBoxFactory FLOAT_BOX_FACTORY = new FakeFloatBoxFactory();
    private final FakeEntityUuid UUID = new FakeEntityUuid();
    private final HashMap<Long, FloatBox> VALUES_AT_TIMES = new HashMap<>();

    private final long TIME_1 = 100L;
    private final float FLOAT_BOX_1_LEFT_X = 0.111f;
    private final float FLOAT_BOX_1_TOP_Y = 0.222f;
    private final float FLOAT_BOX_1_RIGHT_X = 0.333f;
    private final float FLOAT_BOX_1_BOTTOM_Y = 0.444f;
    private final FakeFloatBox FLOAT_BOX_1 = new FakeFloatBox(FLOAT_BOX_1_LEFT_X,
            FLOAT_BOX_1_TOP_Y, FLOAT_BOX_1_RIGHT_X, FLOAT_BOX_1_BOTTOM_Y);

    private final long TIME_2 = 300L;
    private final float FLOAT_BOX_2_LEFT_X = 0.555f;
    private final float FLOAT_BOX_2_TOP_Y = 0.666f;
    private final float FLOAT_BOX_2_RIGHT_X = 0.777f;
    private final float FLOAT_BOX_2_BOTTOM_Y = 0.888f;
    private final FakeFloatBox FLOAT_BOX_2 = new FakeFloatBox(FLOAT_BOX_2_LEFT_X,
            FLOAT_BOX_2_TOP_Y, FLOAT_BOX_2_RIGHT_X, FLOAT_BOX_2_BOTTOM_Y);

    private final long TIME_3 = 500L;
    private final float FLOAT_BOX_3_LEFT_X = 0.123f;
    private final float FLOAT_BOX_3_TOP_Y = 0.234f;
    private final float FLOAT_BOX_3_RIGHT_X = 0.345f;
    private final float FLOAT_BOX_3_BOTTOM_Y = 0.456f;
    private final FakeFloatBox FLOAT_BOX_3 = new FakeFloatBox(FLOAT_BOX_3_LEFT_X,
            FLOAT_BOX_3_TOP_Y, FLOAT_BOX_3_RIGHT_X, FLOAT_BOX_3_BOTTOM_Y);

    private final long MOST_RECENT_TIMESTAMP = 34L;

    private FiniteLinearMovingProvider<FloatBox> _finiteLinearMovingFloatBoxProvider;

    @BeforeEach
    void setUp() {
        VALUES_AT_TIMES.put(TIME_1, FLOAT_BOX_1);
        VALUES_AT_TIMES.put(TIME_2, FLOAT_BOX_2);
        VALUES_AT_TIMES.put(TIME_3, FLOAT_BOX_3);

        _finiteLinearMovingFloatBoxProvider = new FiniteLinearMovingFloatBoxProvider(
                FLOAT_BOX_FACTORY, UUID, VALUES_AT_TIMES, null, MOST_RECENT_TIMESTAMP);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingFloatBoxProvider(null, UUID, VALUES_AT_TIMES,
                        null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingFloatBoxProvider(FLOAT_BOX_FACTORY, null, VALUES_AT_TIMES,
                        null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingFloatBoxProvider(FLOAT_BOX_FACTORY, UUID, null,
                        null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingFloatBoxProvider(FLOAT_BOX_FACTORY, UUID, VALUES_AT_TIMES,
                        2L, null));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingFloatBoxProvider(FLOAT_BOX_FACTORY, UUID, VALUES_AT_TIMES,
                        2L, 1L));
    }

    @Test
    void testProvideAtExtremes() {
        assertEquals(FLOAT_BOX_1, _finiteLinearMovingFloatBoxProvider.provide(TIME_1 - 1));
        assertEquals(FLOAT_BOX_1, _finiteLinearMovingFloatBoxProvider.provide(TIME_1));
        assertEquals(FLOAT_BOX_3, _finiteLinearMovingFloatBoxProvider.provide(TIME_3));
        assertEquals(FLOAT_BOX_3, _finiteLinearMovingFloatBoxProvider.provide(TIME_3 + 1));
    }

    @Test
    void testProvideInterpolatedValue() {
        long timeAfterTime1 = 50;
        long timestamp = TIME_1 + timeAfterTime1;
        long distanceBetweenTimes = TIME_2 - TIME_1;
        float time2Weight = timeAfterTime1 / (float)distanceBetweenTimes;
        float time1Weight = 1f - time2Weight;

        float weightedFloatBox1LeftX = FLOAT_BOX_1_LEFT_X * time1Weight;
        float weightedFloatBox1TopY = FLOAT_BOX_1_TOP_Y * time1Weight;
        float weightedFloatBox1RightX = FLOAT_BOX_1_RIGHT_X * time1Weight;
        float weightedFloatBox1BottomY = FLOAT_BOX_1_BOTTOM_Y * time1Weight;

        float weightedFloatBox2LeftX = FLOAT_BOX_2_LEFT_X * time2Weight;
        float weightedFloatBox2TopY = FLOAT_BOX_2_TOP_Y * time2Weight;
        float weightedFloatBox2RightX = FLOAT_BOX_2_RIGHT_X * time2Weight;
        float weightedFloatBox2BottomY = FLOAT_BOX_2_BOTTOM_Y * time2Weight;

        FakeFloatBox interpolatedFloatBox = new FakeFloatBox(
                weightedFloatBox1LeftX + weightedFloatBox2LeftX,
                weightedFloatBox1TopY + weightedFloatBox2TopY,
                weightedFloatBox1RightX + weightedFloatBox2RightX,
                weightedFloatBox1BottomY + weightedFloatBox2BottomY);

        assertEquals(interpolatedFloatBox, _finiteLinearMovingFloatBoxProvider.provide(timestamp));
    }

    @Test
    void testPausedTimestamp() {
        long pausedTimestamp = 12L;
        FiniteLinearMovingProvider<FloatBox> pausedFiniteLinearMovingFloatBoxProvider =
                new FiniteLinearMovingFloatBoxProvider(FLOAT_BOX_FACTORY, UUID, VALUES_AT_TIMES, 
                        pausedTimestamp, MOST_RECENT_TIMESTAMP);

        assertEquals(pausedTimestamp,
                (long)pausedFiniteLinearMovingFloatBoxProvider.pausedTimestamp());
    }

    @Test
    void testProvideWhenPaused() {
        _finiteLinearMovingFloatBoxProvider.reportPause(TIME_1);

        assertEquals(FLOAT_BOX_1, _finiteLinearMovingFloatBoxProvider.provide(123123123L));
    }

    @Test
    void testProvideOrReportPauseOrUnpauseWithInvalidTimestampAndMostRecentTimestamp() {
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingFloatBoxProvider.provide(MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingFloatBoxProvider.reportPause(MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingFloatBoxProvider.reportUnpause(MOST_RECENT_TIMESTAMP - 1));

        _finiteLinearMovingFloatBoxProvider.provide(MOST_RECENT_TIMESTAMP + 1);

        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingFloatBoxProvider.provide(MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingFloatBoxProvider.reportPause(MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingFloatBoxProvider.reportUnpause(MOST_RECENT_TIMESTAMP));
        assertEquals(MOST_RECENT_TIMESTAMP + 1,
                (long)_finiteLinearMovingFloatBoxProvider.mostRecentTimestamp());

        _finiteLinearMovingFloatBoxProvider.reportPause(MOST_RECENT_TIMESTAMP + 2);

        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingFloatBoxProvider.provide(MOST_RECENT_TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingFloatBoxProvider.reportPause(MOST_RECENT_TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingFloatBoxProvider.reportUnpause(MOST_RECENT_TIMESTAMP + 1));
        assertEquals(MOST_RECENT_TIMESTAMP + 2,
                (long)_finiteLinearMovingFloatBoxProvider.mostRecentTimestamp());
        assertEquals(MOST_RECENT_TIMESTAMP + 2,
                (long)_finiteLinearMovingFloatBoxProvider.pausedTimestamp());

        _finiteLinearMovingFloatBoxProvider.reportUnpause(MOST_RECENT_TIMESTAMP + 3);

        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingFloatBoxProvider.provide(MOST_RECENT_TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingFloatBoxProvider.reportPause(MOST_RECENT_TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingFloatBoxProvider.reportUnpause(MOST_RECENT_TIMESTAMP + 2));
        assertEquals(MOST_RECENT_TIMESTAMP + 3,
                (long)_finiteLinearMovingFloatBoxProvider.mostRecentTimestamp());
        assertNull(_finiteLinearMovingFloatBoxProvider.pausedTimestamp());
    }

    @Test
    void testReportPauseWhilePausedOrViceVersa() {
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingFloatBoxProvider.reportUnpause(MOST_RECENT_TIMESTAMP));

        _finiteLinearMovingFloatBoxProvider.reportPause(MOST_RECENT_TIMESTAMP);

        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingFloatBoxProvider.reportPause(MOST_RECENT_TIMESTAMP));
    }

    @Test
    void testReportPauseAndUnpauseUpdatesTimestampsForValues() {
        long pauseDuration = 123123L;
        long timeAfterTime1 = 50;
        long timestamp = TIME_1 + timeAfterTime1;
        long distanceBetweenTimes = TIME_2 - TIME_1;
        float time2Weight = timeAfterTime1 / (float)distanceBetweenTimes;
        float time1Weight = 1f - time2Weight;

        float weightedFloatBox1LeftX = FLOAT_BOX_1_LEFT_X * time1Weight;
        float weightedFloatBox1TopY = FLOAT_BOX_1_TOP_Y * time1Weight;
        float weightedFloatBox1RightX = FLOAT_BOX_1_RIGHT_X * time1Weight;
        float weightedFloatBox1BottomY = FLOAT_BOX_1_BOTTOM_Y * time1Weight;

        float weightedFloatBox2LeftX = FLOAT_BOX_2_LEFT_X * time2Weight;
        float weightedFloatBox2TopY = FLOAT_BOX_2_TOP_Y * time2Weight;
        float weightedFloatBox2RightX = FLOAT_BOX_2_RIGHT_X * time2Weight;
        float weightedFloatBox2BottomY = FLOAT_BOX_2_BOTTOM_Y * time2Weight;

        FakeFloatBox interpolatedFloatBox = new FakeFloatBox(
                weightedFloatBox1LeftX + weightedFloatBox2LeftX,
                weightedFloatBox1TopY + weightedFloatBox2TopY,
                weightedFloatBox1RightX + weightedFloatBox2RightX,
                weightedFloatBox1BottomY + weightedFloatBox2BottomY);

        _finiteLinearMovingFloatBoxProvider.reportPause(MOST_RECENT_TIMESTAMP);
        _finiteLinearMovingFloatBoxProvider.reportUnpause(MOST_RECENT_TIMESTAMP + pauseDuration);

        Map<Long, FloatBox> valuesAtTimestampsRepresentation =
                _finiteLinearMovingFloatBoxProvider.valuesAtTimestampsRepresentation();

        assertEquals(FLOAT_BOX_1, valuesAtTimestampsRepresentation.get(TIME_1 + pauseDuration));
        assertEquals(FLOAT_BOX_2, valuesAtTimestampsRepresentation.get(TIME_2 + pauseDuration));
        assertEquals(FLOAT_BOX_3, valuesAtTimestampsRepresentation.get(TIME_3 + pauseDuration));

        assertEquals(interpolatedFloatBox,
                _finiteLinearMovingFloatBoxProvider.provide(timestamp + pauseDuration));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(FiniteLinearMovingProvider.class.getCanonicalName() + "<" +
                        FloatBox.class.getCanonicalName() + ">",
                _finiteLinearMovingFloatBoxProvider.getInterfaceName());
    }
}
