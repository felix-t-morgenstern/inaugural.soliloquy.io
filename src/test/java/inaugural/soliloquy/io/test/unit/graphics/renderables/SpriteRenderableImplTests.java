package inaugural.soliloquy.io.test.unit.graphics.renderables;

import inaugural.soliloquy.io.graphics.renderables.SpriteRenderableImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.SpriteRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
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
import static inaugural.soliloquy.tools.testing.Mock.generateMockStaticProvider;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.entities.Action.action;
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
    private final int Z = randomInt();
    private final long TIMESTAMP = randomLong();

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private Component mockContainingComponent;
    @Mock private ProviderAtTime<FloatBox> mockRenderingAreaProvider;
    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private TimestampValidator mockTimestampValidator;
    @Mock private Action<EventInputs> mockOnPressAction;
    @Mock private Action<EventInputs> mockOnMouseOverAction;
    @Mock private Action<EventInputs> mockOnMouseLeaveAction;

    private SpriteRenderable renderable;

    @BeforeEach
    public void setUp() {
        lenient().when(mockRenderingBoundaries.currentBoundaries()).thenReturn(WHOLE_SCREEN);

        renderable =
                new SpriteRenderableImpl(SPRITE_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                        mockOnMouseLeaveAction, COLOR_SHIFTS, mockRenderingAreaProvider, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator);

        renderable.setCapturesMouseEvents(true);
    }

    @Test
    public void testConstructorWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(null, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                        mockOnMouseLeaveAction, COLOR_SHIFTS, mockRenderingAreaProvider, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(SPRITE_SUPPORTING_MOUSE_EVENTS, null,
                        BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                        mockOnMouseLeaveAction, COLOR_SHIFTS, mockRenderingAreaProvider, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(SPRITE_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, null, ON_PRESS_ACTIONS, null,
                        mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS,
                        mockRenderingAreaProvider, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(SPRITE_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null,
                        mockOnMouseOverAction, mockOnMouseLeaveAction, null,
                        mockRenderingAreaProvider, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(SPRITE_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null,
                        mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS, null, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(SPRITE_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null,
                        mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS,
                        mockRenderingAreaProvider, Z, null, mockContainingComponent,
                        mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(SPRITE_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null,
                        mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS,
                        mockRenderingAreaProvider, Z, UUID, mockContainingComponent,
                        null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(SPRITE_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null,
                        mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS,
                        mockRenderingAreaProvider, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries, null));

        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(null, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, COLOR_SHIFTS, mockRenderingAreaProvider, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(SPRITE_NOT_SUPPORTING_MOUSE_EVENTS, null,
                        BORDER_COLOR_PROVIDER, COLOR_SHIFTS, mockRenderingAreaProvider, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(SPRITE_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, null, COLOR_SHIFTS, mockRenderingAreaProvider, Z,
                        UUID, mockContainingComponent,
                        mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(SPRITE_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, null,
                        mockRenderingAreaProvider, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(SPRITE_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFTS, null, Z,
                        UUID, mockContainingComponent,
                        mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(SPRITE_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFTS,
                        mockRenderingAreaProvider, Z, null, mockContainingComponent,
                        mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(SPRITE_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFTS,
                        mockRenderingAreaProvider, Z, UUID, mockContainingComponent,
                        null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new SpriteRenderableImpl(SPRITE_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFTS,
                        mockRenderingAreaProvider, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries, null));
    }

    @Test
    public void testConstructorAddsSelfToContainingComponent() {
        verify(mockContainingComponent, once()).add(renderable);
    }

    @Test
    public void testGetAndSetSprite() {
        assertSame(SPRITE_SUPPORTING_MOUSE_EVENTS, renderable.getSprite());

        var newSprite = new FakeSprite(new FakeImage(true));

        renderable.setSprite(newSprite);

        assertSame(newSprite, renderable.getSprite());
    }

    @Test
    public void testSetSpriteWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> renderable.setSprite(null));
        assertThrows(IllegalArgumentException.class,
                () -> renderable.setSprite(new FakeSprite(new FakeImage(false))));
    }

    @Test
    public void testGetAndSetBorderThicknessProvider() {
        assertSame(BORDER_THICKNESS_PROVIDER, renderable.getBorderThicknessProvider());

        var newProvider = generateMockStaticProvider(randomFloat());

        renderable.setBorderThicknessProvider(newProvider);

        assertSame(newProvider, renderable.getBorderThicknessProvider());
    }

    @Test
    public void testSetBorderThicknessProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setBorderThicknessProvider(null));
    }

    @Test
    public void testGetAndSetBorderColorProvider() {
        assertSame(BORDER_COLOR_PROVIDER, renderable.getBorderColorProvider());

        var newProvider = generateMockStaticProvider(randomColor());

        renderable.setBorderColorProvider(newProvider);

        assertSame(newProvider, renderable.getBorderColorProvider());
    }

    @Test
    public void testSetBorderColorProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setBorderColorProvider(null));
    }

    @Test
    public void testGetAndSetCapturesMouseEvents() {
        assertTrue(renderable.getCapturesMouseEvents());

        renderable.setCapturesMouseEvents(false);

        assertFalse(renderable.getCapturesMouseEvents());
    }

    @Test
    public void testPressAndSetOnPress() {

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
    public void testPressAndSetOnPressWhenNotCapturingMouseEvents() {
        renderable.setCapturesMouseEvents(false);

        assertThrows(UnsupportedOperationException.class, () -> renderable.press(2, 0L));
        assertThrows(UnsupportedOperationException.class,
                () -> renderable.setOnPress(2, action(randomString(), _ -> {})));
    }

    @Test
    public void testPressActionIds() {
        var id1 = randomString();
        var id2 = randomString();
        var id3 = randomString();

        renderable.setOnPress(0, action(id1, _ -> {}));
        renderable.setOnPress(2, action(id2, _ -> {}));
        renderable.setOnPress(7, action(id3, _ -> {}));
        renderable.setOnPress(2, null);

        var pressActionIds = renderable.pressActionIds();

        assertNotNull(pressActionIds);
        assertEquals(2, pressActionIds.size());
        assertEquals(id1, pressActionIds.get(0));
        assertEquals(id3, pressActionIds.get(7));
    }

    @Test
    public void testReleaseAndSetOnRelease() {
        renderable.release(2, TIMESTAMP);

        //noinspection unchecked
        Action<EventInputs> newOnRelease = mock(Action.class);
        renderable.setOnRelease(2, newOnRelease);

        renderable.release(2, TIMESTAMP + 1);

        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(TIMESTAMP);
        verify(newOnRelease, once()).accept(
                eq(inputs(TIMESTAMP + 1, renderable)));
    }

    @Test
    public void testReleaseAndSetOnReleaseWhenNotCapturingMouseEvents() {
        renderable.setCapturesMouseEvents(false);

        assertThrows(UnsupportedOperationException.class, () -> renderable.release(2, 0L));
        assertThrows(UnsupportedOperationException.class,
                () -> renderable.setOnRelease(2, action(randomString(), _ -> {})));
    }

    @Test
    public void testReleaseActionIds() {
        var id1 = randomString();
        var id2 = randomString();
        var id3 = randomString();

        renderable.setOnRelease(0, action(id1, _ -> {}));
        renderable.setOnRelease(2, action(id2, _ -> {}));
        renderable.setOnRelease(7, action(id3, _ -> {}));
        renderable.setOnRelease(2, null);

        Map<Integer, String> releaseActionIds = renderable.releaseActionIds();

        assertNotNull(releaseActionIds);
        assertEquals(2, releaseActionIds.size());
        assertEquals(id1, releaseActionIds.get(0));
        assertEquals(id3, releaseActionIds.get(7));
    }

    @Test
    public void testPressOrReleaseMethodsWithInvalidButtons() {

        assertThrows(IllegalArgumentException.class,
                () -> renderable.setOnPress(-1, action(randomString(), _ -> {})));
        assertThrows(IllegalArgumentException.class,
                () -> renderable.setOnRelease(-1, action(randomString(), _ -> {})));
        assertThrows(IllegalArgumentException.class, () -> renderable.press(-1, TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderable.press(-1, TIMESTAMP + 1));

        assertThrows(IllegalArgumentException.class,
                () -> renderable.setOnPress(8, action(randomString(), _ -> {})));
        assertThrows(IllegalArgumentException.class,
                () -> renderable.setOnRelease(8, action(randomString(), _ -> {})));
        assertThrows(IllegalArgumentException.class, () -> renderable.press(8, TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () -> renderable.press(8, TIMESTAMP + 3));
    }

    @Test
    public void testMouseOverAndSetOnMouseOver() {
        renderable.mouseOver(TIMESTAMP);

        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(TIMESTAMP);
        verify(mockOnMouseOverAction, once()).accept(eq(inputs(TIMESTAMP, renderable)));

        //noinspection unchecked
        Action<EventInputs> newOnMouseOver = mock(Action.class);
        renderable.setOnMouseOver(newOnMouseOver);

        renderable.mouseOver(TIMESTAMP + 1);

        verify(newOnMouseOver, once()).accept(
                eq(inputs(TIMESTAMP + 1, renderable)));
    }

    @Test
    public void testMouseOverAndSetOnMouseOverWhenNotCapturingMouseEvents() {
        renderable.setCapturesMouseEvents(false);

        assertThrows(UnsupportedOperationException.class, () -> renderable.mouseOver(0L));
        assertThrows(UnsupportedOperationException.class,
                () -> renderable.setOnMouseOver(action(randomString(), _ -> {})));
    }

    @Test
    public void testMouseOverActionId() {
        var mouseOverActionId = randomString();

        renderable.setOnMouseOver(null);

        assertNull(renderable.mouseOverActionId());

        renderable.setOnMouseOver(action(mouseOverActionId, _ -> {}));

        assertEquals(mouseOverActionId, renderable.mouseOverActionId());
    }

    @Test
    public void testMouseLeaveAndSetOnMouseLeave() {
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
    public void testMouseLeaveAndSetOnMouseLeaveWhenNotCapturingMouseEvents() {
        renderable.setCapturesMouseEvents(false);

        assertThrows(UnsupportedOperationException.class, () -> renderable.mouseLeave(0L));
        assertThrows(UnsupportedOperationException.class, () -> renderable.setOnMouseLeave(action(randomString(), _ -> {})));
    }

    @Test
    public void testMouseLeaveActionId() {
        var mouseLeaveActionId = randomString();

        renderable.setOnMouseLeave(null);

        assertNull(renderable.mouseLeaveActionId());

        renderable.setOnMouseLeave(action(mouseLeaveActionId, _ -> {}));

        assertEquals(mouseLeaveActionId, renderable.mouseLeaveActionId());
    }

    @Test
    public void testColorShiftProviders() {
        assertSame(COLOR_SHIFTS, renderable.colorShifts());
    }

    @Test
    public void testGetAndSetRenderingAreaProvider() {
        assertSame(mockRenderingAreaProvider, renderable.getRenderingDimensionsProvider());

        var newRenderingDimensionsProvider = generateMockStaticProvider(randomFloatBox());

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
    public void testCapturesMouseEventAtPoint() {
        SPRITE_SUPPORTING_MOUSE_EVENTS.LeftX = 250;
        SPRITE_SUPPORTING_MOUSE_EVENTS.RightX = 750;
        SPRITE_SUPPORTING_MOUSE_EVENTS.TopY = 1000;
        SPRITE_SUPPORTING_MOUSE_EVENTS.BottomY = 2500;
        ((FakeImage) SPRITE_SUPPORTING_MOUSE_EVENTS.Image).Width = 1000;
        ((FakeImage) SPRITE_SUPPORTING_MOUSE_EVENTS.Image).Height = 3000;
        when(mockRenderingAreaProvider.provide(anyLong())).thenReturn(
                floatBoxOf(-0.5f, -2f, 0.75f, 0.5f));

        var capturesMouseEventAtPoint = renderable
                .capturesMouseEventAtPoint(vertexOf(0.123f, 0.456f), TIMESTAMP);

        assertTrue(capturesMouseEventAtPoint);
        var capturesMouseEventsAtPixelInputs = ((FakeImage) SPRITE_SUPPORTING_MOUSE_EVENTS.Image)
                .CapturesMouseEventsAtPixelInputs;
        assertEquals(1, capturesMouseEventsAtPixelInputs.size());
        assertEquals(
                (int) ((((0.123f - (-0.5f)) / (0.75f - (-0.5f))) * (750 - 250)) + 250),
                (int) capturesMouseEventsAtPixelInputs.getFirst().FIRST);
        assertEquals(
                (int) ((((0.456f - (-2.0f)) / (0.5f - (-2.0f))) * (2500 - 1000)) + 1000),
                (int) capturesMouseEventsAtPixelInputs.getFirst().SECOND);
        verify(mockRenderingAreaProvider, once()).provide(anyLong());
        verify(mockRenderingAreaProvider, once()).provide(TIMESTAMP);
        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(TIMESTAMP);
    }

    @Test
    public void testCapturesMouseEventsAtPointDoesNotExceedRenderingBoundaries() {
        when(mockRenderingAreaProvider.provide(anyLong())).thenReturn(WHOLE_SCREEN);
        SPRITE_SUPPORTING_MOUSE_EVENTS.Image = new FakeImage(true);
        when(mockRenderingBoundaries.currentBoundaries()).thenReturn(floatBoxOf(0f, 0f, 0.5f, 1f));

        assertTrue(renderable.capturesMouseEventAtPoint(vertexOf(0.499f, 0.5f), TIMESTAMP));
        assertFalse(renderable.capturesMouseEventAtPoint(vertexOf(0.501f, 0.5f), TIMESTAMP));
    }

    @Test
    public void testCapturesMouseEventAtPointWithInvalidArgs() {
        var verySmallNumber = 0.0001f;

        when(mockRenderingAreaProvider.provide(anyLong())).thenReturn(
                floatBoxOf(.5f, .5f, 1.5f, 1.5f));

        assertThrows(IllegalArgumentException.class, () -> renderable
                .capturesMouseEventAtPoint(vertexOf(.5f - verySmallNumber, .75f), 0L));
        assertThrows(IllegalArgumentException.class, () -> renderable
                .capturesMouseEventAtPoint(vertexOf(1f + verySmallNumber, .75f), 0L));
        assertThrows(IllegalArgumentException.class, () -> renderable
                .capturesMouseEventAtPoint(vertexOf(.75f, .5f - verySmallNumber), 0L));
        assertThrows(IllegalArgumentException.class, () -> renderable
                .capturesMouseEventAtPoint(vertexOf(.75f, 1.5f + verySmallNumber), 0L));

        when(mockRenderingAreaProvider.provide(anyLong())).thenReturn(
                floatBoxOf(-0.5f, -0.5f, 0.5f, 0.5f));

        assertThrows(IllegalArgumentException.class, () -> renderable
                .capturesMouseEventAtPoint(vertexOf(0f - verySmallNumber, .25f), 0L));
        assertThrows(IllegalArgumentException.class, () -> renderable
                .capturesMouseEventAtPoint(vertexOf(0.5f + verySmallNumber, .25f), 0L));
        assertThrows(IllegalArgumentException.class, () -> renderable
                .capturesMouseEventAtPoint(vertexOf(.25f, 0f - verySmallNumber), 0L));
        assertThrows(IllegalArgumentException.class, () -> renderable
                .capturesMouseEventAtPoint(vertexOf(.25f, 0.5f + verySmallNumber), 0L));
    }

    @Test
    public void testCapturesMouseEventAtPointWhenNotCapturingMouseEvents() {
        renderable.setCapturesMouseEvents(false);

        assertThrows(UnsupportedOperationException.class, () -> renderable.capturesMouseEventAtPoint(randomVertex(), randomLong()));
    }

    @Test
    public void testDelete() {
        renderable.delete();

        assertNull(renderable.containingComponent());

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
        ((SpriteRenderableImpl) renderable).setContainingComponent(null);

        assertNull(renderable.containingComponent());
    }
}
