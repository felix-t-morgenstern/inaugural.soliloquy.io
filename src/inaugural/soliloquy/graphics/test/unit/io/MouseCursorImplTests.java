package inaugural.soliloquy.graphics.test.unit.io;

import inaugural.soliloquy.graphics.io.MouseCursorImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.io.MouseCursor;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MouseCursorImplTests {
    private final Map<String, ProviderAtTime<Long>> MOUSE_CURSORS = new HashMap<>();
    private final FakeGlobalClock GLOBAL_CLOCK = new FakeGlobalClock();

    private MouseCursor _mouseCursor;

    @BeforeEach
    void setUp() {
        _mouseCursor = new MouseCursorImpl(MOUSE_CURSORS, GLOBAL_CLOCK);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new MouseCursorImpl(null, GLOBAL_CLOCK));
        assertThrows(IllegalArgumentException.class, () ->
                new MouseCursorImpl(MOUSE_CURSORS, null));
    }

    @Test
    void testSetMouseCursorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _mouseCursor.setMouseCursor(null));
        assertThrows(IllegalArgumentException.class, () -> _mouseCursor.setMouseCursor(""));
        assertThrows(IllegalArgumentException.class, () -> _mouseCursor.setMouseCursor("invalid"));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(MouseCursor.class.getCanonicalName(), _mouseCursor.getInterfaceName());
    }
}
