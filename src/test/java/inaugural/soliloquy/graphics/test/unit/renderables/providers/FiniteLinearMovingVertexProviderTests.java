package inaugural.soliloquy.graphics.test.unit.renderables.providers;

import inaugural.soliloquy.graphics.renderables.providers.FiniteLinearMovingVertexProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.providers.FiniteLinearMovingProvider;

import java.util.HashMap;
import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.RANDOM;
import static org.junit.jupiter.api.Assertions.*;

class FiniteLinearMovingVertexProviderTests {
    private final long TIMESTAMP_1 = 100L;
    private final long TIMESTAMP_2 = 200L;
    private final long TIMESTAMP_3 = 300L;

    private final float X_1 = RANDOM.nextFloat();
    private final float X_2 = RANDOM.nextFloat();
    private final float X_3 = RANDOM.nextFloat();

    private final float Y_1 = RANDOM.nextFloat();
    private final float Y_2 = RANDOM.nextFloat();
    private final float Y_3 = RANDOM.nextFloat();

    private final HashMap<Long, Vertex> VALUES_AT_TIMES =
            new HashMap<Long, Vertex>() {{
                put(TIMESTAMP_1, Vertex.of(X_1, Y_1));
                put(TIMESTAMP_2, Vertex.of(X_2, Y_2));
                put(TIMESTAMP_3, Vertex.of(X_3, Y_3));
            }};

    private final Long PAUSED_TIMESTAMP = 175L;
    private final Long MOST_RECENT_TIMESTAMP = 50L;

    private final float WEIGHT_TIMESTAMP_1 = 0.25f;
    private final float WEIGHT_TIMESTAMP_2 = 1.0f - WEIGHT_TIMESTAMP_1;
    private final long TIMESTAMP =
            TIMESTAMP_1 + ((long) (WEIGHT_TIMESTAMP_2 * (TIMESTAMP_2 - TIMESTAMP_1)));
    private final float EXPECTED_X = (WEIGHT_TIMESTAMP_1 * X_1) + (WEIGHT_TIMESTAMP_2 * X_2);
    private final float EXPECTED_Y = (WEIGHT_TIMESTAMP_1 * Y_1) + (WEIGHT_TIMESTAMP_2 * Y_2);
    private final Vertex EXPECTED = Vertex.of(EXPECTED_X, EXPECTED_Y);

    private static final UUID UUID = java.util.UUID.randomUUID();

    private FiniteLinearMovingProvider<Vertex> _finiteLinearMovingLocationProvider;

    @BeforeEach
    void setUp() {
        _finiteLinearMovingLocationProvider = new FiniteLinearMovingVertexProvider(UUID,
                VALUES_AT_TIMES, null, null);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingVertexProvider(null, VALUES_AT_TIMES,
                        PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingVertexProvider(UUID, null,
                        PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingVertexProvider(UUID, new HashMap<>(),
                        PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingVertexProvider(UUID, VALUES_AT_TIMES,
                        PAUSED_TIMESTAMP, null));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingVertexProvider(UUID, VALUES_AT_TIMES,
                        MOST_RECENT_TIMESTAMP + 1, MOST_RECENT_TIMESTAMP));
    }

    @Test
    void testUuid() {
        assertSame(UUID, _finiteLinearMovingLocationProvider.uuid());
    }

    @Test
    void testPausedTimestamp() {
        _finiteLinearMovingLocationProvider.reportPause(PAUSED_TIMESTAMP);

        assertEquals(PAUSED_TIMESTAMP, _finiteLinearMovingLocationProvider.pausedTimestamp());
    }

    @Test
    void testMostRecentTimestamp() {
        assertEquals(MOST_RECENT_TIMESTAMP,
                new FiniteLinearMovingVertexProvider(UUID, VALUES_AT_TIMES, null,
                        MOST_RECENT_TIMESTAMP)
                        .mostRecentTimestamp());
    }

    @Test
    void testProvide() {
        Vertex providedValue = _finiteLinearMovingLocationProvider.provide(TIMESTAMP);

        assertEquals(EXPECTED, providedValue);
    }


    @Test
    void testProvideBeforeStartOfRange() {
        Vertex providedValue =
                _finiteLinearMovingLocationProvider.provide(Long.MIN_VALUE);

        assertEquals(VALUES_AT_TIMES.get(TIMESTAMP_1), providedValue);
    }

    @Test
    void testProvideAfterEndOfRange() {
        Vertex providedValue =
                _finiteLinearMovingLocationProvider.provide(Long.MAX_VALUE);

        assertEquals(VALUES_AT_TIMES.get(TIMESTAMP_3), providedValue);
    }

    @Test
    void testProvideWhilePaused() {
        _finiteLinearMovingLocationProvider.reportPause(TIMESTAMP);
        Vertex providedValue =
                _finiteLinearMovingLocationProvider.provide(Long.MAX_VALUE);

        assertEquals(EXPECTED, providedValue);
    }


    @Test
    void testUnpauseUpdatesStartingTime() {
        long pauseDuration = 123123L;

        _finiteLinearMovingLocationProvider.reportPause(PAUSED_TIMESTAMP);
        _finiteLinearMovingLocationProvider.reportUnpause(PAUSED_TIMESTAMP + pauseDuration);
        Vertex providedValue =
                _finiteLinearMovingLocationProvider.provide(TIMESTAMP + pauseDuration);

        assertEquals(EXPECTED, providedValue);
    }

    @Test
    void testOutdatedTimestamp() {
        _finiteLinearMovingLocationProvider.provide(MOST_RECENT_TIMESTAMP);

        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingLocationProvider.reportPause(MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingLocationProvider.reportUnpause(MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingLocationProvider.provide(MOST_RECENT_TIMESTAMP - 1));
    }

    @Test
    void testMostRecentTimestampUpdates() {
        _finiteLinearMovingLocationProvider.reportPause(MOST_RECENT_TIMESTAMP + 1);

        assertEquals(MOST_RECENT_TIMESTAMP + 1,
                (long) _finiteLinearMovingLocationProvider.mostRecentTimestamp());

        _finiteLinearMovingLocationProvider.reportUnpause(MOST_RECENT_TIMESTAMP + 2);

        assertEquals(MOST_RECENT_TIMESTAMP + 2,
                (long) _finiteLinearMovingLocationProvider.mostRecentTimestamp());

        _finiteLinearMovingLocationProvider.provide(MOST_RECENT_TIMESTAMP + 3);

        assertEquals(MOST_RECENT_TIMESTAMP + 3,
                (long) _finiteLinearMovingLocationProvider.mostRecentTimestamp());
    }

    @Test
    void testArchetype() {
        assertNotNull(_finiteLinearMovingLocationProvider.archetype());
    }

    @Test
    void testRepresentation() {
        assertEquals(VALUES_AT_TIMES, _finiteLinearMovingLocationProvider.representation());
        assertNotSame(VALUES_AT_TIMES, _finiteLinearMovingLocationProvider.representation());
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(FiniteLinearMovingProvider.class.getCanonicalName() + "<" +
                        Vertex.class.getCanonicalName() + ">",
                _finiteLinearMovingLocationProvider.getInterfaceName());
    }
}
