package inaugural.soliloquy.io.test.unit.graphics.rendering.renderers;

import inaugural.soliloquy.io.graphics.rendering.renderers.AntialiasedLineSegmentRenderer;
import inaugural.soliloquy.io.test.testdoubles.fakes.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.AntialiasedLineSegmentRenderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.Mesh;
import soliloquy.specs.io.graphics.rendering.Shader;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.awt.*;

import static inaugural.soliloquy.tools.random.Random.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

public class AntialiasedLineSegmentRendererTests {
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
    private final ProviderAtTime<Vertex> VERTEX_1_PROVIDER =
            new FakeStaticProvider<>(vertexOf(X1, Y1));
    private final ProviderAtTime<Vertex> VERTEX_2_PROVIDER =
            new FakeStaticProvider<>(vertexOf(X2, Y2));

    private final Mesh MESH = new FakeMesh();
    private final Shader SHADER = new FakeShader();

    private final Long MOST_RECENT_TIMESTAMP = randomLong();

    private final FakeAntialiasedLineSegmentRenderable ANTIALIASED_LINE_SEGMENT_RENDERABLE =
            new FakeAntialiasedLineSegmentRenderable(
                    THICKNESS_GRADIENT_PERCENT_PROVIDER,
                    LENGTH_GRADIENT_PERCENT_PROVIDER,
                    THICKNESS_PROVIDER,
                    COLOR_PROVIDER,
                    VERTEX_1_PROVIDER,
                    VERTEX_2_PROVIDER);

    private Renderer<AntialiasedLineSegmentRenderable> antialiasedLineSegmentRenderer;

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
        antialiasedLineSegmentRenderer =
                new AntialiasedLineSegmentRenderer(WINDOW_RESOLUTION_MANAGER,
                        MOST_RECENT_TIMESTAMP);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new AntialiasedLineSegmentRenderer(null, randomLong()));
    }

    @Test
    public void testSetAndGetMeshAndShader() {
        assertThrows(IllegalStateException.class, () -> antialiasedLineSegmentRenderer
                .render(ANTIALIASED_LINE_SEGMENT_RENDERABLE, MOST_RECENT_TIMESTAMP));

        antialiasedLineSegmentRenderer.setMesh(MESH);
        antialiasedLineSegmentRenderer.setShader(SHADER);

        antialiasedLineSegmentRenderer
                .render(ANTIALIASED_LINE_SEGMENT_RENDERABLE, MOST_RECENT_TIMESTAMP);
    }

    @Test
    public void testGetMostRecentTimestamp() {
        antialiasedLineSegmentRenderer.setMesh(MESH);
        antialiasedLineSegmentRenderer.setShader(SHADER);

        assertEquals(MOST_RECENT_TIMESTAMP, antialiasedLineSegmentRenderer.mostRecentTimestamp());

        antialiasedLineSegmentRenderer
                .render(ANTIALIASED_LINE_SEGMENT_RENDERABLE, MOST_RECENT_TIMESTAMP + 1);

        assertEquals(MOST_RECENT_TIMESTAMP + 1,
                (long) antialiasedLineSegmentRenderer.mostRecentTimestamp());
    }

    @Test
    public void testRenderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                null,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_PROVIDER,
                                VERTEX_2_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                new FakeStaticProvider<>(null),
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_PROVIDER,
                                VERTEX_2_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                new FakeStaticProvider<>(-0.001f),
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_PROVIDER,
                                VERTEX_2_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                new FakeStaticProvider<>(1.001f),
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_PROVIDER,
                                VERTEX_2_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                null,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_PROVIDER,
                                VERTEX_2_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                new FakeStaticProvider<>(null),
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_PROVIDER,
                                VERTEX_2_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                new FakeStaticProvider<>(-0.001f),
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_PROVIDER,
                                VERTEX_2_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                new FakeStaticProvider<>(1.001f),
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_PROVIDER,
                                VERTEX_2_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                null,
                                COLOR_PROVIDER,
                                VERTEX_1_PROVIDER,
                                VERTEX_2_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                new FakeStaticProvider<>(null),
                                COLOR_PROVIDER,
                                VERTEX_1_PROVIDER,
                                VERTEX_2_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                new FakeStaticProvider<>(0f),
                                COLOR_PROVIDER,
                                VERTEX_1_PROVIDER,
                                VERTEX_2_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                null,
                                VERTEX_1_PROVIDER,
                                VERTEX_2_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                new FakeStaticProvider<>(null),
                                VERTEX_1_PROVIDER,
                                VERTEX_2_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                null,
                                VERTEX_2_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                new FakeStaticProvider<>(null),
                                VERTEX_2_PROVIDER),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_PROVIDER,
                                null),
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_PROVIDER,
                                new FakeStaticProvider<>(null)),
                        MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testRenderWithOutOfDateTimestamp() {
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderer
                .render(ANTIALIASED_LINE_SEGMENT_RENDERABLE, MOST_RECENT_TIMESTAMP - 1));
    }

    @Test
    public void testSetMeshAndShaderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> antialiasedLineSegmentRenderer.setMesh(null));
        assertThrows(IllegalArgumentException.class,
                () -> antialiasedLineSegmentRenderer.setShader(null));
    }
}
