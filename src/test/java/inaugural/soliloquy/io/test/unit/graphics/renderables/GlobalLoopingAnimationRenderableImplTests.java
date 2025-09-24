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
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.GlobalLoopingAnimationRenderable;
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

// TODO: This refactor is a WIP
@ExtendWith(MockitoExtension.class)
public class GlobalLoopingAnimationRenderableImplTests {
    private final FakeGlobalLoopingAnimation GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS =
            new FakeGlobalLoopingAnimation(true);
    private final FakeGlobalLoopingAnimation GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS =
            new FakeGlobalLoopingAnimation(false);
    private final Map<Integer, Action<EventInputs>> ON_PRESS_ACTIONS = mapOf();
    private final List<ColorShift> COLOR_SHIFTS = listOf();
    private final int Z = randomInt();
    private final long TIMESTAMP = randomLong();

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private Component mockContainingComponent;
    @Mock private ProviderAtTime<FloatBox> mockRenderingAreaProvider;
    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private TimestampValidator mockTimestampValidator;
    @Mock private ProviderAtTime<Float> mockBorderThicknessProvider;
    @Mock private ProviderAtTime<Color> mockBorderColorProvider;
    @Mock private Action<EventInputs> mockOnPressAction;
    @Mock private Action<EventInputs> mockOnMouseOverAction;
    @Mock private Action<EventInputs> mockOnMouseLeaveAction;

    private GlobalLoopingAnimationRenderable renderable;

    @BeforeEach
    public void setUp() {
        lenient().when(mockRenderingBoundaries.currentBoundaries()).thenReturn(WHOLE_SCREEN);

        ON_PRESS_ACTIONS.put(2, mockOnPressAction);

        renderable = new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, mockBorderThicknessProvider,
                mockBorderColorProvider, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction, COLOR_SHIFTS, mockRenderingAreaProvider, Z, UUID,
                mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator);
        renderable.setCapturesMouseEvents(true);
    }

    @Test
    public void testConstructorWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class,
                () -> new GlobalLoopingAnimationRenderableImpl(null, mockBorderThicknessProvider,
                        mockBorderColorProvider, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                        mockOnMouseLeaveAction, COLOR_SHIFTS, mockRenderingAreaProvider, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, null, mockBorderColorProvider,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS,
                mockRenderingAreaProvider, Z, UUID, mockContainingComponent,
                mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, mockBorderThicknessProvider, null,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS,
                mockRenderingAreaProvider, Z, UUID, mockContainingComponent,
                mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, mockBorderThicknessProvider, null,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction, COLOR_SHIFTS,
                mockRenderingAreaProvider, Z, UUID, mockContainingComponent,
                mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, mockBorderThicknessProvider,
                mockBorderColorProvider, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction, null, mockRenderingAreaProvider, Z, UUID,
                mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, mockBorderThicknessProvider,
                mockBorderColorProvider, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction, COLOR_SHIFTS, null, Z, UUID, mockContainingComponent,
                mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, mockBorderThicknessProvider,
                mockBorderColorProvider, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction, COLOR_SHIFTS, mockRenderingAreaProvider, Z, null,
                mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));

        assertThrows(IllegalArgumentException.class,
                () -> new GlobalLoopingAnimationRenderableImpl(null, mockBorderThicknessProvider,
                        mockBorderColorProvider, COLOR_SHIFTS, mockRenderingAreaProvider, Z, UUID,
                        mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, null, mockBorderColorProvider,
                COLOR_SHIFTS, mockRenderingAreaProvider, Z, UUID, mockContainingComponent,
                mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, mockBorderThicknessProvider,
                null, COLOR_SHIFTS, mockRenderingAreaProvider, Z, UUID, mockContainingComponent,
                mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, mockBorderThicknessProvider,
                null, COLOR_SHIFTS, mockRenderingAreaProvider, Z, UUID, mockContainingComponent,
                mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, mockBorderThicknessProvider,
                mockBorderColorProvider, null, mockRenderingAreaProvider, Z, UUID,
                mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, mockBorderThicknessProvider,
                mockBorderColorProvider, COLOR_SHIFTS, null, Z, UUID, mockContainingComponent,
                mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, mockBorderThicknessProvider,
                mockBorderColorProvider, COLOR_SHIFTS, mockRenderingAreaProvider, Z, null,
                mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));
    }

    @Test
    public void testConstructorAddsSelfToContainingComponent() {
        verify(mockContainingComponent, once()).add(renderable);
    }

    @Test
    public void testGetAndSetGlobalLoopingAnimation() {
        assertSame(GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS,
                renderable.getGlobalLoopingAnimation());

        var newGlobalLoopingAnimation = new FakeGlobalLoopingAnimation(true);

        renderable
                .setGlobalLoopingAnimation(newGlobalLoopingAnimation);

        assertSame(newGlobalLoopingAnimation,
                renderable.getGlobalLoopingAnimation());
    }

    @Test
    public void testSetGlobalLoopingAnimationWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setGlobalLoopingAnimation(null));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setGlobalLoopingAnimation(
                        new FakeGlobalLoopingAnimation(false)));
    }

    @Test
    public void testGetAndSetBorderThicknessProvider() {
        assertSame(mockBorderThicknessProvider, renderable.getBorderThicknessProvider());

        var newProvider = generateMockStaticProvider(randomFloat());

        renderable.setBorderThicknessProvider(newProvider);

        assertSame(newProvider, renderable.getBorderThicknessProvider());
    }

    @Test
    public void testSetBorderThicknessProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> renderable.setBorderThicknessProvider(null));
    }

    @Test
    public void testSetBorderColorProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> renderable.setBorderColorProvider(null));
    }

    @Test
    public void testGetAndSetBorderColorProvider() {
        assertSame(mockBorderColorProvider,
                renderable.getBorderColorProvider());

        var newProvider = generateMockStaticProvider(randomColor());

        renderable.setBorderColorProvider(newProvider);

        assertSame(newProvider, renderable.getBorderColorProvider());
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
        verify(mockOnPressAction, once()).accept(eq(inputs(TIMESTAMP,
                renderable)));

        //noinspection unchecked
        Action<EventInputs> newOnPress = mock(Action.class);
        renderable.setOnPress(2, newOnPress);

        renderable.press(2, TIMESTAMP + 1);

        verify(newOnPress, once()).accept(eq(inputs(TIMESTAMP + 1,
                renderable)));

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
        verify(newOnRelease, once()).accept(eq(inputs(TIMESTAMP + 1,
                renderable)));
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

        var releaseActionIds = renderable.releaseActionIds();

        assertNotNull(releaseActionIds);
        assertEquals(2, releaseActionIds.size());
        assertEquals(id1, releaseActionIds.get(0));
        assertEquals(id3, releaseActionIds.get(7));
    }

    @Test
    public void testPressOrReleaseMethodsWithInvalidButtons() {
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setOnPress(-1,
                        action(randomString(), _ -> {})));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setOnRelease(-1,
                        action(randomString(), _ -> {})));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.press(-1, TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.press(-1, TIMESTAMP + 1));

        assertThrows(IllegalArgumentException.class, () ->
                renderable.setOnPress(8,
                        action(randomString(), _ -> {})));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setOnRelease(8,
                        action(randomString(), _ -> {})));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.press(8, TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.press(8, TIMESTAMP + 3));
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

        verify(newOnMouseOver, once()).accept(eq(inputs(TIMESTAMP + 1, renderable)));
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

        var newProvider = generateMockStaticProvider(randomFloatBox());

        renderable.setRenderingDimensionsProvider(newProvider);

        assertSame(newProvider, renderable.getRenderingDimensionsProvider());
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
        when(mockRenderingAreaProvider.provide(anyLong())).thenReturn(
                floatBoxOf(-0.5f, -2f, 0.75f, 0.5f));

        boolean capturesMouseEventAtPoint = renderable
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
        verify(mockRenderingAreaProvider, once()).provide(anyLong());
        verify(mockRenderingAreaProvider, once()).provide(TIMESTAMP);
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
        when(mockRenderingAreaProvider.provide(anyLong())).thenReturn(WHOLE_SCREEN);
        when(mockRenderingBoundaries.currentBoundaries()).thenReturn(floatBoxOf(0f, 0f, 0.5f, 1f));

        assertTrue(renderable
                .capturesMouseEventAtPoint(vertexOf(0.499f, 0f), TIMESTAMP));
        assertFalse(renderable
                .capturesMouseEventAtPoint(vertexOf(0.501f, 0f), TIMESTAMP));
    }

    @Test
    public void testCapturesMouseEventAtPointWithInvalidArgs() {
        when(mockRenderingAreaProvider.provide(anyLong())).thenReturn(
                floatBoxOf(.5f, .5f, 1.5f, 1.5f));
        GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS.Animation = new FakeAnimation(100);

        float verySmallNumber = 0.0001f;

        assertThrows(IllegalArgumentException.class, () ->
                renderable
                        .capturesMouseEventAtPoint(vertexOf(.5f - verySmallNumber, .75f), 0L));
        assertThrows(IllegalArgumentException.class, () ->
                renderable
                        .capturesMouseEventAtPoint(vertexOf(1f + verySmallNumber, .75f), 0L));
        assertThrows(IllegalArgumentException.class, () ->
                renderable
                        .capturesMouseEventAtPoint(vertexOf(.75f, .5f - verySmallNumber), 0L));
        assertThrows(IllegalArgumentException.class, () ->
                renderable
                        .capturesMouseEventAtPoint(vertexOf(.75f, 1.5f + verySmallNumber), 0L));

        when(mockRenderingAreaProvider.provide(anyLong())).thenReturn(
                floatBoxOf(-0.5f, -0.5f, 0.5f, 0.5f));

        assertThrows(IllegalArgumentException.class, () ->
                renderable
                        .capturesMouseEventAtPoint(vertexOf(0f - verySmallNumber, .25f), 0L));
        assertThrows(IllegalArgumentException.class, () ->
                renderable
                        .capturesMouseEventAtPoint(vertexOf(0.5f + verySmallNumber, .25f), 0L));
        assertThrows(IllegalArgumentException.class, () ->
                renderable
                        .capturesMouseEventAtPoint(vertexOf(.25f, 0f - verySmallNumber), 0L));
        assertThrows(IllegalArgumentException.class, () ->
                renderable
                        .capturesMouseEventAtPoint(vertexOf(.25f, 0.5f + verySmallNumber), 0L));
    }

    @Test
    public void testGetAndSetZ() {
        assertEquals(Z, renderable.getZ());

        var newZ = randomInt();

        renderable.setZ(newZ);

        assertEquals(newZ, renderable.getZ());
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
        ((GlobalLoopingAnimationRenderableImpl) renderable).setContainingComponent(null);

        assertNull(renderable.containingComponent());
    }
}
