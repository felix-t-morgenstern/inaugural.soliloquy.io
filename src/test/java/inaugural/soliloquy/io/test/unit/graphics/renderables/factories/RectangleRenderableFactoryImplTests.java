package inaugural.soliloquy.io.test.unit.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.RectangleRenderableImpl;
import inaugural.soliloquy.io.graphics.renderables.factories.RectangleRenderableFactoryImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeAction;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeProviderAtTime;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeStaticProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.RectangleRenderable;
import soliloquy.specs.io.graphics.renderables.RenderableWithMouseEvents.MouseEventInputs;
import soliloquy.specs.io.graphics.renderables.factories.RectangleRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderableStack;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;

import java.awt.*;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomInt;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class RectangleRenderableFactoryImplTests {
    private final ProviderAtTime<Color> TOP_LEFT_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> TOP_RIGHT_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> BOTTOM_RIGHT_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> BOTTOM_LEFT_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Integer> BACKGROUND_TEXTURE_ID_PROVIDER =
            new FakeProviderAtTime<>();
    private final float BACKGROUND_TEXTURE_TILE_WIDTH = 0.123f;
    private final float BACKGROUND_TEXTURE_TILE_HEIGHT = 0.456f;
    private final Map<Integer, Action<MouseEventInputs>> ON_PRESS_ACTIONS = mapOf();
    private final FakeAction<MouseEventInputs> ON_MOUSE_OVER = new FakeAction<>();
    private final FakeAction<MouseEventInputs> ON_MOUSE_LEAVE = new FakeAction<>();
    private final FakeStaticProvider<FloatBox> RENDERING_AREA_PROVIDER =
            new FakeStaticProvider<>(null);
    private final int Z = randomInt();

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private RenderableStack mockContainingStack;
    @Mock private RenderingBoundaries mockRenderingBoundaries;

    private RectangleRenderableFactory rectangleRenderableFactory;

    @BeforeEach
    public void setUp() {
        mockContainingStack = mock(RenderableStack.class);
        mockRenderingBoundaries = mock(RenderingBoundaries.class);

        rectangleRenderableFactory = new RectangleRenderableFactoryImpl(mockRenderingBoundaries);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new RectangleRenderableFactoryImpl(null));
    }

    @Test
    public void testMake() {
        RectangleRenderable rectangleRenderable = rectangleRenderableFactory.make(
                TOP_LEFT_COLOR_PROVIDER, TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack);

        assertNotNull(rectangleRenderable);
        assertInstanceOf(RectangleRenderableImpl.class, rectangleRenderable);
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderableFactory.make(null,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderableFactory.make(TOP_LEFT_COLOR_PROVIDER,
                        null, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderableFactory.make(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, null,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderableFactory.make(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        null, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderableFactory.make(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, null,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderableFactory.make(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        0f, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderableFactory.make(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, 0f,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderableFactory.make(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        null, Z, UUID, mockContainingStack));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderableFactory.make(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        RENDERING_AREA_PROVIDER, Z, null, mockContainingStack));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderableFactory.make(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        RENDERING_AREA_PROVIDER, Z, UUID, null));
    }
}
