package inaugural.soliloquy.io.test.unit.graphics.renderables.providers;

import inaugural.soliloquy.io.graphics.renderables.providers.FiniteLinearMovingVertexProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.providers.FiniteLinearMovingProvider;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.RANDOM;
import static org.junit.jupiter.api.Assertions.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

public class FiniteLinearMovingVertexProviderTests {
    private final long TIMESTAMP_1 = 100L;
    private final long TIMESTAMP_2 = 200L;
    private final long TIMESTAMP_3 = 300L;

    private final float X_1 = RANDOM.nextFloat();
    private final float X_2 = RANDOM.nextFloat();
    private final float X_3 = RANDOM.nextFloat();

    private final float Y_1 = RANDOM.nextFloat();
    private final float Y_2 = RANDOM.nextFloat();
    private final float Y_3 = RANDOM.nextFloat();

    private final Map<Long, Vertex> VALUES_AT_TIMES = mapOf(
                pairOf(TIMESTAMP_1, vertexOf(X_1, Y_1)),
                pairOf(TIMESTAMP_2, vertexOf(X_2, Y_2)),
                pairOf(TIMESTAMP_3, vertexOf(X_3, Y_3))
    );

    private final Long PAUSED_TIMESTAMP = 175L;
    private final Long MOST_RECENT_TIMESTAMP = 50L;

    private final float WEIGHT_TIMESTAMP_1 = 0.25f;
    private final float WEIGHT_TIMESTAMP_2 = 1.0f - WEIGHT_TIMESTAMP_1;
    private final long TIMESTAMP =
            TIMESTAMP_1 + ((long) (WEIGHT_TIMESTAMP_2 * (TIMESTAMP_2 - TIMESTAMP_1)));
    private final float EXPECTED_X = (WEIGHT_TIMESTAMP_1 * X_1) + (WEIGHT_TIMESTAMP_2 * X_2);
    private final float EXPECTED_Y = (WEIGHT_TIMESTAMP_1 * Y_1) + (WEIGHT_TIMESTAMP_2 * Y_2);
    private final Vertex EXPECTED = vertexOf(EXPECTED_X, EXPECTED_Y);

    private static final UUID UUID = java.util.UUID.randomUUID();

    private FiniteLinearMovingProvider<Vertex> finiteLinearMovingLocationProvider;

    @BeforeEach
    public void setUp() {
        finiteLinearMovingLocationProvider = new FiniteLinearMovingVertexProvider(UUID,
                VALUES_AT_TIMES, null, null);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingVertexProvider(null, VALUES_AT_TIMES,
                        PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingVertexProvider(UUID, null,
                        PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingVertexProvider(UUID, mapOf(),
                        PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingVertexProvider(UUID, VALUES_AT_TIMES,
                        PAUSED_TIMESTAMP, null));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingVertexProvider(UUID, VALUES_AT_TIMES,
                        MOST_RECENT_TIMESTAMP + 1, MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testUuid() {
        assertSame(UUID, finiteLinearMovingLocationProvider.uuid());
    }

    @Test
    public void testPausedTimestamp() {
        finiteLinearMovingLocationProvider.reportPause(PAUSED_TIMESTAMP);

        assertEquals(PAUSED_TIMESTAMP, finiteLinearMovingLocationProvider.pausedTimestamp());
    }

    @Test
    public void testMostRecentTimestamp() {
        assertEquals(MOST_RECENT_TIMESTAMP,
                new FiniteLinearMovingVertexProvider(UUID, VALUES_AT_TIMES, null,
                        MOST_RECENT_TIMESTAMP)
                        .mostRecentTimestamp());
    }

    @Test
    public void testProvide() {
        Vertex providedValue = finiteLinearMovingLocationProvider.provide(TIMESTAMP);

        assertEquals(EXPECTED, providedValue);
    }


    @Test
    public void testProvideBeforeStartOfRange() {
        Vertex providedValue =
                finiteLinearMovingLocationProvider.provide(Long.MIN_VALUE);

        assertEquals(VALUES_AT_TIMES.get(TIMESTAMP_1), providedValue);
    }

    @Test
    public void testProvideAfterEndOfRange() {
        Vertex providedValue =
                finiteLinearMovingLocationProvider.provide(Long.MAX_VALUE);

        assertEquals(VALUES_AT_TIMES.get(TIMESTAMP_3), providedValue);
    }

    @Test
    public void testProvideWhilePaused() {
        finiteLinearMovingLocationProvider.reportPause(TIMESTAMP);
        Vertex providedValue =
                finiteLinearMovingLocationProvider.provide(Long.MAX_VALUE);

        assertEquals(EXPECTED, providedValue);
    }


    @Test
    public void testUnpauseUpdatesStartingTime() {
        long pauseDuration = 123123L;

        finiteLinearMovingLocationProvider.reportPause(PAUSED_TIMESTAMP);
        finiteLinearMovingLocationProvider.reportUnpause(PAUSED_TIMESTAMP + pauseDuration);
        Vertex providedValue =
                finiteLinearMovingLocationProvider.provide(TIMESTAMP + pauseDuration);

        assertEquals(EXPECTED, providedValue);
    }

    @Test
    public void testOutdatedTimestamp() {
        finiteLinearMovingLocationProvider.provide(MOST_RECENT_TIMESTAMP);

        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingLocationProvider.reportPause(MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingLocationProvider.reportUnpause(MOST_RECENT_TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingLocationProvider.provide(MOST_RECENT_TIMESTAMP - 1));
    }

    @Test
    public void testMostRecentTimestampUpdates() {
        finiteLinearMovingLocationProvider.reportPause(MOST_RECENT_TIMESTAMP + 1);

        assertEquals(MOST_RECENT_TIMESTAMP + 1,
                (long) finiteLinearMovingLocationProvider.mostRecentTimestamp());

        finiteLinearMovingLocationProvider.reportUnpause(MOST_RECENT_TIMESTAMP + 2);

        assertEquals(MOST_RECENT_TIMESTAMP + 2,
                (long) finiteLinearMovingLocationProvider.mostRecentTimestamp());

        finiteLinearMovingLocationProvider.provide(MOST_RECENT_TIMESTAMP + 3);

        assertEquals(MOST_RECENT_TIMESTAMP + 3,
                (long) finiteLinearMovingLocationProvider.mostRecentTimestamp());
    }

    @Test
    public void testRepresentation() {
        assertEquals(VALUES_AT_TIMES, finiteLinearMovingLocationProvider.representation());
        assertNotSame(VALUES_AT_TIMES, finiteLinearMovingLocationProvider.representation());
    }
}
