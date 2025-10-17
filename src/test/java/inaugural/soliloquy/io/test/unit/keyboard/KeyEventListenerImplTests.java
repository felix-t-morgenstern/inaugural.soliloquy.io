package inaugural.soliloquy.io.test.unit.keyboard;

import inaugural.soliloquy.io.keyboard.KeyEventListenerImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lwjgl.opengl.GL;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.input.keyboard.KeyEventHandler;
import soliloquy.specs.io.input.keyboard.KeyEventListener;

import java.awt.*;
import java.awt.event.KeyEvent;

import static inaugural.soliloquy.tools.random.Random.randomLong;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class KeyEventListenerImplTests {
    @Mock private KeyEventHandler mockHandler;

    private static long window;
    private KeyEventListener listener;

    @BeforeAll
    public static void setUpFixture() {
        if (!glfwInit()) {
            throw new RuntimeException("GLFW failed to initialize");
        }

        window = glfwCreateWindow(1, 1, "", 0, 0);
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
    }

    @AfterAll
    static void tearDownFixture() {
        glfwTerminate();
    }

    @BeforeEach
    public void setUp() {
        listener = new KeyEventListenerImpl(mockHandler);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new KeyEventListenerImpl(null));
    }

    @Test
    public void testRegisterKeyListenerAndReportKeyEvents() {
        var timestamp = randomLong();

        listener.registerKeyListener(window);

        try {
            var robot = new Robot();

            robot.keyPress(KeyEvent.VK_P);
            robot.keyRelease(KeyEvent.VK_R);
        }
        catch (AWTException e) {
            throw new RuntimeException(e);
        }

        listener.reportKeyEvents(timestamp);

        verify(mockHandler, once()).press(GLFW_KEY_P, timestamp);
        verify(mockHandler, once()).release(GLFW_KEY_R, timestamp);
    }
}
