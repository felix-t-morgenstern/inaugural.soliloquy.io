package inaugural.soliloquy.graphics.test.unit.renderables.providers;

import inaugural.soliloquy.graphics.renderables.providers.ProgressiveStringProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.*;
import static org.junit.jupiter.api.Assertions.*;

class ProgressiveStringProviderTests {
    private final String STRING = randomString();
    private final int STRING_LENGTH = STRING.length();
    // NB: Values here are divided by 2 to prevent overflow issues in tests
    private final long TIME_TO_COMPLETE = randomLongWithInclusiveFloor(1L) / 2;
    private final long START_TIMESTAMP = randomLongWithInclusiveCeiling(TIME_TO_COMPLETE - 1) / 2;
    private final Long MOST_RECENT_TIMESTAMP = randomLongWithInclusiveCeiling(START_TIMESTAMP);
    private final Long PAUSED_TIMESTAMP =
            randomLongInRange(MOST_RECENT_TIMESTAMP, START_TIMESTAMP);

    private static final UUID UUID = java.util.UUID.randomUUID();

    private ProviderAtTime<String> _progressiveStringProvider;

    @BeforeEach
    void setUp() {
        _progressiveStringProvider = new ProgressiveStringProvider(UUID, STRING,
                START_TIMESTAMP, TIME_TO_COMPLETE, null, null);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new ProgressiveStringProvider(null, STRING, START_TIMESTAMP, TIME_TO_COMPLETE,
                        PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                new ProgressiveStringProvider(UUID, null, START_TIMESTAMP, TIME_TO_COMPLETE,
                        PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                new ProgressiveStringProvider(UUID, STRING, START_TIMESTAMP, 0,
                        PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                new ProgressiveStringProvider(UUID, STRING, START_TIMESTAMP, TIME_TO_COMPLETE,
                        PAUSED_TIMESTAMP, null));
        assertThrows(IllegalArgumentException.class, () ->
                new ProgressiveStringProvider(UUID, STRING, START_TIMESTAMP, TIME_TO_COMPLETE,
                        MOST_RECENT_TIMESTAMP + 1, MOST_RECENT_TIMESTAMP));
    }

    @Test
    void testUuid() {
        assertSame(UUID, _progressiveStringProvider.uuid());
    }

    @Test
    void testPausedTimestamp() {
        _progressiveStringProvider.reportPause(PAUSED_TIMESTAMP);

        assertEquals(PAUSED_TIMESTAMP, _progressiveStringProvider.pausedTimestamp());
    }

    @Test
    void testMostRecentTimestamp() {
        assertEquals(MOST_RECENT_TIMESTAMP,
                new ProgressiveStringProvider(UUID, STRING, START_TIMESTAMP, TIME_TO_COMPLETE,
                        null, MOST_RECENT_TIMESTAMP)
                        .mostRecentTimestamp());
    }

    @Test
    void testProvide() {
        int numberOfCharactersToProvide = randomIntInRange(1, STRING_LENGTH - 1);
        double percentOfString = (numberOfCharactersToProvide / (double) STRING_LENGTH);
        long timestampWithinPeriod = (long) (TIME_TO_COMPLETE * percentOfString);
        long timestamp = timestampWithinPeriod + START_TIMESTAMP;

        // NB: I'm subtracting by a margin of error instead of 1, since the random numbers are in
        //     the quadrillions, and so subtracting by 1 to determine whether the provider returns
        //     n - 1 characters is impossible, since a difference of 1 will be washed out by
        //     rounding errors; therefore, I am using a difference of 1% of the duration of a
        //     character within the period instead.
        long characterLengthWithinPeriod =
                (long) (TIME_TO_COMPLETE * (1 / (double) STRING_LENGTH));
        long marginOfError = (long) (0.01D * characterLengthWithinPeriod);

        assertEquals(numberOfCharactersToProvide - 1,
                _progressiveStringProvider.provide(timestamp - marginOfError).length());
        assertEquals(numberOfCharactersToProvide,
                _progressiveStringProvider.provide(timestamp + marginOfError).length());
    }

    @Test
    void testProvideBeforeStartOfRange() {
        long characterLengthWithinPeriod =
                (long) (TIME_TO_COMPLETE * (1 / (double) STRING_LENGTH));
        long marginOfError = 10 * characterLengthWithinPeriod;

        assertEquals(0,
                _progressiveStringProvider.provide(START_TIMESTAMP - marginOfError).length());
    }

    @Test
    void testProvideAfterEndOfRange() {
        assertEquals(STRING_LENGTH,
                _progressiveStringProvider.provide(Long.MAX_VALUE / 2).length());
    }

    @Test
    void testProvideWhilePaused() {
        int numberOfCharactersToProvideWhilePaused = randomIntInRange(1, STRING_LENGTH - 1);
        double percentOfString = (numberOfCharactersToProvideWhilePaused / (double) STRING_LENGTH);
        long timestampWithinPeriod = (long) (TIME_TO_COMPLETE * percentOfString);
        long timestamp = timestampWithinPeriod + START_TIMESTAMP;

        long characterLengthWithinPeriod =
                (long) (TIME_TO_COMPLETE * (1 / (double) STRING_LENGTH));
        long marginOfError = (long) (0.01D * characterLengthWithinPeriod);

        long pauseTimestamp = timestamp + marginOfError;

        _progressiveStringProvider.reportPause(pauseTimestamp);

        assertEquals(numberOfCharactersToProvideWhilePaused,
                _progressiveStringProvider
                        .provide(randomLongWithInclusiveFloor(pauseTimestamp
                                + (characterLengthWithinPeriod * 10)))
                        .length());
    }

    @Test
    void testUnpauseUpdatesStartingTime() {
        long pauseDuration = randomLongInRange(1000000000L, Long.MAX_VALUE / 2);
        int numberOfCharactersToProvide = randomIntInRange(1, STRING_LENGTH - 1);
        double percentOfString = (numberOfCharactersToProvide / (double) STRING_LENGTH);
        long timestampWithinPeriod = (long) (TIME_TO_COMPLETE * percentOfString);
        long timestamp = timestampWithinPeriod + START_TIMESTAMP + pauseDuration;

        // NB: I'm subtracting by a margin of error instead of 1, since the random numbers are in
        //     the quadrillions, and so subtracting by 1 to determine whether the provider returns
        //     n - 1 characters is impossible, since a difference of 1 will be washed out by
        //     rounding errors; therefore, I am using a difference of 1% of the duration of a
        //     character within the period instead.
        long characterLengthWithinPeriod =
                (long) (TIME_TO_COMPLETE * (1 / (double) STRING_LENGTH));
        long marginOfError = (long) (0.01D * characterLengthWithinPeriod);

        _progressiveStringProvider.reportPause(PAUSED_TIMESTAMP);
        _progressiveStringProvider.reportUnpause(PAUSED_TIMESTAMP + pauseDuration);

        assertEquals(numberOfCharactersToProvide - 1,
                _progressiveStringProvider.provide(timestamp - marginOfError).length());
        assertEquals(numberOfCharactersToProvide,
                _progressiveStringProvider.provide(timestamp + marginOfError).length());
    }

    @Test
    void testOutdatedTimestamp() {
        _progressiveStringProvider.provide(MOST_RECENT_TIMESTAMP);

        assertThrows(IllegalArgumentException.class, () ->
                _progressiveStringProvider.reportPause(MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _progressiveStringProvider.reportUnpause(MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _progressiveStringProvider.provide(MOST_RECENT_TIMESTAMP - 1));
    }

    @Test
    void testMostRecentTimestampUpdates() {
        _progressiveStringProvider.reportPause(MOST_RECENT_TIMESTAMP + 1);

        assertEquals(MOST_RECENT_TIMESTAMP + 1,
                (long) _progressiveStringProvider.mostRecentTimestamp());

        _progressiveStringProvider.reportUnpause(MOST_RECENT_TIMESTAMP + 2);

        assertEquals(MOST_RECENT_TIMESTAMP + 2,
                (long) _progressiveStringProvider.mostRecentTimestamp());

        _progressiveStringProvider.provide(MOST_RECENT_TIMESTAMP + 3);

        assertEquals(MOST_RECENT_TIMESTAMP + 3,
                (long) _progressiveStringProvider.mostRecentTimestamp());
    }

    @Test
    void testGetArchetype() {
        assertNotNull(_progressiveStringProvider.getArchetype());
    }

    @Test
    void testRepresentation() {
        assertEquals(new Pair<>(STRING, TIME_TO_COMPLETE),
                _progressiveStringProvider.representation());
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(ProviderAtTime.class.getCanonicalName() + "<" +
                        String.class.getCanonicalName() + ">",
                _progressiveStringProvider.getInterfaceName());
    }
}
