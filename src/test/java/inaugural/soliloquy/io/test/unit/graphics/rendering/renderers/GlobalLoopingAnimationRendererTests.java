package inaugural.soliloquy.io.test.unit.graphics.rendering.renderers;

import inaugural.soliloquy.io.graphics.rendering.renderers.GlobalLoopingAnimationRenderer;
import inaugural.soliloquy.io.test.testdoubles.fakes.*;
import inaugural.soliloquy.io.test.testdoubles.spies.SpyGlobalLoopingAnimation;
import inaugural.soliloquy.tools.collections.Collections;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.graphics.renderables.GlobalLoopingAnimationRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.rendering.Mesh;
import soliloquy.specs.io.graphics.rendering.Shader;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.randomLong;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

@ExtendWith(MockitoExtension.class)
public class GlobalLoopingAnimationRendererTests {
    private final FakeRenderingBoundaries RENDERING_BOUNDARIES = new FakeRenderingBoundaries();
    private final FakeColorShiftStackAggregator COLOR_SHIFT_STACK_AGGREGATOR =
            new FakeColorShiftStackAggregator();
    private final long MOST_RECENT_TIMESTAMP = randomLong();

    @Mock private TimestampValidator mockTimestampValidator;

    private Renderer<GlobalLoopingAnimationRenderable> renderer;

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
        RENDERING_BOUNDARIES.CurrentBoundaries = floatBoxOf(0f, 0f, 1f, 1f);
        renderer =
                new GlobalLoopingAnimationRenderer(RENDERING_BOUNDARIES,
                        COLOR_SHIFT_STACK_AGGREGATOR, mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new GlobalLoopingAnimationRenderer(null, COLOR_SHIFT_STACK_AGGREGATOR,
                        mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new GlobalLoopingAnimationRenderer(RENDERING_BOUNDARIES, null,
                        mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new GlobalLoopingAnimationRenderer(RENDERING_BOUNDARIES,
                        COLOR_SHIFT_STACK_AGGREGATOR, null));
    }

    @Test
    public void testSetMeshWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> renderer.setMesh(null));
    }

    @Test
    public void testSetShaderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> renderer.setShader(null));
    }

    @Test
    public void testRenderWithInvalidArgs() {
        var renderableAnimation = new SpyGlobalLoopingAnimation();
        var colorShiftProviders = Collections.<ColorShift>listOf();
        var leftX = 0.11f;
        var topY = 0.22f;
        var rightX = 0.33f;
        var bottomY = 0.44f;

        assertThrows(IllegalArgumentException.class,
                () -> renderer.render(null, 0L));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeGlobalLoopingAnimationRenderable(null, colorShiftProviders,
                        new FakeStaticProvider<>(
                                floatBoxOf(leftX, topY, rightX, bottomY)),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeGlobalLoopingAnimationRenderable(renderableAnimation, null,
                        new FakeStaticProvider<>(
                                floatBoxOf(leftX, topY, rightX, bottomY)),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeGlobalLoopingAnimationRenderable(renderableAnimation, colorShiftProviders,
                        null,
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeGlobalLoopingAnimationRenderable(renderableAnimation, colorShiftProviders,
                        new FakeStaticProvider<>(null),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeGlobalLoopingAnimationRenderable(renderableAnimation, colorShiftProviders,
                        new FakeStaticProvider<>(
                                floatBoxOf(leftX, topY, leftX, bottomY)),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeGlobalLoopingAnimationRenderable(renderableAnimation, null,
                        new FakeStaticProvider<>(
                                floatBoxOf(leftX, topY, rightX, topY)),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeGlobalLoopingAnimationRenderable(renderableAnimation, null,
                        new FakeStaticProvider<>(
                                floatBoxOf(leftX, topY, rightX, bottomY)),
                        null),
                0L
        ));
    }

    @Test
    public void testRenderPassesTimestampToColorShiftStackAggregator() {
        var renderableAnimation = new SpyGlobalLoopingAnimation();
        var colorShiftProviders = Collections.<ColorShift>listOf();
        var leftX = 0.11f;
        var topY = 0.22f;
        var rightX = 0.33f;
        var bottomY = 0.44f;
        var globalLoopingAnimationRenderable =
                new FakeGlobalLoopingAnimationRenderable(renderableAnimation, colorShiftProviders,
                        new FakeStaticProvider<>(floatBoxOf(leftX, topY, rightX, bottomY)),
                        UUID.randomUUID());
        renderer.setShader(mock(Shader.class));
        renderer.setMesh(mock(Mesh.class));
        renderer.render(globalLoopingAnimationRenderable,
                MOST_RECENT_TIMESTAMP + 123);

        assertEquals(MOST_RECENT_TIMESTAMP + 123, (long) COLOR_SHIFT_STACK_AGGREGATOR.Input);
    }

    @Test
    public void testGetMostRecentTimestamp() {
        var mostRecentTimestamp = randomLong();
        when(mockTimestampValidator.mostRecentTimestamp()).thenReturn(mostRecentTimestamp);

        assertEquals(mostRecentTimestamp, renderer.mostRecentTimestamp());
    }

    @Test
    public void testRenderUpdatesTimestamp() {
        var renderableAnimation = new SpyGlobalLoopingAnimation();
        var colorShiftProviders = Collections.<ColorShift>listOf();
        var leftX = 0.11f;
        var topY = 0.22f;
        var rightX = 0.33f;
        var bottomY = 0.44f;
        var globalLoopingAnimationRenderable =
                new FakeGlobalLoopingAnimationRenderable(renderableAnimation, colorShiftProviders,
                        new FakeStaticProvider<>(floatBoxOf(leftX, topY, rightX, bottomY)),
                        UUID.randomUUID());
        renderer.setShader(mock(Shader.class));
        renderer.setMesh(mock(Mesh.class));
        var timestamp = randomLong();

        renderer.render(globalLoopingAnimationRenderable, timestamp);

        verify(mockTimestampValidator, once()).validateTimestamp(renderer.getClass().getCanonicalName(), timestamp);
    }
}
