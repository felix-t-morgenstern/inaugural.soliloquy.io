package inaugural.soliloquy.graphics.test.unit.renderables.providers;

import inaugural.soliloquy.graphics.renderables.providers.FiniteSinusoidMovingFloatBoxProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.graphics.renderables.providers.FiniteSinusoidMovingProvider;

import java.util.List;
import java.util.Map;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static org.junit.jupiter.api.Assertions.*;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

class FiniteSinusoidMovingFloatBoxProviderTests {
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

    private final Map<Long, FloatBox> VALUES_AT_TIMES = mapOf(
        pairOf(TIME_1, FLOAT_BOX_1),
        pairOf(TIME_2, FLOAT_BOX_2),
        pairOf(TIME_3, FLOAT_BOX_3)
    );

    /** @noinspection FieldCanBeLocal */
    private final float SHARPNESS_1 = 1f;
    private final float SHARPNESS_2 = 0.5f;

    private final List<Float> TRANSITION_SHARPNESSES = listOf(SHARPNESS_1, SHARPNESS_2);

    private final long MOST_RECENT_TIMESTAMP = 34L;

    private final java.util.UUID UUID = java.util.UUID.randomUUID();

    private FiniteSinusoidMovingProvider<FloatBox> finiteSinusoidMovingFloatBoxProvider;

    @BeforeEach
    void setUp() {
        finiteSinusoidMovingFloatBoxProvider =
                new FiniteSinusoidMovingFloatBoxProvider(UUID, VALUES_AT_TIMES,
                        TRANSITION_SHARPNESSES,
                        null, MOST_RECENT_TIMESTAMP);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatBoxProvider(null, VALUES_AT_TIMES,
                        TRANSITION_SHARPNESSES, null, MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatBoxProvider(UUID, null,
                        TRANSITION_SHARPNESSES, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatBoxProvider(UUID, mapOf(),
                        TRANSITION_SHARPNESSES, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatBoxProvider(UUID,
                        mapOf(pairOf(null, FLOAT_BOX_1)),
                        TRANSITION_SHARPNESSES, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatBoxProvider(UUID,
                        mapOf(pairOf(TIME_1, null)),
                        TRANSITION_SHARPNESSES, null, MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatBoxProvider(UUID, VALUES_AT_TIMES,
                        null, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatBoxProvider(UUID, VALUES_AT_TIMES,
                        listOf(), null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatBoxProvider(UUID, VALUES_AT_TIMES,
                        listOf(-0.001f), null, MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatBoxProvider(UUID, VALUES_AT_TIMES,
                        TRANSITION_SHARPNESSES, MOST_RECENT_TIMESTAMP, null));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatBoxProvider(UUID, VALUES_AT_TIMES,
                        TRANSITION_SHARPNESSES, MOST_RECENT_TIMESTAMP + 1, MOST_RECENT_TIMESTAMP));
    }

    @Test
    void testUuid() {
        assertSame(UUID, finiteSinusoidMovingFloatBoxProvider.uuid());
    }

    @Test
    void testMostRecentTimestamp() {
        assertEquals(MOST_RECENT_TIMESTAMP,
                (long) finiteSinusoidMovingFloatBoxProvider.mostRecentTimestamp());
    }

    @Test
    void testValuesAtTimestampsRepresentation() {
        assertNotNull(finiteSinusoidMovingFloatBoxProvider.valuesAtTimestampsRepresentation());
        assertEquals(VALUES_AT_TIMES,
                finiteSinusoidMovingFloatBoxProvider.valuesAtTimestampsRepresentation());
        assertNotSame(VALUES_AT_TIMES,
                finiteSinusoidMovingFloatBoxProvider.valuesAtTimestampsRepresentation());
    }

    @Test
    void testTransitionSharpnesses() {
        assertEquals(TRANSITION_SHARPNESSES,
                finiteSinusoidMovingFloatBoxProvider.transitionSharpnesses());
        assertNotSame(TRANSITION_SHARPNESSES,
                finiteSinusoidMovingFloatBoxProvider.transitionSharpnesses());
    }

    @Test
    void testProvideAtExtremes() {
        assertEquals(FLOAT_BOX_1, finiteSinusoidMovingFloatBoxProvider.provide(TIME_1 - 1));
        assertEquals(FLOAT_BOX_1, finiteSinusoidMovingFloatBoxProvider.provide(TIME_1));
        assertEquals(FLOAT_BOX_3, finiteSinusoidMovingFloatBoxProvider.provide(TIME_3));
        assertEquals(FLOAT_BOX_3, finiteSinusoidMovingFloatBoxProvider.provide(TIME_3 + 1));
    }

    @Test
    void testProvideInterpolatedValueWithNoSharpness() {
        var timeAfterTime1 = 50;
        var timestamp = TIME_1 + timeAfterTime1;
        var distanceBetweenTimes = TIME_2 - TIME_1;
        var time2Weight = timeAfterTime1 / (float) distanceBetweenTimes;
        var weightSine = (-Math.PI / 2f) + (Math.PI * time2Weight);
        var sineValue = Math.sin(weightSine);
        var percentToAdd = (float) ((sineValue + 1f) / 2f);

        var expectedLeftX =
                FLOAT_BOX_1_LEFT_X + ((FLOAT_BOX_2_LEFT_X - FLOAT_BOX_1_LEFT_X) * percentToAdd);
        var expectedTopY =
                FLOAT_BOX_1_TOP_Y + ((FLOAT_BOX_2_TOP_Y - FLOAT_BOX_1_TOP_Y) * percentToAdd);
        var expectedRightX =
                FLOAT_BOX_1_RIGHT_X + ((FLOAT_BOX_2_RIGHT_X - FLOAT_BOX_1_RIGHT_X) * percentToAdd);
        var expectedBottomY = FLOAT_BOX_1_BOTTOM_Y +
                ((FLOAT_BOX_2_BOTTOM_Y - FLOAT_BOX_1_BOTTOM_Y) * percentToAdd);

        var expectedValue =
                floatBoxOf(expectedLeftX, expectedTopY, expectedRightX, expectedBottomY);

        var providedValue = finiteSinusoidMovingFloatBoxProvider.provide(timestamp);

        assertEquals(expectedValue.LEFT_X, providedValue.LEFT_X);
        assertEquals(expectedValue.TOP_Y, providedValue.TOP_Y);
        assertEquals(expectedValue.RIGHT_X, providedValue.RIGHT_X);
        assertEquals(expectedValue.BOTTOM_Y, providedValue.BOTTOM_Y);
    }

    @Test
    void testProvideInterpolatedValueWithSharpness() {
        var timeAfterTime2 = 50;
        var timestamp = TIME_2 + timeAfterTime2;
        var distanceBetweenTimes = TIME_3 - TIME_2;
        var time2Weight = timeAfterTime2 / (float) distanceBetweenTimes;
        var weightSine = (-Math.PI / 2f) + (Math.PI * time2Weight);
        var sineValue = Math.sin(weightSine);
        var sharpenedSineValue =
                -1f * Math.pow(Math.abs(sineValue), SHARPNESS_2);
        var percentToAdd = (float) ((sharpenedSineValue + 1f) / 2f);

        var expectedLeftX =
                FLOAT_BOX_2_LEFT_X + ((FLOAT_BOX_3_LEFT_X - FLOAT_BOX_2_LEFT_X) * percentToAdd);
        var expectedTopY =
                FLOAT_BOX_2_TOP_Y + ((FLOAT_BOX_3_TOP_Y - FLOAT_BOX_2_TOP_Y) * percentToAdd);
        var expectedRightX =
                FLOAT_BOX_2_RIGHT_X + ((FLOAT_BOX_3_RIGHT_X - FLOAT_BOX_2_RIGHT_X) * percentToAdd);
        var expectedBottomY =
                FLOAT_BOX_2_BOTTOM_Y +
                        ((FLOAT_BOX_3_BOTTOM_Y - FLOAT_BOX_2_BOTTOM_Y) * percentToAdd);

        var expectedValue =
                floatBoxOf(expectedLeftX, expectedTopY, expectedRightX, expectedBottomY);

        var providedValue = finiteSinusoidMovingFloatBoxProvider.provide(timestamp);

        assertEquals(expectedValue.LEFT_X, providedValue.LEFT_X);
        assertEquals(expectedValue.TOP_Y, providedValue.TOP_Y);
        assertEquals(expectedValue.RIGHT_X, providedValue.RIGHT_X);
        assertEquals(expectedValue.BOTTOM_Y, providedValue.BOTTOM_Y);
    }

    @Test
    void testPausedTimestamp() {
        var pausedTimestamp = 12L;
        var pausedFiniteSinusoidMovingFloatProvider =
                new FiniteSinusoidMovingFloatBoxProvider(UUID, VALUES_AT_TIMES,
                        TRANSITION_SHARPNESSES, pausedTimestamp, MOST_RECENT_TIMESTAMP);

        assertEquals(pausedTimestamp,
                (long) pausedFiniteSinusoidMovingFloatProvider.pausedTimestamp());
    }

    @Test
    void testProvideWhenPaused() {
        finiteSinusoidMovingFloatBoxProvider.reportPause(TIME_1);

        assertEquals(FLOAT_BOX_1, finiteSinusoidMovingFloatBoxProvider.provide(123123123L));
    }

    @Test
    void testProvideOrReportPauseOrUnpauseWithInvalidTimestampAndMostRecentTimestamp() {
        assertThrows(IllegalArgumentException.class, () ->
                finiteSinusoidMovingFloatBoxProvider.provide(MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                finiteSinusoidMovingFloatBoxProvider.reportPause(MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                finiteSinusoidMovingFloatBoxProvider.reportUnpause(MOST_RECENT_TIMESTAMP - 1));

        finiteSinusoidMovingFloatBoxProvider.provide(MOST_RECENT_TIMESTAMP + 1);

        assertThrows(IllegalArgumentException.class, () ->
                finiteSinusoidMovingFloatBoxProvider.provide(MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                finiteSinusoidMovingFloatBoxProvider.reportPause(MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                finiteSinusoidMovingFloatBoxProvider.reportUnpause(MOST_RECENT_TIMESTAMP));
        assertEquals(MOST_RECENT_TIMESTAMP + 1,
                (long) finiteSinusoidMovingFloatBoxProvider.mostRecentTimestamp());

        finiteSinusoidMovingFloatBoxProvider.reportPause(MOST_RECENT_TIMESTAMP + 2);

        assertThrows(IllegalArgumentException.class, () ->
                finiteSinusoidMovingFloatBoxProvider.provide(MOST_RECENT_TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                finiteSinusoidMovingFloatBoxProvider.reportPause(MOST_RECENT_TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                finiteSinusoidMovingFloatBoxProvider.reportUnpause(MOST_RECENT_TIMESTAMP + 1));
        assertEquals(MOST_RECENT_TIMESTAMP + 2,
                (long) finiteSinusoidMovingFloatBoxProvider.mostRecentTimestamp());
        assertEquals(MOST_RECENT_TIMESTAMP + 2,
                (long) finiteSinusoidMovingFloatBoxProvider.pausedTimestamp());

        finiteSinusoidMovingFloatBoxProvider.reportUnpause(MOST_RECENT_TIMESTAMP + 3);

        assertThrows(IllegalArgumentException.class, () ->
                finiteSinusoidMovingFloatBoxProvider.provide(MOST_RECENT_TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                finiteSinusoidMovingFloatBoxProvider.reportPause(MOST_RECENT_TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                finiteSinusoidMovingFloatBoxProvider.reportUnpause(MOST_RECENT_TIMESTAMP + 2));
        assertEquals(MOST_RECENT_TIMESTAMP + 3,
                (long) finiteSinusoidMovingFloatBoxProvider.mostRecentTimestamp());
        assertNull(finiteSinusoidMovingFloatBoxProvider.pausedTimestamp());
    }

    @Test
    void testReportPauseWhilePausedOrViceVersa() {
        assertThrows(UnsupportedOperationException.class, () ->
                finiteSinusoidMovingFloatBoxProvider.reportUnpause(MOST_RECENT_TIMESTAMP));

        finiteSinusoidMovingFloatBoxProvider.reportPause(MOST_RECENT_TIMESTAMP);

        assertThrows(UnsupportedOperationException.class, () ->
                finiteSinusoidMovingFloatBoxProvider.reportPause(MOST_RECENT_TIMESTAMP));
    }

    @Test
    void testReportPauseAndUnpauseUpdatesTimestampsForValues() {
        var pauseDuration = 123123L;
        var timeAfterTime1 = 50;
        var timestamp = TIME_1 + timeAfterTime1;
        var distanceBetweenTimes = TIME_2 - TIME_1;
        var time2Weight = timeAfterTime1 / (float) distanceBetweenTimes;
        var weightSine = (-Math.PI / 2f) + (Math.PI * time2Weight);
        var sineValue = Math.sin(weightSine);
        var percentToAdd = (float) ((sineValue + 1f) / 2f);

        var expectedLeftX =
                FLOAT_BOX_1_LEFT_X + ((FLOAT_BOX_2_LEFT_X - FLOAT_BOX_1_LEFT_X) * percentToAdd);
        var expectedTopY =
                FLOAT_BOX_1_TOP_Y + ((FLOAT_BOX_2_TOP_Y - FLOAT_BOX_1_TOP_Y) * percentToAdd);
        var expectedRightX =
                FLOAT_BOX_1_RIGHT_X + ((FLOAT_BOX_2_RIGHT_X - FLOAT_BOX_1_RIGHT_X) * percentToAdd);
        var expectedBottomY = FLOAT_BOX_1_BOTTOM_Y +
                ((FLOAT_BOX_2_BOTTOM_Y - FLOAT_BOX_1_BOTTOM_Y) * percentToAdd);

        var expectedValue = floatBoxOf(expectedLeftX, expectedTopY, expectedRightX, expectedBottomY);

        finiteSinusoidMovingFloatBoxProvider.reportPause(MOST_RECENT_TIMESTAMP);
        finiteSinusoidMovingFloatBoxProvider.reportUnpause(MOST_RECENT_TIMESTAMP + pauseDuration);

        var valuesAtTimestampsRepresentation =
                finiteSinusoidMovingFloatBoxProvider.valuesAtTimestampsRepresentation();

        assertEquals(FLOAT_BOX_1, valuesAtTimestampsRepresentation.get(TIME_1 + pauseDuration));
        assertEquals(FLOAT_BOX_2, valuesAtTimestampsRepresentation.get(TIME_2 + pauseDuration));
        assertEquals(FLOAT_BOX_3, valuesAtTimestampsRepresentation.get(TIME_3 + pauseDuration));

        var providedValue =
                finiteSinusoidMovingFloatBoxProvider.provide(timestamp + pauseDuration);

        assertEquals(expectedValue.LEFT_X, providedValue.LEFT_X);
        assertEquals(expectedValue.TOP_Y, providedValue.TOP_Y);
        assertEquals(expectedValue.RIGHT_X, providedValue.RIGHT_X);
        assertEquals(expectedValue.BOTTOM_Y, providedValue.BOTTOM_Y);
    }

    @Test
    void testRepresentation() {
        assertEquals(VALUES_AT_TIMES, finiteSinusoidMovingFloatBoxProvider.representation());
        assertNotSame(VALUES_AT_TIMES, finiteSinusoidMovingFloatBoxProvider.representation());
    }
}
