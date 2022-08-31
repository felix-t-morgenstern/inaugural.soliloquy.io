package inaugural.soliloquy.graphics.test.unit.renderables;

import inaugural.soliloquy.graphics.renderables.RectangleRenderableImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeAction;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeFloatBox;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeProviderAtTime;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeStaticProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.graphics.renderables.RectangleRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.randomInt;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RectangleRenderableImplTests {
    private final ProviderAtTime<Color> TOP_LEFT_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> TOP_RIGHT_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> BOTTOM_RIGHT_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> BOTTOM_LEFT_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Integer> BACKGROUND_TEXTURE_ID_PROVIDER =
            new FakeProviderAtTime<>();
    private final float BACKGROUND_TEXTURE_TILE_WIDTH = 0.123f;
    private final float BACKGROUND_TEXTURE_TILE_HEIGHT = 0.456f;
    private final HashMap<Integer, Action<Long>> ON_PRESS_ACTIONS = new HashMap<>();
    private final FakeAction<Long> ON_PRESS_ACTION = new FakeAction<>();
    private final FakeAction<Long> ON_MOUSE_OVER = new FakeAction<>();
    private final FakeAction<Long> ON_MOUSE_LEAVE = new FakeAction<>();
    private final FakeStaticProvider<FloatBox> RENDERING_AREA_PROVIDER =
            new FakeStaticProvider<>(null);
    private final int Z = randomInt();

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private RenderableStack _mockContainingStack;

    private RectangleRenderable _rectangleRenderable;
    private RectangleRenderable _rectangleRenderableNotSupportingMouseEvents;

    @BeforeEach
    void setUp() {
        _mockContainingStack = mock(RenderableStack.class);

        _rectangleRenderable = new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER, BACKGROUND_TEXTURE_TILE_WIDTH,
                BACKGROUND_TEXTURE_TILE_HEIGHT, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, RENDERING_AREA_PROVIDER, Z, UUID, _mockContainingStack);
        _rectangleRenderable.setCapturesMouseEvents(true);

        _rectangleRenderableNotSupportingMouseEvents = new RectangleRenderableImpl(
                TOP_LEFT_COLOR_PROVIDER, TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT, ON_PRESS_ACTIONS,
                null, ON_MOUSE_OVER, ON_MOUSE_LEAVE, RENDERING_AREA_PROVIDER, Z, UUID,
                _mockContainingStack);
        _rectangleRenderableNotSupportingMouseEvents.setCapturesMouseEvents(false);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(null,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        RENDERING_AREA_PROVIDER, Z, UUID, _mockContainingStack));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        null, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        RENDERING_AREA_PROVIDER, Z, UUID, _mockContainingStack));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, null,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        RENDERING_AREA_PROVIDER, Z, UUID, _mockContainingStack));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        null, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        RENDERING_AREA_PROVIDER, Z, UUID, _mockContainingStack));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, null,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        RENDERING_AREA_PROVIDER, Z, UUID, _mockContainingStack));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        0f, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        RENDERING_AREA_PROVIDER, Z, UUID, _mockContainingStack));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, 0f,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        RENDERING_AREA_PROVIDER, Z, UUID, _mockContainingStack));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        null, Z, UUID, _mockContainingStack));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        RENDERING_AREA_PROVIDER, Z, null, _mockContainingStack));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        RENDERING_AREA_PROVIDER, Z, UUID, null));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(RectangleRenderable.class.getCanonicalName(),
                _rectangleRenderable.getInterfaceName());
    }

    @Test
    void testSetAndGetTopLeftColorProvider() {
        assertSame(TOP_LEFT_COLOR_PROVIDER, _rectangleRenderable.getTopLeftColorProvider());

        FakeProviderAtTime<Color> newProvider = new FakeProviderAtTime<>();

        _rectangleRenderable.setTopLeftColorProvider(newProvider);

        assertSame(newProvider, _rectangleRenderable.getTopLeftColorProvider());
    }

    @Test
    void testSetAndGetTopRightColorProvider() {
        assertSame(TOP_RIGHT_COLOR_PROVIDER, _rectangleRenderable.getTopRightColorProvider());

        FakeProviderAtTime<Color> newProvider = new FakeProviderAtTime<>();

        _rectangleRenderable.setTopRightColorProvider(newProvider);

        assertSame(newProvider, _rectangleRenderable.getTopRightColorProvider());
    }

    @Test
    void testSetAndGetBottomRightColorProvider() {
        assertSame(BOTTOM_RIGHT_COLOR_PROVIDER,
                _rectangleRenderable.getBottomRightColorProvider());

        FakeProviderAtTime<Color> newProvider = new FakeProviderAtTime<>();

        _rectangleRenderable.setBottomRightColorProvider(newProvider);

        assertSame(newProvider, _rectangleRenderable.getBottomRightColorProvider());
    }

    @Test
    void testSetAndGetBottomLeftColorProvider() {
        assertSame(BOTTOM_LEFT_COLOR_PROVIDER, _rectangleRenderable.getBottomLeftColorProvider());

        FakeProviderAtTime<Color> newProvider = new FakeProviderAtTime<>();

        _rectangleRenderable.setBottomLeftColorProvider(newProvider);

        assertSame(newProvider, _rectangleRenderable.getBottomLeftColorProvider());
    }

    @Test
    void testSetAndGetBackgroundTextureIdProvider() {
        assertSame(BACKGROUND_TEXTURE_ID_PROVIDER,
                _rectangleRenderable.getBackgroundTextureIdProvider());

        FakeProviderAtTime<Integer> newProvider = new FakeProviderAtTime<>();

        _rectangleRenderable.setBackgroundTextureIdProvider(newProvider);

        assertSame(newProvider, _rectangleRenderable.getBackgroundTextureIdProvider());
    }

    @Test
    void testSetAndGetBackgroundTextureTileWidth() {
        assertEquals(BACKGROUND_TEXTURE_TILE_WIDTH,
                _rectangleRenderable.getBackgroundTextureTileWidth());

        float newWidth = 0.1312f;

        _rectangleRenderable.setBackgroundTextureTileWidth(newWidth);

        assertEquals(newWidth, _rectangleRenderable.getBackgroundTextureTileWidth());
    }

    @Test
    void testSetAndGetBackgroundTextureTileHeight() {
        assertEquals(BACKGROUND_TEXTURE_TILE_HEIGHT,
                _rectangleRenderable.getBackgroundTextureTileHeight());

        float newHeight = 0.1312f;

        _rectangleRenderable.setBackgroundTextureTileHeight(newHeight);

        assertEquals(newHeight, _rectangleRenderable.getBackgroundTextureTileHeight());
    }

    @Test
    void testSetProvidersAndTileDimensionsWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.setTopLeftColorProvider(null));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.setTopRightColorProvider(null));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.setBottomRightColorProvider(null));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.setBottomLeftColorProvider(null));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.setBackgroundTextureIdProvider(null));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.setBackgroundTextureTileWidth(0f));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.setBackgroundTextureTileHeight(0f));
    }

    @Test
    void testGetAndSetCapturesMouseEvents() {
        assertTrue(_rectangleRenderable.getCapturesMouseEvents());

        _rectangleRenderable.setCapturesMouseEvents(false);

        assertFalse(_rectangleRenderable.getCapturesMouseEvents());
    }

    @Test
    void testPressAndSetOnPress() {
        assertThrows(UnsupportedOperationException.class, () ->
                _rectangleRenderableNotSupportingMouseEvents.press(2, 0L));
        assertThrows(UnsupportedOperationException.class, () ->
                _rectangleRenderableNotSupportingMouseEvents.setOnPress(2, new FakeAction<>()));

        _rectangleRenderable.setOnPress(2, ON_PRESS_ACTION);

        long timestamp = 456456L;
        _rectangleRenderable.press(2, timestamp);
        assertEquals(1, ON_PRESS_ACTION.NumberOfTimesCalled);
        assertEquals(1, ON_PRESS_ACTION.Inputs.size());
        assertEquals(timestamp, (long) ON_PRESS_ACTION.Inputs.get(0));

        FakeAction<Long> newOnPress = new FakeAction<>();
        _rectangleRenderable.setOnPress(2, newOnPress);

        _rectangleRenderable.press(2, timestamp + 1);

        assertEquals(1, newOnPress.NumberOfTimesCalled);
        assertEquals(1, newOnPress.Inputs.size());
        assertEquals(timestamp + 1, (long) newOnPress.Inputs.get(0));

        _rectangleRenderable.press(0, timestamp + 2);

        assertEquals(1, newOnPress.NumberOfTimesCalled);
        assertEquals(1, newOnPress.Inputs.size());
        assertEquals(timestamp + 1, (long) newOnPress.Inputs.get(0));
    }

    @Test
    void testPressActionIds() {
        String id1 = "id1";
        String id2 = "id2";
        String id3 = "id3";

        _rectangleRenderable.setOnPress(0, new FakeAction<>(id1));
        _rectangleRenderable.setOnPress(2, new FakeAction<>(id2));
        _rectangleRenderable.setOnPress(7, new FakeAction<>(id3));
        _rectangleRenderable.setOnPress(2, null);

        Map<Integer, String> pressActionIds = _rectangleRenderable.pressActionIds();

        assertNotNull(pressActionIds);
        assertEquals(2, pressActionIds.size());
        assertEquals(id1, pressActionIds.get(0));
        assertEquals(id3, pressActionIds.get(7));
    }

    @Test
    void testReleaseAndSetOnRelease() {
        assertThrows(UnsupportedOperationException.class, () ->
                _rectangleRenderableNotSupportingMouseEvents.release(2, 0L));
        assertThrows(UnsupportedOperationException.class, () ->
                _rectangleRenderableNotSupportingMouseEvents.setOnRelease(2, new FakeAction<>()));

        long timestamp = 456456L;
        _rectangleRenderable.release(2, timestamp);

        FakeAction<Long> newOnRelease = new FakeAction<>();
        _rectangleRenderable.setOnRelease(2, newOnRelease);

        _rectangleRenderable.release(2, timestamp + 1);
        assertEquals(1, newOnRelease.NumberOfTimesCalled);
        assertEquals(1, newOnRelease.Inputs.size());
        assertEquals(timestamp + 1, (long) newOnRelease.Inputs.get(0));
    }

    @Test
    void testReleaseActionIds() {
        String id1 = "id1";
        String id2 = "id2";
        String id3 = "id3";

        _rectangleRenderable.setOnRelease(0, new FakeAction<>(id1));
        _rectangleRenderable.setOnRelease(2, new FakeAction<>(id2));
        _rectangleRenderable.setOnRelease(7, new FakeAction<>(id3));
        _rectangleRenderable.setOnRelease(2, null);

        Map<Integer, String> releaseActionIds =
                _rectangleRenderable.releaseActionIds();

        assertNotNull(releaseActionIds);
        assertEquals(2, releaseActionIds.size());
        assertEquals(id1, releaseActionIds.get(0));
        assertEquals(id3, releaseActionIds.get(7));
    }

    @Test
    void testPressOrReleaseMethodsWithInvalidButtons() {
        long timestamp = 456456L;

        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.setOnPress(-1, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.setOnRelease(-1, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.press(-1, timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.press(-1, timestamp + 1));

        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.setOnPress(8, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.setOnRelease(8, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.press(8, timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.press(8, timestamp + 3));
    }

    @Test
    void testMouseOverAndSetOnMouseOver() {
        assertThrows(UnsupportedOperationException.class, () ->
                _rectangleRenderableNotSupportingMouseEvents.mouseOver(0L));
        assertThrows(UnsupportedOperationException.class, () ->
                _rectangleRenderableNotSupportingMouseEvents.setOnMouseOver(ON_MOUSE_OVER));

        long timestamp = 456456L;
        _rectangleRenderable.mouseOver(timestamp);
        assertEquals(1, ON_MOUSE_OVER.NumberOfTimesCalled);
        assertEquals(1, ON_MOUSE_OVER.Inputs.size());
        assertEquals(timestamp, (long) ON_MOUSE_OVER.Inputs.get(0));

        FakeAction<Long> newOnMouseOver = new FakeAction<>();
        _rectangleRenderable.setOnMouseOver(newOnMouseOver);

        _rectangleRenderable.mouseOver(timestamp + 1);
        assertEquals(1, newOnMouseOver.NumberOfTimesCalled);
        assertEquals(1, newOnMouseOver.Inputs.size());
        assertEquals(timestamp + 1, (long) newOnMouseOver.Inputs.get(0));
    }

    @Test
    void testMouseOverActionId() {
        String mouseOverActionId = "mouseOverActionId";

        assertThrows(UnsupportedOperationException.class, () ->
                _rectangleRenderableNotSupportingMouseEvents.mouseOverActionId());

        _rectangleRenderable.setOnMouseOver(null);

        assertNull(_rectangleRenderable.mouseOverActionId());

        _rectangleRenderable.setOnMouseOver(new FakeAction<>(mouseOverActionId));

        assertEquals(mouseOverActionId, _rectangleRenderable.mouseOverActionId());
    }

    @Test
    void testMouseLeaveAndSetOnMouseLeave() {
        assertThrows(UnsupportedOperationException.class, () ->
                _rectangleRenderableNotSupportingMouseEvents.mouseLeave(0L));
        assertThrows(UnsupportedOperationException.class, () ->
                _rectangleRenderableNotSupportingMouseEvents.setOnMouseLeave(ON_MOUSE_LEAVE));

        long timestamp = 456456L;
        _rectangleRenderable.mouseLeave(timestamp);
        assertEquals(1, ON_MOUSE_LEAVE.NumberOfTimesCalled);
        assertEquals(1, ON_MOUSE_LEAVE.Inputs.size());
        assertEquals(timestamp, (long) ON_MOUSE_LEAVE.Inputs.get(0));

        FakeAction<Long> newOnMouseLeave = new FakeAction<>();
        _rectangleRenderable.setOnMouseLeave(newOnMouseLeave);

        _rectangleRenderable.mouseLeave(timestamp + 1);
        assertEquals(1, newOnMouseLeave.NumberOfTimesCalled);
        assertEquals(1, newOnMouseLeave.Inputs.size());
        assertEquals(timestamp + 1, (long) newOnMouseLeave.Inputs.get(0));
    }

    @Test
    void testMouseLeaveActionId() {
        String mouseLeaveActionId = "mouseLeaveActionId";

        assertThrows(UnsupportedOperationException.class, () ->
                _rectangleRenderableNotSupportingMouseEvents.mouseLeaveActionId());

        _rectangleRenderable.setOnMouseLeave(null);

        assertNull(_rectangleRenderable.mouseLeaveActionId());

        _rectangleRenderable.setOnMouseLeave(new FakeAction<>(mouseLeaveActionId));

        assertEquals(mouseLeaveActionId, _rectangleRenderable.mouseLeaveActionId());
    }

    @Test
    void testMouseEventCallsToOutdatedTimestamps() {
        long timestamp = 456456L;
        RENDERING_AREA_PROVIDER.ProvidedValue = new FakeFloatBox(0f, 0f, 1f, 1f);

        _rectangleRenderable.press(0, timestamp);
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.press(0, timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.release(0, timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.mouseOver(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.mouseLeave(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.capturesMouseEventAtPoint(0f, 0f, timestamp - 1));

        _rectangleRenderable.release(0, timestamp + 1);
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.press(0, timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.release(0, timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.mouseOver(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.mouseLeave(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.capturesMouseEventAtPoint(0f, 0f, timestamp));

        _rectangleRenderable.mouseOver(timestamp + 2);
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.press(0, timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.release(0, timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.mouseOver(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.mouseLeave(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.capturesMouseEventAtPoint(0f, 0f, timestamp + 1));

        _rectangleRenderable.mouseLeave(timestamp + 3);
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.press(0, timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.release(0, timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.mouseOver(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.mouseLeave(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.capturesMouseEventAtPoint(0f, 0f, timestamp + 2));

        _rectangleRenderable.capturesMouseEventAtPoint(0f, 0f, timestamp + 4);
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.press(0, timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.release(0, timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.mouseOver(timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.mouseLeave(timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                _rectangleRenderable.capturesMouseEventAtPoint(0f, 0f, timestamp + 3));
    }

    @Test
    void testGetAndSetRenderingAreaProvider() {
        assertSame(RENDERING_AREA_PROVIDER,
                _rectangleRenderable.getRenderingDimensionsProvider());

        FakeProviderAtTime<FloatBox> newRenderingDimensionsProvider = new FakeProviderAtTime<>();

        _rectangleRenderable
                .setRenderingDimensionsProvider(newRenderingDimensionsProvider);

        assertSame(newRenderingDimensionsProvider,
                _rectangleRenderable.getRenderingDimensionsProvider());
    }

    @Test
    void testGetAndSetZ() {
        assertEquals(Z, _rectangleRenderable.getZ());

        int newZ = 456;

        _rectangleRenderable.setZ(newZ);

        assertEquals(newZ, _rectangleRenderable.getZ());

        verify(_mockContainingStack, times(1)).add(_rectangleRenderable);
    }

    @Test
    void testCapturesMouseEventsAtPoint() {
        assertTrue(_rectangleRenderable
                .capturesMouseEventAtPoint(-345634563456f, 789789789789f, 123123123123L));

        _rectangleRenderable.setCapturesMouseEvents(false);

        assertFalse(_rectangleRenderable
                .capturesMouseEventAtPoint(-345634563456f, 789789789789f, 123123123123L));
    }

    @Test
    void testDelete() {
        _rectangleRenderable.delete();

        verify(_mockContainingStack, times(1)).remove(_rectangleRenderable);
    }

    @Test
    void testUuid() {
        assertSame(UUID, _rectangleRenderable.uuid());
    }
}
