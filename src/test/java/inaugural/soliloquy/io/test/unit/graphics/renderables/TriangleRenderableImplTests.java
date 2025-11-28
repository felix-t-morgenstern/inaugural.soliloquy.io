package inaugural.soliloquy.io.test.unit.graphics.renderables;

import com.google.common.primitives.Floats;
import inaugural.soliloquy.io.graphics.renderables.TriangleRenderableImpl;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.entities.Consumer;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.TriangleRenderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.ui.EventInputs;

import java.awt.*;
import java.util.Map;

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
import static soliloquy.specs.io.input.mouse.MouseEventHandler.EventType.*;
import static soliloquy.specs.ui.EventInputs.eventInputs;

@ExtendWith(MockitoExtension.class)
public class TriangleRenderableImplTests {
    private final Map<Integer, Consumer<EventInputs>> ON_PRESS_CONSUMERS = mapOf();
    private final int Z = randomInt();
    private final long TIMESTAMP = randomLong();

    private final java.util.UUID UUID = java.util.UUID.randomUUID();

    @Mock private ProviderAtTime<Integer> mockBackgroundTextureIdProvider;
    @Mock private ProviderAtTime<Vertex> mockVertex1Provider;
    @Mock private ProviderAtTime<Vertex> mockVertex2Provider;
    @Mock private ProviderAtTime<Vertex> mockVertex3Provider;
    @Mock private ProviderAtTime<Color> mockVertex1ColorProvider;
    @Mock private ProviderAtTime<Color> mockVertex2ColorProvider;
    @Mock private ProviderAtTime<Color> mockVertex3ColorProvider;
    @Mock private ProviderAtTime<Float> mockTextureTileWidthProvider;
    @Mock private ProviderAtTime<Float> mockTextureTileHeightProvider;
    @Mock private Component mockContainingComponent;
    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private TimestampValidator mockTimestampValidator;
    @Mock private Consumer<EventInputs> mockOnPressAction;
    @Mock private Consumer<EventInputs> mockOnMouseOverAction;
    @Mock private Consumer<EventInputs> mockOnMouseLeaveAction;

    private TriangleRenderable renderable;
    private TriangleRenderable renderableNotSupportingMouseEvents;

    @BeforeEach
    public void setUp() {
        lenient().when(mockRenderingBoundaries.currentBoundaries()).thenReturn(WHOLE_SCREEN);

        renderable = new TriangleRenderableImpl(
                mockVertex1Provider, mockVertex1ColorProvider,
                mockVertex2Provider, mockVertex2ColorProvider,
                mockVertex3Provider, mockVertex3ColorProvider,
                mockBackgroundTextureIdProvider,
                mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                ON_PRESS_CONSUMERS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator
        );
        renderable.setCapturesMouseEvents(true);

        renderableNotSupportingMouseEvents = new TriangleRenderableImpl(
                mockVertex1Provider, mockVertex1ColorProvider,
                mockVertex2Provider, mockVertex2ColorProvider,
                mockVertex3Provider, mockVertex3ColorProvider,
                mockBackgroundTextureIdProvider,
                mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                ON_PRESS_CONSUMERS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator
        );
        renderableNotSupportingMouseEvents.setCapturesMouseEvents(false);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                null, mockVertex1ColorProvider,
                mockVertex2Provider, mockVertex2ColorProvider,
                mockVertex3Provider, mockVertex3ColorProvider,
                mockBackgroundTextureIdProvider,
                mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                ON_PRESS_CONSUMERS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                mockVertex1Provider, null,
                mockVertex2Provider, mockVertex2ColorProvider,
                mockVertex3Provider, mockVertex3ColorProvider,
                mockBackgroundTextureIdProvider,
                mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                ON_PRESS_CONSUMERS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                mockVertex1Provider, mockVertex1ColorProvider,
                null, mockVertex2ColorProvider,
                mockVertex3Provider, mockVertex3ColorProvider,
                mockBackgroundTextureIdProvider,
                mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                ON_PRESS_CONSUMERS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                mockVertex1Provider, mockVertex1ColorProvider,
                mockVertex2Provider, null,
                mockVertex3Provider, mockVertex3ColorProvider,
                mockBackgroundTextureIdProvider,
                mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                ON_PRESS_CONSUMERS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                mockVertex1Provider, mockVertex1ColorProvider,
                mockVertex2Provider, mockVertex2ColorProvider,
                null, mockVertex3ColorProvider,
                mockBackgroundTextureIdProvider,
                mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                ON_PRESS_CONSUMERS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                mockVertex1Provider, mockVertex1ColorProvider,
                mockVertex2Provider, mockVertex2ColorProvider,
                mockVertex3Provider, null,
                mockBackgroundTextureIdProvider,
                mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                ON_PRESS_CONSUMERS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                mockVertex1Provider, mockVertex1ColorProvider,
                mockVertex2Provider, mockVertex2ColorProvider,
                mockVertex3Provider, mockVertex3ColorProvider,
                null,
                mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                ON_PRESS_CONSUMERS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                mockVertex1Provider, mockVertex1ColorProvider,
                mockVertex2Provider, mockVertex2ColorProvider,
                mockVertex3Provider, mockVertex3ColorProvider,
                mockBackgroundTextureIdProvider,
                null, mockTextureTileHeightProvider,
                ON_PRESS_CONSUMERS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                mockVertex1Provider, mockVertex1ColorProvider,
                mockVertex2Provider, mockVertex2ColorProvider,
                mockVertex3Provider, mockVertex3ColorProvider,
                mockBackgroundTextureIdProvider,
                mockTextureTileWidthProvider, null,
                ON_PRESS_CONSUMERS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                mockVertex1Provider, mockVertex1ColorProvider,
                mockVertex2Provider, mockVertex2ColorProvider,
                mockVertex3Provider, mockVertex3ColorProvider,
                mockBackgroundTextureIdProvider,
                mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                ON_PRESS_CONSUMERS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, null, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                mockVertex1Provider, mockVertex1ColorProvider,
                mockVertex2Provider, mockVertex2ColorProvider,
                mockVertex3Provider, mockVertex3ColorProvider,
                mockBackgroundTextureIdProvider,
                mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                ON_PRESS_CONSUMERS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingComponent, null, mockTimestampValidator
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                mockVertex1Provider, mockVertex1ColorProvider,
                mockVertex2Provider, mockVertex2ColorProvider,
                mockVertex3Provider, mockVertex3ColorProvider,
                mockBackgroundTextureIdProvider,
                mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                ON_PRESS_CONSUMERS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingComponent, mockRenderingBoundaries, null
        ));
    }

    @Test
    public void testConstructorDoesNotAddSelfToContainingComponent() {
        verify(mockContainingComponent, never()).add(renderable);
    }

    @Test
    public void testSetAndGetVertexProviders() {
        var provider1 = generateMockStaticProvider(vertexOf(0f, 0f));
        var provider2 = generateMockStaticProvider(vertexOf(0f, 0f));
        var provider3 = generateMockStaticProvider(vertexOf(0f, 0f));

        renderable.setVertex1Provider(provider1);
        renderable.setVertex2Provider(provider2);
        renderable.setVertex3Provider(provider3);

        assertSame(provider1, renderable.getVertex1Provider());
        assertSame(provider2, renderable.getVertex2Provider());
        assertSame(provider3, renderable.getVertex3Provider());
    }

    @Test
    public void testSetAndGetVertexColorProviders() {
        var provider1 = generateMockStaticProvider(Color.BLACK);
        var provider2 = generateMockStaticProvider(Color.BLACK);
        var provider3 = generateMockStaticProvider(Color.BLACK);

        renderable.setVertex1ColorProvider(provider1);
        renderable.setVertex2ColorProvider(provider2);
        renderable.setVertex3ColorProvider(provider3);

        assertSame(provider1, renderable.getVertex1ColorProvider());
        assertSame(provider2, renderable.getVertex2ColorProvider());
        assertSame(provider3, renderable.getVertex3ColorProvider());
    }

    @Test
    public void testSetAndGetBackgroundTextureIdProvider() {
        assertSame(mockBackgroundTextureIdProvider,
                renderable.getTextureIdProvider());

        @SuppressWarnings("unchecked")
        var newProvider = (ProviderAtTime<Integer>) mock(ProviderAtTime.class);

        renderable.setTextureIdProvider(newProvider);

        assertSame(newProvider, renderable.getTextureIdProvider());
    }

    @Test
    public void testSetAndGetBackgroundTextureTileWidthProvider() {
        assertEquals(mockTextureTileWidthProvider,
                renderable.getTextureTileWidthProvider());

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
    public void testSetVertexProvidersWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setVertex1Provider(null));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setVertex2Provider(null));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setVertex3Provider(null));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setVertex1ColorProvider(null));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setVertex2ColorProvider(null));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setVertex3ColorProvider(null));
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
                renderableNotSupportingMouseEvents.setOnPress(2, consumer(randomString(), _ -> {})));

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
        var id1 = randomString();
        var id2 = randomString();
        var id3 = randomString();

        renderable.setOnPress(0, consumer(id1, _ -> {}));
        renderable.setOnPress(2, consumer(id2, _ -> {}));
        renderable.setOnPress(7, consumer(id3, _ -> {}));
        renderable.setOnPress(2, null);

        var pressConsumerIds = renderable.pressConsumerIds();

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
        var id1 = randomString();
        var id2 = randomString();
        var id3 = randomString();

        renderable.setOnRelease(0, consumer(id1, _ -> {}));
        renderable.setOnRelease(2, consumer(id2, _ -> {}));
        renderable.setOnRelease(7, consumer(id3, _ -> {}));
        renderable.setOnRelease(2, null);

        var releaseConsumerIds = renderable.releaseConsumerIds();

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
                renderableNotSupportingMouseEvents.setOnMouseLeave(mockOnMouseLeaveAction));

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
        var mouseLeaveConsumerId = "mouseLeaveConsumerId";

        assertThrows(UnsupportedOperationException.class, () ->
                renderableNotSupportingMouseEvents.mouseLeaveConsumerId());

        renderable.setOnMouseLeave(null);

        assertNull(renderable.mouseLeaveConsumerId());

        renderable.setOnMouseLeave(consumer(mouseLeaveConsumerId, _ -> {}));

        assertEquals(mouseLeaveConsumerId, renderable.mouseLeaveConsumerId());
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
        renderable.setVertex1Provider(
                generateMockStaticProvider(vertexOf(0.5f, 0f))
        );
        renderable.setVertex2Provider(
                generateMockStaticProvider(vertexOf(0f, .5f))
        );
        renderable.setVertex3Provider(
                generateMockStaticProvider(vertexOf(1f, .5f))
        );

        assertFalse(renderable.capturesMouseEventAtPoint(vertexOf(0f, 0f), TIMESTAMP));
        assertFalse(renderable.capturesMouseEventAtPoint(vertexOf(1f, 0f), TIMESTAMP));
        assertFalse(renderable.capturesMouseEventAtPoint(vertexOf(0.55f, 0f), TIMESTAMP));
        assertTrue(renderable.capturesMouseEventAtPoint(vertexOf(0.55f, 0.4f), TIMESTAMP));
        assertTrue(renderable.capturesMouseEventAtPoint(vertexOf(0.26f, 0.25f), TIMESTAMP));
        assertFalse(
                renderable.capturesMouseEventAtPoint(vertexOf(0.24f, 0.25f), TIMESTAMP));
        assertTrue(renderable.capturesMouseEventAtPoint(vertexOf(0.74f, 0.25f), TIMESTAMP));
        assertFalse(
                renderable.capturesMouseEventAtPoint(vertexOf(0.76f, 0.25f), TIMESTAMP));
    }

    @Test
    public void testGetRenderingBoundariesProvider() {
        var vertex1 = randomVertex();
        when(mockVertex1Provider.provide(anyLong())).thenReturn(vertex1);
        var vertex2 = randomVertex();
        when(mockVertex2Provider.provide(anyLong())).thenReturn(vertex2);
        var vertex3 = randomVertex();
        when(mockVertex3Provider.provide(anyLong())).thenReturn(vertex3);
        var leftX = Floats.min(vertex1.X, vertex2.X, vertex3.X);
        var topY = Floats.min(vertex1.Y, vertex2.Y, vertex3.Y);
        var rightX = Floats.max(vertex1.X, vertex2.X, vertex3.X);
        var bottomY = Floats.max(vertex1.Y, vertex2.Y, vertex3.Y);
        var expected = floatBoxOf(leftX, topY, rightX, bottomY);

        var provider = renderable.getRenderingDimensionsProvider();
        var provided = provider.provide(randomLong());

        assertNotNull(provided);
        assertEquals(expected, provided);
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

    @Test
    public void testComponent() {
        assertSame(mockContainingComponent, renderable.containingComponent());
    }

    @Test
    public void testSetComponent() {
        ((TriangleRenderableImpl) renderable).setContainingComponent(null);

        assertNull(renderable.containingComponent());
    }
}
