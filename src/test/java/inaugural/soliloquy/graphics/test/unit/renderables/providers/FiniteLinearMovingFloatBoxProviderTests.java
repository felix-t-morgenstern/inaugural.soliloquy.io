package inaugural.soliloquy.graphics.test.unit.renderables.providers;

import inaugural.soliloquy.graphics.renderables.providers.FiniteLinearMovingFloatBoxProvider;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeFloatBoxFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.providers.FiniteLinearMovingProvider;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.util.HashMap;
import java.util.UUID;

import static inaugural.soliloquy.graphics.test.Mocks.mockFloatBox;
import static org.junit.jupiter.api.Assertions.*;

class FiniteLinearMovingFloatBoxProviderTests {
    private final FakeFloatBoxFactory FLOAT_BOX_FACTORY = new FakeFloatBoxFactory();
    private final HashMap<Long, FloatBox> VALUES_AT_TIMES = new HashMap<>();

    private final long TIME_1 = 100L;
    private final float FLOAT_BOX_1_LEFT_X = 0.111f;
    private final float FLOAT_BOX_1_TOP_Y = 0.222f;
    private final float FLOAT_BOX_1_RIGHT_X = 0.333f;
    private final float FLOAT_BOX_1_BOTTOM_Y = 0.444f;
    private final FloatBox FLOAT_BOX_1 = mockFloatBox(FLOAT_BOX_1_LEFT_X,
            FLOAT_BOX_1_TOP_Y, FLOAT_BOX_1_RIGHT_X, FLOAT_BOX_1_BOTTOM_Y);

    private final long TIME_2 = 300L;
    private final float FLOAT_BOX_2_LEFT_X = 0.555f;
    private final float FLOAT_BOX_2_TOP_Y = 0.666f;
    private final float FLOAT_BOX_2_RIGHT_X = 0.777f;
    private final float FLOAT_BOX_2_BOTTOM_Y = 0.888f;
    private final FloatBox FLOAT_BOX_2 = mockFloatBox(FLOAT_BOX_2_LEFT_X,
            FLOAT_BOX_2_TOP_Y, FLOAT_BOX_2_RIGHT_X, FLOAT_BOX_2_BOTTOM_Y);

    private final long TIME_3 = 500L;
    private final float FLOAT_BOX_3_LEFT_X = 0.123f;
    private final float FLOAT_BOX_3_TOP_Y = 0.234f;
    private final float FLOAT_BOX_3_RIGHT_X = 0.345f;
    private final float FLOAT_BOX_3_BOTTOM_Y = 0.456f;
    private final FloatBox FLOAT_BOX_3 = mockFloatBox(FLOAT_BOX_3_LEFT_X,
            FLOAT_BOX_3_TOP_Y, FLOAT_BOX_3_RIGHT_X, FLOAT_BOX_3_BOTTOM_Y);

    private final long MOST_RECENT_TIMESTAMP = 34L;

    private final UUID UUID = java.util.UUID.randomUUID();

    private FiniteLinearMovingProvider<FloatBox> finiteLinearMovingFloatBoxProvider;

    @BeforeEach
    void setUp() {
        VALUES_AT_TIMES.put(TIME_1, FLOAT_BOX_1);
        VALUES_AT_TIMES.put(TIME_2, FLOAT_BOX_2);
        VALUES_AT_TIMES.put(TIME_3, FLOAT_BOX_3);

        finiteLinearMovingFloatBoxProvider = new FiniteLinearMovingFloatBoxProvider(
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
        assertEquals(FLOAT_BOX_1, finiteLinearMovingFloatBoxProvider.provide(TIME_1 - 1));
        assertEquals(FLOAT_BOX_1, finiteLinearMovingFloatBoxProvider.provide(TIME_1));
        assertEquals(FLOAT_BOX_3, finiteLinearMovingFloatBoxProvider.provide(TIME_3));
        assertEquals(FLOAT_BOX_3, finiteLinearMovingFloatBoxProvider.provide(TIME_3 + 1));
    }

    @Test
    void testProvideInterpolatedValue() {
        var timeAfterTime1 = 50;
        var timestamp = TIME_1 + timeAfterTime1;
        var distanceBetweenTimes = TIME_2 - TIME_1;
        var time2Weight = timeAfterTime1 / (float) distanceBetweenTimes;
        var time1Weight = 1f - time2Weight;

        var weightedFloatBox1LeftX = FLOAT_BOX_1_LEFT_X * time1Weight;
        var weightedFloatBox1TopY = FLOAT_BOX_1_TOP_Y * time1Weight;
        var weightedFloatBox1RightX = FLOAT_BOX_1_RIGHT_X * time1Weight;
        var weightedFloatBox1BottomY = FLOAT_BOX_1_BOTTOM_Y * time1Weight;

        var weightedFloatBox2LeftX = FLOAT_BOX_2_LEFT_X * time2Weight;
        var weightedFloatBox2TopY = FLOAT_BOX_2_TOP_Y * time2Weight;
        var weightedFloatBox2RightX = FLOAT_BOX_2_RIGHT_X * time2Weight;
        var weightedFloatBox2BottomY = FLOAT_BOX_2_BOTTOM_Y * time2Weight;

        var interpolatedFloatBox = mockFloatBox(
                weightedFloatBox1LeftX + weightedFloatBox2LeftX,
                weightedFloatBox1TopY + weightedFloatBox2TopY,
                weightedFloatBox1RightX + weightedFloatBox2RightX,
                weightedFloatBox1BottomY + weightedFloatBox2BottomY);

        var provided = finiteLinearMovingFloatBoxProvider.provide(timestamp);

        assertEquals(interpolatedFloatBox.leftX(), provided.leftX());
        assertEquals(interpolatedFloatBox.topY(), provided.topY());
        assertEquals(interpolatedFloatBox.rightX(), provided.rightX());
        assertEquals(interpolatedFloatBox.bottomY(), provided.bottomY());
    }

    @Test
    void testPausedTimestamp() {
        var pausedTimestamp = 12L;
        var pausedFiniteLinearMovingFloatBoxProvider =
                new FiniteLinearMovingFloatBoxProvider(FLOAT_BOX_FACTORY, UUID, VALUES_AT_TIMES,
                        pausedTimestamp, MOST_RECENT_TIMESTAMP);

        assertEquals(pausedTimestamp,
                (long) pausedFiniteLinearMovingFloatBoxProvider.pausedTimestamp());
    }

    @Test
    void testProvideWhenPaused() {
        finiteLinearMovingFloatBoxProvider.reportPause(TIME_1);

        assertEquals(FLOAT_BOX_1, finiteLinearMovingFloatBoxProvider.provide(123123123L));
    }

    @Test
    void testProvideOrReportPauseOrUnpauseWithInvalidTimestampAndMostRecentTimestamp() {
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingFloatBoxProvider.provide(MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingFloatBoxProvider.reportPause(MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingFloatBoxProvider.reportUnpause(MOST_RECENT_TIMESTAMP - 1));

        finiteLinearMovingFloatBoxProvider.provide(MOST_RECENT_TIMESTAMP + 1);

        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingFloatBoxProvider.provide(MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingFloatBoxProvider.reportPause(MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingFloatBoxProvider.reportUnpause(MOST_RECENT_TIMESTAMP));
        assertEquals(MOST_RECENT_TIMESTAMP + 1,
                (long) finiteLinearMovingFloatBoxProvider.mostRecentTimestamp());

        finiteLinearMovingFloatBoxProvider.reportPause(MOST_RECENT_TIMESTAMP + 2);

        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingFloatBoxProvider.provide(MOST_RECENT_TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingFloatBoxProvider.reportPause(MOST_RECENT_TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingFloatBoxProvider.reportUnpause(MOST_RECENT_TIMESTAMP + 1));
        assertEquals(MOST_RECENT_TIMESTAMP + 2,
                (long) finiteLinearMovingFloatBoxProvider.mostRecentTimestamp());
        assertEquals(MOST_RECENT_TIMESTAMP + 2,
                (long) finiteLinearMovingFloatBoxProvider.pausedTimestamp());

        finiteLinearMovingFloatBoxProvider.reportUnpause(MOST_RECENT_TIMESTAMP + 3);

        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingFloatBoxProvider.provide(MOST_RECENT_TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingFloatBoxProvider.reportPause(MOST_RECENT_TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingFloatBoxProvider.reportUnpause(MOST_RECENT_TIMESTAMP + 2));
        assertEquals(MOST_RECENT_TIMESTAMP + 3,
                (long) finiteLinearMovingFloatBoxProvider.mostRecentTimestamp());
        assertNull(finiteLinearMovingFloatBoxProvider.pausedTimestamp());
    }

    @Test
    void testReportPauseWhilePausedOrViceVersa() {
        assertThrows(UnsupportedOperationException.class, () ->
                finiteLinearMovingFloatBoxProvider.reportUnpause(MOST_RECENT_TIMESTAMP));

        finiteLinearMovingFloatBoxProvider.reportPause(MOST_RECENT_TIMESTAMP);

        assertThrows(UnsupportedOperationException.class, () ->
                finiteLinearMovingFloatBoxProvider.reportPause(MOST_RECENT_TIMESTAMP));
    }

    @Test
    void testReportPauseAndUnpauseUpdatesTimestampsForValues() {
        var pauseDuration = 123123L;
        var timeAfterTime1 = 50;
        var timestamp = TIME_1 + timeAfterTime1;
        var distanceBetweenTimes = TIME_2 - TIME_1;
        var time2Weight = timeAfterTime1 / (float) distanceBetweenTimes;
        var time1Weight = 1f - time2Weight;

        var weightedFloatBox1LeftX = FLOAT_BOX_1_LEFT_X * time1Weight;
        var weightedFloatBox1TopY = FLOAT_BOX_1_TOP_Y * time1Weight;
        var weightedFloatBox1RightX = FLOAT_BOX_1_RIGHT_X * time1Weight;
        var weightedFloatBox1BottomY = FLOAT_BOX_1_BOTTOM_Y * time1Weight;

        var weightedFloatBox2LeftX = FLOAT_BOX_2_LEFT_X * time2Weight;
        var weightedFloatBox2TopY = FLOAT_BOX_2_TOP_Y * time2Weight;
        var weightedFloatBox2RightX = FLOAT_BOX_2_RIGHT_X * time2Weight;
        var weightedFloatBox2BottomY = FLOAT_BOX_2_BOTTOM_Y * time2Weight;

        var interpolatedFloatBox = mockFloatBox(
                weightedFloatBox1LeftX + weightedFloatBox2LeftX,
                weightedFloatBox1TopY + weightedFloatBox2TopY,
                weightedFloatBox1RightX + weightedFloatBox2RightX,
                weightedFloatBox1BottomY + weightedFloatBox2BottomY);

        finiteLinearMovingFloatBoxProvider.reportPause(MOST_RECENT_TIMESTAMP);
        finiteLinearMovingFloatBoxProvider.reportUnpause(MOST_RECENT_TIMESTAMP + pauseDuration);

        var valuesAtTimestampsRepresentation =
                finiteLinearMovingFloatBoxProvider.valuesAtTimestampsRepresentation();

        assertEquals(FLOAT_BOX_1, valuesAtTimestampsRepresentation.get(TIME_1 + pauseDuration));
        assertEquals(FLOAT_BOX_2, valuesAtTimestampsRepresentation.get(TIME_2 + pauseDuration));
        assertEquals(FLOAT_BOX_3, valuesAtTimestampsRepresentation.get(TIME_3 + pauseDuration));

        var providedAfterDuration = finiteLinearMovingFloatBoxProvider.provide(timestamp + pauseDuration);

        assertEquals(interpolatedFloatBox.leftX(), providedAfterDuration.leftX());
        assertEquals(interpolatedFloatBox.topY(), providedAfterDuration.topY());
        assertEquals(interpolatedFloatBox.rightX(), providedAfterDuration.rightX());
        assertEquals(interpolatedFloatBox.bottomY(), providedAfterDuration.bottomY());
    }

    @Test
    void testRepresentation() {
        assertEquals(VALUES_AT_TIMES, finiteLinearMovingFloatBoxProvider.representation());
        assertNotSame(VALUES_AT_TIMES, finiteLinearMovingFloatBoxProvider.representation());
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(FiniteLinearMovingProvider.class.getCanonicalName() + "<" +
                        FloatBox.class.getCanonicalName() + ">",
                finiteLinearMovingFloatBoxProvider.getInterfaceName());
    }
}
