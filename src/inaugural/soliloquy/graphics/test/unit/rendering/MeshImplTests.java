package inaugural.soliloquy.graphics.test.unit.rendering;

import inaugural.soliloquy.graphics.rendering.MeshImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lwjgl.opengl.GL;
import soliloquy.specs.graphics.rendering.Mesh;

import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL30.GL_VERTEX_ARRAY_BINDING;

class MeshImplTests {
    private final static float[] MESH_DATA =
            new float[] {0f, 1f, 1f, 1f, 1f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};

    private Mesh _mesh;

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
        _mesh = new MeshImpl(MESH_DATA, MESH_DATA);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> new MeshImpl(null, MESH_DATA));
        assertThrows(IllegalArgumentException.class, () -> new MeshImpl(MESH_DATA, null));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(Mesh.class.getCanonicalName(), _mesh.getInterfaceName());
    }

    // NB: I cannot figure out how to get the bound vertex array buffers, yet
    @Test
    void testBindAndUnbind() {
        assertEquals(0, glGetInteger(GL_VERTEX_ARRAY_BINDING));

        _mesh.bind();

        assertNotEquals(0, glGetInteger(GL_VERTEX_ARRAY_BINDING));

        _mesh.unbind();

        assertEquals(0, glGetInteger(GL_VERTEX_ARRAY_BINDING));
    }
}
