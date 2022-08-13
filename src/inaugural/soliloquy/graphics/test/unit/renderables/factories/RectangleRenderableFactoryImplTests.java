package inaugural.soliloquy.graphics.test.unit.renderables.factories;

import inaugural.soliloquy.graphics.renderables.RectangleRenderableImpl;
import inaugural.soliloquy.graphics.renderables.factories.RectangleRenderableFactoryImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeAction;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeProviderAtTime;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeStaticProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.graphics.renderables.RectangleRenderable;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.factories.RectangleRenderableFactory;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class RectangleRenderableFactoryImplTests {
    private final ProviderAtTime<Color> TOP_LEFT_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> TOP_RIGHT_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> BOTTOM_RIGHT_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> BOTTOM_LEFT_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Integer> BACKGROUND_TEXTURE_ID_PROVIDER =
            new FakeProviderAtTime<>();
    private final float BACKGROUND_TEXTURE_TILE_WIDTH = 0.123f;
    private final float BACKGROUND_TEXTURE_TILE_HEIGHT = 0.456f;
    private final HashMap<Integer, Action<Long>> ON_PRESS_ACTIONS = new HashMap<>();
    private final FakeAction<Long> ON_MOUSE_OVER = new FakeAction<>();
    private final FakeAction<Long> ON_MOUSE_LEAVE = new FakeAction<>();
    private final FakeStaticProvider<FloatBox> RENDERING_AREA_PROVIDER =
            new FakeStaticProvider<>(null);
    private final int Z = 123;
    private final Consumer<Renderable> RECTANGLE_RENDERABLE_UPDATE_Z_INDEX_IN_CONTAINER =
            renderable -> {};
    private final Consumer<Renderable> RECTANGLE_RENDERABLE_REMOVE_FROM_CONTAINER =
            renderable -> {};

    private final UUID UUID = java.util.UUID.randomUUID();

    private RectangleRenderableFactory _rectangleRenderableFactory;

    @BeforeEach
    void setUp() {
        _rectangleRenderableFactory = new RectangleRenderableFactoryImpl();
    }

    @Test
    void testMake() {
        RectangleRenderable rectangleRenderable = _rectangleRenderableFactory.make(
                TOP_LEFT_COLOR_PROVIDER, TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                RENDERING_AREA_PROVIDER, Z, UUID,
                RECTANGLE_RENDERABLE_UPDATE_Z_INDEX_IN_CONTAINER,
                RECTANGLE_RENDERABLE_REMOVE_FROM_CONTAINER);

        assertNotNull(rectangleRenderable);
        assertTrue(rectangleRenderable instanceof RectangleRenderableImpl);
    }

    @Test
    void testMakeWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderableFactory.make(null,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        RENDERING_AREA_PROVIDER, Z, UUID,
                        RECTANGLE_RENDERABLE_UPDATE_Z_INDEX_IN_CONTAINER,
                        RECTANGLE_RENDERABLE_REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderableFactory.make(TOP_LEFT_COLOR_PROVIDER,
                        null, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        RENDERING_AREA_PROVIDER, Z, UUID,
                        RECTANGLE_RENDERABLE_UPDATE_Z_INDEX_IN_CONTAINER,
                        RECTANGLE_RENDERABLE_REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderableFactory.make(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, null,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        RENDERING_AREA_PROVIDER, Z, UUID,
                        RECTANGLE_RENDERABLE_UPDATE_Z_INDEX_IN_CONTAINER,
                        RECTANGLE_RENDERABLE_REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderableFactory.make(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        null, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        RENDERING_AREA_PROVIDER, Z, UUID,
                        RECTANGLE_RENDERABLE_UPDATE_Z_INDEX_IN_CONTAINER,
                        RECTANGLE_RENDERABLE_REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderableFactory.make(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, null,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        RENDERING_AREA_PROVIDER, Z, UUID,
                        RECTANGLE_RENDERABLE_UPDATE_Z_INDEX_IN_CONTAINER,
                        RECTANGLE_RENDERABLE_REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderableFactory.make(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        0f, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        RENDERING_AREA_PROVIDER, Z, UUID,
                        RECTANGLE_RENDERABLE_UPDATE_Z_INDEX_IN_CONTAINER,
                        RECTANGLE_RENDERABLE_REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderableFactory.make(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, 0f,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        RENDERING_AREA_PROVIDER, Z, UUID,
                        RECTANGLE_RENDERABLE_UPDATE_Z_INDEX_IN_CONTAINER,
                        RECTANGLE_RENDERABLE_REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderableFactory.make(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        null, Z, UUID,
                        RECTANGLE_RENDERABLE_UPDATE_Z_INDEX_IN_CONTAINER,
                        RECTANGLE_RENDERABLE_REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderableFactory.make(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        RENDERING_AREA_PROVIDER, Z, null,
                        RECTANGLE_RENDERABLE_UPDATE_Z_INDEX_IN_CONTAINER,
                        RECTANGLE_RENDERABLE_REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderableFactory.make(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        RENDERING_AREA_PROVIDER, Z, UUID,
                        null,
                        RECTANGLE_RENDERABLE_REMOVE_FROM_CONTAINER));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderableFactory.make(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        RENDERING_AREA_PROVIDER, Z, UUID,
                        RECTANGLE_RENDERABLE_UPDATE_Z_INDEX_IN_CONTAINER,
                        null));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(RectangleRenderableFactory.class.getCanonicalName(),
                _rectangleRenderableFactory.getInterfaceName());
    }
}
