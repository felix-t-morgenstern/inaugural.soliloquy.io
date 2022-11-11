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
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.RectangleRenderable;
import soliloquy.specs.graphics.renderables.RenderableWithMouseEvents.MouseEventInputs;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.graphics.api.Constants.WHOLE_SCREEN;
import static inaugural.soliloquy.tools.random.Random.*;
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
    private final HashMap<Integer, Action<MouseEventInputs>> ON_PRESS_ACTIONS = new HashMap<>();
    private final FakeStaticProvider<FloatBox> RENDERING_AREA_PROVIDER =
            new FakeStaticProvider<>(null);
    private final int Z = randomInt();
    private final long TIMESTAMP = randomLong();

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private RenderableStack mockContainingStack;
    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private Action<MouseEventInputs> mockOnPressAction;
    @Mock private Action<MouseEventInputs> mockOnMouseOverAction;
    @Mock private Action<MouseEventInputs> mockOnMouseLeaveAction;

    private RectangleRenderable rectangleRenderable;
    private RectangleRenderable rectangleRenderableNotSupportingMouseEvents;

    @BeforeEach
    void setUp() {
        mockContainingStack = mock(RenderableStack.class);
        mockRenderingBoundaries = mock(RenderingBoundaries.class);
        when(mockRenderingBoundaries.currentBoundaries()).thenReturn(WHOLE_SCREEN);

        //noinspection unchecked
        mockOnPressAction = mock(Action.class);
        //noinspection unchecked
        mockOnMouseOverAction = mock(Action.class);
        //noinspection unchecked
        mockOnMouseLeaveAction = mock(Action.class);

        rectangleRenderable = new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER, BACKGROUND_TEXTURE_TILE_WIDTH,
                BACKGROUND_TEXTURE_TILE_HEIGHT, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction, RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack,
                mockRenderingBoundaries);
        rectangleRenderable.setCapturesMouseEvents(true);

        rectangleRenderableNotSupportingMouseEvents = new RectangleRenderableImpl(
                TOP_LEFT_COLOR_PROVIDER, TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT, ON_PRESS_ACTIONS,
                null, mockOnMouseOverAction, mockOnMouseLeaveAction, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingStack, mockRenderingBoundaries);
        rectangleRenderableNotSupportingMouseEvents.setCapturesMouseEvents(false);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(null,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack,
                        mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        null, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack,
                        mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, null,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack,
                        mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        null, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack,
                        mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, null,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack,
                        mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        0f, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack,
                        mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, 0f,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack,
                        mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        null, Z, UUID, mockContainingStack, mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, null, mockContainingStack,
                        mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack, null));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, UUID, null, mockRenderingBoundaries));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(RectangleRenderable.class.getCanonicalName(),
                rectangleRenderable.getInterfaceName());
    }

    @Test
    void testSetAndGetTopLeftColorProvider() {
        assertSame(TOP_LEFT_COLOR_PROVIDER, rectangleRenderable.getTopLeftColorProvider());

        FakeProviderAtTime<Color> newProvider = new FakeProviderAtTime<>();

        rectangleRenderable.setTopLeftColorProvider(newProvider);

        assertSame(newProvider, rectangleRenderable.getTopLeftColorProvider());
    }

    @Test
    void testSetAndGetTopRightColorProvider() {
        assertSame(TOP_RIGHT_COLOR_PROVIDER, rectangleRenderable.getTopRightColorProvider());

        FakeProviderAtTime<Color> newProvider = new FakeProviderAtTime<>();

        rectangleRenderable.setTopRightColorProvider(newProvider);

        assertSame(newProvider, rectangleRenderable.getTopRightColorProvider());
    }

    @Test
    void testSetAndGetBottomRightColorProvider() {
        assertSame(BOTTOM_RIGHT_COLOR_PROVIDER,
                rectangleRenderable.getBottomRightColorProvider());

        FakeProviderAtTime<Color> newProvider = new FakeProviderAtTime<>();

        rectangleRenderable.setBottomRightColorProvider(newProvider);

        assertSame(newProvider, rectangleRenderable.getBottomRightColorProvider());
    }

    @Test
    void testSetAndGetBottomLeftColorProvider() {
        assertSame(BOTTOM_LEFT_COLOR_PROVIDER, rectangleRenderable.getBottomLeftColorProvider());

        FakeProviderAtTime<Color> newProvider = new FakeProviderAtTime<>();

        rectangleRenderable.setBottomLeftColorProvider(newProvider);

        assertSame(newProvider, rectangleRenderable.getBottomLeftColorProvider());
    }

    @Test
    void testSetAndGetBackgroundTextureIdProvider() {
        assertSame(BACKGROUND_TEXTURE_ID_PROVIDER,
                rectangleRenderable.getBackgroundTextureIdProvider());

        FakeProviderAtTime<Integer> newProvider = new FakeProviderAtTime<>();

        rectangleRenderable.setBackgroundTextureIdProvider(newProvider);

        assertSame(newProvider, rectangleRenderable.getBackgroundTextureIdProvider());
    }

    @Test
    void testSetAndGetBackgroundTextureTileWidth() {
        assertEquals(BACKGROUND_TEXTURE_TILE_WIDTH,
                rectangleRenderable.getBackgroundTextureTileWidth());

        float newWidth = 0.1312f;

        rectangleRenderable.setBackgroundTextureTileWidth(newWidth);

        assertEquals(newWidth, rectangleRenderable.getBackgroundTextureTileWidth());
    }

    @Test
    void testSetAndGetBackgroundTextureTileHeight() {
        assertEquals(BACKGROUND_TEXTURE_TILE_HEIGHT,
                rectangleRenderable.getBackgroundTextureTileHeight());

        float newHeight = 0.1312f;

        rectangleRenderable.setBackgroundTextureTileHeight(newHeight);

        assertEquals(newHeight, rectangleRenderable.getBackgroundTextureTileHeight());
    }

    @Test
    void testSetProvidersAndTileDimensionsWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.setTopLeftColorProvider(null));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.setTopRightColorProvider(null));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.setBottomRightColorProvider(null));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.setBottomLeftColorProvider(null));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.setBackgroundTextureIdProvider(null));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.setBackgroundTextureTileWidth(0f));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.setBackgroundTextureTileHeight(0f));
    }

    @Test
    void testGetAndSetCapturesMouseEvents() {
        assertTrue(rectangleRenderable.getCapturesMouseEvents());

        rectangleRenderable.setCapturesMouseEvents(false);

        assertFalse(rectangleRenderable.getCapturesMouseEvents());
    }

    @Test
    void testPressAndSetOnPress() {
        assertThrows(UnsupportedOperationException.class, () ->
                rectangleRenderableNotSupportingMouseEvents.press(2, 0L));
        assertThrows(UnsupportedOperationException.class, () ->
                rectangleRenderableNotSupportingMouseEvents.setOnPress(2, new FakeAction<>()));

        rectangleRenderable.setOnPress(2, mockOnPressAction);

        rectangleRenderable.press(2, TIMESTAMP);

        verify(mockOnPressAction, times(1)).run(eq(MouseEventInputs.of(TIMESTAMP, rectangleRenderable)));

        //noinspection unchecked
        Action<MouseEventInputs> newOnPress = mock(Action.class);
        rectangleRenderable.setOnPress(2, newOnPress);

        rectangleRenderable.press(2, TIMESTAMP + 1);

        verify(newOnPress, times(1)).run(eq(MouseEventInputs.of(TIMESTAMP + 1, rectangleRenderable)));

        rectangleRenderable.press(0, TIMESTAMP + 2);

        verify(newOnPress, times(1)).run(any());
    }

    @Test
    void testPressActionIds() {
        String id1 = "id1";
        String id2 = "id2";
        String id3 = "id3";

        rectangleRenderable.setOnPress(0, new FakeAction<>(id1));
        rectangleRenderable.setOnPress(2, new FakeAction<>(id2));
        rectangleRenderable.setOnPress(7, new FakeAction<>(id3));
        rectangleRenderable.setOnPress(2, null);

        Map<Integer, String> pressActionIds = rectangleRenderable.pressActionIds();

        assertNotNull(pressActionIds);
        assertEquals(2, pressActionIds.size());
        assertEquals(id1, pressActionIds.get(0));
        assertEquals(id3, pressActionIds.get(7));
    }

    @Test
    void testReleaseAndSetOnRelease() {
        assertThrows(UnsupportedOperationException.class, () ->
                rectangleRenderableNotSupportingMouseEvents.release(2, 0L));
        assertThrows(UnsupportedOperationException.class, () ->
                rectangleRenderableNotSupportingMouseEvents.setOnRelease(2, new FakeAction<>()));

        rectangleRenderable.release(2, TIMESTAMP);

        //noinspection unchecked
        Action<MouseEventInputs> newOnRelease = mock(Action.class);
        rectangleRenderable.setOnRelease(2, newOnRelease);

        rectangleRenderable.release(2, TIMESTAMP + 1);

        verify(newOnRelease, times(1)).run(eq(MouseEventInputs.of(TIMESTAMP + 1, rectangleRenderable)));
    }

    @Test
    void testReleaseActionIds() {
        String id1 = randomString();
        String id2 = randomString();
        String id3 = randomString();

        rectangleRenderable.setOnRelease(0, new FakeAction<>(id1));
        rectangleRenderable.setOnRelease(2, new FakeAction<>(id2));
        rectangleRenderable.setOnRelease(7, new FakeAction<>(id3));
        rectangleRenderable.setOnRelease(2, null);

        Map<Integer, String> releaseActionIds =
                rectangleRenderable.releaseActionIds();

        assertNotNull(releaseActionIds);
        assertEquals(2, releaseActionIds.size());
        assertEquals(id1, releaseActionIds.get(0));
        assertEquals(id3, releaseActionIds.get(7));
    }

    @Test
    void testPressOrReleaseMethodsWithInvalidButtons() {
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.setOnPress(-1, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.setOnRelease(-1, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.press(-1, TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.press(-1, TIMESTAMP + 1));

        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.setOnPress(8, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.setOnRelease(8, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.press(8, TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.press(8, TIMESTAMP + 3));
    }

    @Test
    void testMouseOverAndSetOnMouseOver() {
        assertThrows(UnsupportedOperationException.class, () ->
                rectangleRenderableNotSupportingMouseEvents.mouseOver(0L));
        assertThrows(UnsupportedOperationException.class, () ->
                rectangleRenderableNotSupportingMouseEvents.setOnMouseOver(mockOnMouseOverAction));

        rectangleRenderable.mouseOver(TIMESTAMP);

        verify(mockOnMouseOverAction, times(1)).run(eq(MouseEventInputs.of(TIMESTAMP, rectangleRenderable)));

        //noinspection unchecked
        Action<MouseEventInputs> newOnMouseOver = mock(Action.class);
        rectangleRenderable.setOnMouseOver(newOnMouseOver);

        rectangleRenderable.mouseOver(TIMESTAMP + 1);

        verify(newOnMouseOver, times(1)).run(eq(MouseEventInputs.of(TIMESTAMP + 1, rectangleRenderable)));
    }

    @Test
    void testMouseOverActionId() {
        String mouseOverActionId = "mouseOverActionId";

        assertThrows(UnsupportedOperationException.class, () ->
                rectangleRenderableNotSupportingMouseEvents.mouseOverActionId());

        rectangleRenderable.setOnMouseOver(null);

        assertNull(rectangleRenderable.mouseOverActionId());

        rectangleRenderable.setOnMouseOver(new FakeAction<>(mouseOverActionId));

        assertEquals(mouseOverActionId, rectangleRenderable.mouseOverActionId());
    }

    @Test
    void testMouseLeaveAndSetOnMouseLeave() {
        assertThrows(UnsupportedOperationException.class, () ->
                rectangleRenderableNotSupportingMouseEvents.mouseLeave(0L));
        assertThrows(UnsupportedOperationException.class, () ->
                rectangleRenderableNotSupportingMouseEvents.setOnMouseLeave(mockOnMouseLeaveAction));

        rectangleRenderable.mouseLeave(TIMESTAMP);

        verify(mockOnMouseLeaveAction, times(1)).run(eq(MouseEventInputs.of(TIMESTAMP, rectangleRenderable)));

        //noinspection unchecked
        Action<MouseEventInputs> newOnMouseLeave = mock(Action.class);
        rectangleRenderable.setOnMouseLeave(newOnMouseLeave);

        rectangleRenderable.mouseLeave(TIMESTAMP + 1);

        verify(newOnMouseLeave, times(1)).run(eq(MouseEventInputs.of(TIMESTAMP + 1, rectangleRenderable)));
    }

    @Test
    void testMouseLeaveActionId() {
        String mouseLeaveActionId = "mouseLeaveActionId";

        assertThrows(UnsupportedOperationException.class, () ->
                rectangleRenderableNotSupportingMouseEvents.mouseLeaveActionId());

        rectangleRenderable.setOnMouseLeave(null);

        assertNull(rectangleRenderable.mouseLeaveActionId());

        rectangleRenderable.setOnMouseLeave(new FakeAction<>(mouseLeaveActionId));

        assertEquals(mouseLeaveActionId, rectangleRenderable.mouseLeaveActionId());
    }

    @Test
    void testMouseEventCallsToOutdatedTimestamps() {
        RENDERING_AREA_PROVIDER.ProvidedValue = new FakeFloatBox(0f, 0f, 1f, 1f);

        rectangleRenderable.press(0, TIMESTAMP);
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.press(0, TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.release(0, TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.mouseOver(TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.mouseLeave(TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.capturesMouseEventAtPoint(Vertex.of(0f, 0f), TIMESTAMP - 1));

        rectangleRenderable.release(0, TIMESTAMP + 1);
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.press(0, TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.release(0, TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.mouseOver(TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.mouseLeave(TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.capturesMouseEventAtPoint(Vertex.of(0f, 0f), TIMESTAMP));

        rectangleRenderable.mouseOver(TIMESTAMP + 2);
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.press(0, TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.release(0, TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.mouseOver(TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.mouseLeave(TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.capturesMouseEventAtPoint(Vertex.of(0f, 0f), TIMESTAMP + 1));

        rectangleRenderable.mouseLeave(TIMESTAMP + 3);
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.press(0, TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.release(0, TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.mouseOver(TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.mouseLeave(TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.capturesMouseEventAtPoint(Vertex.of(0f, 0f), TIMESTAMP + 2));

        rectangleRenderable.capturesMouseEventAtPoint(Vertex.of(0f, 0f), TIMESTAMP + 4);
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.press(0, TIMESTAMP + 3));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.release(0, TIMESTAMP + 3));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.mouseOver(TIMESTAMP + 3));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.mouseLeave(TIMESTAMP + 3));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderable.capturesMouseEventAtPoint(Vertex.of(0f, 0f), TIMESTAMP + 3));
    }

    @Test
    void testGetAndSetRenderingAreaProvider() {
        assertSame(RENDERING_AREA_PROVIDER,
                rectangleRenderable.getRenderingDimensionsProvider());

        FakeProviderAtTime<FloatBox> newRenderingDimensionsProvider = new FakeProviderAtTime<>();

        rectangleRenderable
                .setRenderingDimensionsProvider(newRenderingDimensionsProvider);

        assertSame(newRenderingDimensionsProvider,
                rectangleRenderable.getRenderingDimensionsProvider());
    }

    @Test
    void testGetAndSetZ() {
        assertEquals(Z, rectangleRenderable.getZ());

        int newZ = 456;

        rectangleRenderable.setZ(newZ);

        assertEquals(newZ, rectangleRenderable.getZ());

        verify(mockContainingStack, times(1)).add(rectangleRenderable);
    }

    @Test
    void testCapturesMouseEventsAtPoint() {
        FloatBox renderingDimensions = new FloatBox() {
            @Override
            public float leftX() {
                return 0.25f;
            }

            @Override
            public float topY() {
                return 0.25f;
            }

            @Override
            public float rightX() {
                return 0.75f;
            }

            @Override
            public float bottomY() {
                return 0.75f;
            }

            @Override
            public float width() {
                return 0;
            }

            @Override
            public float height() {
                return 0;
            }

            @Override
            public FloatBox intersection(FloatBox floatBox) throws IllegalArgumentException {
                return null;
            }

            @Override
            public FloatBox translate(float v, float v1) {
                return null;
            }

            @Override
            public String getInterfaceName() {
                return null;
            }
        };
        rectangleRenderable.setRenderingDimensionsProvider(
                new FakeStaticProvider<>(renderingDimensions));

        assertTrue(rectangleRenderable
                .capturesMouseEventAtPoint(Vertex.of(0.251f, 0.5f), TIMESTAMP));
        assertFalse(rectangleRenderable
                .capturesMouseEventAtPoint(Vertex.of(0.249f, 0.5f), TIMESTAMP));

        rectangleRenderable.setCapturesMouseEvents(false);

        assertFalse(rectangleRenderable
                .capturesMouseEventAtPoint(Vertex.of(0.251f, 0.5f), TIMESTAMP));
        assertFalse(rectangleRenderable
                .capturesMouseEventAtPoint(Vertex.of(0.249f, 0.5f), TIMESTAMP));
    }

    @Test
    void testCapturesMouseEventsAtPointDoesNotExceedRenderingBoundaries() {
        FloatBox renderingDimensions = new FloatBox() {
            @Override
            public float leftX() {
                return 0.25f;
            }

            @Override
            public float topY() {
                return 0.25f;
            }

            @Override
            public float rightX() {
                return 0.75f;
            }

            @Override
            public float bottomY() {
                return 0.75f;
            }

            @Override
            public float width() {
                return 0;
            }

            @Override
            public float height() {
                return 0;
            }

            @Override
            public FloatBox intersection(FloatBox floatBox) throws IllegalArgumentException {
                return null;
            }

            @Override
            public FloatBox translate(float v, float v1) {
                return null;
            }

            @Override
            public String getInterfaceName() {
                return null;
            }
        };
        rectangleRenderable.setRenderingDimensionsProvider(
                new FakeStaticProvider<>(renderingDimensions));

        when(mockRenderingBoundaries.currentBoundaries()).thenReturn(new FloatBox() {
            @Override
            public float leftX() {
                return 0f;
            }

            @Override
            public float topY() {
                return 0f;
            }

            @Override
            public float rightX() {
                return 0.5f;
            }

            @Override
            public float bottomY() {
                return 1f;
            }

            @Override
            public float width() {
                return 0;
            }

            @Override
            public float height() {
                return 0;
            }

            @Override
            public FloatBox intersection(FloatBox floatBox) throws IllegalArgumentException {
                return null;
            }

            @Override
            public FloatBox translate(float v, float v1) {
                return null;
            }

            @Override
            public String getInterfaceName() {
                return null;
            }
        });

        assertTrue(rectangleRenderable
                .capturesMouseEventAtPoint(Vertex.of(0.499f, 0.5f), TIMESTAMP));
        assertFalse(rectangleRenderable
                .capturesMouseEventAtPoint(Vertex.of(0.501f, 0.5f), TIMESTAMP));
    }

    @Test
    void testCapturesMouseEventsAtPointWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> rectangleRenderable.capturesMouseEventAtPoint(Vertex.of(-0.001f, 0f),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> rectangleRenderable.capturesMouseEventAtPoint(Vertex.of(1.001f, 0f),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> rectangleRenderable.capturesMouseEventAtPoint(Vertex.of(0f, -0.001f),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> rectangleRenderable.capturesMouseEventAtPoint(Vertex.of(0f, 1.001f),
                        TIMESTAMP));
    }

    @Test
    void testDelete() {
        rectangleRenderable.delete();

        verify(mockContainingStack, times(1)).remove(rectangleRenderable);
    }

    @Test
    void testUuid() {
        assertSame(UUID, rectangleRenderable.uuid());
    }
}
