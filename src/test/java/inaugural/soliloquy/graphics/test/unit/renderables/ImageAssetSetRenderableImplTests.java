package inaugural.soliloquy.graphics.test.unit.renderables;

import inaugural.soliloquy.graphics.renderables.ImageAssetSetRenderableImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.shared.Direction;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.assets.*;
import soliloquy.specs.graphics.renderables.ImageAssetSetRenderable;
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
import static inaugural.soliloquy.tools.testing.Mock.generateMockWithId;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

@ExtendWith(MockitoExtension.class)
public class ImageAssetSetRenderableImplTests {
    private final String TYPE = randomString();
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
    private final Direction DIRECTION = Direction.SOUTHWEST;
    private final float VERY_SMALL_NUMBER = 0.0001f;

    private final UUID UUID = java.util.UUID.randomUUID();

    private Map<Integer, Action<MouseEventInputs>> onPressActions;
    private List<ProviderAtTime<ColorShift>> colorShiftProviders;

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
    @Mock private RenderableStack mockContainingStack;
    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private Action<MouseEventInputs> mockOnMousePressAction;
    @Mock private Action<MouseEventInputs> mockOnMouseOverAction;
    @Mock private Action<MouseEventInputs> mockOnMouseLeaveAction;
    @Mock private Image mockImage;
    @Mock private Sprite mockSprite;
    @Mock private AnimationFrameSnippet mockAnimationFrameSnippet;
    @Mock private Animation mockAnimation;
    @Mock private GlobalLoopingAnimation mockGlobalLoopingAnimation;


    private ImageAssetSetRenderable imageAssetSetRenderable;

    @BeforeEach
    public void setUp() {
        lenient().when(mockRenderingAreaProvider.provide(anyLong())).thenReturn(RENDERING_AREA);

        lenient().when(mockRenderingBoundaries.currentBoundaries())
                .thenReturn(CURRENT_RENDERING_BOUNDARIES);

        onPressActions = mapOf(pairOf(2, mockOnMousePressAction));
        colorShiftProviders = listOf();

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

        imageAssetSetRenderable =
                new ImageAssetSetRenderableImpl(mockImageAssetSet, TYPE, DIRECTION,
                        onPressActions, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        colorShiftProviders, mockBorderThicknessProvider, mockBorderColorProvider,
                        mockRenderingAreaProvider, Z, UUID, mockContainingStack,
                        mockRenderingBoundaries);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        when(mockImageAssetSet.supportsMouseEventCapturing()).thenReturn(true);
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(null, TYPE, DIRECTION, onPressActions,
                        null, mockOnMouseOverAction, mockOnMouseLeaveAction, colorShiftProviders,
                        mockBorderThicknessProvider, mockBorderColorProvider,
                        mockRenderingAreaProvider, Z, UUID, mockContainingStack,
                        mockRenderingBoundaries));
        when(mockImageAssetSet.supportsMouseEventCapturing()).thenReturn(false);
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, TYPE, DIRECTION,
                        onPressActions, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        colorShiftProviders, mockBorderThicknessProvider, mockBorderColorProvider,
                        mockRenderingAreaProvider, Z, UUID, mockContainingStack,
                        mockRenderingBoundaries));
        lenient().when(mockImageAssetSet.supportsMouseEventCapturing()).thenReturn(true);
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, TYPE, DIRECTION,
                        onPressActions, null, mockOnMouseOverAction, mockOnMouseLeaveAction, null,
                        mockBorderThicknessProvider, mockBorderColorProvider,
                        mockRenderingAreaProvider, Z, UUID, mockContainingStack,
                        mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, TYPE, DIRECTION,
                        onPressActions, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        colorShiftProviders, null, mockBorderColorProvider,
                        mockRenderingAreaProvider, Z, UUID, mockContainingStack,
                        mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, TYPE, DIRECTION,
                        onPressActions, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        colorShiftProviders, mockBorderThicknessProvider, null,
                        mockRenderingAreaProvider, Z, UUID, mockContainingStack,
                        mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, TYPE, DIRECTION,
                        onPressActions, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        colorShiftProviders, mockBorderThicknessProvider, null,
                        mockRenderingAreaProvider, Z, UUID, mockContainingStack,
                        mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, TYPE, DIRECTION,
                        onPressActions, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        colorShiftProviders, mockBorderThicknessProvider, mockBorderColorProvider,
                        null, Z, UUID, mockContainingStack, mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, TYPE, DIRECTION,
                        onPressActions, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        colorShiftProviders, mockBorderThicknessProvider, mockBorderColorProvider,
                        mockRenderingAreaProvider, Z, null, mockContainingStack,
                        mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, TYPE, DIRECTION,
                        onPressActions, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        colorShiftProviders, mockBorderThicknessProvider, mockBorderColorProvider,
                        mockRenderingAreaProvider, Z, UUID, null, mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, TYPE, DIRECTION,
                        onPressActions, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                        colorShiftProviders, mockBorderThicknessProvider, mockBorderColorProvider,
                        mockRenderingAreaProvider, Z, UUID, mockContainingStack, null));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(null, TYPE, DIRECTION,
                        colorShiftProviders, mockBorderThicknessProvider, mockBorderColorProvider,
                        mockRenderingAreaProvider, Z, UUID, mockContainingStack,
                        mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, TYPE, DIRECTION, null,
                        mockBorderThicknessProvider, mockBorderColorProvider,
                        mockRenderingAreaProvider, Z, UUID, mockContainingStack,
                        mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, TYPE, DIRECTION,
                        colorShiftProviders, null, mockBorderColorProvider,
                        mockRenderingAreaProvider, Z, UUID, mockContainingStack,
                        mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, TYPE, DIRECTION,
                        colorShiftProviders, mockBorderThicknessProvider, null,
                        mockRenderingAreaProvider, Z, UUID, mockContainingStack,
                        mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, TYPE, DIRECTION,
                        colorShiftProviders, mockBorderThicknessProvider, null,
                        mockRenderingAreaProvider, Z, UUID, mockContainingStack,
                        mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, TYPE, DIRECTION,
                        colorShiftProviders, mockBorderThicknessProvider, mockBorderColorProvider,
                        null, Z, UUID, mockContainingStack, mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, TYPE, DIRECTION,
                        colorShiftProviders, mockBorderThicknessProvider, mockBorderColorProvider,
                        mockRenderingAreaProvider, Z, null, mockContainingStack,
                        mockRenderingBoundaries));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, TYPE, DIRECTION,
                        colorShiftProviders, mockBorderThicknessProvider, mockBorderColorProvider,
                        mockRenderingAreaProvider, Z, UUID, mockContainingStack, null));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableImpl(mockImageAssetSet, TYPE, DIRECTION,
                        colorShiftProviders, mockBorderThicknessProvider, mockBorderColorProvider,
                        mockRenderingAreaProvider, Z, UUID, null, mockRenderingBoundaries));
    }

    @Test
    public void testGetAndSetImageAssetSet() {
        assertSame(mockImageAssetSet, imageAssetSetRenderable.getImageAssetSet());

        var newImageAssetSet = mock(ImageAssetSet.class);
        when(newImageAssetSet.supportsMouseEventCapturing()).thenReturn(true);

        imageAssetSetRenderable.setImageAssetSet(newImageAssetSet);

        assertSame(newImageAssetSet, imageAssetSetRenderable.getImageAssetSet());
    }

    @Test
    public void testSetImageAssetSetWithInvalidArgs() {
        var imageAssetSetNotSupportingMouseEvents = mock(ImageAssetSet.class);
        when(imageAssetSetNotSupportingMouseEvents.supportsMouseEventCapturing()).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () ->
                imageAssetSetRenderable.setImageAssetSet(null));
        assertThrows(IllegalArgumentException.class, () ->
                imageAssetSetRenderable.setImageAssetSet(imageAssetSetNotSupportingMouseEvents));
    }

    @Test
    public void testGetAndSetType() {
        assertEquals(TYPE, imageAssetSetRenderable.getType());

        imageAssetSetRenderable.setType("");

        assertNull(imageAssetSetRenderable.getType());
    }

    @Test
    public void testGetAndSetDirection() {
        assertEquals(DIRECTION, imageAssetSetRenderable.getDirection());

        imageAssetSetRenderable.setDirection(null);

        assertNull(imageAssetSetRenderable.getDirection());
    }

    @Test
    public void testGetAndSetBorderThicknessProvider() {
        assertSame(mockBorderThicknessProvider,
                imageAssetSetRenderable.getBorderThicknessProvider());

        //noinspection unchecked
        var newBorderThicknessProvider = (ProviderAtTime<Float>) mock(ProviderAtTime.class);

        imageAssetSetRenderable.setBorderThicknessProvider(newBorderThicknessProvider);

        assertSame(newBorderThicknessProvider,
                imageAssetSetRenderable.getBorderThicknessProvider());
    }

    @Test
    public void testSetBorderThicknessProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> imageAssetSetRenderable.setBorderThicknessProvider(null));
    }

    @Test
    public void testGetAndSetBorderColorProvider() {
        assertSame(mockBorderColorProvider, imageAssetSetRenderable.getBorderColorProvider());

        //noinspection unchecked
        var newBorderColorProvider = (ProviderAtTime<Color>) mock(ProviderAtTime.class);

        imageAssetSetRenderable.setBorderColorProvider(newBorderColorProvider);

        assertSame(newBorderColorProvider, imageAssetSetRenderable.getBorderColorProvider());
    }

    @Test
    public void testSetBorderColorProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> imageAssetSetRenderable.setBorderColorProvider(null));
    }

    @Test
    public void testGetAndSetCapturesMouseEvents() {
        assertTrue(imageAssetSetRenderable.getCapturesMouseEvents());

        imageAssetSetRenderable.setCapturesMouseEvents(false);

        assertFalse(imageAssetSetRenderable.getCapturesMouseEvents());
    }

    @Test
    public void testSetCapturesMouseEventsWhenUnderlyingAssetDoesNotSupportThem() {
        when(mockImageAssetSet.supportsMouseEventCapturing()).thenReturn(false);

        assertThrows(UnsupportedOperationException.class,
                () -> imageAssetSetRenderable.setCapturesMouseEvents(true));
    }

    @Test
    public void testPressAndSetOnPress() {
        imageAssetSetRenderable.setOnPress(2, mockOnMousePressAction);

        imageAssetSetRenderable.press(2, TIMESTAMP);

        verify(mockOnMousePressAction).run(
                eq(MouseEventInputs.of(TIMESTAMP, imageAssetSetRenderable)));

        //noinspection unchecked
        var newOnPress = (Action<MouseEventInputs>) mock(Action.class);
        imageAssetSetRenderable.setOnPress(2, newOnPress);

        imageAssetSetRenderable.press(2, TIMESTAMP + 1);

        verify(newOnPress).run(eq(MouseEventInputs.of(TIMESTAMP + 1, imageAssetSetRenderable)));

        imageAssetSetRenderable.press(0, TIMESTAMP + 2);

        verify(newOnPress).run(any());
    }

    @Test
    public void testPressActionIds() {
        var id1 = randomString();
        var id2 = randomString();
        var id3 = randomString();

        //noinspection unchecked
        imageAssetSetRenderable.setOnPress(0, generateMockWithId(Action.class, id1));
        //noinspection unchecked
        imageAssetSetRenderable.setOnPress(2, generateMockWithId(Action.class, id2));
        //noinspection unchecked
        imageAssetSetRenderable.setOnPress(7, generateMockWithId(Action.class, id3));
        imageAssetSetRenderable.setOnPress(2, null);

        Map<Integer, String> pressActionIds = imageAssetSetRenderable.pressActionIds();

        assertNotNull(pressActionIds);
        assertNotSame(imageAssetSetRenderable.pressActionIds(), pressActionIds);
        assertEquals(2, pressActionIds.size());
        assertEquals(id1, pressActionIds.get(0));
        assertEquals(id3, pressActionIds.get(7));
    }

    @Test
    public void testReleaseAndSetOnRelease() {
        imageAssetSetRenderable.release(2, TIMESTAMP);

        //noinspection unchecked
        var newOnRelease = (Action<MouseEventInputs>) mock(Action.class);
        imageAssetSetRenderable.setOnRelease(2, newOnRelease);

        imageAssetSetRenderable.release(2, TIMESTAMP + 1);

        verify(newOnRelease).run(eq(MouseEventInputs.of(TIMESTAMP + 1, imageAssetSetRenderable)));
    }

    @Test
    public void testReleaseActionIds() {
        var id1 = randomString();
        var id2 = randomString();
        var id3 = randomString();

        //noinspection unchecked
        imageAssetSetRenderable.setOnRelease(0, generateMockWithId(Action.class, id1));
        //noinspection unchecked
        imageAssetSetRenderable.setOnRelease(2, generateMockWithId(Action.class, id2));
        //noinspection unchecked
        imageAssetSetRenderable.setOnRelease(7, generateMockWithId(Action.class, id3));
        imageAssetSetRenderable.setOnRelease(2, null);

        Map<Integer, String> releaseActionIds = imageAssetSetRenderable.releaseActionIds();

        assertNotNull(releaseActionIds);
        assertNotSame(imageAssetSetRenderable.releaseActionIds(), releaseActionIds);
        assertEquals(2, releaseActionIds.size());
        assertEquals(id1, releaseActionIds.get(0));
        assertEquals(id3, releaseActionIds.get(7));
    }

    @Test
    public void testPressOrReleaseMethodsWithInvalidButtons() {
        assertThrows(IllegalArgumentException.class, () ->
                imageAssetSetRenderable.setOnPress(-1, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                imageAssetSetRenderable.setOnRelease(-1, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                imageAssetSetRenderable.press(-1, TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                imageAssetSetRenderable.press(-1, TIMESTAMP + 1));

        assertThrows(IllegalArgumentException.class, () ->
                imageAssetSetRenderable.setOnPress(8, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                imageAssetSetRenderable.setOnRelease(8, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                imageAssetSetRenderable.press(8, TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                imageAssetSetRenderable.press(8, TIMESTAMP + 3));
    }

    @Test
    public void testMouseOverAndSetOnMouseOver() {

        imageAssetSetRenderable.mouseOver(TIMESTAMP);

        verify(mockOnMouseOverAction).run(eq(MouseEventInputs.of(TIMESTAMP,
                imageAssetSetRenderable)));

        //noinspection unchecked
        Action<MouseEventInputs> newOnMouseOver = mock(Action.class);
        imageAssetSetRenderable.setOnMouseOver(newOnMouseOver);

        imageAssetSetRenderable.mouseOver(TIMESTAMP + 1);

        verify(newOnMouseOver).run(eq(MouseEventInputs.of(TIMESTAMP + 1,
                imageAssetSetRenderable)));
    }

    @Test
    public void testMouseOverActionId() {
        var mouseOverActionId = randomString();

        imageAssetSetRenderable.setOnMouseOver(null);

        assertNull(imageAssetSetRenderable.mouseOverActionId());

        //noinspection unchecked
        imageAssetSetRenderable.setOnMouseOver(generateMockWithId(Action.class, mouseOverActionId));

        assertEquals(mouseOverActionId, imageAssetSetRenderable.mouseOverActionId());
    }

    @Test
    public void testMouseLeaveAndSetOnMouseLeave() {
        imageAssetSetRenderable.mouseLeave(TIMESTAMP);

        verify(mockOnMouseLeaveAction).run(
                eq(MouseEventInputs.of(TIMESTAMP, imageAssetSetRenderable)));

        //noinspection unchecked
        var newOnMouseLeave = (Action<MouseEventInputs>) mock(Action.class);
        imageAssetSetRenderable.setOnMouseLeave(newOnMouseLeave);

        imageAssetSetRenderable.mouseLeave(TIMESTAMP + 1);

        verify(newOnMouseLeave).run(
                eq(MouseEventInputs.of(TIMESTAMP + 1, imageAssetSetRenderable)));
    }

    @Test
    public void testMouseLeaveActionId() {
        var mouseLeaveActionId = randomString();

        imageAssetSetRenderable.setOnMouseLeave(null);

        assertNull(imageAssetSetRenderable.mouseLeaveActionId());

        //noinspection unchecked
        imageAssetSetRenderable.setOnMouseLeave(
                generateMockWithId(Action.class, mouseLeaveActionId));

        assertEquals(mouseLeaveActionId, imageAssetSetRenderable.mouseLeaveActionId());
    }

    @Test
    public void testMouseEventsWhenNotSupportingMouseEvents() {
        imageAssetSetRenderable.setCapturesMouseEvents(false);

        //noinspection unchecked
        assertThrows(UnsupportedOperationException.class,
                () -> imageAssetSetRenderable.setOnPress(randomIntInRange(0, 7),
                        (Action<MouseEventInputs>) mock(Action.class)));
        assertThrows(UnsupportedOperationException.class,
                () -> imageAssetSetRenderable.press(randomIntInRange(0, 7), TIMESTAMP + 1));
        //noinspection unchecked
        assertThrows(UnsupportedOperationException.class,
                () -> imageAssetSetRenderable.setOnRelease(randomIntInRange(0, 7),
                        (Action<MouseEventInputs>) mock(Action.class)));
        assertThrows(UnsupportedOperationException.class,
                () -> imageAssetSetRenderable.release(randomIntInRange(0, 7), TIMESTAMP + 1));
        //noinspection unchecked
        assertThrows(UnsupportedOperationException.class,
                () -> imageAssetSetRenderable.setOnMouseOver(
                        (Action<MouseEventInputs>) mock(Action.class)));
        assertThrows(UnsupportedOperationException.class,
                () -> imageAssetSetRenderable.mouseOver(TIMESTAMP + 1));
        //noinspection unchecked
        assertThrows(UnsupportedOperationException.class,
                () -> imageAssetSetRenderable.setOnMouseLeave(
                        (Action<MouseEventInputs>) mock(Action.class)));
        assertThrows(UnsupportedOperationException.class,
                () -> imageAssetSetRenderable.mouseLeave(TIMESTAMP + 1));
        assertThrows(UnsupportedOperationException.class,
                () -> imageAssetSetRenderable.capturesMouseEventAtPoint(vertexOf(0f, 0f),
                        TIMESTAMP + 1));
    }

    @Test
    public void testMouseEventCallsToOutdatedTimestamps() {
        imageAssetSetRenderable.press(0, TIMESTAMP);
        assertThrows(IllegalArgumentException.class, () ->
                imageAssetSetRenderable.press(0, TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                imageAssetSetRenderable.release(0, TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                imageAssetSetRenderable.mouseOver(TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                imageAssetSetRenderable.mouseLeave(TIMESTAMP - 1));

        imageAssetSetRenderable.release(0, TIMESTAMP + 1);
        assertThrows(IllegalArgumentException.class, () ->
                imageAssetSetRenderable.press(0, TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                imageAssetSetRenderable.release(0, TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                imageAssetSetRenderable.mouseOver(TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                imageAssetSetRenderable.mouseLeave(TIMESTAMP));

        imageAssetSetRenderable.mouseOver(TIMESTAMP + 2);
        assertThrows(IllegalArgumentException.class, () ->
                imageAssetSetRenderable.press(0, TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                imageAssetSetRenderable.release(0, TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                imageAssetSetRenderable.mouseOver(TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                imageAssetSetRenderable.mouseLeave(TIMESTAMP + 1));

        imageAssetSetRenderable.mouseLeave(TIMESTAMP + 3);
        assertThrows(IllegalArgumentException.class, () ->
                imageAssetSetRenderable.press(0, TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                imageAssetSetRenderable.release(0, TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                imageAssetSetRenderable.mouseOver(TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                imageAssetSetRenderable.mouseLeave(TIMESTAMP + 2));
    }

    @Test
    public void testColorShiftProviders() {
        assertSame(colorShiftProviders,
                imageAssetSetRenderable.colorShiftProviders());
    }

    @Test
    public void testGetAndSetRenderingDimensionsProvider() {
        assertSame(mockRenderingAreaProvider,
                imageAssetSetRenderable.getRenderingDimensionsProvider());

        //noinspection unchecked
        var newRenderingDimensionsProvider = (ProviderAtTime<FloatBox>) mock(ProviderAtTime.class);

        imageAssetSetRenderable
                .setRenderingDimensionsProvider(newRenderingDimensionsProvider);

        assertSame(newRenderingDimensionsProvider,
                imageAssetSetRenderable.getRenderingDimensionsProvider());
    }

    @Test
    public void testSetRenderingDimensionsProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> imageAssetSetRenderable.setRenderingDimensionsProvider(null));
    }

    @Test
    public void testGetAndSetZ() {
        assertEquals(Z, imageAssetSetRenderable.getZ());

        var newZ = randomInt();

        imageAssetSetRenderable.setZ(newZ);

        assertEquals(newZ, imageAssetSetRenderable.getZ());

        verify(mockContainingStack).add(imageAssetSetRenderable);
    }

    @Test
    public void testCapturesMouseEventAtPointForSprite() {
        when(mockImageAssetSet.getImageAssetForTypeAndDirection(anyString(), any()))
                .thenReturn(mockSprite);
        var expectedImageX =
                (int) (((POINT_X - RENDERING_AREA_LEFT_X) / RENDERING_AREA.width()) *
                        (SNIPPET_RIGHT_X - SNIPPET_LEFT_X)) + SNIPPET_LEFT_X;
        var expectedImageY =
                (int) (((POINT_Y - RENDERING_AREA_TOP_Y) / RENDERING_AREA.height()) *
                        (SNIPPET_BOTTOM_Y - SNIPPET_TOP_Y)) + SNIPPET_TOP_Y;

        var capturesMouseEventAtPoint =
                imageAssetSetRenderable.capturesMouseEventAtPoint(POINT, TIMESTAMP);

        assertEquals(IMAGE_CAPTURES_EVENTS_AT_PIXEL, capturesMouseEventAtPoint);
        var inOrder = inOrder(mockImageAssetSet, mockRenderingBoundaries, mockSprite, mockImage);
        inOrder.verify(mockRenderingBoundaries).currentBoundaries();
        inOrder.verify(mockImageAssetSet).getImageAssetForTypeAndDirection(TYPE, DIRECTION);
        inOrder.verify(mockSprite).image();
        inOrder.verify(mockImage).capturesMouseEventsAtPixel(expectedImageX, expectedImageY);
    }

    @Test
    public void testCapturesMouseEventAtPointForAnimation() {
        when(mockImageAssetSet.getImageAssetForTypeAndDirection(anyString(), any()))
                .thenReturn(mockAnimation);
        var expectedImageX =
                (int) ((((POINT_X - ANIMATION_FRAME_SNIPPET_OFFSET_X) - RENDERING_AREA_LEFT_X) /
                        RENDERING_AREA.width()) * (SNIPPET_RIGHT_X - SNIPPET_LEFT_X)) +
                        SNIPPET_LEFT_X;
        var expectedImageY =
                (int) ((((POINT_Y - ANIMATION_FRAME_SNIPPET_OFFSET_Y) - RENDERING_AREA_TOP_Y) /
                        RENDERING_AREA.height()) * (SNIPPET_BOTTOM_Y - SNIPPET_TOP_Y)) +
                        SNIPPET_TOP_Y;

        var capturesMouseEventAtPoint =
                imageAssetSetRenderable.capturesMouseEventAtPoint(POINT, TIMESTAMP);

        assertEquals(IMAGE_CAPTURES_EVENTS_AT_PIXEL, capturesMouseEventAtPoint);
        var inOrder = inOrder(mockImageAssetSet, mockRenderingBoundaries, mockAnimation,
                mockAnimationFrameSnippet, mockImage);
        inOrder.verify(mockRenderingBoundaries).currentBoundaries();
        inOrder.verify(mockImageAssetSet).getImageAssetForTypeAndDirection(TYPE, DIRECTION);
        inOrder.verify(mockAnimation).snippetAtFrame((int) (TIMESTAMP % ANIMATION_MS_DURATION));
        inOrder.verify(mockAnimationFrameSnippet).offsetX();
        inOrder.verify(mockAnimationFrameSnippet).offsetY();
        inOrder.verify(mockAnimationFrameSnippet).image();
        inOrder.verify(mockImage).capturesMouseEventsAtPixel(expectedImageX, expectedImageY);
    }

    @Test
    public void testCapturesMouseEventAtPointForGlobalLoopingAnimation() {
        when(mockImageAssetSet.getImageAssetForTypeAndDirection(anyString(), any()))
                .thenReturn(mockGlobalLoopingAnimation);
        var expectedImageX =
                (int) ((((POINT_X - ANIMATION_FRAME_SNIPPET_OFFSET_X) - RENDERING_AREA_LEFT_X) /
                        RENDERING_AREA.width()) * (SNIPPET_RIGHT_X - SNIPPET_LEFT_X)) +
                        SNIPPET_LEFT_X;
        var expectedImageY =
                (int) ((((POINT_Y - ANIMATION_FRAME_SNIPPET_OFFSET_Y) - RENDERING_AREA_TOP_Y) /
                        RENDERING_AREA.height()) * (SNIPPET_BOTTOM_Y - SNIPPET_TOP_Y)) +
                        SNIPPET_TOP_Y;

        var capturesMouseEventAtPoint =
                imageAssetSetRenderable.capturesMouseEventAtPoint(POINT, TIMESTAMP);

        assertEquals(IMAGE_CAPTURES_EVENTS_AT_PIXEL, capturesMouseEventAtPoint);
        var inOrder =
                inOrder(mockImageAssetSet, mockRenderingBoundaries, mockGlobalLoopingAnimation,
                        mockAnimationFrameSnippet, mockImage);
        inOrder.verify(mockRenderingBoundaries).currentBoundaries();
        inOrder.verify(mockImageAssetSet).getImageAssetForTypeAndDirection(TYPE, DIRECTION);
        inOrder.verify(mockGlobalLoopingAnimation).provide(TIMESTAMP);
        inOrder.verify(mockAnimationFrameSnippet).offsetX();
        inOrder.verify(mockAnimationFrameSnippet).offsetY();
        inOrder.verify(mockAnimationFrameSnippet).image();
        inOrder.verify(mockImage).capturesMouseEventsAtPixel(expectedImageX, expectedImageY);
    }

    @Test
    public void testCapturesMouseEventAtPointWhenExceedingRenderingBoundaries() {
        var distanceWithinWindow = randomFloatInRange(VERY_SMALL_NUMBER, 1f - VERY_SMALL_NUMBER);
        when(mockImageAssetSet.getImageAssetForTypeAndDirection(anyString(), any()))
                .thenReturn(mockSprite);
        when(mockRenderingAreaProvider.provide(anyLong())).thenReturn(WHOLE_SCREEN);
        when(mockRenderingBoundaries.currentBoundaries())
                .thenReturn(floatBoxOf(0f, 0f, distanceWithinWindow, distanceWithinWindow));
        when(mockImage.capturesMouseEventsAtPixel(anyInt(), anyInt())).thenReturn(true);

        assertTrue(imageAssetSetRenderable.capturesMouseEventAtPoint(
                vertexOf(Math.min(1f, distanceWithinWindow - VERY_SMALL_NUMBER), 0f), TIMESTAMP));
        assertFalse(imageAssetSetRenderable.capturesMouseEventAtPoint(
                vertexOf(Math.min(1f, distanceWithinWindow + VERY_SMALL_NUMBER), 0f), TIMESTAMP));
    }

    @Test
    public void testCapturesMouseEventAtPointWithInvalidArgs() {
        when(mockRenderingAreaProvider.provide(anyLong())).thenReturn(
                floatBoxOf(0.5f, 1.5f, 0.5f, 1.5f));
        when(mockRenderingBoundaries.currentBoundaries()).thenReturn(WHOLE_SCREEN);

        assertThrows(IllegalArgumentException.class, () -> imageAssetSetRenderable
                .capturesMouseEventAtPoint(vertexOf(.5f - VERY_SMALL_NUMBER, .75f), 0L));
        assertThrows(IllegalArgumentException.class, () -> imageAssetSetRenderable
                .capturesMouseEventAtPoint(vertexOf(1f + VERY_SMALL_NUMBER, .75f), 0L));
        assertThrows(IllegalArgumentException.class, () -> imageAssetSetRenderable
                .capturesMouseEventAtPoint(vertexOf(.75f, .5f - VERY_SMALL_NUMBER), 0L));
        assertThrows(IllegalArgumentException.class, () -> imageAssetSetRenderable
                .capturesMouseEventAtPoint(vertexOf(.75f, 1.5f + VERY_SMALL_NUMBER), 0L));

        when(mockRenderingAreaProvider.provide(anyLong())).thenReturn(
                floatBoxOf(-0.5f, 0.5f, -0.5f, 0.5f));

        assertThrows(IllegalArgumentException.class, () -> imageAssetSetRenderable
                .capturesMouseEventAtPoint(vertexOf(0f - VERY_SMALL_NUMBER, .25f), 0L));
        assertThrows(IllegalArgumentException.class, () -> imageAssetSetRenderable
                .capturesMouseEventAtPoint(vertexOf(0.5f + VERY_SMALL_NUMBER, .25f), 0L));
        assertThrows(IllegalArgumentException.class, () -> imageAssetSetRenderable
                .capturesMouseEventAtPoint(vertexOf(.25f, 0f - VERY_SMALL_NUMBER), 0L));
        assertThrows(IllegalArgumentException.class, () -> imageAssetSetRenderable
                .capturesMouseEventAtPoint(vertexOf(.25f, 0.5f + VERY_SMALL_NUMBER), 0L));
    }

    @Test
    public void testDelete() {
        imageAssetSetRenderable.delete();

        verify(mockContainingStack).remove(imageAssetSetRenderable);
    }

    @Test
    public void testUuid() {
        assertSame(UUID, imageAssetSetRenderable.uuid());
    }
}
