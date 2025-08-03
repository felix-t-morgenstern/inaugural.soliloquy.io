package inaugural.soliloquy.io.test.unit.graphics.renderables;

import inaugural.soliloquy.io.graphics.renderables.GlobalLoopingAnimationRenderableImpl;
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
import soliloquy.specs.io.graphics.renderables.GlobalLoopingAnimationRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
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

// TODO: This refactor is a WIP
@ExtendWith(MockitoExtension.class)
public class GlobalLoopingAnimationRenderableImplTests {
    private final FakeGlobalLoopingAnimation GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS =
            new FakeGlobalLoopingAnimation(true);
    private final FakeGlobalLoopingAnimation GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS =
            new FakeGlobalLoopingAnimation(false);
    private final Map<Integer, Action<EventInputs>> ON_PRESS_ACTIONS = mapOf();
    private final List<ColorShift> COLOR_SHIFTS = listOf();
    private final FakeStaticProvider<FloatBox> RENDERING_AREA_PROVIDER =
            new FakeStaticProvider<>(null);
    private final int Z = randomInt();
    private final long TIMESTAMP = randomLong();

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private Component mockContainingComponent;
    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private TimestampValidator mockTimestampValidator;
    @Mock private ProviderAtTime<Float> mockBorderThicknessProvider;
    @Mock private ProviderAtTime<Color> mockBorderColorProvider;
    @Mock private Action<EventInputs> mockOnPressAction;
    @Mock private Action<EventInputs> mockOnMouseOverAction;
    @Mock private Action<EventInputs> mockOnMouseLeaveAction;

    private GlobalLoopingAnimationRenderable renderableWithMouseEvents;
    private GlobalLoopingAnimationRenderable renderableWithoutMouseEvents;

    @BeforeEach
    public void setUp() {
        lenient().when(mockRenderingBoundaries.currentBoundaries()).thenReturn(WHOLE_SCREEN);

        ON_PRESS_ACTIONS.put(2, mockOnPressAction);

        renderableWithMouseEvents = new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, mockBorderThicknessProvider,
                mockBorderColorProvider, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator);
        renderableWithoutMouseEvents =
                new GlobalLoopingAnimationRenderableImpl(
                        GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        mockBorderThicknessProvider, mockBorderColorProvider, COLOR_SHIFTS,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries, mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class,
                () -> new GlobalLoopingAnimationRenderableImpl(null, mockBorderThicknessProvider,
                        mockBorderColorProvider, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                        mockOnMouseLeaveAction, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, mockBorderThicknessProvider,
                mockBorderColorProvider, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, null, mockBorderColorProvider,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS,
                RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, mockBorderThicknessProvider, null,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS,
                RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, mockBorderThicknessProvider, null,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS,
                RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, mockBorderThicknessProvider,
                mockBorderColorProvider, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction, null, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, mockBorderThicknessProvider,
                mockBorderColorProvider, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction, COLOR_SHIFTS, null, Z, UUID, mockContainingComponent,
                mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, mockBorderThicknessProvider,
                mockBorderColorProvider, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, null,
                mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));

        assertThrows(IllegalArgumentException.class,
                () -> new GlobalLoopingAnimationRenderableImpl(null, mockBorderThicknessProvider,
                        mockBorderColorProvider, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, null, mockBorderColorProvider,
                COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, mockBorderThicknessProvider,
                null, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, mockBorderThicknessProvider,
                null, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent,
                mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, mockBorderThicknessProvider,
                mockBorderColorProvider, null, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, mockBorderThicknessProvider,
                mockBorderColorProvider, COLOR_SHIFTS, null, Z, UUID, mockContainingComponent,
                mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, mockBorderThicknessProvider,
                mockBorderColorProvider, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, null,
                mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));
    }

    @Test
    public void testGetAndSetGlobalLoopingAnimation() {
        assertSame(GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS,
                renderableWithMouseEvents.getGlobalLoopingAnimation());
        assertSame(GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                renderableWithoutMouseEvents.getGlobalLoopingAnimation());

        var newGlobalLoopingAnimation = new FakeGlobalLoopingAnimation(true);

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
        assertSame(mockBorderThicknessProvider,
                renderableWithMouseEvents.getBorderThicknessProvider());
        assertSame(mockBorderThicknessProvider,
                renderableWithoutMouseEvents.getBorderThicknessProvider());

        var newBorderThicknessProvider = new FakeProviderAtTime<Float>();

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
        assertSame(mockBorderColorProvider,
                renderableWithMouseEvents.getBorderColorProvider());
        assertSame(mockBorderColorProvider,
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

        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(TIMESTAMP);
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

        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(TIMESTAMP);
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

        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(TIMESTAMP);
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

        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(TIMESTAMP);
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
        var animationFrameSnippet = new FakeAnimationFrameSnippet();
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
                .capturesMouseEventAtPoint(vertexOf(0.123f, 0.456f), TIMESTAMP);

        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(TIMESTAMP);
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
        assertEquals(TIMESTAMP, (long) RENDERING_AREA_PROVIDER.TimestampInputs.getFirst());
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

    @Test
    public void testComponent() {
        assertSame(mockContainingComponent, renderableWithMouseEvents.component());
    }

    @Test
    public void testSetComponent() {
        ((GlobalLoopingAnimationRenderableImpl) renderableWithMouseEvents).setComponent(null);

        assertNull(renderableWithMouseEvents.component());
    }
}
