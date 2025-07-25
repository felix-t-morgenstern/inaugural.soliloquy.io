package inaugural.soliloquy.io.test.unit.graphics.renderables;

import inaugural.soliloquy.io.graphics.renderables.RectangleRenderableImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeAction;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeProviderAtTime;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeStaticProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.RectangleRenderable;
import soliloquy.specs.io.graphics.renderables.Renderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.ui.Component;
import soliloquy.specs.ui.EventInputs;

import java.awt.*;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

import static inaugural.soliloquy.io.api.Constants.WHOLE_SCREEN;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;
import static soliloquy.specs.ui.EventInputs.inputs;

@ExtendWith(MockitoExtension.class)
public class RectangleRenderableImplTests {
    private final ProviderAtTime<Color> TOP_LEFT_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> TOP_RIGHT_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> BOTTOM_RIGHT_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> BOTTOM_LEFT_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Integer> BACKGROUND_TEXTURE_ID_PROVIDER =
            new FakeProviderAtTime<>();
    private final Map<Integer, Action<EventInputs>> ON_PRESS_ACTIONS = mapOf();
    private final FakeStaticProvider<FloatBox> RENDERING_AREA_PROVIDER =
            new FakeStaticProvider<>(null);
    private final int Z = randomInt();
    private final long TIMESTAMP = randomLong();

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private ProviderAtTime<Float> mockTextureTileWidthProvider;
    @Mock private ProviderAtTime<Float> mockTextureTileHeightProvider;
    @Mock private Component mockContainingComponent;
    @Mock private BiConsumer<Component, Renderable> mockRemoveFromComponent;
    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private Action<EventInputs> mockOnPressAction;
    @Mock private Action<EventInputs> mockOnMouseOverAction;
    @Mock private Action<EventInputs> mockOnMouseLeaveAction;

    private RectangleRenderable renderable;
    private RectangleRenderable rectangleRenderableNotSupportingMouseEvents;

    @BeforeEach
    public void setUp() {
        lenient().when(mockRenderingBoundaries.currentBoundaries()).thenReturn(WHOLE_SCREEN);

        renderable = new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER, mockTextureTileWidthProvider,
                mockTextureTileHeightProvider, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction, RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                mockRemoveFromComponent, mockRenderingBoundaries);
        renderable.setCapturesMouseEvents(true);

        rectangleRenderableNotSupportingMouseEvents = new RectangleRenderableImpl(
                TOP_LEFT_COLOR_PROVIDER, TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                mockTextureTileWidthProvider, mockTextureTileHeightProvider, ON_PRESS_ACTIONS,
                null, mockOnMouseOverAction, mockOnMouseLeaveAction, RENDERING_AREA_PROVIDER, Z,
                UUID,
                mockContainingComponent, mockRemoveFromComponent, mockRenderingBoundaries);
        rectangleRenderableNotSupportingMouseEvents.setCapturesMouseEvents(false);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(null,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                        mockRemoveFromComponent, mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        null, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                        mockRemoveFromComponent, mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, null,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                        mockRemoveFromComponent, mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        null, BACKGROUND_TEXTURE_ID_PROVIDER,
                        mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                        mockRemoveFromComponent, mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, null,
                        mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                        mockRemoveFromComponent, mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        null, mockTextureTileHeightProvider,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                        mockRemoveFromComponent, mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        mockTextureTileWidthProvider, null,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                        mockRemoveFromComponent, mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        null, Z, UUID, mockContainingComponent, mockRemoveFromComponent,
                        mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, null, mockContainingComponent,
                        mockRemoveFromComponent, mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent, null, mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent, mockRemoveFromComponent, null));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, UUID, null, mockRemoveFromComponent,
                        mockRenderingBoundaries));
    }

    @Test
    public void testSetAndGetTopLeftColorProvider() {
        assertSame(TOP_LEFT_COLOR_PROVIDER, renderable.getTopLeftColorProvider());

        var newProvider = new FakeProviderAtTime<Color>();

        renderable.setTopLeftColorProvider(newProvider);

        assertSame(newProvider, renderable.getTopLeftColorProvider());
    }

    @Test
    public void testSetAndGetTopRightColorProvider() {
        assertSame(TOP_RIGHT_COLOR_PROVIDER, renderable.getTopRightColorProvider());

        var newProvider = new FakeProviderAtTime<Color>();

        renderable.setTopRightColorProvider(newProvider);

        assertSame(newProvider, renderable.getTopRightColorProvider());
    }

    @Test
    public void testSetAndGetBottomRightColorProvider() {
        assertSame(BOTTOM_RIGHT_COLOR_PROVIDER,
                renderable.getBottomRightColorProvider());

        FakeProviderAtTime<Color> newProvider = new FakeProviderAtTime<>();

        renderable.setBottomRightColorProvider(newProvider);

        assertSame(newProvider, renderable.getBottomRightColorProvider());
    }

    @Test
    public void testSetAndGetBottomLeftColorProvider() {
        assertSame(BOTTOM_LEFT_COLOR_PROVIDER, renderable.getBottomLeftColorProvider());

        FakeProviderAtTime<Color> newProvider = new FakeProviderAtTime<>();

        renderable.setBottomLeftColorProvider(newProvider);

        assertSame(newProvider, renderable.getBottomLeftColorProvider());
    }

    @Test
    public void testSetAndGetBackgroundTextureIdProvider() {
        assertSame(BACKGROUND_TEXTURE_ID_PROVIDER, renderable.getTextureIdProvider());

        var newProvider = new FakeProviderAtTime<Integer>();

        renderable.setTextureIdProvider(newProvider);

        assertSame(newProvider, renderable.getTextureIdProvider());
    }

    @Test
    public void testSetAndGetBackgroundTextureTileWidthProvider() {
        assertEquals(mockTextureTileWidthProvider, renderable.getTextureTileWidthProvider());

        @SuppressWarnings("unchecked") var newProvider =
                (ProviderAtTime<Float>) mock(ProviderAtTime.class);

        renderable.setTextureTileWidthProvider(newProvider);

        assertEquals(newProvider, renderable.getTextureTileWidthProvider());
    }

    @Test
    public void testSetAndGetBackgrogetTextureTileHeightProvider() {
        assertEquals(mockTextureTileHeightProvider,
                renderable.getTextureTileHeightProvider());

        @SuppressWarnings("unchecked") var newProvider =
                (ProviderAtTime<Float>) mock(ProviderAtTime.class);

        renderable.setTextureTileHeightProvider(newProvider);

        assertEquals(newProvider, renderable.getTextureTileHeightProvider());
    }

    @Test
    public void testSetProvidersAndTileDimensionsWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setTopLeftColorProvider(null));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setTopRightColorProvider(null));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setBottomRightColorProvider(null));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setBottomLeftColorProvider(null));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setTextureIdProvider(null));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setTextureTileWidthProvider(null));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setTextureTileHeightProvider(null));
    }

    @Test
    public void testGetAndSetCapturesMouseEvents() {
        assertTrue(renderable.getCapturesMouseEvents());

        renderable.setCapturesMouseEvents(false);

        assertFalse(renderable.getCapturesMouseEvents());
    }

    @Test
    public void testPressAndSetOnPress() {
        assertThrows(UnsupportedOperationException.class, () ->
                rectangleRenderableNotSupportingMouseEvents.press(2, 0L));
        assertThrows(UnsupportedOperationException.class, () ->
                rectangleRenderableNotSupportingMouseEvents.setOnPress(2, new FakeAction<>()));

        renderable.setOnPress(2, mockOnPressAction);

        renderable.press(2, TIMESTAMP);

        verify(mockOnPressAction, once()).run(eq(inputs(TIMESTAMP, renderable)));

        //noinspection unchecked
        Action<EventInputs> newOnPress = mock(Action.class);
        renderable.setOnPress(2, newOnPress);

        renderable.press(2, TIMESTAMP + 1);

        verify(newOnPress, once()).run(eq(inputs(TIMESTAMP + 1, renderable)));

        renderable.press(0, TIMESTAMP + 2);

        verify(newOnPress, once()).run(any());
    }

    @Test
    public void testPressActionIds() {
        var id1 = "id1";
        var id2 = "id2";
        var id3 = "id3";

        renderable.setOnPress(0, new FakeAction<>(id1));
        renderable.setOnPress(2, new FakeAction<>(id2));
        renderable.setOnPress(7, new FakeAction<>(id3));
        renderable.setOnPress(2, null);

        Map<Integer, String> pressActionIds = renderable.pressActionIds();

        assertNotNull(pressActionIds);
        assertEquals(2, pressActionIds.size());
        assertEquals(id1, pressActionIds.get(0));
        assertEquals(id3, pressActionIds.get(7));
    }

    @Test
    public void testReleaseAndSetOnRelease() {
        assertThrows(UnsupportedOperationException.class, () ->
                rectangleRenderableNotSupportingMouseEvents.release(2, 0L));
        assertThrows(UnsupportedOperationException.class, () ->
                rectangleRenderableNotSupportingMouseEvents.setOnRelease(2, new FakeAction<>()));

        renderable.release(2, TIMESTAMP);

        //noinspection unchecked
        Action<EventInputs> newOnRelease = mock(Action.class);
        renderable.setOnRelease(2, newOnRelease);

        renderable.release(2, TIMESTAMP + 1);

        verify(newOnRelease, once()).run(eq(inputs(TIMESTAMP + 1, renderable)));
    }

    @Test
    public void testReleaseActionIds() {
        String id1 = randomString();
        String id2 = randomString();
        String id3 = randomString();

        renderable.setOnRelease(0, new FakeAction<>(id1));
        renderable.setOnRelease(2, new FakeAction<>(id2));
        renderable.setOnRelease(7, new FakeAction<>(id3));
        renderable.setOnRelease(2, null);

        Map<Integer, String> releaseActionIds =
                renderable.releaseActionIds();

        assertNotNull(releaseActionIds);
        assertEquals(2, releaseActionIds.size());
        assertEquals(id1, releaseActionIds.get(0));
        assertEquals(id3, releaseActionIds.get(7));
    }

    @Test
    public void testPressOrReleaseMethodsWithInvalidButtons() {
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setOnPress(-1, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setOnRelease(-1, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.press(-1, TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.press(-1, TIMESTAMP + 1));

        assertThrows(IllegalArgumentException.class, () ->
                renderable.setOnPress(8, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setOnRelease(8, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.press(8, TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.press(8, TIMESTAMP + 3));
    }

    @Test
    public void testMouseOverAndSetOnMouseOver() {
        assertThrows(UnsupportedOperationException.class, () ->
                rectangleRenderableNotSupportingMouseEvents.mouseOver(0L));
        assertThrows(UnsupportedOperationException.class, () ->
                rectangleRenderableNotSupportingMouseEvents.setOnMouseOver(mockOnMouseOverAction));

        renderable.mouseOver(TIMESTAMP);

        verify(mockOnMouseOverAction, once()).run(eq(inputs(TIMESTAMP, renderable)));

        //noinspection unchecked
        Action<EventInputs> newOnMouseOver = mock(Action.class);
        renderable.setOnMouseOver(newOnMouseOver);

        renderable.mouseOver(TIMESTAMP + 1);

        verify(newOnMouseOver, once()).run(eq(inputs(TIMESTAMP + 1, renderable)));
    }

    @Test
    public void testMouseOverActionId() {
        String mouseOverActionId = "mouseOverActionId";

        assertThrows(UnsupportedOperationException.class, () ->
                rectangleRenderableNotSupportingMouseEvents.mouseOverActionId());

        renderable.setOnMouseOver(null);

        assertNull(renderable.mouseOverActionId());

        renderable.setOnMouseOver(new FakeAction<>(mouseOverActionId));

        assertEquals(mouseOverActionId, renderable.mouseOverActionId());
    }

    @Test
    public void testMouseLeaveAndSetOnMouseLeave() {
        assertThrows(UnsupportedOperationException.class, () ->
                rectangleRenderableNotSupportingMouseEvents.mouseLeave(0L));
        assertThrows(UnsupportedOperationException.class, () ->
                rectangleRenderableNotSupportingMouseEvents.setOnMouseLeave(
                        mockOnMouseLeaveAction));

        renderable.mouseLeave(TIMESTAMP);

        verify(mockOnMouseLeaveAction, once()).run(eq(inputs(TIMESTAMP, renderable)));

        //noinspection unchecked
        Action<EventInputs> newOnMouseLeave = mock(Action.class);
        renderable.setOnMouseLeave(newOnMouseLeave);

        renderable.mouseLeave(TIMESTAMP + 1);

        verify(newOnMouseLeave, once()).run(eq(inputs(TIMESTAMP + 1, renderable)));
    }

    @Test
    public void testMouseLeaveActionId() {
        String mouseLeaveActionId = "mouseLeaveActionId";

        assertThrows(UnsupportedOperationException.class, () ->
                rectangleRenderableNotSupportingMouseEvents.mouseLeaveActionId());

        renderable.setOnMouseLeave(null);

        assertNull(renderable.mouseLeaveActionId());

        renderable.setOnMouseLeave(new FakeAction<>(mouseLeaveActionId));

        assertEquals(mouseLeaveActionId, renderable.mouseLeaveActionId());
    }

    @Test
    public void testMouseEventCallsToOutdatedTimestamps() {
        RENDERING_AREA_PROVIDER.ProvidedValue = floatBoxOf(0f, 0f, 1f, 1f);

        renderable.press(0, TIMESTAMP);
        assertThrows(IllegalArgumentException.class, () ->
                renderable.press(0, TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.release(0, TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.mouseOver(TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.mouseLeave(TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.capturesMouseEventAtPoint(vertexOf(0f, 0f), TIMESTAMP - 1));

        renderable.release(0, TIMESTAMP + 1);
        assertThrows(IllegalArgumentException.class, () ->
                renderable.press(0, TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.release(0, TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.mouseOver(TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.mouseLeave(TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.capturesMouseEventAtPoint(vertexOf(0f, 0f), TIMESTAMP));

        renderable.mouseOver(TIMESTAMP + 2);
        assertThrows(IllegalArgumentException.class, () ->
                renderable.press(0, TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.release(0, TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.mouseOver(TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.mouseLeave(TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.capturesMouseEventAtPoint(vertexOf(0f, 0f), TIMESTAMP + 1));

        renderable.mouseLeave(TIMESTAMP + 3);
        assertThrows(IllegalArgumentException.class, () ->
                renderable.press(0, TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.release(0, TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.mouseOver(TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.mouseLeave(TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.capturesMouseEventAtPoint(vertexOf(0f, 0f), TIMESTAMP + 2));

        renderable.capturesMouseEventAtPoint(vertexOf(0f, 0f), TIMESTAMP + 4);
        assertThrows(IllegalArgumentException.class, () ->
                renderable.press(0, TIMESTAMP + 3));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.release(0, TIMESTAMP + 3));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.mouseOver(TIMESTAMP + 3));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.mouseLeave(TIMESTAMP + 3));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.capturesMouseEventAtPoint(vertexOf(0f, 0f), TIMESTAMP + 3));
    }

    @Test
    public void testGetAndSetRenderingAreaProvider() {
        assertSame(RENDERING_AREA_PROVIDER,
                renderable.getRenderingDimensionsProvider());

        FakeProviderAtTime<FloatBox> newRenderingDimensionsProvider = new FakeProviderAtTime<>();

        renderable
                .setRenderingDimensionsProvider(newRenderingDimensionsProvider);

        assertSame(newRenderingDimensionsProvider,
                renderable.getRenderingDimensionsProvider());
    }

    @Test
    public void testGetAndSetZ() {
        assertEquals(Z, renderable.getZ());

        int newZ = 456;

        renderable.setZ(newZ);

        assertEquals(newZ, renderable.getZ());

        verify(mockContainingComponent, once()).add(renderable);
    }

    @Test
    public void testCapturesMouseEventsAtPoint() {
        var renderingDimensions = floatBoxOf(0.25f, 0.25f, 0.75f, 0.75f);
        renderable.setRenderingDimensionsProvider(
                new FakeStaticProvider<>(renderingDimensions));

        assertTrue(renderable
                .capturesMouseEventAtPoint(vertexOf(0.251f, 0.5f), TIMESTAMP));
        assertFalse(renderable
                .capturesMouseEventAtPoint(vertexOf(0.249f, 0.5f), TIMESTAMP));

        renderable.setCapturesMouseEvents(false);

        assertFalse(renderable
                .capturesMouseEventAtPoint(vertexOf(0.251f, 0.5f), TIMESTAMP));
        assertFalse(renderable
                .capturesMouseEventAtPoint(vertexOf(0.249f, 0.5f), TIMESTAMP));
    }

    @Test
    public void testCapturesMouseEventsAtPointDoesNotExceedRenderingBoundaries() {
        var renderingDimensions = floatBoxOf(0.25f, 0.25f, 0.75f, 0.75f);
        renderable.setRenderingDimensionsProvider(
                new FakeStaticProvider<>(renderingDimensions));

        when(mockRenderingBoundaries.currentBoundaries()).thenReturn(floatBoxOf(0f, 0f, 0.5f, 1f));

        assertTrue(renderable
                .capturesMouseEventAtPoint(vertexOf(0.499f, 0.5f), TIMESTAMP));
        assertFalse(renderable
                .capturesMouseEventAtPoint(vertexOf(0.501f, 0.5f), TIMESTAMP));
    }

    @Test
    public void testCapturesMouseEventsAtPointWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> renderable.capturesMouseEventAtPoint(vertexOf(-0.001f, 0f),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> renderable.capturesMouseEventAtPoint(vertexOf(1.001f, 0f),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> renderable.capturesMouseEventAtPoint(vertexOf(0f, -0.001f),
                        TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> renderable.capturesMouseEventAtPoint(vertexOf(0f, 1.001f),
                        TIMESTAMP));
    }

    @Test
    public void testDelete() {
        renderable.delete();

        verify(mockRemoveFromComponent, once())
                .accept(same(mockContainingComponent), same(renderable));
    }

    @Test
    public void testUuid() {
        assertSame(UUID, renderable.uuid());
    }
}
