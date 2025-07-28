package inaugural.soliloquy.io.test.unit.graphics.renderables;

import inaugural.soliloquy.io.graphics.renderables.GlobalLoopingAnimationRenderableImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.io.graphics.renderables.GlobalLoopingAnimationRenderable;
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
public class GlobalLoopingAnimationRenderableImplTests {
    private final FakeGlobalLoopingAnimation GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS =
            new FakeGlobalLoopingAnimation(true);
    private final FakeGlobalLoopingAnimation GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS =
            new FakeGlobalLoopingAnimation(false);
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

    private GlobalLoopingAnimationRenderable renderableWithMouseEvents;
    private GlobalLoopingAnimationRenderable renderableWithoutMouseEvents;

    @BeforeEach
    public void setUp() {
        mockRenderingBoundaries = mock(RenderingBoundaries.class);
        lenient().when(mockRenderingBoundaries.currentBoundaries()).thenReturn(WHOLE_SCREEN);

        ON_PRESS_ACTIONS.put(2, mockOnPressAction);

        renderableWithMouseEvents = new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingComponent, mockRenderingBoundaries);
        renderableWithoutMouseEvents =
                new GlobalLoopingAnimationRenderableImpl(
                        GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFTS,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries);
    }

    @Test
    public void testConstructorWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class,
                () -> new GlobalLoopingAnimationRenderableImpl(null, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                        mockOnMouseLeaveAction, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingComponent, mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, null, BORDER_COLOR_PROVIDER,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS,
                RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, null,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS,
                RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, null,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS,
                RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction, null, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingComponent, mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction, COLOR_SHIFTS, null, Z, UUID, mockContainingComponent,
                mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, null,
                mockContainingComponent, mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID, null,
                mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingComponent, null));

        assertThrows(IllegalArgumentException.class,
                () -> new GlobalLoopingAnimationRenderableImpl(null, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, null, BORDER_COLOR_PROVIDER,
                COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                null, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                null, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, null, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingComponent, mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFTS, null, Z, UUID, mockContainingComponent,
                mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, null,
                mockContainingComponent, mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID, null,
                mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingComponent, null));
    }

    @Test
    public void testGetAndSetGlobalLoopingAnimation() {
        assertSame(GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS,
                renderableWithMouseEvents.getGlobalLoopingAnimation());
        assertSame(GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                renderableWithoutMouseEvents.getGlobalLoopingAnimation());

        FakeGlobalLoopingAnimation newGlobalLoopingAnimation =
                new FakeGlobalLoopingAnimation(true);

        renderableWithMouseEvents
                .setGlobalLoopingAnimation(newGlobalLoopingAnimation);
        renderableWithoutMouseEvents
                .setGlobalLoopingAnimation(newGlobalLoopingAnimation);

        assertSame(newGlobalLoopingAnimation,
                renderableWithMouseEvents.getGlobalLoopingAnimation());
        assertSame(newGlobalLoopingAnimation,
                renderableWithoutMouseEvents.getGlobalLoopingAnimation());
    }

    @Test
    public void testSetGlobalLoopingAnimationWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.setGlobalLoopingAnimation(null));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithoutMouseEvents
                        .setGlobalLoopingAnimation(null));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.setGlobalLoopingAnimation(
                        new FakeGlobalLoopingAnimation(false)));
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
    public void testSetBorderColorProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithoutMouseEvents.setBorderColorProvider(null));
    }

    @Test
    public void testGetAndSetBorderColorProvider() {
        assertSame(BORDER_COLOR_PROVIDER,
                renderableWithMouseEvents.getBorderColorProvider());
        assertSame(BORDER_COLOR_PROVIDER,
                renderableWithoutMouseEvents.getBorderColorProvider());

        FakeProviderAtTime<Color> newBorderColorProvider = new FakeProviderAtTime<>();

        renderableWithMouseEvents
                .setBorderColorProvider(newBorderColorProvider);
        renderableWithoutMouseEvents
                .setBorderColorProvider(newBorderColorProvider);

        assertSame(newBorderColorProvider,
                renderableWithMouseEvents.getBorderColorProvider());
        assertSame(newBorderColorProvider,
                renderableWithoutMouseEvents.getBorderColorProvider());
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
                renderableWithoutMouseEvents.setOnPress(2,
                        new FakeAction<>()));

        renderableWithMouseEvents.setOnPress(2, mockOnPressAction);

        renderableWithMouseEvents.press(2, TIMESTAMP);

        verify(mockOnPressAction, once()).run(eq(inputs(TIMESTAMP,
                renderableWithMouseEvents)));

        //noinspection unchecked
        Action<EventInputs> newOnPress = mock(Action.class);
        renderableWithMouseEvents.setOnPress(2, newOnPress);

        renderableWithMouseEvents.press(2, TIMESTAMP + 1);

        verify(newOnPress, once()).run(eq(inputs(TIMESTAMP + 1,
                renderableWithMouseEvents)));

        renderableWithMouseEvents.press(0, TIMESTAMP + 2);

        verify(newOnPress, once()).run(any());
    }

    @Test
    public void testPressActionIds() {
        var id1 = randomString();
        var id2 = randomString();
        var id3 = randomString();

        renderableWithMouseEvents.setOnPress(0, new FakeAction<>(id1));
        renderableWithMouseEvents.setOnPress(2, new FakeAction<>(id2));
        renderableWithMouseEvents.setOnPress(7, new FakeAction<>(id3));
        renderableWithMouseEvents.setOnPress(2, null);

        var pressActionIds = renderableWithMouseEvents.pressActionIds();

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
                renderableWithoutMouseEvents.setOnRelease(2,
                        new FakeAction<>()));

        renderableWithMouseEvents.release(2, TIMESTAMP);

        //noinspection unchecked
        Action<EventInputs> newOnRelease = mock(Action.class);
        renderableWithMouseEvents.setOnRelease(2, newOnRelease);

        renderableWithMouseEvents.release(2, TIMESTAMP + 1);

        verify(newOnRelease, once()).run(eq(inputs(TIMESTAMP + 1,
                renderableWithMouseEvents)));
    }

    @Test
    public void testReleaseActionIds() {
        var id1 = randomString();
        var id2 = randomString();
        var id3 = randomString();

        renderableWithMouseEvents.setOnRelease(0, new FakeAction<>(id1));
        renderableWithMouseEvents.setOnRelease(2, new FakeAction<>(id2));
        renderableWithMouseEvents.setOnRelease(7, new FakeAction<>(id3));
        renderableWithMouseEvents.setOnRelease(2, null);

        var releaseActionIds = renderableWithMouseEvents.releaseActionIds();

        assertNotNull(releaseActionIds);
        assertEquals(2, releaseActionIds.size());
        assertEquals(id1, releaseActionIds.get(0));
        assertEquals(id3, releaseActionIds.get(7));
    }

    @Test
    public void testPressOrReleaseMethodsWithInvalidButtons() {
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.setOnPress(-1,
                        new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.setOnRelease(-1,
                        new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.press(-1, TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.press(-1, TIMESTAMP + 1));

        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.setOnPress(8,
                        new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.setOnRelease(8,
                        new FakeAction<>()));
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
                renderableWithoutMouseEvents.setOnMouseOver(
                        mockOnMouseOverAction));

        renderableWithMouseEvents.mouseOver(TIMESTAMP);

        verify(mockOnMouseOverAction, once()).run(eq(inputs(TIMESTAMP,
                renderableWithMouseEvents)));

        //noinspection unchecked
        Action<EventInputs> newOnMouseOver = mock(Action.class);
        renderableWithMouseEvents.setOnMouseOver(newOnMouseOver);

        renderableWithMouseEvents.mouseOver(TIMESTAMP + 1);

        verify(newOnMouseOver, once()).run(eq(inputs(TIMESTAMP + 1,
                renderableWithMouseEvents)));
    }

    @Test
    public void testMouseOverActionId() {
        var mouseOverActionId = randomString();

        assertThrows(UnsupportedOperationException.class, () ->
                renderableWithoutMouseEvents.mouseOverActionId());

        renderableWithMouseEvents.setOnMouseOver(null);

        assertNull(renderableWithMouseEvents.mouseOverActionId());

        renderableWithMouseEvents.setOnMouseOver(
                new FakeAction<>(mouseOverActionId));

        assertEquals(mouseOverActionId,
                renderableWithMouseEvents.mouseOverActionId());
    }

    @Test
    public void testMouseLeaveAndSetOnMouseLeave() {
        assertThrows(UnsupportedOperationException.class, () ->
                renderableWithoutMouseEvents.mouseLeave(0L));
        assertThrows(UnsupportedOperationException.class, () ->
                renderableWithoutMouseEvents
                        .setOnMouseLeave(mockOnMouseLeaveAction));

        renderableWithMouseEvents.mouseLeave(TIMESTAMP);

        verify(mockOnMouseLeaveAction, once()).run(eq(inputs(TIMESTAMP,
                renderableWithMouseEvents)));

        //noinspection unchecked
        Action<EventInputs> newOnMouseLeave = mock(Action.class);
        renderableWithMouseEvents.setOnMouseLeave(newOnMouseLeave);

        renderableWithMouseEvents.mouseLeave(TIMESTAMP + 1);

        verify(newOnMouseLeave, once()).run(eq(inputs(TIMESTAMP + 1,
                renderableWithMouseEvents)));
    }

    @Test
    public void testMouseLeaveActionId() {
        var mouseLeaveActionId = randomString();

        assertThrows(UnsupportedOperationException.class, () ->
                renderableWithoutMouseEvents.mouseLeaveActionId());

        renderableWithMouseEvents.setOnMouseLeave(null);

        assertNull(renderableWithMouseEvents.mouseLeaveActionId());

        renderableWithMouseEvents.setOnMouseLeave(
                new FakeAction<>(mouseLeaveActionId));

        assertEquals(mouseLeaveActionId,
                renderableWithMouseEvents.mouseLeaveActionId());
    }

    @Test
    public void testMouseEventCallsToOutdatedTimestamps() {
        RENDERING_AREA_PROVIDER.ProvidedValue = floatBoxOf(0f, 0f, 1f, 1f);
        GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS.Animation = new FakeAnimation(789789);

        long timestamp = 456456L;

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
                renderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(0f, 0f), timestamp + 2));

        renderableWithMouseEvents
                .capturesMouseEventAtPoint(vertexOf(0f, 0f), timestamp + 4);
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.press(0, timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.release(0, timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.mouseOver(timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents.mouseLeave(timestamp + 3));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(0f, 0f), timestamp + 3));
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
                renderableWithMouseEvents
                        .getRenderingDimensionsProvider());
        assertSame(RENDERING_AREA_PROVIDER,
                renderableWithoutMouseEvents
                        .getRenderingDimensionsProvider());

        FakeProviderAtTime<FloatBox> newRenderingDimensionsProvider = new FakeProviderAtTime<>();

        renderableWithMouseEvents
                .setRenderingDimensionsProvider(newRenderingDimensionsProvider);
        renderableWithoutMouseEvents
                .setRenderingDimensionsProvider(newRenderingDimensionsProvider);

        assertSame(newRenderingDimensionsProvider,
                renderableWithMouseEvents
                        .getRenderingDimensionsProvider());
        assertSame(newRenderingDimensionsProvider,
                renderableWithoutMouseEvents
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

        boolean capturesMouseEventAtPoint = renderableWithMouseEvents
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

        assertTrue(renderableWithMouseEvents
                .capturesMouseEventAtPoint(vertexOf(0.499f, 0f), TIMESTAMP));
        assertFalse(renderableWithMouseEvents
                .capturesMouseEventAtPoint(vertexOf(0.501f, 0f), TIMESTAMP));
    }

    @Test
    public void testCapturesMouseEventAtPointWithInvalidArgs() {
        RENDERING_AREA_PROVIDER.ProvidedValue = floatBoxOf(.5f, .5f, 1.5f, 1.5f);
        GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS.Animation = new FakeAnimation(100);

        float verySmallNumber = 0.0001f;

        assertThrows(UnsupportedOperationException.class, () ->
                renderableWithoutMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(0f, 0f), 0L));

        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(.5f - verySmallNumber, .75f), 0L));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(1f + verySmallNumber, .75f), 0L));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(.75f, .5f - verySmallNumber), 0L));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(.75f, 1.5f + verySmallNumber), 0L));

        RENDERING_AREA_PROVIDER.ProvidedValue = floatBoxOf(-0.5f, -0.5f, 0.5f, 0.5f);

        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(0f - verySmallNumber, .25f), 0L));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(0.5f + verySmallNumber, .25f), 0L));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(.25f, 0f - verySmallNumber), 0L));
        assertThrows(IllegalArgumentException.class, () ->
                renderableWithMouseEvents
                        .capturesMouseEventAtPoint(vertexOf(.25f, 0.5f + verySmallNumber), 0L));
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

        verify(mockContainingComponent, once()).add(
                renderableWithMouseEvents);
        verify(mockContainingComponent, once()).add(
                renderableWithoutMouseEvents);
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
}
