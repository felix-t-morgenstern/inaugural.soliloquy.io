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

public class ShaderFactoryImplTests {
    private static final String SHADER_FILENAME_PREFIX = "./src/test/resources/shaders/unitTestingShader";

    private ShaderFactory shaderFactory;

    @BeforeAll
    public static void setUpFixture() {
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
    public void setUp() {
        shaderFactory = new ShaderFactoryImpl();
    }

    @Test
    public void testMake() {
        Shader shader = shaderFactory.make(SHADER_FILENAME_PREFIX);

        assertNotNull(shader);
        assertInstanceOf(ShaderImpl.class, shader);
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> shaderFactory.make(null));
        assertThrows(IllegalArgumentException.class, () -> shaderFactory.make(""));
    }
}
