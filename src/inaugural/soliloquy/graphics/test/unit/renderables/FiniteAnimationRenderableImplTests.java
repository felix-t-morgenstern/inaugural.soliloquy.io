package inaugural.soliloquy.graphics.test.unit.renderables;

import inaugural.soliloquy.graphics.renderables.FiniteAnimationRenderableImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.infrastructure.Pair;
import soliloquy.specs.graphics.renderables.FiniteAnimationRenderable;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertSame;

class FiniteAnimationRenderableImplTests {
    int ANIMATION_DURATION = 555;
    private final String ANIMATION_SUPPORTING_ID = "animationSupportingId";
    private final FakeAnimation ANIMATION_SUPPORTING_MOUSE_EVENTS =
            new FakeAnimation(ANIMATION_SUPPORTING_ID, ANIMATION_DURATION, true);
    private final String ANIMATION_NOT_SUPPORTING_ID = "animationNotSupportingId";
    private final FakeAnimation ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS =
            new FakeAnimation(ANIMATION_NOT_SUPPORTING_ID, ANIMATION_DURATION, false);
    private final FakeProviderAtTime<Float> BORDER_THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Color> BORDER_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final HashMap<Integer, Action<Long>> ON_PRESS_ACTIONS = new HashMap<>();
    private final FakeAction<Long> ON_PRESS_ACTION = new FakeAction<>();
    private final FakeAction<Long> ON_MOUSE_OVER = new FakeAction<>();
    private final FakeAction<Long> ON_MOUSE_LEAVE = new FakeAction<>();
    private final ArrayList<ProviderAtTime<ColorShift>> COLOR_SHIFT_PROVIDERS = new ArrayList<>();
    private final FakeStaticProvider<FloatBox> RENDERING_AREA_PROVIDER =
            new FakeStaticProvider<>(null);
    private final int Z = 123;
    private final FakeEntityUuid UUID = new FakeEntityUuid();
    private final Consumer<Renderable>
            FINITE_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER =
            renderable -> _finiteAnimationRenderableWithMouseEventsUpdateZIndexInContainerInput =
                    renderable;
    private final Consumer<Renderable>
            FINITE_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER =
            renderable -> _finiteAnimationRenderableWithoutMouseEventsUpdateZIndexInContainerInput =
                    renderable;
    private final Consumer<Renderable>
            FINITE_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER =
            renderable -> _finiteAnimationRenderableWithMouseEventsRemoveFromContainerInput =
                    renderable;
    private final Consumer<Renderable>
            FINITE_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER =
            renderable -> _finiteAnimationRenderableWithoutMouseEventsRemoveFromContainerInput =
                    renderable;

    private final long START_TIMESTAMP = 111L;
    private final Long PAUSED_TIMESTAMP_1 = -456L;
    private final Long PAUSED_TIMESTAMP_2 = 456L;
    private final Long MOST_RECENT_TIMESTAMP = -123L;

    private static Renderable _finiteAnimationRenderableWithMouseEventsRemoveFromContainerInput;
    private static Renderable _finiteAnimationRenderableWithoutMouseEventsRemoveFromContainerInput;
    private static Renderable _finiteAnimationRenderableWithMouseEventsUpdateZIndexInContainerInput;
    private static Renderable _finiteAnimationRenderableWithoutMouseEventsUpdateZIndexInContainerInput;

    private FiniteAnimationRenderable _finiteAnimationRenderableWithMouseEvents;
    private FiniteAnimationRenderable _finiteAnimationRenderableWithoutMouseEvents;


    @BeforeEach
    void setUp() {
        _finiteAnimationRenderableWithMouseEvents = new FiniteAnimationRenderableImpl(
                ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                FINITE_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                FINITE_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER,
                START_TIMESTAMP, null, MOST_RECENT_TIMESTAMP);
        _finiteAnimationRenderableWithoutMouseEvents = new FiniteAnimationRenderableImpl(
                ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                FINITE_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                FINITE_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER,
                START_TIMESTAMP, null, MOST_RECENT_TIMESTAMP);
    }

    @Test
    void testConstructorWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class, () -> new FiniteAnimationRenderableImpl(
                null, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                FINITE_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                FINITE_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER,
                START_TIMESTAMP, PAUSED_TIMESTAMP_1, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> new FiniteAnimationRenderableImpl(
                ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, null,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                FINITE_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                FINITE_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER,
                START_TIMESTAMP, PAUSED_TIMESTAMP_1, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> new FiniteAnimationRenderableImpl(
                ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                null, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                FINITE_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                FINITE_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER,
                START_TIMESTAMP, PAUSED_TIMESTAMP_1, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> new FiniteAnimationRenderableImpl(
                ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, null, RENDERING_AREA_PROVIDER, Z, UUID,
                FINITE_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                FINITE_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER,
                START_TIMESTAMP, PAUSED_TIMESTAMP_1, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> new FiniteAnimationRenderableImpl(
                ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, null, Z, UUID,
                FINITE_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                FINITE_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER,
                START_TIMESTAMP, PAUSED_TIMESTAMP_1, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> new FiniteAnimationRenderableImpl(
                ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, null,
                FINITE_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                FINITE_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER,
                START_TIMESTAMP, PAUSED_TIMESTAMP_1, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> new FiniteAnimationRenderableImpl(
                ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                null,
                FINITE_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER,
                START_TIMESTAMP, PAUSED_TIMESTAMP_1, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> new FiniteAnimationRenderableImpl(
                ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                FINITE_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                null,
                START_TIMESTAMP, PAUSED_TIMESTAMP_1, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> new FiniteAnimationRenderableImpl(
                ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                FINITE_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                FINITE_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER,
                START_TIMESTAMP, PAUSED_TIMESTAMP_1, null
        ));
        assertThrows(IllegalArgumentException.class, () -> new FiniteAnimationRenderableImpl(
                ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                FINITE_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                FINITE_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER,
                START_TIMESTAMP, MOST_RECENT_TIMESTAMP + 1, MOST_RECENT_TIMESTAMP
        ));


        assertThrows(IllegalArgumentException.class, () -> new FiniteAnimationRenderableImpl(
                null, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                FINITE_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                FINITE_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER,
                START_TIMESTAMP, PAUSED_TIMESTAMP_1, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> new FiniteAnimationRenderableImpl(
                ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, null,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                FINITE_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                FINITE_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER,
                START_TIMESTAMP, PAUSED_TIMESTAMP_1, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> new FiniteAnimationRenderableImpl(
                ANIMATION_SUPPORTING_MOUSE_EVENTS, null,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                FINITE_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                FINITE_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER,
                START_TIMESTAMP, PAUSED_TIMESTAMP_1, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> new FiniteAnimationRenderableImpl(
                ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                null, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                FINITE_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                FINITE_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER,
                START_TIMESTAMP, PAUSED_TIMESTAMP_1, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> new FiniteAnimationRenderableImpl(
                ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, null, RENDERING_AREA_PROVIDER, Z, UUID,
                FINITE_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                FINITE_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER,
                START_TIMESTAMP, PAUSED_TIMESTAMP_1, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> new FiniteAnimationRenderableImpl(
                ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, null, Z, UUID,
                FINITE_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                FINITE_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER,
                START_TIMESTAMP, PAUSED_TIMESTAMP_1, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> new FiniteAnimationRenderableImpl(
                ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, null,
                FINITE_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                FINITE_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER,
                START_TIMESTAMP, PAUSED_TIMESTAMP_1, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> new FiniteAnimationRenderableImpl(
                ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                null,
                FINITE_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER,
                START_TIMESTAMP, PAUSED_TIMESTAMP_1, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> new FiniteAnimationRenderableImpl(
                ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                FINITE_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                null,
                START_TIMESTAMP, PAUSED_TIMESTAMP_1, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> new FiniteAnimationRenderableImpl(
                ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                FINITE_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                FINITE_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER,
                START_TIMESTAMP, PAUSED_TIMESTAMP_1, null
        ));
        assertThrows(IllegalArgumentException.class, () -> new FiniteAnimationRenderableImpl(
                ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                FINITE_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                FINITE_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER,
                START_TIMESTAMP, MOST_RECENT_TIMESTAMP + 1, MOST_RECENT_TIMESTAMP
        ));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(FiniteAnimationRenderable.class.getCanonicalName(),
                _finiteAnimationRenderableWithMouseEvents.getInterfaceName());
    }

    @Test
    void testAnimationId() {
        assertEquals(ANIMATION_SUPPORTING_ID,
                _finiteAnimationRenderableWithMouseEvents.animationId());
        assertEquals(ANIMATION_NOT_SUPPORTING_ID,
                _finiteAnimationRenderableWithoutMouseEvents.animationId());
    }

    @Test
    void testGetAndSetBorderThicknessProvider() {
        assertSame(BORDER_THICKNESS_PROVIDER,
                _finiteAnimationRenderableWithMouseEvents.getBorderThicknessProvider());
        assertSame(BORDER_THICKNESS_PROVIDER,
                _finiteAnimationRenderableWithoutMouseEvents.getBorderThicknessProvider());

        FakeProviderAtTime<Float> newBorderThicknessProvider = new FakeProviderAtTime<>();

        _finiteAnimationRenderableWithMouseEvents.setBorderThicknessProvider(newBorderThicknessProvider);
        _finiteAnimationRenderableWithoutMouseEvents.setBorderThicknessProvider(newBorderThicknessProvider);

        assertSame(newBorderThicknessProvider,
                _finiteAnimationRenderableWithMouseEvents.getBorderThicknessProvider());
        assertSame(newBorderThicknessProvider,
                _finiteAnimationRenderableWithoutMouseEvents.getBorderThicknessProvider());
    }

    @Test
    void testSetBorderThicknessProviderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.setBorderThicknessProvider(null));
    }

    @Test
    void testGetAndSetBorderColorProvider() {
        assertSame(BORDER_COLOR_PROVIDER,
                _finiteAnimationRenderableWithMouseEvents.getBorderColorProvider());
        assertSame(BORDER_COLOR_PROVIDER,
                _finiteAnimationRenderableWithoutMouseEvents.getBorderColorProvider());

        FakeProviderAtTime<Color> newBorderColorProvider = new FakeProviderAtTime<>();

        _finiteAnimationRenderableWithMouseEvents.setBorderColorProvider(newBorderColorProvider);
        _finiteAnimationRenderableWithoutMouseEvents.setBorderColorProvider(newBorderColorProvider);

        assertSame(newBorderColorProvider,
                _finiteAnimationRenderableWithMouseEvents.getBorderColorProvider());
        assertSame(newBorderColorProvider,
                _finiteAnimationRenderableWithoutMouseEvents.getBorderColorProvider());
    }

    @Test
    void testSetBorderColorProviderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.setBorderColorProvider(null));
    }

    @Test
    void testMostRecentTimestamp() {
        assertEquals(MOST_RECENT_TIMESTAMP,
                _finiteAnimationRenderableWithMouseEvents.mostRecentTimestamp());
        assertEquals(MOST_RECENT_TIMESTAMP,
                _finiteAnimationRenderableWithoutMouseEvents.mostRecentTimestamp());
    }

    @Test
    void testStartAndEndTimestamps() {
        assertEquals(START_TIMESTAMP,
                _finiteAnimationRenderableWithMouseEvents.startTimestamp());
        assertEquals(START_TIMESTAMP,
                _finiteAnimationRenderableWithoutMouseEvents.startTimestamp());
        assertEquals(START_TIMESTAMP + ANIMATION_DURATION,
                _finiteAnimationRenderableWithMouseEvents.endTimestamp());
        assertEquals(START_TIMESTAMP + ANIMATION_DURATION,
                _finiteAnimationRenderableWithoutMouseEvents.endTimestamp());
    }

    @Test
    void testReportPause() {
        _finiteAnimationRenderableWithMouseEvents.reportPause(PAUSED_TIMESTAMP_2);
        _finiteAnimationRenderableWithoutMouseEvents.reportPause(PAUSED_TIMESTAMP_2);

        assertEquals(PAUSED_TIMESTAMP_2, _finiteAnimationRenderableWithMouseEvents.pausedTimestamp());
        assertEquals(PAUSED_TIMESTAMP_2, _finiteAnimationRenderableWithoutMouseEvents.pausedTimestamp());
    }

    @Test
    void testReportUnpauseUpdatesStartAndEndTimestamps() {
        long pauseDuration = 789789L;

        _finiteAnimationRenderableWithMouseEvents.reportPause(PAUSED_TIMESTAMP_2);
        _finiteAnimationRenderableWithoutMouseEvents.reportPause(PAUSED_TIMESTAMP_2);

        assertEquals(PAUSED_TIMESTAMP_2,
                _finiteAnimationRenderableWithMouseEvents.pausedTimestamp());
        assertEquals(PAUSED_TIMESTAMP_2,
                _finiteAnimationRenderableWithoutMouseEvents.pausedTimestamp());

        _finiteAnimationRenderableWithMouseEvents.reportUnpause(PAUSED_TIMESTAMP_2 + pauseDuration);
        _finiteAnimationRenderableWithoutMouseEvents.reportUnpause(PAUSED_TIMESTAMP_2 + pauseDuration);

        assertEquals(START_TIMESTAMP + pauseDuration,
                _finiteAnimationRenderableWithMouseEvents.startTimestamp());
        assertEquals(START_TIMESTAMP + pauseDuration,
                _finiteAnimationRenderableWithoutMouseEvents.startTimestamp());
        assertNull(_finiteAnimationRenderableWithMouseEvents.pausedTimestamp());
        assertEquals(START_TIMESTAMP + ANIMATION_DURATION + pauseDuration,
                _finiteAnimationRenderableWithMouseEvents.endTimestamp());
        assertEquals(START_TIMESTAMP + ANIMATION_DURATION + pauseDuration,
                _finiteAnimationRenderableWithoutMouseEvents.endTimestamp());
        assertNull(_finiteAnimationRenderableWithoutMouseEvents.pausedTimestamp());
    }

    @Test
    void testPauseWhilePaused() {
        _finiteAnimationRenderableWithMouseEvents.reportPause(PAUSED_TIMESTAMP_2);
        _finiteAnimationRenderableWithoutMouseEvents.reportPause(PAUSED_TIMESTAMP_2);

        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.reportPause(PAUSED_TIMESTAMP_2));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithoutMouseEvents.reportPause(PAUSED_TIMESTAMP_2));
    }

    @Test
    void testUnpauseWhileUnpaused() {
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.reportUnpause(999999L));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithoutMouseEvents.reportUnpause(999999L));
    }

    @Test
    void testGetAndSetCapturesMouseEvents() {
        assertTrue(_finiteAnimationRenderableWithMouseEvents.getCapturesMouseEvents());
        assertFalse(_finiteAnimationRenderableWithoutMouseEvents.getCapturesMouseEvents());

        _finiteAnimationRenderableWithMouseEvents.setCapturesMouseEvents(false);
        assertThrows(UnsupportedOperationException.class, () ->
                _finiteAnimationRenderableWithoutMouseEvents.setCapturesMouseEvents(false));

        assertFalse(_finiteAnimationRenderableWithMouseEvents.getCapturesMouseEvents());
    }

    @Test
    void testPressAndSetOnPress() {
        assertThrows(UnsupportedOperationException.class, () ->
                _finiteAnimationRenderableWithoutMouseEvents.press(2, 0L));
        assertThrows(UnsupportedOperationException.class, () ->
                _finiteAnimationRenderableWithoutMouseEvents.setOnPress(2, new FakeAction<>()));

        _finiteAnimationRenderableWithMouseEvents.setOnPress(2, ON_PRESS_ACTION);

        long timestamp = 456456L;
        _finiteAnimationRenderableWithMouseEvents.press(2, timestamp);
        assertEquals(1, ON_PRESS_ACTION.NumberOfTimesCalled);
        assertEquals(1, ON_PRESS_ACTION.Inputs.size());
        assertEquals(timestamp, (long)ON_PRESS_ACTION.Inputs.get(0));

        FakeAction<Long> newOnPress = new FakeAction<>();
        _finiteAnimationRenderableWithMouseEvents.setOnPress(2, newOnPress);

        _finiteAnimationRenderableWithMouseEvents.press(2, timestamp + 1);

        assertEquals(1, newOnPress.NumberOfTimesCalled);
        assertEquals(1, newOnPress.Inputs.size());
        assertEquals(timestamp + 1, (long)newOnPress.Inputs.get(0));

        _finiteAnimationRenderableWithMouseEvents.press(0, timestamp + 2);

        assertEquals(1, newOnPress.NumberOfTimesCalled);
        assertEquals(1, newOnPress.Inputs.size());
        assertEquals(timestamp + 1, (long)newOnPress.Inputs.get(0));
    }

    @Test
    void testPressActionIds() {
        String id1 = "id1";
        String id2 = "id2";
        String id3 = "id3";

        _finiteAnimationRenderableWithMouseEvents.setOnPress(0, new FakeAction<>(id1));
        _finiteAnimationRenderableWithMouseEvents.setOnPress(2, new FakeAction<>(id2));
        _finiteAnimationRenderableWithMouseEvents.setOnPress(7, new FakeAction<>(id3));
        _finiteAnimationRenderableWithMouseEvents.setOnPress(2, null);

        Map<Integer, String> pressActionIds = _finiteAnimationRenderableWithMouseEvents.pressActionIds();

        assertNotNull(pressActionIds);
        assertEquals(2, pressActionIds.size());
        assertEquals(id1, pressActionIds.get(0));
        assertEquals(id3, pressActionIds.get(7));
    }

    @Test
    void testReleaseAndSetOnRelease() {
        assertThrows(UnsupportedOperationException.class, () ->
                _finiteAnimationRenderableWithoutMouseEvents.release(2, 0L));
        assertThrows(UnsupportedOperationException.class, () ->
                _finiteAnimationRenderableWithoutMouseEvents.setOnRelease(2, new FakeAction<>()));

        long timestamp = 456456L;
        _finiteAnimationRenderableWithMouseEvents.release(2, timestamp);

        FakeAction<Long> newOnRelease = new FakeAction<>();
        _finiteAnimationRenderableWithMouseEvents.setOnRelease(2, newOnRelease);

        _finiteAnimationRenderableWithMouseEvents.release(2, timestamp + 1);
        assertEquals(1, newOnRelease.NumberOfTimesCalled);
        assertEquals(1, newOnRelease.Inputs.size());
        assertEquals(timestamp + 1, (long)newOnRelease.Inputs.get(0));
    }

    @Test
    void testReleaseActionIds() {
        String id1 = "id1";
        String id2 = "id2";
        String id3 = "id3";

        _finiteAnimationRenderableWithMouseEvents.setOnRelease(0, new FakeAction<>(id1));
        _finiteAnimationRenderableWithMouseEvents.setOnRelease(2, new FakeAction<>(id2));
        _finiteAnimationRenderableWithMouseEvents.setOnRelease(7, new FakeAction<>(id3));
        _finiteAnimationRenderableWithMouseEvents.setOnRelease(2, null);

        Map<Integer, String> releaseActionIds =
                _finiteAnimationRenderableWithMouseEvents.releaseActionIds();

        assertNotNull(releaseActionIds);
        assertEquals(2, releaseActionIds.size());
        assertEquals(id1, releaseActionIds.get(0));
        assertEquals(id3, releaseActionIds.get(7));
    }

    @Test
    void testPressOrReleaseMethodsWithInvalidButtons() {
        long timestamp = 456456L;

        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.setOnPress(-1, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.setOnRelease(-1, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.press(-1, timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.press(-1, timestamp + 1));

        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.setOnPress(8, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.setOnRelease(8, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.press(8, timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.press(8, timestamp + 3));
    }

    @Test
    void testMouseOverAndSetOnMouseOver() {
        assertThrows(UnsupportedOperationException.class, () ->
                _finiteAnimationRenderableWithoutMouseEvents.mouseOver(0L));
        assertThrows(UnsupportedOperationException.class, () ->
                _finiteAnimationRenderableWithoutMouseEvents.setOnMouseOver(ON_MOUSE_OVER));

        long timestamp = 456456L;
        _finiteAnimationRenderableWithMouseEvents.mouseOver(timestamp);
        assertEquals(1, ON_MOUSE_OVER.NumberOfTimesCalled);
        assertEquals(1, ON_MOUSE_OVER.Inputs.size());
        assertEquals(timestamp, (long)ON_MOUSE_OVER.Inputs.get(0));

        FakeAction<Long> newOnMouseOver = new FakeAction<>();
        _finiteAnimationRenderableWithMouseEvents.setOnMouseOver(newOnMouseOver);

        _finiteAnimationRenderableWithMouseEvents.mouseOver(timestamp + 1);
        assertEquals(1, newOnMouseOver.NumberOfTimesCalled);
        assertEquals(1, newOnMouseOver.Inputs.size());
        assertEquals(timestamp + 1, (long)newOnMouseOver.Inputs.get(0));
    }

    @Test
    void testMouseOverActionId() {
        String mouseOverActionId = "mouseOverActionId";

        assertThrows(UnsupportedOperationException.class, () ->
                _finiteAnimationRenderableWithoutMouseEvents.mouseOverActionId());

        _finiteAnimationRenderableWithMouseEvents.setOnMouseOver(null);

        assertNull(_finiteAnimationRenderableWithMouseEvents.mouseOverActionId());

        _finiteAnimationRenderableWithMouseEvents.setOnMouseOver(new FakeAction<>(mouseOverActionId));

        assertEquals(mouseOverActionId, _finiteAnimationRenderableWithMouseEvents.mouseOverActionId());
    }

    @Test
    void testMouseLeaveAndSetOnMouseLeave() {
        assertThrows(UnsupportedOperationException.class, () ->
                _finiteAnimationRenderableWithoutMouseEvents.mouseLeave(0L));
        assertThrows(UnsupportedOperationException.class, () ->
                _finiteAnimationRenderableWithoutMouseEvents.setOnMouseLeave(ON_MOUSE_LEAVE));

        long timestamp = 456456L;
        _finiteAnimationRenderableWithMouseEvents.mouseLeave(timestamp);
        assertEquals(1, ON_MOUSE_LEAVE.NumberOfTimesCalled);
        assertEquals(1, ON_MOUSE_LEAVE.Inputs.size());
        assertEquals(timestamp, (long)ON_MOUSE_LEAVE.Inputs.get(0));

        FakeAction<Long> newOnMouseLeave = new FakeAction<>();
        _finiteAnimationRenderableWithMouseEvents.setOnMouseLeave(newOnMouseLeave);

        _finiteAnimationRenderableWithMouseEvents.mouseLeave(timestamp + 1);
        assertEquals(1, newOnMouseLeave.NumberOfTimesCalled);
        assertEquals(1, newOnMouseLeave.Inputs.size());
        assertEquals(timestamp + 1, (long)newOnMouseLeave.Inputs.get(0));
    }

    @Test
    void testMouseLeaveActionId() {
        String mouseLeaveActionId = "mouseLeaveActionId";

        assertThrows(UnsupportedOperationException.class, () ->
                _finiteAnimationRenderableWithoutMouseEvents.mouseLeaveActionId());

        _finiteAnimationRenderableWithMouseEvents.setOnMouseLeave(null);

        assertNull(_finiteAnimationRenderableWithMouseEvents.mouseLeaveActionId());

        _finiteAnimationRenderableWithMouseEvents.setOnMouseLeave(new FakeAction<>(mouseLeaveActionId));

        assertEquals(mouseLeaveActionId, _finiteAnimationRenderableWithMouseEvents.mouseLeaveActionId());
    }

    @Test
    void testProvide() {
        int msAfterStartTimestampForMidpointFrame = 333;

        _finiteAnimationRenderableWithMouseEvents.provide(START_TIMESTAMP - 1);
        _finiteAnimationRenderableWithMouseEvents.provide(START_TIMESTAMP);
        _finiteAnimationRenderableWithMouseEvents
                .provide(START_TIMESTAMP + msAfterStartTimestampForMidpointFrame);
        _finiteAnimationRenderableWithMouseEvents.provide(START_TIMESTAMP + ANIMATION_DURATION);
        _finiteAnimationRenderableWithMouseEvents
                .provide(START_TIMESTAMP + ANIMATION_DURATION + 1);

        _finiteAnimationRenderableWithoutMouseEvents.provide(START_TIMESTAMP - 1);
        _finiteAnimationRenderableWithoutMouseEvents.provide(START_TIMESTAMP);
        _finiteAnimationRenderableWithoutMouseEvents
                .provide(START_TIMESTAMP + msAfterStartTimestampForMidpointFrame);
        _finiteAnimationRenderableWithoutMouseEvents.provide(START_TIMESTAMP + ANIMATION_DURATION);
        _finiteAnimationRenderableWithoutMouseEvents
                .provide(START_TIMESTAMP + ANIMATION_DURATION + 1);

        assertEquals(5, ANIMATION_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.size());
        assertEquals(0,
                (int)ANIMATION_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.get(0).getItem1());
        assertEquals(0,
                (int)ANIMATION_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.get(1).getItem1());
        assertEquals(msAfterStartTimestampForMidpointFrame,
                (int)ANIMATION_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.get(2).getItem1());
        assertEquals(ANIMATION_DURATION,
                (int)ANIMATION_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.get(3).getItem1());
        assertEquals(ANIMATION_DURATION,
                (int)ANIMATION_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.get(4).getItem1());

        assertEquals(5, ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.size());
        assertEquals(0,
                (int)ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.get(0).getItem1());
        assertEquals(0,
                (int)ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.get(1).getItem1());
        assertEquals(msAfterStartTimestampForMidpointFrame,
                (int)ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.get(2).getItem1());
        assertEquals(ANIMATION_DURATION,
                (int)ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.get(3).getItem1());
        assertEquals(ANIMATION_DURATION,
                (int)ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.get(4).getItem1());
    }

    @Test
    void testProvideWhenPaused() {
        _finiteAnimationRenderableWithMouseEvents.reportPause(PAUSED_TIMESTAMP_2);
        _finiteAnimationRenderableWithoutMouseEvents.reportPause(PAUSED_TIMESTAMP_2);

        _finiteAnimationRenderableWithMouseEvents.provide(789789L);
        _finiteAnimationRenderableWithoutMouseEvents.provide(789789L);

        assertEquals(1, ANIMATION_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.size());
        assertEquals(PAUSED_TIMESTAMP_2 - START_TIMESTAMP,
                (int)ANIMATION_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.get(0).getItem1());
        assertEquals(1, ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.size());
        assertEquals(PAUSED_TIMESTAMP_2 - START_TIMESTAMP,
                (int)ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS.SnippetsProvided.get(0).getItem1());
    }

    @Test
    void testUnpauseUpdatesStartAndEndTimestamps() {
        long pauseTimestamp = 777777L;
        long unpauseTimestamp = 888888L;

        _finiteAnimationRenderableWithMouseEvents.reportPause(pauseTimestamp);
        _finiteAnimationRenderableWithoutMouseEvents.reportPause(pauseTimestamp);
        _finiteAnimationRenderableWithMouseEvents.reportUnpause(unpauseTimestamp);
        _finiteAnimationRenderableWithoutMouseEvents.reportUnpause(unpauseTimestamp);

        assertEquals(START_TIMESTAMP + (unpauseTimestamp - pauseTimestamp),
                _finiteAnimationRenderableWithMouseEvents.startTimestamp());
        assertEquals(START_TIMESTAMP + ANIMATION_DURATION +
                        (unpauseTimestamp - pauseTimestamp),
                _finiteAnimationRenderableWithMouseEvents.endTimestamp());
        assertEquals(START_TIMESTAMP + (unpauseTimestamp - pauseTimestamp),
                _finiteAnimationRenderableWithoutMouseEvents.startTimestamp());
        assertEquals(START_TIMESTAMP + ANIMATION_DURATION +
                        (unpauseTimestamp - pauseTimestamp),
                _finiteAnimationRenderableWithoutMouseEvents.endTimestamp());
    }

    @Test
    void testMouseEventPauseUnpauseAndProvideCallsToOutdatedTimestamps() {
        long timestamp = 456456L;
        RENDERING_AREA_PROVIDER.ProvidedValue = new FakeFloatBox(0f, 0f, 1f, 1f);
        ANIMATION_SUPPORTING_MOUSE_EVENTS.AnimationFrameSnippet = new FakeAnimationFrameSnippet();

        _finiteAnimationRenderableWithMouseEvents.press(0, timestamp);
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.press(0, timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.release(0, timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.mouseOver(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.mouseLeave(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.reportPause(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.reportUnpause(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.provide(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.capturesMouseEventAtPoint(0f, 0f, timestamp - 1));

        _finiteAnimationRenderableWithMouseEvents.release(0, timestamp + 1);
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.press(0, timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.release(0, timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.mouseOver(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.mouseLeave(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.reportPause(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.reportUnpause(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.provide(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.capturesMouseEventAtPoint(0f, 0f, timestamp));

        _finiteAnimationRenderableWithMouseEvents.mouseOver(timestamp + 2);
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.press(0, timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.release(0, timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.mouseOver(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.mouseLeave(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.reportPause(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.reportUnpause(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.provide(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.capturesMouseEventAtPoint(0f, 0f, timestamp + 1));

        _finiteAnimationRenderableWithMouseEvents.mouseLeave(timestamp + 3);
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.press(0, timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.release(0, timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.mouseOver(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.mouseLeave(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.reportPause(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.reportUnpause(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.provide(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.capturesMouseEventAtPoint(0f, 0f, timestamp + 2));

        _finiteAnimationRenderableWithMouseEvents.capturesMouseEventAtPoint(0f, 0f, timestamp + 4);
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.press(0, timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.release(0, timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.mouseOver(timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.mouseLeave(timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.reportPause(timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.reportUnpause(timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.provide(timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.capturesMouseEventAtPoint(0f, 0f, timestamp + 3));

        _finiteAnimationRenderableWithMouseEvents.reportPause(timestamp + 5);
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.press(0, timestamp + 4));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.release(0, timestamp + 4));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.mouseOver(timestamp + 4));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.mouseLeave(timestamp + 4));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.reportPause(timestamp + 4));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.reportUnpause(timestamp + 4));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.provide(timestamp + 4));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.capturesMouseEventAtPoint(0f, 0f, timestamp + 4));

        _finiteAnimationRenderableWithMouseEvents.reportUnpause(timestamp + 6);
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.press(0, timestamp + 5));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.release(0, timestamp + 5));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.mouseOver(timestamp + 5));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.mouseLeave(timestamp + 5));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.reportPause(timestamp + 5));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.reportUnpause(timestamp + 5));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.provide(timestamp + 5));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.capturesMouseEventAtPoint(0f, 0f, timestamp + 5));

        _finiteAnimationRenderableWithMouseEvents.provide(timestamp + 7);
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.press(0, timestamp + 6));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.release(0, timestamp + 6));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.mouseOver(timestamp + 6));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.mouseLeave(timestamp + 6));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.reportPause(timestamp + 6));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.reportUnpause(timestamp + 6));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.provide(timestamp + 6));
        assertThrows(IllegalArgumentException.class, () ->
                _finiteAnimationRenderableWithMouseEvents.capturesMouseEventAtPoint(0f, 0f, timestamp + 6));
    }

    @Test
    void testColorShiftProviders() {
        assertSame(COLOR_SHIFT_PROVIDERS,
                _finiteAnimationRenderableWithMouseEvents.colorShiftProviders());
        assertSame(COLOR_SHIFT_PROVIDERS,
                _finiteAnimationRenderableWithoutMouseEvents.colorShiftProviders());
    }

    @Test
    void testGetAndSetRenderingAreaProvider() {
        assertSame(RENDERING_AREA_PROVIDER,
                _finiteAnimationRenderableWithMouseEvents.getRenderingDimensionsProvider());
        assertSame(RENDERING_AREA_PROVIDER,
                _finiteAnimationRenderableWithoutMouseEvents.getRenderingDimensionsProvider());

        FakeProviderAtTime<FloatBox> newRenderingDimensionsProvider = new FakeProviderAtTime<>();

        _finiteAnimationRenderableWithMouseEvents
                .setRenderingDimensionsProvider(newRenderingDimensionsProvider);
        _finiteAnimationRenderableWithoutMouseEvents
                .setRenderingDimensionsProvider(newRenderingDimensionsProvider);

        assertSame(newRenderingDimensionsProvider,
                _finiteAnimationRenderableWithMouseEvents.getRenderingDimensionsProvider());
        assertSame(newRenderingDimensionsProvider,
                _finiteAnimationRenderableWithoutMouseEvents.getRenderingDimensionsProvider());
    }

    @Test
    void testGetAndSetZ() {
        assertSame(Z, _finiteAnimationRenderableWithMouseEvents.getZ());
        assertSame(Z, _finiteAnimationRenderableWithoutMouseEvents.getZ());

        int newZ = 456;

        _finiteAnimationRenderableWithMouseEvents.setZ(newZ);
        _finiteAnimationRenderableWithoutMouseEvents.setZ(newZ);

        assertEquals(newZ, _finiteAnimationRenderableWithMouseEvents.getZ());
        assertEquals(newZ, _finiteAnimationRenderableWithoutMouseEvents.getZ());

        assertSame(_finiteAnimationRenderableWithMouseEvents,
                _finiteAnimationRenderableWithMouseEventsUpdateZIndexInContainerInput);
        assertSame(_finiteAnimationRenderableWithoutMouseEvents,
                _finiteAnimationRenderableWithoutMouseEventsUpdateZIndexInContainerInput);
    }

    @Test
    void testCapturesMouseEventAtPoint() {
        ANIMATION_SUPPORTING_MOUSE_EVENTS.AnimationFrameSnippet = new FakeAnimationFrameSnippet();
        ANIMATION_SUPPORTING_MOUSE_EVENTS.AnimationFrameSnippet.LeftX = 250;
        ANIMATION_SUPPORTING_MOUSE_EVENTS.AnimationFrameSnippet.RightX = 750;
        ANIMATION_SUPPORTING_MOUSE_EVENTS.AnimationFrameSnippet.TopY = 1000;
        ANIMATION_SUPPORTING_MOUSE_EVENTS.AnimationFrameSnippet.BottomY = 2500;
        ((FakeImage) ANIMATION_SUPPORTING_MOUSE_EVENTS.AnimationFrameSnippet.Image).Width = 1000;
        ((FakeImage) ANIMATION_SUPPORTING_MOUSE_EVENTS.AnimationFrameSnippet.Image).Height = 3000;
        RENDERING_AREA_PROVIDER.ProvidedValue = new FakeFloatBox(-0.5f, -2f, 0.75f, 0.5f);

        boolean capturesMouseEventAtPoint =
                _finiteAnimationRenderableWithMouseEvents.capturesMouseEventAtPoint(0.123f, 0.456f, 789L);

        assertTrue(capturesMouseEventAtPoint);
        ArrayList<Pair<Integer, Integer>> capturesMouseEventsAtPixelInputs =
                ((FakeImage) ANIMATION_SUPPORTING_MOUSE_EVENTS.AnimationFrameSnippet.Image)
                        .CapturesMouseEventsAtPixelInputs;
        assertEquals(1, capturesMouseEventsAtPixelInputs.size());
        assertEquals(
                (int)((((0.123f - (-0.5f)) / (0.75f - (-0.5f))) * (750 - 250)) + 250),
                (int)capturesMouseEventsAtPixelInputs.get(0).getItem1());
        assertEquals(
                (int)((((0.456f - (-2.0f)) / (0.5f - (-2.0f))) * (2500 - 1000)) + 1000),
                (int)capturesMouseEventsAtPixelInputs.get(0).getItem2());
        assertEquals(1, RENDERING_AREA_PROVIDER.TimestampInputs.size());
        assertEquals(789L, (long)RENDERING_AREA_PROVIDER.TimestampInputs.get(0));
    }

    @Test
    void testCapturesMouseEventAtPointWithInvalidParams() {
        float verySmallNumber = 0.0001f;

        assertThrows(UnsupportedOperationException.class, () ->
                _finiteAnimationRenderableWithoutMouseEvents.capturesMouseEventAtPoint(.5f, .5f, 0L));

        RENDERING_AREA_PROVIDER.ProvidedValue = new FakeFloatBox(.5f, .5f, 1.5f, 1.5f);

        assertThrows(IllegalArgumentException.class, () -> _finiteAnimationRenderableWithMouseEvents
                .capturesMouseEventAtPoint(.5f - verySmallNumber, .75f, 0L));
        assertThrows(IllegalArgumentException.class, () -> _finiteAnimationRenderableWithMouseEvents
                .capturesMouseEventAtPoint(1f + verySmallNumber, .75f, 0L));
        assertThrows(IllegalArgumentException.class, () -> _finiteAnimationRenderableWithMouseEvents
                .capturesMouseEventAtPoint(.75f, .5f - verySmallNumber, 0L));
        assertThrows(IllegalArgumentException.class, () -> _finiteAnimationRenderableWithMouseEvents
                .capturesMouseEventAtPoint(.75f, 1.5f + verySmallNumber, 0L));

        RENDERING_AREA_PROVIDER.ProvidedValue = new FakeFloatBox(-0.5f, -0.5f, 0.5f, 0.5f);

        assertThrows(IllegalArgumentException.class, () -> _finiteAnimationRenderableWithMouseEvents
                .capturesMouseEventAtPoint(0f - verySmallNumber, .25f, 0L));
        assertThrows(IllegalArgumentException.class, () -> _finiteAnimationRenderableWithMouseEvents
                .capturesMouseEventAtPoint(0.5f + verySmallNumber, .25f, 0L));
        assertThrows(IllegalArgumentException.class, () -> _finiteAnimationRenderableWithMouseEvents
                .capturesMouseEventAtPoint(.25f, 0f - verySmallNumber, 0L));
        assertThrows(IllegalArgumentException.class, () -> _finiteAnimationRenderableWithMouseEvents
                .capturesMouseEventAtPoint(.25f, 0.5f + verySmallNumber, 0L));
    }

    @Test
    void testDelete() {
        _finiteAnimationRenderableWithMouseEvents.delete();
        assertSame(_finiteAnimationRenderableWithMouseEvents,
                _finiteAnimationRenderableWithMouseEventsRemoveFromContainerInput);

        _finiteAnimationRenderableWithoutMouseEvents.delete();
        assertSame(_finiteAnimationRenderableWithoutMouseEvents,
                _finiteAnimationRenderableWithoutMouseEventsRemoveFromContainerInput);
    }

    @Test
    void testUuid() {
        assertSame(UUID, _finiteAnimationRenderableWithMouseEvents.uuid());
        assertSame(UUID, _finiteAnimationRenderableWithoutMouseEvents.uuid());
    }

    @Test
    void testGetArchetype() {
        assertThrows(UnsupportedOperationException.class,
                _finiteAnimationRenderableWithMouseEvents::getArchetype);
        assertThrows(UnsupportedOperationException.class,
                _finiteAnimationRenderableWithoutMouseEvents::getArchetype);
    }
}
