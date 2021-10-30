package inaugural.soliloquy.graphics.test.unit.renderables.providers.factories;

import inaugural.soliloquy.graphics.renderables.providers.AnimatedMouseCursorProviderImpl;
import inaugural.soliloquy.graphics.renderables.providers.factories.AnimatedMouseCursorProviderFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.renderables.providers.factories.AnimatedMouseCursorProviderFactory;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/** @noinspection FieldCanBeLocal*/
class AnimatedMouseCursorProviderFactoryImplTests {
    private final long MS_1 = 0L;
    private final long MOUSE_CURSOR_1 = 123L;
    private final long MS_2 = 111L;
    private final long MOUSE_CURSOR_2 = 456L;
    private final long MS_3 = 444L;
    private final long MOUSE_CURSOR_3 = 789L;

    private final HashMap<Long, Long> CURSORS_AT_MS = new HashMap<>();

    private final String ID = "id";
    private final int MS_DURATION = 777;
    private final int PERIOD_MODULO_OFFSET = 321;
    private final long MOST_RECENT_TIMESTAMP = 12L;

    private AnimatedMouseCursorProviderFactory _animatedMouseCursorProviderFactory;

    @BeforeEach
    void setUp() {
        CURSORS_AT_MS.put(MS_1, MOUSE_CURSOR_1);
        CURSORS_AT_MS.put(MS_2, MOUSE_CURSOR_2);
        CURSORS_AT_MS.put(MS_3, MOUSE_CURSOR_3);

        _animatedMouseCursorProviderFactory = new AnimatedMouseCursorProviderFactoryImpl();
    }

    @Test
    void testMake() {
        ProviderAtTime<Long> animatedMouseCursorProvider = _animatedMouseCursorProviderFactory
                .make(ID, CURSORS_AT_MS, MS_DURATION, PERIOD_MODULO_OFFSET, null,
                        MOST_RECENT_TIMESTAMP);

        assertNotNull(animatedMouseCursorProvider);
        assertTrue(animatedMouseCursorProvider instanceof AnimatedMouseCursorProviderImpl);
    }

    @Test
    void testMakeWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _animatedMouseCursorProviderFactory
                .make(null, CURSORS_AT_MS, MS_DURATION, PERIOD_MODULO_OFFSET, null,
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _animatedMouseCursorProviderFactory
                .make("", CURSORS_AT_MS, MS_DURATION, PERIOD_MODULO_OFFSET, null,
                        MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class, () -> _animatedMouseCursorProviderFactory
                .make(ID, null, MS_DURATION, PERIOD_MODULO_OFFSET, null,
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _animatedMouseCursorProviderFactory
                .make(ID, new HashMap<>(), MS_DURATION, PERIOD_MODULO_OFFSET, null,
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _animatedMouseCursorProviderFactory
                .make(ID, new HashMap<Long, Long>() {{
                    put(null, MOUSE_CURSOR_1);
                }}, MS_DURATION, PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _animatedMouseCursorProviderFactory
                .make(ID, new HashMap<Long, Long>() {{
                    put(-1L, MOUSE_CURSOR_1);
                }}, MS_DURATION, PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _animatedMouseCursorProviderFactory
                .make(ID, new HashMap<Long, Long>() {{
                    put(MS_1, null);
                }}, MS_DURATION, PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _animatedMouseCursorProviderFactory
                .make(ID, new HashMap<Long, Long>() {{
                    put(1L, MOUSE_CURSOR_1);
                }}, MS_DURATION, PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class, () -> _animatedMouseCursorProviderFactory
                .make(ID, CURSORS_AT_MS, 0, PERIOD_MODULO_OFFSET, null,
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _animatedMouseCursorProviderFactory
                .make(ID, CURSORS_AT_MS, (int)MS_3, PERIOD_MODULO_OFFSET, null,
                        MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class, () -> _animatedMouseCursorProviderFactory
                .make(ID, CURSORS_AT_MS, MS_DURATION, MS_DURATION, null,
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _animatedMouseCursorProviderFactory
                .make(ID, CURSORS_AT_MS, MS_DURATION, -1, null,
                        MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class, () -> _animatedMouseCursorProviderFactory
                .make(ID, CURSORS_AT_MS, MS_DURATION, PERIOD_MODULO_OFFSET,
                        MOST_RECENT_TIMESTAMP + 1, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _animatedMouseCursorProviderFactory
                .make(ID, CURSORS_AT_MS, MS_DURATION, PERIOD_MODULO_OFFSET,
                        MOST_RECENT_TIMESTAMP, null));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(AnimatedMouseCursorProviderFactory.class.getCanonicalName(),
                _animatedMouseCursorProviderFactory.getInterfaceName());
    }
}
