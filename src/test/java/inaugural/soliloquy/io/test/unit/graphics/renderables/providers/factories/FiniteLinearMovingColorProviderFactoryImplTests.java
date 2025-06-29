package inaugural.soliloquy.io.test.unit.graphics.renderables.providers.factories;

import inaugural.soliloquy.io.graphics.renderables.providers.FiniteLinearMovingColorProviderImpl;
import inaugural.soliloquy.io.graphics.renderables.providers.factories.FiniteLinearMovingColorProviderFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.io.graphics.renderables.providers.FiniteLinearMovingColorProvider;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static org.junit.jupiter.api.Assertions.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

public class FiniteLinearMovingColorProviderFactoryImplTests {
    private final Map<Long, Color> VALUES_AT_TIMES = mapOf();
    @SuppressWarnings("FieldCanBeLocal")
    private final long TIME_1 = 100L;
    private final Color VALUE_1 = new Color(188, 130, 217, 255);
    @SuppressWarnings("FieldCanBeLocal")
    private final long TIME_2 = 300L;
    private final Color VALUE_2 = new Color(8, 79, 35, 127);
    @SuppressWarnings("FieldCanBeLocal")
    private final long TIME_3 = 500L;
    private final Color VALUE_3 = new Color(0, 191, 255, 63);
    @SuppressWarnings("FieldCanBeLocal")
    private final long TIME_4 = 800L;
    private final Color VALUE_4 = new Color(199, 222, 140, 127);
    @SuppressWarnings("FieldCanBeLocal")
    private final long TIME_5 = 1200L;
    private final Color VALUE_5 = new Color(6, 36, 117, 255);

    private final boolean TRANSITION_1_IS_CLOCKWISE = false;
    private final boolean TRANSITION_2_IS_CLOCKWISE = true;
    private final boolean TRANSITION_3_IS_CLOCKWISE = true;
    private final boolean TRANSITION_4_IS_CLOCKWISE = false;

    private final UUID UUID = java.util.UUID.randomUUID();

    private List<Boolean> hueMovementIsClockwise;

    private final long MOST_RECENT_TIMESTAMP = 34L;

    private FiniteLinearMovingColorProviderFactoryImpl finiteLinearMovingColorProviderFactory;

    @BeforeEach
    public void setUp() {
        VALUES_AT_TIMES.put(TIME_1, VALUE_1);
        VALUES_AT_TIMES.put(TIME_2, VALUE_2);
        VALUES_AT_TIMES.put(TIME_3, VALUE_3);
        VALUES_AT_TIMES.put(TIME_4, VALUE_4);
        VALUES_AT_TIMES.put(TIME_5, VALUE_5);

        hueMovementIsClockwise = listOf(
            TRANSITION_1_IS_CLOCKWISE,
            TRANSITION_2_IS_CLOCKWISE,
            TRANSITION_3_IS_CLOCKWISE,
            TRANSITION_4_IS_CLOCKWISE
        );

        finiteLinearMovingColorProviderFactory = new FiniteLinearMovingColorProviderFactoryImpl();
    }

    @Test
    public void testMake() {
        FiniteLinearMovingColorProvider finiteLinearMovingColorProvider =
                finiteLinearMovingColorProviderFactory.make(
                        UUID, VALUES_AT_TIMES, hueMovementIsClockwise, null, MOST_RECENT_TIMESTAMP
                );

        assertNotNull(finiteLinearMovingColorProvider);
        assertInstanceOf(FiniteLinearMovingColorProviderImpl.class,
                finiteLinearMovingColorProvider);
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingColorProviderFactory.make(null, VALUES_AT_TIMES,
                        hueMovementIsClockwise, null, MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingColorProviderFactory.make(UUID, null,
                        hueMovementIsClockwise, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingColorProviderFactory.make(UUID, mapOf(),
                        hueMovementIsClockwise, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingColorProviderFactory.make(UUID, mapOf(pairOf(null, Color.RED)),
                        hueMovementIsClockwise, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingColorProviderFactory.make(UUID, mapOf(pairOf(123L, null)),
                        hueMovementIsClockwise, null, MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingColorProviderFactory.make(UUID, VALUES_AT_TIMES,
                        null, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingColorProviderFactory.make(UUID, VALUES_AT_TIMES,
                        listOf(
                            TRANSITION_1_IS_CLOCKWISE,
                            TRANSITION_1_IS_CLOCKWISE,
                            TRANSITION_1_IS_CLOCKWISE,
                            null
                        ), null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingColorProviderFactory.make(UUID, VALUES_AT_TIMES,
                        listOf(
                            TRANSITION_1_IS_CLOCKWISE,
                            TRANSITION_1_IS_CLOCKWISE,
                            TRANSITION_1_IS_CLOCKWISE
                        ), null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingColorProviderFactory.make(UUID, VALUES_AT_TIMES,
                        listOf(
                            TRANSITION_1_IS_CLOCKWISE,
                            TRANSITION_1_IS_CLOCKWISE,
                            TRANSITION_1_IS_CLOCKWISE,
                            TRANSITION_1_IS_CLOCKWISE,
                            TRANSITION_1_IS_CLOCKWISE
                        ), null, MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingColorProviderFactory.make(UUID, VALUES_AT_TIMES,
                        hueMovementIsClockwise, 12L, null));
        assertThrows(IllegalArgumentException.class, () ->
                finiteLinearMovingColorProviderFactory.make(UUID, VALUES_AT_TIMES,
                        hueMovementIsClockwise, MOST_RECENT_TIMESTAMP + 1,
                        MOST_RECENT_TIMESTAMP));
    }
}
