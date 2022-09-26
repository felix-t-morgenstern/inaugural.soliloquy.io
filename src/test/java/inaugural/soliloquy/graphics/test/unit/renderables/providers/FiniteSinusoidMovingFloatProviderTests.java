package inaugural.soliloquy.graphics.test.unit.renderables.providers;

import inaugural.soliloquy.graphics.renderables.providers.FiniteSinusoidMovingFloatProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.providers.FiniteSinusoidMovingProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FiniteSinusoidMovingFloatProviderTests {
    private final long TIME_1 = 100L;
    private final float VALUE_1 = 0.2f;
    private final long TIME_2 = 300L;
    private final float VALUE_2 = 0.4f;
    private final long TIME_3 = 500L;
    private final float VALUE_3 = 0.6f;

    private final HashMap<Long, Float> VALUES_AT_TIMES = new HashMap<Long, Float>() {{
        put(TIME_1, VALUE_1);
        put(TIME_2, VALUE_2);
        put(TIME_3, VALUE_3);
    }};

    /** @noinspection FieldCanBeLocal */
    private final float SHARPNESS_1 = 1f;
    private final float SHARPNESS_2 = 0.5f;

    private final ArrayList<Float> TRANSITION_SHARPNESSES = new ArrayList<Float>() {{
        add(SHARPNESS_1);
        add(SHARPNESS_2);
    }};

    private final java.util.UUID UUID = java.util.UUID.randomUUID();

    private final long MOST_RECENT_TIMESTAMP = 34L;

    private FiniteSinusoidMovingProvider<Float> _finiteSinusoidMovingFloatProvider;

    @BeforeEach
    void setUp() {
        _finiteSinusoidMovingFloatProvider =
                new FiniteSinusoidMovingFloatProvider(UUID, VALUES_AT_TIMES, TRANSITION_SHARPNESSES,
                        null, MOST_RECENT_TIMESTAMP);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatProvider(null, VALUES_AT_TIMES,
                        TRANSITION_SHARPNESSES, null, MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatProvider(UUID, null,
                        TRANSITION_SHARPNESSES, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatProvider(UUID, new HashMap<>(),
                        TRANSITION_SHARPNESSES, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatProvider(UUID, new HashMap<Long, Float>() {{
                    put(null, VALUE_1);
                }},
                        TRANSITION_SHARPNESSES, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatProvider(UUID, new HashMap<Long, Float>() {{
                    put(TIME_1, null);
                }},
                        TRANSITION_SHARPNESSES, null, MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatProvider(UUID, VALUES_AT_TIMES,
                        null, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatProvider(UUID, VALUES_AT_TIMES,
                        new ArrayList<>(), null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatProvider(UUID, VALUES_AT_TIMES,
                        new ArrayList<Float>() {{
                            add(-0.001f);
                        }}, null, MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatProvider(UUID, VALUES_AT_TIMES,
                        TRANSITION_SHARPNESSES, MOST_RECENT_TIMESTAMP, null));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteSinusoidMovingFloatProvider(UUID, VALUES_AT_TIMES,
                        TRANSITION_SHARPNESSES, MOST_RECENT_TIMESTAMP + 1, MOST_RECENT_TIMESTAMP));
    }

    @Test
    void testUuid() {
        assertSame(UUID, _finiteSinusoidMovingFloatProvider.uuid());
    }

    @Test
    void testMostRecentTimestamp() {
        assertEquals(MOST_RECENT_TIMESTAMP,
                (long) _finiteSinusoidMovingFloatProvider.mostRecentTimestamp());
    }

    @Test
    void testValuesAtTimestampsRepresentation() {
        assertNotNull(_finiteSinusoidMovingFloatProvider.valuesAtTimestampsRepresentation());
        assertEquals(VALUES_AT_TIMES,
                _finiteSinusoidMovingFloatProvider.valuesAtTimestampsRepresentation());
        assertNotSame(VALUES_AT_TIMES,
                _finiteSinusoidMovingFloatProvider.valuesAtTimestampsRepresentation());
    }

    @Test
    void testGetArchetype() {
        assertNotNull(_finiteSinusoidMovingFloatProvider.getArchetype());
    }

    @Test
    void testTransitionSharpnesses() {
        assertEquals(TRANSITION_SHARPNESSES,
                _finiteSinusoidMovingFloatProvider.transitionSharpnesses());
        assertNotSame(TRANSITION_SHARPNESSES,
                _finiteSinusoidMovingFloatProvider.transitionSharpnesses());
    }

    @Test
    void testProvideAtExtremes() {
        assertEquals(VALUE_1, (float) _finiteSinusoidMovingFloatProvider.provide(TIME_1 - 1));
        assertEquals(VALUE_1, (float) _finiteSinusoidMovingFloatProvider.provide(TIME_1));
        assertEquals(VALUE_3, (float) _finiteSinusoidMovingFloatProvider.provide(TIME_3));
        assertEquals(VALUE_3, (float) _finiteSinusoidMovingFloatProvider.provide(TIME_3 + 1));
    }

    @Test
    void testProvideInterpolatedValueWithNoSharpness() {
        long timeAfterTime1 = 50;
        long timestamp = TIME_1 + timeAfterTime1;
        long distanceBetweenTimes = TIME_2 - TIME_1;
        float distanceBetweenValues = VALUE_2 - VALUE_1;
        float time2Weight = timeAfterTime1 / (float) distanceBetweenTimes;
        double weightSine = (-Math.PI / 2f) + (Math.PI * time2Weight);
        double sineValue = Math.sin(weightSine);
        float percentToAdd = (float) ((sineValue + 1f) / 2f);
        float expectedValue = VALUE_1 + (distanceBetweenValues * percentToAdd);

        assertEquals(expectedValue, (float) _finiteSinusoidMovingFloatProvider.provide(timestamp));
    }

    @Test
    void testProvideInterpolatedValueWithSharpness() {
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

        assertEquals(expectedValue, (float) _finiteSinusoidMovingFloatProvider.provide(timestamp));
    }

    @Test
    void testPausedTimestamp() {
        long pausedTimestamp = 12L;
        FiniteSinusoidMovingProvider<Float> pausedFiniteSinusoidMovingFloatProvider =
                new FiniteSinusoidMovingFloatProvider(UUID, VALUES_AT_TIMES, TRANSITION_SHARPNESSES,
                        pausedTimestamp, MOST_RECENT_TIMESTAMP);

        assertEquals(pausedTimestamp,
                (long) pausedFiniteSinusoidMovingFloatProvider.pausedTimestamp());
    }

    @Test
    void testProvideWhenPaused() {
        _finiteSinusoidMovingFloatProvider.reportPause(TIME_1);

        assertEquals(VALUE_1, (float) _finiteSinusoidMovingFloatProvider.provide(123123123L));
    }

    @Test
    void testProvideOrReportPauseOrUnpauseWithInvalidTimestampAndMostRecentTimestamp() {
        assertThrows(IllegalArgumentException.class, () ->
                _finiteSinusoidMovingFloatProvider.provide(MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteSinusoidMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteSinusoidMovingFloatProvider.reportUnpause(MOST_RECENT_TIMESTAMP - 1));

        _finiteSinusoidMovingFloatProvider.provide(MOST_RECENT_TIMESTAMP + 1);

        assertThrows(IllegalArgumentException.class, () ->
                _finiteSinusoidMovingFloatProvider.provide(MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteSinusoidMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteSinusoidMovingFloatProvider.reportUnpause(MOST_RECENT_TIMESTAMP));
        assertEquals(MOST_RECENT_TIMESTAMP + 1,
                (long) _finiteSinusoidMovingFloatProvider.mostRecentTimestamp());

        _finiteSinusoidMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP + 2);

        assertThrows(IllegalArgumentException.class, () ->
                _finiteSinusoidMovingFloatProvider.provide(MOST_RECENT_TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteSinusoidMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteSinusoidMovingFloatProvider.reportUnpause(MOST_RECENT_TIMESTAMP + 1));
        assertEquals(MOST_RECENT_TIMESTAMP + 2,
                (long) _finiteSinusoidMovingFloatProvider.mostRecentTimestamp());
        assertEquals(MOST_RECENT_TIMESTAMP + 2,
                (long) _finiteSinusoidMovingFloatProvider.pausedTimestamp());

        _finiteSinusoidMovingFloatProvider.reportUnpause(MOST_RECENT_TIMESTAMP + 3);

        assertThrows(IllegalArgumentException.class, () ->
                _finiteSinusoidMovingFloatProvider.provide(MOST_RECENT_TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteSinusoidMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteSinusoidMovingFloatProvider.reportUnpause(MOST_RECENT_TIMESTAMP + 2));
        assertEquals(MOST_RECENT_TIMESTAMP + 3,
                (long) _finiteSinusoidMovingFloatProvider.mostRecentTimestamp());
        assertNull(_finiteSinusoidMovingFloatProvider.pausedTimestamp());
    }

    @Test
    void testReportPauseWhilePausedOrViceVersa() {
        assertThrows(IllegalArgumentException.class, () ->
                _finiteSinusoidMovingFloatProvider.reportUnpause(MOST_RECENT_TIMESTAMP));

        _finiteSinusoidMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP);

        assertThrows(IllegalArgumentException.class, () ->
                _finiteSinusoidMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP));
    }

    @Test
    void testReportPauseAndUnpauseUpdatesTimestampsForValues() {
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

        _finiteSinusoidMovingFloatProvider.reportPause(MOST_RECENT_TIMESTAMP);
        _finiteSinusoidMovingFloatProvider.reportUnpause(MOST_RECENT_TIMESTAMP + pauseDuration);

        Map<Long, Float> valuesAtTimestampsRepresentation =
                _finiteSinusoidMovingFloatProvider.valuesAtTimestampsRepresentation();

        assertEquals(VALUE_1, (float) valuesAtTimestampsRepresentation.get(TIME_1 + pauseDuration));
        assertEquals(VALUE_2, (float) valuesAtTimestampsRepresentation.get(TIME_2 + pauseDuration));
        assertEquals(VALUE_3, (float) valuesAtTimestampsRepresentation.get(TIME_3 + pauseDuration));

        assertEquals(expectedValue,
                (float) _finiteSinusoidMovingFloatProvider.provide(timestamp + pauseDuration));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(FiniteSinusoidMovingProvider.class.getCanonicalName() + "<" +
                        Float.class.getCanonicalName() + ">",
                _finiteSinusoidMovingFloatProvider.getInterfaceName());
    }
}
