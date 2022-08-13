package inaugural.soliloquy.graphics.test.unit.rendering.renderers;

import inaugural.soliloquy.graphics.rendering.renderers.RasterizedLineSegmentRenderer;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;

class RasterizedLineSegmentRendererTests {
    private final FakeMesh MESH = new FakeMesh();
    private final FakeShader SHADER = new FakeShader();
    private final long MOST_RECENT_TIMESTAMP = 123123L;

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
        _lineSegmentRenderer = new RasterizedLineSegmentRenderer(MOST_RECENT_TIMESTAMP);
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(Renderer.class.getCanonicalName() + "<" +
                        RasterizedLineSegmentRenderable.class.getCanonicalName() + ">",
                _lineSegmentRenderer.getInterfaceName());
    }

    @Test
    void testSetMeshOrShaderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.setMesh(null));
        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.setShader(null));
    }

    @Test
    void testRenderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(null, 0L));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        null, (short) 0xAAAA, (short) 1,
                        new FakeStaticProvider<>(Color.WHITE),
                        new FakeStaticProvider<>(new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f)),
                        1, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProvider<>(null), (short) 0xAAAA, (short) 1,
                        new FakeStaticProvider<>(Color.WHITE),
                        new FakeStaticProvider<>(new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f)),
                        1, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProvider<>(1.0f), (short) 0xAAAA, (short) 1,
                        null,
                        new FakeStaticProvider<>(new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f)),
                        1, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProvider<>(1.0f), (short) 0xAAAA, (short) 1,
                        new FakeStaticProvider<>(null),
                        new FakeStaticProvider<>(new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f)),
                        1, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProvider<>(1.0f), (short) 0xAAAA, (short) 1,
                        new FakeStaticProvider<>(Color.WHITE),
                        null,
                        1, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProvider<>(1.0f), (short) 0xAAAA, (short) 1,
                        new FakeStaticProvider<>(Color.WHITE),
                        new FakeStaticProvider<>(null),
                        1, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProvider<>(0f), (short) 0xAAAA, (short) 1,
                        new FakeStaticProvider<>(Color.WHITE),
                        new FakeStaticProvider<>(new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f)),
                        1, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProvider<>(1.0f), (short) 0x0000, (short) 1,
                        new FakeStaticProvider<>(Color.WHITE),
                        new FakeStaticProvider<>(new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f)),
                        1, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProvider<>(1.0f), (short) 0xAAAA, (short) 0,
                        new FakeStaticProvider<>(Color.WHITE),
                        new FakeStaticProvider<>(new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f)),
                        1, UUID.randomUUID()),
                0L
        ));
        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProvider<>(1.0f), (short) 0xAAAA, (short) 257,
                        new FakeStaticProvider<>(Color.WHITE),
                        new FakeStaticProvider<>(new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f)),
                        1, UUID.randomUUID()),
                0L
        ));

        //noinspection RedundantCast
        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProvider<>(1.0f), (short) 0xAAAA, (short) 1,
                        new FakeStaticProvider<>((Color) null),
                        new FakeStaticProvider<>(new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f)),
                        1, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProvider<>(1.0f), (short) 0xAAAA, (short) 1,
                        new FakeStaticProvider<>(Color.WHITE),
                        null,
                        1, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProvider<>(1.0f), (short) 0xAAAA, (short) 1,
                        new FakeStaticProvider<>(Color.WHITE),
                        new FakeStaticProvider<>(null),
                        1, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _lineSegmentRenderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProvider<>(1.0f), (short) 0xAAAA, (short) 1,
                        new FakeStaticProvider<>(Color.WHITE),
                        new FakeStaticProvider<>(new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f)),
                        1, null),
                0L
        ));
    }

    @Test
    void testRenderOutdatedTimestamp() {
        FakeRasterizedLineSegmentRenderable lineSegmentRenderable =
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProvider<>(1.0f), (short) 0xAAAA, (short) 1,
                        new FakeStaticProvider<>(Color.WHITE),
                        new FakeStaticProvider<>(new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f)),
                        1, UUID.randomUUID());
        _lineSegmentRenderer.setMesh(MESH);
        _lineSegmentRenderer.setShader(SHADER);

        assertThrows(IllegalArgumentException.class, () ->
                _lineSegmentRenderer.render(lineSegmentRenderable, MOST_RECENT_TIMESTAMP - 1L));
    }

    @Test
    void testRenderWithoutMeshOrShader() {
        FakeRasterizedLineSegmentRenderable lineSegmentRenderable =
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProvider<>(1.0f), (short) 0xAAAA, (short) 1,
                        new FakeStaticProvider<>(Color.WHITE),
                        new FakeStaticProvider<>(new FakeFloatBox(-0.5f, 0.5f, 0.5f, -0.5f)),
                        1, UUID.randomUUID());

        Renderer<RasterizedLineSegmentRenderable> lineSegmentRendererWithoutMesh =
                new RasterizedLineSegmentRenderer(MOST_RECENT_TIMESTAMP);

        lineSegmentRendererWithoutMesh.setShader(SHADER);

        assertThrows(IllegalStateException.class, () -> lineSegmentRendererWithoutMesh
                .render(lineSegmentRenderable, MOST_RECENT_TIMESTAMP));

        Renderer<RasterizedLineSegmentRenderable> lineSegmentRendererWithoutShader =
                new RasterizedLineSegmentRenderer(MOST_RECENT_TIMESTAMP);

        lineSegmentRendererWithoutShader.setMesh(MESH);

        assertThrows(IllegalStateException.class, () -> lineSegmentRendererWithoutShader
                .render(lineSegmentRenderable, MOST_RECENT_TIMESTAMP));
    }

    @Test
    void testMostRecentTimestamp() {
        assertEquals(MOST_RECENT_TIMESTAMP, (long) _lineSegmentRenderer.mostRecentTimestamp());
    }
}
