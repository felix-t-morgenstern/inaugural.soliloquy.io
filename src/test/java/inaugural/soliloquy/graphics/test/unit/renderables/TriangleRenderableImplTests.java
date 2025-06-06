package inaugural.soliloquy.graphics.test.unit.renderables;

import inaugural.soliloquy.graphics.renderables.TriangleRenderableImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeAction;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeProviderAtTime;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeStaticProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.RenderableWithMouseEvents.MouseEventInputs;
import soliloquy.specs.graphics.renderables.TriangleRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;

import java.awt.*;
import java.util.Map;

import static inaugural.soliloquy.graphics.api.Constants.WHOLE_SCREEN;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

public class TriangleRenderableImplTests {
    private final ProviderAtTime<Color> VERTEX_1_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> VERTEX_2_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> VERTEX_3_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Integer> BACKGROUND_TEXTURE_ID_PROVIDER =
            new FakeProviderAtTime<>();
    private final float BACKGROUND_TEXTURE_TILE_WIDTH = 0.123f;
    private final float BACKGROUND_TEXTURE_TILE_HEIGHT = 0.456f;
    private final Map<Integer, Action<MouseEventInputs>> ON_PRESS_ACTIONS = mapOf();
    private final FakeProviderAtTime<Vertex> VERTEX_1_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Vertex> VERTEX_2_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Vertex> VERTEX_3_PROVIDER = new FakeProviderAtTime<>();
    private final int Z = randomInt();
    private final long TIMESTAMP = randomLong();

    private final java.util.UUID UUID = java.util.UUID.randomUUID();
    @Mock private RenderableStack mockContainingStack;
    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private Action<MouseEventInputs> mockOnPressAction;
    @Mock private Action<MouseEventInputs> mockOnMouseOverAction;
    @Mock private Action<MouseEventInputs> mockOnMouseLeaveAction;

    private TriangleRenderable triangleRenderable;
    private TriangleRenderable triangleRenderableNotSupportingMouseEvents;

    @BeforeEach
    public void setUp() {
        mockContainingStack = mock(RenderableStack.class);
        mockRenderingBoundaries = mock(RenderingBoundaries.class);
        when(mockRenderingBoundaries.currentBoundaries()).thenReturn(WHOLE_SCREEN);

        //noinspection unchecked
        mockOnPressAction = mock(Action.class);
        //noinspection unchecked
        mockOnMouseOverAction = mock(Action.class);
        //noinspection unchecked
        mockOnMouseLeaveAction = mock(Action.class);

        triangleRenderable = new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingStack, mockRenderingBoundaries
        );
        triangleRenderable.setCapturesMouseEvents(true);

        triangleRenderableNotSupportingMouseEvents = new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingStack, mockRenderingBoundaries
        );
        triangleRenderableNotSupportingMouseEvents.setCapturesMouseEvents(false);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                null, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingStack, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, null,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingStack, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                null, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingStack, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, null,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingStack, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                null, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingStack, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, null,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingStack, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                null,
                BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingStack, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                0f, BACKGROUND_TEXTURE_TILE_HEIGHT,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingStack, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                BACKGROUND_TEXTURE_TILE_WIDTH, 0f,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingStack, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, null, mockContainingStack, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, null, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                BACKGROUND_TEXTURE_ID_PROVIDER,
                BACKGROUND_TEXTURE_TILE_WIDTH, BACKGROUND_TEXTURE_TILE_HEIGHT,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                Z, UUID, mockContainingStack, null
        ));
    }

    @Test
    public void testSetAndGetVertexProviders() {
        var provider1 = new FakeStaticProvider<>(vertexOf(0f, 0f));
        var provider2 = new FakeStaticProvider<>(vertexOf(0f, 0f));
        var provider3 = new FakeStaticProvider<>(vertexOf(0f, 0f));

        triangleRenderable.setVertex1Provider(provider1);
        triangleRenderable.setVertex2Provider(provider2);
        triangleRenderable.setVertex3Provider(provider3);

        assertSame(provider1, triangleRenderable.getVertex1Provider());
        assertSame(provider2, triangleRenderable.getVertex2Provider());
        assertSame(provider3, triangleRenderable.getVertex3Provider());
    }

    @Test
    public void testSetAndGetVertexColorProviders() {
        var provider1 = new FakeStaticProvider<>(Color.BLACK);
        var provider2 = new FakeStaticProvider<>(Color.BLACK);
        var provider3 = new FakeStaticProvider<>(Color.BLACK);

        triangleRenderable.setVertex1ColorProvider(provider1);
        triangleRenderable.setVertex2ColorProvider(provider2);
        triangleRenderable.setVertex3ColorProvider(provider3);

        assertSame(provider1, triangleRenderable.getVertex1ColorProvider());
        assertSame(provider2, triangleRenderable.getVertex2ColorProvider());
        assertSame(provider3, triangleRenderable.getVertex3ColorProvider());
    }

    @Test
    public void testSetAndGetBackgroundTextureIdProvider() {
        assertSame(BACKGROUND_TEXTURE_ID_PROVIDER,
                triangleRenderable.getBackgroundTextureIdProvider());

        FakeProviderAtTime<Integer> newProvider = new FakeProviderAtTime<>();

        triangleRenderable.setBackgroundTextureIdProvider(newProvider);

        assertSame(newProvider, triangleRenderable.getBackgroundTextureIdProvider());
    }

    @Test
    public void testSetAndGetBackgroundTextureTileWidth() {
        assertEquals(BACKGROUND_TEXTURE_TILE_WIDTH,
                triangleRenderable.getBackgroundTextureTileWidth());

        var newWidth = 0.1312f;

        triangleRenderable.setBackgroundTextureTileWidth(newWidth);

        assertEquals(newWidth, triangleRenderable.getBackgroundTextureTileWidth());
    }

    @Test
    public void testSetAndGetBackgroundTextureTileHeight() {
        assertEquals(BACKGROUND_TEXTURE_TILE_HEIGHT,
                triangleRenderable.getBackgroundTextureTileHeight());

        var newHeight = 0.1312f;

        triangleRenderable.setBackgroundTextureTileHeight(newHeight);

        assertEquals(newHeight, triangleRenderable.getBackgroundTextureTileHeight());
    }

    @Test
    public void testSetVertexProvidersWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.setVertex1Provider(null));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.setVertex2Provider(null));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.setVertex3Provider(null));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.setVertex1ColorProvider(null));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.setVertex2ColorProvider(null));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.setVertex3ColorProvider(null));
    }

    @Test
    public void testGetAndSetCapturesMouseEvents() {
        assertTrue(triangleRenderable.getCapturesMouseEvents());

        triangleRenderable.setCapturesMouseEvents(false);

        assertFalse(triangleRenderable.getCapturesMouseEvents());
    }

    @Test
    public void testPressAndSetOnPress() {
        assertThrows(UnsupportedOperationException.class, () ->
                triangleRenderableNotSupportingMouseEvents.press(2, 0L));
        assertThrows(UnsupportedOperationException.class, () ->
                triangleRenderableNotSupportingMouseEvents.setOnPress(2, new FakeAction<>()));

        triangleRenderable.setOnPress(2, mockOnPressAction);

        triangleRenderable.press(2, TIMESTAMP);

        verify(mockOnPressAction, once()).run(
                eq(MouseEventInputs.of(TIMESTAMP, triangleRenderable)));

        //noinspection unchecked
        Action<MouseEventInputs> newOnPress = mock(Action.class);
        triangleRenderable.setOnPress(2, newOnPress);

        triangleRenderable.press(2, TIMESTAMP + 1);

        verify(newOnPress, once()).run(
                eq(MouseEventInputs.of(TIMESTAMP + 1, triangleRenderable)));

        triangleRenderable.press(0, TIMESTAMP + 2);

        verify(newOnPress, once()).run(any());
    }

    @Test
    public void testPressActionIds() {
        var id1 = "id1";
        var id2 = "id2";
        var id3 = "id3";

        triangleRenderable.setOnPress(0, new FakeAction<>(id1));
        triangleRenderable.setOnPress(2, new FakeAction<>(id2));
        triangleRenderable.setOnPress(7, new FakeAction<>(id3));
        triangleRenderable.setOnPress(2, null);

        Map<Integer, String> pressActionIds = triangleRenderable.pressActionIds();

        assertNotNull(pressActionIds);
        assertEquals(2, pressActionIds.size());
        assertEquals(id1, pressActionIds.get(0));
        assertEquals(id3, pressActionIds.get(7));
    }

    @Test
    public void testReleaseAndSetOnRelease() {
        assertThrows(UnsupportedOperationException.class, () ->
                triangleRenderableNotSupportingMouseEvents.release(2, 0L));
        assertThrows(UnsupportedOperationException.class, () ->
                triangleRenderableNotSupportingMouseEvents.setOnRelease(2, new FakeAction<>()));

        triangleRenderable.release(2, TIMESTAMP);

        //noinspection unchecked
        Action<MouseEventInputs> newOnRelease = mock(Action.class);
        triangleRenderable.setOnRelease(2, newOnRelease);

        triangleRenderable.release(2, TIMESTAMP + 1);

        verify(newOnRelease, once()).run(
                eq(MouseEventInputs.of(TIMESTAMP + 1, triangleRenderable)));
    }

    @Test
    public void testReleaseActionIds() {
        var id1 = "id1";
        var id2 = "id2";
        var id3 = "id3";

        triangleRenderable.setOnRelease(0, new FakeAction<>(id1));
        triangleRenderable.setOnRelease(2, new FakeAction<>(id2));
        triangleRenderable.setOnRelease(7, new FakeAction<>(id3));
        triangleRenderable.setOnRelease(2, null);

        Map<Integer, String> releaseActionIds =
                triangleRenderable.releaseActionIds();

        assertNotNull(releaseActionIds);
        assertEquals(2, releaseActionIds.size());
        assertEquals(id1, releaseActionIds.get(0));
        assertEquals(id3, releaseActionIds.get(7));
    }

    @Test
    public void testPressOrReleaseMethodsWithInvalidButtons() {
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.setOnPress(-1, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.setOnRelease(-1, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.press(-1, TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.press(-1, TIMESTAMP + 1));

        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.setOnPress(8, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.setOnRelease(8, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.press(8, TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.press(8, TIMESTAMP + 3));
    }

    @Test
    public void testMouseOverAndSetOnMouseOver() {
        assertThrows(UnsupportedOperationException.class, () ->
                triangleRenderableNotSupportingMouseEvents.mouseOver(0L));
        assertThrows(UnsupportedOperationException.class, () ->
                triangleRenderableNotSupportingMouseEvents.setOnMouseOver(mockOnMouseOverAction));

        triangleRenderable.mouseOver(TIMESTAMP);

        verify(mockOnMouseOverAction, once()).run(
                eq(MouseEventInputs.of(TIMESTAMP, triangleRenderable)));

        //noinspection unchecked
        Action<MouseEventInputs> newOnMouseOver = mock(Action.class);
        triangleRenderable.setOnMouseOver(newOnMouseOver);

        triangleRenderable.mouseOver(TIMESTAMP + 1);

        verify(newOnMouseOver, once()).run(
                eq(MouseEventInputs.of(TIMESTAMP + 1, triangleRenderable)));
    }

    @Test
    public void testMouseOverActionId() {
        var mouseOverActionId = "mouseOverActionId";

        assertThrows(UnsupportedOperationException.class, () ->
                triangleRenderableNotSupportingMouseEvents.mouseOverActionId());

        triangleRenderable.setOnMouseOver(null);

        assertNull(triangleRenderable.mouseOverActionId());

        triangleRenderable.setOnMouseOver(new FakeAction<>(mouseOverActionId));

        assertEquals(mouseOverActionId, triangleRenderable.mouseOverActionId());
    }

    @Test
    public void testMouseLeaveAndSetOnMouseLeave() {
        assertThrows(UnsupportedOperationException.class, () ->
                triangleRenderableNotSupportingMouseEvents.mouseLeave(0L));
        assertThrows(UnsupportedOperationException.class, () ->
                triangleRenderableNotSupportingMouseEvents.setOnMouseLeave(mockOnMouseLeaveAction));

        triangleRenderable.mouseLeave(TIMESTAMP);

        verify(mockOnMouseLeaveAction, once()).run(
                eq(MouseEventInputs.of(TIMESTAMP, triangleRenderable)));

        //noinspection unchecked
        Action<MouseEventInputs> newOnMouseLeave = mock(Action.class);
        triangleRenderable.setOnMouseLeave(newOnMouseLeave);

        triangleRenderable.mouseLeave(TIMESTAMP + 1);

        verify(newOnMouseLeave, once()).run(
                eq(MouseEventInputs.of(TIMESTAMP + 1, triangleRenderable)));
    }

    @Test
    public void testMouseLeaveActionId() {
        var mouseLeaveActionId = "mouseLeaveActionId";

        assertThrows(UnsupportedOperationException.class, () ->
                triangleRenderableNotSupportingMouseEvents.mouseLeaveActionId());

        triangleRenderable.setOnMouseLeave(null);

        assertNull(triangleRenderable.mouseLeaveActionId());

        triangleRenderable.setOnMouseLeave(new FakeAction<>(mouseLeaveActionId));

        assertEquals(mouseLeaveActionId, triangleRenderable.mouseLeaveActionId());
    }

    @Test
    public void testMouseEventCallsToOutdatedTimestamps() {
        triangleRenderable.setVertex1Provider(
                new FakeStaticProvider<>(
                        vertexOf(randomFloatInRange(0f, 1f), randomFloatInRange(0f, 1f)))
        );
        triangleRenderable.setVertex2Provider(
                new FakeStaticProvider<>(
                        vertexOf(randomFloatInRange(0f, 1f), randomFloatInRange(0f, 1f)))
        );
        triangleRenderable.setVertex3Provider(
                new FakeStaticProvider<>(
                        vertexOf(randomFloatInRange(0f, 1f), randomFloatInRange(0f, 1f)))
        );

        triangleRenderable.press(0, TIMESTAMP);
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.press(0, TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.release(0, TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.mouseOver(TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.mouseLeave(TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.capturesMouseEventAtPoint(vertexOf(0f, 0f), TIMESTAMP - 1));

        triangleRenderable.release(0, TIMESTAMP + 1);
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.press(0, TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.release(0, TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.mouseOver(TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.mouseLeave(TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.capturesMouseEventAtPoint(vertexOf(0f, 0f), TIMESTAMP));

        triangleRenderable.mouseOver(TIMESTAMP + 2);
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.press(0, TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.release(0, TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.mouseOver(TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.mouseLeave(TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.capturesMouseEventAtPoint(vertexOf(0f, 0f), TIMESTAMP + 1));

        triangleRenderable.mouseLeave(TIMESTAMP + 3);
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.press(0, TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.release(0, TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.mouseOver(TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.mouseLeave(TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.capturesMouseEventAtPoint(vertexOf(0f, 0f), TIMESTAMP + 2));

        triangleRenderable.capturesMouseEventAtPoint(vertexOf(0f, 0f), TIMESTAMP + 4);
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.press(0, TIMESTAMP + 3));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.release(0, TIMESTAMP + 3));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.mouseOver(TIMESTAMP + 3));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.mouseLeave(TIMESTAMP + 3));
        assertThrows(IllegalArgumentException.class, () ->
                triangleRenderable.capturesMouseEventAtPoint(vertexOf(0f, 0f), TIMESTAMP + 3));
    }

    @Test
    public void testGetAndSetZ() {
        assertEquals(Z, triangleRenderable.getZ());

        int newZ = 456;

        triangleRenderable.setZ(newZ);

        assertEquals(newZ, triangleRenderable.getZ());
        verify(mockContainingStack, once()).add(triangleRenderable);
    }

    @Test
    public void testCapturesMouseEventsAtPoint() {
        triangleRenderable.setVertex1Provider(
                new FakeStaticProvider<>(vertexOf(0.5f, 0f))
        );
        triangleRenderable.setVertex2Provider(
                new FakeStaticProvider<>(vertexOf(0f, .5f))
        );
        triangleRenderable.setVertex3Provider(
                new FakeStaticProvider<>(vertexOf(1f, .5f))
        );

        assertFalse(triangleRenderable.capturesMouseEventAtPoint(vertexOf(0f, 0f), TIMESTAMP));
        assertFalse(triangleRenderable.capturesMouseEventAtPoint(vertexOf(1f, 0f), TIMESTAMP));
        assertFalse(triangleRenderable.capturesMouseEventAtPoint(vertexOf(0.55f, 0f), TIMESTAMP));
        assertTrue(triangleRenderable.capturesMouseEventAtPoint(vertexOf(0.55f, 0.4f), TIMESTAMP));
        assertTrue(triangleRenderable.capturesMouseEventAtPoint(vertexOf(0.26f, 0.25f), TIMESTAMP));
        assertFalse(
                triangleRenderable.capturesMouseEventAtPoint(vertexOf(0.24f, 0.25f), TIMESTAMP));
        assertTrue(triangleRenderable.capturesMouseEventAtPoint(vertexOf(0.74f, 0.25f), TIMESTAMP));
        assertFalse(
                triangleRenderable.capturesMouseEventAtPoint(vertexOf(0.76f, 0.25f), TIMESTAMP));
    }

    @Test
    public void testDelete() {
        triangleRenderable.delete();
        verify(mockContainingStack, once()).remove(triangleRenderable);
    }

    @Test
    public void testUuid() {
        assertSame(UUID, triangleRenderable.uuid());
    }
}
