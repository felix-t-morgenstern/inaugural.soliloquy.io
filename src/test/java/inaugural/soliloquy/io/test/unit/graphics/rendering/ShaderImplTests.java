package inaugural.soliloquy.io.test.unit.graphics.rendering;

import inaugural.soliloquy.io.graphics.rendering.ShaderImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import soliloquy.specs.io.graphics.rendering.Shader;

import java.awt.*;

import static inaugural.soliloquy.io.api.Constants.MAX_CHANNEL_VAL;
import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.*;

public class ShaderImplTests {
    private static final String SHADER_FILENAME_PREFIX = "./src/test/resources/shaders/unitTestingShader";

    private static Shader Shader;

    @BeforeAll
    public static void setUpFixture() {
        if (!glfwInit()) {
            throw new RuntimeException("GLFW failed to initialize");
        }

        var window = glfwCreateWindow(1, 1, "", 0, 0);
        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        Shader = new ShaderImpl(SHADER_FILENAME_PREFIX);
    }

    @AfterAll
    static void tearDownFixture() {
        glfwTerminate();
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new ShaderImpl(null));
        assertThrows(IllegalArgumentException.class, () -> new ShaderImpl(""));
    }

    @Test
    public void testBindAndUnbind() {
        Shader.bind();

        assertNotEquals(0, glGetInteger(GL_CURRENT_PROGRAM));

        Shader.unbind();

        assertEquals(0, glGetInteger(GL_CURRENT_PROGRAM));
    }

    @Test
    public void testSetUniform1f() {
        var uniformName = "zIndex";
        var value = 0.111f;

        Shader.bind();
        Shader.setUniform(uniformName, value);

        var program = glGetInteger(GL_CURRENT_PROGRAM);
        var location = glGetUniformLocation(program, uniformName);

        var valueFromShader = glGetUniformf(program, location);

        assertEquals(value, valueFromShader);
    }

    @Test
    public void testSetUniform2f() {
        var uniformName = "pixelScale";
        var value1 = 0.111f;
        var value2 = 0.222f;

        Shader.bind();
        Shader.setUniform(uniformName, value1, value2);

        var program = glGetInteger(GL_CURRENT_PROGRAM);
        var location = glGetUniformLocation(program, uniformName);

        var valuesFromShader = BufferUtils.createFloatBuffer(2);
        glGetUniformfv(program, location, valuesFromShader);

        assertEquals(value1, valuesFromShader.get(0));
        assertEquals(value2, valuesFromShader.get(1));
    }

    @Test
    public void testSetUniform3f() {
        var uniformName = "dummyVar";
        var value1 = 0.111f;
        var value2 = 0.222f;
        var value3 = 0.333f;

        Shader.bind();
        Shader.setUniform(uniformName, value1, value2, value3);

        var program = glGetInteger(GL_CURRENT_PROGRAM);
        var location = glGetUniformLocation(program, uniformName);

        var valuesFromShader = BufferUtils.createFloatBuffer(3);
        glGetUniformfv(program, location, valuesFromShader);

        assertEquals(value1, valuesFromShader.get(0));
        assertEquals(value2, valuesFromShader.get(1));
        assertEquals(value3, valuesFromShader.get(2));
    }

    @Test
    public void testSetUniform4f() {
        var uniformName = "matColor";
        var value1 = 0.111f;
        var value2 = 0.222f;
        var value3 = 0.333f;
        var value4 = 0.444f;

        Shader.bind();
        Shader.setUniform(uniformName, value1, value2, value3, value4);

        var program = glGetInteger(GL_CURRENT_PROGRAM);
        var location = glGetUniformLocation(program, uniformName);

        var valuesFromShader = BufferUtils.createFloatBuffer(4);
        glGetUniformfv(program, location, valuesFromShader);

        assertEquals(value1, valuesFromShader.get(0));
        assertEquals(value2, valuesFromShader.get(1));
        assertEquals(value3, valuesFromShader.get(2));
        assertEquals(value4, valuesFromShader.get(3));
    }

    @Test
    public void testSetUniformColor() {
        var uniformName = "matColor";
        var red = 12;
        var green = 23;
        var blue = 34;
        var alpha = 45;
        var matColor = new Color(red, green, blue, alpha);

        Shader.bind();
        Shader.setUniform(uniformName, matColor);

        var program = glGetInteger(GL_CURRENT_PROGRAM);
        var location = glGetUniformLocation(program, uniformName);

        var valuesFromShader = BufferUtils.createFloatBuffer(4);
        glGetUniformfv(program, location, valuesFromShader);

        assertEquals(red / MAX_CHANNEL_VAL, valuesFromShader.get(0));
        assertEquals(green / MAX_CHANNEL_VAL, valuesFromShader.get(1));
        assertEquals(blue / MAX_CHANNEL_VAL, valuesFromShader.get(2));
        assertEquals(alpha / MAX_CHANNEL_VAL, valuesFromShader.get(3));
    }

    @Test
    public void testSetUniformWithInvalidArgs() {
        var uniformName = "matColor";

        Shader.bind();

        assertThrows(IllegalArgumentException.class,
                () -> Shader.setUniform(null, 0f));
        assertThrows(IllegalArgumentException.class,
                () -> Shader.setUniform("", 0f));

        assertThrows(IllegalArgumentException.class,
                () -> Shader.setUniform(null, 0f, 0f));
        assertThrows(IllegalArgumentException.class,
                () -> Shader.setUniform("", 0f, 0f));

        assertThrows(IllegalArgumentException.class,
                () -> Shader.setUniform(null, 0f, 0f, 0f));
        assertThrows(IllegalArgumentException.class,
                () -> Shader.setUniform("", 0f, 0f, 0f));

        assertThrows(IllegalArgumentException.class,
                () -> Shader.setUniform(null, 0f, 0f, 0f, 0f));
        assertThrows(IllegalArgumentException.class,
                () -> Shader.setUniform("", 0f, 0f, 0f, 0f));

        assertThrows(IllegalArgumentException.class,
                () -> Shader.setUniform(null, Color.RED));
        assertThrows(IllegalArgumentException.class,
                () -> Shader.setUniform("", Color.RED));
        assertThrows(IllegalArgumentException.class,
                () -> Shader.setUniform(uniformName, (Color) null));
    }
}
