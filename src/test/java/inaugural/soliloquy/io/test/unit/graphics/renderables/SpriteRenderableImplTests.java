package inaugural.soliloquy.io.test.unit.graphics.renderables;

import inaugural.soliloquy.io.graphics.renderables.SpriteRenderableImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.io.graphics.renderables.RenderableWithMouseEvents.MouseEventInputs;
import soliloquy.specs.io.graphics.renderables.SpriteRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderableStack;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;

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

public class SpriteRenderableImplTests {
    private final FakeSprite SPRITE_SUPPORTING_MOUSE_EVENTS = new FakeSprite(new FakeImage(true));
    private final FakeSprite SPRITE_NOT_SUPPORTING_MOUSE_EVENTS =
            new FakeSprite(new FakeImage(false));
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

    private SpriteRenderable spriteRenderableWithMouseEvents;
    private SpriteRenderable spriteRenderableWithoutMouseEvents;

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

        spriteRenderableWithMouseEvents = new SpriteRenderableImpl(
                SPRITE_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction,
                COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack,
                mockRenderingBoundaries);
        spriteRenderableWithoutMouseEvents = new SpriteRenderableImpl(
                SPRITE_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingStack, mockRenderingBoundaries);
    }

    @Test
    public void testConstructorWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                null, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                COLOR_SHIFT_PROVIDERS,
                RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                SPRITE_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, mockOnMouseOverAction,
                mockOnMouseLeaveAction,
                COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack,
                mockRenderingBoundaries
        ));
        // NB: These following two constructors should not_ throw exceptions
        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                SPRITE_SUPPORTING_MOUSE_EVENTS, null, BORDER_COLOR_PROVIDER,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                COLOR_SHIFT_PROVIDERS,
                RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                SPRITE_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, null,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                COLOR_SHIFT_PROVIDERS,
                RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                SPRITE_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction, null,
                RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                SPRITE_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                COLOR_SHIFT_PROVIDERS,
                null, Z, UUID, mockContainingStack, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                SPRITE_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                COLOR_SHIFT_PROVIDERS,
                RENDERING_AREA_PROVIDER, Z, null, mockContainingStack, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                SPRITE_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                COLOR_SHIFT_PROVIDERS,
                RENDERING_AREA_PROVIDER, Z, UUID, null, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                SPRITE_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                ON_PRESS_ACTIONS, null, mockOnMouseOverAction, mockOnMouseLeaveAction,
                COLOR_SHIFT_PROVIDERS,
                RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack, null
        ));

        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                null, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS,
                RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                SPRITE_NOT_SUPPORTING_MOUSE_EVENTS, null, BORDER_COLOR_PROVIDER,
                COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack,
                mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                SPRITE_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER, null,
                COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack,
                mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                SPRITE_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, null, RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack,
                mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                SPRITE_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, null, Z, UUID, mockContainingStack,
                mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                SPRITE_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, null,
                mockContainingStack, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                SPRITE_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                null, mockRenderingBoundaries
        ));
        assertThrows(IllegalArgumentException.class, () -> new SpriteRenderableImpl(
                SPRITE_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingStack, null
        ));
    }

    @Test
    public void testGetAndSetSprite() {
        assertSame(SPRITE_SUPPORTING_MOUSE_EVENTS, spriteRenderableWithMouseEvents.getSprite());
        assertSame(SPRITE_NOT_SUPPORTING_MOUSE_EVENTS,
                spriteRenderableWithoutMouseEvents.getSprite());

        FakeSprite newSprite = new FakeSprite(new FakeImage(true));

        spriteRenderableWithMouseEvents.setSprite(newSprite);
        spriteRenderableWithoutMouseEvents.setSprite(newSprite);

        assertSame(newSprite, spriteRenderableWithMouseEvents.getSprite());
        assertSame(newSprite, spriteRenderableWithoutMouseEvents.getSprite());
    }

    @Test
    public void testSetSpriteWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.setSprite(null));
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithoutMouseEvents.setSprite(null));
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.setSprite(new FakeSprite(new FakeImage(false))));
    }

    @Test
    public void testGetAndSetBorderThicknessProvider() {
        assertSame(BORDER_THICKNESS_PROVIDER,
                spriteRenderableWithMouseEvents.getBorderThicknessProvider());
        assertSame(BORDER_THICKNESS_PROVIDER,
                spriteRenderableWithoutMouseEvents.getBorderThicknessProvider());

        FakeProviderAtTime<Float> newBorderThicknessProvider = new FakeProviderAtTime<>();

        spriteRenderableWithMouseEvents.setBorderThicknessProvider(newBorderThicknessProvider);
        spriteRenderableWithoutMouseEvents.setBorderThicknessProvider(newBorderThicknessProvider);

        assertSame(newBorderThicknessProvider,
                spriteRenderableWithMouseEvents.getBorderThicknessProvider());
        assertSame(newBorderThicknessProvider,
                spriteRenderableWithoutMouseEvents.getBorderThicknessProvider());
    }

    @Test
    public void testSetBorderThicknessProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.setBorderThicknessProvider(null));
    }

    @Test
    public void testGetAndSetBorderColorProvider() {
        assertSame(BORDER_COLOR_PROVIDER,
                spriteRenderableWithMouseEvents.getBorderColorProvider());
        assertSame(BORDER_COLOR_PROVIDER,
                spriteRenderableWithoutMouseEvents.getBorderColorProvider());

        FakeProviderAtTime<Color> newBorderColorProvider = new FakeProviderAtTime<>();

        spriteRenderableWithMouseEvents.setBorderColorProvider(newBorderColorProvider);
        spriteRenderableWithoutMouseEvents.setBorderColorProvider(newBorderColorProvider);

        assertSame(newBorderColorProvider,
                spriteRenderableWithMouseEvents.getBorderColorProvider());
        assertSame(newBorderColorProvider,
                spriteRenderableWithoutMouseEvents.getBorderColorProvider());
    }

    @Test
    public void testSetBorderColorProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.setBorderColorProvider(null));
    }

    @Test
    public void testGetAndSetCapturesMouseEvents() {
        assertTrue(spriteRenderableWithMouseEvents.getCapturesMouseEvents());
        assertFalse(spriteRenderableWithoutMouseEvents.getCapturesMouseEvents());

        spriteRenderableWithMouseEvents.setCapturesMouseEvents(false);
        assertThrows(UnsupportedOperationException.class, () ->
                spriteRenderableWithoutMouseEvents.setCapturesMouseEvents(false));

        assertFalse(spriteRenderableWithMouseEvents.getCapturesMouseEvents());
    }

    @Test
    public void testPressAndSetOnPress() {
        assertThrows(UnsupportedOperationException.class, () ->
                spriteRenderableWithoutMouseEvents.press(2, 0L));
        assertThrows(UnsupportedOperationException.class, () ->
                spriteRenderableWithoutMouseEvents.setOnPress(2, new FakeAction<>()));

        spriteRenderableWithMouseEvents.setOnPress(2, mockOnPressAction);

        spriteRenderableWithMouseEvents.press(2, TIMESTAMP);

        verify(mockOnPressAction, once()).run(
                eq(MouseEventInputs.of(TIMESTAMP, spriteRenderableWithMouseEvents)));

        //noinspection unchecked
        Action<MouseEventInputs> newOnPress = mock(Action.class);
        spriteRenderableWithMouseEvents.setOnPress(2, newOnPress);

        spriteRenderableWithMouseEvents.press(2, TIMESTAMP + 1);

        verify(newOnPress, once()).run(
                eq(MouseEventInputs.of(TIMESTAMP + 1, spriteRenderableWithMouseEvents)));

        spriteRenderableWithMouseEvents.press(0, TIMESTAMP + 2);

        verify(newOnPress, once()).run(any());
    }

    @Test
    public void testPressActionIds() {
        String id1 = randomString();
        String id2 = randomString();
        String id3 = randomString();

        spriteRenderableWithMouseEvents.setOnPress(0, new FakeAction<>(id1));
        spriteRenderableWithMouseEvents.setOnPress(2, new FakeAction<>(id2));
        spriteRenderableWithMouseEvents.setOnPress(7, new FakeAction<>(id3));
        spriteRenderableWithMouseEvents.setOnPress(2, null);

        Map<Integer, String> pressActionIds = spriteRenderableWithMouseEvents.pressActionIds();

        assertNotNull(pressActionIds);
        assertEquals(2, pressActionIds.size());
        assertEquals(id1, pressActionIds.get(0));
        assertEquals(id3, pressActionIds.get(7));
    }

    @Test
    public void testReleaseAndSetOnRelease() {
        assertThrows(UnsupportedOperationException.class, () ->
                spriteRenderableWithoutMouseEvents.release(2, 0L));
        assertThrows(UnsupportedOperationException.class, () ->
                spriteRenderableWithoutMouseEvents.setOnRelease(2, new FakeAction<>()));

        spriteRenderableWithMouseEvents.release(2, TIMESTAMP);

        //noinspection unchecked
        Action<MouseEventInputs> newOnRelease = mock(Action.class);
        spriteRenderableWithMouseEvents.setOnRelease(2, newOnRelease);

        spriteRenderableWithMouseEvents.release(2, TIMESTAMP + 1);

        verify(newOnRelease, once()).run(
                eq(MouseEventInputs.of(TIMESTAMP + 1, spriteRenderableWithMouseEvents)));
    }

    @Test
    public void testReleaseActionIds() {
        String id1 = randomString();
        String id2 = randomString();
        String id3 = randomString();

        spriteRenderableWithMouseEvents.setOnRelease(0, new FakeAction<>(id1));
        spriteRenderableWithMouseEvents.setOnRelease(2, new FakeAction<>(id2));
        spriteRenderableWithMouseEvents.setOnRelease(7, new FakeAction<>(id3));
        spriteRenderableWithMouseEvents.setOnRelease(2, null);

        Map<Integer, String> releaseActionIds = spriteRenderableWithMouseEvents.releaseActionIds();

        assertNotNull(releaseActionIds);
        assertEquals(2, releaseActionIds.size());
        assertEquals(id1, releaseActionIds.get(0));
        assertEquals(id3, releaseActionIds.get(7));
    }

    @Test
    public void testPressOrReleaseMethodsWithInvalidButtons() {

        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.setOnPress(-1, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.setOnRelease(-1, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.press(-1, TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.press(-1, TIMESTAMP + 1));

        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.setOnPress(8, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.setOnRelease(8, new FakeAction<>()));
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.press(8, TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.press(8, TIMESTAMP + 3));
    }

    @Test
    public void testMouseOverAndSetOnMouseOver() {
        assertThrows(UnsupportedOperationException.class, () ->
                spriteRenderableWithoutMouseEvents.mouseOver(0L));
        assertThrows(UnsupportedOperationException.class, () ->
                spriteRenderableWithoutMouseEvents.setOnMouseOver(mockOnMouseOverAction));

        spriteRenderableWithMouseEvents.mouseOver(TIMESTAMP);

        verify(mockOnMouseOverAction, once()).run(
                eq(MouseEventInputs.of(TIMESTAMP, spriteRenderableWithMouseEvents)));

        //noinspection unchecked
        Action<MouseEventInputs> newOnMouseOver = mock(Action.class);
        spriteRenderableWithMouseEvents.setOnMouseOver(newOnMouseOver);

        spriteRenderableWithMouseEvents.mouseOver(TIMESTAMP + 1);

        verify(newOnMouseOver, once()).run(
                eq(MouseEventInputs.of(TIMESTAMP + 1, spriteRenderableWithMouseEvents)));
    }

    @Test
    public void testMouseOverActionId() {
        String mouseOverActionId = randomString();

        assertThrows(UnsupportedOperationException.class, () ->
                spriteRenderableWithoutMouseEvents.mouseOverActionId());

        spriteRenderableWithMouseEvents.setOnMouseOver(null);

        assertNull(spriteRenderableWithMouseEvents.mouseOverActionId());

        spriteRenderableWithMouseEvents.setOnMouseOver(new FakeAction<>(mouseOverActionId));

        assertEquals(mouseOverActionId, spriteRenderableWithMouseEvents.mouseOverActionId());
    }

    @Test
    public void testMouseLeaveAndSetOnMouseLeave() {
        assertThrows(UnsupportedOperationException.class, () ->
                spriteRenderableWithoutMouseEvents.mouseLeave(0L));
        assertThrows(UnsupportedOperationException.class, () ->
                spriteRenderableWithoutMouseEvents.setOnMouseLeave(mockOnMouseLeaveAction));

        spriteRenderableWithMouseEvents.mouseLeave(TIMESTAMP);

        verify(mockOnMouseLeaveAction, once()).run(
                eq(MouseEventInputs.of(TIMESTAMP, spriteRenderableWithMouseEvents)));

        //noinspection unchecked
        Action<MouseEventInputs> newOnMouseLeave = mock(Action.class);
        spriteRenderableWithMouseEvents.setOnMouseLeave(newOnMouseLeave);

        spriteRenderableWithMouseEvents.mouseLeave(TIMESTAMP + 1);

        verify(newOnMouseLeave, once()).run(
                eq(MouseEventInputs.of(TIMESTAMP + 1, spriteRenderableWithMouseEvents)));
    }

    @Test
    public void testMouseLeaveActionId() {
        String mouseLeaveActionId = randomString();

        assertThrows(UnsupportedOperationException.class, () ->
                spriteRenderableWithoutMouseEvents.mouseLeaveActionId());

        spriteRenderableWithMouseEvents.setOnMouseLeave(null);

        assertNull(spriteRenderableWithMouseEvents.mouseLeaveActionId());

        spriteRenderableWithMouseEvents.setOnMouseLeave(new FakeAction<>(mouseLeaveActionId));

        assertEquals(mouseLeaveActionId, spriteRenderableWithMouseEvents.mouseLeaveActionId());
    }

    @Test
    public void testMouseEventCallsToOutdatedTimestamps() {
        RENDERING_AREA_PROVIDER.ProvidedValue = floatBoxOf(0f, 0f, 1f, 1f);

        spriteRenderableWithMouseEvents.press(0, TIMESTAMP);
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.press(0, TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.release(0, TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.mouseOver(TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.mouseLeave(TIMESTAMP - 1));
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.capturesMouseEventAtPoint(vertexOf(0f, 0f),
                        TIMESTAMP - 1));

        spriteRenderableWithMouseEvents.release(0, TIMESTAMP + 1);
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.press(0, TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.release(0, TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.mouseOver(TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.mouseLeave(TIMESTAMP));
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.capturesMouseEventAtPoint(vertexOf(0f, 0f),
                        TIMESTAMP));

        spriteRenderableWithMouseEvents.mouseOver(TIMESTAMP + 2);
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.press(0, TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.release(0, TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.mouseOver(TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.mouseLeave(TIMESTAMP + 1));
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.capturesMouseEventAtPoint(vertexOf(0f, 0f),
                        TIMESTAMP + 1));

        spriteRenderableWithMouseEvents.mouseLeave(TIMESTAMP + 3);
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.press(0, TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.release(0, TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.mouseOver(TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.mouseLeave(TIMESTAMP + 2));
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.capturesMouseEventAtPoint(vertexOf(0f, 0f),
                        TIMESTAMP + 2));

        spriteRenderableWithMouseEvents.capturesMouseEventAtPoint(vertexOf(0f, 0f), TIMESTAMP + 4);
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.press(0, TIMESTAMP + 3));
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.release(0, TIMESTAMP + 3));
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.mouseOver(TIMESTAMP + 3));
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.mouseLeave(TIMESTAMP + 3));
        assertThrows(IllegalArgumentException.class, () ->
                spriteRenderableWithMouseEvents.capturesMouseEventAtPoint(vertexOf(0f, 0f),
                        TIMESTAMP + 3));
    }

    @Test
    public void testColorShiftProviders() {
        assertSame(COLOR_SHIFT_PROVIDERS,
                spriteRenderableWithMouseEvents.colorShiftProviders());
        assertSame(COLOR_SHIFT_PROVIDERS,
                spriteRenderableWithoutMouseEvents.colorShiftProviders());
    }

    @Test
    public void testGetAndSetRenderingAreaProvider() {
        assertSame(RENDERING_AREA_PROVIDER,
                spriteRenderableWithMouseEvents.getRenderingDimensionsProvider());
        assertSame(RENDERING_AREA_PROVIDER,
                spriteRenderableWithoutMouseEvents.getRenderingDimensionsProvider());

        FakeProviderAtTime<FloatBox> newRenderingDimensionsProvider = new FakeProviderAtTime<>();

        spriteRenderableWithMouseEvents
                .setRenderingDimensionsProvider(newRenderingDimensionsProvider);
        spriteRenderableWithoutMouseEvents
                .setRenderingDimensionsProvider(newRenderingDimensionsProvider);

        assertSame(newRenderingDimensionsProvider,
                spriteRenderableWithMouseEvents.getRenderingDimensionsProvider());
        assertSame(newRenderingDimensionsProvider,
                spriteRenderableWithoutMouseEvents.getRenderingDimensionsProvider());
    }

    @Test
    public void testGetAndSetZ() {
        assertEquals(Z, spriteRenderableWithMouseEvents.getZ());
        assertEquals(Z, spriteRenderableWithoutMouseEvents.getZ());

        int newZ = 456;

        spriteRenderableWithMouseEvents.setZ(newZ);
        spriteRenderableWithoutMouseEvents.setZ(newZ);

        assertEquals(newZ, spriteRenderableWithMouseEvents.getZ());
        assertEquals(newZ, spriteRenderableWithoutMouseEvents.getZ());

        verify(mockContainingStack, once()).add(spriteRenderableWithMouseEvents);
        verify(mockContainingStack, once()).add(spriteRenderableWithoutMouseEvents);
    }

    @Test
    public void testCapturesMouseEventAtPoint() {
        SPRITE_SUPPORTING_MOUSE_EVENTS.LeftX = 250;
        SPRITE_SUPPORTING_MOUSE_EVENTS.RightX = 750;
        SPRITE_SUPPORTING_MOUSE_EVENTS.TopY = 1000;
        SPRITE_SUPPORTING_MOUSE_EVENTS.BottomY = 2500;
        ((FakeImage) SPRITE_SUPPORTING_MOUSE_EVENTS.Image).Width = 1000;
        ((FakeImage) SPRITE_SUPPORTING_MOUSE_EVENTS.Image).Height = 3000;
        RENDERING_AREA_PROVIDER.ProvidedValue = floatBoxOf(-0.5f, -2f, 0.75f, 0.5f);

        boolean capturesMouseEventAtPoint = spriteRenderableWithMouseEvents
                .capturesMouseEventAtPoint(vertexOf(0.123f, 0.456f), TIMESTAMP);

        assertTrue(capturesMouseEventAtPoint);
        List<Pair<Integer, Integer>> capturesMouseEventsAtPixelInputs =
                ((FakeImage) SPRITE_SUPPORTING_MOUSE_EVENTS.Image).CapturesMouseEventsAtPixelInputs;
        assertEquals(1, capturesMouseEventsAtPixelInputs.size());
        assertEquals(
                (int) ((((0.123f - (-0.5f)) / (0.75f - (-0.5f))) * (750 - 250)) + 250),
                (int) capturesMouseEventsAtPixelInputs.getFirst().FIRST);
        assertEquals(
                (int) ((((0.456f - (-2.0f)) / (0.5f - (-2.0f))) * (2500 - 1000)) + 1000),
                (int) capturesMouseEventsAtPixelInputs.getFirst().SECOND);
        assertEquals(1, RENDERING_AREA_PROVIDER.TimestampInputs.size());
        assertEquals(TIMESTAMP, (long) RENDERING_AREA_PROVIDER.TimestampInputs.getFirst());
    }

    @Test
    public void testCapturesMouseEventsAtPointDoesNotExceedRenderingBoundaries() {
        RENDERING_AREA_PROVIDER.ProvidedValue = WHOLE_SCREEN;
        SPRITE_SUPPORTING_MOUSE_EVENTS.Image = new FakeImage(true);
        when(mockRenderingBoundaries.currentBoundaries()).thenReturn(floatBoxOf(0f, 0f, 0.5f, 1f));

        assertTrue(spriteRenderableWithMouseEvents
                .capturesMouseEventAtPoint(vertexOf(0.499f, 0.5f), TIMESTAMP));
        assertFalse(spriteRenderableWithMouseEvents
                .capturesMouseEventAtPoint(vertexOf(0.501f, 0.5f), TIMESTAMP));
    }

    @Test
    public void testCapturesMouseEventAtPointWithInvalidArgs() {
        float verySmallNumber = 0.0001f;

        assertThrows(UnsupportedOperationException.class, () ->
                spriteRenderableWithoutMouseEvents.capturesMouseEventAtPoint(vertexOf(.5f, .5f),
                        0L));

        RENDERING_AREA_PROVIDER.ProvidedValue = floatBoxOf(.5f, .5f, 1.5f, 1.5f);

        assertThrows(IllegalArgumentException.class, () -> spriteRenderableWithMouseEvents
                .capturesMouseEventAtPoint(vertexOf(.5f - verySmallNumber, .75f), 0L));
        assertThrows(IllegalArgumentException.class, () -> spriteRenderableWithMouseEvents
                .capturesMouseEventAtPoint(vertexOf(1f + verySmallNumber, .75f), 0L));
        assertThrows(IllegalArgumentException.class, () -> spriteRenderableWithMouseEvents
                .capturesMouseEventAtPoint(vertexOf(.75f, .5f - verySmallNumber), 0L));
        assertThrows(IllegalArgumentException.class, () -> spriteRenderableWithMouseEvents
                .capturesMouseEventAtPoint(vertexOf(.75f, 1.5f + verySmallNumber), 0L));

        RENDERING_AREA_PROVIDER.ProvidedValue = floatBoxOf(-0.5f, -0.5f, 0.5f, 0.5f);

        assertThrows(IllegalArgumentException.class, () -> spriteRenderableWithMouseEvents
                .capturesMouseEventAtPoint(vertexOf(0f - verySmallNumber, .25f), 0L));
        assertThrows(IllegalArgumentException.class, () -> spriteRenderableWithMouseEvents
                .capturesMouseEventAtPoint(vertexOf(0.5f + verySmallNumber, .25f), 0L));
        assertThrows(IllegalArgumentException.class, () -> spriteRenderableWithMouseEvents
                .capturesMouseEventAtPoint(vertexOf(.25f, 0f - verySmallNumber), 0L));
        assertThrows(IllegalArgumentException.class, () -> spriteRenderableWithMouseEvents
                .capturesMouseEventAtPoint(vertexOf(.25f, 0.5f + verySmallNumber), 0L));
    }

    @Test
    public void testDelete() {
        spriteRenderableWithMouseEvents.delete();
        spriteRenderableWithoutMouseEvents.delete();

        verify(mockContainingStack, once()).remove(spriteRenderableWithMouseEvents);
        verify(mockContainingStack, once()).remove(spriteRenderableWithoutMouseEvents);
    }

    @Test
    public void testUuid() {
        assertSame(UUID, spriteRenderableWithMouseEvents.uuid());
        assertSame(UUID, spriteRenderableWithoutMouseEvents.uuid());
    }
}
