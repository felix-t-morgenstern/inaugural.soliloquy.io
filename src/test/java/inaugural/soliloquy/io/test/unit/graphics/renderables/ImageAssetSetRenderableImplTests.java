package inaugural.soliloquy.io.test.unit.graphics.renderables;

import inaugural.soliloquy.io.graphics.renderables.ImageAssetSetRenderableImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeAction;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.assets.*;
import soliloquy.specs.io.graphics.assets.Image;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.ImageAssetSetRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.ui.EventInputs;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.io.api.Constants.WHOLE_SCREEN;
import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static inaugural.soliloquy.tools.testing.Mock.generateMockWithId;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;
import static soliloquy.specs.ui.EventInputs.inputs;

@ExtendWith(MockitoExtension.class)
public class ImageAssetSetRenderableImplTests {
    private final String STANCE_PARAM = randomString();
    private final String STANCE = randomString();
    private final String DIRECTION_PARAM = randomString();
    private final String DIRECTION = randomString();
    private final Map<String, String> DISPLAY_PARAMS = mapOf(
            pairOf(STANCE_PARAM, STANCE),
            pairOf(DIRECTION_PARAM, DIRECTION)
    );
    private final int Z = randomInt();
    private final long TIMESTAMP = randomLong();
    private final int SNIPPET_LEFT_X = randomInt();
    private final int SNIPPET_RIGHT_X = randomInt();
    private final int SNIPPET_TOP_Y = randomInt();
    private final int SNIPPET_BOTTOM_Y = randomInt();
    private final float RENDERING_AREA_LEFT_X = randomFloatInRange(0f, 1f);
    private final float RENDERING_AREA_RIGHT_X = randomFloatInRange(RENDERING_AREA_LEFT_X, 1f);
    private final float RENDERING_AREA_TOP_Y = randomFloatInRange(0f, 1f);
    private final float RENDERING_AREA_BOTTOM_Y = randomFloatInRange(RENDERING_AREA_TOP_Y, 1f);
    private final float RENDERING_BOUNDARIES_LEFT_X =
            randomFloatInRange(RENDERING_AREA_LEFT_X, RENDERING_AREA_RIGHT_X);
    private final float RENDERING_BOUNDARIES_RIGHT_X =
            randomFloatInRange(RENDERING_BOUNDARIES_LEFT_X, RENDERING_AREA_RIGHT_X);
    private final float RENDERING_BOUNDARIES_TOP_Y =
            randomFloatInRange(RENDERING_AREA_TOP_Y, RENDERING_AREA_BOTTOM_Y);
    private final float RENDERING_BOUNDARIES_BOTTOM_Y =
            randomFloatInRange(RENDERING_BOUNDARIES_TOP_Y, RENDERING_AREA_BOTTOM_Y);
    private final float POINT_X = randomFloatInRange(
            Math.max(RENDERING_AREA_LEFT_X, RENDERING_BOUNDARIES_LEFT_X),
            Math.min(RENDERING_AREA_RIGHT_X, RENDERING_BOUNDARIES_RIGHT_X));
    private final float POINT_Y = randomFloatInRange(
            Math.max(RENDERING_AREA_TOP_Y, RENDERING_BOUNDARIES_TOP_Y),
            Math.min(RENDERING_AREA_BOTTOM_Y, RENDERING_BOUNDARIES_BOTTOM_Y));
    private final Vertex POINT = vertexOf(POINT_X, POINT_Y);
    private final int ANIMATION_MS_DURATION = randomInt();
    private final float ANIMATION_FRAME_SNIPPET_OFFSET_X = randomFloat();
    private final float ANIMATION_FRAME_SNIPPET_OFFSET_Y = randomFloat();
    private final boolean IMAGE_CAPTURES_EVENTS_AT_PIXEL = randomBoolean();
    private final float VERY_SMALL_NUMBER = 0.0001f;

    private final UUID UUID = java.util.UUID.randomUUID();

    private Map<Integer, Action<EventInputs>> onPressActions;
    private List<ColorShift> colorShifts;

    private final FloatBox RENDERING_AREA = floatBoxOf(
            RENDERING_AREA_LEFT_X, RENDERING_AREA_TOP_Y,
            RENDERING_AREA_RIGHT_X, RENDERING_AREA_BOTTOM_Y);
    private final FloatBox CURRENT_RENDERING_BOUNDARIES = floatBoxOf(
            RENDERING_BOUNDARIES_LEFT_X, RENDERING_BOUNDARIES_TOP_Y,
            RENDERING_BOUNDARIES_RIGHT_X, RENDERING_BOUNDARIES_BOTTOM_Y);

    @Mock private ImageAssetSet mockImageAssetSet;
    @Mock private ProviderAtTime<Float> mockBorderThicknessProvider;
    @Mock private ProviderAtTime<Color> mockBorderColorProvider;
    @Mock private ProviderAtTime<FloatBox> mockRenderingAreaProvider;
    @Mock private Component mockContainingComponent;
    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private TimestampValidator mockTimestampValidator;
    @Mock private Action<EventInputs> mockOnMousePressAction;
    @Mock private Action<EventInputs> mockOnMouseOverAction;
    @Mock private Action<EventInputs> mockOnMouseLeaveAction;
    @Mock private Image mockImage;
    @Mock private Sprite mockSprite;
    @Mock private AnimationFrameSnippet mockAnimationFrameSnippet;
    @Mock private Animation mockAnimation;
    @Mock private GlobalLoopingAnimation mockGlobalLoopingAnimation;


    private ImageAssetSetRenderable renderable;

    @BeforeEach
    public void setUp() {
        lenient().when(mockRenderingAreaProvider.provide(anyLong())).thenReturn(RENDERING_AREA);

        lenient().when(mockRenderingBoundaries.currentBoundaries())
                .thenReturn(CURRENT_RENDERING_BOUNDARIES);

        onPressActions = mapOf(pairOf(2, mockOnMousePressAction));
        colorShifts = listOf();

        lenient().when(mockImage.capturesMouseEventsAtPixel(anyInt(), anyInt()))
                .thenReturn(IMAGE_CAPTURES_EVENTS_AT_PIXEL);

        lenient().when(mockSprite.leftX()).thenReturn(SNIPPET_LEFT_X);
        lenient().when(mockSprite.rightX()).thenReturn(SNIPPET_RIGHT_X);
        lenient().when(mockSprite.topY()).thenReturn(SNIPPET_TOP_Y);
        lenient().when(mockSprite.bottomY()).thenReturn(SNIPPET_BOTTOM_Y);
        lenient().when(mockSprite.image()).thenReturn(mockImage);

        lenient().when(mockAnimationFrameSnippet.leftX()).thenReturn(SNIPPET_LEFT_X);
        lenient().when(mockAnimationFrameSnippet.rightX()).thenReturn(SNIPPET_RIGHT_X);
        lenient().when(mockAnimationFrameSnippet.topY()).thenReturn(SNIPPET_TOP_Y);
        lenient().when(mockAnimationFrameSnippet.bottomY()).thenReturn(SNIPPET_BOTTOM_Y);
        lenient().when(mockAnimationFrameSnippet.image()).thenReturn(mockImage);
        lenient().when(mockAnimationFrameSnippet.offsetX())
                .thenReturn(ANIMATION_FRAME_SNIPPET_OFFSET_X);
        lenient().when(mockAnimationFrameSnippet.offsetY())
                .thenReturn(ANIMATION_FRAME_SNIPPET_OFFSET_Y);

        lenient().when(mockAnimation.snippetAtFrame(anyInt()))
                .thenReturn(mockAnimationFrameSnippet);
        lenient().when(mockAnimation.msDuration()).thenReturn(ANIMATION_MS_DURATION);
        lenient().when(mockAnimation.snippetAtFrame(anyInt()))
                .thenReturn(mockAnimationFrameSnippet);

        lenient().when(mockGlobalLoopingAnimation.provide(anyLong()))
                .thenReturn(mockAnimationFrameSnippet);

        lenient().when(mockImageAssetSet.supportsMouseEventCapturing()).thenReturn(true);

        renderable =
                new ImageAssetSetRenderableImpl(mockImageAssetSet, DISPLAY_PARAMS, onPressActions,
                        null, mockOnMouseOverAction, mockOnMouseLeaveAction, colorShifts,
                        mockBorderThicknessProvider, mockBorderColorProvider,
                        mockRenderingAreaProvider, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries, mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        when(mockImageAssetSet.supportsMouseEventCapturing()).thenReturn(true);
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(null, DISPLAY_PARAMS, onPressActions, null,
                        mockOnMouseOverAction, mockOnMouseLeaveAction, colorShifts,
                        mockBorderThicknessProvider, mockBorderColorProvider,
                        mockRenderingAreaProvider, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries, mockTimestampValidator));
        when(mockImageAssetSet.supportsMouseEventCapturing()).thenReturn(false);
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, DISPLAY_PARAMS,
                        onPressActions, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        colorShifts, mockBorderThicknessProvider, mockBorderColorProvider,
                        mockRenderingAreaProvider, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries, mockTimestampValidator));
        lenient().when(mockImageAssetSet.supportsMouseEventCapturing()).thenReturn(true);
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, DISPLAY_PARAMS,
                        onPressActions, null, mockOnMouseOverAction, mockOnMouseLeaveAction, null,
                        mockBorderThicknessProvider, mockBorderColorProvider,
                        mockRenderingAreaProvider, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, DISPLAY_PARAMS,
                        onPressActions, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        colorShifts, null, mockBorderColorProvider, mockRenderingAreaProvider, Z,
                        UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, DISPLAY_PARAMS,
                        onPressActions, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        colorShifts, mockBorderThicknessProvider, null, mockRenderingAreaProvider,
                        Z, UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, DISPLAY_PARAMS,
                        onPressActions, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        colorShifts, mockBorderThicknessProvider, null, mockRenderingAreaProvider,
                        Z, UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, DISPLAY_PARAMS,
                        onPressActions, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        colorShifts, mockBorderThicknessProvider, mockBorderColorProvider, null, Z,
                        UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, DISPLAY_PARAMS,
                        onPressActions, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        colorShifts, mockBorderThicknessProvider, mockBorderColorProvider,
                        mockRenderingAreaProvider, Z, null, mockContainingComponent,
                        mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, DISPLAY_PARAMS,
                        onPressActions, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        colorShifts, mockBorderThicknessProvider, mockBorderColorProvider,
                        mockRenderingAreaProvider, Z, UUID, mockContainingComponent,
                        null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, DISPLAY_PARAMS,
                        onPressActions, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        colorShifts, mockBorderThicknessProvider, mockBorderColorProvider,
                        mockRenderingAreaProvider, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries, null));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(null, DISPLAY_PARAMS, colorShifts,
                        mockBorderThicknessProvider, mockBorderColorProvider,
                        mockRenderingAreaProvider, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, DISPLAY_PARAMS, null,
                        mockBorderThicknessProvider, mockBorderColorProvider,
                        mockRenderingAreaProvider, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, DISPLAY_PARAMS,
                        colorShifts, null, mockBorderColorProvider, mockRenderingAreaProvider, Z,
                        UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, DISPLAY_PARAMS,
                        colorShifts, mockBorderThicknessProvider, null, mockRenderingAreaProvider,
                        Z, UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, DISPLAY_PARAMS,
                        colorShifts, mockBorderThicknessProvider, null, mockRenderingAreaProvider,
                        Z, UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, DISPLAY_PARAMS,
                        colorShifts, mockBorderThicknessProvider, mockBorderColorProvider, null, Z,
                        UUID, mockContainingComponent, mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, DISPLAY_PARAMS,
                        colorShifts, mockBorderThicknessProvider, mockBorderColorProvider,
                        mockRenderingAreaProvider, Z, null, mockContainingComponent,
                        mockRenderingBoundaries, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, DISPLAY_PARAMS,
                        colorShifts, mockBorderThicknessProvider, mockBorderColorProvider,
                        mockRenderingAreaProvider, Z, UUID, mockContainingComponent,
                        null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, DISPLAY_PARAMS,
                        colorShifts, mockBorderThicknessProvider, mockBorderColorProvider,
                        mockRenderingAreaProvider, Z, UUID, mockContainingComponent,
                        mockRenderingBoundaries, null));
    }

    @Test
    public void testConstructorAddsSelfToContainingComponent() {
        verify(mockContainingComponent, once()).add(renderable);
    }

    @Test
    public void testGetAndSetImageAssetSet() {
        assertSame(mockImageAssetSet, renderable.getImageAssetSet());

        var newImageAssetSet = mock(ImageAssetSet.class);
        when(newImageAssetSet.supportsMouseEventCapturing()).thenReturn(true);

        renderable.setImageAssetSet(newImageAssetSet);

        assertSame(newImageAssetSet, renderable.getImageAssetSet());
    }

    @Test
    public void testSetImageAssetSetWithInvalidArgs() {
        var imageAssetSetNotSupportingMouseEvents = mock(ImageAssetSet.class);
        when(imageAssetSetNotSupportingMouseEvents.supportsMouseEventCapturing()).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> renderable.setImageAssetSet(null));
        assertThrows(IllegalArgumentException.class,
                () -> renderable.setImageAssetSet(imageAssetSetNotSupportingMouseEvents));
    }

    @Test
    public void testDisplayParams() {
        assertSame(DISPLAY_PARAMS, renderable.displayParams());
    }

    @Test
    public void testGetAndSetBorderThicknessProvider() {
        assertSame(mockBorderThicknessProvider, renderable.getBorderThicknessProvider());

        //noinspection unchecked
        var newBorderThicknessProvider = (ProviderAtTime<Float>) mock(ProviderAtTime.class);

        renderable.setBorderThicknessProvider(newBorderThicknessProvider);

        assertSame(newBorderThicknessProvider, renderable.getBorderThicknessProvider());
    }

    @Test
    public void testSetBorderThicknessProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> renderable.setBorderThicknessProvider(null));
    }

    @Test
    public void testGetAndSetBorderColorProvider() {
        assertSame(mockBorderColorProvider, renderable.getBorderColorProvider());

        //noinspection unchecked
        var newBorderColorProvider = (ProviderAtTime<Color>) mock(ProviderAtTime.class);

        renderable.setBorderColorProvider(newBorderColorProvider);

        assertSame(newBorderColorProvider, renderable.getBorderColorProvider());
    }

    @Test
    public void testSetBorderColorProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> renderable.setBorderColorProvider(null));
    }

    @Test
    public void testGetAndSetCapturesMouseEvents() {
        assertTrue(renderable.getCapturesMouseEvents());

        renderable.setCapturesMouseEvents(false);

        assertFalse(renderable.getCapturesMouseEvents());
    }

    @Test
    public void testSetCapturesMouseEventsWhenUnderlyingAssetDoesNotSupportThem() {
        when(mockImageAssetSet.supportsMouseEventCapturing()).thenReturn(false);

        assertThrows(UnsupportedOperationException.class,
                () -> renderable.setCapturesMouseEvents(true));
    }

    @Test
    public void testPressAndSetOnPress() {
        renderable.setOnPress(2, mockOnMousePressAction);

        renderable.press(2, TIMESTAMP);

        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(TIMESTAMP);
        verify(mockOnMousePressAction).run(eq(inputs(TIMESTAMP, renderable)));

        //noinspection unchecked
        var newOnPress = (Action<EventInputs>) mock(Action.class);
        renderable.setOnPress(2, newOnPress);

        renderable.press(2, TIMESTAMP + 1);

        verify(newOnPress).run(eq(inputs(TIMESTAMP + 1, renderable)));

        renderable.press(0, TIMESTAMP + 2);

        verify(newOnPress).run(any());
    }

    @Test
    public void testPressActionIds() {
        var id1 = randomString();
        var id2 = randomString();
        var id3 = randomString();

        //noinspection unchecked
        renderable.setOnPress(0, generateMockWithId(Action.class, id1));
        //noinspection unchecked
        renderable.setOnPress(2, generateMockWithId(Action.class, id2));
        //noinspection unchecked
        renderable.setOnPress(7, generateMockWithId(Action.class, id3));
        renderable.setOnPress(2, null);

        var pressActionIds = renderable.pressActionIds();

        assertNotNull(pressActionIds);
        assertNotSame(renderable.pressActionIds(), pressActionIds);
        assertEquals(2, pressActionIds.size());
        assertEquals(id1, pressActionIds.get(0));
        assertEquals(id3, pressActionIds.get(7));
    }

    @Test
    public void testReleaseAndSetOnRelease() {
        renderable.release(2, TIMESTAMP);

        //noinspection unchecked
        var newOnRelease = (Action<EventInputs>) mock(Action.class);
        renderable.setOnRelease(2, newOnRelease);

        renderable.release(2, TIMESTAMP + 1);

        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(TIMESTAMP);
        verify(newOnRelease).run(eq(inputs(TIMESTAMP + 1, renderable)));
    }

    @Test
    public void testReleaseActionIds() {
        var id1 = randomString();
        var id2 = randomString();
        var id3 = randomString();

        //noinspection unchecked
        renderable.setOnRelease(0, generateMockWithId(Action.class, id1));
        //noinspection unchecked
        renderable.setOnRelease(2, generateMockWithId(Action.class, id2));
        //noinspection unchecked
        renderable.setOnRelease(7, generateMockWithId(Action.class, id3));
        renderable.setOnRelease(2, null);

        var releaseActionIds = renderable.releaseActionIds();

        assertNotNull(releaseActionIds);
        assertNotSame(renderable.releaseActionIds(), releaseActionIds);
        assertEquals(2, releaseActionIds.size());
        assertEquals(id1, releaseActionIds.get(0));
        assertEquals(id3, releaseActionIds.get(7));
    }

    @Test
    public void testPressOrReleaseMethodsWithInvalidButtons() {
        assertThrows(IllegalArgumentException.class,
                () -> renderable.setOnPress(-1, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class,
                () -> renderable.setOnRelease(-1, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () -> renderable.press(-1, TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () -> renderable.press(-1, TIMESTAMP + 1));

        assertThrows(IllegalArgumentException.class,
                () -> renderable.setOnPress(8, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class,
                () -> renderable.setOnRelease(8, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () -> renderable.press(8, TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () -> renderable.press(8, TIMESTAMP + 3));
    }

    @Test
    public void testMouseOverAndSetOnMouseOver() {

        renderable.mouseOver(TIMESTAMP);

        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(TIMESTAMP);
        verify(mockOnMouseOverAction).run(eq(inputs(TIMESTAMP, renderable)));

        //noinspection unchecked
        Action<EventInputs> newOnMouseOver = mock(Action.class);
        renderable.setOnMouseOver(newOnMouseOver);

        renderable.mouseOver(TIMESTAMP + 1);

        verify(newOnMouseOver).run(eq(inputs(TIMESTAMP + 1, renderable)));
    }

    @Test
    public void testMouseOverActionId() {
        var mouseOverActionId = randomString();

        renderable.setOnMouseOver(null);

        assertNull(renderable.mouseOverActionId());

        //noinspection unchecked
        renderable.setOnMouseOver(generateMockWithId(Action.class, mouseOverActionId));

        assertEquals(mouseOverActionId, renderable.mouseOverActionId());
    }

    @Test
    public void testMouseLeaveAndSetOnMouseLeave() {
        renderable.mouseLeave(TIMESTAMP);

        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(TIMESTAMP);
        verify(mockOnMouseLeaveAction).run(eq(inputs(TIMESTAMP, renderable)));

        //noinspection unchecked
        var newOnMouseLeave = (Action<EventInputs>) mock(Action.class);
        renderable.setOnMouseLeave(newOnMouseLeave);

        renderable.mouseLeave(TIMESTAMP + 1);

        verify(newOnMouseLeave).run(eq(inputs(TIMESTAMP + 1, renderable)));
    }

    @Test
    public void testMouseLeaveActionId() {
        var mouseLeaveActionId = randomString();

        renderable.setOnMouseLeave(null);

        assertNull(renderable.mouseLeaveActionId());

        //noinspection unchecked
        renderable.setOnMouseLeave(generateMockWithId(Action.class, mouseLeaveActionId));

        assertEquals(mouseLeaveActionId, renderable.mouseLeaveActionId());
    }

    @Test
    public void testMouseEventsWhenNotSupportingMouseEvents() {
        renderable.setCapturesMouseEvents(false);

        //noinspection unchecked
        assertThrows(UnsupportedOperationException.class,
                () -> renderable.setOnPress(randomIntInRange(0, 7),
                        (Action<EventInputs>) mock(Action.class)));
        assertThrows(UnsupportedOperationException.class,
                () -> renderable.press(randomIntInRange(0, 7), TIMESTAMP + 1));
        //noinspection unchecked
        assertThrows(UnsupportedOperationException.class,
                () -> renderable.setOnRelease(randomIntInRange(0, 7),
                        (Action<EventInputs>) mock(Action.class)));
        assertThrows(UnsupportedOperationException.class,
                () -> renderable.release(randomIntInRange(0, 7), TIMESTAMP + 1));
        //noinspection unchecked
        assertThrows(UnsupportedOperationException.class,
                () -> renderable.setOnMouseOver((Action<EventInputs>) mock(Action.class)));
        assertThrows(UnsupportedOperationException.class,
                () -> renderable.mouseOver(TIMESTAMP + 1));
        //noinspection unchecked
        assertThrows(UnsupportedOperationException.class,
                () -> renderable.setOnMouseLeave((Action<EventInputs>) mock(Action.class)));
        assertThrows(UnsupportedOperationException.class,
                () -> renderable.mouseLeave(TIMESTAMP + 1));
        assertThrows(UnsupportedOperationException.class,
                () -> renderable.capturesMouseEventAtPoint(vertexOf(0f, 0f), TIMESTAMP + 1));
    }

    @Test
    public void testColorShiftProviders() {
        assertSame(colorShifts, renderable.colorShifts());
    }

    @Test
    public void testGetAndSetRenderingDimensionsProvider() {
        assertSame(mockRenderingAreaProvider, renderable.getRenderingDimensionsProvider());

        //noinspection unchecked
        var newRenderingDimensionsProvider = (ProviderAtTime<FloatBox>) mock(ProviderAtTime.class);

        renderable.setRenderingDimensionsProvider(newRenderingDimensionsProvider);

        assertSame(newRenderingDimensionsProvider, renderable.getRenderingDimensionsProvider());
    }

    @Test
    public void testSetRenderingDimensionsProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> renderable.setRenderingDimensionsProvider(null));
    }

    @Test
    public void testGetAndSetZ() {
        assertEquals(Z, renderable.getZ());

        var newZ = randomInt();

        renderable.setZ(newZ);

        assertEquals(newZ, renderable.getZ());
    }

    @Test
    public void testGetAndSetAnimationStart() {
        var animationStart = randomLong();

        renderable.setAnimationStart(animationStart);

        assertEquals(animationStart, renderable.getAnimationStart());
    }

    @Test
    public void testCapturesMouseEventAtPointForSprite() {
        when(mockImageAssetSet.getImageAssetWithDisplayParams(any())).thenReturn(mockSprite);
        var expectedImageX = (int) (((POINT_X - RENDERING_AREA_LEFT_X) / RENDERING_AREA.width()) *
                (SNIPPET_RIGHT_X - SNIPPET_LEFT_X)) + SNIPPET_LEFT_X;
        var expectedImageY = (int) (((POINT_Y - RENDERING_AREA_TOP_Y) / RENDERING_AREA.height()) *
                (SNIPPET_BOTTOM_Y - SNIPPET_TOP_Y)) + SNIPPET_TOP_Y;

        var capturesMouseEventAtPoint = renderable.capturesMouseEventAtPoint(POINT, TIMESTAMP);

        assertEquals(IMAGE_CAPTURES_EVENTS_AT_PIXEL, capturesMouseEventAtPoint);
        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(TIMESTAMP);
        var inOrder = inOrder(mockImageAssetSet, mockRenderingBoundaries, mockSprite, mockImage);
        inOrder.verify(mockRenderingBoundaries).currentBoundaries();
        inOrder.verify(mockImageAssetSet).getImageAssetWithDisplayParams(DISPLAY_PARAMS);
        inOrder.verify(mockSprite).image();
        inOrder.verify(mockImage).capturesMouseEventsAtPixel(expectedImageX, expectedImageY);
    }

    @Test
    public void testCapturesMouseEventAtPointForAnimation() {
        when(mockImageAssetSet.getImageAssetWithDisplayParams(any())).thenReturn(mockAnimation);
        var expectedImageX =
                (int) ((((POINT_X - ANIMATION_FRAME_SNIPPET_OFFSET_X) - RENDERING_AREA_LEFT_X) /
                        RENDERING_AREA.width()) * (SNIPPET_RIGHT_X - SNIPPET_LEFT_X)) +
                        SNIPPET_LEFT_X;
        var expectedImageY =
                (int) ((((POINT_Y - ANIMATION_FRAME_SNIPPET_OFFSET_Y) - RENDERING_AREA_TOP_Y) /
                        RENDERING_AREA.height()) * (SNIPPET_BOTTOM_Y - SNIPPET_TOP_Y)) +
                        SNIPPET_TOP_Y;

        var capturesMouseEventAtPoint = renderable.capturesMouseEventAtPoint(POINT, TIMESTAMP);

        assertEquals(IMAGE_CAPTURES_EVENTS_AT_PIXEL, capturesMouseEventAtPoint);
        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(TIMESTAMP);
        var inOrder = inOrder(mockImageAssetSet, mockRenderingBoundaries, mockAnimation,
                mockAnimationFrameSnippet, mockImage);
        inOrder.verify(mockRenderingBoundaries).currentBoundaries();
        inOrder.verify(mockImageAssetSet).getImageAssetWithDisplayParams(DISPLAY_PARAMS);
        inOrder.verify(mockAnimation).snippetAtFrame((int) (TIMESTAMP % ANIMATION_MS_DURATION));
        inOrder.verify(mockAnimationFrameSnippet).offsetX();
        inOrder.verify(mockAnimationFrameSnippet).offsetY();
        inOrder.verify(mockAnimationFrameSnippet).image();
        inOrder.verify(mockImage).capturesMouseEventsAtPixel(expectedImageX, expectedImageY);
    }

    @Test
    public void testCapturesMouseEventAtPointForGlobalLoopingAnimation() {
        when(mockImageAssetSet.getImageAssetWithDisplayParams(any())).thenReturn(
                mockGlobalLoopingAnimation);
        var expectedImageX =
                (int) ((((POINT_X - ANIMATION_FRAME_SNIPPET_OFFSET_X) - RENDERING_AREA_LEFT_X) /
                        RENDERING_AREA.width()) * (SNIPPET_RIGHT_X - SNIPPET_LEFT_X)) +
                        SNIPPET_LEFT_X;
        var expectedImageY =
                (int) ((((POINT_Y - ANIMATION_FRAME_SNIPPET_OFFSET_Y) - RENDERING_AREA_TOP_Y) /
                        RENDERING_AREA.height()) * (SNIPPET_BOTTOM_Y - SNIPPET_TOP_Y)) +
                        SNIPPET_TOP_Y;

        var capturesMouseEventAtPoint = renderable.capturesMouseEventAtPoint(POINT, TIMESTAMP);

        assertEquals(IMAGE_CAPTURES_EVENTS_AT_PIXEL, capturesMouseEventAtPoint);
        verify(mockTimestampValidator, atLeastOnce()).validateTimestamp(TIMESTAMP);
        var inOrder = inOrder(mockImageAssetSet, mockRenderingBoundaries,
                mockGlobalLoopingAnimation, mockAnimationFrameSnippet, mockImage);
        inOrder.verify(mockRenderingBoundaries).currentBoundaries();
        inOrder.verify(mockImageAssetSet).getImageAssetWithDisplayParams(DISPLAY_PARAMS);
        inOrder.verify(mockGlobalLoopingAnimation).provide(TIMESTAMP);
        inOrder.verify(mockAnimationFrameSnippet).offsetX();
        inOrder.verify(mockAnimationFrameSnippet).offsetY();
        inOrder.verify(mockAnimationFrameSnippet).image();
        inOrder.verify(mockImage).capturesMouseEventsAtPixel(expectedImageX, expectedImageY);
    }

    @Test
    public void testCapturesMouseEventAtPointWhenExceedingRenderingBoundaries() {
        var distanceWithinWindow = randomFloatInRange(VERY_SMALL_NUMBER, 1f - VERY_SMALL_NUMBER);
        when(mockImageAssetSet.getImageAssetWithDisplayParams(any())).thenReturn(mockSprite);
        when(mockRenderingAreaProvider.provide(anyLong())).thenReturn(WHOLE_SCREEN);
        when(mockRenderingBoundaries.currentBoundaries()).thenReturn(
                floatBoxOf(0f, 0f, distanceWithinWindow, distanceWithinWindow));
        when(mockImage.capturesMouseEventsAtPixel(anyInt(), anyInt())).thenReturn(true);

        assertTrue(renderable.capturesMouseEventAtPoint(
                vertexOf(Math.min(1f, distanceWithinWindow - VERY_SMALL_NUMBER), 0f), TIMESTAMP));
        assertFalse(renderable.capturesMouseEventAtPoint(
                vertexOf(Math.min(1f, distanceWithinWindow + VERY_SMALL_NUMBER), 0f), TIMESTAMP));
    }

    @Test
    public void testCapturesMouseEventAtPointWithInvalidArgs() {
        when(mockRenderingAreaProvider.provide(anyLong())).thenReturn(
                floatBoxOf(0.5f, 1.5f, 0.5f, 1.5f));
        when(mockRenderingBoundaries.currentBoundaries()).thenReturn(WHOLE_SCREEN);

        assertThrows(IllegalArgumentException.class,
                () -> renderable.capturesMouseEventAtPoint(vertexOf(.5f - VERY_SMALL_NUMBER, .75f),
                        0L));
        assertThrows(IllegalArgumentException.class,
                () -> renderable.capturesMouseEventAtPoint(vertexOf(1f + VERY_SMALL_NUMBER, .75f),
                        0L));
        assertThrows(IllegalArgumentException.class,
                () -> renderable.capturesMouseEventAtPoint(vertexOf(.75f, .5f - VERY_SMALL_NUMBER),
                        0L));
        assertThrows(IllegalArgumentException.class,
                () -> renderable.capturesMouseEventAtPoint(vertexOf(.75f, 1.5f + VERY_SMALL_NUMBER),
                        0L));

        when(mockRenderingAreaProvider.provide(anyLong())).thenReturn(
                floatBoxOf(-0.5f, 0.5f, -0.5f, 0.5f));

        assertThrows(IllegalArgumentException.class,
                () -> renderable.capturesMouseEventAtPoint(vertexOf(0f - VERY_SMALL_NUMBER, .25f),
                        0L));
        assertThrows(IllegalArgumentException.class,
                () -> renderable.capturesMouseEventAtPoint(vertexOf(0.5f + VERY_SMALL_NUMBER, .25f),
                        0L));
        assertThrows(IllegalArgumentException.class,
                () -> renderable.capturesMouseEventAtPoint(vertexOf(.25f, 0f - VERY_SMALL_NUMBER),
                        0L));
        assertThrows(IllegalArgumentException.class,
                () -> renderable.capturesMouseEventAtPoint(vertexOf(.25f, 0.5f + VERY_SMALL_NUMBER),
                        0L));
    }

    @Test
    public void testDelete() {
        renderable.delete();

        assertNull(renderable.component());
        assertTrue(renderable.isDeleted());
    }

    @Test
    public void testUuid() {
        assertSame(UUID, renderable.uuid());
    }

    @Test
    public void testComponent() {
        assertSame(mockContainingComponent, renderable.component());
    }

    @Test
    public void testSetComponent() {
        ((ImageAssetSetRenderableImpl) renderable).setComponent(null);

        assertNull(renderable.component());
    }
}
