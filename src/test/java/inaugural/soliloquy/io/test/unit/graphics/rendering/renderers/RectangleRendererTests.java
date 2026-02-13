package inaugural.soliloquy.io.test.unit.graphics.rendering.renderers;

import inaugural.soliloquy.io.graphics.rendering.renderers.RectangleRenderer;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeRectangleRenderable;
import inaugural.soliloquy.tools.timing.TimestampValidator;
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
import soliloquy.specs.io.graphics.rendering.Mesh;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.io.graphics.rendering.Shader;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.UUID;

import static inaugural.soliloquy.io.api.Constants.WHOLE_SCREEN;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static inaugural.soliloquy.tools.testing.Mock.generateMockStaticProvider;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

@ExtendWith(MockitoExtension.class)
public class RectangleRendererTests {
    private final ProviderAtTime<FloatBox> RENDERING_AREA_PROVIDER =
            generateMockStaticProvider(floatBoxOf(0f, 0f, 1f, 1f));
    private final UUID UUID = java.util.UUID.randomUUID();
    private final long MOST_RECENT_TIMESTAMP = randomLong();

    @Mock private ProviderAtTime<Color> mockTopLeftColorProvider;
    @Mock private ProviderAtTime<Color> mockTopRightColorProvider;
    @Mock private ProviderAtTime<Color> mockBottomRightColorProvider;
    @Mock private ProviderAtTime<Color> mockBottomLeftColorProvider;
    @Mock private ProviderAtTime<Integer> mockBackgroundTextureIdProvider;
    @Mock private TimestampValidator mockTimestampValidator;
    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private Mesh mockMesh;
    @Mock private Shader mockShader;
    @Mock private ProviderAtTime<Float> mockTextureTileWidthProvider;
    @Mock private ProviderAtTime<Float> mockTextureTileHeightProvider;

    private Renderer<RectangleRenderable> renderer;

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
        lenient().when(mockTextureTileWidthProvider.provide(anyLong()))
                .thenReturn(randomFloatInRange(0f, 1f));
        lenient().when(mockTextureTileHeightProvider.provide(anyLong()))
                .thenReturn(randomFloatInRange(0f, 1f));

        lenient().when(mockRenderingBoundaries.currentBoundaries()).thenReturn(WHOLE_SCREEN);

        renderer = new RectangleRenderer(mockTimestampValidator, mockRenderingBoundaries);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new RectangleRenderer(null, mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () -> new RectangleRenderer(mockTimestampValidator, null));
    }

    @Test
    public void testSetMeshAndShaderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> renderer.setMesh(null));
        assertThrows(IllegalArgumentException.class, () -> renderer.setShader(null));
    }

    @Test
    public void testRenderWithInvalidArgs() {
        when(mockBackgroundTextureIdProvider.provide(anyLong())).thenReturn(randomInt());
        renderer.setMesh(mockMesh);
        renderer.setShader(mockShader);

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeRectangleRenderable(null, mockTopRightColorProvider,
                        mockBottomRightColorProvider, mockBottomLeftColorProvider,
                        mockBackgroundTextureIdProvider, mockTextureTileWidthProvider,
                        mockTextureTileHeightProvider, RENDERING_AREA_PROVIDER, UUID),
                MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeRectangleRenderable(mockTopLeftColorProvider, null,
                        mockBottomRightColorProvider, mockBottomLeftColorProvider,
                        mockBackgroundTextureIdProvider, mockTextureTileWidthProvider,
                        mockTextureTileHeightProvider, RENDERING_AREA_PROVIDER, UUID),
                MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeRectangleRenderable(mockTopLeftColorProvider, mockTopRightColorProvider,
                        null, mockBottomLeftColorProvider,
                        mockBackgroundTextureIdProvider, mockTextureTileWidthProvider,
                        mockTextureTileHeightProvider, RENDERING_AREA_PROVIDER, UUID),
                MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeRectangleRenderable(mockTopLeftColorProvider, mockTopRightColorProvider,
                        mockBottomRightColorProvider, null,
                        mockBackgroundTextureIdProvider, mockTextureTileWidthProvider,
                        mockTextureTileHeightProvider, RENDERING_AREA_PROVIDER, UUID),
                MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeRectangleRenderable(mockTopLeftColorProvider, mockTopRightColorProvider,
                        mockBottomRightColorProvider, mockBottomLeftColorProvider,
                        null, mockTextureTileWidthProvider,
                        mockTextureTileHeightProvider, RENDERING_AREA_PROVIDER, UUID),
                MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeRectangleRenderable(mockTopLeftColorProvider, mockTopRightColorProvider,
                        mockBottomRightColorProvider, mockBottomLeftColorProvider,
                        mockBackgroundTextureIdProvider, mockTextureTileWidthProvider,
                        null, RENDERING_AREA_PROVIDER, UUID),
                MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeRectangleRenderable(mockTopLeftColorProvider, mockTopRightColorProvider,
                        mockBottomRightColorProvider, mockBottomLeftColorProvider,
                        mockBackgroundTextureIdProvider, null,
                        mockTextureTileHeightProvider, RENDERING_AREA_PROVIDER, UUID),
                MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeRectangleRenderable(mockTopLeftColorProvider, mockTopRightColorProvider,
                        mockBottomRightColorProvider, mockBottomLeftColorProvider,
                        mockBackgroundTextureIdProvider, mockTextureTileWidthProvider,
                        mockTextureTileHeightProvider, null, UUID),
                MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeRectangleRenderable(mockTopLeftColorProvider, mockTopRightColorProvider,
                        mockBottomRightColorProvider, mockBottomLeftColorProvider,
                        mockBackgroundTextureIdProvider, mockTextureTileWidthProvider,
                        mockTextureTileHeightProvider, generateMockStaticProvider(null), UUID),
                MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeRectangleRenderable(mockTopLeftColorProvider, mockTopRightColorProvider,
                        mockBottomRightColorProvider, mockBottomLeftColorProvider,
                        mockBackgroundTextureIdProvider, mockTextureTileWidthProvider,
                        mockTextureTileHeightProvider, RENDERING_AREA_PROVIDER, null),
                MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testRenderWithoutMeshOrShader() {
        var rectangleRenderable = new FakeRectangleRenderable(
                mockTopLeftColorProvider, mockTopRightColorProvider,
                mockBottomRightColorProvider, mockBottomLeftColorProvider,
                mockBackgroundTextureIdProvider, mockTextureTileWidthProvider,
                mockTextureTileHeightProvider, RENDERING_AREA_PROVIDER, UUID);

        var rendererWithoutMesh = new RectangleRenderer(mockTimestampValidator, mockRenderingBoundaries);
        rendererWithoutMesh.setShader(mockShader);

        assertThrows(IllegalStateException.class, () ->
                rendererWithoutMesh.render(rectangleRenderable, MOST_RECENT_TIMESTAMP));

        var rendererWithoutShader = new RectangleRenderer(mockTimestampValidator, mockRenderingBoundaries);
        rendererWithoutShader.setMesh(mockMesh);

        assertThrows(IllegalStateException.class, () ->
                rendererWithoutShader.render(rectangleRenderable, MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testRenderUpdatesTimestamp() {
        RectangleRenderable rectangleRenderable = new FakeRectangleRenderable(
                mockTopLeftColorProvider, mockTopRightColorProvider,
                mockBottomRightColorProvider, mockBottomLeftColorProvider,
                mockBackgroundTextureIdProvider, mockTextureTileWidthProvider,
                mockTextureTileHeightProvider, RENDERING_AREA_PROVIDER, UUID);
        renderer.setMesh(mockMesh);
        renderer.setShader(mockShader);
        var timestamp = randomLong();

        renderer.render(rectangleRenderable, timestamp);

        verify(mockTimestampValidator, once()).validateTimestamp(
                renderer.getClass().getCanonicalName(), timestamp);
    }
}
