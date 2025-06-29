package inaugural.soliloquy.io.test.unit.graphics.rendering;

import inaugural.soliloquy.io.graphics.rendering.MeshImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lwjgl.opengl.GL;
import soliloquy.specs.io.graphics.rendering.Mesh;

import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL30.GL_VERTEX_ARRAY_BINDING;

public class MeshImplTests {
    private final static float[] MESH_DATA =
            new float[]{0f, 1f, 1f, 1f, 1f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};

    private Mesh mesh;

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
        mesh = new MeshImpl(MESH_DATA, MESH_DATA);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new MeshImpl(null, MESH_DATA));
        assertThrows(IllegalArgumentException.class, () -> new MeshImpl(MESH_DATA, null));
    }

    // NB: I cannot figure out how to get the bound vertex array buffers, yet
    @Test
    public void testBindAndUnbind() {
        assertEquals(0, glGetInteger(GL_VERTEX_ARRAY_BINDING));

        mesh.bind();

        assertNotEquals(0, glGetInteger(GL_VERTEX_ARRAY_BINDING));

        mesh.unbind();

        assertEquals(0, glGetInteger(GL_VERTEX_ARRAY_BINDING));
    }
}
