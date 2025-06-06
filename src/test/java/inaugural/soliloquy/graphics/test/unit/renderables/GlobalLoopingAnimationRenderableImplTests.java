package inaugural.soliloquy.graphics.test.unit.renderables;

import inaugural.soliloquy.graphics.renderables.GlobalLoopingAnimationRenderableImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.graphics.renderables.GlobalLoopingAnimationRenderable;
import soliloquy.specs.graphics.renderables.RenderableWithMouseEvents.MouseEventInputs;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.graphics.api.Constants.WHOLE_SCREEN;
import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

public class GlobalLoopingAnimationRenderableImplTests {
    private final FakeGlobalLoopingAnimation GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS =
            new FakeGlobalLoopingAnimation(true);
    private final FakeGlobalLoopingAnimation GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS =
            new FakeGlobalLoopingAnimation(false);
    private final FakeProviderAtTime<Float> BORDER_THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Color> BORDER_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final Map<Integer, Action<MouseEventInputs>> ON_PRESS_ACTIONS = mapOf();
    private final List<ProviderAtTime<ColorShift>> COLOR_SHIFT_PROVIDERS = listOf();
    private final FakeStaticProvider<FloatBox> RENDERING_AREA_PROVIDER =
            new FakeStaticProvider<>(null);
    private final int Z = randomInt();
    private final long TIMESTAMP = randomLong();

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private RenderableStack mockContainingStack;
    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private Action<MouseEventInputs> mockOnPressAction;
    @Mock private Action<MouseEventInputs> mockOnMouseOverAction;
    @Mock private Action<MouseEventInputs> mockOnMouseLeaveAction;

    private GlobalLoopingAnimationRenderable globalLoopingAnimationRenderableWithMouseEvents;
    private GlobalLoopingAnimationRenderable globalLoopingAnimationRenderableWithoutMouseEvents;

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

        ON_PRESS_ACTIONS.put(2, mockOnPressAction);

        globalLoopingAnimationRenderableWithMouseEvents =
                new GlobalLoopingAnimationRenderableImpl(
                        GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS,
                        null, mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFT_PROVIDERS,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack,
                        mockRenderingBoundaries);
        globalLoopingAnimationRenderableWithoutMouseEvents =
                new GlobalLoopingAnimationRenderableImpl(
                        GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack,
                        mockRenderingBoundaries);
    }

    @Test
    public void testConstructorWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                null, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingStack, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingStack, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, null,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingStack, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                null, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingStack, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                null, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingStack, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction, null, RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack,
                mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction, COLOR_SHIFT_PROVIDERS, null, Z, UUID, mockContainingStack,
                mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, null,
                mockContainingStack, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                null,
                mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingStack, null
        ));

        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                null, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingStack, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, null,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingStack, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                null, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack,
                mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                null, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack,
                mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, null, RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack,
                mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, null, Z, UUID, mockContainingStack,
                mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, null,
                mockContainingStack, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                null, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingStack, null
        ));
    }

    @Test
    public void testGetAndSetGlobalLoopingAnimation() {
        assertSame(GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS,
                globalLoopingAnimationRenderableWithMouseEvents.getGlobalLoopingAnimation());
        assertSame(GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                globalLoopingAnimationRenderableWithoutMouseEvents.getGlobalLoopingAnimation());

        FakeGlobalLoopingAnimation newGlobalLoopingAnimation =
                new FakeGlobalLoopingAnimation(true);

        globalLoopingAnimationRenderableWithMouseEvents
                .setGlobalLoopingAnimation(newGlobalLoopingAnimation);
        globalLoopingAnimationRenderableWithoutMouseEvents
                .setGlobalLoopingAnimation(newGlobalLoopingAnimation);

        assertSame(newGlobalLoopingAnimation,
                globalLoopingAnimationRenderableWithMouseEvents.getGlobalLoopingAnimation());
        assertSame(newGlobalLoopingAnimation,
                globalLoopingAnimationRenderableWithoutMouseEvents.getGlobalLoopingAnimation());
    }

    @Test
    public void testSetGlobalLoopingAnimationWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.setGlobalLoopingAnimation(null));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithoutMouseEvents
                        .setGlobalLoopingAnimation(null));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.setGlobalLoopingAnimation(
                        new FakeGlobalLoopingAnimation(false)));
    }

    @Test
    public void testGetAndSetBorderThicknessProvider() {
        assertSame(BORDER_THICKNESS_PROVIDER,
                globalLoopingAnimationRenderableWithMouseEvents.getBorderThicknessProvider());
        assertSame(BORDER_THICKNESS_PROVIDER,
                globalLoopingAnimationRenderableWithoutMouseEvents.getBorderThicknessProvider());

        FakeProviderAtTime<Float> newBorderThicknessProvider = new FakeProviderAtTime<>();

        globalLoopingAnimationRenderableWithMouseEvents
                .setBorderThicknessProvider(newBorderThicknessProvider);
        globalLoopingAnimationRenderableWithoutMouseEvents
                .setBorderThicknessProvider(newBorderThicknessProvider);

        assertSame(newBorderThicknessProvider,
                globalLoopingAnimationRenderableWithMouseEvents.getBorderThicknessProvider());
        assertSame(newBorderThicknessProvider,
                globalLoopingAnimationRenderableWithoutMouseEvents.getBorderThicknessProvider());
    }

    @Test
    public void testSetBorderThicknessProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.setBorderThicknessProvider(null));
    }

    @Test
    public void testSetBorderColorProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithoutMouseEvents.setBorderColorProvider(null));
    }

    @Test
    public void testGetAndSetBorderColorProvider() {
        assertSame(BORDER_COLOR_PROVIDER,
                globalLoopingAnimationRenderableWithMouseEvents.getBorderColorProvider());
        assertSame(BORDER_COLOR_PROVIDER,
                globalLoopingAnimationRenderableWithoutMouseEvents.getBorderColorProvider());

        FakeProviderAtTime<Color> newBorderColorProvider = new FakeProviderAtTime<>();

        globalLoopingAnimationRenderableWithMouseEvents
                .setBorderColorProvider(newBorderColorProvider);
        globalLoopingAnimationRenderableWithoutMouseEvents
                .setBorderColorProvider(newBorderColorProvider);

        assertSame(newBorderColorProvider,
                globalLoopingAnimationRenderableWithMouseEvents.getBorderColorProvider());
        assertSame(newBorderColorProvider,
                globalLoopingAnimationRenderableWithoutMouseEvents.getBorderColorProvider());
    }

    @Test
    public void testGetAndSetCapturesMouseEvents() {
        assertTrue(globalLoopingAnimationRenderableWithMouseEvents.getCapturesMouseEvents());
        assertFalse(globalLoopingAnimationRenderableWithoutMouseEvents.getCapturesMouseEvents());

        globalLoopingAnimationRenderableWithMouseEvents.setCapturesMouseEvents(false);
        assertThrows(UnsupportedOperationException.class, () ->
                globalLoopingAnimationRenderableWithoutMouseEvents.setCapturesMouseEvents(false));

        assertFalse(globalLoopingAnimationRenderableWithMouseEvents.getCapturesMouseEvents());
    }

    @Test
    public void testPressAndSetOnPress() {
        assertThrows(UnsupportedOperationException.class, () ->
                globalLoopingAnimationRenderableWithoutMouseEvents.press(2, 0L));
        assertThrows(UnsupportedOperationException.class, () ->
                globalLoopingAnimationRenderableWithoutMouseEvents.setOnPress(2,
                        new FakeAction<>()));

        globalLoopingAnimationRenderableWithMouseEvents.setOnPress(2, mockOnPressAction);

        globalLoopingAnimationRenderableWithMouseEvents.press(2, TIMESTAMP);

        verify(mockOnPressAction, once()).run(eq(MouseEventInputs.of(TIMESTAMP,
                globalLoopingAnimationRenderableWithMouseEvents)));

        //noinspection unchecked
        Action<MouseEventInputs> newOnPress = mock(Action.class);
        globalLoopingAnimationRenderableWithMouseEvents.setOnPress(2, newOnPress);

        globalLoopingAnimationRenderableWithMouseEvents.press(2, TIMESTAMP + 1);

        verify(newOnPress, once()).run(eq(MouseEventInputs.of(TIMESTAMP + 1,
                globalLoopingAnimationRenderableWithMouseEvents)));

        globalLoopingAnimationRenderableWithMouseEvents.press(0, TIMESTAMP + 2);

        verify(newOnPress, once()).run(any());
    }

    @Test
    public void testPressActionIds() {
        String id1 = "id1";
        String id2 = "id2";
        String id3 = "id3";

        globalLoopingAnimationRenderableWithMouseEvents.setOnPress(0, new FakeAction<>(id1));
        globalLoopingAnimationRenderableWithMouseEvents.setOnPress(2, new FakeAction<>(id2));
        globalLoopingAnimationRenderableWithMouseEvents.setOnPress(7, new FakeAction<>(id3));
        globalLoopingAnimationRenderableWithMouseEvents.setOnPress(2, null);

        Map<Integer, String> pressActionIds =
                globalLoopingAnimationRenderableWithMouseEvents.pressActionIds();

        assertNotNull(pressActionIds);
        assertEquals(2, pressActionIds.size());
        assertEquals(id1, pressActionIds.get(0));
        assertEquals(id3, pressActionIds.get(7));
    }

    @Test
    public void testReleaseAndSetOnRelease() {
        assertThrows(UnsupportedOperationException.class, () ->
                globalLoopingAnimationRenderableWithoutMouseEvents.release(2, 0L));
        assertThrows(UnsupportedOperationException.class, () ->
                globalLoopingAnimationRenderableWithoutMouseEvents.setOnRelease(2,
                        new FakeAction<>()));

        globalLoopingAnimationRenderableWithMouseEvents.release(2, TIMESTAMP);

        //noinspection unchecked
        Action<MouseEventInputs> newOnRelease = mock(Action.class);
        globalLoopingAnimationRenderableWithMouseEvents.setOnRelease(2, newOnRelease);

        globalLoopingAnimationRenderableWithMouseEvents.release(2, TIMESTAMP + 1);

        verify(newOnRelease, once()).run(eq(MouseEventInputs.of(TIMESTAMP + 1,
                globalLoopingAnimationRenderableWithMouseEvents)));
    }

    @Test
    public void testReleaseActionIds() {
        String id1 = randomString();
        String id2 = randomString();
        String id3 = randomString();

        globalLoopingAnimationRenderableWithMouseEvents.setOnRelease(0, new FakeAction<>(id1));
        globalLoopingAnimationRenderableWithMouseEvents.setOnRelease(2, new FakeAction<>(id2));
        globalLoopingAnimationRenderableWithMouseEvents.setOnRelease(7, new FakeAction<>(id3));
        globalLoopingAnimationRenderableWithMouseEvents.setOnRelease(2, null);

        Map<Integer, String> releaseActionIds =
                globalLoopingAnimationRenderableWithMouseEvents.releaseActionIds();

        assertNotNull(releaseActionIds);
        assertEquals(2, releaseActionIds.size());
        assertEquals(id1, releaseActionIds.get(0));
        assertEquals(id3, releaseActionIds.get(7));
    }

    @Test
    public void testPressOrReleaseMethodsWithInvalidButtons() {
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.setOnPress(-1,
                        new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.setOnRelease(-1,
                        new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.press(-1, TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.press(-1, TIMESTAMP + 1));

        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.setOnPress(8,
                        new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.setOnRelease(8,
                        new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.press(8, TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.press(8, TIMESTAMP + 3));
    }

    @Test
    public void testMouseOverAndSetOnMouseOver() {
        assertThrows(UnsupportedOperationException.class, () ->
                globalLoopingAnimationRenderableWithoutMouseEvents.mouseOver(0L));
        assertThrows(UnsupportedOperationException.class, () ->
                globalLoopingAnimationRenderableWithoutMouseEvents.setOnMouseOver(
                        mockOnMouseOverAction));

        globalLoopingAnimationRenderableWithMouseEvents.mouseOver(TIMESTAMP);

        verify(mockOnMouseOverAction, once()).run(eq(MouseEventInputs.of(TIMESTAMP,
                globalLoopingAnimationRenderableWithMouseEvents)));

        //noinspection unchecked
        Action<MouseEventInputs> newOnMouseOver = mock(Action.class);
        globalLoopingAnimationRenderableWithMouseEvents.setOnMouseOver(newOnMouseOver);

        globalLoopingAnimationRenderableWithMouseEvents.mouseOver(TIMESTAMP + 1);

        verify(newOnMouseOver, once()).run(eq(MouseEventInputs.of(TIMESTAMP + 1,
                globalLoopingAnimationRenderableWithMouseEvents)));
    }

    @Test
    public void testMouseOverActionId() {
        String mouseOverActionId = "mouseOverActionId";

        assertThrows(UnsupportedOperationException.class, () ->
                globalLoopingAnimationRenderableWithoutMouseEvents.mouseOverActionId());

        globalLoopingAnimationRenderableWithMouseEvents.setOnMouseOver(null);

        assertNull(globalLoopingAnimationRenderableWithMouseEvents.mouseOverActionId());

        globalLoopingAnimationRenderableWithMouseEvents.setOnMouseOver(
                new FakeAction<>(mouseOverActionId));

        assertEquals(mouseOverActionId,
                globalLoopingAnimationRenderableWithMouseEvents.mouseOverActionId());
    }

    @Test
    public void testMouseLeaveAndSetOnMouseLeave() {
        assertThrows(UnsupportedOperationException.class, () ->
                globalLoopingAnimationRenderableWithoutMouseEvents.mouseLeave(0L));
        assertThrows(UnsupportedOperationException.class, () ->
                globalLoopingAnimationRenderableWithoutMouseEvents
                        .setOnMouseLeave(mockOnMouseLeaveAction));

        globalLoopingAnimationRenderableWithMouseEvents.mouseLeave(TIMESTAMP);

        verify(mockOnMouseLeaveAction, once()).run(eq(MouseEventInputs.of(TIMESTAMP, globalLoopingAnimationRenderableWithMouseEvents)));

        //noinspection unchecked
        Action<MouseEventInputs> newOnMouseLeave = mock(Action.class);
        globalLoopingAnimationRenderableWithMouseEvents.setOnMouseLeave(newOnMouseLeave);

        globalLoopingAnimationRenderableWithMouseEvents.mouseLeave(TIMESTAMP + 1);

        verify(newOnMouseLeave, once()).run(eq(MouseEventInputs.of(TIMESTAMP + 1, globalLoopingAnimationRenderableWithMouseEvents)));
    }

    @Test
    public void testMouseLeaveActionId() {
        String mouseLeaveActionId = "mouseLeaveActionId";

        assertThrows(UnsupportedOperationException.class, () ->
                globalLoopingAnimationRenderableWithoutMouseEvents.mouseLeaveActionId());

        globalLoopingAnimationRenderableWithMouseEvents.setOnMouseLeave(null);

        assertNull(globalLoopingAnimationRenderableWithMouseEvents.mouseLeaveActionId());

        globalLoopingAnimationRenderableWithMouseEvents.setOnMouseLeave(
                new FakeAction<>(mouseLeaveActionId));

        assertEquals(mouseLeaveActionId,
                globalLoopingAnimationRenderableWithMouseEvents.mouseLeaveActionId());
    }

    @Test
    public void testMouseEventCallsToOutdatedTimestamps() {
        RENDERING_AREA_PROVIDER.ProvidedValue = floatBoxOf(0f, 0f, 1f, 1f);
        GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS.Animation = new FakeAnimation(789789);

        long timestamp = 456456L;

        globalLoopingAnimationRenderableWithMouseEvents.press(0, timestamp);
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.press(0, timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.release(0, timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.mouseOver(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.mouseLeave(timestamp - 1));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(0f, 0f), timestamp - 1));

        globalLoopingAnimationRenderableWithMouseEvents.release(0, timestamp + 1);
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.press(0, timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.release(0, timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.mouseOver(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.mouseLeave(timestamp));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(0f, 0f), timestamp));

        globalLoopingAnimationRenderableWithMouseEvents.mouseOver(timestamp + 2);
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.press(0, timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.release(0, timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.mouseOver(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.mouseLeave(timestamp + 1));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(0f, 0f), timestamp + 1));

        globalLoopingAnimationRenderableWithMouseEvents.mouseLeave(timestamp + 3);
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.press(0, timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.release(0, timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.mouseOver(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.mouseLeave(timestamp + 2));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(0f, 0f), timestamp + 2));

        globalLoopingAnimationRenderableWithMouseEvents
                .capturesMouseEventAtPoint(vertexOf(0f, 0f), timestamp + 4);
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.press(0, timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.release(0, timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.mouseOver(timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents.mouseLeave(timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(0f, 0f), timestamp + 3));
    }

    @Test
    public void testColorShiftProviders() {
        assertSame(COLOR_SHIFT_PROVIDERS,
                globalLoopingAnimationRenderableWithMouseEvents.colorShiftProviders());
        assertSame(COLOR_SHIFT_PROVIDERS,
                globalLoopingAnimationRenderableWithoutMouseEvents.colorShiftProviders());
    }

    @Test
    public void testGetAndSetRenderingAreaProvider() {
        assertSame(RENDERING_AREA_PROVIDER,
                globalLoopingAnimationRenderableWithMouseEvents
                        .getRenderingDimensionsProvider());
        assertSame(RENDERING_AREA_PROVIDER,
                globalLoopingAnimationRenderableWithoutMouseEvents
                        .getRenderingDimensionsProvider());

        FakeProviderAtTime<FloatBox> newRenderingDimensionsProvider = new FakeProviderAtTime<>();

        globalLoopingAnimationRenderableWithMouseEvents
                .setRenderingDimensionsProvider(newRenderingDimensionsProvider);
        globalLoopingAnimationRenderableWithoutMouseEvents
                .setRenderingDimensionsProvider(newRenderingDimensionsProvider);

        assertSame(newRenderingDimensionsProvider,
                globalLoopingAnimationRenderableWithMouseEvents
                        .getRenderingDimensionsProvider());
        assertSame(newRenderingDimensionsProvider,
                globalLoopingAnimationRenderableWithoutMouseEvents
                        .getRenderingDimensionsProvider());
    }

    @Test
    public void testCapturesMouseEventAtPoint() {
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
        RENDERING_AREA_PROVIDER.ProvidedValue = floatBoxOf(-0.5f, -2f, 0.75f, 0.5f);

        boolean capturesMouseEventAtPoint = globalLoopingAnimationRenderableWithMouseEvents
                .capturesMouseEventAtPoint(vertexOf(0.123f, 0.456f), 789L);

        assertTrue(capturesMouseEventAtPoint);
        List<Pair<Integer, Integer>> capturesMouseEventsAtPixelInputs =
                snippetImage.CapturesMouseEventsAtPixelInputs;
        assertEquals(1, capturesMouseEventsAtPixelInputs.size());
        assertEquals(
                (int) (((((0.123f - 0.0123f) - (-0.5f)) / (0.75f - (-0.5f))) * (750 - 250)) + 250),
                (int) capturesMouseEventsAtPixelInputs.getFirst().FIRST);
        assertEquals(
                (int) (((((0.456f - 0.0456f) - (-2.0f)) / (0.5f - (-2.0f))) * (2500 - 1000))
                        + 1000),
                (int) capturesMouseEventsAtPixelInputs.getFirst().SECOND);
        assertEquals(1, RENDERING_AREA_PROVIDER.TimestampInputs.size());
        assertEquals(789L, (long) RENDERING_AREA_PROVIDER.TimestampInputs.getFirst());
    }

    @Test
    public void testCapturesMouseEventAtPointDoesNotExceedRenderingBoundaries() {
        FakeAnimationFrameSnippet animationFrameSnippet = new FakeAnimationFrameSnippet();
        animationFrameSnippet.OffsetX = 0.0123f;
        animationFrameSnippet.OffsetY = 0.0456f;
        FakeAnimation animation = new FakeAnimation(randomIntWithInclusiveFloor(1));
        animation.AnimationFrameSnippet = animationFrameSnippet;
        GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS.Animation = animation;
        ((FakeImage) animationFrameSnippet.Image).SupportsMouseEventCapturing = true;
        RENDERING_AREA_PROVIDER.ProvidedValue = WHOLE_SCREEN;
        when(mockRenderingBoundaries.currentBoundaries()).thenReturn(floatBoxOf(0f, 0f, 0.5f, 1f));

        assertTrue(globalLoopingAnimationRenderableWithMouseEvents
                .capturesMouseEventAtPoint(vertexOf(0.499f, 0f), TIMESTAMP));
        assertFalse(globalLoopingAnimationRenderableWithMouseEvents
                .capturesMouseEventAtPoint(vertexOf(0.501f, 0f), TIMESTAMP));
    }

    @Test
    public void testCapturesMouseEventAtPointWithInvalidArgs() {
        RENDERING_AREA_PROVIDER.ProvidedValue = floatBoxOf(.5f, .5f, 1.5f, 1.5f);
        GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS.Animation = new FakeAnimation(100);

        float verySmallNumber = 0.0001f;

        assertThrows(UnsupportedOperationException.class, () ->
                globalLoopingAnimationRenderableWithoutMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(0f, 0f), 0L));

        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(.5f - verySmallNumber, .75f), 0L));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(1f + verySmallNumber, .75f), 0L));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(.75f, .5f - verySmallNumber), 0L));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(.75f, 1.5f + verySmallNumber), 0L));

        RENDERING_AREA_PROVIDER.ProvidedValue = floatBoxOf(-0.5f, -0.5f, 0.5f, 0.5f);

        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(0f - verySmallNumber, .25f), 0L));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(0.5f + verySmallNumber, .25f), 0L));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(.25f, 0f - verySmallNumber), 0L));
        assertThrows(IllegalArgumentException.class, () ->
                globalLoopingAnimationRenderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(.25f, 0.5f + verySmallNumber), 0L));
    }

    @Test
    public void testGetAndSetZ() {
        assertEquals(Z, globalLoopingAnimationRenderableWithMouseEvents.getZ());
        assertEquals(Z, globalLoopingAnimationRenderableWithoutMouseEvents.getZ());

        int newZ = 456;

        globalLoopingAnimationRenderableWithMouseEvents.setZ(newZ);
        globalLoopingAnimationRenderableWithoutMouseEvents.setZ(newZ);

        assertEquals(newZ, globalLoopingAnimationRenderableWithMouseEvents.getZ());
        assertEquals(newZ, globalLoopingAnimationRenderableWithoutMouseEvents.getZ());

        verify(mockContainingStack, once()).add(
                globalLoopingAnimationRenderableWithMouseEvents);
        verify(mockContainingStack, once()).add(
                globalLoopingAnimationRenderableWithoutMouseEvents);
    }

    @Test
    public void testDelete() {
        globalLoopingAnimationRenderableWithMouseEvents.delete();
        globalLoopingAnimationRenderableWithoutMouseEvents.delete();

        assertNull(globalLoopingAnimationRenderableWithMouseEvents.containingStack());
        assertNull(globalLoopingAnimationRenderableWithoutMouseEvents.containingStack());

        verify(mockContainingStack, once()).remove(
                globalLoopingAnimationRenderableWithMouseEvents);
        verify(mockContainingStack, once()).remove(
                globalLoopingAnimationRenderableWithoutMouseEvents);
    }

    @Test
    public void testUuid() {
        assertSame(UUID, globalLoopingAnimationRenderableWithMouseEvents.uuid());
        assertSame(UUID, globalLoopingAnimationRenderableWithoutMouseEvents.uuid());
    }
}
