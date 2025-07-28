package inaugural.soliloquy.io.test.unit.graphics.renderables;

import inaugural.soliloquy.io.graphics.renderables.FiniteAnimationRenderableImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.io.graphics.renderables.FiniteAnimationRenderable;
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
import static inaugural.soliloquy.tools.random.Random.randomInt;
import static inaugural.soliloquy.tools.random.Random.randomLongWithInclusiveFloor;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;
import static soliloquy.specs.ui.EventInputs.inputs;

@ExtendWith(MockitoExtension.class)
public class FiniteAnimationRenderableImplTests {
    int ANIMATION_DURATION = 555;
    private final String ANIMATION_SUPPORTING_ID = "animationSupportingId";
    private final FakeAnimation ANIMATION_SUPPORTING_MOUSE_EVENTS =
            new FakeAnimation(ANIMATION_SUPPORTING_ID, ANIMATION_DURATION, true);
    private final String ANIMATION_NOT_SUPPORTING_ID = "animationNotSupportingId";
    private final FakeAnimation ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS =
            new FakeAnimation(ANIMATION_NOT_SUPPORTING_ID, ANIMATION_DURATION, false);
    private final FakeProviderAtTime<Float> BORDER_THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Color> BORDER_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final Map<Integer, Action<EventInputs>> ON_PRESS_ACTIONS = mapOf();
    private final List<ColorShift> COLOR_SHIFTS = listOf();
    private final FakeStaticProvider<FloatBox> RENDERING_AREA_PROVIDER =
            new FakeStaticProvider<>(null);
    private final int Z = randomInt();

    private final long START_TIMESTAMP = 111L;
    private final Long PAUSED_TIMESTAMP_1 = -456L;
    private final Long PAUSED_TIMESTAMP_2 = 456L;
    private final Long MOST_RECENT_TIMESTAMP = -123L;

    long TIMESTAMP = randomLongWithInclusiveFloor(MOST_RECENT_TIMESTAMP);

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private Component mockContainingComponent;
    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private Action<EventInputs> mockOnPressAction;
    @Mock private Action<EventInputs> mockOnMouseOverAction;
    @Mock private Action<EventInputs> mockOnMouseLeaveAction;

    private FiniteAnimationRenderable renderableWithMouseEvents;
    private FiniteAnimationRenderable renderableWithoutMouseEvents;


    @BeforeEach
    public void setUp() {
        lenient().when(mockRenderingBoundaries.currentBoundaries()).thenReturn(WHOLE_SCREEN);

        renderableWithMouseEvents =
                new FiniteAnimationRenderableImpl(ANIMATION_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null,
                        mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries, START_TIMESTAMP, null, MOST_RECENT_TIMESTAMP);
        renderableWithoutMouseEvents =
                new FiniteAnimationRenderableImpl(ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFTS,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries, START_TIMESTAMP, null, MOST_RECENT_TIMESTAMP);
    }

    @Test
    public void testConstructorWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(null, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries,
                        START_TIMESTAMP, PAUSED_TIMESTAMP_1, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, null,
                        BORDER_COLOR_PROVIDER, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries,
                        START_TIMESTAMP, PAUSED_TIMESTAMP_1, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, null, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z,
                        UUID, mockContainingComponent,
                        mockRenderingBoundaries, START_TIMESTAMP, PAUSED_TIMESTAMP_1,
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, null,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent, mockRenderingBoundaries, START_TIMESTAMP,
                        PAUSED_TIMESTAMP_1, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFTS, null, Z,
                        UUID, mockContainingComponent,
                        mockRenderingBoundaries, START_TIMESTAMP, PAUSED_TIMESTAMP_1,
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFTS,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent, null, START_TIMESTAMP, PAUSED_TIMESTAMP_1,
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFTS,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent, mockRenderingBoundaries, START_TIMESTAMP,
                        PAUSED_TIMESTAMP_1, null));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFTS,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent, mockRenderingBoundaries, START_TIMESTAMP,
                        MOST_RECENT_TIMESTAMP + 1, MOST_RECENT_TIMESTAMP));

        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(null, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                        mockOnMouseLeaveAction, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries,
                        START_TIMESTAMP, PAUSED_TIMESTAMP_1, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, null,
                        BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                        mockOnMouseLeaveAction, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries,
                        START_TIMESTAMP, PAUSED_TIMESTAMP_1, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(ANIMATION_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, null, ON_PRESS_ACTIONS, null,
                        mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent, mockRenderingBoundaries, START_TIMESTAMP,
                        PAUSED_TIMESTAMP_1, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(ANIMATION_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null,
                        mockOnMouseOverAction, mockOnMouseLeaveAction, null,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent, mockRenderingBoundaries, START_TIMESTAMP,
                        PAUSED_TIMESTAMP_1, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(ANIMATION_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null,
                        mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS, null, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries,
                        START_TIMESTAMP, PAUSED_TIMESTAMP_1, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(ANIMATION_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null,
                        mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS,
                        RENDERING_AREA_PROVIDER, Z, null, mockContainingComponent, mockRenderingBoundaries, START_TIMESTAMP,
                        PAUSED_TIMESTAMP_1, MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(ANIMATION_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null,
                        mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent, null, START_TIMESTAMP, PAUSED_TIMESTAMP_1,
                        MOST_RECENT_TIMESTAMP));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(ANIMATION_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null,
                        mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent, mockRenderingBoundaries, START_TIMESTAMP,
                        PAUSED_TIMESTAMP_1, null));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableImpl(ANIMATION_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null,
                        mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent, mockRenderingBoundaries, START_TIMESTAMP,
                        MOST_RECENT_TIMESTAMP + 1, MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testAnimationId() {
        assertEquals(ANIMATION_SUPPORTING_ID, renderableWithMouseEvents.animationId());
        assertEquals(ANIMATION_NOT_SUPPORTING_ID,
                renderableWithoutMouseEvents.animationId());
    }

    @Test
    public void testGetAndSetBorderThicknessProvider() {
        assertSame(BORDER_THICKNESS_PROVIDER,
                renderableWithMouseEvents.getBorderThicknessProvider());
        assertSame(BORDER_THICKNESS_PROVIDER,
                renderableWithoutMouseEvents.getBorderThicknessProvider());

        FakeProviderAtTime<Float> newBorderThicknessProvider = new FakeProviderAtTime<>();

        renderableWithMouseEvents
                .setBorderThicknessProvider(newBorderThicknessProvider);
        renderableWithoutMouseEvents
                .setBorderThicknessProvider(newBorderThicknessProvider);

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
    public void testMostRecentTimestamp() {
        assertEquals(MOST_RECENT_TIMESTAMP,
                renderableWithMouseEvents.mostRecentTimestamp());
        assertEquals(MOST_RECENT_TIMESTAMP,
                renderableWithoutMouseEvents.mostRecentTimestamp());
    }

    @Test
    public void testStartAndEndTimestamps() {
        assertEquals(START_TIMESTAMP,
                renderableWithMouseEvents.startTimestamp());
        assertEquals(START_TIMESTAMP,
                renderableWithoutMouseEvents.startTimestamp());
        assertEquals(START_TIMESTAMP + ANIMATION_DURATION,
                renderableWithMouseEvents.endTimestamp());
        assertEquals(START_TIMESTAMP + ANIMATION_DURATION,
                renderableWithoutMouseEvents.endTimestamp());
    }

    @Test
    public void testReportPause() {
        renderableWithMouseEvents.reportPause(PAUSED_TIMESTAMP_2);
        renderableWithoutMouseEvents.reportPause(PAUSED_TIMESTAMP_2);

        assertEquals(PAUSED_TIMESTAMP_2,
                renderableWithMouseEvents.pausedTimestamp());
        assertEquals(PAUSED_TIMESTAMP_2,
                renderableWithoutMouseEvents.pausedTimestamp());
    }

    @Test
    public void testReportUnpauseUpdatesStartAndEndTimestamps() {
        long pauseDuration = 789789L;

        renderableWithMouseEvents.reportPause(PAUSED_TIMESTAMP_2);
        renderableWithoutMouseEvents.reportPause(PAUSED_TIMESTAMP_2);

        assertEquals(PAUSED_TIMESTAMP_2,
                renderableWithMouseEvents.pausedTimestamp());
        assertEquals(PAUSED_TIMESTAMP_2,
                renderableWithoutMouseEvents.pausedTimestamp());

        renderableWithMouseEvents.reportUnpause(PAUSED_TIMESTAMP_2 + pauseDuration);
        renderableWithoutMouseEvents
                .reportUnpause(PAUSED_TIMESTAMP_2 + pauseDuration);

        assertEquals(START_TIMESTAMP + pauseDuration,
                renderableWithMouseEvents.startTimestamp());
        assertEquals(START_TIMESTAMP + pauseDuration,
                renderableWithoutMouseEvents.startTimestamp());
        assertNull(renderableWithMouseEvents.pausedTimestamp());
        assertEquals(START_TIMESTAMP + ANIMATION_DURATION + pauseDuration,
                renderableWithMouseEvents.endTimestamp());
        assertEquals(START_TIMESTAMP + ANIMATION_DURATION + pauseDuration,
                renderableWithoutMouseEvents.endTimestamp());
        assertNull(renderableWithoutMouseEvents.pausedTimestamp());
    }

    @Test
    public void testPauseWhilePaused() {
        renderableWithMouseEvents.reportPause(PAUSED_TIMESTAMP_2);
        renderableWithoutMouseEvents.reportPause(PAUSED_TIMESTAMP_2);

        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.reportPause(PAUSED_TIMESTAMP_2));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithoutMouseEvents.reportPause(PAUSED_TIMESTAMP_2));
    }

    @Test
    public void testUnpauseWhileUnpaused() {
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.reportUnpause(999999L));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithoutMouseEvents.reportUnpause(999999L));
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
        String id1 = "id1";
        String id2 = "id2";
        String id3 = "id3";

        renderableWithMouseEvents.setOnPress(0, new FakeAction<>(id1));
        renderableWithMouseEvents.setOnPress(2, new FakeAction<>(id2));
        renderableWithMouseEvents.setOnPress(7, new FakeAction<>(id3));
        renderableWithMouseEvents.setOnPress(2, null);

        Map<Integer, String> pressActionIds =
                renderableWithMouseEvents.pressActionIds();

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
        String id1 = "id1";
        String id2 = "id2";
        String id3 = "id3";

        renderableWithMouseEvents.setOnRelease(0, new FakeAction<>(id1));
        renderableWithMouseEvents.setOnRelease(2, new FakeAction<>(id2));
        renderableWithMouseEvents.setOnRelease(7, new FakeAction<>(id3));
        renderableWithMouseEvents.setOnRelease(2, null);

        Map<Integer, String> releaseActionIds =
                renderableWithMouseEvents.releaseActionIds();

        assertNotNull(releaseActionIds);
        assertEquals(2, releaseActionIds.size());
        assertEquals(id1, releaseActionIds.get(0));
        assertEquals(id3, releaseActionIds.get(7));
    }

    @Test
    public void testPressOrReleaseMethodsWithInvalidButtons() {
        long timestamp = 456456L;

        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.setOnPress(-1, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.setOnRelease(-1, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.press(-1, timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.press(-1, timestamp + 1));

        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.setOnPress(8, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.setOnRelease(8, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.press(8, timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.press(8, timestamp + 3));
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
        String mouseOverActionId = "mouseOverActionId";

        assertThrows(UnsupportedOperationException.class, () ->
                renderableWithoutMouseEvents.mouseOverActionId());

        renderableWithMouseEvents.setOnMouseOver(null);

        assertNull(renderableWithMouseEvents.mouseOverActionId());

        renderableWithMouseEvents
                .setOnMouseOver(new FakeAction<>(mouseOverActionId));

        assertEquals(mouseOverActionId,
                renderableWithMouseEvents.mouseOverActionId());
    }

    @Test
    public void testMouseLeaveAndSetOnMouseLeave() {
        assertThrows(UnsupportedOperationException.class, () ->
                renderableWithoutMouseEvents.mouseLeave(0L));
        assertThrows(UnsupportedOperationException.class, () ->
                renderableWithoutMouseEvents.setOnMouseLeave(
                        mockOnMouseLeaveAction));

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
        String mouseLeaveActionId = "mouseLeaveActionId";

        assertThrows(UnsupportedOperationException.class, () ->
                renderableWithoutMouseEvents.mouseLeaveActionId());

        renderableWithMouseEvents.setOnMouseLeave(null);

        assertNull(renderableWithMouseEvents.mouseLeaveActionId());

        renderableWithMouseEvents
                .setOnMouseLeave(new FakeAction<>(mouseLeaveActionId));

        assertEquals(mouseLeaveActionId,
                renderableWithMouseEvents.mouseLeaveActionId());
    }

    @Test
    public void testProvide() {
        int msAfterStartTimestampForMidpointFrame = 333;

        renderableWithMouseEvents.provide(START_TIMESTAMP - 1);
        renderableWithMouseEvents.provide(START_TIMESTAMP);
        renderableWithMouseEvents
                .provide(START_TIMESTAMP + msAfterStartTimestampForMidpointFrame);
        renderableWithMouseEvents.provide(START_TIMESTAMP + ANIMATION_DURATION);
        renderableWithMouseEvents
                .provide(START_TIMESTAMP + ANIMATION_DURATION + 1);

        renderableWithoutMouseEvents.provide(START_TIMESTAMP - 1);
        renderableWithoutMouseEvents.provide(START_TIMESTAMP);
        renderableWithoutMouseEvents
                .provide(START_TIMESTAMP + msAfterStartTimestampForMidpointFrame);
        renderableWithoutMouseEvents.provide(START_TIMESTAMP + ANIMATION_DURATION);
        renderableWithoutMouseEvents
                .provide(START_TIMESTAMP + ANIMATION_DURATION + 1);

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

        assertEquals(5, ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.size());
        assertEquals(0,
                (int) ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.get(0).FIRST);
        assertEquals(0,
                (int) ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.get(1).FIRST);
        assertEquals(msAfterStartTimestampForMidpointFrame,
                (int) ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.get(2).FIRST);
        assertEquals(ANIMATION_DURATION,
                (int) ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.get(3).FIRST);
        assertEquals(ANIMATION_DURATION,
                (int) ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.get(4).FIRST);
    }

    @Test
    public void testProvideWhenPaused() {
        renderableWithMouseEvents.reportPause(PAUSED_TIMESTAMP_2);
        renderableWithoutMouseEvents.reportPause(PAUSED_TIMESTAMP_2);

        renderableWithMouseEvents.provide(789789L);
        renderableWithoutMouseEvents.provide(789789L);

        assertEquals(1, ANIMATION_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.size());
        assertEquals(PAUSED_TIMESTAMP_2 - START_TIMESTAMP,
                (int) ANIMATION_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.getFirst().FIRST);
        assertEquals(1, ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.size());
        assertEquals(PAUSED_TIMESTAMP_2 - START_TIMESTAMP,
                (int) ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.getFirst().FIRST);
    }

    @Test
    public void testUnpauseUpdatesStartAndEndTimestamps() {
        long pauseTimestamp = 777777L;
        long unpauseTimestamp = 888888L;

        renderableWithMouseEvents.reportPause(pauseTimestamp);
        renderableWithoutMouseEvents.reportPause(pauseTimestamp);
        renderableWithMouseEvents.reportUnpause(unpauseTimestamp);
        renderableWithoutMouseEvents.reportUnpause(unpauseTimestamp);

        assertEquals(START_TIMESTAMP + (unpauseTimestamp - pauseTimestamp),
                renderableWithMouseEvents.startTimestamp());
        assertEquals(START_TIMESTAMP + ANIMATION_DURATION +
                        (unpauseTimestamp - pauseTimestamp),
                renderableWithMouseEvents.endTimestamp());
        assertEquals(START_TIMESTAMP + (unpauseTimestamp - pauseTimestamp),
                renderableWithoutMouseEvents.startTimestamp());
        assertEquals(START_TIMESTAMP + ANIMATION_DURATION +
                        (unpauseTimestamp - pauseTimestamp),
                renderableWithoutMouseEvents.endTimestamp());
    }

    @Test
    public void testMouseEventPauseUnpauseAndProvideCallsToOutdatedTimestamps() {
        long timestamp = 456456L;
        RENDERING_AREA_PROVIDER.ProvidedValue = floatBoxOf(0f, 0f, 1f, 1f);
        ANIMATION_SUPPORTING_MOUSE_EVENTS.AnimationFrameSnippet = new FakeAnimationFrameSnippet();

        renderableWithMouseEvents.press(0, timestamp);
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.press(0, timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.release(0, timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.mouseOver(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.mouseLeave(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.reportPause(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.reportUnpause(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.provide(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(0f, 0f), timestamp - 1));

        renderableWithMouseEvents.release(0, timestamp + 1);
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.press(0, timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.release(0, timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.mouseOver(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.mouseLeave(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.reportPause(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.reportUnpause(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.provide(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(0f, 0f), timestamp));

        renderableWithMouseEvents.mouseOver(timestamp + 2);
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.press(0, timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.release(0, timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.mouseOver(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.mouseLeave(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.reportPause(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.reportUnpause(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.provide(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(0f, 0f), timestamp + 1));

        renderableWithMouseEvents.mouseLeave(timestamp + 3);
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.press(0, timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.release(0, timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.mouseOver(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.mouseLeave(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.reportPause(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.reportUnpause(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.provide(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(0f, 0f), timestamp + 2));

        renderableWithMouseEvents.capturesMouseEventAtPoint(vertexOf(0f, 0f),
                timestamp + 4);
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.press(0, timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.release(0, timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.mouseOver(timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.mouseLeave(timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.reportPause(timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.reportUnpause(timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.provide(timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(0f, 0f), timestamp + 3));

        renderableWithMouseEvents.reportPause(timestamp + 5);
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.press(0, timestamp + 4));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.release(0, timestamp + 4));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.mouseOver(timestamp + 4));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.mouseLeave(timestamp + 4));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.reportPause(timestamp + 4));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.reportUnpause(timestamp + 4));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.provide(timestamp + 4));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(0f, 0f), timestamp + 4));

        renderableWithMouseEvents.reportUnpause(timestamp + 6);
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.press(0, timestamp + 5));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.release(0, timestamp + 5));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.mouseOver(timestamp + 5));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.mouseLeave(timestamp + 5));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.reportPause(timestamp + 5));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.reportUnpause(timestamp + 5));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.provide(timestamp + 5));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(0f, 0f), timestamp + 5));

        renderableWithMouseEvents.provide(timestamp + 7);
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.press(0, timestamp + 6));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.release(0, timestamp + 6));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.mouseOver(timestamp + 6));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.mouseLeave(timestamp + 6));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.reportPause(timestamp + 6));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.reportUnpause(timestamp + 6));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.provide(timestamp + 6));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(0f, 0f), timestamp + 6));
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
        ANIMATION_SUPPORTING_MOUSE_EVENTS.AnimationFrameSnippet = new FakeAnimationFrameSnippet();
        ANIMATION_SUPPORTING_MOUSE_EVENTS.AnimationFrameSnippet.LeftX = 250;
        ANIMATION_SUPPORTING_MOUSE_EVENTS.AnimationFrameSnippet.RightX = 750;
        ANIMATION_SUPPORTING_MOUSE_EVENTS.AnimationFrameSnippet.TopY = 1000;
        ANIMATION_SUPPORTING_MOUSE_EVENTS.AnimationFrameSnippet.BottomY = 2500;
        ((FakeImage) ANIMATION_SUPPORTING_MOUSE_EVENTS.AnimationFrameSnippet.Image).Width = 1000;
        ((FakeImage) ANIMATION_SUPPORTING_MOUSE_EVENTS.AnimationFrameSnippet.Image).Height = 3000;
        RENDERING_AREA_PROVIDER.ProvidedValue = floatBoxOf(-0.5f, -2f, 0.75f, 0.5f);

        boolean capturesMouseEventAtPoint =
                renderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(0.123f, 0.456f), 789L);

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
        assertEquals(1, RENDERING_AREA_PROVIDER.TimestampInputs.size());
        assertEquals(789L, (long) RENDERING_AREA_PROVIDER.TimestampInputs.getFirst());
    }

    @Test
    public void testCapturesMouseEventAtPointDoesNotExceedRenderingBoundaries() {
        ANIMATION_SUPPORTING_MOUSE_EVENTS.AnimationFrameSnippet = new FakeAnimationFrameSnippet();
        ((FakeImage) ANIMATION_SUPPORTING_MOUSE_EVENTS.AnimationFrameSnippet.Image)
                .SupportsMouseEventCapturing = true;
        RENDERING_AREA_PROVIDER.ProvidedValue = WHOLE_SCREEN;
        when(mockRenderingBoundaries.currentBoundaries()).thenReturn(floatBoxOf(0f, 0f, 0.5f, 1f));

        assertTrue(renderableWithMouseEvents.capturesMouseEventAtPoint(
                vertexOf(0.499f, 0f), MOST_RECENT_TIMESTAMP));
        assertFalse(renderableWithMouseEvents.capturesMouseEventAtPoint(
                vertexOf(0.501f, 0f), MOST_RECENT_TIMESTAMP));
    }

    @Test
    public void testCapturesMouseEventAtPointWithInvalidArgs() {
        float verySmallNumber = 0.0001f;

        assertThrows(UnsupportedOperationException.class, () ->
                renderableWithoutMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(.5f, .5f), 0L));

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
    public void testComponent() {
        assertSame(mockContainingComponent,
                renderableWithMouseEvents.component());
        assertSame(mockContainingComponent,
                renderableWithoutMouseEvents.component());
    }

    @Test
    public void testSetComponent() {
        ((FiniteAnimationRenderableImpl) renderableWithMouseEvents).setComponent(null);

        assertNull(renderableWithMouseEvents.component());
    }

    @Test
    public void testDelete() {
        renderableWithMouseEvents.delete();
        assertNull(renderableWithMouseEvents.component());
        assertTrue(renderableWithMouseEvents.isDeleted());

        renderableWithoutMouseEvents.delete();
        assertNull(renderableWithoutMouseEvents.component());
        assertTrue(renderableWithoutMouseEvents.isDeleted());
    }

    @Test
    public void testUuid() {
        assertSame(UUID, renderableWithMouseEvents.uuid());
        assertSame(UUID, renderableWithoutMouseEvents.uuid());
    }
}
