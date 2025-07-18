package inaugural.soliloquy.io.test.unit.graphics.rendering.renderers;

import inaugural.soliloquy.io.graphics.rendering.renderers.RectangleRenderer;
import inaugural.soliloquy.io.test.testdoubles.fakes.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.RectangleRenderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.randomFloatInRange;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

@ExtendWith(MockitoExtension.class)
public class RectangleRendererTests {
    private final ProviderAtTime<Color> TOP_LEFT_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> TOP_RIGHT_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> BOTTOM_RIGHT_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> BOTTOM_LEFT_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Integer> BACKGROUND_TEXTURE_ID_PROVIDER =
            new FakeProviderAtTime<>();
    private final ProviderAtTime<FloatBox> RENDERING_AREA_PROVIDER =
            new FakeStaticProvider<>(floatBoxOf(0f, 0f, 1f, 1f));
    private final UUID UUID = java.util.UUID.randomUUID();
    private final long MOST_RECENT_TIMESTAMP = 123123L;
    private final FakeMesh MESH = new FakeMesh();
    private final FakeShader SHADER = new FakeShader();

    @Mock private ProviderAtTime<Float> mockTextureTileWidthProvider;
    @Mock private ProviderAtTime<Float> mockTextureTileHeightProvider;

    private Renderer<RectangleRenderable> rectangleRenderable;

    @BeforeAll
    public static void setUpFixture() {
        if (!glfwInit()) {
            throw new RuntimeException("GLFW failed to initialize");
        }

        var window = glfwCreateWindow(1, 1, "", 0, 0);
        glfwMakeContextCurrent(window);
        createCapabilities();
    }

    @AfterAll
    static void tearDownFixture() {
        glfwTerminate();
    }

    @BeforeEach
    public void setUp() {
        lenient().when(mockTextureTileWidthProvider.provide(anyLong())).thenReturn(randomFloatInRange(0f, 1f));
        lenient().when(mockTextureTileHeightProvider.provide(anyLong())).thenReturn(randomFloatInRange(0f, 1f));

        rectangleRenderable = new RectangleRenderer(MOST_RECENT_TIMESTAMP);
    }

    @Test
    public void testSetMeshAndShaderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> rectangleRenderable.setMesh(null));
        assertThrows(IllegalArgumentException.class, () -> rectangleRenderable.setShader(null));
    }

    @Test
    public void testRenderWithInvalidArgs() {
        rectangleRenderable.setMesh(MESH);
        rectangleRenderable.setShader(SHADER);

        assertThrows(IllegalArgumentException.class, () -> rectangleRenderable.render(
                new FakeRectangleRenderable(null, TOP_RIGHT_COLOR_PROVIDER,
                        BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, mockTextureTileWidthProvider,
                        mockTextureTileHeightProvider, RENDERING_AREA_PROVIDER, UUID),
                MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> rectangleRenderable.render(
                new FakeRectangleRenderable(TOP_LEFT_COLOR_PROVIDER, null,
                        BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, mockTextureTileWidthProvider,
                        mockTextureTileHeightProvider, RENDERING_AREA_PROVIDER, UUID),
                MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> rectangleRenderable.render(
                new FakeRectangleRenderable(TOP_LEFT_COLOR_PROVIDER, TOP_RIGHT_COLOR_PROVIDER,
                        null, BOTTOM_LEFT_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, mockTextureTileWidthProvider,
                        mockTextureTileHeightProvider, RENDERING_AREA_PROVIDER, UUID),
                MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> rectangleRenderable.render(
                new FakeRectangleRenderable(TOP_LEFT_COLOR_PROVIDER, TOP_RIGHT_COLOR_PROVIDER,
                        BOTTOM_RIGHT_COLOR_PROVIDER, null,
                        BACKGROUND_TEXTURE_ID_PROVIDER, mockTextureTileWidthProvider,
                        mockTextureTileHeightProvider, RENDERING_AREA_PROVIDER, UUID),
                MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> rectangleRenderable.render(
                new FakeRectangleRenderable(TOP_LEFT_COLOR_PROVIDER, TOP_RIGHT_COLOR_PROVIDER,
                        BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                        null, mockTextureTileWidthProvider,
                        mockTextureTileHeightProvider, RENDERING_AREA_PROVIDER, UUID),
                MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> rectangleRenderable.render(
                new FakeRectangleRenderable(TOP_LEFT_COLOR_PROVIDER, TOP_RIGHT_COLOR_PROVIDER,
                        BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, null,
                        mockTextureTileHeightProvider, RENDERING_AREA_PROVIDER, UUID),
                MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> rectangleRenderable.render(
                new FakeRectangleRenderable(TOP_LEFT_COLOR_PROVIDER, TOP_RIGHT_COLOR_PROVIDER,
                        BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, mockTextureTileWidthProvider,
                        null, RENDERING_AREA_PROVIDER, UUID),
                MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> rectangleRenderable.render(
                new FakeRectangleRenderable(TOP_LEFT_COLOR_PROVIDER, TOP_RIGHT_COLOR_PROVIDER,
                        BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, mockTextureTileWidthProvider,
                        mockTextureTileHeightProvider, null, UUID),
                MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> rectangleRenderable.render(
                new FakeRectangleRenderable(TOP_LEFT_COLOR_PROVIDER, TOP_RIGHT_COLOR_PROVIDER,
                        BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, mockTextureTileWidthProvider,
                        mockTextureTileHeightProvider, new FakeStaticProvider<>(null), UUID),
                MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> rectangleRenderable.render(
                new FakeRectangleRenderable(TOP_LEFT_COLOR_PROVIDER, TOP_RIGHT_COLOR_PROVIDER,
                        BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, mockTextureTileWidthProvider,
                        mockTextureTileHeightProvider, RENDERING_AREA_PROVIDER, null),
                MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testRenderWithInvalidTimestamp() {
        rectangleRenderable.setMesh(MESH);
        rectangleRenderable.setShader(SHADER);

        assertThrows(IllegalArgumentException.class, () -> rectangleRenderable.render(
                new FakeRectangleRenderable(TOP_LEFT_COLOR_PROVIDER, TOP_RIGHT_COLOR_PROVIDER,
                        BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, mockTextureTileWidthProvider,
                        mockTextureTileHeightProvider, RENDERING_AREA_PROVIDER, UUID),
                MOST_RECENT_TIMESTAMP - 1L));
    }

    @Test
    public void testRenderWithoutMeshOrShader() {
        RectangleRenderable rectangleRenderable = new FakeRectangleRenderable(
                TOP_LEFT_COLOR_PROVIDER, TOP_RIGHT_COLOR_PROVIDER,
                BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER, mockTextureTileWidthProvider,
                mockTextureTileHeightProvider, RENDERING_AREA_PROVIDER, UUID);

        Renderer<RectangleRenderable> rectangleRendererWithoutMesh =
                new RectangleRenderer(MOST_RECENT_TIMESTAMP);
        rectangleRendererWithoutMesh.setShader(SHADER);

        assertThrows(IllegalStateException.class, () ->
                rectangleRendererWithoutMesh.render(rectangleRenderable, MOST_RECENT_TIMESTAMP));

        Renderer<RectangleRenderable> rectangleRendererWithoutShader =
                new RectangleRenderer(MOST_RECENT_TIMESTAMP);
        rectangleRendererWithoutShader.setMesh(MESH);

        assertThrows(IllegalStateException.class, () ->
                rectangleRendererWithoutShader.render(rectangleRenderable, MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testMostRecentTimestamp() {
        assertEquals(MOST_RECENT_TIMESTAMP, (long) rectangleRenderable.mostRecentTimestamp());
    }
}
