package inaugural.soliloquy.io.test.unit.graphics.renderables.providers.factories;

import inaugural.soliloquy.io.graphics.renderables.providers.FiniteLinearMovingColorProviderImpl;
import inaugural.soliloquy.io.graphics.renderables.providers.factories.FiniteLinearMovingColorProviderFactoryImpl;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static org.junit.jupiter.api.Assertions.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

@ExtendWith(MockitoExtension.class)
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

    private final UUID UUID = java.util.UUID.randomUUID();

    private List<Boolean> hueMovementIsClockwise;

    @Mock private TimestampValidator mockTimestampValidator;

    private FiniteLinearMovingColorProviderFactoryImpl factory;

    @BeforeEach
    public void setUp() {
        VALUES_AT_TIMES.put(TIME_1, VALUE_1);
        VALUES_AT_TIMES.put(TIME_2, VALUE_2);
        VALUES_AT_TIMES.put(TIME_3, VALUE_3);
        VALUES_AT_TIMES.put(TIME_4, VALUE_4);
        VALUES_AT_TIMES.put(TIME_5, VALUE_5);

        var transition2IsClockwise = true;
        var transition3IsClockwise = true;
        var transition4IsClockwise = false;

        hueMovementIsClockwise = listOf(
                TRANSITION_1_IS_CLOCKWISE,
                transition2IsClockwise,
                transition3IsClockwise,
                transition4IsClockwise
        );

        factory = new FiniteLinearMovingColorProviderFactoryImpl(mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteLinearMovingColorProviderFactoryImpl(null));
    }

    @Test
    public void testMake() {
        var provider = factory.make(UUID, VALUES_AT_TIMES, hueMovementIsClockwise, null);

        assertNotNull(provider);
        assertInstanceOf(FiniteLinearMovingColorProviderImpl.class, provider);
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                factory.make(null, VALUES_AT_TIMES,
                        hueMovementIsClockwise, null));

        assertThrows(IllegalArgumentException.class, () ->
                factory.make(UUID, null,
                        hueMovementIsClockwise, null));
        assertThrows(IllegalArgumentException.class, () ->
                factory.make(UUID, mapOf(),
                        hueMovementIsClockwise, null));
        assertThrows(IllegalArgumentException.class, () ->
                factory.make(UUID, mapOf(pairOf(null, Color.RED)),
                        hueMovementIsClockwise, null));
        assertThrows(IllegalArgumentException.class, () ->
                factory.make(UUID, mapOf(pairOf(123L, null)),
                        hueMovementIsClockwise, null));

        assertThrows(IllegalArgumentException.class, () ->
                factory.make(UUID, VALUES_AT_TIMES,
                        null, null));
        assertThrows(IllegalArgumentException.class, () ->
                factory.make(UUID, VALUES_AT_TIMES,
                        listOf(
                                TRANSITION_1_IS_CLOCKWISE,
                                TRANSITION_1_IS_CLOCKWISE,
                                TRANSITION_1_IS_CLOCKWISE,
                                null
                        ), null));
        assertThrows(IllegalArgumentException.class, () ->
                factory.make(UUID, VALUES_AT_TIMES,
                        listOf(
                                TRANSITION_1_IS_CLOCKWISE,
                                TRANSITION_1_IS_CLOCKWISE,
                                TRANSITION_1_IS_CLOCKWISE
                        ), null));
        assertThrows(IllegalArgumentException.class, () ->
                factory.make(UUID, VALUES_AT_TIMES,
                        listOf(
                                TRANSITION_1_IS_CLOCKWISE,
                                TRANSITION_1_IS_CLOCKWISE,
                                TRANSITION_1_IS_CLOCKWISE,
                                TRANSITION_1_IS_CLOCKWISE,
                                TRANSITION_1_IS_CLOCKWISE
                        ), null));
    }
}
