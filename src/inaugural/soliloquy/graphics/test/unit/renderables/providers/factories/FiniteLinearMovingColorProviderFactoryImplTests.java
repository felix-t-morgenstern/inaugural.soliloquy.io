package inaugural.soliloquy.graphics.test.unit.renderables.providers.factories;

import inaugural.soliloquy.graphics.renderables.providers.FiniteLinearMovingColorProviderImpl;
import inaugural.soliloquy.graphics.renderables.providers.factories.FiniteLinearMovingColorProviderFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.providers.FiniteLinearMovingColorProvider;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FiniteLinearMovingColorProviderFactoryImplTests {
    private final HashMap<Long, Color> VALUES_AT_TIMES = new HashMap<>();
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

    private ArrayList<Boolean> _hueMovementIsClockwise;

    private final long MOST_RECENT_TIMESTAMP = 34L;

    private FiniteLinearMovingColorProviderFactoryImpl _finiteLinearMovingColorProviderFactory;

    @BeforeEach
    void setUp() {
        VALUES_AT_TIMES.put(TIME_1, VALUE_1);
        VALUES_AT_TIMES.put(TIME_2, VALUE_2);
        VALUES_AT_TIMES.put(TIME_3, VALUE_3);
        VALUES_AT_TIMES.put(TIME_4, VALUE_4);
        VALUES_AT_TIMES.put(TIME_5, VALUE_5);

        _hueMovementIsClockwise = new ArrayList<Boolean>() {{
            add(TRANSITION_1_IS_CLOCKWISE);
            add(TRANSITION_2_IS_CLOCKWISE);
            add(TRANSITION_3_IS_CLOCKWISE);
            add(TRANSITION_4_IS_CLOCKWISE);
        }};

        _finiteLinearMovingColorProviderFactory = new FiniteLinearMovingColorProviderFactoryImpl();
    }

    @Test
    void testMake() {
        FiniteLinearMovingColorProvider finiteLinearMovingColorProvider =
                _finiteLinearMovingColorProviderFactory.make(
                        UUID, VALUES_AT_TIMES, _hueMovementIsClockwise, null, MOST_RECENT_TIMESTAMP
                );

        assertNotNull(finiteLinearMovingColorProvider);
        assertTrue(finiteLinearMovingColorProvider instanceof FiniteLinearMovingColorProviderImpl);
    }

    @Test
    void testMakeWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingColorProviderFactory.make(null, VALUES_AT_TIMES,
                        _hueMovementIsClockwise, null, MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingColorProviderFactory.make(UUID, null,
                        _hueMovementIsClockwise, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingColorProviderFactory.make(UUID, new HashMap<>(),
                        _hueMovementIsClockwise, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingColorProviderFactory.make(UUID, new HashMap<Long, Color>() {{
                            put(null, Color.RED);
                        }},
                        _hueMovementIsClockwise, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingColorProviderFactory.make(UUID, new HashMap<Long, Color>() {{
                            put(123L, null);
                        }},
                        _hueMovementIsClockwise, null, MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingColorProviderFactory.make(UUID, VALUES_AT_TIMES,
                        null, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingColorProviderFactory.make(UUID, VALUES_AT_TIMES,
                        new ArrayList<Boolean>() {{
                            add(TRANSITION_1_IS_CLOCKWISE);
                            add(TRANSITION_1_IS_CLOCKWISE);
                            add(TRANSITION_1_IS_CLOCKWISE);
                            add(null);
                        }}, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingColorProviderFactory.make(UUID, VALUES_AT_TIMES,
                        new ArrayList<Boolean>() {{
                            add(TRANSITION_1_IS_CLOCKWISE);
                            add(TRANSITION_1_IS_CLOCKWISE);
                            add(TRANSITION_1_IS_CLOCKWISE);
                        }}, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingColorProviderFactory.make(UUID, VALUES_AT_TIMES,
                        new ArrayList<Boolean>() {{
                            add(TRANSITION_1_IS_CLOCKWISE);
                            add(TRANSITION_1_IS_CLOCKWISE);
                            add(TRANSITION_1_IS_CLOCKWISE);
                            add(TRANSITION_1_IS_CLOCKWISE);
                            add(TRANSITION_1_IS_CLOCKWISE);
                        }}, null, MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingColorProviderFactory.make(UUID, VALUES_AT_TIMES,
                        _hueMovementIsClockwise, 12L, null));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteLinearMovingColorProviderFactory.make(UUID, VALUES_AT_TIMES,
                        _hueMovementIsClockwise, MOST_RECENT_TIMESTAMP + 1,
                        MOST_RECENT_TIMESTAMP));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(FiniteLinearMovingColorProviderFactoryImpl.class.getCanonicalName(),
                _finiteLinearMovingColorProviderFactory.getInterfaceName());
    }
}
