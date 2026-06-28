package inaugural.soliloquy.io.test.unit.graphics.renderables;

import inaugural.soliloquy.io.graphics.renderables.RectangleRenderableImpl;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.entities.Consumer;
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
import static soliloquy.specs.common.entities.Consumer.consumer;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;
import static soliloquy.specs.io.input.mouse.Mouse.EventType.*;
import static soliloquy.specs.ui.EventInputs.eventInputs;

@ExtendWith(MockitoExtension.class)
public class RectangleRenderableImplTests {
    private final Map<Integer, Consumer<EventInputs>> ON_PRESS_CONSUMERS = mapOf();
    private final ProviderAtTime<FloatBox> RENDERING_AREA_PROVIDER =
            generateMockStaticProvider(null);
    private final int Z = randomInt();
    private final long TIMESTAMP = randomLong();

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private ProviderAtTime<Color> mockTopLeftColorProvider;
    @Mock private ProviderAtTime<Color> mockTopRightColorProvider;
    @Mock private ProviderAtTime<Color> mockBottomRightColorProvider;
    @Mock private ProviderAtTime<Color> mockBottomLeftColorProvider;
    @Mock private ProviderAtTime<Integer> mockBackgroundTextureIdProvider;
    @Mock private ProviderAtTime<Float> mockTextureTilesPerWidthProvider;
    @Mock private ProviderAtTime<Float> mockTextureXOffsetProvider;
    @Mock private ProviderAtTime<Float> mockTextureTilesPerHeightProvider;
    @Mock private ProviderAtTime<Float> mockTextureYOffsetProvider;
    @Mock private Component mockContainingComponent;
    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private TimestampValidator mockTimestampValidator;
    @Mock private Consumer<EventInputs> mockOnPressAction;
    @Mock private Consumer<EventInputs> mockOnMouseOverAction;
    @Mock private Consumer<EventInputs> mockOnMouseLeaveAction;

    @Mock private ProviderAtTime<Color> newColorProvider;

    private RectangleRenderable renderable;
    private RectangleRenderable renderableNotSupportingMouseEvents;

    @BeforeEach
    public void setUp() {
        lenient().when(mockRenderingBoundaries.currentBoundaries()).thenReturn(WHOLE_SCREEN);

        renderable =
                new RectangleRenderableImpl(mockTopLeftColorProvider, mockTopRightColorProvider,
                        mockBottomRightColorProvider, mockBottomLeftColorProvider,
                        mockBackgroundTextureIdProvider, mockTextureTilesPerWidthProvider,
                        mockTextureXOffsetProvider, mockTextureTilesPerHeightProvider,
                        mockTextureYOffsetProvider, ON_PRESS_CONSUMERS, null, mockOnMouseOverAction,
                        mockOnMouseLeaveAction, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator);
        renderable.setCapturesMouseEvents(true);

        renderableNotSupportingMouseEvents =
                new RectangleRenderableImpl(mockTopLeftColorProvider, mockTopRightColorProvider,
                        mockBottomRightColorProvider, mockBottomLeftColorProvider,
                        mockBackgroundTextureIdProvider, mockTextureTilesPerWidthProvider,
                        mockTextureXOffsetProvider, mockTextureTilesPerHeightProvider,
                        mockTextureYOffsetProvider, ON_PRESS_CONSUMERS, null, mockOnMouseOverAction,
                        mockOnMouseLeaveAction, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator);
        renderableNotSupportingMouseEvents.setCapturesMouseEvents(false);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new RectangleRenderableImpl(null, mockTopRightColorProvider,
                        mockBottomRightColorProvider, mockBottomLeftColorProvider,
                        mockBackgroundTextureIdProvider, mockTextureTilesPerWidthProvider,
                        mockTextureXOffsetProvider, mockTextureTilesPerHeightProvider,
                        mockTextureYOffsetProvider, ON_PRESS_CONSUMERS, null, mockOnMouseOverAction,
                        mockOnMouseLeaveAction, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new RectangleRenderableImpl(mockTopLeftColorProvider, null,
                        mockBottomRightColorProvider, mockBottomLeftColorProvider,
                        mockBackgroundTextureIdProvider, mockTextureTilesPerWidthProvider,
                        mockTextureXOffsetProvider, mockTextureTilesPerHeightProvider,
                        mockTextureYOffsetProvider, ON_PRESS_CONSUMERS, null, mockOnMouseOverAction,
                        mockOnMouseLeaveAction, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new RectangleRenderableImpl(mockTopLeftColorProvider,
                        mockTopRightColorProvider, null, mockBottomLeftColorProvider,
                        mockBackgroundTextureIdProvider, mockTextureTilesPerWidthProvider,
                        mockTextureXOffsetProvider, mockTextureTilesPerHeightProvider,
                        mockTextureYOffsetProvider, ON_PRESS_CONSUMERS, null, mockOnMouseOverAction,
                        mockOnMouseLeaveAction, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new RectangleRenderableImpl(mockTopLeftColorProvider,
                        mockTopRightColorProvider, mockBottomRightColorProvider, null,
                        mockBackgroundTextureIdProvider, mockTextureTilesPerWidthProvider,
                        mockTextureXOffsetProvider, mockTextureTilesPerHeightProvider,
                        mockTextureYOffsetProvider, ON_PRESS_CONSUMERS, null, mockOnMouseOverAction,
                        mockOnMouseLeaveAction, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new RectangleRenderableImpl(mockTopLeftColorProvider,
                        mockTopRightColorProvider, mockBottomRightColorProvider,
                        mockBottomLeftColorProvider, null, mockTextureTilesPerWidthProvider,
                        mockTextureXOffsetProvider, mockTextureTilesPerHeightProvider,
                        mockTextureYOffsetProvider, ON_PRESS_CONSUMERS, null, mockOnMouseOverAction,
                        mockOnMouseLeaveAction, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new RectangleRenderableImpl(mockTopLeftColorProvider,
                        mockTopRightColorProvider, mockBottomRightColorProvider,
                        mockBottomLeftColorProvider, mockBackgroundTextureIdProvider,
                        mockTextureTilesPerWidthProvider, mockTextureXOffsetProvider,
                        mockTextureTilesPerHeightProvider, mockTextureYOffsetProvider,
                        ON_PRESS_CONSUMERS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        null, Z, UUID, mockContainingComponent, mockRenderingBoundaries,
                        mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new RectangleRenderableImpl(mockTopLeftColorProvider,
                        mockTopRightColorProvider, mockBottomRightColorProvider,
                        mockBottomLeftColorProvider, mockBackgroundTextureIdProvider,
                        mockTextureTilesPerWidthProvider, mockTextureXOffsetProvider,
                        mockTextureTilesPerHeightProvider, mockTextureYOffsetProvider,
                        ON_PRESS_CONSUMERS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, null, mockContainingComponent,
                        mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new RectangleRenderableImpl(mockTopLeftColorProvider,
                        mockTopRightColorProvider, mockBottomRightColorProvider,
                        mockBottomLeftColorProvider, mockBackgroundTextureIdProvider,
                        mockTextureTilesPerWidthProvider, mockTextureXOffsetProvider,
                        mockTextureTilesPerHeightProvider, mockTextureYOffsetProvider,
                        ON_PRESS_CONSUMERS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent, null,
                        mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new RectangleRenderableImpl(mockTopLeftColorProvider,
                        mockTopRightColorProvider, mockBottomRightColorProvider,
                        mockBottomLeftColorProvider, mockBackgroundTextureIdProvider,
                        mockTextureTilesPerWidthProvider, mockTextureXOffsetProvider,
                        mockTextureTilesPerHeightProvider, mockTextureYOffsetProvider,
                        ON_PRESS_CONSUMERS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries, null));
    }

    @Test
    public void testConstructorDoesNotAddSelfToContainingComponent() {
        verify(mockContainingComponent, never()).add(renderable);
    }

    @Test
    public void testSetAndGetTopLeftColorProvider() {
        assertSame(mockTopLeftColorProvider, renderable.getTopLeftColorProvider());

        renderable.setTopLeftColorProvider(newColorProvider);

        assertSame(newColorProvider, renderable.getTopLeftColorProvider());
    }

    @Test
    public void testSetAndGetTopRightColorProvider() {
        assertSame(mockTopRightColorProvider, renderable.getTopRightColorProvider());

        renderable.setTopRightColorProvider(newColorProvider);

        assertSame(newColorProvider, renderable.getTopRightColorProvider());
    }

    @Test
    public void testSetAndGetBottomRightColorProvider() {
        assertSame(mockBottomRightColorProvider,
                renderable.getBottomRightColorProvider());

        renderable.setBottomRightColorProvider(newColorProvider);

        assertSame(newColorProvider, renderable.getBottomRightColorProvider());
    }

    @Test
    public void testSetAndGetBottomLeftColorProvider() {
        assertSame(mockBottomLeftColorProvider, renderable.getBottomLeftColorProvider());

        renderable.setBottomLeftColorProvider(newColorProvider);

        assertSame(newColorProvider, renderable.getBottomLeftColorProvider());
    }

    @Test
    public void testSetAndGetBackgroundTextureIdProvider() {
        assertSame(mockBackgroundTextureIdProvider, renderable.getTextureIdProvider());

        @SuppressWarnings("unchecked") var newProvider =
                (ProviderAtTime<Integer>) mock(ProviderAtTime.class);

        renderable.setTextureIdProvider(newProvider);

        assertSame(newProvider, renderable.getTextureIdProvider());
    }

    @Test
    public void testSetAndGetBackgroundTextureTileWidthProvider() {
        assertEquals(mockTextureTilesPerWidthProvider,
                renderable.getTextureTilesPerWidthProvider());

        @SuppressWarnings("unchecked") var newProvider =
                (ProviderAtTime<Float>) mock(ProviderAtTime.class);

        renderable.setTextureTilesPerWidthProvider(newProvider);

        assertEquals(newProvider, renderable.getTextureTilesPerWidthProvider());
    }

    @Test
    public void testSetAndGetBackgroundTextureTileHeightProvider() {
        assertEquals(mockTextureTilesPerHeightProvider,
                renderable.getTextureTilesPerHeightProvider());

        @SuppressWarnings("unchecked") var newProvider =
                (ProviderAtTime<Float>) mock(ProviderAtTime.class);

        renderable.setTextureTilesPerHeightProvider(newProvider);

        assertEquals(newProvider, renderable.getTextureTilesPerHeightProvider());
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
                renderableNotSupportingMouseEvents.setOnPress(2,
                        consumer(randomString(), _ -> {})));

        renderable.setOnPress(2, mockOnPressAction);

        renderable.press(2, TIMESTAMP);

        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(TIMESTAMP);
        verify(mockOnPressAction, once()).accept(
                eq(eventInputs(TIMESTAMP).withMouseEvent(2, PRESS, renderable,
                        mockContainingComponent)));

        //noinspection unchecked
        Consumer<EventInputs> newOnPress = mock(Consumer.class);
        renderable.setOnPress(2, newOnPress);

        renderable.press(2, TIMESTAMP + 1);

        verify(newOnPress, once()).accept(
                eq(eventInputs(TIMESTAMP + 1).withMouseEvent(2, PRESS, renderable,
                        mockContainingComponent)));

        renderable.press(0, TIMESTAMP + 2);

        verify(newOnPress, once()).accept(any());
    }

    @Test
    public void testPressActionIds() {
        var id1 = "id1";
        var id2 = "id2";
        var id3 = "id3";

        renderable.setOnPress(0, consumer(id1, _ -> {}));
        renderable.setOnPress(2, consumer(id2, _ -> {}));
        renderable.setOnPress(7, consumer(id3, _ -> {}));
        renderable.setOnPress(2, null);

        Map<Integer, String> pressConsumerIds = renderable.pressConsumerIds();

        assertNotNull(pressConsumerIds);
        assertEquals(2, pressConsumerIds.size());
        assertEquals(id1, pressConsumerIds.get(0));
        assertEquals(id3, pressConsumerIds.get(7));
    }

    @Test
    public void testReleaseAndSetOnRelease() {
        assertThrows(UnsupportedOperationException.class, () ->
                renderableNotSupportingMouseEvents.release(2, 0L));
        assertThrows(UnsupportedOperationException.class, () ->
                renderableNotSupportingMouseEvents.setOnRelease(2,
                        consumer(randomString(), _ -> {})));

        renderable.release(2, TIMESTAMP);

        //noinspection unchecked
        Consumer<EventInputs> newOnRelease = mock(Consumer.class);
        renderable.setOnRelease(2, newOnRelease);

        renderable.release(2, TIMESTAMP + 1);

        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(TIMESTAMP);
        verify(newOnRelease, once()).accept(
                eq(eventInputs(TIMESTAMP + 1).withMouseEvent(2, RELEASE, renderable,
                        mockContainingComponent)));
    }

    @Test
    public void testReleaseActionIds() {
        String id1 = randomString();
        String id2 = randomString();
        String id3 = randomString();

        renderable.setOnRelease(0, consumer(id1, _ -> {}));
        renderable.setOnRelease(2, consumer(id2, _ -> {}));
        renderable.setOnRelease(7, consumer(id3, _ -> {}));
        renderable.setOnRelease(2, null);

        Map<Integer, String> releaseConsumerIds =
                renderable.releaseConsumerIds();

        assertNotNull(releaseConsumerIds);
        assertEquals(2, releaseConsumerIds.size());
        assertEquals(id1, releaseConsumerIds.get(0));
        assertEquals(id3, releaseConsumerIds.get(7));
    }

    @Test
    public void testPressOrReleaseMethodsWithInvalidButtons() {
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setOnPress(-1, consumer(randomString(), _ -> {})));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setOnRelease(-1, consumer(randomString(), _ -> {})));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.press(-1, TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.press(-1, TIMESTAMP + 1));

        assertThrows(IllegalArgumentException.class, () ->
                renderable.setOnPress(8, consumer(randomString(), _ -> {})));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setOnRelease(8, consumer(randomString(), _ -> {})));
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
        verify(mockOnMouseOverAction, once()).accept(
                eq(eventInputs(TIMESTAMP).withMouseEvent(null, MOUSE_OVER, renderable,
                        mockContainingComponent)));

        //noinspection unchecked
        Consumer<EventInputs> newOnMouseOver = mock(Consumer.class);
        renderable.setOnMouseOver(newOnMouseOver);

        renderable.mouseOver(TIMESTAMP + 1);

        verify(newOnMouseOver, once()).accept(
                eq(eventInputs(TIMESTAMP + 1).withMouseEvent(null, MOUSE_OVER, renderable,
                        mockContainingComponent)));
    }

    @Test
    public void testMouseOverActionId() {
        var mouseOverConsumerId = randomString();

        assertThrows(UnsupportedOperationException.class, () ->
                renderableNotSupportingMouseEvents.mouseOverConsumerId());

        renderable.setOnMouseOver(null);

        assertNull(renderable.mouseOverConsumerId());

        renderable.setOnMouseOver(consumer(mouseOverConsumerId, _ -> {}));

        assertEquals(mouseOverConsumerId, renderable.mouseOverConsumerId());
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
        verify(mockOnMouseLeaveAction, once()).accept(
                eq(eventInputs(TIMESTAMP).withMouseEvent(null, MOUSE_LEAVE, renderable,
                        mockContainingComponent)));

        //noinspection unchecked
        Consumer<EventInputs> newOnMouseLeave = mock(Consumer.class);
        renderable.setOnMouseLeave(newOnMouseLeave);

        renderable.mouseLeave(TIMESTAMP + 1);

        verify(newOnMouseLeave, once()).accept(
                eq(eventInputs(TIMESTAMP + 1).withMouseEvent(null, MOUSE_LEAVE, renderable,
                        mockContainingComponent)));
    }

    @Test
    public void testMouseLeaveActionId() {
        var mouseLeaveConsumerId = randomString();

        assertThrows(UnsupportedOperationException.class,
                () -> renderableNotSupportingMouseEvents.mouseLeaveConsumerId());

        renderable.setOnMouseLeave(null);

        assertNull(renderable.mouseLeaveConsumerId());

        renderable.setOnMouseLeave(consumer(mouseLeaveConsumerId, _ -> {}));

        assertEquals(mouseLeaveConsumerId, renderable.mouseLeaveConsumerId());
    }

    @Test
    public void testGetAndSetRenderingAreaProvider() {
        assertSame(RENDERING_AREA_PROVIDER,
                renderable.getRenderingDimensionsProvider());

        @SuppressWarnings("unchecked") var newRenderingDimensionsProvider =
                (ProviderAtTime<FloatBox>) mock(ProviderAtTime.class);

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
