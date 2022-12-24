package inaugural.soliloquy.graphics.test.unit.renderables.providers;

import inaugural.soliloquy.graphics.renderables.providers.FiniteSinusoidMovingFloatBoxProvider;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeFloatBox;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeFloatBoxFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.providers.FiniteSinusoidMovingProvider;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FiniteSinusoidMovingFloatBoxProviderTests {
    private final FakeFloatBoxFactory FLOAT_BOX_FACTORY = new FakeFloatBoxFactory();

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

    private final HashMap<Long, FloatBox> VALUES_AT_TIMES = new HashMap<Long, FloatBox>() {{
        put(TIME_1, FLOAT_BOX_1);
        put(TIME_2, FLOAT_BOX_2);
        put(TIME_3, FLOAT_BOX_3);
    }};

    /** @noinspection FieldCanBeLocal */
    private final float SHARPNESS_1 = 1f;
    private final float SHARPNESS_2 = 0.5f;

    private final ArrayList<Float> TRANSITION_SHARPNESSES = new ArrayList<Float>() {{
        add(SHARPNESS_1);
        add(SHARPNESS_2);
    }};

    private final long MOST_RECENT_TIMESTAMP = 34L;

    private final java.util.UUID UUID = java.util.UUID.randomUUID();

    private FiniteSinusoidMovingProvider<FloatBox> finiteSinusoidMovingFloatBoxProvider;

    @BeforeEach
    void setUp() {
        finiteSinusoidMovingFloatBoxProvider =
                new FiniteSinusoidMovingFloatBoxProvider(UUID, VALUES_AT_TIMES,
                        TRANSITION_SHARPNESSES,
                        null, MOST_RECENT_TIMESTAMP, FLOAT_BOX_FACTORY);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatBoxProvider(null, VALUES_AT_TIMES,
                        TRANSITION_SHARPNESSES, null, MOST_RECENT_TIMESTAMP, FLOAT_BOX_FACTORY));

        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatBoxProvider(UUID, null,
                        TRANSITION_SHARPNESSES, null, MOST_RECENT_TIMESTAMP, FLOAT_BOX_FACTORY));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatBoxProvider(UUID, new HashMap<>(),
                        TRANSITION_SHARPNESSES, null, MOST_RECENT_TIMESTAMP, FLOAT_BOX_FACTORY));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatBoxProvider(UUID,
                        new HashMap<Long, FloatBox>() {{
                            put(null, FLOAT_BOX_1);
                        }},
                        TRANSITION_SHARPNESSES, null, MOST_RECENT_TIMESTAMP, FLOAT_BOX_FACTORY));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatBoxProvider(UUID,
                        new HashMap<Long, FloatBox>() {{
                            put(TIME_1, null);
                        }},
                        TRANSITION_SHARPNESSES, null, MOST_RECENT_TIMESTAMP, FLOAT_BOX_FACTORY));

        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatBoxProvider(UUID, VALUES_AT_TIMES,
                        null, null, MOST_RECENT_TIMESTAMP, FLOAT_BOX_FACTORY));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatBoxProvider(UUID, VALUES_AT_TIMES,
                        new ArrayList<>(), null, MOST_RECENT_TIMESTAMP, FLOAT_BOX_FACTORY));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatBoxProvider(UUID, VALUES_AT_TIMES,
                        new ArrayList<Float>() {{
                            add(-0.001f);
                        }}, null, MOST_RECENT_TIMESTAMP, FLOAT_BOX_FACTORY));

        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatBoxProvider(UUID, VALUES_AT_TIMES,
                        TRANSITION_SHARPNESSES, MOST_RECENT_TIMESTAMP, null, FLOAT_BOX_FACTORY));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatBoxProvider(UUID, VALUES_AT_TIMES,
                        TRANSITION_SHARPNESSES, MOST_RECENT_TIMESTAMP + 1, MOST_RECENT_TIMESTAMP,
                        FLOAT_BOX_FACTORY));

        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatBoxProvider(UUID, VALUES_AT_TIMES,
                        TRANSITION_SHARPNESSES, null, MOST_RECENT_TIMESTAMP, null));
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
    void testGetArchetype() {
        assertNotNull(finiteSinusoidMovingFloatBoxProvider.getArchetype());
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
        long timeAfterTime1 = 50;
        long timestamp = TIME_1 + timeAfterTime1;
        long distanceBetweenTimes = TIME_2 - TIME_1;
        float time2Weight = timeAfterTime1 / (float) distanceBetweenTimes;
        double weightSine = (-Math.PI / 2f) + (Math.PI * time2Weight);
        double sineValue = Math.sin(weightSine);
        float percentToAdd = (float) ((sineValue + 1f) / 2f);

        float expectedLeftX =
                FLOAT_BOX_1_LEFT_X + ((FLOAT_BOX_2_LEFT_X - FLOAT_BOX_1_LEFT_X) * percentToAdd);
        float expectedTopY =
                FLOAT_BOX_1_TOP_Y + ((FLOAT_BOX_2_TOP_Y - FLOAT_BOX_1_TOP_Y) * percentToAdd);
        float expectedRightX =
                FLOAT_BOX_1_RIGHT_X + ((FLOAT_BOX_2_RIGHT_X - FLOAT_BOX_1_RIGHT_X) * percentToAdd);
        float expectedBottomY = FLOAT_BOX_1_BOTTOM_Y +
                ((FLOAT_BOX_2_BOTTOM_Y - FLOAT_BOX_1_BOTTOM_Y) * percentToAdd);

        FakeFloatBox expectedValue =
                new FakeFloatBox(expectedLeftX, expectedTopY, expectedRightX, expectedBottomY);

        FloatBox providedValue = finiteSinusoidMovingFloatBoxProvider.provide(timestamp);

        assertEquals(expectedValue.LeftX, providedValue.leftX());
        assertEquals(expectedValue.TopY, providedValue.topY());
        assertEquals(expectedValue.RightX, providedValue.rightX());
        assertEquals(expectedValue.BottomY, providedValue.bottomY());
    }

    @Test
    void testProvideInterpolatedValueWithSharpness() {
        long timeAfterTime2 = 50;
        long timestamp = TIME_2 + timeAfterTime2;
        long distanceBetweenTimes = TIME_3 - TIME_2;
        float time2Weight = timeAfterTime2 / (float) distanceBetweenTimes;
        double weightSine = (-Math.PI / 2f) + (Math.PI * time2Weight);
        double sineValue = Math.sin(weightSine);
        double sharpenedSineValue =
                (sineValue < 0f ? -1f : 1f) * Math.pow(Math.abs(sineValue), SHARPNESS_2);
        float percentToAdd = (float) ((sharpenedSineValue + 1f) / 2f);

        float expectedLeftX =
                FLOAT_BOX_2_LEFT_X + ((FLOAT_BOX_3_LEFT_X - FLOAT_BOX_2_LEFT_X) * percentToAdd);
        float expectedTopY =
                FLOAT_BOX_2_TOP_Y + ((FLOAT_BOX_3_TOP_Y - FLOAT_BOX_2_TOP_Y) * percentToAdd);
        float expectedRightX =
                FLOAT_BOX_2_RIGHT_X + ((FLOAT_BOX_3_RIGHT_X - FLOAT_BOX_2_RIGHT_X) * percentToAdd);
        float expectedBottomY =
                FLOAT_BOX_2_BOTTOM_Y +
                        ((FLOAT_BOX_3_BOTTOM_Y - FLOAT_BOX_2_BOTTOM_Y) * percentToAdd);

        FakeFloatBox expectedValue =
                new FakeFloatBox(expectedLeftX, expectedTopY, expectedRightX, expectedBottomY);

        FloatBox providedValue = finiteSinusoidMovingFloatBoxProvider.provide(timestamp);

        assertEquals(expectedValue.LeftX, providedValue.leftX());
        assertEquals(expectedValue.TopY, providedValue.topY());
        assertEquals(expectedValue.RightX, providedValue.rightX());
        assertEquals(expectedValue.BottomY, providedValue.bottomY());
    }

    @Test
    void testPausedTimestamp() {
        long pausedTimestamp = 12L;
        FiniteSinusoidMovingProvider<FloatBox> pausedFiniteSinusoidMovingFloatProvider =
                new FiniteSinusoidMovingFloatBoxProvider(UUID, VALUES_AT_TIMES,
                        TRANSITION_SHARPNESSES, pausedTimestamp, MOST_RECENT_TIMESTAMP,
                        FLOAT_BOX_FACTORY);

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
        long pauseDuration = 123123L;
        long timeAfterTime1 = 50;
        long timestamp = TIME_1 + timeAfterTime1;
        long distanceBetweenTimes = TIME_2 - TIME_1;
        float time2Weight = timeAfterTime1 / (float) distanceBetweenTimes;
        double weightSine = (-Math.PI / 2f) + (Math.PI * time2Weight);
        double sineValue = Math.sin(weightSine);
        float percentToAdd = (float) ((sineValue + 1f) / 2f);

        float expectedLeftX =
                FLOAT_BOX_1_LEFT_X + ((FLOAT_BOX_2_LEFT_X - FLOAT_BOX_1_LEFT_X) * percentToAdd);
        float expectedTopY =
                FLOAT_BOX_1_TOP_Y + ((FLOAT_BOX_2_TOP_Y - FLOAT_BOX_1_TOP_Y) * percentToAdd);
        float expectedRightX =
                FLOAT_BOX_1_RIGHT_X + ((FLOAT_BOX_2_RIGHT_X - FLOAT_BOX_1_RIGHT_X) * percentToAdd);
        float expectedBottomY = FLOAT_BOX_1_BOTTOM_Y +
                ((FLOAT_BOX_2_BOTTOM_Y - FLOAT_BOX_1_BOTTOM_Y) * percentToAdd);

        FakeFloatBox expectedValue =
                new FakeFloatBox(expectedLeftX, expectedTopY, expectedRightX, expectedBottomY);

        finiteSinusoidMovingFloatBoxProvider.reportPause(MOST_RECENT_TIMESTAMP);
        finiteSinusoidMovingFloatBoxProvider.reportUnpause(MOST_RECENT_TIMESTAMP + pauseDuration);

        Map<Long, FloatBox> valuesAtTimestampsRepresentation =
                finiteSinusoidMovingFloatBoxProvider.valuesAtTimestampsRepresentation();

        assertEquals(FLOAT_BOX_1, valuesAtTimestampsRepresentation.get(TIME_1 + pauseDuration));
        assertEquals(FLOAT_BOX_2, valuesAtTimestampsRepresentation.get(TIME_2 + pauseDuration));
        assertEquals(FLOAT_BOX_3, valuesAtTimestampsRepresentation.get(TIME_3 + pauseDuration));

        FloatBox providedValue =
                finiteSinusoidMovingFloatBoxProvider.provide(timestamp + pauseDuration);

        assertEquals(expectedValue.LeftX, providedValue.leftX());
        assertEquals(expectedValue.TopY, providedValue.topY());
        assertEquals(expectedValue.RightX, providedValue.rightX());
        assertEquals(expectedValue.BottomY, providedValue.bottomY());
    }

    @Test
    void testRepresentation() {
        assertEquals(VALUES_AT_TIMES, finiteSinusoidMovingFloatBoxProvider.representation());
        assertNotSame(VALUES_AT_TIMES, finiteSinusoidMovingFloatBoxProvider.representation());
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(FiniteSinusoidMovingProvider.class.getCanonicalName() + "<" +
                        FloatBox.class.getCanonicalName() + ">",
                finiteSinusoidMovingFloatBoxProvider.getInterfaceName());
    }
}
