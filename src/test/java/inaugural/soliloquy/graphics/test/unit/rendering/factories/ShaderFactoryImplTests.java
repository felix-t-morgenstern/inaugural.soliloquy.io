package inaugural.soliloquy.graphics.test.unit.rendering.factories;

import inaugural.soliloquy.graphics.rendering.ShaderImpl;
import inaugural.soliloquy.graphics.rendering.factories.ShaderFactoryImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lwjgl.opengl.GL;
import soliloquy.specs.graphics.rendering.Shader;
import soliloquy.specs.graphics.rendering.factories.ShaderFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.glfw.GLFW.*;

class ShaderFactoryImplTests {
    private static final String SHADER_FILENAME_PREFIX = "./src/test/resources/shaders/unitTestingShader";

    private ShaderFactory _shaderFactory;

    @BeforeAll
    static void setUpFixture() {
        if (!glfwInit()) {
            throw new RuntimeException("GLFW failed to initialize");
        }

        long window = glfwCreateWindow(1, 1, "", 0, 0);
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
    }

    @AfterAll
    static void tearDownFixture() {
        glfwTerminate();
    }

    @BeforeEach
    void setUp() {
        _shaderFactory = new ShaderFactoryImpl();
    }

    @Test
    void testMake() {
        Shader shader = _shaderFactory.make(SHADER_FILENAME_PREFIX);

        assertNotNull(shader);
        assertTrue(shader instanceof ShaderImpl);
    }

    @Test
    void testMakeWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _shaderFactory.make(null));
        assertThrows(IllegalArgumentException.class, () -> _shaderFactory.make(""));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(ShaderFactory.class.getCanonicalName(), _shaderFactory.getInterfaceName());
    }
}
