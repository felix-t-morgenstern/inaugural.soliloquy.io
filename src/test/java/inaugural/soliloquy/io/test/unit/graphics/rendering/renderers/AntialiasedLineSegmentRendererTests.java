package inaugural.soliloquy.io.test.unit.graphics.rendering.renderers;

import inaugural.soliloquy.io.graphics.rendering.renderers.AntialiasedLineSegmentRenderer;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeAntialiasedLineSegmentRenderable;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeStaticProvider;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeWindowResolutionManager;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.AntialiasedLineSegmentRenderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.Mesh;
import soliloquy.specs.io.graphics.rendering.Shader;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.awt.*;

import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.mockito.Mockito.verify;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

@ExtendWith(MockitoExtension.class)
public class AntialiasedLineSegmentRendererTests {
    private final long TIMESTAMP = randomLong();
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

    private final FakeAntialiasedLineSegmentRenderable ANTIALIASED_LINE_SEGMENT_RENDERABLE =
            new FakeAntialiasedLineSegmentRenderable(
                    THICKNESS_GRADIENT_PERCENT_PROVIDER,
                    LENGTH_GRADIENT_PERCENT_PROVIDER,
                    THICKNESS_PROVIDER,
                    COLOR_PROVIDER,
                    VERTEX_1_PROVIDER,
                    VERTEX_2_PROVIDER);

    @Mock private Mesh mockMesh;
    @Mock private Shader mockShader;
    @Mock private TimestampValidator mockTimestampValidator;

    private Renderer<AntialiasedLineSegmentRenderable> renderer;

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
        renderer = new AntialiasedLineSegmentRenderer(WINDOW_RESOLUTION_MANAGER,
                mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new AntialiasedLineSegmentRenderer(null, mockTimestampValidator));
    }

    @Test
    public void testSetAndGetMeshAndShader() {
        assertThrows(IllegalStateException.class, () -> renderer
                .render(ANTIALIASED_LINE_SEGMENT_RENDERABLE, TIMESTAMP));

        renderer.setMesh(mockMesh);
        renderer.setShader(mockShader);

        renderer.render(ANTIALIASED_LINE_SEGMENT_RENDERABLE, TIMESTAMP);
    }

    @Test
    public void testRenderUpdatesTimestamp() {
        renderer.setMesh(mockMesh);
        renderer.setShader(mockShader);

        renderer.render(ANTIALIASED_LINE_SEGMENT_RENDERABLE, TIMESTAMP);

        verify(mockTimestampValidator, once()).validateTimestamp(renderer.getClass().getCanonicalName(), TIMESTAMP);
    }

    @Test
    public void testRenderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                null,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_PROVIDER,
                                VERTEX_2_PROVIDER),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                new FakeStaticProvider<>(null),
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_PROVIDER,
                                VERTEX_2_PROVIDER),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                new FakeStaticProvider<>(-0.001f),
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_PROVIDER,
                                VERTEX_2_PROVIDER),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                new FakeStaticProvider<>(1.001f),
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_PROVIDER,
                                VERTEX_2_PROVIDER),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                null,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_PROVIDER,
                                VERTEX_2_PROVIDER),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                new FakeStaticProvider<>(null),
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_PROVIDER,
                                VERTEX_2_PROVIDER),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                new FakeStaticProvider<>(-0.001f),
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_PROVIDER,
                                VERTEX_2_PROVIDER),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                new FakeStaticProvider<>(1.001f),
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_PROVIDER,
                                VERTEX_2_PROVIDER),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                null,
                                COLOR_PROVIDER,
                                VERTEX_1_PROVIDER,
                                VERTEX_2_PROVIDER),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                new FakeStaticProvider<>(null),
                                COLOR_PROVIDER,
                                VERTEX_1_PROVIDER,
                                VERTEX_2_PROVIDER),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                new FakeStaticProvider<>(0f),
                                COLOR_PROVIDER,
                                VERTEX_1_PROVIDER,
                                VERTEX_2_PROVIDER),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                null,
                                VERTEX_1_PROVIDER,
                                VERTEX_2_PROVIDER),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                new FakeStaticProvider<>(null),
                                VERTEX_1_PROVIDER,
                                VERTEX_2_PROVIDER),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                null,
                                VERTEX_2_PROVIDER),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                new FakeStaticProvider<>(null),
                                VERTEX_2_PROVIDER),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_PROVIDER,
                                null),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderer
                .render(new FakeAntialiasedLineSegmentRenderable(
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER,
                                THICKNESS_PROVIDER,
                                COLOR_PROVIDER,
                                VERTEX_1_PROVIDER,
                                new FakeStaticProvider<>(null)),
                        TIMESTAMP));
    }

    @Test
    public void testSetMeshAndShaderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> renderer.setMesh(null));
        assertThrows(IllegalArgumentException.class,
                () -> renderer.setShader(null));
    }
}
