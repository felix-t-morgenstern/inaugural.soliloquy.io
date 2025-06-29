package inaugural.soliloquy.io.test.unit.graphics.renderables.providers;

import inaugural.soliloquy.io.graphics.renderables.providers.FiniteSinusoidMovingFloatProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.io.graphics.renderables.providers.FiniteSinusoidMovingProvider;

import java.util.List;
import java.util.Map;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static org.junit.jupiter.api.Assertions.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

public class FiniteSinusoidMovingFloatProviderTests {
    private final long TIME_1 = 100L;
    private final float VALUE_1 = 0.2f;
    private final long TIME_2 = 300L;
    private final float VALUE_2 = 0.4f;
    private final long TIME_3 = 500L;
    private final float VALUE_3 = 0.6f;

    private final Map<Long, Float> VALUES_AT_TIMES = mapOf(
            pairOf(TIME_1, VALUE_1),
            pairOf(TIME_2, VALUE_2),
            pairOf(TIME_3, VALUE_3)
    );

    /** @noinspection FieldCanBeLocal */
    private final float SHARPNESS_1 = 1f;
    private final float SHARPNESS_2 = 0.5f;

    private final List<Float> TRANSITION_SHARPNESSES = listOf(
            SHARPNESS_1,
            SHARPNESS_2
    );

    private final java.util.UUID UUID = java.util.UUID.randomUUID();

    private final long MOST_RECENT_TIMESTAMP = 34L;

    private FiniteSinusoidMovingProvider<Float> finiteSinusoidMovingFloatProvider;

    @BeforeEach
    public void setUp() {
        finiteSinusoidMovingFloatProvider =
                new FiniteSinusoidMovingFloatProvider(UUID, VALUES_AT_TIMES, TRANSITION_SHARPNESSES,
                        null, MOST_RECENT_TIMESTAMP);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatProvider(null, VALUES_AT_TIMES,
                        TRANSITION_SHARPNESSES, null, MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatProvider(UUID, null,
                        TRANSITION_SHARPNESSES, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatProvider(UUID, mapOf(),
                        TRANSITION_SHARPNESSES, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatProvider(UUID, mapOf(
                        pairOf(null, VALUE_1)
                ),
                        TRANSITION_SHARPNESSES, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatProvider(UUID, mapOf(
                        pairOf(TIME_1, null)
                ),
                        TRANSITION_SHARPNESSES, null, MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatProvider(UUID, VALUES_AT_TIMES,
                        null, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatProvider(UUID, VALUES_AT_TIMES,
                        listOf(), null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatProvider(UUID, VALUES_AT_TIMES,
                        listOf(-0.001f), null, MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatProvider(UUID, VALUES_AT_TIMES,
                        TRANSITION_SHARPNESSES, MOST_RECENT_TIMESTAMP, null));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatProvider(UUID, VALUES_AT_TIMES,
                        TRANSITION_SHARPNESSES, MOST_RECENT_TIMESTAMP + 1, MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testUuid() {
        assertSame(UUID, finiteSinusoidMovingFloatProvider.uuid());
    }

    @Test
    public void testMostRecentTimestamp() {
        assertEquals(MOST_RECENT_TIMESTAMP,
                (long) finiteSinusoidMovingFloatProvider.mostRecentTimestamp());
    }

    @Test
    public void testValuesAtTimestampsRepresentation() {
        assertNotNull(finiteSinusoidMovingFloatProvider.valuesAtTimestampsRepresentation());
        assertEquals(VALUES_AT_TIMES,
                finiteSinusoidMovingFloatProvider.valuesAtTimestampsRepresentation());
        assertNotSame(VALUES_AT_TIMES,
                finiteSinusoidMovingFloatProvider.valuesAtTimestampsRepresentation());
    }

    @Test
    public void testTransitionSharpnesses() {
        assertEquals(TRANSITION_SHARPNESSES,
                finiteSinusoidMovingFloatProvider.transitionSharpnesses());
        assertNotSame(TRANSITION_SHARPNESSES,
                finiteSinusoidMovingFloatProvider.transitionSharpnesses());
    }

    @Test
    public void testProvideAtExtremes() {
        assertEquals(VALUE_1, (float) finiteSinusoidMovingFloatProvider.provide(TIME_1 - 1));
        assertEquals(VALUE_1, (float) finiteSinusoidMovingFloatProvider.provide(TIME_1));
        assertEquals(VALUE_3, (float) finiteSinusoidMovingFloatProvider.provide(TIME_3));
        assertEquals(VALUE_3, (float) finiteSinusoidMovingFloatProvider.provide(TIME_3 + 1));
    }

    @Test
    public void testProvideInterpolatedValueWithNoSharpness() {
        long timeAfterTime1 = 50;
        long timestamp = TIME_1 + timeAfterTime1;
        long distanceBetweenTimes = TIME_2 - TIME_1;
        float distanceBetweenValues = VALUE_2 - VALUE_1;
        float time2Weight = timeAfterTime1 / (float) distanceBetweenTimes;
        double weightSine = (-Math.PI / 2f) + (Math.PI * time2Weight);
        double sineValue = Math.sin(weightSine);
        float percentToAdd = (float) ((sineValue + 1f) / 2f);
        float expectedValue = VALUE_1 + (distanceBetweenValues * percentToAdd);

        assertEquals(expectedValue, (float) finiteSinusoidMovingFloatProvider.provide(timestamp));
    }

    @Test
    public void testProvideInterpolatedValueWithSharpness() {
        long timeAfterTime2 = 50;
        long timestamp = TIME_2 + timeAfterTime2;
        long distanceBetweenTimes = TIME_3 - TIME_2;
        float distanceBetweenValues = VALUE_3 - VALUE_2;
        float time2Weight = timeAfterTime2 / (float) distanceBetweenTimes;
        double weightSine = (-Math.PI / 2f) + (Math.PI * time2Weight);
        double sineValue = Math.sin(weightSine);
        double sharpenedSineValue =
                (sineValue < 0f ? -1f : 1f) * Math.pow(Math.abs(sineValue), SHARPNESS_2);
        float percentToAdd = (float) ((sharpenedSineValue + 1f) / 2f);
        float expectedValue = VALUE_2 + (distanceBetweenValues * percentToAdd);

        assertEquals(expectedValue, (float) finiteSinusoidMovingFloatProvider.provide(timestamp));
    }

    @Test
    public void testPausedTimestamp() {
        long pausedTimestamp = 12L;
        FiniteSinusoidMovingProvider<Float> pausedFiniteSinusoidMovingFloatProvider =
                new FiniteSinusoidMovingFloatProvider(UUID, VALUES_AT_TIMES, TRANSITION_SHARPNESSES,
                        pausedTimestamp, MOST_RECENT_TIMESTAMP);

        assertEquals(pausedTimestamp,
                (long) pausedFiniteSinusoidMovingFloatProvider.pausedTimestamp());
    }

    @Test
    public void testProvideWhenPaused() {
        finiteSinusoidMovingFloatProvider.reportPause(TIME_1);

        assertEquals(VALUE_1, (float) finiteSinusoidMovingFloatProvider.provide(123123123L));
    }

    @Test
    public void testProvideOrReportPauseOrUnpauseWithInvalidTimestampAndMostRecentTimestamp() {
        assertThrows(IllegalArgumentException.class, () ->
                finiteSinusoidMovingFloatProvider.provide(MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                finiteSinusoidMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                finiteSinusoidMovingFloatProvider.reportUnpause(MOST_RECENT_TIMESTAMP - 1));

        finiteSinusoidMovingFloatProvider.provide(MOST_RECENT_TIMESTAMP + 1);

        assertThrows(IllegalArgumentException.class, () ->
                finiteSinusoidMovingFloatProvider.provide(MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                finiteSinusoidMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                finiteSinusoidMovingFloatProvider.reportUnpause(MOST_RECENT_TIMESTAMP));
        assertEquals(MOST_RECENT_TIMESTAMP + 1,
                (long) finiteSinusoidMovingFloatProvider.mostRecentTimestamp());

        finiteSinusoidMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP + 2);

        assertThrows(IllegalArgumentException.class, () ->
                finiteSinusoidMovingFloatProvider.provide(MOST_RECENT_TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                finiteSinusoidMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                finiteSinusoidMovingFloatProvider.reportUnpause(MOST_RECENT_TIMESTAMP + 1));
        assertEquals(MOST_RECENT_TIMESTAMP + 2,
                (long) finiteSinusoidMovingFloatProvider.mostRecentTimestamp());
        assertEquals(MOST_RECENT_TIMESTAMP + 2,
                (long) finiteSinusoidMovingFloatProvider.pausedTimestamp());

        finiteSinusoidMovingFloatProvider.reportUnpause(MOST_RECENT_TIMESTAMP + 3);

        assertThrows(IllegalArgumentException.class, () ->
                finiteSinusoidMovingFloatProvider.provide(MOST_RECENT_TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                finiteSinusoidMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                finiteSinusoidMovingFloatProvider.reportUnpause(MOST_RECENT_TIMESTAMP + 2));
        assertEquals(MOST_RECENT_TIMESTAMP + 3,
                (long) finiteSinusoidMovingFloatProvider.mostRecentTimestamp());
        assertNull(finiteSinusoidMovingFloatProvider.pausedTimestamp());
    }

    @Test
    public void testReportPauseWhilePausedOrViceVersa() {
        assertThrows(UnsupportedOperationException.class, () ->
                finiteSinusoidMovingFloatProvider.reportUnpause(MOST_RECENT_TIMESTAMP));

        finiteSinusoidMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP);

        assertThrows(UnsupportedOperationException.class, () ->
                finiteSinusoidMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testReportPauseAndUnpauseUpdatesTimestampsForValues() {
        long pauseDuration = 123123L;
        long timeAfterTime1 = 50;
        long timestamp = TIME_1 + timeAfterTime1;
        long distanceBetweenTimes = TIME_2 - TIME_1;
        float distanceBetweenValues = VALUE_2 - VALUE_1;
        float time2Weight = timeAfterTime1 / (float) distanceBetweenTimes;
        double weightSine = (-Math.PI / 2f) + (Math.PI * time2Weight);
        double sineValue = Math.sin(weightSine);
        float percentToAdd = (float) ((sineValue + 1f) / 2f);
        float expectedValue = VALUE_1 + (distanceBetweenValues * percentToAdd);

        finiteSinusoidMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP);
        finiteSinusoidMovingFloatProvider.reportUnpause(MOST_RECENT_TIMESTAMP + pauseDuration);

        Map<Long, Float> valuesAtTimestampsRepresentation =
                finiteSinusoidMovingFloatProvider.valuesAtTimestampsRepresentation();

        assertEquals(VALUE_1, (float) valuesAtTimestampsRepresentation.get(TIME_1 + pauseDuration));
        assertEquals(VALUE_2, (float) valuesAtTimestampsRepresentation.get(TIME_2 + pauseDuration));
        assertEquals(VALUE_3, (float) valuesAtTimestampsRepresentation.get(TIME_3 + pauseDuration));

        assertEquals(expectedValue,
                (float) finiteSinusoidMovingFloatProvider.provide(timestamp + pauseDuration));
    }

    @Test
    public void testRepresentation() {
        assertEquals(VALUES_AT_TIMES, finiteSinusoidMovingFloatProvider.representation());
        assertNotSame(VALUES_AT_TIMES, finiteSinusoidMovingFloatProvider.representation());
    }
}
