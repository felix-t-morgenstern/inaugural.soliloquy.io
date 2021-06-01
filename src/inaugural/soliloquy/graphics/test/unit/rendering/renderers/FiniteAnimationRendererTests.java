package inaugural.soliloquy.graphics.test.unit.rendering.renderers;

import inaugural.soliloquy.graphics.rendering.renderers.FiniteAnimationRenderer;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.FiniteAnimationRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;

class FiniteAnimationRendererTests {
    private final FakeRenderingBoundaries RENDERING_BOUNDARIES = new FakeRenderingBoundaries();
    private final FakeFloatBoxFactory FLOAT_BOX_FACTORY = new FakeFloatBoxFactory();
    private final long START_TIMESTAMP = 123123L;

    private Renderer<FiniteAnimationRenderable> _finiteAnimationRenderer;

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
        _finiteAnimationRenderer =
                new FiniteAnimationRenderer(RENDERING_BOUNDARIES, FLOAT_BOX_FACTORY);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderer(null, FLOAT_BOX_FACTORY));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderer(RENDERING_BOUNDARIES, null));
    }

    @Test
    void testSetMeshWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> _finiteAnimationRenderer.setMesh(null));
    }

    @Test
    void testSetShaderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> _finiteAnimationRenderer.setShader(null));
    }

    @Test
    void testRenderWithInvalidParams() {
        FakeAnimation animation = new FakeAnimation("id");
        ArrayList<ColorShift> colorShifts = new ArrayList<>();
        float leftX = 0.11f;
        float topY = 0.22f;
        float rightX = 0.33f;
        float bottomY = 0.44f;

        assertThrows(IllegalArgumentException.class,
                () -> _finiteAnimationRenderer.render(null, 0L));

        assertThrows(IllegalArgumentException.class, () -> _finiteAnimationRenderer.render(
                new FakeFiniteAnimationRenderable(animation, null,
                        new FakeStaticProviderAtTime<>(
                                new FakeFloatBox(leftX, topY, rightX, bottomY)),
                        START_TIMESTAMP, new FakeEntityUuid()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _finiteAnimationRenderer.render(
                new FakeFiniteAnimationRenderable(animation, colorShifts,
                        null,
                        START_TIMESTAMP, new FakeEntityUuid()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _finiteAnimationRenderer.render(
                new FakeFiniteAnimationRenderable(animation, colorShifts,
                        new FakeStaticProviderAtTime<>(
                                null),
                        START_TIMESTAMP, new FakeEntityUuid()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _finiteAnimationRenderer.render(
                new FakeFiniteAnimationRenderable(animation, colorShifts,
                        new FakeStaticProviderAtTime<>(
                                new FakeFloatBox(leftX, topY, leftX, bottomY)),
                        START_TIMESTAMP, new FakeEntityUuid()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _finiteAnimationRenderer.render(
                new FakeFiniteAnimationRenderable(animation, colorShifts,
                        new FakeStaticProviderAtTime<>(
                                new FakeFloatBox(leftX, topY, rightX, topY)),
                        START_TIMESTAMP, new FakeEntityUuid()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _finiteAnimationRenderer.render(
                new FakeFiniteAnimationRenderable(animation, colorShifts,
                        new FakeStaticProviderAtTime<>(
                                new FakeFloatBox(leftX, topY, rightX, bottomY)),
                        START_TIMESTAMP, null),
                0L
        ));
    }

    @Test
    void testRenderOutdatedTimestamp() {
        FakeAnimation animation = new FakeAnimation("id");
        ArrayList<ColorShift> colorShifts = new ArrayList<>();
        float leftX = 0.11f;
        float topY = 0.22f;
        float rightX = 0.33f;
        float bottomY = 0.44f;
        FakeFiniteAnimationRenderable finiteAnimationRenderable =
                new FakeFiniteAnimationRenderable(animation, colorShifts,
                        new FakeStaticProviderAtTime<>(
                                new FakeFloatBox(leftX, topY, rightX, bottomY)),
                        START_TIMESTAMP, new FakeEntityUuid());
        long timestamp = 100L;
        _finiteAnimationRenderer.setShader(new FakeShader());
        _finiteAnimationRenderer.setMesh(new FakeMesh());
        _finiteAnimationRenderer.render(finiteAnimationRenderable, START_TIMESTAMP + timestamp);

        assertThrows(IllegalArgumentException.class,
                () -> _finiteAnimationRenderer.render(finiteAnimationRenderable,
                        START_TIMESTAMP + timestamp));
    }

    @Test
    void testRenderBeforeStartingTimestamp() {
        FakeAnimation animation = new FakeAnimation("id");
        ArrayList<ColorShift> colorShifts = new ArrayList<>();
        float leftX = 0.11f;
        float topY = 0.22f;
        float rightX = 0.33f;
        float bottomY = 0.44f;
        FakeFiniteAnimationRenderable finiteAnimationRenderable =
                new FakeFiniteAnimationRenderable(animation, colorShifts,
                        new FakeStaticProviderAtTime<>(
                                new FakeFloatBox(leftX, topY, rightX, bottomY)),
                        START_TIMESTAMP, new FakeEntityUuid());
        _finiteAnimationRenderer.setShader(new FakeShader());
        _finiteAnimationRenderer.setMesh(new FakeMesh());

        _finiteAnimationRenderer.render(finiteAnimationRenderable, START_TIMESTAMP - 1L);

        assertFalse(animation.SnippetAtFrameCalled);
    }

    @Test
    void testRenderAfterAnimationEndDeletes() {
        int animationMsDuration = 456;
        FakeAnimation animation = new FakeAnimation("id", animationMsDuration);
        ArrayList<ColorShift> colorShifts = new ArrayList<>();
        float leftX = 0.11f;
        float topY = 0.22f;
        float rightX = 0.33f;
        float bottomY = 0.44f;
        FakeFiniteAnimationRenderable finiteAnimationRenderable =
                new FakeFiniteAnimationRenderable(animation, colorShifts,
                        new FakeStaticProviderAtTime<>(
                                new FakeFloatBox(leftX, topY, rightX, bottomY)),
                        START_TIMESTAMP, new FakeEntityUuid());
        _finiteAnimationRenderer.setShader(new FakeShader());
        _finiteAnimationRenderer.setMesh(new FakeMesh());

        _finiteAnimationRenderer.render(finiteAnimationRenderable,
                START_TIMESTAMP + animationMsDuration - 1);

        assertFalse(finiteAnimationRenderable.Deleted);

        _finiteAnimationRenderer.render(finiteAnimationRenderable,
                START_TIMESTAMP + animationMsDuration);

        assertTrue(finiteAnimationRenderable.Deleted);
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(Renderer.class.getCanonicalName() + "<" +
                        FiniteAnimationRenderable.class.getCanonicalName() + ">",
                _finiteAnimationRenderer.getInterfaceName());
    }
}
