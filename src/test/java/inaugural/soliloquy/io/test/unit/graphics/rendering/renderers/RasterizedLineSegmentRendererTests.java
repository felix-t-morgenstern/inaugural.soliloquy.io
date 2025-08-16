package inaugural.soliloquy.io.test.unit.graphics.rendering.renderers;

import inaugural.soliloquy.io.graphics.rendering.renderers.RasterizedLineSegmentRenderer;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeRasterizedLineSegmentRenderable;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeStaticProvider;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.graphics.rendering.Mesh;
import soliloquy.specs.io.graphics.rendering.Shader;

import java.awt.*;
import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.randomLong;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

@ExtendWith(MockitoExtension.class)
public class RasterizedLineSegmentRendererTests {
    private final long MOST_RECENT_TIMESTAMP = randomLong();

    @Mock private TimestampValidator mockTimestampValidator;
    @Mock private Mesh mockMesh;
    @Mock private Shader mockShader;

    private RasterizedLineSegmentRenderer renderer;

    @BeforeAll
    public static void setUpFixture() {
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
    public void setUp() {
        renderer = new RasterizedLineSegmentRenderer(mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderer(null));
    }

    @Test
    public void testSetMeshOrShaderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> renderer.setMesh(null));
        assertThrows(IllegalArgumentException.class, () -> renderer.setShader(null));
    }

    @Test
    public void testRenderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> renderer.render(null, 0L));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        null,
                        new FakeStaticProvider<>(vertexOf(0.5f, -0.5f)),
                        new FakeStaticProvider<>(1.0f), (short) 0xAAAA, (short) 1,
                        new FakeStaticProvider<>(Color.WHITE),
                        1, UUID.randomUUID()),
                0L
        ));
        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProvider<>(null),
                        new FakeStaticProvider<>(vertexOf(0.5f, -0.5f)),
                        new FakeStaticProvider<>(1.0f), (short) 0xAAAA, (short) 1,
                        new FakeStaticProvider<>(Color.WHITE),
                        1, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProvider<>(vertexOf(-0.5f, 0.5f)),
                        null,
                        new FakeStaticProvider<>(1.0f), (short) 0xAAAA, (short) 1,
                        new FakeStaticProvider<>(Color.WHITE),
                        1, UUID.randomUUID()),
                0L
        ));
        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProvider<>(vertexOf(-0.5f, 0.5f)),
                        new FakeStaticProvider<>(null),
                        new FakeStaticProvider<>(1.0f), (short) 0xAAAA, (short) 1,
                        new FakeStaticProvider<>(Color.WHITE),
                        1, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProvider<>(vertexOf(-0.5f, 0.5f)),
                        new FakeStaticProvider<>(vertexOf(0.5f, -0.5f)),
                        null, (short) 0xAAAA, (short) 1,
                        new FakeStaticProvider<>(Color.WHITE),
                        1, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProvider<>(vertexOf(-0.5f, 0.5f)),
                        new FakeStaticProvider<>(vertexOf(0.5f, -0.5f)),
                        new FakeStaticProvider<>(null), (short) 0xAAAA, (short) 1,
                        new FakeStaticProvider<>(Color.WHITE),
                        1, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProvider<>(vertexOf(-0.5f, 0.5f)),
                        new FakeStaticProvider<>(vertexOf(0.5f, -0.5f)),
                        new FakeStaticProvider<>(1.0f), (short) 0xAAAA, (short) 1,
                        null,
                        1, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProvider<>(vertexOf(-0.5f, 0.5f)),
                        new FakeStaticProvider<>(vertexOf(0.5f, -0.5f)),
                        new FakeStaticProvider<>(1.0f), (short) 0xAAAA, (short) 1,
                        new FakeStaticProvider<>(null),
                        1, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProvider<>(vertexOf(-0.5f, 0.5f)),
                        new FakeStaticProvider<>(vertexOf(0.5f, -0.5f)),
                        new FakeStaticProvider<>(0f), (short) 0xAAAA, (short) 1,
                        new FakeStaticProvider<>(Color.WHITE),
                        1, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProvider<>(vertexOf(-0.5f, 0.5f)),
                        new FakeStaticProvider<>(vertexOf(0.5f, -0.5f)),
                        new FakeStaticProvider<>(1.0f), (short) 0x0000, (short) 1,
                        new FakeStaticProvider<>(Color.WHITE),
                        1, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProvider<>(vertexOf(-0.5f, 0.5f)),
                        new FakeStaticProvider<>(vertexOf(0.5f, -0.5f)),
                        new FakeStaticProvider<>(1.0f), (short) 0xAAAA, (short) 0,
                        new FakeStaticProvider<>(Color.WHITE),
                        1, UUID.randomUUID()),
                0L
        ));
        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProvider<>(vertexOf(-0.5f, 0.5f)),
                        new FakeStaticProvider<>(vertexOf(0.5f, -0.5f)),
                        new FakeStaticProvider<>(1.0f), (short) 0xAAAA, (short) 257,
                        new FakeStaticProvider<>(Color.WHITE),
                        1, UUID.randomUUID()),
                0L
        ));

        //noinspection RedundantCast
        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProvider<>(vertexOf(-0.5f, 0.5f)),
                        new FakeStaticProvider<>(vertexOf(0.5f, -0.5f)),
                        new FakeStaticProvider<>(1.0f), (short) 0xAAAA, (short) 1,
                        new FakeStaticProvider<>((Color) null),
                        1, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProvider<>(vertexOf(-0.5f, 0.5f)),
                        new FakeStaticProvider<>(vertexOf(0.5f, -0.5f)),
                        new FakeStaticProvider<>(1.0f), (short) 0xAAAA, (short) 1,
                        new FakeStaticProvider<>(Color.WHITE),
                        1, null),
                0L
        ));
    }

    @Test
    public void testRenderWithoutMeshOrShader() {
        FakeRasterizedLineSegmentRenderable lineSegmentRenderable =
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProvider<>(vertexOf(-0.5f, 0.5f)),
                        new FakeStaticProvider<>(vertexOf(0.5f, -0.5f)),
                        new FakeStaticProvider<>(1.0f), (short) 0xAAAA, (short) 1,
                        new FakeStaticProvider<>(Color.WHITE),
                        1, UUID.randomUUID());

        var rendererWithoutMesh = new RasterizedLineSegmentRenderer(mockTimestampValidator);

        rendererWithoutMesh.setShader(mockShader);

        assertThrows(IllegalStateException.class, () -> rendererWithoutMesh
                .render(lineSegmentRenderable, MOST_RECENT_TIMESTAMP));

        var rendererWithoutShader = new RasterizedLineSegmentRenderer(mockTimestampValidator);

        rendererWithoutShader.setMesh(mockMesh);

        assertThrows(IllegalStateException.class, () -> rendererWithoutShader
                .render(lineSegmentRenderable, MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testRenderUpdatesTimestamp() {
        FakeRasterizedLineSegmentRenderable lineSegmentRenderable =
                new FakeRasterizedLineSegmentRenderable(
                        new FakeStaticProvider<>(vertexOf(-0.5f, 0.5f)),
                        new FakeStaticProvider<>(vertexOf(0.5f, -0.5f)),
                        new FakeStaticProvider<>(1.0f), (short) 0xAAAA, (short) 1,
                        new FakeStaticProvider<>(Color.WHITE),
                        1, UUID.randomUUID());
        renderer.setMesh(mockMesh);
        renderer.setShader(mockShader);
        var timestamp = randomLong();

        renderer.render(lineSegmentRenderable, timestamp);

        verify(mockTimestampValidator, once()).validateTimestamp(
                renderer.getClass().getCanonicalName(), timestamp);
    }
}
