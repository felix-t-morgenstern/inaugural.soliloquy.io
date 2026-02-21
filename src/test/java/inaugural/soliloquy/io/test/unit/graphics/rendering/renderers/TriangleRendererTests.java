package inaugural.soliloquy.io.test.unit.graphics.rendering.renderers;

import inaugural.soliloquy.io.graphics.rendering.renderers.TriangleRenderer;
import inaugural.soliloquy.io.graphics.rendering.renderers.TriangleSegmentRenderer;
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
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.io.graphics.rendering.Shader;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.awt.*;

import static inaugural.soliloquy.io.api.Constants.WHOLE_SCREEN;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static inaugural.soliloquy.tools.testing.Mock.generateMockStaticProvider;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

@ExtendWith(MockitoExtension.class)
public class TriangleRendererTests {
    private final ProviderAtTime<Vertex> VERTEX_1_PROVIDER =
            generateMockStaticProvider(vertexOf(randomFloat(), randomFloat()));
    private final ProviderAtTime<Color> VERTEX_1_COLOR_PROVIDER =
            generateMockStaticProvider(randomColor());
    private final ProviderAtTime<Vertex> VERTEX_2_PROVIDER =
            generateMockStaticProvider(vertexOf(randomFloat(), randomFloat()));
    private final ProviderAtTime<Color> VERTEX_2_COLOR_PROVIDER =
            generateMockStaticProvider(randomColor());
    private final ProviderAtTime<Vertex> VERTEX_3_PROVIDER =
            generateMockStaticProvider(vertexOf(randomFloat(), randomFloat()));
    private final ProviderAtTime<Color> VERTEX_3_COLOR_PROVIDER =
            generateMockStaticProvider(randomColor());
    private final ProviderAtTime<Integer> BACKGROUND_TEXTURE_ID_PROVIDER =
            generateMockStaticProvider(randomInt());
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
    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private TriangleSegmentRenderer mockTriangleSegmentRenderer;
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

        lenient().when(mockRenderingBoundaries.currentBoundaries()).thenReturn(WHOLE_SCREEN);

        mockMesh = mock(Mesh.class);
        mockShader = mock(Shader.class);

        renderer = new TriangleRenderer(mockTimestampValidator, mockRenderingBoundaries,
                mockTriangleSegmentRenderer);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new TriangleRenderer(null, mockRenderingBoundaries,
                        mockTriangleSegmentRenderer));
        assertThrows(IllegalArgumentException.class,
                () -> new TriangleRenderer(mockTimestampValidator, null,
                        mockTriangleSegmentRenderer));
        assertThrows(IllegalArgumentException.class,
                () -> new TriangleRenderer(mockTimestampValidator, mockRenderingBoundaries, null));
    }

    @Test
    public void testRenderUpdatesTimestamp() {
        var timestamp = randomLong();

        renderer.render(TRIANGLE_RENDERABLE, timestamp);

        verify(mockTimestampValidator, once()).validateTimestamp(
                renderer.getClass().getCanonicalName(), timestamp);
    }

    @Test
    public void testRenderWithInvalidArgs() {
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
                .render(new FakeTriangleRenderable(generateMockStaticProvider(null),
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
                                VERTEX_1_COLOR_PROVIDER, generateMockStaticProvider(null),
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
                                VERTEX_2_COLOR_PROVIDER, generateMockStaticProvider(null),
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
                                VERTEX_3_COLOR_PROVIDER, generateMockStaticProvider(randomInt()),
                                null, MOCK_TEXTURE_TILE_HEIGHT_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeTriangleRenderable(VERTEX_1_PROVIDER,
                                VERTEX_1_COLOR_PROVIDER, VERTEX_2_PROVIDER,
                                VERTEX_2_COLOR_PROVIDER, VERTEX_3_PROVIDER,
                                VERTEX_3_COLOR_PROVIDER, generateMockStaticProvider(randomInt()),
                                MOCK_TEXTURE_TILE_WIDTH_PROVIDER, null),
                        MOST_RECENT_TIMESTAMP));
    }
}
