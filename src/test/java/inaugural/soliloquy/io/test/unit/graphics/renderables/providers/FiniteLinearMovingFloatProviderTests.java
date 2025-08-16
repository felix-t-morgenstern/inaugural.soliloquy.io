package inaugural.soliloquy.io.test.unit.graphics.renderables.providers;

import inaugural.soliloquy.io.graphics.renderables.providers.FiniteLinearMovingFloatProvider;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.graphics.renderables.providers.FiniteLinearMovingProvider;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomLong;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

@ExtendWith(MockitoExtension.class)
public class FiniteLinearMovingFloatProviderTests {
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

    @Mock private TimestampValidator mockTimestampValidator;

    private final UUID UUID = java.util.UUID.randomUUID();

    private FiniteLinearMovingProvider<Float> provider;

    @BeforeEach
    public void setUp() {
        provider = new FiniteLinearMovingFloatProvider(UUID,
                VALUES_AT_TIMES, null, mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteLinearMovingFloatProvider(null, VALUES_AT_TIMES, null,
                        mockTimestampValidator));

        assertThrows(IllegalArgumentException.class,
                () -> new FiniteLinearMovingFloatProvider(UUID, null, null,
                        mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteLinearMovingFloatProvider(UUID, mapOf(), null,
                        mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteLinearMovingFloatProvider(UUID, mapOf(pairOf(null, VALUE_1)), null,
                        null));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteLinearMovingFloatProvider(UUID, mapOf(pairOf(TIME_1, null)), null,
                        mockTimestampValidator));

        assertThrows(IllegalArgumentException.class,
                () -> new FiniteLinearMovingFloatProvider(UUID, VALUES_AT_TIMES, null, null));
    }

    @Test
    public void testUuid() {
        assertSame(UUID, provider.uuid());
    }

    @Test
    public void testValuesAtTimestampsRepresentation() {
        assertNotNull(provider.valuesAtTimestampsRepresentation());
        assertEquals(VALUES_AT_TIMES,
                provider.valuesAtTimestampsRepresentation());
        assertNotSame(VALUES_AT_TIMES,
                provider.valuesAtTimestampsRepresentation());
    }

    @Test
    public void testProvideAtExtremes() {
        assertEquals(VALUE_1, (float) provider.provide(TIME_1 - 1));
        assertEquals(VALUE_1, (float) provider.provide(TIME_1));
        assertEquals(VALUE_3, (float) provider.provide(TIME_3));
        assertEquals(VALUE_3, (float) provider.provide(TIME_3 + 1));
    }

    @Test
    public void testProvideInterpolatedValue() {
        long timeAfterTime1 = 50;
        long timestamp = TIME_1 + timeAfterTime1;
        long distanceBetweenTimes = TIME_2 - TIME_1;
        float time2Weight = timeAfterTime1 / (float) distanceBetweenTimes;
        float time1Weight = 1f - time2Weight;
        float weightedValue1 = VALUE_1 * time1Weight;
        float weightedValue2 = VALUE_2 * time2Weight;

        assertEquals(weightedValue1 + weightedValue2,
                (float) provider.provide(timestamp));
    }

    @Test
    public void testPausedTimestamp() {
        var pausedTimestamp = randomLong();
        when(mockTimestampValidator.mostRecentTimestamp()).thenReturn(pausedTimestamp);

        var pausedProvider = new FiniteLinearMovingFloatProvider(UUID, VALUES_AT_TIMES, pausedTimestamp, mockTimestampValidator);

        assertEquals(pausedTimestamp, (long) pausedProvider.pausedTimestamp());
    }

    @Test
    public void testProvideWhenPaused() {
        provider.reportPause(TIME_1);

        assertEquals(VALUE_1, (float) provider.provide(123123123L));
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
        var weightedValue1 = VALUE_1 * time1Weight;
        var weightedValue2 = VALUE_2 * time2Weight;

        provider.reportPause(timestamp);
        provider.reportUnpause(timestamp + pauseDuration);

        var valuesAtTimestampsRepresentation =
                provider.valuesAtTimestampsRepresentation();

        assertEquals(VALUE_1, (float) valuesAtTimestampsRepresentation.get(TIME_1 + pauseDuration));
        assertEquals(VALUE_2, (float) valuesAtTimestampsRepresentation.get(TIME_2 + pauseDuration));
        assertEquals(VALUE_3, (float) valuesAtTimestampsRepresentation.get(TIME_3 + pauseDuration));

        assertEquals(weightedValue1 + weightedValue2,
                (float) provider.provide(timestamp + pauseDuration));
    }

    @Test
    public void testRepresentation() {
        assertEquals(VALUES_AT_TIMES, provider.representation());
        assertNotSame(VALUES_AT_TIMES, provider.representation());
    }
}
