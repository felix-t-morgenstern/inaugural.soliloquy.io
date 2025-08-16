package inaugural.soliloquy.io.test.unit.graphics.renderables.providers;

import inaugural.soliloquy.io.graphics.renderables.providers.FiniteLinearMovingFloatBoxProvider;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.providers.FiniteLinearMovingProvider;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomLong;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

@ExtendWith(MockitoExtension.class)
public class FiniteLinearMovingFloatBoxProviderTests {
    private final Map<Long, FloatBox> VALUES_AT_TIMES = mapOf();

    private final long TIME_1 = 100L;
    private final float FLOAT_BOX_1_LEFT_X = 0.111f;
    private final float FLOAT_BOX_1_TOP_Y = 0.222f;
    private final float FLOAT_BOX_1_RIGHT_X = 0.333f;
    private final float FLOAT_BOX_1_BOTTOM_Y = 0.444f;
    private final FloatBox FLOAT_BOX_1 = floatBoxOf(FLOAT_BOX_1_LEFT_X,
            FLOAT_BOX_1_TOP_Y, FLOAT_BOX_1_RIGHT_X, FLOAT_BOX_1_BOTTOM_Y);

    private final long TIME_2 = 300L;
    private final float FLOAT_BOX_2_LEFT_X = 0.555f;
    private final float FLOAT_BOX_2_TOP_Y = 0.666f;
    private final float FLOAT_BOX_2_RIGHT_X = 0.777f;
    private final float FLOAT_BOX_2_BOTTOM_Y = 0.888f;
    private final FloatBox FLOAT_BOX_2 = floatBoxOf(FLOAT_BOX_2_LEFT_X,
            FLOAT_BOX_2_TOP_Y, FLOAT_BOX_2_RIGHT_X, FLOAT_BOX_2_BOTTOM_Y);

    private final long TIME_3 = 500L;
    private final float FLOAT_BOX_3_LEFT_X = 0.123f;
    private final float FLOAT_BOX_3_TOP_Y = 0.234f;
    private final float FLOAT_BOX_3_RIGHT_X = 0.345f;
    private final float FLOAT_BOX_3_BOTTOM_Y = 0.456f;
    private final FloatBox FLOAT_BOX_3 = floatBoxOf(FLOAT_BOX_3_LEFT_X,
            FLOAT_BOX_3_TOP_Y, FLOAT_BOX_3_RIGHT_X, FLOAT_BOX_3_BOTTOM_Y);

    @Mock private TimestampValidator mockTimestampValidator;

    private final UUID UUID = java.util.UUID.randomUUID();


    private FiniteLinearMovingProvider<FloatBox> provider;

    @BeforeEach
    public void setUp() {
        VALUES_AT_TIMES.put(TIME_1, FLOAT_BOX_1);
        VALUES_AT_TIMES.put(TIME_2, FLOAT_BOX_2);
        VALUES_AT_TIMES.put(TIME_3, FLOAT_BOX_3);

        provider = new FiniteLinearMovingFloatBoxProvider(UUID, VALUES_AT_TIMES, null,
                mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteLinearMovingFloatBoxProvider(null, VALUES_AT_TIMES, null,
                        mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteLinearMovingFloatBoxProvider(UUID, null, null,
                        mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteLinearMovingFloatBoxProvider(UUID, VALUES_AT_TIMES, null, null));
    }

    @Test
    public void testProvideAtLowerExtremeIsStartingPoint() {
        var provided = provider.provide(TIME_1 - 1);

        assertEquals(FLOAT_BOX_1, provided);
    }

    @Test
    public void testProvideAtUpperExtremeIsEndingPoint() {
        var provided = provider.provide(TIME_3 + 1);

        assertEquals(FLOAT_BOX_3, provided);
    }

    @Test
    public void testProvideInterpolatedValue() {
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

        var interpolatedFloatBox = floatBoxOf(
                weightedFloatBox1LeftX + weightedFloatBox2LeftX,
                weightedFloatBox1TopY + weightedFloatBox2TopY,
                weightedFloatBox1RightX + weightedFloatBox2RightX,
                weightedFloatBox1BottomY + weightedFloatBox2BottomY);

        var provided = provider.provide(timestamp);

        assertEquals(interpolatedFloatBox, provided);
    }

    @Test
    public void testPausedTimestamp() {
        var pausedTimestamp = randomLong();
        when(mockTimestampValidator.mostRecentTimestamp()).thenReturn(pausedTimestamp);

        var pausedProvider =
                new FiniteLinearMovingFloatBoxProvider(UUID, VALUES_AT_TIMES, pausedTimestamp,
                        mockTimestampValidator);

        assertEquals(pausedTimestamp, (long) pausedProvider.pausedTimestamp());
    }

    @Test
    public void testProvideWhenPaused() {
        provider.reportPause(TIME_1);

        assertEquals(FLOAT_BOX_1, provider.provide(123123123L));
    }

    @Test
    public void testReportPauseWhilePausedOrViceVersa() {
        var timestamp = randomLong();
        when(mockTimestampValidator.mostRecentTimestamp()).thenReturn(timestamp);

        assertThrows(UnsupportedOperationException.class, () -> provider.reportUnpause(timestamp));

        provider.reportPause(timestamp);

        assertThrows(UnsupportedOperationException.class, () -> provider.reportPause(timestamp));
    }

    @Test
    public void testReportPauseAndUnpauseUpdatesTimestampsForValues() {
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

        var interpolatedFloatBox = floatBoxOf(
                weightedFloatBox1LeftX + weightedFloatBox2LeftX,
                weightedFloatBox1TopY + weightedFloatBox2TopY,
                weightedFloatBox1RightX + weightedFloatBox2RightX,
                weightedFloatBox1BottomY + weightedFloatBox2BottomY);

        provider.reportPause(timestamp);
        provider.reportUnpause(timestamp + pauseDuration);

        var valuesAtTimestampsRepresentation =
                provider.valuesAtTimestampsRepresentation();

        assertEquals(FLOAT_BOX_1, valuesAtTimestampsRepresentation.get(TIME_1 + pauseDuration));
        assertEquals(FLOAT_BOX_2, valuesAtTimestampsRepresentation.get(TIME_2 + pauseDuration));
        assertEquals(FLOAT_BOX_3, valuesAtTimestampsRepresentation.get(TIME_3 + pauseDuration));

        var providedAfterDuration =
                provider.provide(timestamp + pauseDuration);

        assertEquals(interpolatedFloatBox, providedAfterDuration);
    }

    @Test
    public void testRepresentation() {
        assertEquals(VALUES_AT_TIMES, provider.representation());
        assertNotSame(VALUES_AT_TIMES, provider.representation());
    }
}
