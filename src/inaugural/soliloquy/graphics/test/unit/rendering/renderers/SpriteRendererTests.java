package inaugural.soliloquy.graphics.test.unit.rendering.renderers;

import inaugural.soliloquy.graphics.rendering.renderers.SpriteRenderer;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.assets.Sprite;
import soliloquy.specs.graphics.renderables.SpriteRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL.createCapabilities;

class SpriteRendererTests {
    private final FakeRenderingBoundaries RENDERING_BOUNDARIES = new FakeRenderingBoundaries();
    private final FakeFloatBoxFactory FLOAT_BOX_FACTORY = new FakeFloatBoxFactory();
    private final FakeWindowResolutionManager WINDOW_RESOLUTION_MANAGER =
            new FakeWindowResolutionManager();
    private final FakeColorShiftStackAggregator COLOR_SHIFT_STACK_AGGREGATOR =
            new FakeColorShiftStackAggregator();

    private Renderer<SpriteRenderable> _spriteRenderer;

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
        _spriteRenderer = new SpriteRenderer(RENDERING_BOUNDARIES, FLOAT_BOX_FACTORY,
                WINDOW_RESOLUTION_MANAGER, COLOR_SHIFT_STACK_AGGREGATOR);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new SpriteRenderer(null, FLOAT_BOX_FACTORY,
                        WINDOW_RESOLUTION_MANAGER, COLOR_SHIFT_STACK_AGGREGATOR));
        assertThrows(IllegalArgumentException.class, () ->
                new SpriteRenderer(RENDERING_BOUNDARIES, null,
                        WINDOW_RESOLUTION_MANAGER, COLOR_SHIFT_STACK_AGGREGATOR));
        assertThrows(IllegalArgumentException.class, () ->
                new SpriteRenderer(RENDERING_BOUNDARIES, FLOAT_BOX_FACTORY,
                        null, COLOR_SHIFT_STACK_AGGREGATOR));
        assertThrows(IllegalArgumentException.class, () ->
                new SpriteRenderer(RENDERING_BOUNDARIES, FLOAT_BOX_FACTORY,
                        WINDOW_RESOLUTION_MANAGER, null));
    }

    @Test
    void testSetMeshWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _spriteRenderer.setMesh(null));
    }

    @Test
    void testSetShaderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _spriteRenderer.setShader(null));
    }

    @Test
    void testRenderWithInvalidParams() {
        Sprite sprite = new FakeSprite();
        List<ColorShift> colorShifts = new ArrayList<>();
        float leftX = 0.11f;
        float topY = 0.22f;
        float rightX = 0.33f;
        float bottomY = 0.44f;
        Float borderThickness = 0.01f;
        Color borderColor = Color.RED;

        assertThrows(IllegalArgumentException.class, () -> _spriteRenderer.render(null, 0L));

        assertThrows(IllegalArgumentException.class, () -> _spriteRenderer.render(
                new FakeSpriteRenderable(null, colorShifts,
                        new FakeStaticProviderAtTime<>(
                                new FakeFloatBox(leftX, topY, rightX, bottomY)),
                        new FakeStaticProviderAtTime<>(null),
                        new FakeStaticProviderAtTime<>(null),
                        new FakeEntityUuid()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _spriteRenderer.render(
                new FakeSpriteRenderable(sprite, null,
                        new FakeStaticProviderAtTime<>(
                                new FakeFloatBox(leftX, topY, rightX, bottomY)),
                        new FakeStaticProviderAtTime<>(null),
                        new FakeStaticProviderAtTime<>(null),
                        new FakeEntityUuid()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _spriteRenderer.render(
                new FakeSpriteRenderable(sprite, colorShifts,
                        null,
                        new FakeStaticProviderAtTime<>(null),
                        new FakeStaticProviderAtTime<>(null),
                        new FakeEntityUuid()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _spriteRenderer.render(
                new FakeSpriteRenderable(sprite, colorShifts,
                        new FakeStaticProviderAtTime<>(
                                new FakeFloatBox(leftX, topY, rightX, bottomY)),
                        null,
                        new FakeStaticProviderAtTime<>(null),
                        new FakeEntityUuid()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _spriteRenderer.render(
                new FakeSpriteRenderable(sprite, colorShifts,
                        new FakeStaticProviderAtTime<>(
                                new FakeFloatBox(leftX, topY, rightX, bottomY)),
                        new FakeStaticProviderAtTime<>(null),
                        null,
                        new FakeEntityUuid()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _spriteRenderer.render(
                new FakeSpriteRenderable(sprite, colorShifts,
                        new FakeStaticProviderAtTime<>(new FakeFloatBox(leftX, topY, leftX, bottomY)),
                        new FakeStaticProviderAtTime<>(null),
                        new FakeStaticProviderAtTime<>(null),
                        new FakeEntityUuid()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _spriteRenderer.render(
                new FakeSpriteRenderable(sprite, null,
                        new FakeStaticProviderAtTime<>(
                                new FakeFloatBox(leftX, topY, rightX, topY)),
                        new FakeStaticProviderAtTime<>(null),
                        new FakeStaticProviderAtTime<>(null),
                        new FakeEntityUuid()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _spriteRenderer.render(
                new FakeSpriteRenderable(sprite, colorShifts,
                        new FakeStaticProviderAtTime<>(
                                new FakeFloatBox(leftX, topY, rightX, bottomY)),
                        new FakeStaticProviderAtTime<>(borderThickness),
                        new FakeStaticProviderAtTime<>(null),
                        new FakeEntityUuid()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _spriteRenderer.render(
                new FakeSpriteRenderable(sprite, colorShifts,
                        new FakeStaticProviderAtTime<>(
                                new FakeFloatBox(leftX, topY, rightX, bottomY)),
                        new FakeStaticProviderAtTime<>(-0.0001f),
                        new FakeStaticProviderAtTime<>(borderColor),
                        new FakeEntityUuid()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _spriteRenderer.render(
                new FakeSpriteRenderable(sprite, colorShifts,
                        new FakeStaticProviderAtTime<>(
                                new FakeFloatBox(leftX, topY, rightX, bottomY)),
                        new FakeStaticProviderAtTime<>(1.0001f),
                        new FakeStaticProviderAtTime<>(borderColor),
                        new FakeEntityUuid()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> _spriteRenderer.render(
                new FakeSpriteRenderable(sprite, null,
                        new FakeStaticProviderAtTime<>(
                                new FakeFloatBox(leftX, topY, rightX, bottomY)),
                        new FakeStaticProviderAtTime<>(null),
                        new FakeStaticProviderAtTime<>(null),
                        null),
                0L
        ));
    }

    @Test
    void testRenderOutdatedTimestamp() {
        FakeSprite sprite = new FakeSprite();
        sprite.Image = new FakeImage("imageId");
        List<ColorShift> colorShifts = new ArrayList<>();
        float leftX = 0.11f;
        float topY = 0.22f;
        float rightX = 0.33f;
        float bottomY = 0.44f;
        FakeSpriteRenderable spriteRenderable = new FakeSpriteRenderable(sprite, colorShifts,
                new FakeStaticProviderAtTime<>(new FakeFloatBox(leftX, topY, rightX, bottomY)),
                new FakeStaticProviderAtTime<>(null),
                new FakeStaticProviderAtTime<>(null),
                new FakeEntityUuid());
        long timestamp = 100L;
        _spriteRenderer.setShader(new FakeShader());
        _spriteRenderer.setMesh(new FakeMesh());
        _spriteRenderer.render(spriteRenderable, timestamp);

        assertThrows(IllegalArgumentException.class,
                () -> _spriteRenderer.render(spriteRenderable, timestamp - 1L));
    }

    @Test
    void testRenderPassesTimestampToColorShiftStackAggregator() {
        FakeSprite sprite = new FakeSprite();
        sprite.Image = new FakeImage("imageId");
        List<ColorShift> colorShifts = new ArrayList<>();
        float leftX = 0.11f;
        float topY = 0.22f;
        float rightX = 0.33f;
        float bottomY = 0.44f;
        FakeSpriteRenderable spriteRenderable = new FakeSpriteRenderable(sprite, colorShifts,
                new FakeStaticProviderAtTime<>(new FakeFloatBox(leftX, topY, rightX, bottomY)),
                new FakeStaticProviderAtTime<>(null),
                new FakeStaticProviderAtTime<>(null),
                new FakeEntityUuid());
        long timestamp = 100L;
        _spriteRenderer.setShader(new FakeShader());
        _spriteRenderer.setMesh(new FakeMesh());
        _spriteRenderer.render(spriteRenderable, timestamp);

        assertEquals(timestamp, (long)COLOR_SHIFT_STACK_AGGREGATOR.Input);
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(Renderer.class.getCanonicalName() + "<" +
                SpriteRenderable.class.getCanonicalName() + ">",
                _spriteRenderer.getInterfaceName());
    }
}
