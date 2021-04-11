package inaugural.soliloquy.graphics.test.unit.rendering;

import inaugural.soliloquy.graphics.rendering.GlobalLoopingAnimationRenderer;
import inaugural.soliloquy.graphics.test.fakes.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.GlobalLoopingAnimationRenderable;
import soliloquy.specs.graphics.rendering.Renderer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;

class GlobalLoopingAnimationRendererTests {
    private final FakeRenderingBoundaries RENDERING_BOUNDARIES = new FakeRenderingBoundaries();
    private final FakeFloatBoxFactory FLOAT_BOX_FACTORY = new FakeFloatBoxFactory();

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
                new GlobalLoopingAnimationRenderer(RENDERING_BOUNDARIES, FLOAT_BOX_FACTORY);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new GlobalLoopingAnimationRenderer(null, FLOAT_BOX_FACTORY));
        assertThrows(IllegalArgumentException.class,
                () -> new GlobalLoopingAnimationRenderer(RENDERING_BOUNDARIES, null));
    }

    @Test
    void testSetMeshWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderer.setMesh(null));
    }

    @Test
    void testSetShaderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderer.setShader(null));
    }

    @Test
    void testRenderWithInvalidParams() {
        FakeRenderableAnimation renderableAnimation = new FakeRenderableAnimation();
        List<ColorShift> colorShifts = new ArrayList<>();
        float leftX = 0.11f;
        float topY = 0.22f;
        float rightX = 0.33f;
        float bottomY = 0.44f;

        assertThrows(IllegalArgumentException.class,
                () -> _globalLoopingAnimationRenderer.render(null, 0L));

        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderer.render(
                new FakeGlobalLoopingAnimationRenderable(null, colorShifts,
                        new FakeFloatBox(leftX, topY, rightX, bottomY)),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderer.render(
                new FakeGlobalLoopingAnimationRenderable(renderableAnimation, null,
                        new FakeFloatBox(leftX, topY, rightX, bottomY)),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderer.render(
                new FakeGlobalLoopingAnimationRenderable(renderableAnimation, colorShifts,
                        new FakeFloatBox(leftX, topY, leftX, bottomY)),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderer.render(
                new FakeGlobalLoopingAnimationRenderable(renderableAnimation, null,
                        new FakeFloatBox(leftX, topY, rightX, topY)),
                0L
        ));
    }

    @Test
    void testRenderOutdatedTimestamp() {
        FakeRenderableAnimation renderableAnimation = new FakeRenderableAnimation();
        List<ColorShift> colorShifts = new ArrayList<>();
        float leftX = 0.11f;
        float topY = 0.22f;
        float rightX = 0.33f;
        float bottomY = 0.44f;
        FakeGlobalLoopingAnimationRenderable globalLoopingAnimationRenderable =
                new FakeGlobalLoopingAnimationRenderable(renderableAnimation, colorShifts,
                        new FakeFloatBox(leftX, topY, rightX, bottomY));
        long timestamp = 100L;
        _globalLoopingAnimationRenderer.setShader(new FakeShader());
        _globalLoopingAnimationRenderer.setMesh(new FakeMesh());
        _globalLoopingAnimationRenderer.render(globalLoopingAnimationRenderable, timestamp);

        assertThrows(IllegalArgumentException.class,
                () -> _globalLoopingAnimationRenderer.render(globalLoopingAnimationRenderable,
                        timestamp - 1L));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(Renderer.class.getCanonicalName() + "<" +
                        GlobalLoopingAnimationRenderable.class.getCanonicalName() + ">",
                _globalLoopingAnimationRenderer.getInterfaceName());
    }
}
