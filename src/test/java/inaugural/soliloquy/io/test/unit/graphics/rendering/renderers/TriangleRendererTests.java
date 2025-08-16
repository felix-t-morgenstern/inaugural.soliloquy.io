package inaugural.soliloquy.io.test.unit.graphics.rendering.renderers;

import inaugural.soliloquy.io.graphics.rendering.renderers.TriangleRenderer;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeStaticProvider;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeTriangleRenderable;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.TriangleRenderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.Mesh;
import soliloquy.specs.io.graphics.rendering.Shader;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.awt.*;

import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

@ExtendWith(MockitoExtension.class)
public class TriangleRendererTests {
    private final ProviderAtTime<Vertex> VERTEX_1_PROVIDER =
            new FakeStaticProvider<>(vertexOf(randomFloat(), randomFloat()));
    private final ProviderAtTime<Color> VERTEX_1_COLOR_PROVIDER =
            new FakeStaticProvider<>(randomColor());
    private final ProviderAtTime<Vertex> VERTEX_2_PROVIDER =
            new FakeStaticProvider<>(vertexOf(randomFloat(), randomFloat()));
    private final ProviderAtTime<Color> VERTEX_2_COLOR_PROVIDER =
            new FakeStaticProvider<>(randomColor());
    private final ProviderAtTime<Vertex> VERTEX_3_PROVIDER =
            new FakeStaticProvider<>(vertexOf(randomFloat(), randomFloat()));
    private final ProviderAtTime<Color> VERTEX_3_COLOR_PROVIDER =
            new FakeStaticProvider<>(randomColor());
    private final ProviderAtTime<Integer> BACKGROUND_TEXTURE_ID_PROVIDER =
            new FakeStaticProvider<>(randomInt());
    @SuppressWarnings("unchecked") private final ProviderAtTime<Float>
            MOCK_TEXTURE_TILE_WIDTH_PROVIDER = (ProviderAtTime<Float>) mock(ProviderAtTime.class);
    @SuppressWarnings("unchecked") private final ProviderAtTime<Float>
            MOCK_TEXTURE_TILE_HEIGHT_PROVIDER = (ProviderAtTime<Float>) mock(ProviderAtTime.class);
    private final TriangleRenderable TRIANGLE_RENDERABLE =
            new FakeTriangleRenderable(VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                    VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER, VERTEX_3_PROVIDER,
                    VERTEX_3_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                    MOCK_TEXTURE_TILE_WIDTH_PROVIDER, MOCK_TEXTURE_TILE_HEIGHT_PROVIDER);

    private final Long MOST_RECENT_TIMESTAMP = randomLong();

    @Mock private TimestampValidator mockTimestampValidator;
    @Mock private Mesh mockMesh;
    @Mock private Shader mockShader;

    private Renderer<TriangleRenderable> renderer;

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
        lenient().when(MOCK_TEXTURE_TILE_WIDTH_PROVIDER.provide(anyLong()))
                .thenReturn(randomFloatInRange(0f, 1f));
        lenient().when(MOCK_TEXTURE_TILE_HEIGHT_PROVIDER.provide(anyLong()))
                .thenReturn(randomFloatInRange(0f, 1f));

        mockMesh = mock(Mesh.class);
        mockShader = mock(Shader.class);

        renderer = new TriangleRenderer(mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderer(null));
    }

    @Test
    public void testRenderUpdatesTimestamp() {
        renderer.setMesh(mockMesh);
        renderer.setShader(mockShader);
        var timestamp = randomLong();

        renderer.render(TRIANGLE_RENDERABLE, timestamp);

        verify(mockTimestampValidator, once()).validateTimestamp(renderer.getClass().getCanonicalName(), timestamp);
    }

    @Test
    public void testRenderWithInvalidArgs() {
        renderer.setMesh(mockMesh);
        renderer.setShader(mockShader);

        assertThrows(IllegalArgumentException.class,
                () -> renderer.render(null, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeTriangleRenderable(null,
                                VERTEX_1_COLOR_PROVIDER, VERTEX_2_PROVIDER,
                                VERTEX_2_COLOR_PROVIDER, VERTEX_3_PROVIDER,
                                VERTEX_3_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                                MOCK_TEXTURE_TILE_WIDTH_PROVIDER,
                                MOCK_TEXTURE_TILE_HEIGHT_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeTriangleRenderable(new FakeStaticProvider<>(null),
                                VERTEX_1_COLOR_PROVIDER, VERTEX_2_PROVIDER,
                                VERTEX_2_COLOR_PROVIDER, VERTEX_3_PROVIDER,
                                VERTEX_3_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                                MOCK_TEXTURE_TILE_WIDTH_PROVIDER,
                                MOCK_TEXTURE_TILE_HEIGHT_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeTriangleRenderable(VERTEX_1_PROVIDER,
                                null, VERTEX_2_PROVIDER,
                                VERTEX_2_COLOR_PROVIDER, VERTEX_3_PROVIDER,
                                VERTEX_3_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                                MOCK_TEXTURE_TILE_WIDTH_PROVIDER,
                                MOCK_TEXTURE_TILE_HEIGHT_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeTriangleRenderable(VERTEX_1_PROVIDER,
                                VERTEX_1_COLOR_PROVIDER, null,
                                VERTEX_2_COLOR_PROVIDER, VERTEX_3_PROVIDER,
                                VERTEX_3_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                                MOCK_TEXTURE_TILE_WIDTH_PROVIDER,
                                MOCK_TEXTURE_TILE_HEIGHT_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeTriangleRenderable(VERTEX_1_PROVIDER,
                                VERTEX_1_COLOR_PROVIDER, new FakeStaticProvider<>(null),
                                VERTEX_2_COLOR_PROVIDER, VERTEX_3_PROVIDER,
                                VERTEX_3_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                                MOCK_TEXTURE_TILE_WIDTH_PROVIDER,
                                MOCK_TEXTURE_TILE_HEIGHT_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeTriangleRenderable(VERTEX_1_PROVIDER,
                                VERTEX_1_COLOR_PROVIDER, VERTEX_2_PROVIDER,
                                null, VERTEX_3_PROVIDER,
                                VERTEX_3_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                                MOCK_TEXTURE_TILE_WIDTH_PROVIDER,
                                MOCK_TEXTURE_TILE_HEIGHT_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeTriangleRenderable(VERTEX_1_PROVIDER,
                                VERTEX_1_COLOR_PROVIDER, VERTEX_2_PROVIDER,
                                VERTEX_2_COLOR_PROVIDER, null,
                                VERTEX_3_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                                MOCK_TEXTURE_TILE_WIDTH_PROVIDER,
                                MOCK_TEXTURE_TILE_HEIGHT_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeTriangleRenderable(VERTEX_1_PROVIDER,
                                VERTEX_1_COLOR_PROVIDER, VERTEX_2_PROVIDER,
                                VERTEX_2_COLOR_PROVIDER, new FakeStaticProvider<>(null),
                                VERTEX_3_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                                MOCK_TEXTURE_TILE_WIDTH_PROVIDER,
                                MOCK_TEXTURE_TILE_HEIGHT_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeTriangleRenderable(VERTEX_1_PROVIDER,
                                VERTEX_1_COLOR_PROVIDER, VERTEX_2_PROVIDER,
                                VERTEX_2_COLOR_PROVIDER, VERTEX_3_PROVIDER,
                                null, BACKGROUND_TEXTURE_ID_PROVIDER,
                                MOCK_TEXTURE_TILE_WIDTH_PROVIDER,
                                MOCK_TEXTURE_TILE_HEIGHT_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeTriangleRenderable(VERTEX_1_PROVIDER,
                                VERTEX_1_COLOR_PROVIDER, VERTEX_2_PROVIDER,
                                VERTEX_2_COLOR_PROVIDER, VERTEX_3_PROVIDER,
                                VERTEX_3_COLOR_PROVIDER, null,
                                MOCK_TEXTURE_TILE_WIDTH_PROVIDER,
                                MOCK_TEXTURE_TILE_HEIGHT_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeTriangleRenderable(VERTEX_1_PROVIDER,
                                VERTEX_1_COLOR_PROVIDER, VERTEX_2_PROVIDER,
                                VERTEX_2_COLOR_PROVIDER, VERTEX_3_PROVIDER,
                                VERTEX_3_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                                null,
                                MOCK_TEXTURE_TILE_HEIGHT_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeTriangleRenderable(VERTEX_1_PROVIDER,
                                VERTEX_1_COLOR_PROVIDER, VERTEX_2_PROVIDER,
                                VERTEX_2_COLOR_PROVIDER, VERTEX_3_PROVIDER,
                                VERTEX_3_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                                MOCK_TEXTURE_TILE_WIDTH_PROVIDER,
                                null),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeTriangleRenderable(VERTEX_1_PROVIDER,
                                VERTEX_1_COLOR_PROVIDER, VERTEX_2_PROVIDER,
                                VERTEX_2_COLOR_PROVIDER, VERTEX_3_PROVIDER,
                                VERTEX_3_COLOR_PROVIDER, new FakeStaticProvider<>(randomInt()),
                                null, MOCK_TEXTURE_TILE_HEIGHT_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeTriangleRenderable(VERTEX_1_PROVIDER,
                                VERTEX_1_COLOR_PROVIDER, VERTEX_2_PROVIDER,
                                VERTEX_2_COLOR_PROVIDER, VERTEX_3_PROVIDER,
                                VERTEX_3_COLOR_PROVIDER, new FakeStaticProvider<>(randomInt()),
                                MOCK_TEXTURE_TILE_WIDTH_PROVIDER, null),
                        MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testRenderWithMeshAndShaderUnset() {
        TriangleRenderer triangleRendererWithoutMesh = new TriangleRenderer(mockTimestampValidator);
        TriangleRenderer triangleRendererWithoutShader = new TriangleRenderer(mockTimestampValidator);

        triangleRendererWithoutMesh.setShader(mockShader);
        triangleRendererWithoutShader.setMesh(mockMesh);

        assertThrows(IllegalStateException.class, () -> triangleRendererWithoutMesh
                .render(TRIANGLE_RENDERABLE, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalStateException.class, () -> triangleRendererWithoutShader
                .render(TRIANGLE_RENDERABLE, MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testRenderUnbindsMeshAndShader() {
        renderer.setMesh(mockMesh);
        renderer.setShader(mockShader);

        renderer.render(TRIANGLE_RENDERABLE, MOST_RECENT_TIMESTAMP);

        verify(mockMesh, once()).unbind();
        verify(mockShader, once()).unbind();
    }
}
