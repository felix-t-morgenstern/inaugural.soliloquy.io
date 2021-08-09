package inaugural.soliloquy.graphics.test.unit.rendering.renderers;

import inaugural.soliloquy.graphics.rendering.renderers.GlobalLoopingAnimationRenderer;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import inaugural.soliloquy.graphics.test.testdoubles.spies.SpyGlobalLoopingAnimation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.GlobalLoopingAnimationRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;

class GlobalLoopingAnimationRendererTests {
    private final FakeRenderingBoundaries RENDERING_BOUNDARIES = new FakeRenderingBoundaries();
    private final FakeFloatBoxFactory FLOAT_BOX_FACTORY = new FakeFloatBoxFactory();
    private final FakeColorShiftStackAggregator COLOR_SHIFT_STACK_AGGREGATOR =
            new FakeColorShiftStackAggregator();
    private final long MOST_RECENT_TIMESTAMP = 123123L;

    private Renderer<GlobalLoopingAnimationRenderable> _globalLoopingAnimationRenderer;

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
        RENDERING_BOUNDARIES.CurrentBoundaries = new FakeFloatBox(0f, 0f, 1f, 1f);
        _globalLoopingAnimationRenderer =
                new GlobalLoopingAnimationRenderer(RENDERING_BOUNDARIES, FLOAT_BOX_FACTORY,
                        COLOR_SHIFT_STACK_AGGREGATOR, MOST_RECENT_TIMESTAMP);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationRenderer(null, FLOAT_BOX_FACTORY,
                        COLOR_SHIFT_STACK_AGGREGATOR, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationRenderer(RENDERING_BOUNDARIES, null,
                        COLOR_SHIFT_STACK_AGGREGATOR, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                new GlobalLoopingAnimationRenderer(RENDERING_BOUNDARIES, FLOAT_BOX_FACTORY,
                        null, MOST_RECENT_TIMESTAMP));
    }

    @Test
    void testSetMeshWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> _globalLoopingAnimationRenderer.setMesh(null));
    }

    @Test
    void testSetShaderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> _globalLoopingAnimationRenderer.setShader(null));
    }

    @Test
    void testRenderWithInvalidParams() {
        SpyGlobalLoopingAnimation renderableAnimation = new SpyGlobalLoopingAnimation();
        ArrayList<ProviderAtTime<ColorShift>> colorShiftProviders = new ArrayList<>();
        float leftX = 0.11f;
        float topY = 0.22f;
        float rightX = 0.33f;
        float bottomY = 0.44f;

        assertThrows(IllegalArgumentException.class,
                () -> _globalLoopingAnimationRenderer.render(null, 0L));

        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderer.render(
                new FakeGlobalLoopingAnimationRenderable(null, colorShiftProviders,
                        new FakeStaticProviderAtTime<>(
                                new FakeFloatBox(leftX, topY, rightX, bottomY)),
                        new FakeEntityUuid()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderer.render(
                new FakeGlobalLoopingAnimationRenderable(renderableAnimation, null,
                        new FakeStaticProviderAtTime<>(
                                new FakeFloatBox(leftX, topY, rightX, bottomY)),
                        new FakeEntityUuid()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderer.render(
                new FakeGlobalLoopingAnimationRenderable(renderableAnimation, colorShiftProviders,
                        null,
                        new FakeEntityUuid()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderer.render(
                new FakeGlobalLoopingAnimationRenderable(renderableAnimation, colorShiftProviders,
                        new FakeStaticProviderAtTime<>(null),
                        new FakeEntityUuid()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderer.render(
                new FakeGlobalLoopingAnimationRenderable(renderableAnimation, colorShiftProviders,
                        new FakeStaticProviderAtTime<>(
                                new FakeFloatBox(leftX, topY, leftX, bottomY)),
                        new FakeEntityUuid()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderer.render(
                new FakeGlobalLoopingAnimationRenderable(renderableAnimation, null,
                        new FakeStaticProviderAtTime<>(
                                new FakeFloatBox(leftX, topY, rightX, topY)),
                        new FakeEntityUuid()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderer.render(
                new FakeGlobalLoopingAnimationRenderable(renderableAnimation, null,
                        new FakeStaticProviderAtTime<>(
                                new FakeFloatBox(leftX, topY, rightX, bottomY)),
                        null),
                0L
        ));
    }

    @Test
    void testRenderOutdatedTimestamp() {
        SpyGlobalLoopingAnimation renderableAnimation = new SpyGlobalLoopingAnimation();
        ArrayList<ProviderAtTime<ColorShift>> colorShiftProviders = new ArrayList<>();
        float leftX = 0.11f;
        float topY = 0.22f;
        float rightX = 0.33f;
        float bottomY = 0.44f;
        FakeGlobalLoopingAnimationRenderable globalLoopingAnimationRenderable =
                new FakeGlobalLoopingAnimationRenderable(renderableAnimation, colorShiftProviders,
                        new FakeStaticProviderAtTime<>(
                                new FakeFloatBox(leftX, topY, rightX, bottomY)),
                        new FakeEntityUuid());
        _globalLoopingAnimationRenderer.setShader(new FakeShader());
        _globalLoopingAnimationRenderer.setMesh(new FakeMesh());

        assertThrows(IllegalArgumentException.class,
                () -> _globalLoopingAnimationRenderer.render(globalLoopingAnimationRenderable,
                        MOST_RECENT_TIMESTAMP - 1L));
    }

    @Test
    void testRenderPassesTimestampToColorShiftStackAggregator() {
        SpyGlobalLoopingAnimation renderableAnimation = new SpyGlobalLoopingAnimation();
        ArrayList<ProviderAtTime<ColorShift>> colorShiftProviders = new ArrayList<>();
        float leftX = 0.11f;
        float topY = 0.22f;
        float rightX = 0.33f;
        float bottomY = 0.44f;
        FakeGlobalLoopingAnimationRenderable globalLoopingAnimationRenderable =
                new FakeGlobalLoopingAnimationRenderable(renderableAnimation, colorShiftProviders,
                        new FakeStaticProviderAtTime<>(
                                new FakeFloatBox(leftX, topY, rightX, bottomY)),
                        new FakeEntityUuid());
        _globalLoopingAnimationRenderer.setShader(new FakeShader());
        _globalLoopingAnimationRenderer.setMesh(new FakeMesh());
        _globalLoopingAnimationRenderer.render(globalLoopingAnimationRenderable,
                MOST_RECENT_TIMESTAMP + 123);

        assertEquals(MOST_RECENT_TIMESTAMP + 123, (long)COLOR_SHIFT_STACK_AGGREGATOR.Input);
    }

    @Test
    void testMostRecentTimestamp() {
        assertEquals(MOST_RECENT_TIMESTAMP,
                (long)_globalLoopingAnimationRenderer.mostRecentTimestamp());
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(Renderer.class.getCanonicalName() + "<" +
                        GlobalLoopingAnimationRenderable.class.getCanonicalName() + ">",
                _globalLoopingAnimationRenderer.getInterfaceName());
    }
}
