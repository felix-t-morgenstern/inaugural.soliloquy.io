package inaugural.soliloquy.io.keyboard;

import inaugural.soliloquy.tools.Check;
import org.lwjgl.glfw.GLFWKeyCallback;
import soliloquy.specs.io.input.keyboard.KeyEventHandler;
import soliloquy.specs.io.input.keyboard.KeyEventListener;

import java.util.Set;

import static inaugural.soliloquy.tools.collections.Collections.setOf;
import static org.lwjgl.glfw.GLFW.*;

public class KeyEventListenerImpl implements KeyEventListener {
    private final KeyEventHandler HANDLER;
    private final Set<Integer> KEYS_PRESSED;
    private final Set<Integer> KEYS_RELEASED;

    public KeyEventListenerImpl(KeyEventHandler handler) {
        HANDLER = Check.ifNull(handler, "handler");
        KEYS_PRESSED = setOf();
        KEYS_RELEASED = setOf();
    }

    @Override
    public void registerKeyListener(long window) {
        glfwSetKeyCallback(window,
                new GLFWKeyCallback() {
                    @Override
                    public void invoke(long window, int key, int scancode, int action, int mods) {
                        if (action == GLFW_PRESS) {
                            KEYS_PRESSED.add(key);
                        }
                        else if (action == GLFW_RELEASE) {
                            KEYS_RELEASED.add(key);
                        }
                    }
                });
    }

    @Override
    public void reportKeyEvents(long timestamp) {
        KEYS_PRESSED.forEach(k -> HANDLER.press(k, timestamp));
        KEYS_RELEASED.forEach(k -> HANDLER.release(k, timestamp));

        KEYS_PRESSED.clear();
        KEYS_RELEASED.clear();
    }
}
