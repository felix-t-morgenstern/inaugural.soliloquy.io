package inaugural.soliloquy.io.test.unit.mouse;

import inaugural.soliloquy.io.mouse.MouseCursorImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeGlobalClock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.io.mouse.MouseCursor;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.util.Map;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MouseCursorImplTests {
    private final Map<String, ProviderAtTime<Long>> MOUSE_CURSORS = mapOf();
    private final FakeGlobalClock GLOBAL_CLOCK = new FakeGlobalClock();

    private MouseCursor mouseCursor;

    @BeforeEach
    public void setUp() {
        mouseCursor = new MouseCursorImpl(MOUSE_CURSORS, GLOBAL_CLOCK);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new MouseCursorImpl(null, GLOBAL_CLOCK));
        assertThrows(IllegalArgumentException.class, () ->
                new MouseCursorImpl(MOUSE_CURSORS, null));
    }

    @Test
    public void testSetMouseCursorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> mouseCursor.setMouseCursor(null));
        assertThrows(IllegalArgumentException.class, () -> mouseCursor.setMouseCursor(""));
        assertThrows(IllegalArgumentException.class, () -> mouseCursor.setMouseCursor("invalid"));
    }
}
