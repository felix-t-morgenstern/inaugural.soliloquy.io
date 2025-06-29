package inaugural.soliloquy.io.test.unit.graphics.rendering.renderers;

import inaugural.soliloquy.io.graphics.rendering.renderers.GlobalLoopingAnimationRenderer;
import inaugural.soliloquy.io.test.testdoubles.fakes.*;
import inaugural.soliloquy.io.test.testdoubles.spies.SpyGlobalLoopingAnimation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.io.graphics.renderables.GlobalLoopingAnimationRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.util.List;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

public class GlobalLoopingAnimationRendererTests {
    private final FakeRenderingBoundaries RENDERING_BOUNDARIES = new FakeRenderingBoundaries();
    private final FakeColorShiftStackAggregator COLOR_SHIFT_STACK_AGGREGATOR =
            new FakeColorShiftStackAggregator();
    private final long MOST_RECENT_TIMESTAMP = 123123L;

    private Renderer<GlobalLoopingAnimationRenderable> globalLoopingAnimationRenderer;

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
        globalLoopingAnimationRenderer =
                new GlobalLoopingAnimationRenderer(RENDERING_BOUNDARIES,
                        COLOR_SHIFT_STACK_AGGREGATOR, MOST_RECENT_TIMESTAMP);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationRenderer(null,
                        COLOR_SHIFT_STACK_AGGREGATOR, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationRenderer(RENDERING_BOUNDARIES,
                        null, MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testSetMeshWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> globalLoopingAnimationRenderer.setMesh(null));
    }

    @Test
    public void testSetShaderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> globalLoopingAnimationRenderer.setShader(null));
    }

    @Test
    public void testRenderWithInvalidArgs() {
        var renderableAnimation = new SpyGlobalLoopingAnimation();
        List<ProviderAtTime<ColorShift>> colorShiftProviders = listOf();
        var leftX = 0.11f;
        var topY = 0.22f;
        var rightX = 0.33f;
        var bottomY = 0.44f;

        assertThrows(IllegalArgumentException.class,
                () -> globalLoopingAnimationRenderer.render(null, 0L));

        assertThrows(IllegalArgumentException.class, () -> globalLoopingAnimationRenderer.render(
                new FakeGlobalLoopingAnimationRenderable(null, colorShiftProviders,
                        new FakeStaticProvider<>(
                                floatBoxOf(leftX, topY, rightX, bottomY)),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> globalLoopingAnimationRenderer.render(
                new FakeGlobalLoopingAnimationRenderable(renderableAnimation, null,
                        new FakeStaticProvider<>(
                                floatBoxOf(leftX, topY, rightX, bottomY)),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> globalLoopingAnimationRenderer.render(
                new FakeGlobalLoopingAnimationRenderable(renderableAnimation, colorShiftProviders,
                        null,
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> globalLoopingAnimationRenderer.render(
                new FakeGlobalLoopingAnimationRenderable(renderableAnimation, colorShiftProviders,
                        new FakeStaticProvider<>(null),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> globalLoopingAnimationRenderer.render(
                new FakeGlobalLoopingAnimationRenderable(renderableAnimation, colorShiftProviders,
                        new FakeStaticProvider<>(
                                floatBoxOf(leftX, topY, leftX, bottomY)),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> globalLoopingAnimationRenderer.render(
                new FakeGlobalLoopingAnimationRenderable(renderableAnimation, null,
                        new FakeStaticProvider<>(
                                floatBoxOf(leftX, topY, rightX, topY)),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> globalLoopingAnimationRenderer.render(
                new FakeGlobalLoopingAnimationRenderable(renderableAnimation, null,
                        new FakeStaticProvider<>(
                                floatBoxOf(leftX, topY, rightX, bottomY)),
                        null),
                0L
        ));
    }

    @Test
    public void testRenderOutdatedTimestamp() {
        var renderableAnimation = new SpyGlobalLoopingAnimation();
        List<ProviderAtTime<ColorShift>> colorShiftProviders = listOf();
        var leftX = 0.11f;
        var topY = 0.22f;
        var rightX = 0.33f;
        var bottomY = 0.44f;
        var globalLoopingAnimationRenderable =
                new FakeGlobalLoopingAnimationRenderable(renderableAnimation, colorShiftProviders,
                        new FakeStaticProvider<>(floatBoxOf(leftX, topY, rightX, bottomY)),
                        UUID.randomUUID());
        globalLoopingAnimationRenderer.setShader(new FakeShader());
        globalLoopingAnimationRenderer.setMesh(new FakeMesh());

        assertThrows(IllegalArgumentException.class,
                () -> globalLoopingAnimationRenderer.render(globalLoopingAnimationRenderable,
                        MOST_RECENT_TIMESTAMP - 1L));
    }

    @Test
    public void testRenderPassesTimestampToColorShiftStackAggregator() {
        var renderableAnimation = new SpyGlobalLoopingAnimation();
        List<ProviderAtTime<ColorShift>> colorShiftProviders = listOf();
        var leftX = 0.11f;
        var topY = 0.22f;
        var rightX = 0.33f;
        var bottomY = 0.44f;
        var globalLoopingAnimationRenderable =
                new FakeGlobalLoopingAnimationRenderable(renderableAnimation, colorShiftProviders,
                        new FakeStaticProvider<>(floatBoxOf(leftX, topY, rightX, bottomY)),
                        UUID.randomUUID());
        globalLoopingAnimationRenderer.setShader(new FakeShader());
        globalLoopingAnimationRenderer.setMesh(new FakeMesh());
        globalLoopingAnimationRenderer.render(globalLoopingAnimationRenderable,
                MOST_RECENT_TIMESTAMP + 123);

        assertEquals(MOST_RECENT_TIMESTAMP + 123, (long) COLOR_SHIFT_STACK_AGGREGATOR.Input);
    }

    @Test
    public void testMostRecentTimestamp() {
        assertEquals(MOST_RECENT_TIMESTAMP,
                (long) globalLoopingAnimationRenderer.mostRecentTimestamp());
    }
}
