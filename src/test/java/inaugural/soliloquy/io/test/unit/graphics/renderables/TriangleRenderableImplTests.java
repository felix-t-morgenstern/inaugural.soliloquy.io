package inaugural.soliloquy.io.test.unit.graphics.renderables;

import inaugural.soliloquy.io.graphics.renderables.TriangleRenderableImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeAction;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeProviderAtTime;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeStaticProvider;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.entities.Action;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;
import static soliloquy.specs.ui.EventInputs.inputs;

@ExtendWith(MockitoExtension.class)
public class TriangleRenderableImplTests {
    private final ProviderAtTime<Color> VERTEX_1_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> VERTEX_2_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> VERTEX_3_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Integer> BACKGROUND_TEXTURE_ID_PROVIDER =
            new FakeProviderAtTime<>();
    private final Map<Integer, Action<EventInputs>> ON_PRESS_ACTIONS = mapOf();
    private final FakeProviderAtTime<Vertex> VERTEX_1_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Vertex> VERTEX_2_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Vertex> VERTEX_3_PROVIDER = new FakeProviderAtTime<>();
    private final int Z = randomInt();
    private final long TIMESTAMP = randomLong();

    private final java.util.UUID UUID = java.util.UUID.randomUUID();

    @Mock private ProviderAtTime<Float> mockTextureTileWidthProvider;
    @Mock private ProviderAtTime<Float> mockTextureTileHeightProvider;
    @Mock private Component mockContainingComponent;
    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private TimestampValidator mockTimestampValidator;
    @Mock private Action<EventInputs> mockOnPressAction;
    @Mock private Action<EventInputs> mockOnMouseOverAction;
    @Mock private Action<EventInputs> mockOnMouseLeaveAction;

    private TriangleRenderable renderable;
    private TriangleRenderable renderableNotSupportingMouseEvents;

    @BeforeEach
    public void setUp() {
        lenient().when(mockRenderingBoundaries.currentBoundaries()).thenReturn(WHOLE_SCREEN);

        renderable = new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator
        );
        renderable.setCapturesMouseEvents(true);

        renderableNotSupportingMouseEvents = new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator
        );
        renderableNotSupportingMouseEvents.setCapturesMouseEvents(false);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                null, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, null,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                null, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, null,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                null, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, null,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                null,
                mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                null, mockTextureTileHeightProvider,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                mockTextureTileWidthProvider, null,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, null, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingComponent, null, mockTimestampValidator
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingComponent, mockRenderingBoundaries, null
        ));
    }

    @Test
    public void testSetAndGetVertexProviders() {
        var provider1 = new FakeStaticProvider<>(vertexOf(0f, 0f));
        var provider2 = new FakeStaticProvider<>(vertexOf(0f, 0f));
        var provider3 = new FakeStaticProvider<>(vertexOf(0f, 0f));

        renderable.setVertex1Provider(provider1);
        renderable.setVertex2Provider(provider2);
        renderable.setVertex3Provider(provider3);

        assertSame(provider1, renderable.getVertex1Provider());
        assertSame(provider2, renderable.getVertex2Provider());
        assertSame(provider3, renderable.getVertex3Provider());
    }

    @Test
    public void testSetAndGetVertexColorProviders() {
        var provider1 = new FakeStaticProvider<>(Color.BLACK);
        var provider2 = new FakeStaticProvider<>(Color.BLACK);
        var provider3 = new FakeStaticProvider<>(Color.BLACK);

        renderable.setVertex1ColorProvider(provider1);
        renderable.setVertex2ColorProvider(provider2);
        renderable.setVertex3ColorProvider(provider3);

        assertSame(provider1, renderable.getVertex1ColorProvider());
        assertSame(provider2, renderable.getVertex2ColorProvider());
        assertSame(provider3, renderable.getVertex3ColorProvider());
    }

    @Test
    public void testSetAndGetBackgroundTextureIdProvider() {
        assertSame(BACKGROUND_TEXTURE_ID_PROVIDER,
                renderable.getTextureIdProvider());

        FakeProviderAtTime<Integer> newProvider = new FakeProviderAtTime<>();

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
                renderableNotSupportingMouseEvents.setOnPress(2, new FakeAction<>()));

        renderable.setOnPress(2, mockOnPressAction);

        renderable.press(2, TIMESTAMP);

        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(TIMESTAMP);
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
        var id1 = randomString();
        var id2 = randomString();
        var id3 = randomString();

        renderable.setOnPress(0, new FakeAction<>(id1));
        renderable.setOnPress(2, new FakeAction<>(id2));
        renderable.setOnPress(7, new FakeAction<>(id3));
        renderable.setOnPress(2, null);

        var pressActionIds = renderable.pressActionIds();

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
                renderableNotSupportingMouseEvents.setOnRelease(2, new FakeAction<>()));

        renderable.release(2, TIMESTAMP);

        //noinspection unchecked
        Action<EventInputs> newOnRelease = mock(Action.class);
        renderable.setOnRelease(2, newOnRelease);

        renderable.release(2, TIMESTAMP + 1);

        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(TIMESTAMP);
        verify(newOnRelease, once()).run(
                eq(inputs(TIMESTAMP + 1, renderable)));
    }

    @Test
    public void testReleaseActionIds() {
        var id1 = randomString();
        var id2 = randomString();
        var id3 = randomString();

        renderable.setOnRelease(0, new FakeAction<>(id1));
        renderable.setOnRelease(2, new FakeAction<>(id2));
        renderable.setOnRelease(7, new FakeAction<>(id3));
        renderable.setOnRelease(2, null);

        var releaseActionIds = renderable.releaseActionIds();

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
                renderableNotSupportingMouseEvents.mouseOver(0L));
        assertThrows(UnsupportedOperationException.class, () ->
                renderableNotSupportingMouseEvents.setOnMouseOver(mockOnMouseOverAction));

        renderable.mouseOver(TIMESTAMP);

        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(TIMESTAMP);
        verify(mockOnMouseOverAction, once()).run(eq(inputs(TIMESTAMP, renderable)));

        //noinspection unchecked
        Action<EventInputs> newOnMouseOver = mock(Action.class);
        renderable.setOnMouseOver(newOnMouseOver);

        renderable.mouseOver(TIMESTAMP + 1);

        verify(newOnMouseOver, once()).run(eq(inputs(TIMESTAMP + 1, renderable)));
    }

    @Test
    public void testMouseOverActionId() {
        var mouseOverActionId = randomString();

        assertThrows(UnsupportedOperationException.class, () ->
                renderableNotSupportingMouseEvents.mouseOverActionId());

        renderable.setOnMouseOver(null);

        assertNull(renderable.mouseOverActionId());

        renderable.setOnMouseOver(new FakeAction<>(mouseOverActionId));

        assertEquals(mouseOverActionId, renderable.mouseOverActionId());
    }

    @Test
    public void testMouseLeaveAndSetOnMouseLeave() {
        assertThrows(UnsupportedOperationException.class, () ->
                renderableNotSupportingMouseEvents.mouseLeave(0L));
        assertThrows(UnsupportedOperationException.class, () ->
                renderableNotSupportingMouseEvents.setOnMouseLeave(mockOnMouseLeaveAction));

        renderable.mouseLeave(TIMESTAMP);

        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(TIMESTAMP);
        verify(mockOnMouseLeaveAction, once()).run(eq(inputs(TIMESTAMP, renderable)));

        //noinspection unchecked
        Action<EventInputs> newOnMouseLeave = mock(Action.class);
        renderable.setOnMouseLeave(newOnMouseLeave);

        renderable.mouseLeave(TIMESTAMP + 1);

        verify(newOnMouseLeave, once()).run(eq(inputs(TIMESTAMP + 1, renderable)));
    }

    @Test
    public void testMouseLeaveActionId() {
        var mouseLeaveActionId = "mouseLeaveActionId";

        assertThrows(UnsupportedOperationException.class, () ->
                renderableNotSupportingMouseEvents.mouseLeaveActionId());

        renderable.setOnMouseLeave(null);

        assertNull(renderable.mouseLeaveActionId());

        renderable.setOnMouseLeave(new FakeAction<>(mouseLeaveActionId));

        assertEquals(mouseLeaveActionId, renderable.mouseLeaveActionId());
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
        renderable.setVertex1Provider(
                new FakeStaticProvider<>(vertexOf(0.5f, 0f))
        );
        renderable.setVertex2Provider(
                new FakeStaticProvider<>(vertexOf(0f, .5f))
        );
        renderable.setVertex3Provider(
                new FakeStaticProvider<>(vertexOf(1f, .5f))
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
    public void testDelete() {
        renderable.delete();

        assertNull(renderable.component());
        assertTrue(renderable.isDeleted());
    }

    @Test
    public void testUuid() {
        assertSame(UUID, renderable.uuid());
    }

    @Test
    public void testComponent() {
        assertSame(mockContainingComponent, renderable.component());
    }

    @Test
    public void testSetComponent() {
        ((TriangleRenderableImpl) renderable).setComponent(null);

        assertNull(renderable.component());
    }
}
