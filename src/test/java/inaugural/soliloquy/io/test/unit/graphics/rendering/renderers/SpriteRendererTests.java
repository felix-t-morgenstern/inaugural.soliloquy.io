package inaugural.soliloquy.io.test.unit.graphics.rendering.renderers;

import inaugural.soliloquy.io.graphics.rendering.renderers.SpriteRenderer;
import inaugural.soliloquy.io.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.collections.Collections;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.graphics.assets.Sprite;
import soliloquy.specs.io.graphics.renderables.SpriteRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.rendering.Mesh;
import soliloquy.specs.io.graphics.rendering.Shader;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.awt.*;
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
public class SpriteRendererTests {
    private final FakeRenderingBoundaries RENDERING_BOUNDARIES = new FakeRenderingBoundaries();
    private final FakeWindowResolutionManager WINDOW_RESOLUTION_MANAGER =
            new FakeWindowResolutionManager();
    private final FakeColorShiftStackAggregator COLOR_SHIFT_STACK_AGGREGATOR =
            new FakeColorShiftStackAggregator();
    private final long MOST_RECENT_TIMESTAMP = randomLong();

    @Mock private TimestampValidator mockTimestampValidator;

    private Renderer<SpriteRenderable> renderer;

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
        renderer = new SpriteRenderer(RENDERING_BOUNDARIES,
                WINDOW_RESOLUTION_MANAGER, COLOR_SHIFT_STACK_AGGREGATOR, mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderer(null, WINDOW_RESOLUTION_MANAGER,
                        COLOR_SHIFT_STACK_AGGREGATOR, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderer(RENDERING_BOUNDARIES, null, COLOR_SHIFT_STACK_AGGREGATOR,
                        mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderer(RENDERING_BOUNDARIES, WINDOW_RESOLUTION_MANAGER, null,
                        mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderer(RENDERING_BOUNDARIES, WINDOW_RESOLUTION_MANAGER,
                        COLOR_SHIFT_STACK_AGGREGATOR, null));
    }

    @Test
    public void testSetMeshWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> renderer.setMesh(null));
    }

    @Test
    public void testSetShaderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> renderer.setShader(null));
    }

    @Test
    public void testRenderWithInvalidArgs() {
        Sprite sprite = new FakeSprite();
        var colorShiftProviders = Collections.<ColorShift>listOf();
        var leftX = 0.11f;
        var topY = 0.22f;
        var rightX = 0.33f;
        var bottomY = 0.44f;
        Float borderThickness = 0.01f;
        Color borderColor = Color.RED;

        assertThrows(IllegalArgumentException.class, () -> renderer.render(null, 0L));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeSpriteRenderable(null, colorShiftProviders,
                        new FakeStaticProvider<>(
                                floatBoxOf(leftX, topY, rightX, bottomY)),
                        new FakeStaticProvider<>(null),
                        new FakeStaticProvider<>(null),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeSpriteRenderable(sprite, null,
                        new FakeStaticProvider<>(
                                floatBoxOf(leftX, topY, rightX, bottomY)),
                        new FakeStaticProvider<>(null),
                        new FakeStaticProvider<>(null),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeSpriteRenderable(sprite, colorShiftProviders,
                        null,
                        new FakeStaticProvider<>(null),
                        new FakeStaticProvider<>(null),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeSpriteRenderable(sprite, colorShiftProviders,
                        new FakeStaticProvider<>(
                                floatBoxOf(leftX, topY, rightX, bottomY)),
                        null,
                        new FakeStaticProvider<>(null),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeSpriteRenderable(sprite, colorShiftProviders,
                        new FakeStaticProvider<>(
                                floatBoxOf(leftX, topY, rightX, bottomY)),
                        new FakeStaticProvider<>(null),
                        null,
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeSpriteRenderable(sprite, colorShiftProviders,
                        new FakeStaticProvider<>(
                                floatBoxOf(leftX, topY, leftX, bottomY)),
                        new FakeStaticProvider<>(null),
                        new FakeStaticProvider<>(null),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeSpriteRenderable(sprite, null,
                        new FakeStaticProvider<>(
                                floatBoxOf(leftX, topY, rightX, topY)),
                        new FakeStaticProvider<>(null),
                        new FakeStaticProvider<>(null),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeSpriteRenderable(sprite, colorShiftProviders,
                        new FakeStaticProvider<>(
                                floatBoxOf(leftX, topY, rightX, bottomY)),
                        new FakeStaticProvider<>(borderThickness),
                        new FakeStaticProvider<>(null),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeSpriteRenderable(sprite, colorShiftProviders,
                        new FakeStaticProvider<>(
                                floatBoxOf(leftX, topY, rightX, bottomY)),
                        new FakeStaticProvider<>(-0.0001f),
                        new FakeStaticProvider<>(borderColor),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
                new FakeSpriteRenderable(sprite, colorShiftProviders,
                        new FakeStaticProvider<>(
                                floatBoxOf(leftX, topY, rightX, bottomY)),
                        new FakeStaticProvider<>(1.0001f),
                        new FakeStaticProvider<>(borderColor),
                        UUID.randomUUID()),
                0L
        ));

        assertThrows(IllegalArgumentException.class, () -> renderer.render(
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
    public void testRenderPassesTimestampToColorShiftStackAggregator() {
        var sprite = new FakeSprite();
        sprite.Image = new FakeImage("imageId");
        var colorShiftProviders = Collections.<ColorShift>listOf();
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
        renderer.setShader(mock(Shader.class));
        renderer.setMesh(mock(Mesh.class));
        renderer.render(spriteRenderable, MOST_RECENT_TIMESTAMP + 123);

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
        renderer.setMesh(mock(Mesh.class));
        renderer.setShader(mock(Shader.class));
        var sprite = new FakeSprite();
        sprite.Image = new FakeImage("imageId");
        var colorShiftProviders = Collections.<ColorShift>listOf();
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
        var timestamp = randomLong();

        renderer.render(spriteRenderable, timestamp);

        verify(mockTimestampValidator, once()).validateTimestamp(
                renderer.getClass().getCanonicalName(), timestamp);
    }
}
