package inaugural.soliloquy.io.test.unit.graphics.renderables;

import inaugural.soliloquy.io.graphics.renderables.SpriteRenderableImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.io.graphics.renderables.SpriteRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.ui.Component;
import soliloquy.specs.ui.EventInputs;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.io.api.Constants.WHOLE_SCREEN;
import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;
import static soliloquy.specs.ui.EventInputs.inputs;

@ExtendWith(MockitoExtension.class)
public class SpriteRenderableImplTests {
    private final FakeSprite SPRITE_SUPPORTING_MOUSE_EVENTS = new FakeSprite(new FakeImage(true));
    private final FakeSprite SPRITE_NOT_SUPPORTING_MOUSE_EVENTS =
            new FakeSprite(new FakeImage(false));
    private final FakeProviderAtTime<Float> BORDER_THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Color> BORDER_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final Map<Integer, Action<EventInputs>> ON_PRESS_ACTIONS = mapOf();
    private final List<ColorShift> COLOR_SHIFTS = listOf();
    private final FakeStaticProvider<FloatBox> RENDERING_AREA_PROVIDER =
            new FakeStaticProvider<>(null);
    private final int Z = randomInt();
    private final long TIMESTAMP = randomLong();

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private Component mockContainingComponent;
    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private Action<EventInputs> mockOnPressAction;
    @Mock private Action<EventInputs> mockOnMouseOverAction;
    @Mock private Action<EventInputs> mockOnMouseLeaveAction;

    private SpriteRenderable renderableWithMouseEvents;
    private SpriteRenderable renderableWithoutMouseEvents;

    @BeforeEach
    public void setUp() {
        lenient().when(mockRenderingBoundaries.currentBoundaries()).thenReturn(WHOLE_SCREEN);

        renderableWithMouseEvents =
                new SpriteRenderableImpl(SPRITE_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                        mockOnMouseLeaveAction, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries);
        renderableWithoutMouseEvents =
                new SpriteRenderableImpl(SPRITE_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFTS,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries);
    }

    @Test
    public void testConstructorWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(null, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                        mockOnMouseLeaveAction, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(SPRITE_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null,
                        mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries));
        // NB: These following two constructors should not_ throw exceptions
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(SPRITE_SUPPORTING_MOUSE_EVENTS, null,
                        BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                        mockOnMouseLeaveAction, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(SPRITE_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, null, ON_PRESS_ACTIONS, null,
                        mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(SPRITE_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null,
                        mockOnMouseOverAction, mockOnMouseLeaveAction, null,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(SPRITE_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null,
                        mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS, null, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(SPRITE_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null,
                        mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS,
                        RENDERING_AREA_PROVIDER, Z, null, mockContainingComponent,
                        mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(SPRITE_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null,
                        mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                        null));

        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(null, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(SPRITE_NOT_SUPPORTING_MOUSE_EVENTS, null,
                        BORDER_COLOR_PROVIDER, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(SPRITE_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, null, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z,
                        UUID, mockContainingComponent,
                        mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(SPRITE_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, null,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(SPRITE_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFTS, null, Z,
                        UUID, mockContainingComponent,
                        mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(SPRITE_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFTS,
                        RENDERING_AREA_PROVIDER, Z, null, mockContainingComponent,
                        mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(SPRITE_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFTS,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                        null));
    }

    @Test
    public void testGetAndSetSprite() {
        assertSame(SPRITE_SUPPORTING_MOUSE_EVENTS, renderableWithMouseEvents.getSprite());
        assertSame(SPRITE_NOT_SUPPORTING_MOUSE_EVENTS,
                renderableWithoutMouseEvents.getSprite());

        FakeSprite newSprite = new FakeSprite(new FakeImage(true));

        renderableWithMouseEvents.setSprite(newSprite);
        renderableWithoutMouseEvents.setSprite(newSprite);

        assertSame(newSprite, renderableWithMouseEvents.getSprite());
        assertSame(newSprite, renderableWithoutMouseEvents.getSprite());
    }

    @Test
    public void testSetSpriteWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.setSprite(null));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithoutMouseEvents.setSprite(null));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.setSprite(new FakeSprite(new FakeImage(false))));
    }

    @Test
    public void testGetAndSetBorderThicknessProvider() {
        assertSame(BORDER_THICKNESS_PROVIDER,
                renderableWithMouseEvents.getBorderThicknessProvider());
        assertSame(BORDER_THICKNESS_PROVIDER,
                renderableWithoutMouseEvents.getBorderThicknessProvider());

        FakeProviderAtTime<Float> newBorderThicknessProvider = new FakeProviderAtTime<>();

        renderableWithMouseEvents.setBorderThicknessProvider(newBorderThicknessProvider);
        renderableWithoutMouseEvents.setBorderThicknessProvider(newBorderThicknessProvider);

        assertSame(newBorderThicknessProvider,
                renderableWithMouseEvents.getBorderThicknessProvider());
        assertSame(newBorderThicknessProvider,
                renderableWithoutMouseEvents.getBorderThicknessProvider());
    }

    @Test
    public void testSetBorderThicknessProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.setBorderThicknessProvider(null));
    }

    @Test
    public void testGetAndSetBorderColorProvider() {
        assertSame(BORDER_COLOR_PROVIDER,
                renderableWithMouseEvents.getBorderColorProvider());
        assertSame(BORDER_COLOR_PROVIDER,
                renderableWithoutMouseEvents.getBorderColorProvider());

        FakeProviderAtTime<Color> newBorderColorProvider = new FakeProviderAtTime<>();

        renderableWithMouseEvents.setBorderColorProvider(newBorderColorProvider);
        renderableWithoutMouseEvents.setBorderColorProvider(newBorderColorProvider);

        assertSame(newBorderColorProvider,
                renderableWithMouseEvents.getBorderColorProvider());
        assertSame(newBorderColorProvider,
                renderableWithoutMouseEvents.getBorderColorProvider());
    }

    @Test
    public void testSetBorderColorProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.setBorderColorProvider(null));
    }

    @Test
    public void testGetAndSetCapturesMouseEvents() {
        assertTrue(renderableWithMouseEvents.getCapturesMouseEvents());
        assertFalse(renderableWithoutMouseEvents.getCapturesMouseEvents());

        renderableWithMouseEvents.setCapturesMouseEvents(false);
        assertThrows(UnsupportedOperationException.class, () ->
                renderableWithoutMouseEvents.setCapturesMouseEvents(false));

        assertFalse(renderableWithMouseEvents.getCapturesMouseEvents());
    }

    @Test
    public void testPressAndSetOnPress() {
        assertThrows(UnsupportedOperationException.class, () ->
                renderableWithoutMouseEvents.press(2, 0L));
        assertThrows(UnsupportedOperationException.class, () ->
                renderableWithoutMouseEvents.setOnPress(2, new FakeAction<>()));

        renderableWithMouseEvents.setOnPress(2, mockOnPressAction);

        renderableWithMouseEvents.press(2, TIMESTAMP);

        verify(mockOnPressAction, once()).run(
                eq(inputs(TIMESTAMP, renderableWithMouseEvents)));

        //noinspection unchecked
        Action<EventInputs> newOnPress = mock(Action.class);
        renderableWithMouseEvents.setOnPress(2, newOnPress);

        renderableWithMouseEvents.press(2, TIMESTAMP + 1);

        verify(newOnPress, once()).run(
                eq(inputs(TIMESTAMP + 1, renderableWithMouseEvents)));

        renderableWithMouseEvents.press(0, TIMESTAMP + 2);

        verify(newOnPress, once()).run(any());
    }

    @Test
    public void testPressActionIds() {
        String id1 = randomString();
        String id2 = randomString();
        String id3 = randomString();

        renderableWithMouseEvents.setOnPress(0, new FakeAction<>(id1));
        renderableWithMouseEvents.setOnPress(2, new FakeAction<>(id2));
        renderableWithMouseEvents.setOnPress(7, new FakeAction<>(id3));
        renderableWithMouseEvents.setOnPress(2, null);

        Map<Integer, String> pressActionIds = renderableWithMouseEvents.pressActionIds();

        assertNotNull(pressActionIds);
        assertEquals(2, pressActionIds.size());
        assertEquals(id1, pressActionIds.get(0));
        assertEquals(id3, pressActionIds.get(7));
    }

    @Test
    public void testReleaseAndSetOnRelease() {
        assertThrows(UnsupportedOperationException.class, () ->
                renderableWithoutMouseEvents.release(2, 0L));
        assertThrows(UnsupportedOperationException.class, () ->
                renderableWithoutMouseEvents.setOnRelease(2, new FakeAction<>()));

        renderableWithMouseEvents.release(2, TIMESTAMP);

        //noinspection unchecked
        Action<EventInputs> newOnRelease = mock(Action.class);
        renderableWithMouseEvents.setOnRelease(2, newOnRelease);

        renderableWithMouseEvents.release(2, TIMESTAMP + 1);

        verify(newOnRelease, once()).run(
                eq(inputs(TIMESTAMP + 1, renderableWithMouseEvents)));
    }

    @Test
    public void testReleaseActionIds() {
        String id1 = randomString();
        String id2 = randomString();
        String id3 = randomString();

        renderableWithMouseEvents.setOnRelease(0, new FakeAction<>(id1));
        renderableWithMouseEvents.setOnRelease(2, new FakeAction<>(id2));
        renderableWithMouseEvents.setOnRelease(7, new FakeAction<>(id3));
        renderableWithMouseEvents.setOnRelease(2, null);

        Map<Integer, String> releaseActionIds = renderableWithMouseEvents.releaseActionIds();

        assertNotNull(releaseActionIds);
        assertEquals(2, releaseActionIds.size());
        assertEquals(id1, releaseActionIds.get(0));
        assertEquals(id3, releaseActionIds.get(7));
    }

    @Test
    public void testPressOrReleaseMethodsWithInvalidButtons() {

        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.setOnPress(-1, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.setOnRelease(-1, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.press(-1, TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.press(-1, TIMESTAMP + 1));

        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.setOnPress(8, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.setOnRelease(8, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.press(8, TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.press(8, TIMESTAMP + 3));
    }

    @Test
    public void testMouseOverAndSetOnMouseOver() {
        assertThrows(UnsupportedOperationException.class, () ->
                renderableWithoutMouseEvents.mouseOver(0L));
        assertThrows(UnsupportedOperationException.class, () ->
                renderableWithoutMouseEvents.setOnMouseOver(mockOnMouseOverAction));

        renderableWithMouseEvents.mouseOver(TIMESTAMP);

        verify(mockOnMouseOverAction, once()).run(
                eq(inputs(TIMESTAMP, renderableWithMouseEvents)));

        //noinspection unchecked
        Action<EventInputs> newOnMouseOver = mock(Action.class);
        renderableWithMouseEvents.setOnMouseOver(newOnMouseOver);

        renderableWithMouseEvents.mouseOver(TIMESTAMP + 1);

        verify(newOnMouseOver, once()).run(
                eq(inputs(TIMESTAMP + 1, renderableWithMouseEvents)));
    }

    @Test
    public void testMouseOverActionId() {
        String mouseOverActionId = randomString();

        assertThrows(UnsupportedOperationException.class, () ->
                renderableWithoutMouseEvents.mouseOverActionId());

        renderableWithMouseEvents.setOnMouseOver(null);

        assertNull(renderableWithMouseEvents.mouseOverActionId());

        renderableWithMouseEvents.setOnMouseOver(new FakeAction<>(mouseOverActionId));

        assertEquals(mouseOverActionId, renderableWithMouseEvents.mouseOverActionId());
    }

    @Test
    public void testMouseLeaveAndSetOnMouseLeave() {
        assertThrows(UnsupportedOperationException.class, () ->
                renderableWithoutMouseEvents.mouseLeave(0L));
        assertThrows(UnsupportedOperationException.class, () ->
                renderableWithoutMouseEvents.setOnMouseLeave(mockOnMouseLeaveAction));

        renderableWithMouseEvents.mouseLeave(TIMESTAMP);

        verify(mockOnMouseLeaveAction, once()).run(
                eq(inputs(TIMESTAMP, renderableWithMouseEvents)));

        //noinspection unchecked
        Action<EventInputs> newOnMouseLeave = mock(Action.class);
        renderableWithMouseEvents.setOnMouseLeave(newOnMouseLeave);

        renderableWithMouseEvents.mouseLeave(TIMESTAMP + 1);

        verify(newOnMouseLeave, once()).run(
                eq(inputs(TIMESTAMP + 1, renderableWithMouseEvents)));
    }

    @Test
    public void testMouseLeaveActionId() {
        String mouseLeaveActionId = randomString();

        assertThrows(UnsupportedOperationException.class, () ->
                renderableWithoutMouseEvents.mouseLeaveActionId());

        renderableWithMouseEvents.setOnMouseLeave(null);

        assertNull(renderableWithMouseEvents.mouseLeaveActionId());

        renderableWithMouseEvents.setOnMouseLeave(new FakeAction<>(mouseLeaveActionId));

        assertEquals(mouseLeaveActionId, renderableWithMouseEvents.mouseLeaveActionId());
    }

    @Test
    public void testMouseEventCallsToOutdatedTimestamps() {
        RENDERING_AREA_PROVIDER.ProvidedValue = floatBoxOf(0f, 0f, 1f, 1f);

        renderableWithMouseEvents.press(0, TIMESTAMP);
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.press(0, TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.release(0, TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.mouseOver(TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.mouseLeave(TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.capturesMouseEventAtPoint(vertexOf(0f, 0f),
                        TIMESTAMP - 1));

        renderableWithMouseEvents.release(0, TIMESTAMP + 1);
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.press(0, TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.release(0, TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.mouseOver(TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.mouseLeave(TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.capturesMouseEventAtPoint(vertexOf(0f, 0f),
                        TIMESTAMP));

        renderableWithMouseEvents.mouseOver(TIMESTAMP + 2);
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.press(0, TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.release(0, TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.mouseOver(TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.mouseLeave(TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.capturesMouseEventAtPoint(vertexOf(0f, 0f),
                        TIMESTAMP + 1));

        renderableWithMouseEvents.mouseLeave(TIMESTAMP + 3);
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.press(0, TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.release(0, TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.mouseOver(TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.mouseLeave(TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.capturesMouseEventAtPoint(vertexOf(0f, 0f),
                        TIMESTAMP + 2));

        renderableWithMouseEvents.capturesMouseEventAtPoint(vertexOf(0f, 0f), TIMESTAMP + 4);
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.press(0, TIMESTAMP + 3));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.release(0, TIMESTAMP + 3));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.mouseOver(TIMESTAMP + 3));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.mouseLeave(TIMESTAMP + 3));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.capturesMouseEventAtPoint(vertexOf(0f, 0f),
                        TIMESTAMP + 3));
    }

    @Test
    public void testColorShiftProviders() {
        assertSame(COLOR_SHIFTS,
                renderableWithMouseEvents.colorShifts());
        assertSame(COLOR_SHIFTS,
                renderableWithoutMouseEvents.colorShifts());
    }

    @Test
    public void testGetAndSetRenderingAreaProvider() {
        assertSame(RENDERING_AREA_PROVIDER,
                renderableWithMouseEvents.getRenderingDimensionsProvider());
        assertSame(RENDERING_AREA_PROVIDER,
                renderableWithoutMouseEvents.getRenderingDimensionsProvider());

        FakeProviderAtTime<FloatBox> newRenderingDimensionsProvider = new FakeProviderAtTime<>();

        renderableWithMouseEvents
                .setRenderingDimensionsProvider(newRenderingDimensionsProvider);
        renderableWithoutMouseEvents
                .setRenderingDimensionsProvider(newRenderingDimensionsProvider);

        assertSame(newRenderingDimensionsProvider,
                renderableWithMouseEvents.getRenderingDimensionsProvider());
        assertSame(newRenderingDimensionsProvider,
                renderableWithoutMouseEvents.getRenderingDimensionsProvider());
    }

    @Test
    public void testGetAndSetZ() {
        assertEquals(Z, renderableWithMouseEvents.getZ());
        assertEquals(Z, renderableWithoutMouseEvents.getZ());

        int newZ = 456;

        renderableWithMouseEvents.setZ(newZ);
        renderableWithoutMouseEvents.setZ(newZ);

        assertEquals(newZ, renderableWithMouseEvents.getZ());
        assertEquals(newZ, renderableWithoutMouseEvents.getZ());

        verify(mockContainingComponent, once()).add(renderableWithMouseEvents);
        verify(mockContainingComponent, once()).add(renderableWithoutMouseEvents);
    }

    @Test
    public void testCapturesMouseEventAtPoint() {
        SPRITE_SUPPORTING_MOUSE_EVENTS.LeftX = 250;
        SPRITE_SUPPORTING_MOUSE_EVENTS.RightX = 750;
        SPRITE_SUPPORTING_MOUSE_EVENTS.TopY = 1000;
        SPRITE_SUPPORTING_MOUSE_EVENTS.BottomY = 2500;
        ((FakeImage) SPRITE_SUPPORTING_MOUSE_EVENTS.Image).Width = 1000;
        ((FakeImage) SPRITE_SUPPORTING_MOUSE_EVENTS.Image).Height = 3000;
        RENDERING_AREA_PROVIDER.ProvidedValue = floatBoxOf(-0.5f, -2f, 0.75f, 0.5f);

        boolean capturesMouseEventAtPoint = renderableWithMouseEvents
                .capturesMouseEventAtPoint(vertexOf(0.123f, 0.456f), TIMESTAMP);

        assertTrue(capturesMouseEventAtPoint);
        List<Pair<Integer, Integer>> capturesMouseEventsAtPixelInputs =
                ((FakeImage) SPRITE_SUPPORTING_MOUSE_EVENTS.Image).CapturesMouseEventsAtPixelInputs;
        assertEquals(1, capturesMouseEventsAtPixelInputs.size());
        assertEquals(
                (int) ((((0.123f - (-0.5f)) / (0.75f - (-0.5f))) * (750 - 250)) + 250),
                (int) capturesMouseEventsAtPixelInputs.getFirst().FIRST);
        assertEquals(
                (int) ((((0.456f - (-2.0f)) / (0.5f - (-2.0f))) * (2500 - 1000)) + 1000),
                (int) capturesMouseEventsAtPixelInputs.getFirst().SECOND);
        assertEquals(1, RENDERING_AREA_PROVIDER.TimestampInputs.size());
        assertEquals(TIMESTAMP, (long) RENDERING_AREA_PROVIDER.TimestampInputs.getFirst());
    }

    @Test
    public void testCapturesMouseEventsAtPointDoesNotExceedRenderingBoundaries() {
        RENDERING_AREA_PROVIDER.ProvidedValue = WHOLE_SCREEN;
        SPRITE_SUPPORTING_MOUSE_EVENTS.Image = new FakeImage(true);
        when(mockRenderingBoundaries.currentBoundaries()).thenReturn(floatBoxOf(0f, 0f, 0.5f, 1f));

        assertTrue(renderableWithMouseEvents
                .capturesMouseEventAtPoint(vertexOf(0.499f, 0.5f), TIMESTAMP));
        assertFalse(renderableWithMouseEvents
                .capturesMouseEventAtPoint(vertexOf(0.501f, 0.5f), TIMESTAMP));
    }

    @Test
    public void testCapturesMouseEventAtPointWithInvalidArgs() {
        float verySmallNumber = 0.0001f;

        assertThrows(UnsupportedOperationException.class, () ->
                renderableWithoutMouseEvents.capturesMouseEventAtPoint(vertexOf(.5f, .5f),
                        0L));

        RENDERING_AREA_PROVIDER.ProvidedValue = floatBoxOf(.5f, .5f, 1.5f, 1.5f);

        assertThrows(IllegalArgumentException.class, () -> renderableWithMouseEvents
                .capturesMouseEventAtPoint(vertexOf(.5f - verySmallNumber, .75f), 0L));
        assertThrows(IllegalArgumentException.class, () -> renderableWithMouseEvents
                .capturesMouseEventAtPoint(vertexOf(1f + verySmallNumber, .75f), 0L));
        assertThrows(IllegalArgumentException.class, () -> renderableWithMouseEvents
                .capturesMouseEventAtPoint(vertexOf(.75f, .5f - verySmallNumber), 0L));
        assertThrows(IllegalArgumentException.class, () -> renderableWithMouseEvents
                .capturesMouseEventAtPoint(vertexOf(.75f, 1.5f + verySmallNumber), 0L));

        RENDERING_AREA_PROVIDER.ProvidedValue = floatBoxOf(-0.5f, -0.5f, 0.5f, 0.5f);

        assertThrows(IllegalArgumentException.class, () -> renderableWithMouseEvents
                .capturesMouseEventAtPoint(vertexOf(0f - verySmallNumber, .25f), 0L));
        assertThrows(IllegalArgumentException.class, () -> renderableWithMouseEvents
                .capturesMouseEventAtPoint(vertexOf(0.5f + verySmallNumber, .25f), 0L));
        assertThrows(IllegalArgumentException.class, () -> renderableWithMouseEvents
                .capturesMouseEventAtPoint(vertexOf(.25f, 0f - verySmallNumber), 0L));
        assertThrows(IllegalArgumentException.class, () -> renderableWithMouseEvents
                .capturesMouseEventAtPoint(vertexOf(.25f, 0.5f + verySmallNumber), 0L));
    }

    @Test
    public void testDelete() {
        renderableWithMouseEvents.delete();
        renderableWithoutMouseEvents.delete();

        assertNull(renderableWithMouseEvents.component());
        assertNull(renderableWithoutMouseEvents.component());
        assertTrue(renderableWithMouseEvents.isDeleted());
        assertTrue(renderableWithoutMouseEvents.isDeleted());
    }

    @Test
    public void testUuid() {
        assertSame(UUID, renderableWithMouseEvents.uuid());
        assertSame(UUID, renderableWithoutMouseEvents.uuid());
    }

    @Test
    public void testComponent() {
        assertSame(mockContainingComponent, renderableWithMouseEvents.component());
    }

    @Test
    public void testSetComponent() {
        ((SpriteRenderableImpl) renderableWithMouseEvents).setComponent(null);

        assertNull(renderableWithMouseEvents.component());
    }
}
