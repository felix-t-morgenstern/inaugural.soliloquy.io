package inaugural.soliloquy.graphics.test.unit.rendering.renderers;

import inaugural.soliloquy.graphics.rendering.renderers.RasterizedLineSegmentRenderer;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeEntityUuid;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeFloatBox;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeRasterizedLineSegmentRenderable;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeStaticProviderAtTime;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;

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

    // TODO: Add test cases for null providers
    @Test
    void testRenderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(null, 0L));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        null, (short)0xAAAA, 1,
                        new FakeStaticProviderAtTime<>(Color.WHITE),
                        new FakeStaticProviderAtTime<>(new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f)),
                        1, new FakeEntityUuid()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProviderAtTime<>(1.0f), (short)0xAAAA, 1,
                        null,
                        new FakeStaticProviderAtTime<>(new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f)),
                        1, new FakeEntityUuid()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProviderAtTime<>(1.0f), (short)0xAAAA, 1,
                        new FakeStaticProviderAtTime<>(Color.WHITE),
                        null,
                        1, new FakeEntityUuid()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProviderAtTime<>(0f), (short)0xAAAA, 1,
                        new FakeStaticProviderAtTime<>(Color.WHITE),
                        new FakeStaticProviderAtTime<>(new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f)),
                        1, new FakeEntityUuid()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProviderAtTime<>(1.0f), (short)0x0000, 1,
                        new FakeStaticProviderAtTime<>(Color.WHITE),
                        new FakeStaticProviderAtTime<>(new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f)),
                        1, new FakeEntityUuid()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProviderAtTime<>(1.0f), (short)0xAAAA, 0,
                        new FakeStaticProviderAtTime<>(Color.WHITE),
                        new FakeStaticProviderAtTime<>(new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f)),
                        1, new FakeEntityUuid()),
                0L
        ));
        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProviderAtTime<>(1.0f), (short)0xAAAA, 257,
                        new FakeStaticProviderAtTime<>(Color.WHITE),
                        new FakeStaticProviderAtTime<>(new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f)),
                        1, new FakeEntityUuid()),
                0L
        ));

        //noinspection RedundantCast
        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProviderAtTime<>(1.0f), (short)0xAAAA, 1,
                        new FakeStaticProviderAtTime<>((Color)null),
                        new FakeStaticProviderAtTime<>(new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f)),
                        1, new FakeEntityUuid()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProviderAtTime<>(1.0f), (short)0xAAAA, 1,
                        new FakeStaticProviderAtTime<>(Color.WHITE),
                        null,
                        1, new FakeEntityUuid()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProviderAtTime<>(1.0f), (short)0xAAAA, 1,
                        new FakeStaticProviderAtTime<>(Color.WHITE),
                        new FakeStaticProviderAtTime<>(null),
                        1, new FakeEntityUuid()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProviderAtTime<>(1.0f), (short)0xAAAA, 1,
                        new FakeStaticProviderAtTime<>(Color.WHITE),
                        new FakeStaticProviderAtTime<>(new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f)),
                        1, null),
                0L
        ));
    }

    @Test
    void testRenderOutdatedTimestamp() {
        FakeRasterizedLineSegmentRenderable lineSegmentRenderable =
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProviderAtTime<>(1.0f), (short)0xAAAA, 1,
                        new FakeStaticProviderAtTime<>(Color.WHITE),
                        new FakeStaticProviderAtTime<>(new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f)),
                        1, new FakeEntityUuid());
        long timestamp = 100L;
        _lineSegmentRenderer.render(lineSegmentRenderable, timestamp);

        assertThrows(IllegalArgumentException.class,
                () -> _lineSegmentRenderer.render(lineSegmentRenderable, timestamp - 1L));
    }
}
