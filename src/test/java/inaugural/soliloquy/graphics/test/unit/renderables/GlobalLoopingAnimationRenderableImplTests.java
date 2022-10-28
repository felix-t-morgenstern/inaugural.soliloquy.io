package inaugural.soliloquy.graphics.test.unit.renderables;

import inaugural.soliloquy.graphics.renderables.GlobalLoopingAnimationRenderableImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.GlobalLoopingAnimationRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.graphics.api.Constants.WHOLE_SCREEN;
import static inaugural.soliloquy.tools.random.Random.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalLoopingAnimationRenderableImplTests {
    private final FakeGlobalLoopingAnimation GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS =
            new FakeGlobalLoopingAnimation(true);
    private final FakeGlobalLoopingAnimation GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS =
            new FakeGlobalLoopingAnimation(false);
    private final FakeProviderAtTime<Float> BORDER_THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Color> BORDER_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final FakeAction<Long> ON_PRESS_ACTION = new FakeAction<>();
    private final HashMap<Integer, Action<Long>> ON_PRESS_ACTIONS = new HashMap<>();
    private final FakeAction<Long> ON_MOUSE_OVER = new FakeAction<>();
    private final FakeAction<Long> ON_MOUSE_LEAVE = new FakeAction<>();
    private final ArrayList<ProviderAtTime<ColorShift>> COLOR_SHIFT_PROVIDERS = new ArrayList<>();
    private final FakeStaticProvider<FloatBox> RENDERING_AREA_PROVIDER =
            new FakeStaticProvider<>(null);
    private final int Z = randomInt();
    private final long TIMESTAMP = randomLong();

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private RenderableStack _mockContainingStack;
    @Mock private RenderingBoundaries _mockRenderingBoundaries;

    private GlobalLoopingAnimationRenderable _globalLoopingAnimationRenderableWithMouseEvents;
    private GlobalLoopingAnimationRenderable _globalLoopingAnimationRenderableWithoutMouseEvents;

    @BeforeEach
    void setUp() {
        _mockContainingStack = mock(RenderableStack.class);
        _mockRenderingBoundaries = mock(RenderingBoundaries.class);
        when(_mockRenderingBoundaries.currentBoundaries()).thenReturn(WHOLE_SCREEN);

        ON_PRESS_ACTIONS.put(2, ON_PRESS_ACTION);

        _globalLoopingAnimationRenderableWithMouseEvents =
                new GlobalLoopingAnimationRenderableImpl(
                        GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS,
                        null, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS,
                        RENDERING_AREA_PROVIDER, Z, UUID, _mockContainingStack,
                        _mockRenderingBoundaries);
        _globalLoopingAnimationRenderableWithoutMouseEvents =
                new GlobalLoopingAnimationRenderableImpl(
                        GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS,
                        RENDERING_AREA_PROVIDER, Z, UUID, _mockContainingStack,
                        _mockRenderingBoundaries);
    }

    @Test
    void testConstructorWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                null, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                _mockContainingStack, _mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                _mockContainingStack, _mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, null,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                _mockContainingStack, _mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                null, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                _mockContainingStack, _mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                null, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                _mockContainingStack, _mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, null, RENDERING_AREA_PROVIDER, Z, UUID, _mockContainingStack,
                _mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, null, Z, UUID, _mockContainingStack,
                _mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, null,
                _mockContainingStack, _mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID, null,
                _mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                _mockContainingStack, null
        ));

        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                null, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                _mockContainingStack, _mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, null,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                _mockContainingStack, _mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                null, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID, _mockContainingStack,
                _mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                null, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID, _mockContainingStack,
                _mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, null, RENDERING_AREA_PROVIDER, Z, UUID, _mockContainingStack,
                _mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, null, Z, UUID, _mockContainingStack,
                _mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, null,
                _mockContainingStack, _mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                null, _mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                _mockContainingStack, null
        ));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(GlobalLoopingAnimationRenderable.class.getCanonicalName(),
                _globalLoopingAnimationRenderableWithMouseEvents.getInterfaceName());
    }

    @Test
    void testGetAndSetGlobalLoopingAnimation() {
        assertSame(GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS,
                _globalLoopingAnimationRenderableWithMouseEvents.getGlobalLoopingAnimation());
        assertSame(GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                _globalLoopingAnimationRenderableWithoutMouseEvents.getGlobalLoopingAnimation());

        FakeGlobalLoopingAnimation newGlobalLoopingAnimation =
                new FakeGlobalLoopingAnimation(true);

        _globalLoopingAnimationRenderableWithMouseEvents
                .setGlobalLoopingAnimation(newGlobalLoopingAnimation);
        _globalLoopingAnimationRenderableWithoutMouseEvents
                .setGlobalLoopingAnimation(newGlobalLoopingAnimation);

        assertSame(newGlobalLoopingAnimation,
                _globalLoopingAnimationRenderableWithMouseEvents.getGlobalLoopingAnimation());
        assertSame(newGlobalLoopingAnimation,
                _globalLoopingAnimationRenderableWithoutMouseEvents.getGlobalLoopingAnimation());
    }

    @Test
    void testSetGlobalLoopingAnimationWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.setGlobalLoopingAnimation(null));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithoutMouseEvents
                        .setGlobalLoopingAnimation(null));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.setGlobalLoopingAnimation(
                        new FakeGlobalLoopingAnimation(false)));
    }

    @Test
    void testGetAndSetBorderThicknessProvider() {
        assertSame(BORDER_THICKNESS_PROVIDER,
                _globalLoopingAnimationRenderableWithMouseEvents.getBorderThicknessProvider());
        assertSame(BORDER_THICKNESS_PROVIDER,
                _globalLoopingAnimationRenderableWithoutMouseEvents.getBorderThicknessProvider());

        FakeProviderAtTime<Float> newBorderThicknessProvider = new FakeProviderAtTime<>();

        _globalLoopingAnimationRenderableWithMouseEvents
                .setBorderThicknessProvider(newBorderThicknessProvider);
        _globalLoopingAnimationRenderableWithoutMouseEvents
                .setBorderThicknessProvider(newBorderThicknessProvider);

        assertSame(newBorderThicknessProvider,
                _globalLoopingAnimationRenderableWithMouseEvents.getBorderThicknessProvider());
        assertSame(newBorderThicknessProvider,
                _globalLoopingAnimationRenderableWithoutMouseEvents.getBorderThicknessProvider());
    }

    @Test
    void testSetBorderThicknessProviderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.setBorderThicknessProvider(null));
    }

    @Test
    void testSetBorderColorProviderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithoutMouseEvents.setBorderColorProvider(null));
    }

    @Test
    void testGetAndSetBorderColorProvider() {
        assertSame(BORDER_COLOR_PROVIDER,
                _globalLoopingAnimationRenderableWithMouseEvents.getBorderColorProvider());
        assertSame(BORDER_COLOR_PROVIDER,
                _globalLoopingAnimationRenderableWithoutMouseEvents.getBorderColorProvider());

        FakeProviderAtTime<Color> newBorderColorProvider = new FakeProviderAtTime<>();

        _globalLoopingAnimationRenderableWithMouseEvents
                .setBorderColorProvider(newBorderColorProvider);
        _globalLoopingAnimationRenderableWithoutMouseEvents
                .setBorderColorProvider(newBorderColorProvider);

        assertSame(newBorderColorProvider,
                _globalLoopingAnimationRenderableWithMouseEvents.getBorderColorProvider());
        assertSame(newBorderColorProvider,
                _globalLoopingAnimationRenderableWithoutMouseEvents.getBorderColorProvider());
    }

    @Test
    void testGetAndSetCapturesMouseEvents() {
        assertTrue(_globalLoopingAnimationRenderableWithMouseEvents.getCapturesMouseEvents());
        assertFalse(_globalLoopingAnimationRenderableWithoutMouseEvents.getCapturesMouseEvents());

        _globalLoopingAnimationRenderableWithMouseEvents.setCapturesMouseEvents(false);
        assertThrows(UnsupportedOperationException.class, () ->
                _globalLoopingAnimationRenderableWithoutMouseEvents.setCapturesMouseEvents(false));

        assertFalse(_globalLoopingAnimationRenderableWithMouseEvents.getCapturesMouseEvents());
    }

    @Test
    void testPressAndSetOnPress() {
        assertThrows(UnsupportedOperationException.class, () ->
                _globalLoopingAnimationRenderableWithoutMouseEvents.press(2, 0L));
        assertThrows(UnsupportedOperationException.class, () ->
                _globalLoopingAnimationRenderableWithoutMouseEvents.setOnPress(2,
                        new FakeAction<>()));

        _globalLoopingAnimationRenderableWithMouseEvents.setOnPress(2, ON_PRESS_ACTION);

        _globalLoopingAnimationRenderableWithMouseEvents.press(2, TIMESTAMP);
        assertEquals(1, ON_PRESS_ACTION.NumberOfTimesCalled);
        assertEquals(1, ON_PRESS_ACTION.Inputs.size());
        assertEquals(TIMESTAMP, (long) ON_PRESS_ACTION.Inputs.get(0));

        FakeAction<Long> newOnPress = new FakeAction<>();
        _globalLoopingAnimationRenderableWithMouseEvents.setOnPress(2, newOnPress);

        _globalLoopingAnimationRenderableWithMouseEvents.press(2, TIMESTAMP + 1);

        assertEquals(1, newOnPress.NumberOfTimesCalled);
        assertEquals(1, newOnPress.Inputs.size());
        assertEquals(TIMESTAMP + 1, (long) newOnPress.Inputs.get(0));

        _globalLoopingAnimationRenderableWithMouseEvents.press(0, TIMESTAMP + 2);

        assertEquals(1, newOnPress.NumberOfTimesCalled);
        assertEquals(1, newOnPress.Inputs.size());
        assertEquals(TIMESTAMP + 1, (long) newOnPress.Inputs.get(0));
    }

    @Test
    void testPressActionIds() {
        String id1 = "id1";
        String id2 = "id2";
        String id3 = "id3";

        _globalLoopingAnimationRenderableWithMouseEvents.setOnPress(0, new FakeAction<>(id1));
        _globalLoopingAnimationRenderableWithMouseEvents.setOnPress(2, new FakeAction<>(id2));
        _globalLoopingAnimationRenderableWithMouseEvents.setOnPress(7, new FakeAction<>(id3));
        _globalLoopingAnimationRenderableWithMouseEvents.setOnPress(2, null);

        Map<Integer, String> pressActionIds =
                _globalLoopingAnimationRenderableWithMouseEvents.pressActionIds();

        assertNotNull(pressActionIds);
        assertEquals(2, pressActionIds.size());
        assertEquals(id1, pressActionIds.get(0));
        assertEquals(id3, pressActionIds.get(7));
    }

    @Test
    void testReleaseAndSetOnRelease() {
        assertThrows(UnsupportedOperationException.class, () ->
                _globalLoopingAnimationRenderableWithoutMouseEvents.release(2, 0L));
        assertThrows(UnsupportedOperationException.class, () ->
                _globalLoopingAnimationRenderableWithoutMouseEvents.setOnRelease(2,
                        new FakeAction<>()));

        long timestamp = 456456L;
        _globalLoopingAnimationRenderableWithMouseEvents.release(2, timestamp);

        FakeAction<Long> newOnRelease = new FakeAction<>();
        _globalLoopingAnimationRenderableWithMouseEvents.setOnRelease(2, newOnRelease);

        _globalLoopingAnimationRenderableWithMouseEvents.release(2, timestamp + 1);
        assertEquals(1, newOnRelease.NumberOfTimesCalled);
        assertEquals(1, newOnRelease.Inputs.size());
        assertEquals(timestamp + 1, (long) newOnRelease.Inputs.get(0));
    }

    @Test
    void testReleaseActionIds() {
        String id1 = "id1";
        String id2 = "id2";
        String id3 = "id3";

        _globalLoopingAnimationRenderableWithMouseEvents.setOnRelease(0, new FakeAction<>(id1));
        _globalLoopingAnimationRenderableWithMouseEvents.setOnRelease(2, new FakeAction<>(id2));
        _globalLoopingAnimationRenderableWithMouseEvents.setOnRelease(7, new FakeAction<>(id3));
        _globalLoopingAnimationRenderableWithMouseEvents.setOnRelease(2, null);

        Map<Integer, String> releaseActionIds =
                _globalLoopingAnimationRenderableWithMouseEvents.releaseActionIds();

        assertNotNull(releaseActionIds);
        assertEquals(2, releaseActionIds.size());
        assertEquals(id1, releaseActionIds.get(0));
        assertEquals(id3, releaseActionIds.get(7));
    }

    @Test
    void testPressOrReleaseMethodsWithInvalidButtons() {
        long timestamp = 456456L;

        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.setOnPress(-1,
                        new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.setOnRelease(-1,
                        new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.press(-1, timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.press(-1, timestamp + 1));

        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.setOnPress(8,
                        new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.setOnRelease(8,
                        new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.press(8, timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.press(8, timestamp + 3));
    }

    @Test
    void testMouseOverAndSetOnMouseOver() {
        assertThrows(UnsupportedOperationException.class, () ->
                _globalLoopingAnimationRenderableWithoutMouseEvents.mouseOver(0L));
        assertThrows(UnsupportedOperationException.class, () ->
                _globalLoopingAnimationRenderableWithoutMouseEvents.setOnMouseOver(ON_MOUSE_OVER));

        long timestamp = 456456L;
        _globalLoopingAnimationRenderableWithMouseEvents.mouseOver(timestamp);
        assertEquals(1, ON_MOUSE_OVER.NumberOfTimesCalled);
        assertEquals(1, ON_MOUSE_OVER.Inputs.size());
        assertEquals(timestamp, (long) ON_MOUSE_OVER.Inputs.get(0));

        FakeAction<Long> newOnMouseOver = new FakeAction<>();
        _globalLoopingAnimationRenderableWithMouseEvents.setOnMouseOver(newOnMouseOver);

        _globalLoopingAnimationRenderableWithMouseEvents.mouseOver(timestamp + 1);
        assertEquals(1, newOnMouseOver.NumberOfTimesCalled);
        assertEquals(1, newOnMouseOver.Inputs.size());
        assertEquals(timestamp + 1, (long) newOnMouseOver.Inputs.get(0));
    }

    @Test
    void testMouseOverActionId() {
        String mouseOverActionId = "mouseOverActionId";

        assertThrows(UnsupportedOperationException.class, () ->
                _globalLoopingAnimationRenderableWithoutMouseEvents.mouseOverActionId());

        _globalLoopingAnimationRenderableWithMouseEvents.setOnMouseOver(null);

        assertNull(_globalLoopingAnimationRenderableWithMouseEvents.mouseOverActionId());

        _globalLoopingAnimationRenderableWithMouseEvents.setOnMouseOver(
                new FakeAction<>(mouseOverActionId));

        assertEquals(mouseOverActionId,
                _globalLoopingAnimationRenderableWithMouseEvents.mouseOverActionId());
    }

    @Test
    void testMouseLeaveAndSetOnMouseLeave() {
        assertThrows(UnsupportedOperationException.class, () ->
                _globalLoopingAnimationRenderableWithoutMouseEvents.mouseLeave(0L));
        assertThrows(UnsupportedOperationException.class, () ->
                _globalLoopingAnimationRenderableWithoutMouseEvents
                        .setOnMouseLeave(ON_MOUSE_LEAVE));

        long timestamp = 456456L;
        _globalLoopingAnimationRenderableWithMouseEvents.mouseLeave(timestamp);
        assertEquals(1, ON_MOUSE_LEAVE.NumberOfTimesCalled);
        assertEquals(1, ON_MOUSE_LEAVE.Inputs.size());
        assertEquals(timestamp, (long) ON_MOUSE_LEAVE.Inputs.get(0));

        FakeAction<Long> newOnMouseLeave = new FakeAction<>();
        _globalLoopingAnimationRenderableWithMouseEvents.setOnMouseLeave(newOnMouseLeave);

        _globalLoopingAnimationRenderableWithMouseEvents.mouseLeave(timestamp + 1);
        assertEquals(1, newOnMouseLeave.NumberOfTimesCalled);
        assertEquals(1, newOnMouseLeave.Inputs.size());
        assertEquals(timestamp + 1, (long) newOnMouseLeave.Inputs.get(0));
    }

    @Test
    void testMouseLeaveActionId() {
        String mouseLeaveActionId = "mouseLeaveActionId";

        assertThrows(UnsupportedOperationException.class, () ->
                _globalLoopingAnimationRenderableWithoutMouseEvents.mouseLeaveActionId());

        _globalLoopingAnimationRenderableWithMouseEvents.setOnMouseLeave(null);

        assertNull(_globalLoopingAnimationRenderableWithMouseEvents.mouseLeaveActionId());

        _globalLoopingAnimationRenderableWithMouseEvents.setOnMouseLeave(
                new FakeAction<>(mouseLeaveActionId));

        assertEquals(mouseLeaveActionId,
                _globalLoopingAnimationRenderableWithMouseEvents.mouseLeaveActionId());
    }

    @Test
    void testMouseEventCallsToOutdatedTimestamps() {
        RENDERING_AREA_PROVIDER.ProvidedValue = new FakeFloatBox(0f, 0f, 1f, 1f);
        GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS.Animation = new FakeAnimation(789789);

        long timestamp = 456456L;

        _globalLoopingAnimationRenderableWithMouseEvents.press(0, timestamp);
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.press(0, timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.release(0, timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.mouseOver(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.mouseLeave(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents
                        .capturesMouseEventAtPoint(Vertex.of(0f, 0f), timestamp - 1));

        _globalLoopingAnimationRenderableWithMouseEvents.release(0, timestamp + 1);
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.press(0, timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.release(0, timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.mouseOver(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.mouseLeave(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents
                        .capturesMouseEventAtPoint(Vertex.of(0f, 0f), timestamp));

        _globalLoopingAnimationRenderableWithMouseEvents.mouseOver(timestamp + 2);
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.press(0, timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.release(0, timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.mouseOver(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.mouseLeave(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents
                        .capturesMouseEventAtPoint(Vertex.of(0f, 0f), timestamp + 1));

        _globalLoopingAnimationRenderableWithMouseEvents.mouseLeave(timestamp + 3);
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.press(0, timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.release(0, timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.mouseOver(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.mouseLeave(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents
                        .capturesMouseEventAtPoint(Vertex.of(0f, 0f), timestamp + 2));

        _globalLoopingAnimationRenderableWithMouseEvents
                .capturesMouseEventAtPoint(Vertex.of(0f, 0f), timestamp + 4);
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.press(0, timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.release(0, timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.mouseOver(timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.mouseLeave(timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents
                        .capturesMouseEventAtPoint(Vertex.of(0f, 0f), timestamp + 3));
    }

    @Test
    void testColorShiftProviders() {
        assertSame(COLOR_SHIFT_PROVIDERS,
                _globalLoopingAnimationRenderableWithMouseEvents.colorShiftProviders());
        assertSame(COLOR_SHIFT_PROVIDERS,
                _globalLoopingAnimationRenderableWithoutMouseEvents.colorShiftProviders());
    }

    @Test
    void testGetAndSetRenderingAreaProvider() {
        assertSame(RENDERING_AREA_PROVIDER,
                _globalLoopingAnimationRenderableWithMouseEvents
                        .getRenderingDimensionsProvider());
        assertSame(RENDERING_AREA_PROVIDER,
                _globalLoopingAnimationRenderableWithoutMouseEvents
                        .getRenderingDimensionsProvider());

        FakeProviderAtTime<FloatBox> newRenderingDimensionsProvider = new FakeProviderAtTime<>();

        _globalLoopingAnimationRenderableWithMouseEvents
                .setRenderingDimensionsProvider(newRenderingDimensionsProvider);
        _globalLoopingAnimationRenderableWithoutMouseEvents
                .setRenderingDimensionsProvider(newRenderingDimensionsProvider);

        assertSame(newRenderingDimensionsProvider,
                _globalLoopingAnimationRenderableWithMouseEvents
                        .getRenderingDimensionsProvider());
        assertSame(newRenderingDimensionsProvider,
                _globalLoopingAnimationRenderableWithoutMouseEvents
                        .getRenderingDimensionsProvider());
    }

    @Test
    void testCapturesMouseEventAtPoint() {
        FakeAnimationFrameSnippet animationFrameSnippet = new FakeAnimationFrameSnippet();
        animationFrameSnippet.OffsetX = 0.0123f;
        animationFrameSnippet.OffsetY = 0.0456f;
        FakeAnimation animation = new FakeAnimation(789789);
        GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS.Animation = animation;
        animation.AnimationFrameSnippet = animationFrameSnippet;
        animationFrameSnippet.LeftX = 250;
        animationFrameSnippet.RightX = 750;
        animationFrameSnippet.TopY = 1000;
        animationFrameSnippet.BottomY = 2500;
        FakeImage snippetImage = (FakeImage) animationFrameSnippet.Image;
        snippetImage.Width = 1000;
        snippetImage.Height = 3000;
        RENDERING_AREA_PROVIDER.ProvidedValue = new FakeFloatBox(-0.5f, -2f, 0.75f, 0.5f);

        boolean capturesMouseEventAtPoint = _globalLoopingAnimationRenderableWithMouseEvents
                .capturesMouseEventAtPoint(Vertex.of(0.123f, 0.456f), 789L);

        assertTrue(capturesMouseEventAtPoint);
        ArrayList<Pair<Integer, Integer>> capturesMouseEventsAtPixelInputs =
                snippetImage.CapturesMouseEventsAtPixelInputs;
        assertEquals(1, capturesMouseEventsAtPixelInputs.size());
        assertEquals(
                (int) (((((0.123f - 0.0123f) - (-0.5f)) / (0.75f - (-0.5f))) * (750 - 250)) + 250),
                (int) capturesMouseEventsAtPixelInputs.get(0).getItem1());
        assertEquals(
                (int) (((((0.456f - 0.0456f) - (-2.0f)) / (0.5f - (-2.0f))) * (2500 - 1000))
                        + 1000),
                (int) capturesMouseEventsAtPixelInputs.get(0).getItem2());
        assertEquals(1, RENDERING_AREA_PROVIDER.TimestampInputs.size());
        assertEquals(789L, (long) RENDERING_AREA_PROVIDER.TimestampInputs.get(0));
    }

    @Test
    void testCapturesMouseEventAtPointDoesNotExceedRenderingBoundaries() {
        FakeAnimationFrameSnippet animationFrameSnippet = new FakeAnimationFrameSnippet();
        animationFrameSnippet.OffsetX = 0.0123f;
        animationFrameSnippet.OffsetY = 0.0456f;
        FakeAnimation animation = new FakeAnimation(randomIntWithInclusiveFloor(1));
        animation.AnimationFrameSnippet = animationFrameSnippet;
        GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS.Animation = animation;
        ((FakeImage) animationFrameSnippet.Image).SupportsMouseEventCapturing = true;
        RENDERING_AREA_PROVIDER.ProvidedValue = WHOLE_SCREEN;
        when(_mockRenderingBoundaries.currentBoundaries()).thenReturn(new FloatBox() {
            @Override
            public float leftX() {
                return 0f;
            }

            @Override
            public float topY() {
                return 0f;
            }

            @Override
            public float rightX() {
                return 0.5f;
            }

            @Override
            public float bottomY() {
                return 1f;
            }

            @Override
            public float width() {
                return 0;
            }

            @Override
            public float height() {
                return 0;
            }

            @Override
            public FloatBox intersection(FloatBox floatBox) throws IllegalArgumentException {
                return null;
            }

            @Override
            public FloatBox translate(float v, float v1) {
                return null;
            }

            @Override
            public String getInterfaceName() {
                return null;
            }
        });

        assertTrue(_globalLoopingAnimationRenderableWithMouseEvents
                .capturesMouseEventAtPoint(Vertex.of(0.499f, 0f), TIMESTAMP));
        assertFalse(_globalLoopingAnimationRenderableWithMouseEvents
                .capturesMouseEventAtPoint(Vertex.of(0.501f, 0f), TIMESTAMP));
    }

    @Test
    void testCapturesMouseEventAtPointWithInvalidParams() {
        RENDERING_AREA_PROVIDER.ProvidedValue = new FakeFloatBox(.5f, .5f, 1.5f, 1.5f);
        GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS.Animation = new FakeAnimation(100);

        float verySmallNumber = 0.0001f;

        assertThrows(UnsupportedOperationException.class, () ->
                _globalLoopingAnimationRenderableWithoutMouseEvents
                        .capturesMouseEventAtPoint(Vertex.of(0f, 0f), 0L));

        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents
                        .capturesMouseEventAtPoint(Vertex.of(.5f - verySmallNumber, .75f), 0L));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents
                        .capturesMouseEventAtPoint(Vertex.of(1f + verySmallNumber, .75f), 0L));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents
                        .capturesMouseEventAtPoint(Vertex.of(.75f, .5f - verySmallNumber), 0L));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents
                        .capturesMouseEventAtPoint(Vertex.of(.75f, 1.5f + verySmallNumber), 0L));

        RENDERING_AREA_PROVIDER.ProvidedValue = new FakeFloatBox(-0.5f, -0.5f, 0.5f, 0.5f);

        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents
                        .capturesMouseEventAtPoint(Vertex.of(0f - verySmallNumber, .25f), 0L));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents
                        .capturesMouseEventAtPoint(Vertex.of(0.5f + verySmallNumber, .25f), 0L));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents
                        .capturesMouseEventAtPoint(Vertex.of(.25f, 0f - verySmallNumber), 0L));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents
                        .capturesMouseEventAtPoint(Vertex.of(.25f, 0.5f + verySmallNumber), 0L));
    }

    @Test
    void testGetAndSetZ() {
        assertEquals(Z, _globalLoopingAnimationRenderableWithMouseEvents.getZ());
        assertEquals(Z, _globalLoopingAnimationRenderableWithoutMouseEvents.getZ());

        int newZ = 456;

        _globalLoopingAnimationRenderableWithMouseEvents.setZ(newZ);
        _globalLoopingAnimationRenderableWithoutMouseEvents.setZ(newZ);

        assertEquals(newZ, _globalLoopingAnimationRenderableWithMouseEvents.getZ());
        assertEquals(newZ, _globalLoopingAnimationRenderableWithoutMouseEvents.getZ());

        verify(_mockContainingStack, times(1)).add(
                _globalLoopingAnimationRenderableWithMouseEvents);
        verify(_mockContainingStack, times(1)).add(
                _globalLoopingAnimationRenderableWithoutMouseEvents);
    }

    @Test
    void testDelete() {
        _globalLoopingAnimationRenderableWithMouseEvents.delete();
        _globalLoopingAnimationRenderableWithoutMouseEvents.delete();

        assertNull(_globalLoopingAnimationRenderableWithMouseEvents.containingStack());
        assertNull(_globalLoopingAnimationRenderableWithoutMouseEvents.containingStack());

        verify(_mockContainingStack, times(1)).remove(
                _globalLoopingAnimationRenderableWithMouseEvents);
        verify(_mockContainingStack, times(1)).remove(
                _globalLoopingAnimationRenderableWithoutMouseEvents);
    }

    @Test
    void testUuid() {
        assertSame(UUID, _globalLoopingAnimationRenderableWithMouseEvents.uuid());
        assertSame(UUID, _globalLoopingAnimationRenderableWithoutMouseEvents.uuid());
    }
}
