package inaugural.soliloquy.graphics.test.unit.rendering;

import inaugural.soliloquy.graphics.rendering.ShaderImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import soliloquy.specs.graphics.rendering.Shader;

import java.nio.FloatBuffer;

import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL20.*;

class ShaderImplTests {
    private static final String SHADER_FILENAME_PREFIX = "./res/shaders/unitTestingShader";

    private static Shader Shader;

    @BeforeAll
    static void setUpFixture() {
        if (!glfwInit()) {
            throw new RuntimeException("GLFW failed to initialize");
        }

        long window = glfwCreateWindow(1, 1, "", 0, 0);
        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        Shader = new ShaderImpl(SHADER_FILENAME_PREFIX);
    }

    @AfterAll
    static void tearDownFixture() {
        glfwTerminate();
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> new ShaderImpl(null));
        assertThrows(IllegalArgumentException.class, () -> new ShaderImpl(""));
    }

    @Test
    void testBindAndUnbind() {
        Shader.bind();

        assertNotEquals(0, glGetInteger(GL_CURRENT_PROGRAM));

        Shader.unbind();

        assertEquals(0, glGetInteger(GL_CURRENT_PROGRAM));
    }

    @Test
    void testSetUniform1f() {
        String uniformName = "zIndex";
        float value = 0.111f;

        Shader.bind();
        Shader.setUniform(uniformName, value);

        int program = glGetInteger(GL_CURRENT_PROGRAM);
        int location = glGetUniformLocation(program, uniformName);

        float valueFromShader = glGetUniformf(program, location);

        assertEquals(value, valueFromShader);
    }

    @Test
    void testSetUniform2f() {
        String uniformName = "pixelScale";
        float value1 = 0.111f;
        float value2 = 0.222f;

        Shader.bind();
        Shader.setUniform(uniformName, value1, value2);

        int program = glGetInteger(GL_CURRENT_PROGRAM);
        int location = glGetUniformLocation(program, uniformName);

        FloatBuffer valuesFromShader = BufferUtils.createFloatBuffer(2);
        glGetUniformfv(program, location, valuesFromShader);

        assertEquals(value1, valuesFromShader.get(0));
        assertEquals(value2, valuesFromShader.get(1));
    }

    @Test
    void testSetUniform3f() {
        String uniformName = "dummyVar";
        float value1 = 0.111f;
        float value2 = 0.222f;
        float value3 = 0.333f;

        Shader.bind();
        Shader.setUniform(uniformName, value1, value2, value3);

        int program = glGetInteger(GL_CURRENT_PROGRAM);
        int location = glGetUniformLocation(program, uniformName);

        FloatBuffer valuesFromShader = BufferUtils.createFloatBuffer(3);
        glGetUniformfv(program, location, valuesFromShader);

        assertEquals(value1, valuesFromShader.get(0));
        assertEquals(value2, valuesFromShader.get(1));
        assertEquals(value3, valuesFromShader.get(2));
    }

    @Test
    void testSetUniform4f() {
        String uniformName = "matColor";
        float value1 = 0.111f;
        float value2 = 0.222f;
        float value3 = 0.333f;
        float value4 = 0.444f;

        Shader.bind();
        Shader.setUniform(uniformName, value1, value2, value3, value4);

        int program = glGetInteger(GL_CURRENT_PROGRAM);
        int location = glGetUniformLocation(program, uniformName);

        FloatBuffer valuesFromShader = BufferUtils.createFloatBuffer(4);
        glGetUniformfv(program, location, valuesFromShader);

        assertEquals(value1, valuesFromShader.get(0));
        assertEquals(value2, valuesFromShader.get(1));
        assertEquals(value3, valuesFromShader.get(2));
        assertEquals(value4, valuesFromShader.get(3));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(soliloquy.specs.graphics.rendering.Shader.class.getCanonicalName(),
                Shader.getInterfaceName());
    }
}
