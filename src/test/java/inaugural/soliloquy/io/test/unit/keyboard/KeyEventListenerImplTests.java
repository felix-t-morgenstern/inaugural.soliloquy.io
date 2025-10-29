package inaugural.soliloquy.io.test.unit.keyboard;

import inaugural.soliloquy.io.keyboard.KeyEventListenerImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class KeyEventListenerImplTests {
    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new KeyEventListenerImpl(null));
    }

    // NB: KeyEventListener behavior cannot be tested within a unit test, since java.awt.Robot
    // can only simulate key press events within the current context, but KeyEventListenerImpl
    // listens to events in a window spun up by OpenGL. To test its behavior, c.f.
    // KeyEventDisplayTest
}
