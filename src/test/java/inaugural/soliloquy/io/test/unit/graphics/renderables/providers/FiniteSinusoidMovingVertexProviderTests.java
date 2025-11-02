package inaugural.soliloquy.io.test.unit.graphics.renderables.providers;

import inaugural.soliloquy.io.graphics.renderables.providers.FiniteSinusoidMovingVertexProvider;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.*;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

@ExtendWith(MockitoExtension.class)
public class FiniteSinusoidMovingVertexProviderTests {
    private final UUID UUID = randomUUID();

    private final long TIME_1 = randomLong();
    private final Vertex VERTEX_1 = randomVertex();

    private final long TIME_2 = randomLongWithInclusiveFloor(TIME_1 + 1);
    private final Vertex VERTEX_2 = randomVertex();

    private final float TRANSITION_SHARPNESS = randomFloatInRange(0f, 1f);

    private final long TIMESTAMP_IN_PERIOD = randomLongInRange(TIME_1, TIME_2);

    @Mock private TimestampValidator mockTimestampValidator;

    private ProviderAtTime<Vertex> provider;

    @BeforeEach
    public void setUp() {
        provider = new FiniteSinusoidMovingVertexProvider(UUID,
                mapOf(
                        pairOf(TIME_1, VERTEX_1),
                        pairOf(TIME_2, VERTEX_2)
                ), arrayFloats(TRANSITION_SHARPNESS), null, mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteSinusoidMovingVertexProvider(null,
                        mapOf(pairOf(TIME_1, VERTEX_1), pairOf(TIME_2, VERTEX_2)),
                        arrayFloats(TRANSITION_SHARPNESS), null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteSinusoidMovingVertexProvider(UUID,
                        null,
                        arrayFloats(TRANSITION_SHARPNESS), null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteSinusoidMovingVertexProvider(UUID,
                        mapOf(pairOf(null, VERTEX_1), pairOf(TIME_2, VERTEX_2)),
                        arrayFloats(TRANSITION_SHARPNESS), null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteSinusoidMovingVertexProvider(UUID,
                        mapOf(pairOf(TIME_1, null), pairOf(TIME_2, VERTEX_2)),
                        arrayFloats(TRANSITION_SHARPNESS), null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteSinusoidMovingVertexProvider(UUID,
                        mapOf(pairOf(TIME_1, VERTEX_1), pairOf(TIME_2, VERTEX_2)),
                        null, null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteSinusoidMovingVertexProvider(UUID,
                        mapOf(pairOf(TIME_1, VERTEX_1), pairOf(TIME_2, VERTEX_2)),
                        arrayFloats(TRANSITION_SHARPNESS), null, null));
    }

    @Test
    public void testProvideBeforePeriod() {
        var provided = provider.provide(TIME_1 - 1);

        assertEquals(VERTEX_1, provided);
    }

    @Test
    public void testProvideWithinPeriod() {
        var weight2 = (float) (TIMESTAMP_IN_PERIOD - TIME_1) / (TIME_2 - TIME_1);
        var weightSine = (-Math.PI / 2f) + (Math.PI * weight2);
        var sineValue = Math.sin(weightSine);
        var distFromTarget = (sineValue > 0 ? 1 : -1) - sineValue;
        var sharpenedSineValue = sineValue + (distFromTarget * TRANSITION_SHARPNESS);
        var sineAdjustedWeight2 = (float) ((sharpenedSineValue + 1f) / 2f);
        var sineAdjustedWeight1 = 1f - sineAdjustedWeight2;
        var expected = vertexOf(
                (VERTEX_1.X * sineAdjustedWeight1) + (VERTEX_2.X * sineAdjustedWeight2),
                (VERTEX_1.Y * sineAdjustedWeight1) + (VERTEX_2.Y * sineAdjustedWeight2)
        );

        var provided = provider.provide(TIMESTAMP_IN_PERIOD);

        assertEquals(expected, provided);
        verify(mockTimestampValidator, once()).validateTimestamp(TIMESTAMP_IN_PERIOD);
    }

    @Test
    public void testProvideAfterPeriod() {
        var provided = provider.provide(TIME_2 + 1);

        assertEquals(VERTEX_2, provided);
    }
}
