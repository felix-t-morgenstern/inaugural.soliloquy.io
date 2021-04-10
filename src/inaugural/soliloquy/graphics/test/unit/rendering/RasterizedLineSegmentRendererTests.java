package inaugural.soliloquy.graphics.test.unit.rendering;

import inaugural.soliloquy.graphics.rendering.RasterizedLineSegmentRenderer;
import inaugural.soliloquy.graphics.test.fakes.FakeFloatBox;
import inaugural.soliloquy.graphics.test.fakes.FakeRasterizedLineSegmentRenderable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.graphics.rendering.Renderer;

import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL.createCapabilities;

class RasterizedLineSegmentRendererTests {
    private RasterizedLineSegmentRenderer _lineSegmentRenderer;

    @BeforeAll
    static void setUpFixture() {
        if (!glfwInit()) {
            throw new RuntimeException("GLFW failed to initialize");
        }

        long window = glfwCreateWindow(1, 1, "", 0, 0);
        glfwMakeContextCurrent(window);
        createCapabilities();
    }

    @AfterAll
    static void tearDownFixture() {
        glfwTerminate();
    }

    @BeforeEach
    void setUp() {
        _lineSegmentRenderer = new RasterizedLineSegmentRenderer();
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(Renderer.class.getCanonicalName() + "<" +
                        RasterizedLineSegmentRenderable.class.getCanonicalName() + ">",
                _lineSegmentRenderer.getInterfaceName());
    }

    @Test
    void testSetMesh() {
        assertThrows(UnsupportedOperationException.class,
                () -> _lineSegmentRenderer.setMesh(null));
    }

    @Test
    void testSetShader() {
        assertThrows(UnsupportedOperationException.class,
                () -> _lineSegmentRenderer.setShader(null));
    }

    @Test
    void testRenderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(null, 0L));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(0f, (short)0xAAAA,
                        1, 1.0f, 1.0f, 1.0f, 1.0f,
                        new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f), 1),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0x0000,
                        1, 1.0f, 1.0f, 1.0f, 1.0f,
                        new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f), 1),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0xAAAA,
                        0, 1.0f, 1.0f, 1.0f, 1.0f,
                        new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f), 1),
                0L
        ));
        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0xAAAA,
                        257, 1.0f, 1.0f, 1.0f, 1.0f,
                        new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f), 1),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0xAAAA, 1,
                        1.0001f, 1.0f, 1.0f, 1.0f,
                        new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f), 1),
                0L
        ));
        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0xAAAA, 1,
                        -0.0001f, 1.0f, 1.0f, 1.0f,
                        new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f), 1),
                0L
        ));
        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0xAAAA, 1,
                        1.0f, 1.0001f, 1.0f, 1.0f,
                        new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f), 1),
                0L
        ));
        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0xAAAA, 1,
                        1.0f, -0.0001f, 1.0f, 1.0f,
                        new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f), 1),
                0L
        ));
        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0xAAAA, 1,
                        1.0f, 1.0f, 1.0001f, 1.0f,
                        new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f), 1),
                0L
        ));
        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0xAAAA, 1,
                        1.0f, 1.0f, -0.0001f, 1.0f,
                        new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f), 1),
                0L
        ));
        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0xAAAA, 1,
                        1.0f, 1.0f, 1.0f, 1.0001f,
                        new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f), 1),
                0L
        ));
        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0xAAAA, 1,
                        1.0f, 1.0f, 1.0f, -0.0001f,
                        new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f), 1),
                0L
        ));
    }

    @Test
    void testRenderOutdatedTimestamp() {
        FakeRasterizedLineSegmentRenderable lineSegmentRenderable =
                new FakeRasterizedLineSegmentRenderable(1.0f, (short)0xAAAA, 1,
                        1.0f, 1.0f, 1.0f, 1.0f,
                        new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f), 1);
        long timestamp = 100L;
        _lineSegmentRenderer.render(lineSegmentRenderable, timestamp);

        assertThrows(IllegalArgumentException.class,
                () -> _lineSegmentRenderer.render(lineSegmentRenderable, timestamp - 1L));
    }
}
