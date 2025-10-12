package inaugural.soliloquy.io.test.unit.graphics.renderables;

import inaugural.soliloquy.io.graphics.renderables.FiniteAnimationRenderableImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.FiniteAnimationRenderable;
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
import static soliloquy.specs.io.input.mouse.MouseEventHandler.EventType.*;
import static soliloquy.specs.ui.EventInputs.eventInputs;

// NB: This is a total fucking mess. Don't feel any pressure to fix it all at once.
@ExtendWith(MockitoExtension.class)
public class FiniteAnimationRenderableImplTests {
    int ANIMATION_DURATION = 555;
    private final String ANIMATION_SUPPORTING_ID = randomString();
    private final FakeAnimation ANIMATION_SUPPORTING_MOUSE_EVENTS =
            new FakeAnimation(ANIMATION_SUPPORTING_ID, ANIMATION_DURATION, true);
    private final String ANIMATION_NOT_SUPPORTING_ID = randomString();
    private final FakeAnimation ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS =
            new FakeAnimation(ANIMATION_NOT_SUPPORTING_ID, ANIMATION_DURATION, false);
    private final FakeProviderAtTime<Float> BORDER_THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Color> BORDER_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final Map<Integer, Action<EventInputs>> ON_PRESS_ACTIONS = mapOf();
    private final List<ColorShift> COLOR_SHIFTS = listOf();
    private final int Z = randomInt();

    private final long START_TIMESTAMP = 111L;
    private final Long PAUSED_TIMESTAMP_1 = -456L;
    private final Long PAUSED_TIMESTAMP_2 = 456L;
    private final Long MOST_RECENT_TIMESTAMP = -123L;

    long TIMESTAMP = randomLongWithInclusiveFloor(MOST_RECENT_TIMESTAMP);

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private Component mockContainingComponent;
    @Mock private ProviderAtTime<FloatBox> mockRenderingAreaProvider;
    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private TimestampValidator mockTimestampValidator;
    @Mock private Action<EventInputs> mockOnPressAction;
    @Mock private Action<EventInputs> mockOnMouseOverAction;
    @Mock private Action<EventInputs> mockOnMouseLeaveAction;

    private FiniteAnimationRenderable renderable;

    @BeforeEach
    public void setUp() {
        lenient().when(mockRenderingBoundaries.currentBoundaries()).thenReturn(WHOLE_SCREEN);

        renderable = new FiniteAnimationRenderableImpl(ANIMATION_SUPPORTING_MOUSE_EVENTS,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null,
                mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS,
                mockRenderingAreaProvider, Z, UUID, mockContainingComponent,
                mockRenderingBoundaries, START_TIMESTAMP, null, mockTimestampValidator);
        renderable.setCapturesMouseEvents(true);
    }

    @Test
    public void testConstructorWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(null, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, COLOR_SHIFTS, mockRenderingAreaProvider, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries,
                        START_TIMESTAMP, PAUSED_TIMESTAMP_1, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, null,
                        BORDER_COLOR_PROVIDER, COLOR_SHIFTS, mockRenderingAreaProvider, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries,
                        START_TIMESTAMP, PAUSED_TIMESTAMP_1, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, null, COLOR_SHIFTS, mockRenderingAreaProvider, Z,
                        UUID, mockContainingComponent,
                        mockRenderingBoundaries, START_TIMESTAMP, PAUSED_TIMESTAMP_1,
                        mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, null,
                        mockRenderingAreaProvider, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries, START_TIMESTAMP,
                        PAUSED_TIMESTAMP_1, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFTS, null, Z,
                        UUID, mockContainingComponent,
                        mockRenderingBoundaries, START_TIMESTAMP, PAUSED_TIMESTAMP_1,
                        mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFTS,
                        mockRenderingAreaProvider, Z, UUID, mockContainingComponent, null,
                        START_TIMESTAMP, PAUSED_TIMESTAMP_1, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFTS,
                        mockRenderingAreaProvider, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries, START_TIMESTAMP,
                        PAUSED_TIMESTAMP_1, null));

        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(null, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                        mockOnMouseLeaveAction, COLOR_SHIFTS, mockRenderingAreaProvider, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries,
                        START_TIMESTAMP, PAUSED_TIMESTAMP_1, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, null,
                        BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                        mockOnMouseLeaveAction, COLOR_SHIFTS, mockRenderingAreaProvider, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries,
                        START_TIMESTAMP, PAUSED_TIMESTAMP_1, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(ANIMATION_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, null, ON_PRESS_ACTIONS, null,
                        mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS,
                        mockRenderingAreaProvider, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries, START_TIMESTAMP,
                        PAUSED_TIMESTAMP_1, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(ANIMATION_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null,
                        mockOnMouseOverAction, mockOnMouseLeaveAction, null,
                        mockRenderingAreaProvider, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries, START_TIMESTAMP,
                        PAUSED_TIMESTAMP_1, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(ANIMATION_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null,
                        mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS, null, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries,
                        START_TIMESTAMP, PAUSED_TIMESTAMP_1, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(ANIMATION_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null,
                        mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS,
                        mockRenderingAreaProvider, Z, null, mockContainingComponent,
                        mockRenderingBoundaries, START_TIMESTAMP,
                        PAUSED_TIMESTAMP_1, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(ANIMATION_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null,
                        mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS,
                        mockRenderingAreaProvider, Z, UUID, mockContainingComponent, null,
                        START_TIMESTAMP, PAUSED_TIMESTAMP_1, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(ANIMATION_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null,
                        mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS,
                        mockRenderingAreaProvider, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries, START_TIMESTAMP,
                        PAUSED_TIMESTAMP_1, null));
    }

    @Test
    public void testConstructorAddsSelfToContainingComponent() {
        verify(mockContainingComponent, once()).add(renderable);
    }

    @Test
    public void testAnimationId() {
        assertEquals(ANIMATION_SUPPORTING_ID, renderable.animationId());
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
        assertThrows(IllegalArgumentException.class, () -> renderable.setBorderColorProvider(null));
    }

    @Test
    public void testStartAndEndTimestamp() {
        assertEquals(START_TIMESTAMP, renderable.startTimestamp());
        assertEquals(START_TIMESTAMP + ANIMATION_DURATION, renderable.endTimestamp());
    }

    @Test
    public void testReportPause() {
        renderable.reportPause(PAUSED_TIMESTAMP_2);

        assertEquals(PAUSED_TIMESTAMP_2, renderable.pausedTimestamp());
    }

    @Test
    public void testReportUnpauseUpdatesStartAndEndTimestamps() {
        var pauseDuration = 789789L;

        renderable.reportPause(PAUSED_TIMESTAMP_2);

        assertEquals(PAUSED_TIMESTAMP_2, renderable.pausedTimestamp());

        renderable.reportUnpause(PAUSED_TIMESTAMP_2 + pauseDuration);

        assertEquals(START_TIMESTAMP + pauseDuration, renderable.startTimestamp());
        assertNull(renderable.pausedTimestamp());
        assertEquals(START_TIMESTAMP + ANIMATION_DURATION + pauseDuration,
                renderable.endTimestamp());
    }

    @Test
    public void testPauseWhilePaused() {
        renderable.reportPause(PAUSED_TIMESTAMP_2);

        assertThrows(IllegalArgumentException.class,
                () -> renderable.reportPause(PAUSED_TIMESTAMP_2));
    }

    @Test
    public void testUnpauseWhileUnpaused() {
        assertThrows(IllegalArgumentException.class, () -> renderable.reportUnpause(999999L));
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
        verify(mockOnPressAction, once()).accept(
                eq(eventInputs(TIMESTAMP).withMouseEvent(2, PRESS, renderable,
                        mockContainingComponent)));

        //noinspection unchecked
        Action<EventInputs> newOnPress = mock(Action.class);
        renderable.setOnPress(2, newOnPress);

        renderable.press(2, TIMESTAMP + 1);

        verify(newOnPress, once()).accept(
                eq(eventInputs(TIMESTAMP + 1).withMouseEvent(0, PRESS, renderable,
                        mockContainingComponent)));

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

        Map<Integer, String> pressActionIds =
                renderable.pressActionIds();

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

        verify(newOnRelease, once()).accept(
                eq(eventInputs(TIMESTAMP + 1).withMouseEvent(2, RELEASE, renderable,
                        mockContainingComponent)));
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

        Map<Integer, String> releaseActionIds =
                renderable.releaseActionIds();

        assertNotNull(releaseActionIds);
        assertEquals(2, releaseActionIds.size());
        assertEquals(id1, releaseActionIds.get(0));
        assertEquals(id3, releaseActionIds.get(7));
    }

    @Test
    public void testPressOrReleaseMethodsWithInvalidButtons() {
        var timestamp = 456456L;

        assertThrows(IllegalArgumentException.class, () ->
                renderable.setOnPress(-1, action(randomString(), _ -> {})));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setOnRelease(-1, action(randomString(), _ -> {})));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.press(-1, timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.press(-1, timestamp + 1));

        assertThrows(IllegalArgumentException.class, () ->
                renderable.setOnPress(8, action(randomString(), _ -> {})));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setOnRelease(8, action(randomString(), _ -> {})));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.press(8, timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.press(8, timestamp + 3));
    }

    @Test
    public void testMouseOverAndSetOnMouseOver() {
        renderable.mouseOver(TIMESTAMP);

        verify(mockOnMouseOverAction, once()).accept(
                eq(eventInputs(TIMESTAMP).withMouseEvent(null, MOUSE_OVER, renderable,
                        mockContainingComponent)));

        //noinspection unchecked
        Action<EventInputs> newOnMouseOver = mock(Action.class);
        renderable.setOnMouseOver(newOnMouseOver);
        renderable.mouseOver(TIMESTAMP + 1);

        verify(newOnMouseOver, once()).accept(
                eq(eventInputs(TIMESTAMP + 1).withMouseEvent(null, MOUSE_OVER, renderable,
                        mockContainingComponent)));
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

        assertEquals(mouseOverActionId,
                renderable.mouseOverActionId());
    }

    @Test
    public void testMouseLeaveAndSetOnMouseLeave() {
        renderable.mouseLeave(TIMESTAMP);

        verify(mockOnMouseLeaveAction, once()).accept(
                eq(eventInputs(TIMESTAMP).withMouseEvent(null, MOUSE_LEAVE, renderable,
                        mockContainingComponent)));

        //noinspection unchecked
        Action<EventInputs> newOnMouseLeave = mock(Action.class);
        renderable.setOnMouseLeave(newOnMouseLeave);
        renderable.mouseLeave(TIMESTAMP + 1);

        verify(newOnMouseLeave, once()).accept(
                eq(eventInputs(TIMESTAMP + 1).withMouseEvent(null, MOUSE_LEAVE, renderable,
                        mockContainingComponent)));
    }

    @Test
    public void testMouseLeaveAndSetOnMouseLeaveWhenNotCapturingMouseEvents() {
        renderable.setCapturesMouseEvents(false);

        assertThrows(UnsupportedOperationException.class, () -> renderable.mouseLeave(0L));
        assertThrows(UnsupportedOperationException.class,
                () -> renderable.setOnMouseLeave(action(randomString(), _ -> {})));
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
    public void testProvide() {
        var msAfterStartTimestampForMidpointFrame = 333;

        renderable.provide(START_TIMESTAMP - 1);
        renderable.provide(START_TIMESTAMP);
        renderable.provide(START_TIMESTAMP + msAfterStartTimestampForMidpointFrame);
        renderable.provide(START_TIMESTAMP + ANIMATION_DURATION);
        renderable.provide(START_TIMESTAMP + ANIMATION_DURATION + 1);

        assertEquals(5, ANIMATION_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.size());
        assertEquals(0,
                (int) ANIMATION_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.get(0).FIRST);
        assertEquals(0,
                (int) ANIMATION_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.get(1).FIRST);
        assertEquals(msAfterStartTimestampForMidpointFrame,
                (int) ANIMATION_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.get(2).FIRST);
        assertEquals(ANIMATION_DURATION,
                (int) ANIMATION_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.get(3).FIRST);
        assertEquals(ANIMATION_DURATION,
                (int) ANIMATION_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.get(4).FIRST);
    }

    @Test
    public void testProvideWhenPaused() {
        renderable.reportPause(PAUSED_TIMESTAMP_2);

        renderable.provide(789789L);

        assertEquals(1, ANIMATION_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.size());
        assertEquals(PAUSED_TIMESTAMP_2 - START_TIMESTAMP,
                (int) ANIMATION_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.getFirst().FIRST);
    }

    @Test
    public void testUnpauseUpdatesStartAndEndTimestamps() {
        var pauseTimestamp = 777777L;
        var unpauseTimestamp = 888888L;

        renderable.reportPause(pauseTimestamp);
        renderable.reportUnpause(unpauseTimestamp);

        assertEquals(START_TIMESTAMP + (unpauseTimestamp - pauseTimestamp),
                renderable.startTimestamp());
        assertEquals(START_TIMESTAMP + ANIMATION_DURATION + (unpauseTimestamp - pauseTimestamp),
                renderable.endTimestamp());
    }

    @Test
    public void testColorShiftProviders() {
        assertSame(COLOR_SHIFTS, renderable.colorShifts());
    }

    @Test
    public void testGetAndSetRenderingAreaProvider() {
        assertSame(mockRenderingAreaProvider, renderable.getRenderingDimensionsProvider());

        var newProvider = generateMockStaticProvider(randomFloatBox());

        renderable.setRenderingDimensionsProvider(newProvider);

        assertSame(newProvider, renderable.getRenderingDimensionsProvider());
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
        ANIMATION_SUPPORTING_MOUSE_EVENTS.AnimationFrameSnippet = new FakeAnimationFrameSnippet();
        ANIMATION_SUPPORTING_MOUSE_EVENTS.AnimationFrameSnippet.LeftX = 250;
        ANIMATION_SUPPORTING_MOUSE_EVENTS.AnimationFrameSnippet.RightX = 750;
        ANIMATION_SUPPORTING_MOUSE_EVENTS.AnimationFrameSnippet.TopY = 1000;
        ANIMATION_SUPPORTING_MOUSE_EVENTS.AnimationFrameSnippet.BottomY = 2500;
        ((FakeImage) ANIMATION_SUPPORTING_MOUSE_EVENTS.AnimationFrameSnippet.Image).Width = 1000;
        ((FakeImage) ANIMATION_SUPPORTING_MOUSE_EVENTS.AnimationFrameSnippet.Image).Height = 3000;
        when(mockRenderingAreaProvider.provide(anyLong())).thenReturn(
                floatBoxOf(-0.5f, -2f, 0.75f, 0.5f));

        var capturesMouseEventAtPoint =
                renderable.capturesMouseEventAtPoint(vertexOf(0.123f, 0.456f), 789L);

        assertTrue(capturesMouseEventAtPoint);
        List<Pair<Integer, Integer>> capturesMouseEventsAtPixelInputs =
                ((FakeImage) ANIMATION_SUPPORTING_MOUSE_EVENTS.AnimationFrameSnippet.Image)
                        .CapturesMouseEventsAtPixelInputs;
        assertEquals(1, capturesMouseEventsAtPixelInputs.size());
        assertEquals(
                (int) ((((0.123f - (-0.5f)) / (0.75f - (-0.5f))) * (750 - 250)) + 250),
                (int) capturesMouseEventsAtPixelInputs.getFirst().FIRST);
        assertEquals(
                (int) ((((0.456f - (-2.0f)) / (0.5f - (-2.0f))) * (2500 - 1000)) + 1000),
                (int) capturesMouseEventsAtPixelInputs.getFirst().SECOND);
        verify(mockRenderingAreaProvider, once()).provide(anyLong());
        verify(mockRenderingAreaProvider, once()).provide(789L);
    }

    @Test
    public void testCapturesMouseEventAtPointDoesNotExceedRenderingBoundaries() {
        ANIMATION_SUPPORTING_MOUSE_EVENTS.AnimationFrameSnippet = new FakeAnimationFrameSnippet();
        ((FakeImage) ANIMATION_SUPPORTING_MOUSE_EVENTS.AnimationFrameSnippet.Image)
                .SupportsMouseEventCapturing = true;
        when(mockRenderingAreaProvider.provide(anyLong())).thenReturn(WHOLE_SCREEN);
        when(mockRenderingBoundaries.currentBoundaries()).thenReturn(floatBoxOf(0f, 0f, 0.5f, 1f));

        assertTrue(renderable.capturesMouseEventAtPoint(
                vertexOf(0.499f, 0f), MOST_RECENT_TIMESTAMP));
        assertFalse(renderable.capturesMouseEventAtPoint(
                vertexOf(0.501f, 0f), MOST_RECENT_TIMESTAMP));
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
    public void testComponent() {
        assertSame(mockContainingComponent,
                renderable.containingComponent());
    }

    @Test
    public void testSetComponent() {
        ((FiniteAnimationRenderableImpl) renderable).setContainingComponent(null);

        assertNull(renderable.containingComponent());
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
}
