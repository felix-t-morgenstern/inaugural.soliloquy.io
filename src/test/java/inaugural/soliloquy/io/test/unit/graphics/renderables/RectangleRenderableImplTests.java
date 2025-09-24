package inaugural.soliloquy.io.test.unit.graphics.renderables;

import inaugural.soliloquy.io.graphics.renderables.RectangleRenderableImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeProviderAtTime;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.RectangleRenderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.ui.EventInputs;

import java.awt.*;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.io.api.Constants.WHOLE_SCREEN;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static inaugural.soliloquy.tools.testing.Mock.generateMockStaticProvider;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.entities.Action.action;
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
    private final ProviderAtTime<FloatBox> RENDERING_AREA_PROVIDER =
            generateMockStaticProvider(null);
    private final int Z = randomInt();
    private final long TIMESTAMP = randomLong();

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private ProviderAtTime<Float> mockTextureTileWidthProvider;
    @Mock private ProviderAtTime<Float> mockTextureTileHeightProvider;
    @Mock private Component mockContainingComponent;
    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private TimestampValidator mockTimestampValidator;
    @Mock private Action<EventInputs> mockOnPressAction;
    @Mock private Action<EventInputs> mockOnMouseOverAction;
    @Mock private Action<EventInputs> mockOnMouseLeaveAction;

    private RectangleRenderable renderable;
    private RectangleRenderable renderableNotSupportingMouseEvents;

    @BeforeEach
    public void setUp() {
        lenient().when(mockRenderingBoundaries.currentBoundaries()).thenReturn(WHOLE_SCREEN);

        renderable = new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER, BOTTOM_LEFT_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER, mockTextureTileWidthProvider,
                mockTextureTileHeightProvider, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction, RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                mockRenderingBoundaries, mockTimestampValidator);
        renderable.setCapturesMouseEvents(true);

        renderableNotSupportingMouseEvents = new RectangleRenderableImpl(
                TOP_LEFT_COLOR_PROVIDER, TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                mockTextureTileWidthProvider, mockTextureTileHeightProvider, ON_PRESS_ACTIONS,
                null, mockOnMouseOverAction, mockOnMouseLeaveAction, RENDERING_AREA_PROVIDER, Z,
                UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator);
        renderableNotSupportingMouseEvents.setCapturesMouseEvents(false);
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
                        mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        null, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, null,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        null, BACKGROUND_TEXTURE_ID_PROVIDER,
                        mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, null,
                        mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        null, mockTextureTileHeightProvider,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        mockTextureTileWidthProvider, null,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        null, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, null, mockContainingComponent,
                        mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent, null,
                        mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () ->
                new RectangleRenderableImpl(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                        ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries, null));
    }

    @Test
    public void testConstructorAddsSelfToContainingComponent() {
        verify(mockContainingComponent, once()).add(renderable);
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
    public void testSetAndGetBackgroundTextureTileHeightProvider() {
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
                renderableNotSupportingMouseEvents.press(2, 0L));
        assertThrows(UnsupportedOperationException.class, () ->
                renderableNotSupportingMouseEvents.setOnPress(2, action(randomString(), _ -> {})));

        renderable.setOnPress(2, mockOnPressAction);

        renderable.press(2, TIMESTAMP);

        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(TIMESTAMP);
        verify(mockOnPressAction, once()).accept(eq(inputs(TIMESTAMP, renderable)));

        //noinspection unchecked
        Action<EventInputs> newOnPress = mock(Action.class);
        renderable.setOnPress(2, newOnPress);

        renderable.press(2, TIMESTAMP + 1);

        verify(newOnPress, once()).accept(eq(inputs(TIMESTAMP + 1, renderable)));

        renderable.press(0, TIMESTAMP + 2);

        verify(newOnPress, once()).accept(any());
    }

    @Test
    public void testPressActionIds() {
        var id1 = "id1";
        var id2 = "id2";
        var id3 = "id3";

        renderable.setOnPress(0, action(id1, _ -> {}));
        renderable.setOnPress(2, action(id2, _ -> {}));
        renderable.setOnPress(7, action(id3, _ -> {}));
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
                renderableNotSupportingMouseEvents.release(2, 0L));
        assertThrows(UnsupportedOperationException.class, () ->
                renderableNotSupportingMouseEvents.setOnRelease(2, action(randomString(), _ -> {})));

        renderable.release(2, TIMESTAMP);

        //noinspection unchecked
        Action<EventInputs> newOnRelease = mock(Action.class);
        renderable.setOnRelease(2, newOnRelease);

        renderable.release(2, TIMESTAMP + 1);

        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(TIMESTAMP);
        verify(newOnRelease, once()).accept(eq(inputs(TIMESTAMP + 1, renderable)));
    }

    @Test
    public void testReleaseActionIds() {
        String id1 = randomString();
        String id2 = randomString();
        String id3 = randomString();

        renderable.setOnRelease(0, action(id1, _ -> {}));
        renderable.setOnRelease(2, action(id2, _ -> {}));
        renderable.setOnRelease(7, action(id3, _ -> {}));
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
                renderable.setOnPress(-1, action(randomString(), _ -> {})));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setOnRelease(-1, action(randomString(), _ -> {})));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.press(-1, TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.press(-1, TIMESTAMP + 1));

        assertThrows(IllegalArgumentException.class, () ->
                renderable.setOnPress(8, action(randomString(), _ -> {})));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setOnRelease(8, action(randomString(), _ -> {})));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.press(8, TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.press(8, TIMESTAMP + 3));
    }

    @Test
    public void testMouseOverAndSetOnMouseOver() {
        assertThrows(UnsupportedOperationException.class, () ->
                renderableNotSupportingMouseEvents.mouseOver(0L));
        assertThrows(UnsupportedOperationException.class, () ->
                renderableNotSupportingMouseEvents.setOnMouseOver(mockOnMouseOverAction));

        renderable.mouseOver(TIMESTAMP);

        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(TIMESTAMP);
        verify(mockOnMouseOverAction, once()).accept(eq(inputs(TIMESTAMP, renderable)));

        //noinspection unchecked
        Action<EventInputs> newOnMouseOver = mock(Action.class);
        renderable.setOnMouseOver(newOnMouseOver);

        renderable.mouseOver(TIMESTAMP + 1);

        verify(newOnMouseOver, once()).accept(eq(inputs(TIMESTAMP + 1, renderable)));
    }

    @Test
    public void testMouseOverActionId() {
        var mouseOverActionId = randomString();

        assertThrows(UnsupportedOperationException.class, () ->
                renderableNotSupportingMouseEvents.mouseOverActionId());

        renderable.setOnMouseOver(null);

        assertNull(renderable.mouseOverActionId());

        renderable.setOnMouseOver(action(mouseOverActionId, _ -> {}));

        assertEquals(mouseOverActionId, renderable.mouseOverActionId());
    }

    @Test
    public void testMouseLeaveAndSetOnMouseLeave() {
        assertThrows(UnsupportedOperationException.class, () ->
                renderableNotSupportingMouseEvents.mouseLeave(0L));
        assertThrows(UnsupportedOperationException.class, () ->
                renderableNotSupportingMouseEvents.setOnMouseLeave(
                        mockOnMouseLeaveAction));

        renderable.mouseLeave(TIMESTAMP);

        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(TIMESTAMP);
        verify(mockOnMouseLeaveAction, once()).accept(eq(inputs(TIMESTAMP, renderable)));

        //noinspection unchecked
        Action<EventInputs> newOnMouseLeave = mock(Action.class);
        renderable.setOnMouseLeave(newOnMouseLeave);

        renderable.mouseLeave(TIMESTAMP + 1);

        verify(newOnMouseLeave, once()).accept(eq(inputs(TIMESTAMP + 1, renderable)));
    }

    @Test
    public void testMouseLeaveActionId() {
        var mouseLeaveActionId = randomString();

        assertThrows(UnsupportedOperationException.class,
                () -> renderableNotSupportingMouseEvents.mouseLeaveActionId());

        renderable.setOnMouseLeave(null);

        assertNull(renderable.mouseLeaveActionId());

        renderable.setOnMouseLeave(action(mouseLeaveActionId, _ -> {}));

        assertEquals(mouseLeaveActionId, renderable.mouseLeaveActionId());
    }

    @Test
    public void testGetAndSetRenderingAreaProvider() {
        assertSame(RENDERING_AREA_PROVIDER,
                renderable.getRenderingDimensionsProvider());

        FakeProviderAtTime<FloatBox> newRenderingDimensionsProvider = new FakeProviderAtTime<>();

        renderable.setRenderingDimensionsProvider(newRenderingDimensionsProvider);

        assertSame(newRenderingDimensionsProvider, renderable.getRenderingDimensionsProvider());
    }

    @Test
    public void testGetAndSetZ() {
        assertEquals(Z, renderable.getZ());

        var newZ = randomInt();

        renderable.setZ(newZ);

        assertEquals(newZ, renderable.getZ());
    }

    @Test
    public void testCapturesMouseEventsAtPoint() {
        var renderingDimensions = floatBoxOf(0.25f, 0.25f, 0.75f, 0.75f);
        renderable.setRenderingDimensionsProvider(
                generateMockStaticProvider(renderingDimensions));

        assertTrue(renderable
                .capturesMouseEventAtPoint(vertexOf(0.251f, 0.5f), TIMESTAMP));
        assertFalse(renderable
                .capturesMouseEventAtPoint(vertexOf(0.249f, 0.5f), TIMESTAMP));

        renderable.setCapturesMouseEvents(false);

        assertFalse(renderable
                .capturesMouseEventAtPoint(vertexOf(0.251f, 0.5f), TIMESTAMP));
        assertFalse(renderable
                .capturesMouseEventAtPoint(vertexOf(0.249f, 0.5f), TIMESTAMP));
        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(TIMESTAMP);
    }

    @Test
    public void testCapturesMouseEventsAtPointDoesNotExceedRenderingBoundaries() {
        var renderingDimensions = floatBoxOf(0.25f, 0.25f, 0.75f, 0.75f);
        renderable.setRenderingDimensionsProvider(
                generateMockStaticProvider(renderingDimensions));

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

        assertTrue(renderable.isDeleted());
        verify(mockContainingComponent, once()).remove(renderable);
    }

    @Test
    public void testUuid() {
        assertSame(UUID, renderable.uuid());
    }
}
