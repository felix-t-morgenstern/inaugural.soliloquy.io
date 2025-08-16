package inaugural.soliloquy.io.test.unit.graphics.renderables.providers;

import inaugural.soliloquy.io.graphics.renderables.providers.FiniteLinearMovingVertexProvider;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.providers.FiniteLinearMovingProvider;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.RANDOM;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

@ExtendWith(MockitoExtension.class)
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

    private final float WEIGHT_TIMESTAMP_1 = 0.25f;
    private final float WEIGHT_TIMESTAMP_2 = 1.0f - WEIGHT_TIMESTAMP_1;
    private final long TIMESTAMP =
            TIMESTAMP_1 + ((long) (WEIGHT_TIMESTAMP_2 * (TIMESTAMP_2 - TIMESTAMP_1)));
    private final float EXPECTED_X = (WEIGHT_TIMESTAMP_1 * X_1) + (WEIGHT_TIMESTAMP_2 * X_2);
    private final float EXPECTED_Y = (WEIGHT_TIMESTAMP_1 * Y_1) + (WEIGHT_TIMESTAMP_2 * Y_2);
    private final Vertex EXPECTED = vertexOf(EXPECTED_X, EXPECTED_Y);

    private static final UUID UUID = java.util.UUID.randomUUID();

    @Mock private TimestampValidator mockTimestampValidator;

    private FiniteLinearMovingProvider<Vertex> provider;

    @BeforeEach
    public void setUp() {
        provider = new FiniteLinearMovingVertexProvider(UUID, VALUES_AT_TIMES, null, mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingVertexProvider(null, VALUES_AT_TIMES,
                        PAUSED_TIMESTAMP, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingVertexProvider(UUID, null,
                        PAUSED_TIMESTAMP, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingVertexProvider(UUID, mapOf(),
                        PAUSED_TIMESTAMP, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteLinearMovingVertexProvider(UUID, VALUES_AT_TIMES,
                        PAUSED_TIMESTAMP, null));
    }

    @Test
    public void testUuid() {
        assertSame(UUID, provider.uuid());
    }

    @Test
    public void testPausedTimestamp() {
        provider.reportPause(PAUSED_TIMESTAMP);

        assertEquals(PAUSED_TIMESTAMP, provider.pausedTimestamp());
    }

    @Test
    public void testProvide() {
        var providedValue = provider.provide(TIMESTAMP);

        assertEquals(EXPECTED, providedValue);
        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(TIMESTAMP);
    }


    @Test
    public void testProvideBeforeStartOfRange() {
        Vertex providedValue =
                provider.provide(Long.MIN_VALUE);

        assertEquals(VALUES_AT_TIMES.get(TIMESTAMP_1), providedValue);
    }

    @Test
    public void testProvideAfterEndOfRange() {
        var providedValue = provider.provide(Long.MAX_VALUE);

        assertEquals(VALUES_AT_TIMES.get(TIMESTAMP_3), providedValue);
    }

    @Test
    public void testProvideWhilePaused() {
        provider.reportPause(TIMESTAMP);
        var providedValue = provider.provide(Long.MAX_VALUE);

        assertEquals(EXPECTED, providedValue);
    }


    @Test
    public void testUnpauseUpdatesStartingTime() {
        long pauseDuration = 123123L;

        provider.reportPause(PAUSED_TIMESTAMP);
        provider.reportUnpause(PAUSED_TIMESTAMP + pauseDuration);
        var providedValue = provider.provide(TIMESTAMP + pauseDuration);

        assertEquals(EXPECTED, providedValue);
    }

    @Test
    public void testRepresentation() {
        assertEquals(VALUES_AT_TIMES, provider.representation());
        assertNotSame(VALUES_AT_TIMES, provider.representation());
    }
}
