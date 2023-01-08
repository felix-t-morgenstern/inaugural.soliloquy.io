package inaugural.soliloquy.graphics.test.unit.rendering.renderers;

import inaugural.soliloquy.graphics.rendering.renderers.FiniteAnimationRenderer;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.assets.AnimationFrameSnippet;
import soliloquy.specs.graphics.renderables.FiniteAnimationRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;

class FiniteAnimationRendererTests {
    private final FakeRenderingBoundaries RENDERING_BOUNDARIES = new FakeRenderingBoundaries();
    private final FakeFloatBoxFactory FLOAT_BOX_FACTORY = new FakeFloatBoxFactory();
    private final FakeColorShiftStackAggregator COLOR_SHIFT_STACK_AGGREGATOR =
            new FakeColorShiftStackAggregator();
    private final long START_TIMESTAMP = 123123L;
    private final long MOST_RECENT_TIMESTAMP = 456L;

    private Renderer<FiniteAnimationRenderable> finiteAnimationRenderer;

    @BeforeAll
    static void setUpFixture() {
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
    void setUp() {
        RENDERING_BOUNDARIES.CurrentBoundaries = new FakeFloatBox(0f, 0f, 1f, 1f);
        finiteAnimationRenderer = new FiniteAnimationRenderer(RENDERING_BOUNDARIES,
                FLOAT_BOX_FACTORY, COLOR_SHIFT_STACK_AGGREGATOR, MOST_RECENT_TIMESTAMP);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteAnimationRenderer(null, FLOAT_BOX_FACTORY,
                        COLOR_SHIFT_STACK_AGGREGATOR, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteAnimationRenderer(RENDERING_BOUNDARIES, null,
                        COLOR_SHIFT_STACK_AGGREGATOR, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                new FiniteAnimationRenderer(RENDERING_BOUNDARIES, FLOAT_BOX_FACTORY,
                        null, MOST_RECENT_TIMESTAMP));
    }

    @Test
    void testSetMeshWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> finiteAnimationRenderer.setMesh(null));
    }

    @Test
    void testSetShaderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> finiteAnimationRenderer.setShader(null));
    }

    @Test
    void testRenderWithInvalidParams() {
        var animation = new FakeAnimation("id", 5000);
        var colorShiftProviders = new ArrayList<ProviderAtTime<ColorShift>>();
        var leftX = 0.11f;
        var topY = 0.22f;
        var rightX = 0.33f;
        var bottomY = 0.44f;

        assertThrows(IllegalArgumentException.class,
                () -> finiteAnimationRenderer.render(null, 0L));

        assertThrows(IllegalArgumentException.class, () -> finiteAnimationRenderer.render(
                new FakeFiniteAnimationRenderable(animation, null,
                        new FakeStaticProvider<>(
                                new FakeFloatBox(leftX, topY, rightX, bottomY)),
                        START_TIMESTAMP, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> finiteAnimationRenderer.render(
                new FakeFiniteAnimationRenderable(animation, colorShiftProviders,
                        null,
                        START_TIMESTAMP, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> finiteAnimationRenderer.render(
                new FakeFiniteAnimationRenderable(animation, colorShiftProviders,
                        new FakeStaticProvider<>(
                                null),
                        START_TIMESTAMP, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> finiteAnimationRenderer.render(
                new FakeFiniteAnimationRenderable(animation, colorShiftProviders,
                        new FakeStaticProvider<>(
                                new FakeFloatBox(leftX, topY, leftX, bottomY)),
                        START_TIMESTAMP, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> finiteAnimationRenderer.render(
                new FakeFiniteAnimationRenderable(animation, colorShiftProviders,
                        new FakeStaticProvider<>(
                                new FakeFloatBox(leftX, topY, rightX, topY)),
                        START_TIMESTAMP, UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> finiteAnimationRenderer.render(
                new FakeFiniteAnimationRenderable(animation, colorShiftProviders,
                        new FakeStaticProvider<>(
                                new FakeFloatBox(leftX, topY, rightX, bottomY)),
                        START_TIMESTAMP, null),
                0L
        ));
    }

    @Test
    void testRenderOutdatedTimestamp() {
        var animation = new FakeAnimation("id", 5000);
        var colorShiftProviders = new ArrayList<ProviderAtTime<ColorShift>>();
        var leftX = 0.11f;
        var topY = 0.22f;
        var rightX = 0.33f;
        var bottomY = 0.44f;
        var finiteAnimationRenderable =
                new FakeFiniteAnimationRenderable(animation, colorShiftProviders,
                        new FakeStaticProvider<>(
                                new FakeFloatBox(leftX, topY, rightX, bottomY)),
                        START_TIMESTAMP, UUID.randomUUID());
        finiteAnimationRenderer.setShader(new FakeShader());
        finiteAnimationRenderer.setMesh(new FakeMesh());
        finiteAnimationRenderer.render(finiteAnimationRenderable,
                START_TIMESTAMP + MOST_RECENT_TIMESTAMP);

        assertThrows(IllegalArgumentException.class,
                () -> finiteAnimationRenderer.render(finiteAnimationRenderable,
                        START_TIMESTAMP + MOST_RECENT_TIMESTAMP - 1L));
    }

    @Test
    void testRenderBeforeStartingTimestamp() {
        var animation = new FakeAnimation("id", 5000);
        var colorShiftProviders = new ArrayList<ProviderAtTime<ColorShift>>();
        var leftX = 0.11f;
        var topY = 0.22f;
        var rightX = 0.33f;
        var bottomY = 0.44f;
        var finiteAnimationRenderable =
                new FakeFiniteAnimationRenderable(animation, colorShiftProviders,
                        new FakeStaticProvider<>(
                                new FakeFloatBox(leftX, topY, rightX, bottomY)),
                        START_TIMESTAMP, UUID.randomUUID());
        finiteAnimationRenderer.setShader(new FakeShader());
        finiteAnimationRenderer.setMesh(new FakeMesh());

        finiteAnimationRenderer.render(finiteAnimationRenderable, START_TIMESTAMP - 1L);

        assertFalse(animation.SnippetAtFrameCalled);
    }

    @Test
    void testRenderPassesTimestampToColorShiftStackAggregator() {
        var animation = new FakeAnimation("id", 5000);
        var colorShiftProviders = new ArrayList<ProviderAtTime<ColorShift>>();
        var leftX = 0.11f;
        var topY = 0.22f;
        var rightX = 0.33f;
        var bottomY = 0.44f;
        var finiteAnimationRenderable =
                new FakeFiniteAnimationRenderable(animation, colorShiftProviders,
                        new FakeStaticProvider<>(
                                new FakeFloatBox(leftX, topY, rightX, bottomY)),
                        START_TIMESTAMP, UUID.randomUUID());
        finiteAnimationRenderer.setShader(new FakeShader());
        finiteAnimationRenderer.setMesh(new FakeMesh());
        finiteAnimationRenderer.render(finiteAnimationRenderable, START_TIMESTAMP);

        assertEquals(START_TIMESTAMP, (long) COLOR_SHIFT_STACK_AGGREGATOR.Input);
    }

    @Test
    void testRenderAfterAnimationEndDeletes() {
        int animationMsDuration = 456;
        var animation = new FakeAnimation("id", animationMsDuration);
        var colorShiftProviders = new ArrayList<ProviderAtTime<ColorShift>>();
        var leftX = 0.11f;
        var topY = 0.22f;
        var rightX = 0.33f;
        var bottomY = 0.44f;
        var finiteAnimationRenderable =
                new FakeFiniteAnimationRenderable(animation, colorShiftProviders,
                        new FakeStaticProvider<>(
                                new FakeFloatBox(leftX, topY, rightX, bottomY)),
                        START_TIMESTAMP, UUID.randomUUID());
        finiteAnimationRenderer.setShader(new FakeShader());
        finiteAnimationRenderer.setMesh(new FakeMesh());

        finiteAnimationRenderer.render(finiteAnimationRenderable,
                START_TIMESTAMP + animationMsDuration - 1);

        assertFalse(finiteAnimationRenderable.Deleted);

        finiteAnimationRenderer.render(finiteAnimationRenderable,
                START_TIMESTAMP + animationMsDuration);

        assertTrue(finiteAnimationRenderable.Deleted);
    }

    @Test
    void testMostRecentTimestamp() {
        assertEquals(MOST_RECENT_TIMESTAMP, (long) finiteAnimationRenderer.mostRecentTimestamp());
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(Renderer.class.getCanonicalName() + "<" +
                        FiniteAnimationRenderable.class.getCanonicalName() + "<" +
                        AnimationFrameSnippet.class.getCanonicalName() + ">>",
                finiteAnimationRenderer.getInterfaceName());
    }
}
