package inaugural.soliloquy.graphics.test.unit.renderables.providers.factories;

import inaugural.soliloquy.graphics.renderables.providers.LoopingLinearMovingColorProviderImpl;
import inaugural.soliloquy.graphics.renderables.providers.factories.LoopingLinearMovingColorProviderFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.providers.LoopingLinearMovingColorProvider;
import soliloquy.specs.graphics.renderables.providers.factories.LoopingLinearMovingColorProviderFactory;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static org.junit.jupiter.api.Assertions.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

public class LoopingLinearMovingColorProviderFactoryImplTests {
    private final Map<Integer, Color> VALUES_AT_TIMES = mapOf();
    private final int TIME_1 = 0;
    private final Color VALUE_1 = new Color(188, 130, 217, 255);
    private final int TIME_2 = 100;
    private final Color VALUE_2 = new Color(8, 79, 35, 127);
    private final int TIME_3 = 300;
    private final Color VALUE_3 = new Color(0, 191, 255, 63);
    private final int TIME_4 = 600;
    private final Color VALUE_4 = new Color(199, 222, 140, 127);
    private final int TIME_5 = 1000;
    private final Color VALUE_5 = new Color(6, 36, 117, 255);

    private final int PERIOD_DURATION = 1500;
    private final int PERIOD_MODULO_OFFSET = 12;

    private final boolean TRANSITION_1_IS_CLOCKWISE = false;
    private final boolean TRANSITION_2_IS_CLOCKWISE = true;
    private final boolean TRANSITION_3_IS_CLOCKWISE = true;
    private final boolean TRANSITION_4_IS_CLOCKWISE = false;
    private final boolean TRANSITION_5_IS_CLOCKWISE = false;

    private final UUID UUID = java.util.UUID.randomUUID();

    private List<Boolean> hueMovementIsClockwise;

    private LoopingLinearMovingColorProviderFactory loopingLinearMovingColorProviderFactory;

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
            TRANSITION_4_IS_CLOCKWISE,
            TRANSITION_5_IS_CLOCKWISE
        );

        loopingLinearMovingColorProviderFactory =
                new LoopingLinearMovingColorProviderFactoryImpl();
    }

    @Test
    public void testMake() {
        LoopingLinearMovingColorProvider loopingMovingColorProvider =
                loopingLinearMovingColorProviderFactory.make(UUID, VALUES_AT_TIMES,
                        hueMovementIsClockwise, PERIOD_DURATION, PERIOD_MODULO_OFFSET, null,
                        null);

        assertNotNull(loopingMovingColorProvider);
        assertInstanceOf(LoopingLinearMovingColorProviderImpl.class, loopingMovingColorProvider);
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingColorProviderFactory.make(null, VALUES_AT_TIMES,
                        hueMovementIsClockwise, PERIOD_DURATION, PERIOD_MODULO_OFFSET, null,
                        null));

        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingColorProviderFactory.make(UUID, null,
                        hueMovementIsClockwise, PERIOD_DURATION, PERIOD_MODULO_OFFSET, null,
                        null));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingColorProviderFactory.make(UUID, mapOf(),
                        hueMovementIsClockwise, PERIOD_DURATION, PERIOD_MODULO_OFFSET, null,
                        null));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingColorProviderFactory.make(UUID, mapOf(pairOf(null, Color.RED)),
                        hueMovementIsClockwise, PERIOD_DURATION, PERIOD_MODULO_OFFSET, null,
                        null));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingColorProviderFactory.make(UUID, mapOf(pairOf(0, null)),
                        hueMovementIsClockwise, PERIOD_DURATION, PERIOD_MODULO_OFFSET, null,
                        null));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingColorProviderFactory.make(UUID, mapOf(pairOf(123, Color.RED)),
                        hueMovementIsClockwise, PERIOD_DURATION, PERIOD_MODULO_OFFSET, null,
                        null));

        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingColorProviderFactory.make(UUID, VALUES_AT_TIMES,
                        null, PERIOD_DURATION, PERIOD_MODULO_OFFSET, null, null));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingColorProviderFactory.make(UUID, VALUES_AT_TIMES,
                        listOf(
                            TRANSITION_1_IS_CLOCKWISE,
                            TRANSITION_1_IS_CLOCKWISE,
                            TRANSITION_1_IS_CLOCKWISE,
                            TRANSITION_1_IS_CLOCKWISE,
                            null
                        ), PERIOD_DURATION, PERIOD_MODULO_OFFSET, null, null));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingColorProviderFactory.make(UUID, VALUES_AT_TIMES,
                        listOf(
                            TRANSITION_1_IS_CLOCKWISE,
                            TRANSITION_1_IS_CLOCKWISE,
                            TRANSITION_1_IS_CLOCKWISE,
                            TRANSITION_1_IS_CLOCKWISE
                        ), PERIOD_DURATION, PERIOD_MODULO_OFFSET, null, null));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingColorProviderFactory.make(UUID, VALUES_AT_TIMES,
                        listOf(
                            TRANSITION_1_IS_CLOCKWISE,
                            TRANSITION_1_IS_CLOCKWISE,
                            TRANSITION_1_IS_CLOCKWISE,
                            TRANSITION_1_IS_CLOCKWISE,
                            TRANSITION_1_IS_CLOCKWISE,
                            TRANSITION_1_IS_CLOCKWISE
                        ), PERIOD_DURATION, PERIOD_MODULO_OFFSET, null, null));

        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingColorProviderFactory.make(UUID, VALUES_AT_TIMES,
                        hueMovementIsClockwise, PERIOD_DURATION, PERIOD_DURATION, null, null));

        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingColorProviderFactory.make(UUID, VALUES_AT_TIMES,
                        hueMovementIsClockwise, PERIOD_DURATION, PERIOD_MODULO_OFFSET, 12L,
                        null));
        assertThrows(IllegalArgumentException.class, () ->
                loopingLinearMovingColorProviderFactory.make(UUID, VALUES_AT_TIMES,
                        hueMovementIsClockwise, PERIOD_DURATION, PERIOD_MODULO_OFFSET, 1L, 0L));
    }
}
