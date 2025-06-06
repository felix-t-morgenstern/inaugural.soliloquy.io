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
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.List;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;

public class SpriteRendererTests {
    private final FakeRenderingBoundaries RENDERING_BOUNDARIES = new FakeRenderingBoundaries();
    private final FakeWindowResolutionManager WINDOW_RESOLUTION_MANAGER =
            new FakeWindowResolutionManager();
    private final FakeColorShiftStackAggregator COLOR_SHIFT_STACK_AGGREGATOR =
            new FakeColorShiftStackAggregator();
    private final long MOST_RECENT_TIMESTAMP = 123123L;

    private Renderer<SpriteRenderable> spriteRenderer;

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
        spriteRenderer = new SpriteRenderer(RENDERING_BOUNDARIES,
                WINDOW_RESOLUTION_MANAGER, COLOR_SHIFT_STACK_AGGREGATOR, MOST_RECENT_TIMESTAMP);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new SpriteRenderer(null,
                        WINDOW_RESOLUTION_MANAGER, COLOR_SHIFT_STACK_AGGREGATOR,
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                new SpriteRenderer(RENDERING_BOUNDARIES,
                        null, COLOR_SHIFT_STACK_AGGREGATOR,
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                new SpriteRenderer(RENDERING_BOUNDARIES,
                        WINDOW_RESOLUTION_MANAGER, null,
                        MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testSetMeshWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> spriteRenderer.setMesh(null));
    }

    @Test
    public void testSetShaderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> spriteRenderer.setShader(null));
    }

    @Test
    public void testRenderWithInvalidArgs() {
        Sprite sprite = new FakeSprite();
        List<ProviderAtTime<ColorShift>> colorShiftProviders = listOf();
        var leftX = 0.11f;
        var topY = 0.22f;
        var rightX = 0.33f;
        var bottomY = 0.44f;
        Float borderThickness = 0.01f;
        Color borderColor = Color.RED;

        assertThrows(IllegalArgumentException.class, () -> spriteRenderer.render(null, 0L));

        assertThrows(IllegalArgumentException.class, () -> spriteRenderer.render(
                new FakeSpriteRenderable(null, colorShiftProviders,
                        new FakeStaticProvider<>(
                                floatBoxOf(leftX, topY, rightX, bottomY)),
                        new FakeStaticProvider<>(null),
                        new FakeStaticProvider<>(null),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> spriteRenderer.render(
                new FakeSpriteRenderable(sprite, null,
                        new FakeStaticProvider<>(
                                floatBoxOf(leftX, topY, rightX, bottomY)),
                        new FakeStaticProvider<>(null),
                        new FakeStaticProvider<>(null),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> spriteRenderer.render(
                new FakeSpriteRenderable(sprite, colorShiftProviders,
                        null,
                        new FakeStaticProvider<>(null),
                        new FakeStaticProvider<>(null),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> spriteRenderer.render(
                new FakeSpriteRenderable(sprite, colorShiftProviders,
                        new FakeStaticProvider<>(
                                floatBoxOf(leftX, topY, rightX, bottomY)),
                        null,
                        new FakeStaticProvider<>(null),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> spriteRenderer.render(
                new FakeSpriteRenderable(sprite, colorShiftProviders,
                        new FakeStaticProvider<>(
                                floatBoxOf(leftX, topY, rightX, bottomY)),
                        new FakeStaticProvider<>(null),
                        null,
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> spriteRenderer.render(
                new FakeSpriteRenderable(sprite, colorShiftProviders,
                        new FakeStaticProvider<>(
                                floatBoxOf(leftX, topY, leftX, bottomY)),
                        new FakeStaticProvider<>(null),
                        new FakeStaticProvider<>(null),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> spriteRenderer.render(
                new FakeSpriteRenderable(sprite, null,
                        new FakeStaticProvider<>(
                                floatBoxOf(leftX, topY, rightX, topY)),
                        new FakeStaticProvider<>(null),
                        new FakeStaticProvider<>(null),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> spriteRenderer.render(
                new FakeSpriteRenderable(sprite, colorShiftProviders,
                        new FakeStaticProvider<>(
                                floatBoxOf(leftX, topY, rightX, bottomY)),
                        new FakeStaticProvider<>(borderThickness),
                        new FakeStaticProvider<>(null),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> spriteRenderer.render(
                new FakeSpriteRenderable(sprite, colorShiftProviders,
                        new FakeStaticProvider<>(
                                floatBoxOf(leftX, topY, rightX, bottomY)),
                        new FakeStaticProvider<>(-0.0001f),
                        new FakeStaticProvider<>(borderColor),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> spriteRenderer.render(
                new FakeSpriteRenderable(sprite, colorShiftProviders,
                        new FakeStaticProvider<>(
                                floatBoxOf(leftX, topY, rightX, bottomY)),
                        new FakeStaticProvider<>(1.0001f),
                        new FakeStaticProvider<>(borderColor),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> spriteRenderer.render(
                new FakeSpriteRenderable(sprite, null,
                        new FakeStaticProvider<>(
                                floatBoxOf(leftX, topY, rightX, bottomY)),
                        new FakeStaticProvider<>(null),
                        new FakeStaticProvider<>(null),
                        null),
                0L
        ));
    }

    @Test
    public void testRenderOutdatedTimestamp() {
        var sprite = new FakeSprite();
        sprite.Image = new FakeImage("imageId");
        List<ProviderAtTime<ColorShift>> colorShiftProviders = listOf();
        var leftX = 0.11f;
        var topY = 0.22f;
        var rightX = 0.33f;
        var bottomY = 0.44f;
        FakeSpriteRenderable spriteRenderable = new FakeSpriteRenderable(sprite,
                colorShiftProviders,
                new FakeStaticProvider<>(floatBoxOf(leftX, topY, rightX, bottomY)),
                new FakeStaticProvider<>(null),
                new FakeStaticProvider<>(null),
                UUID.randomUUID());
        spriteRenderer.setShader(new FakeShader());
        spriteRenderer.setMesh(new FakeMesh());

        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderer.render(spriteRenderable, MOST_RECENT_TIMESTAMP - 1L));
    }

    @Test
    public void testRenderPassesTimestampToColorShiftStackAggregator() {
        var sprite = new FakeSprite();
        sprite.Image = new FakeImage("imageId");
        List<ProviderAtTime<ColorShift>> colorShiftProviders = listOf();
        var leftX = 0.11f;
        var topY = 0.22f;
        var rightX = 0.33f;
        var bottomY = 0.44f;
        FakeSpriteRenderable spriteRenderable = new FakeSpriteRenderable(sprite,
                colorShiftProviders,
                new FakeStaticProvider<>(floatBoxOf(leftX, topY, rightX, bottomY)),
                new FakeStaticProvider<>(null),
                new FakeStaticProvider<>(null),
                UUID.randomUUID());
        spriteRenderer.setShader(new FakeShader());
        spriteRenderer.setMesh(new FakeMesh());
        spriteRenderer.render(spriteRenderable, MOST_RECENT_TIMESTAMP + 123);

        assertEquals(MOST_RECENT_TIMESTAMP + 123, (long) COLOR_SHIFT_STACK_AGGREGATOR.Input);
    }

    @Test
    public void testMostRecentTimestamp() {
        assertEquals(MOST_RECENT_TIMESTAMP, (long) spriteRenderer.mostRecentTimestamp());
    }
}
