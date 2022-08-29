package inaugural.soliloquy.graphics.test.unit.renderables;

import inaugural.soliloquy.graphics.renderables.TriangleRenderableImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeAction;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeProviderAtTime;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeStaticProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.TriangleRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static inaugural.soliloquy.tools.random.Random.randomFloatInRange;
import static inaugural.soliloquy.tools.random.Random.randomLong;
import static org.junit.jupiter.api.Assertions.*;

class TriangleRenderableImplTests {
    private final ProviderAtTime<Color> VERTEX_1_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> VERTEX_2_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> VERTEX_3_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Integer> BACKGROUND_TEXTURE_ID_PROVIDER =
            new FakeProviderAtTime<>();
    private final float BACKGROUND_TEXTURE_TILE_WIDTH = 0.123f;
    private final float BACKGROUND_TEXTURE_TILE_HEIGHT = 0.456f;
    private final HashMap<Integer, Action<Long>> ON_PRESS_ACTIONS = new HashMap<>();
    private final FakeAction<Long> ON_PRESS_ACTION = new FakeAction<>();
    private final FakeAction<Long> ON_MOUSE_OVER = new FakeAction<>();
    private final FakeAction<Long> ON_MOUSE_LEAVE = new FakeAction<>();
    private final FakeProviderAtTime<Vertex> VERTEX_1_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Vertex> VERTEX_2_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Vertex> VERTEX_3_PROVIDER = new FakeProviderAtTime<>();
    private final int Z = 123;
    private final Consumer<Renderable>
            TRIANGLE_RENDERABLE_UPDATE_Z_INDEX_IN_CONTAINER =
            renderable -> _triangleRenderableUpdateZIndexInContainerInput =
                    renderable;
    private final Consumer<Renderable>
            TRIANGLE_RENDERABLE_REMOVE_FROM_CONTAINER =
            renderable -> _triangleRenderableRemoveFromContainerInput = renderable;

    private static Renderable _triangleRenderableRemoveFromContainerInput;
    private static Renderable _triangleRenderableUpdateZIndexInContainerInput;

    private final java.util.UUID UUID = java.util.UUID.randomUUID();

    private TriangleRenderable _triangleRenderable;
    private TriangleRenderable _triangleRenderableNotSupportingMouseEvents;

    @BeforeEach
    void setUp() {
        _triangleRenderable = new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                Z, UUID,
                TRIANGLE_RENDERABLE_UPDATE_Z_INDEX_IN_CONTAINER,
                TRIANGLE_RENDERABLE_REMOVE_FROM_CONTAINER
        );
        _triangleRenderable.setCapturesMouseEvents(true);

        _triangleRenderableNotSupportingMouseEvents = new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                Z, UUID,
                TRIANGLE_RENDERABLE_UPDATE_Z_INDEX_IN_CONTAINER,
                TRIANGLE_RENDERABLE_REMOVE_FROM_CONTAINER
        );
        _triangleRenderableNotSupportingMouseEvents.setCapturesMouseEvents(false);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                null, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                Z, UUID,
                TRIANGLE_RENDERABLE_UPDATE_Z_INDEX_IN_CONTAINER,
                TRIANGLE_RENDERABLE_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, null,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                Z, UUID,
                TRIANGLE_RENDERABLE_UPDATE_Z_INDEX_IN_CONTAINER,
                TRIANGLE_RENDERABLE_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                null, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                Z, UUID,
                TRIANGLE_RENDERABLE_UPDATE_Z_INDEX_IN_CONTAINER,
                TRIANGLE_RENDERABLE_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, null,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                Z, UUID,
                TRIANGLE_RENDERABLE_UPDATE_Z_INDEX_IN_CONTAINER,
                TRIANGLE_RENDERABLE_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                null, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                Z, UUID,
                TRIANGLE_RENDERABLE_UPDATE_Z_INDEX_IN_CONTAINER,
                TRIANGLE_RENDERABLE_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, null,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                Z, UUID,
                TRIANGLE_RENDERABLE_UPDATE_Z_INDEX_IN_CONTAINER,
                TRIANGLE_RENDERABLE_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                null,
                BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                Z, UUID,
                TRIANGLE_RENDERABLE_UPDATE_Z_INDEX_IN_CONTAINER,
                TRIANGLE_RENDERABLE_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                0f, BACKGROUND_TEXTURE_TILE_HEIGHT,
                ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                Z, UUID,
                TRIANGLE_RENDERABLE_UPDATE_Z_INDEX_IN_CONTAINER,
                TRIANGLE_RENDERABLE_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                BACKGROUND_TEXTURE_TILE_WIDTH, 0f,
                ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                Z, UUID,
                TRIANGLE_RENDERABLE_UPDATE_Z_INDEX_IN_CONTAINER,
                TRIANGLE_RENDERABLE_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                Z, null,
                TRIANGLE_RENDERABLE_UPDATE_Z_INDEX_IN_CONTAINER,
                TRIANGLE_RENDERABLE_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                Z, UUID,
                null,
                TRIANGLE_RENDERABLE_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                Z, UUID,
                TRIANGLE_RENDERABLE_UPDATE_Z_INDEX_IN_CONTAINER,
                null
        ));
    }

    @Test
    void testSetAndGetVertexProviders() {
        FakeStaticProvider<Vertex> provider1 =
                new FakeStaticProvider<>(Vertex.of(0f, 0f));
        FakeStaticProvider<Vertex> provider2 =
                new FakeStaticProvider<>(Vertex.of(0f, 0f));
        FakeStaticProvider<Vertex> provider3 =
                new FakeStaticProvider<>(Vertex.of(0f, 0f));

        _triangleRenderable.setVertex1Provider(provider1);
        _triangleRenderable.setVertex2Provider(provider2);
        _triangleRenderable.setVertex3Provider(provider3);

        assertSame(provider1, _triangleRenderable.getVertex1Provider());
        assertSame(provider2, _triangleRenderable.getVertex2Provider());
        assertSame(provider3, _triangleRenderable.getVertex3Provider());
    }

    @Test
    void testSetAndGetVertexColorProviders() {
        FakeStaticProvider<Color> provider1 = new FakeStaticProvider<>(Color.BLACK);
        FakeStaticProvider<Color> provider2 = new FakeStaticProvider<>(Color.BLACK);
        FakeStaticProvider<Color> provider3 = new FakeStaticProvider<>(Color.BLACK);

        _triangleRenderable.setVertex1ColorProvider(provider1);
        _triangleRenderable.setVertex2ColorProvider(provider2);
        _triangleRenderable.setVertex3ColorProvider(provider3);

        assertSame(provider1, _triangleRenderable.getVertex1ColorProvider());
        assertSame(provider2, _triangleRenderable.getVertex2ColorProvider());
        assertSame(provider3, _triangleRenderable.getVertex3ColorProvider());
    }

    @Test
    void testSetAndGetBackgroundTextureIdProvider() {
        assertSame(BACKGROUND_TEXTURE_ID_PROVIDER,
                _triangleRenderable.getBackgroundTextureIdProvider());

        FakeProviderAtTime<Integer> newProvider = new FakeProviderAtTime<>();

        _triangleRenderable.setBackgroundTextureIdProvider(newProvider);

        assertSame(newProvider, _triangleRenderable.getBackgroundTextureIdProvider());
    }

    @Test
    void testSetAndGetBackgroundTextureTileWidth() {
        assertEquals(BACKGROUND_TEXTURE_TILE_WIDTH,
                _triangleRenderable.getBackgroundTextureTileWidth());

        float newWidth = 0.1312f;

        _triangleRenderable.setBackgroundTextureTileWidth(newWidth);

        assertEquals(newWidth, _triangleRenderable.getBackgroundTextureTileWidth());
    }

    @Test
    void testSetAndGetBackgroundTextureTileHeight() {
        assertEquals(BACKGROUND_TEXTURE_TILE_HEIGHT,
                _triangleRenderable.getBackgroundTextureTileHeight());

        float newHeight = 0.1312f;

        _triangleRenderable.setBackgroundTextureTileHeight(newHeight);

        assertEquals(newHeight, _triangleRenderable.getBackgroundTextureTileHeight());
    }

    @Test
    void testSetVertexProvidersWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.setVertex1Provider(null));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.setVertex2Provider(null));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.setVertex3Provider(null));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.setVertex1ColorProvider(null));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.setVertex2ColorProvider(null));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.setVertex3ColorProvider(null));
    }

    @Test
    void testGetAndSetCapturesMouseEvents() {
        assertTrue(_triangleRenderable.getCapturesMouseEvents());

        _triangleRenderable.setCapturesMouseEvents(false);

        assertFalse(_triangleRenderable.getCapturesMouseEvents());
    }

    @Test
    void testPressAndSetOnPress() {
        assertThrows(UnsupportedOperationException.class, () ->
                _triangleRenderableNotSupportingMouseEvents.press(2, 0L));
        assertThrows(UnsupportedOperationException.class, () ->
                _triangleRenderableNotSupportingMouseEvents.setOnPress(2, new FakeAction<>()));

        _triangleRenderable.setOnPress(2, ON_PRESS_ACTION);

        long timestamp = 456456L;
        _triangleRenderable.press(2, timestamp);
        assertEquals(1, ON_PRESS_ACTION.NumberOfTimesCalled);
        assertEquals(1, ON_PRESS_ACTION.Inputs.size());
        assertEquals(timestamp, (long) ON_PRESS_ACTION.Inputs.get(0));

        FakeAction<Long> newOnPress = new FakeAction<>();
        _triangleRenderable.setOnPress(2, newOnPress);

        _triangleRenderable.press(2, timestamp + 1);

        assertEquals(1, newOnPress.NumberOfTimesCalled);
        assertEquals(1, newOnPress.Inputs.size());
        assertEquals(timestamp + 1, (long) newOnPress.Inputs.get(0));

        _triangleRenderable.press(0, timestamp + 2);

        assertEquals(1, newOnPress.NumberOfTimesCalled);
        assertEquals(1, newOnPress.Inputs.size());
        assertEquals(timestamp + 1, (long) newOnPress.Inputs.get(0));
    }

    @Test
    void testPressActionIds() {
        String id1 = "id1";
        String id2 = "id2";
        String id3 = "id3";

        _triangleRenderable.setOnPress(0, new FakeAction<>(id1));
        _triangleRenderable.setOnPress(2, new FakeAction<>(id2));
        _triangleRenderable.setOnPress(7, new FakeAction<>(id3));
        _triangleRenderable.setOnPress(2, null);

        Map<Integer, String> pressActionIds = _triangleRenderable.pressActionIds();

        assertNotNull(pressActionIds);
        assertEquals(2, pressActionIds.size());
        assertEquals(id1, pressActionIds.get(0));
        assertEquals(id3, pressActionIds.get(7));
    }

    @Test
    void testReleaseAndSetOnRelease() {
        assertThrows(UnsupportedOperationException.class, () ->
                _triangleRenderableNotSupportingMouseEvents.release(2, 0L));
        assertThrows(UnsupportedOperationException.class, () ->
                _triangleRenderableNotSupportingMouseEvents.setOnRelease(2, new FakeAction<>()));

        long timestamp = 456456L;
        _triangleRenderable.release(2, timestamp);

        FakeAction<Long> newOnRelease = new FakeAction<>();
        _triangleRenderable.setOnRelease(2, newOnRelease);

        _triangleRenderable.release(2, timestamp + 1);
        assertEquals(1, newOnRelease.NumberOfTimesCalled);
        assertEquals(1, newOnRelease.Inputs.size());
        assertEquals(timestamp + 1, (long) newOnRelease.Inputs.get(0));
    }

    @Test
    void testReleaseActionIds() {
        String id1 = "id1";
        String id2 = "id2";
        String id3 = "id3";

        _triangleRenderable.setOnRelease(0, new FakeAction<>(id1));
        _triangleRenderable.setOnRelease(2, new FakeAction<>(id2));
        _triangleRenderable.setOnRelease(7, new FakeAction<>(id3));
        _triangleRenderable.setOnRelease(2, null);

        Map<Integer, String> releaseActionIds =
                _triangleRenderable.releaseActionIds();

        assertNotNull(releaseActionIds);
        assertEquals(2, releaseActionIds.size());
        assertEquals(id1, releaseActionIds.get(0));
        assertEquals(id3, releaseActionIds.get(7));
    }

    @Test
    void testPressOrReleaseMethodsWithInvalidButtons() {
        long timestamp = 456456L;

        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.setOnPress(-1, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.setOnRelease(-1, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.press(-1, timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.press(-1, timestamp + 1));

        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.setOnPress(8, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.setOnRelease(8, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.press(8, timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.press(8, timestamp + 3));
    }

    @Test
    void testMouseOverAndSetOnMouseOver() {
        assertThrows(UnsupportedOperationException.class, () ->
                _triangleRenderableNotSupportingMouseEvents.mouseOver(0L));
        assertThrows(UnsupportedOperationException.class, () ->
                _triangleRenderableNotSupportingMouseEvents.setOnMouseOver(ON_MOUSE_OVER));

        long timestamp = 456456L;
        _triangleRenderable.mouseOver(timestamp);
        assertEquals(1, ON_MOUSE_OVER.NumberOfTimesCalled);
        assertEquals(1, ON_MOUSE_OVER.Inputs.size());
        assertEquals(timestamp, (long) ON_MOUSE_OVER.Inputs.get(0));

        FakeAction<Long> newOnMouseOver = new FakeAction<>();
        _triangleRenderable.setOnMouseOver(newOnMouseOver);

        _triangleRenderable.mouseOver(timestamp + 1);
        assertEquals(1, newOnMouseOver.NumberOfTimesCalled);
        assertEquals(1, newOnMouseOver.Inputs.size());
        assertEquals(timestamp + 1, (long) newOnMouseOver.Inputs.get(0));
    }

    @Test
    void testMouseOverActionId() {
        String mouseOverActionId = "mouseOverActionId";

        assertThrows(UnsupportedOperationException.class, () ->
                _triangleRenderableNotSupportingMouseEvents.mouseOverActionId());

        _triangleRenderable.setOnMouseOver(null);

        assertNull(_triangleRenderable.mouseOverActionId());

        _triangleRenderable.setOnMouseOver(new FakeAction<>(mouseOverActionId));

        assertEquals(mouseOverActionId, _triangleRenderable.mouseOverActionId());
    }

    @Test
    void testMouseLeaveAndSetOnMouseLeave() {
        assertThrows(UnsupportedOperationException.class, () ->
                _triangleRenderableNotSupportingMouseEvents.mouseLeave(0L));
        assertThrows(UnsupportedOperationException.class, () ->
                _triangleRenderableNotSupportingMouseEvents.setOnMouseLeave(ON_MOUSE_LEAVE));

        long timestamp = 456456L;
        _triangleRenderable.mouseLeave(timestamp);
        assertEquals(1, ON_MOUSE_LEAVE.NumberOfTimesCalled);
        assertEquals(1, ON_MOUSE_LEAVE.Inputs.size());
        assertEquals(timestamp, (long) ON_MOUSE_LEAVE.Inputs.get(0));

        FakeAction<Long> newOnMouseLeave = new FakeAction<>();
        _triangleRenderable.setOnMouseLeave(newOnMouseLeave);

        _triangleRenderable.mouseLeave(timestamp + 1);
        assertEquals(1, newOnMouseLeave.NumberOfTimesCalled);
        assertEquals(1, newOnMouseLeave.Inputs.size());
        assertEquals(timestamp + 1, (long) newOnMouseLeave.Inputs.get(0));
    }

    @Test
    void testMouseLeaveActionId() {
        String mouseLeaveActionId = "mouseLeaveActionId";

        assertThrows(UnsupportedOperationException.class, () ->
                _triangleRenderableNotSupportingMouseEvents.mouseLeaveActionId());

        _triangleRenderable.setOnMouseLeave(null);

        assertNull(_triangleRenderable.mouseLeaveActionId());

        _triangleRenderable.setOnMouseLeave(new FakeAction<>(mouseLeaveActionId));

        assertEquals(mouseLeaveActionId, _triangleRenderable.mouseLeaveActionId());
    }

    @Test
    void testMouseEventCallsToOutdatedTimestamps() {
        long timestamp = 456456L;
        _triangleRenderable.setVertex1Provider(
                new FakeStaticProvider<>(
                        Vertex.of(randomFloatInRange(0f, 1f), randomFloatInRange(0f, 1f)))
        );
        _triangleRenderable.setVertex2Provider(
                new FakeStaticProvider<>(
                        Vertex.of(randomFloatInRange(0f, 1f), randomFloatInRange(0f, 1f)))
        );
        _triangleRenderable.setVertex3Provider(
                new FakeStaticProvider<>(
                        Vertex.of(randomFloatInRange(0f, 1f), randomFloatInRange(0f, 1f)))
        );

        _triangleRenderable.press(0, timestamp);
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.press(0, timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.release(0, timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.mouseOver(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.mouseLeave(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.capturesMouseEventAtPoint(0f, 0f, timestamp - 1));

        _triangleRenderable.release(0, timestamp + 1);
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.press(0, timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.release(0, timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.mouseOver(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.mouseLeave(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.capturesMouseEventAtPoint(0f, 0f, timestamp));

        _triangleRenderable.mouseOver(timestamp + 2);
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.press(0, timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.release(0, timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.mouseOver(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.mouseLeave(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.capturesMouseEventAtPoint(0f, 0f, timestamp + 1));

        _triangleRenderable.mouseLeave(timestamp + 3);
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.press(0, timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.release(0, timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.mouseOver(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.mouseLeave(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.capturesMouseEventAtPoint(0f, 0f, timestamp + 2));

        _triangleRenderable.capturesMouseEventAtPoint(0f, 0f, timestamp + 4);
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.press(0, timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.release(0, timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.mouseOver(timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.mouseLeave(timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                _triangleRenderable.capturesMouseEventAtPoint(0f, 0f, timestamp + 3));
    }

    @Test
    void testGetAndSetZ() {
        assertSame(Z, _triangleRenderable.getZ());

        int newZ = 456;

        _triangleRenderable.setZ(newZ);

        assertEquals(newZ, _triangleRenderable.getZ());

        assertSame(_triangleRenderable,
                _triangleRenderableUpdateZIndexInContainerInput);
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(TriangleRenderable.class.getCanonicalName(),
                _triangleRenderable.getInterfaceName());
    }

    @Test
    void testCapturesMouseEventsAtPoint() {
        long timestamp = randomLong();

        _triangleRenderable.setVertex1Provider(
                new FakeStaticProvider<>(Vertex.of(0.5f, 0f))
        );
        _triangleRenderable.setVertex2Provider(
                new FakeStaticProvider<>(Vertex.of(0f, .5f))
        );
        _triangleRenderable.setVertex3Provider(
                new FakeStaticProvider<>(Vertex.of(1f, .5f))
        );

        assertFalse(_triangleRenderable.capturesMouseEventAtPoint(0f, 0f, timestamp));
        assertFalse(_triangleRenderable.capturesMouseEventAtPoint(1f, 0f, timestamp));
        assertFalse(_triangleRenderable.capturesMouseEventAtPoint(0.55f, 0f, timestamp));
        assertTrue(_triangleRenderable.capturesMouseEventAtPoint(0.55f, 0.4f, timestamp));
        assertTrue(_triangleRenderable.capturesMouseEventAtPoint(0.26f, 0.25f, timestamp));
        assertFalse(_triangleRenderable.capturesMouseEventAtPoint(0.24f, 0.25f, timestamp));
        assertTrue(_triangleRenderable.capturesMouseEventAtPoint(0.74f, 0.25f, timestamp));
        assertFalse(_triangleRenderable.capturesMouseEventAtPoint(0.76f, 0.25f, timestamp));
    }

    @Test
    void testDelete() {
        _triangleRenderable.delete();
        assertSame(_triangleRenderable,
                _triangleRenderableRemoveFromContainerInput);
    }

    @Test
    void testUuid() {
        assertSame(UUID, _triangleRenderable.uuid());
    }
}
