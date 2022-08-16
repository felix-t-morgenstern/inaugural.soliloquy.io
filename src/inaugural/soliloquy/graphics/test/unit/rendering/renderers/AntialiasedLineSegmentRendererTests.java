package inaugural.soliloquy.graphics.test.unit.rendering.renderers;

import inaugural.soliloquy.graphics.rendering.renderers.AntialiasedLineSegmentRenderer;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.infrastructure.Pair;
import soliloquy.specs.graphics.renderables.AntialiasedLineSegmentRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.Mesh;
import soliloquy.specs.graphics.rendering.Shader;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;

import static inaugural.soliloquy.tools.random.Random.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;

class AntialiasedLineSegmentRendererTests {
    private final FakeWindowResolutionManager WINDOW_RESOLUTION_MANAGER =
            new FakeWindowResolutionManager();

    private final ProviderAtTime<Float> THICKNESS_GRADIENT_PERCENT_PROVIDER =
            new FakeStaticProvider<>(randomFloatInRange(0f, 1f));
    private final ProviderAtTime<Float> LENGTH_GRADIENT_PERCENT_PROVIDER =
            new FakeStaticProvider<>(randomFloatInRange(0f, 1f));
    private final ProviderAtTime<Float> THICKNESS_PROVIDER =
            new FakeStaticProvider<>(randomFloatWithInclusiveFloor(0.001f));
    private final ProviderAtTime<Color> COLOR_PROVIDER = new FakeStaticProvider<>(randomColor());
    private final float X1 = randomFloat();
    private final float Y1 = randomFloat();
    private final float X2 = randomFloat();
    private final float Y2 = randomFloat();
    private final ProviderAtTime<Pair<Float, Float>> VERTEX_1_LOCATION_PROVIDER =
            new FakeStaticProvider<>(new Pair<>(X1, Y1));
    private final ProviderAtTime<Pair<Float, Float>> VERTEX_2_LOCATION_PROVIDER =
            new FakeStaticProvider<>(new Pair<>(X2, Y2));

    private final Mesh MESH = new FakeMesh();
    private final Shader SHADER = new FakeShader();

    private final Long MOST_RECENT_TIMESTAMP = randomLong();

    private FakeAntialiasedLineSegmentRenderable _antialiasedLineSegmentRenderable =
            new FakeAntialiasedLineSegmentRenderable(
                    THICKNESS_GRADIENT_PERCENT_PROVIDER,
                    LENGTH_GRADIENT_PERCENT_PROVIDER,
                    THICKNESS_PROVIDER,
                    COLOR_PROVIDER,
                    VERTEX_1_LOCATION_PROVIDER,
                    VERTEX_2_LOCATION_PROVIDER);

    private Renderer<AntialiasedLineSegmentRenderable> _antialiasedLineSegmentRenderer;

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
        _antialiasedLineSegmentRenderer =
                new AntialiasedLineSegmentRenderer(WINDOW_RESOLUTION_MANAGER,
                        MOST_RECENT_TIMESTAMP);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new AntialiasedLineSegmentRenderer(null, randomLong()));
    }

    @Test
    void testSetAndGetMeshAndShader() {
        assertThrows(IllegalStateException.class, () -> _antialiasedLineSegmentRenderer
                .render(_antialiasedLineSegmentRenderable, MOST_RECENT_TIMESTAMP));

        _antialiasedLineSegmentRenderer.setMesh(MESH);
        _antialiasedLineSegmentRenderer.setShader(SHADER);

        _antialiasedLineSegmentRenderer
                .render(_antialiasedLineSegmentRenderable, MOST_RECENT_TIMESTAMP);
    }

    @Test
    void testGetMostRecentTimestamp() {
        _antialiasedLineSegmentRenderer.setMesh(MESH);
        _antialiasedLineSegmentRenderer.setShader(SHADER);

        assertEquals(MOST_RECENT_TIMESTAMP, _antialiasedLineSegmentRenderer.mostRecentTimestamp());

        _antialiasedLineSegmentRenderer
                .render(_antialiasedLineSegmentRenderable, MOST_RECENT_TIMESTAMP + 1);

        assertEquals(MOST_RECENT_TIMESTAMP + 1,
                (long) _antialiasedLineSegmentRenderer.mostRecentTimestamp());
    }

    @Test
    void testRenderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                null,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_LOCATION_PROVIDER,
                                VERTEX_2_LOCATION_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                new FakeStaticProvider<>(null),
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_LOCATION_PROVIDER,
                                VERTEX_2_LOCATION_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                new FakeStaticProvider<>(-0.001f),
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_LOCATION_PROVIDER,
                                VERTEX_2_LOCATION_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                new FakeStaticProvider<>(1.001f),
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_LOCATION_PROVIDER,
                                VERTEX_2_LOCATION_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                null,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_LOCATION_PROVIDER,
                                VERTEX_2_LOCATION_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                new FakeStaticProvider<>(null),
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_LOCATION_PROVIDER,
                                VERTEX_2_LOCATION_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                new FakeStaticProvider<>(-0.001f),
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_LOCATION_PROVIDER,
                                VERTEX_2_LOCATION_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                new FakeStaticProvider<>(1.001f),
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_LOCATION_PROVIDER,
                                VERTEX_2_LOCATION_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                null,
                                COLOR_PROVIDER,
                                VERTEX_1_LOCATION_PROVIDER,
                                VERTEX_2_LOCATION_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                new FakeStaticProvider<>(null),
                                COLOR_PROVIDER,
                                VERTEX_1_LOCATION_PROVIDER,
                                VERTEX_2_LOCATION_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                new FakeStaticProvider<>(0f),
                                COLOR_PROVIDER,
                                VERTEX_1_LOCATION_PROVIDER,
                                VERTEX_2_LOCATION_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                null,
                                VERTEX_1_LOCATION_PROVIDER,
                                VERTEX_2_LOCATION_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                new FakeStaticProvider<>(null),
                                VERTEX_1_LOCATION_PROVIDER,
                                VERTEX_2_LOCATION_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                null,
                                VERTEX_2_LOCATION_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                new FakeStaticProvider<>(null),
                                VERTEX_2_LOCATION_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                new FakeStaticProvider<>(new Pair<>(null, Y1, 0f, 0f)),
                                VERTEX_2_LOCATION_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                new FakeStaticProvider<>(new Pair<>(X1, null, 0f, 0f)),
                                VERTEX_2_LOCATION_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_LOCATION_PROVIDER,
                                null),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_LOCATION_PROVIDER,
                                new FakeStaticProvider<>(null)),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_LOCATION_PROVIDER,
                                new FakeStaticProvider<>(new Pair<>(null, Y2, 0f, 0f))),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_LOCATION_PROVIDER,
                                new FakeStaticProvider<>(new Pair<>(X2, null, 0f, 0f))),
                        MOST_RECENT_TIMESTAMP));
    }

    @Test
    void testRenderWithOutOfDateTimestamp() {
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderer
                .render(_antialiasedLineSegmentRenderable, MOST_RECENT_TIMESTAMP - 1));
    }

    @Test
    void testSetMeshAndShaderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> _antialiasedLineSegmentRenderer.setMesh(null));
        assertThrows(IllegalArgumentException.class,
                () -> _antialiasedLineSegmentRenderer.setShader(null));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(Renderer.class.getCanonicalName() + "<" +
                        AntialiasedLineSegmentRenderable.class.getCanonicalName() + ">",
                _antialiasedLineSegmentRenderer.getInterfaceName());
    }
}
