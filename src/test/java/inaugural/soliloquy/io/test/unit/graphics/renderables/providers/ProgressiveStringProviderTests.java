package inaugural.soliloquy.io.test.unit.graphics.renderables.providers;

import inaugural.soliloquy.io.graphics.renderables.providers.ProgressiveStringProvider;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

@ExtendWith(MockitoExtension.class)
public class ProgressiveStringProviderTests {
    private final String STRING = randomString();
    private final int STRING_LENGTH = STRING.length();
    // NB: Values here are divided by 2 to prevent overflow issues in tests
    private final long TIME_TO_COMPLETE = randomLongWithInclusiveFloor(1L) / 2;
    private final long START_TIMESTAMP = randomLongWithInclusiveCeiling(TIME_TO_COMPLETE - 1) / 2;
    private final long CHARACTER_LENGTH_WITHIN_PERIOD =
            (long) (TIME_TO_COMPLETE * (1 / (double) STRING_LENGTH));
    private final Long PAUSED_TIMESTAMP = randomLongWithInclusiveCeiling(START_TIMESTAMP - 1);

    private static final UUID UUID = java.util.UUID.randomUUID();

    @Mock private TimestampValidator mockTimestampValidator;

    private ProviderAtTime<String> provider;

    @BeforeEach
    public void setUp() {
        provider =
                new ProgressiveStringProvider(UUID, STRING, START_TIMESTAMP, TIME_TO_COMPLETE, null,
                        mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new ProgressiveStringProvider(null, STRING, START_TIMESTAMP, TIME_TO_COMPLETE,
                        PAUSED_TIMESTAMP, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new ProgressiveStringProvider(UUID, null, START_TIMESTAMP, TIME_TO_COMPLETE,
                        PAUSED_TIMESTAMP, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new ProgressiveStringProvider(UUID, STRING, START_TIMESTAMP, 0,
                        PAUSED_TIMESTAMP, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new ProgressiveStringProvider(UUID, STRING, START_TIMESTAMP, TIME_TO_COMPLETE,
                        PAUSED_TIMESTAMP, null));
    }

    @Test
    public void testUuid() {
        assertSame(UUID, provider.uuid());
    }

    @Test
    public void testPausedTimestamp() {
        when(mockTimestampValidator.mostRecentTimestamp()).thenReturn(PAUSED_TIMESTAMP);

        provider.reportPause(PAUSED_TIMESTAMP);

        assertEquals(PAUSED_TIMESTAMP, provider.pausedTimestamp());
        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(PAUSED_TIMESTAMP);
    }

    @Test
    public void testProvide() {
        var numberOfCharactersToProvide = randomIntInRange(1, STRING_LENGTH - 1);
        var percentOfString = (numberOfCharactersToProvide / (double) STRING_LENGTH);
        var timestampWithinPeriod = (long) (TIME_TO_COMPLETE * percentOfString);
        var timestamp = timestampWithinPeriod + START_TIMESTAMP;

        // NB: I'm subtracting by a margin of error instead of 1, since the random numbers are in
        //     the quadrillions, and so subtracting by 1 to determine whether the provider returns
        //     n - 1 characters is impossible, since a difference of 1 will be washed out by
        //     rounding errors; therefore, I am using a difference of 1% of the duration of a
        //     character within the period instead.
        var marginOfError = (long) (0.01D * CHARACTER_LENGTH_WITHIN_PERIOD);

        assertEquals(numberOfCharactersToProvide - 1,
                provider.provide(timestamp - marginOfError).length());
        assertEquals(numberOfCharactersToProvide,
                provider.provide(timestamp + marginOfError).length());
    }

    @Test
    public void testProvideBeforeStartOfRange() {
        var marginOfError = 10 * CHARACTER_LENGTH_WITHIN_PERIOD;

        assertEquals(0, provider.provide(START_TIMESTAMP - marginOfError).length());
    }

    @Test
    public void testProvideAfterEndOfRange() {
        var marginOfError = 10 * CHARACTER_LENGTH_WITHIN_PERIOD;
        var endTimestamp = START_TIMESTAMP + TIME_TO_COMPLETE + marginOfError;

        var provided = provider.provide(endTimestamp).length();

        assertEquals(STRING_LENGTH, provided);
    }

    @Test
    public void testProvideWhilePaused() {
        when(mockTimestampValidator.mostRecentTimestamp()).thenReturn(PAUSED_TIMESTAMP);

        var numberOfCharactersToProvideWhilePaused = randomIntInRange(1, STRING_LENGTH - 1);
        var percentOfString = (numberOfCharactersToProvideWhilePaused / (double) STRING_LENGTH);
        var timestampWithinPeriod = (long) (TIME_TO_COMPLETE * percentOfString);
        var timestamp = timestampWithinPeriod + START_TIMESTAMP;

        var marginOfError = (long) (0.01D * CHARACTER_LENGTH_WITHIN_PERIOD);

        var pauseTimestamp = timestamp + marginOfError;

        provider.reportPause(pauseTimestamp);
        var provided = provider.provide(randomLongWithInclusiveFloor(
                pauseTimestamp + (CHARACTER_LENGTH_WITHIN_PERIOD * 10))).length();

        assertEquals(numberOfCharactersToProvideWhilePaused, provided);
    }

    @Test
    public void testUnpauseUpdatesStartingTime() {
        when(mockTimestampValidator.mostRecentTimestamp()).thenReturn(PAUSED_TIMESTAMP);

        var pauseDuration = randomLongInRange(1000000000L, Long.MAX_VALUE / 2);
        var numberOfCharactersToProvide = randomIntInRange(1, STRING_LENGTH - 1);
        var percentOfString = (numberOfCharactersToProvide / (double) STRING_LENGTH);
        var timestampWithinPeriod = (long) (TIME_TO_COMPLETE * percentOfString);
        var timestamp = timestampWithinPeriod + START_TIMESTAMP + pauseDuration;

        // NB: I'm subtracting by a margin of error instead of 1, since the random numbers are in
        //     the quadrillions, and so subtracting by 1 to determine whether the provider returns
        //     n - 1 characters is impossible, since a difference of 1 will be washed out by
        //     rounding errors; therefore, I am using a difference of 1% of the duration of a
        //     character within the period instead.
        var marginOfError = (long) (0.01D * CHARACTER_LENGTH_WITHIN_PERIOD);

        provider.reportPause(PAUSED_TIMESTAMP);
        provider.reportUnpause(PAUSED_TIMESTAMP + pauseDuration);

        assertEquals(numberOfCharactersToProvide - 1,
                provider.provide(timestamp - marginOfError).length());
        assertEquals(numberOfCharactersToProvide,
                provider.provide(timestamp + marginOfError).length());
    }

    @Test
    public void testRepresentation() {
        assertEquals(pairOf(STRING, pairOf(TIME_TO_COMPLETE, START_TIMESTAMP)),
                provider.representation());
    }
}
