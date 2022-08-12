package inaugural.soliloquy.graphics.test.unit.rendering.renderers;

import inaugural.soliloquy.graphics.rendering.renderers.RectangleRenderer;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.RectangleRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL.createCapabilities;

class RectangleRendererTests {
    private final ProviderAtTime<Color> TOP_LEFT_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> TOP_RIGHT_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> BOTTOM_RIGHT_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> BOTTOM_LEFT_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Integer> BACKGROUND_TEXTURE_ID_PROVIDER =
            new FakeProviderAtTime<>();
    private final float BACKGROUND_TEXTURE_TILE_WIDTH = 0.123f;
    private final float BACKGROUND_TEXTURE_TILE_HEIGHT = 0.456f;
    private final ProviderAtTime<FloatBox> RENDERING_AREA_PROVIDER =
            new FakeStaticProvider<>(new FakeFloatBox(0f, 0f, 1f, 1f));
    private final UUID UUID = java.util.UUID.randomUUID();
    private final long MOST_RECENT_TIMESTAMP = 123123L;
    private final FakeMesh MESH = new FakeMesh();
    private final FakeShader SHADER = new FakeShader();

    private Renderer<RectangleRenderable> _rectangleRenderable;

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
        _rectangleRenderable = new RectangleRenderer(MOST_RECENT_TIMESTAMP);
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(Renderer.class.getCanonicalName() + "<" +
                RectangleRenderable.class.getCanonicalName() + ">",
                _rectangleRenderable.getInterfaceName());
    }

    @Test
    void testSetMeshAndShaderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _rectangleRenderable.setMesh(null));
        assertThrows(IllegalArgumentException.class, () -> _rectangleRenderable.setShader(null));
    }

    @Test
    void testRenderWithInvalidParams() {
        _rectangleRenderable.setMesh(MESH);
        _rectangleRenderable.setShader(SHADER);

        assertThrows(IllegalArgumentException.class, () -> _rectangleRenderable.render(
                new FakeRectangleRenderable(null, TOP_RIGHT_COLOR_PROVIDER,
                        BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER,  BACKGROUND_TEXTURE_TILE_WIDTH,
                        BACKGROUND_TEXTURE_TILE_HEIGHT, RENDERING_AREA_PROVIDER, UUID),
                MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _rectangleRenderable.render(
                new FakeRectangleRenderable(TOP_LEFT_COLOR_PROVIDER, null,
                        BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER,  BACKGROUND_TEXTURE_TILE_WIDTH,
                        BACKGROUND_TEXTURE_TILE_HEIGHT, RENDERING_AREA_PROVIDER, UUID),
                MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _rectangleRenderable.render(
                new FakeRectangleRenderable(TOP_LEFT_COLOR_PROVIDER, TOP_RIGHT_COLOR_PROVIDER,
                        null, BOTTOM_LEFT_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER,  BACKGROUND_TEXTURE_TILE_WIDTH,
                        BACKGROUND_TEXTURE_TILE_HEIGHT, RENDERING_AREA_PROVIDER, UUID),
                MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _rectangleRenderable.render(
                new FakeRectangleRenderable(TOP_LEFT_COLOR_PROVIDER, TOP_RIGHT_COLOR_PROVIDER,
                        BOTTOM_RIGHT_COLOR_PROVIDER, null,
                        BACKGROUND_TEXTURE_ID_PROVIDER,  BACKGROUND_TEXTURE_TILE_WIDTH,
                        BACKGROUND_TEXTURE_TILE_HEIGHT, RENDERING_AREA_PROVIDER, UUID),
                MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _rectangleRenderable.render(
                new FakeRectangleRenderable(TOP_LEFT_COLOR_PROVIDER, TOP_RIGHT_COLOR_PROVIDER,
                        BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                        null,  BACKGROUND_TEXTURE_TILE_WIDTH,
                        BACKGROUND_TEXTURE_TILE_HEIGHT, RENDERING_AREA_PROVIDER, UUID),
                MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _rectangleRenderable.render(
                new FakeRectangleRenderable(TOP_LEFT_COLOR_PROVIDER, TOP_RIGHT_COLOR_PROVIDER,
                        BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER,  -0.0001f,
                        BACKGROUND_TEXTURE_TILE_HEIGHT, RENDERING_AREA_PROVIDER, UUID),
                MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _rectangleRenderable.render(
                new FakeRectangleRenderable(TOP_LEFT_COLOR_PROVIDER, TOP_RIGHT_COLOR_PROVIDER,
                        BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER,  BACKGROUND_TEXTURE_TILE_WIDTH,
                        -0.0001f, RENDERING_AREA_PROVIDER, UUID),
                MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _rectangleRenderable.render(
                new FakeRectangleRenderable(TOP_LEFT_COLOR_PROVIDER, TOP_RIGHT_COLOR_PROVIDER,
                        BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER,  BACKGROUND_TEXTURE_TILE_WIDTH,
                        BACKGROUND_TEXTURE_TILE_HEIGHT, null, UUID),
                MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _rectangleRenderable.render(
                new FakeRectangleRenderable(TOP_LEFT_COLOR_PROVIDER, TOP_RIGHT_COLOR_PROVIDER,
                        BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER,  BACKGROUND_TEXTURE_TILE_WIDTH,
                        BACKGROUND_TEXTURE_TILE_HEIGHT, new FakeStaticProvider<>(null), UUID),
                MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _rectangleRenderable.render(
                new FakeRectangleRenderable(TOP_LEFT_COLOR_PROVIDER, TOP_RIGHT_COLOR_PROVIDER,
                        BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER,  BACKGROUND_TEXTURE_TILE_WIDTH,
                        BACKGROUND_TEXTURE_TILE_HEIGHT, RENDERING_AREA_PROVIDER, null),
                MOST_RECENT_TIMESTAMP));
    }

    @Test
    void testRenderWithInvalidTimestamp() {
        _rectangleRenderable.setMesh(MESH);
        _rectangleRenderable.setShader(SHADER);

        assertThrows(IllegalArgumentException.class, () -> _rectangleRenderable.render(
                new FakeRectangleRenderable(TOP_LEFT_COLOR_PROVIDER, TOP_RIGHT_COLOR_PROVIDER,
                        BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER,  BACKGROUND_TEXTURE_TILE_WIDTH,
                        BACKGROUND_TEXTURE_TILE_HEIGHT, RENDERING_AREA_PROVIDER, UUID),
                MOST_RECENT_TIMESTAMP - 1L));
    }

    @Test
    void testRenderWithoutMeshOrShader() {
        RectangleRenderable rectangleRenderable = new FakeRectangleRenderable(
                TOP_LEFT_COLOR_PROVIDER, TOP_RIGHT_COLOR_PROVIDER,
                BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,  BACKGROUND_TEXTURE_TILE_WIDTH,
                BACKGROUND_TEXTURE_TILE_HEIGHT, RENDERING_AREA_PROVIDER, UUID);

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
    void testMostRecentTimestamp() {
        assertEquals(MOST_RECENT_TIMESTAMP, (long)_rectangleRenderable.mostRecentTimestamp());
    }
}
