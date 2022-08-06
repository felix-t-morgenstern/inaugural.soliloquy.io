package inaugural.soliloquy.graphics.test.unit.renderables.providers.factories;

import inaugural.soliloquy.graphics.renderables.providers.AnimatedMouseCursorProviderImpl;
import inaugural.soliloquy.graphics.renderables.providers.factories.AnimatedMouseCursorProviderFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.AnimatedMouseCursorProviderDefinition;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.renderables.providers.factories.AnimatedMouseCursorProviderFactory;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/** @noinspection FieldCanBeLocal*/
class AnimatedMouseCursorProviderFactoryImplTests {
    private final int MS_1 = 0;
    private final long MOUSE_CURSOR_1 = 123L;
    private final int MS_2 = 111;
    private final long MOUSE_CURSOR_2 = 456L;
    private final int MS_3 = 444;
    private final long MOUSE_CURSOR_3 = 789L;

    private final HashMap<Integer, Long> CURSORS_AT_MS = new HashMap<>();

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
                .make(new AnimatedMouseCursorProviderDefinition(ID, CURSORS_AT_MS, MS_DURATION,
                        PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP));

        assertNotNull(animatedMouseCursorProvider);
        assertTrue(animatedMouseCursorProvider instanceof AnimatedMouseCursorProviderImpl);
    }

    @Test
    void testMakeWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _animatedMouseCursorProviderFactory
                .make(new AnimatedMouseCursorProviderDefinition(null, CURSORS_AT_MS,
                        MS_DURATION, PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP)));
        assertThrows(IllegalArgumentException.class, () -> _animatedMouseCursorProviderFactory
                .make(new AnimatedMouseCursorProviderDefinition("", CURSORS_AT_MS, MS_DURATION,
                        PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP)));

        assertThrows(IllegalArgumentException.class, () -> _animatedMouseCursorProviderFactory
                .make(new AnimatedMouseCursorProviderDefinition(ID, null, MS_DURATION,
                        PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP)));
        assertThrows(IllegalArgumentException.class, () -> _animatedMouseCursorProviderFactory
                .make(new AnimatedMouseCursorProviderDefinition(ID, new HashMap<>(),
                        MS_DURATION, PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP)));
        assertThrows(IllegalArgumentException.class, () -> _animatedMouseCursorProviderFactory
                .make(new AnimatedMouseCursorProviderDefinition(ID, new
                        HashMap<Integer, Long>() {{ put(null, MOUSE_CURSOR_1); }}, MS_DURATION,
                        PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP)));
        assertThrows(IllegalArgumentException.class, () -> _animatedMouseCursorProviderFactory
                .make(new AnimatedMouseCursorProviderDefinition(ID,
                        new HashMap<Integer, Long>() {{ put(-1, MOUSE_CURSOR_1); }}, MS_DURATION,
                        PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP)));
        assertThrows(IllegalArgumentException.class, () -> _animatedMouseCursorProviderFactory
                .make(new AnimatedMouseCursorProviderDefinition(ID,
                        new HashMap<Integer, Long>() {{ put(MS_1, null); }}, MS_DURATION,
                        PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP)));
        assertThrows(IllegalArgumentException.class, () -> _animatedMouseCursorProviderFactory
                .make(new AnimatedMouseCursorProviderDefinition(ID,
                        new HashMap<Integer, Long>() {{ put(1, MOUSE_CURSOR_1); }}, MS_DURATION,
                        PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP)));

        assertThrows(IllegalArgumentException.class, () -> _animatedMouseCursorProviderFactory
                .make(new AnimatedMouseCursorProviderDefinition(ID, CURSORS_AT_MS, 0,
                        PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP)));
        assertThrows(IllegalArgumentException.class, () -> _animatedMouseCursorProviderFactory
                .make(new AnimatedMouseCursorProviderDefinition(ID, CURSORS_AT_MS, MS_3,
                        PERIOD_MODULO_OFFSET, null, MOST_RECENT_TIMESTAMP)));

        assertThrows(IllegalArgumentException.class, () -> _animatedMouseCursorProviderFactory
                .make(new AnimatedMouseCursorProviderDefinition(ID, CURSORS_AT_MS, MS_DURATION,
                        MS_DURATION, null, MOST_RECENT_TIMESTAMP)));
        assertThrows(IllegalArgumentException.class, () -> _animatedMouseCursorProviderFactory
                .make(new AnimatedMouseCursorProviderDefinition(ID, CURSORS_AT_MS, MS_DURATION,
                        -1, null, MOST_RECENT_TIMESTAMP)));

        assertThrows(IllegalArgumentException.class, () -> _animatedMouseCursorProviderFactory
                .make(new AnimatedMouseCursorProviderDefinition(ID, CURSORS_AT_MS, MS_DURATION,
                        PERIOD_MODULO_OFFSET, MOST_RECENT_TIMESTAMP + 1, MOST_RECENT_TIMESTAMP)));
        assertThrows(IllegalArgumentException.class, () -> _animatedMouseCursorProviderFactory
                .make(new AnimatedMouseCursorProviderDefinition(ID, CURSORS_AT_MS, MS_DURATION,
                        PERIOD_MODULO_OFFSET, MOST_RECENT_TIMESTAMP, null)));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(AnimatedMouseCursorProviderFactory.class.getCanonicalName(),
                _animatedMouseCursorProviderFactory.getInterfaceName());
    }

}
