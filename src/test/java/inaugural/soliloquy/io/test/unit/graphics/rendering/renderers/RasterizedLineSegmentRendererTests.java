package inaugural.soliloquy.io.test.unit.graphics.rendering.renderers;

import inaugural.soliloquy.io.graphics.rendering.renderers.RasterizedLineSegmentRenderer;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.Mesh;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.io.graphics.rendering.Shader;

import java.awt.*;
import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.randomLong;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static inaugural.soliloquy.tools.testing.Mock.generateMockStaticProvider;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

@ExtendWith(MockitoExtension.class)
public class RasterizedLineSegmentRendererTests {
    private final long MOST_RECENT_TIMESTAMP = randomLong();

    @Mock private TimestampValidator mockTimestampValidator;
    @Mock private RenderingBoundaries mockRenderingBoundaries;
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
        renderer = new RasterizedLineSegmentRenderer(mockTimestampValidator, mockRenderingBoundaries);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderer(null, mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderer(mockTimestampValidator, null));
    }

    @Test
    public void testSetMeshOrShaderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> renderer.setMesh(null));
        assertThrows(IllegalArgumentException.class, () -> renderer.setShader(null));
    }

    @Test
    public void testRenderWithInvalidArgs() {
        renderer.setMesh(mockMesh);
        renderer.setShader(mockShader);

        assertThrows(IllegalArgumentException.class, () -> renderer.render(null, 0L));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                makeMockRenderable(
                        null,
                        generateMockStaticProvider(vertexOf(0.5f, -0.5f)),
                        generateMockStaticProvider(1.0f), (short) 0xAAAA, (short) 1,
                        generateMockStaticProvider(Color.WHITE),
                        1, UUID.randomUUID()),
                0L
        ));
        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                makeMockRenderable(
                        generateMockStaticProvider(null),
                        generateMockStaticProvider(vertexOf(0.5f, -0.5f)),
                        generateMockStaticProvider(1.0f), (short) 0xAAAA, (short) 1,
                        generateMockStaticProvider(Color.WHITE),
                        1, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                makeMockRenderable(
                        generateMockStaticProvider(vertexOf(-0.5f, 0.5f)),
                        null,
                        generateMockStaticProvider(1.0f), (short) 0xAAAA, (short) 1,
                        generateMockStaticProvider(Color.WHITE),
                        1, UUID.randomUUID()),
                0L
        ));
        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                makeMockRenderable(
                        generateMockStaticProvider(vertexOf(-0.5f, 0.5f)),
                        generateMockStaticProvider(null),
                        generateMockStaticProvider(1.0f), (short) 0xAAAA, (short) 1,
                        generateMockStaticProvider(Color.WHITE),
                        1, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                makeMockRenderable(
                        generateMockStaticProvider(vertexOf(-0.5f, 0.5f)),
                        generateMockStaticProvider(vertexOf(0.5f, -0.5f)),
                        null, (short) 0xAAAA, (short) 1,
                        generateMockStaticProvider(Color.WHITE),
                        1, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                makeMockRenderable(
                        generateMockStaticProvider(vertexOf(-0.5f, 0.5f)),
                        generateMockStaticProvider(vertexOf(0.5f, -0.5f)),
                        generateMockStaticProvider(null), (short) 0xAAAA, (short) 1,
                        generateMockStaticProvider(Color.WHITE),
                        1, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                makeMockRenderable(
                        generateMockStaticProvider(vertexOf(-0.5f, 0.5f)),
                        generateMockStaticProvider(vertexOf(0.5f, -0.5f)),
                        generateMockStaticProvider(1.0f), (short) 0xAAAA, (short) 1,
                        null,
                        1, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                makeMockRenderable(
                        generateMockStaticProvider(vertexOf(-0.5f, 0.5f)),
                        generateMockStaticProvider(vertexOf(0.5f, -0.5f)),
                        generateMockStaticProvider(1.0f), (short) 0xAAAA, (short) 1,
                        generateMockStaticProvider(null),
                        1, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                makeMockRenderable(
                        generateMockStaticProvider(vertexOf(-0.5f, 0.5f)),
                        generateMockStaticProvider(vertexOf(0.5f, -0.5f)),
                        generateMockStaticProvider(0f), (short) 0xAAAA, (short) 1,
                        generateMockStaticProvider(Color.WHITE),
                        1, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                makeMockRenderable(
                        generateMockStaticProvider(vertexOf(-0.5f, 0.5f)),
                        generateMockStaticProvider(vertexOf(0.5f, -0.5f)),
                        generateMockStaticProvider(1.0f), (short) 0x0000, (short) 1,
                        generateMockStaticProvider(Color.WHITE),
                        1, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                makeMockRenderable(
                        generateMockStaticProvider(vertexOf(-0.5f, 0.5f)),
                        generateMockStaticProvider(vertexOf(0.5f, -0.5f)),
                        generateMockStaticProvider(1.0f), (short) 0xAAAA, (short) 0,
                        generateMockStaticProvider(Color.WHITE),
                        1, UUID.randomUUID()),
                0L
        ));
        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                makeMockRenderable(
                        generateMockStaticProvider(vertexOf(-0.5f, 0.5f)),
                        generateMockStaticProvider(vertexOf(0.5f, -0.5f)),
                        generateMockStaticProvider(1.0f), (short) 0xAAAA, (short) 257,
                        generateMockStaticProvider(Color.WHITE),
                        1, UUID.randomUUID()),
                0L
        ));

        //noinspection RedundantCast
        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                makeMockRenderable(
                        generateMockStaticProvider(vertexOf(-0.5f, 0.5f)),
                        generateMockStaticProvider(vertexOf(0.5f, -0.5f)),
                        generateMockStaticProvider(1.0f), (short) 0xAAAA, (short) 1,
                        generateMockStaticProvider((Color) null),
                        1, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                makeMockRenderable(
                        generateMockStaticProvider(vertexOf(-0.5f, 0.5f)),
                        generateMockStaticProvider(vertexOf(0.5f, -0.5f)),
                        generateMockStaticProvider(1.0f), (short) 0xAAAA, (short) 1,
                        generateMockStaticProvider(Color.WHITE),
                        1, null),
                0L
        ));
    }

    @Test
    public void testRenderWithoutMeshOrShader() {
        var lineSegmentRenderable = makeMockRenderable(
                        generateMockStaticProvider(vertexOf(-0.5f, 0.5f)),
                        generateMockStaticProvider(vertexOf(0.5f, -0.5f)),
                        generateMockStaticProvider(1.0f), (short) 0xAAAA, (short) 1,
                        generateMockStaticProvider(Color.WHITE),
                        1, UUID.randomUUID());

        var rendererWithoutMesh = new RasterizedLineSegmentRenderer(mockTimestampValidator, mockRenderingBoundaries);

        rendererWithoutMesh.setShader(mockShader);

        assertThrows(IllegalStateException.class, () -> rendererWithoutMesh
                .render(lineSegmentRenderable, MOST_RECENT_TIMESTAMP));

        var rendererWithoutShader = new RasterizedLineSegmentRenderer(mockTimestampValidator, mockRenderingBoundaries);

        rendererWithoutShader.setMesh(mockMesh);

        assertThrows(IllegalStateException.class, () -> rendererWithoutShader
                .render(lineSegmentRenderable, MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testRenderUpdatesTimestamp() {
        var lineSegmentRenderable = makeMockRenderable(
                        generateMockStaticProvider(vertexOf(-0.5f, 0.5f)),
                        generateMockStaticProvider(vertexOf(0.5f, -0.5f)),
                        generateMockStaticProvider(1.0f), (short) 0xAAAA, (short) 1,
                        generateMockStaticProvider(Color.WHITE),
                        1, UUID.randomUUID());
        renderer.setMesh(mockMesh);
        renderer.setShader(mockShader);
        var timestamp = randomLong();

        renderer.render(lineSegmentRenderable, timestamp);

        verify(mockTimestampValidator, once()).validateTimestamp(
                renderer.getClass().getCanonicalName(), timestamp);
    }

    private RasterizedLineSegmentRenderable makeMockRenderable(
            ProviderAtTime<Vertex> vertex1Provider,
            ProviderAtTime<Vertex> vertex2Provider,
            ProviderAtTime<Float> thicknessProvider,
            short stipplePattern,
            short stippleFactor,
            ProviderAtTime<Color> colorProvider,
            int z, UUID uuid
    ) {
        var mockRenderable = mock(RasterizedLineSegmentRenderable.class);

        lenient().when(mockRenderable.getVertex1Provider()).thenReturn(vertex1Provider);
        lenient().when(mockRenderable.getVertex2Provider()).thenReturn(vertex2Provider);
        lenient().when(mockRenderable.getThicknessProvider()).thenReturn(thicknessProvider);
        lenient().when(mockRenderable.getStippleFactor()).thenReturn(stippleFactor);
        lenient().when(mockRenderable.getStipplePattern()).thenReturn(stipplePattern);
        lenient().when(mockRenderable.getColorProvider()).thenReturn(colorProvider);
        lenient().when(mockRenderable.getZ()).thenReturn(z);
        lenient().when(mockRenderable.uuid()).thenReturn(uuid);

        return mockRenderable;
    }
}
