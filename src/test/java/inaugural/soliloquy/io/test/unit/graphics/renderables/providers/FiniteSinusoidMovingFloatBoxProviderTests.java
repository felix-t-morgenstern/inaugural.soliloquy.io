package inaugural.soliloquy.io.test.unit.graphics.renderables.providers;

import inaugural.soliloquy.io.graphics.renderables.providers.FiniteSinusoidMovingFloatBoxProvider;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.providers.FiniteSinusoidMovingProvider;

import java.util.Map;

import static inaugural.soliloquy.tools.collections.Collections.arrayFloats;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomLong;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

@ExtendWith(MockitoExtension.class)
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

    private final float[] TRANSITION_SHARPNESSES = arrayFloats(SHARPNESS_1, SHARPNESS_2);

    private final java.util.UUID UUID = java.util.UUID.randomUUID();

    @Mock private TimestampValidator mockTimestampValidator;

    private FiniteSinusoidMovingProvider<FloatBox> provider;

    @BeforeEach
    void setUp() {
        provider =
                new FiniteSinusoidMovingFloatBoxProvider(UUID, VALUES_AT_TIMES,
                        TRANSITION_SHARPNESSES,
                        null, mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatBoxProvider(null, VALUES_AT_TIMES,
                        TRANSITION_SHARPNESSES, null, mockTimestampValidator));

        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatBoxProvider(UUID, null,
                        TRANSITION_SHARPNESSES, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatBoxProvider(UUID, mapOf(),
                        TRANSITION_SHARPNESSES, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatBoxProvider(UUID,
                        mapOf(pairOf(null, FLOAT_BOX_1)),
                        TRANSITION_SHARPNESSES, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatBoxProvider(UUID,
                        mapOf(pairOf(TIME_1, null)),
                        TRANSITION_SHARPNESSES, null, mockTimestampValidator));

        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatBoxProvider(UUID, VALUES_AT_TIMES,
                        null, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatBoxProvider(UUID, VALUES_AT_TIMES,
                        arrayFloats(), null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatBoxProvider(UUID, VALUES_AT_TIMES,
                        arrayFloats(-0.001f), null, mockTimestampValidator));

        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatBoxProvider(UUID, VALUES_AT_TIMES,
                        TRANSITION_SHARPNESSES, null, null));
    }

    @Test
    public void testUuid() {
        assertSame(UUID, provider.uuid());
    }

    @Test
    public void testValuesAtTimestampsRepresentation() {
        assertNotNull(provider.valuesAtTimestampsRepresentation());
        assertEquals(VALUES_AT_TIMES, provider.valuesAtTimestampsRepresentation());
        assertNotSame(VALUES_AT_TIMES, provider.valuesAtTimestampsRepresentation());
    }

    @Test
    public void testTransitionSharpnesses() {
        assertEquals(TRANSITION_SHARPNESSES, provider.transitionSharpnesses());
    }

    @Test
    public void testProvideAtExtremes() {
        assertEquals(FLOAT_BOX_1, provider.provide(TIME_1 - 1));
        assertEquals(FLOAT_BOX_1, provider.provide(TIME_1));
        assertEquals(FLOAT_BOX_3, provider.provide(TIME_3));
        assertEquals(FLOAT_BOX_3, provider.provide(TIME_3 + 1));
    }

    @Test
    public void testProvideInterpolatedValueWithNoSharpness() {
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

        var providedValue = provider.provide(timestamp);

        assertEquals(expectedValue.LEFT_X, providedValue.LEFT_X);
        assertEquals(expectedValue.TOP_Y, providedValue.TOP_Y);
        assertEquals(expectedValue.RIGHT_X, providedValue.RIGHT_X);
        assertEquals(expectedValue.BOTTOM_Y, providedValue.BOTTOM_Y);
    }

    @Test
    public void testProvideInterpolatedValueWithSharpness() {
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

        var providedValue = provider.provide(timestamp);

        assertEquals(expectedValue.LEFT_X, providedValue.LEFT_X);
        assertEquals(expectedValue.TOP_Y, providedValue.TOP_Y);
        assertEquals(expectedValue.RIGHT_X, providedValue.RIGHT_X);
        assertEquals(expectedValue.BOTTOM_Y, providedValue.BOTTOM_Y);
    }

    @Test
    public void testPausedTimestamp() {
        var pausedTimestamp = randomLong();
        when(mockTimestampValidator.mostRecentTimestamp()).thenReturn(pausedTimestamp);

        var pausedProvider = new FiniteSinusoidMovingFloatBoxProvider(UUID, VALUES_AT_TIMES,
                TRANSITION_SHARPNESSES, pausedTimestamp, mockTimestampValidator);

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

        provider.reportPause(timestamp);
        provider.reportUnpause(timestamp + pauseDuration);

        var valuesAtTimestampsRepresentation =
                provider.valuesAtTimestampsRepresentation();

        assertEquals(FLOAT_BOX_1, valuesAtTimestampsRepresentation.get(TIME_1 + pauseDuration));
        assertEquals(FLOAT_BOX_2, valuesAtTimestampsRepresentation.get(TIME_2 + pauseDuration));
        assertEquals(FLOAT_BOX_3, valuesAtTimestampsRepresentation.get(TIME_3 + pauseDuration));

        var providedValue =
                provider.provide(timestamp + pauseDuration);

        assertEquals(expectedValue.LEFT_X, providedValue.LEFT_X);
        assertEquals(expectedValue.TOP_Y, providedValue.TOP_Y);
        assertEquals(expectedValue.RIGHT_X, providedValue.RIGHT_X);
        assertEquals(expectedValue.BOTTOM_Y, providedValue.BOTTOM_Y);
    }

    @Test
    public void testRepresentation() {
        assertEquals(VALUES_AT_TIMES, provider.representation());
        assertNotSame(VALUES_AT_TIMES, provider.representation());
    }
}
