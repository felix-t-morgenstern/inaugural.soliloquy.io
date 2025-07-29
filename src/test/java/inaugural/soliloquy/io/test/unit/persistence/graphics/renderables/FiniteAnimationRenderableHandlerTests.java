package inaugural.soliloquy.io.test.unit.persistence.graphics.renderables;

import inaugural.soliloquy.io.persistence.graphics.renderables.FiniteAnimationRenderableHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.assets.Animation;
import soliloquy.specs.io.graphics.renderables.FiniteAnimationRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.factories.FiniteAnimationRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.ui.EventInputs;

import java.awt.*;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static inaugural.soliloquy.tools.testing.Mock.*;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

@ExtendWith(MockitoExtension.class)
public class FiniteAnimationRenderableHandlerTests {
    private final String ANIMATION_ID = randomString();
    private final LookupAndEntitiesWithId<Animation> MOCK_ANIMATION_AND_LOOKUP =
            generateMockLookupFunctionWithId(Animation.class, ANIMATION_ID);
    private final Animation MOCK_ANIMATION = MOCK_ANIMATION_AND_LOOKUP.entities.getFirst();
    private final Function<String, Animation> MOCK_GET_ANIMATION = MOCK_ANIMATION_AND_LOOKUP.lookup;

    private final String BORDER_THICKNESS = randomString();
    private final String BORDER_COLOR = randomString();

    private final int ON_PRESS_BUTTON = randomInt();
    private final String ON_PRESS_ACTION_ID = randomString();
    private final int ON_RELEASE_BUTTON = randomInt();
    private final String ON_RELEASE_ACTION_ID = randomString();
    private final String ON_MOUSE_OVER_ACTION_ID = randomString();
    private final String ON_MOUSE_LEAVE_ACTION_ID = randomString();
    @SuppressWarnings("rawtypes") private final LookupAndEntitiesWithId<Action>
            MOCK_ACTIONS_AND_LOOKUP =
            generateMockLookupFunctionWithId(Action.class, ON_PRESS_ACTION_ID, ON_RELEASE_ACTION_ID,
                    ON_MOUSE_OVER_ACTION_ID, ON_MOUSE_LEAVE_ACTION_ID);
    @SuppressWarnings("unchecked") private final Action<EventInputs> MOCK_ON_PRESS_ACTION =
            MOCK_ACTIONS_AND_LOOKUP.entities.getFirst();
    @SuppressWarnings("unchecked") private final Action<EventInputs> MOCK_ON_RELEASE_ACTION =
            MOCK_ACTIONS_AND_LOOKUP.entities.get(1);
    @SuppressWarnings("unchecked") private final Action<EventInputs> MOCK_ON_MOUSE_OVER_ACTION =
            MOCK_ACTIONS_AND_LOOKUP.entities.get(2);
    @SuppressWarnings("unchecked") private final Action<EventInputs> MOCK_ON_MOUSE_LEAVE_ACTION =
            MOCK_ACTIONS_AND_LOOKUP.entities.get(3);
    @SuppressWarnings("rawtypes") private final Function<String, Action> MOCK_GET_ACTION =
            MOCK_ACTIONS_AND_LOOKUP.lookup;

    private final String COLOR_SHIFT = randomString();
    private final String AREA = randomString();
    private final int Z = randomInt();
    private final UUID UUID = randomUUID();
    private final long START = randomLong();
    private final long PAUSE = randomLong();

    @Mock private ProviderAtTime<Float> mockBorderThicknessProvider;
    @Mock private ProviderAtTime<Color> mockBorderColorProvider;
    @Mock private ProviderAtTime<FloatBox> mockAreaProvider;
    @SuppressWarnings("rawtypes") @Mock private TypeHandler<ProviderAtTime> mockProviderHandler;

    @Mock private ColorShift mockShift;
    @Mock private TypeHandler<ColorShift> mockShiftHandler;

    @Mock private FiniteAnimationRenderable mockRenderable;
    @Mock private FiniteAnimationRenderableFactory mockFactory;

    private Map<Integer, String> onPressIds;
    private Map<Integer, String> onReleaseIds;

    private final String WRITTEN_VALUE = String.format(
            "{\"animationId\":\"%s\",\"borderThickness\":\"%s\",\"borderColor\":\"%s\"," +
                    "\"onPress\":[{\"button\":%d,\"actionId\":\"%s\"}]," +
                    "\"onRelease\":[{\"button\":%d,\"actionId\":\"%s\"}],\"mouseOver\":\"%s\"," +
                    "\"mouseLeave\":\"%s\",\"colorShifts\":[\"%s\"],\"area\":\"%s\",\"z\":%d," +
                    "\"uuid\":\"%s\",\"start\":%d,\"pause\":%d}",
            ANIMATION_ID, BORDER_THICKNESS, BORDER_COLOR, ON_PRESS_BUTTON, ON_PRESS_ACTION_ID,
            ON_RELEASE_BUTTON, ON_RELEASE_ACTION_ID, ON_MOUSE_OVER_ACTION_ID,
            ON_MOUSE_LEAVE_ACTION_ID, COLOR_SHIFT, AREA, Z, UUID, START, PAUSE);

    private TypeHandler<FiniteAnimationRenderable> handler;

    @BeforeEach
    public void setUp() {
        onPressIds = mapOf(pairOf(ON_PRESS_BUTTON, ON_PRESS_ACTION_ID));
        onReleaseIds = mapOf(pairOf(ON_RELEASE_BUTTON, ON_RELEASE_ACTION_ID));

        hydrateMockHandler(mockProviderHandler,
                pairOf(mockBorderThicknessProvider, BORDER_THICKNESS),
                pairOf(mockBorderColorProvider, BORDER_COLOR),
                pairOf(mockAreaProvider, AREA)
        );

        hydrateMockHandler(mockShiftHandler,
                pairOf(mockShift, COLOR_SHIFT)
        );

        handler = new FiniteAnimationRenderableHandler(MOCK_GET_ANIMATION, MOCK_GET_ACTION,
                mockProviderHandler, mockShiftHandler, mockFactory);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableHandler(null, MOCK_GET_ACTION,
                        mockProviderHandler, mockShiftHandler, mockFactory));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableHandler(MOCK_GET_ANIMATION, null,
                        mockProviderHandler, mockShiftHandler, mockFactory));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableHandler(MOCK_GET_ANIMATION, MOCK_GET_ACTION,
                        null, mockShiftHandler, mockFactory));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableHandler(MOCK_GET_ANIMATION, MOCK_GET_ACTION,
                        mockProviderHandler, null, mockFactory));
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableHandler(MOCK_GET_ANIMATION, MOCK_GET_ACTION,
                        mockProviderHandler, mockShiftHandler, null));
    }

    @Test
    public void testWrite() {
        when(mockRenderable.animationId()).thenReturn(ANIMATION_ID);
        when(mockRenderable.getBorderThicknessProvider()).thenReturn(mockBorderThicknessProvider);
        when(mockRenderable.getBorderColorProvider()).thenReturn(mockBorderColorProvider);
        when(mockRenderable.pressActionIds()).thenReturn(onPressIds);
        when(mockRenderable.releaseActionIds()).thenReturn(onReleaseIds);
        when(mockRenderable.mouseOverActionId()).thenReturn(ON_MOUSE_OVER_ACTION_ID);
        when(mockRenderable.mouseLeaveActionId()).thenReturn(ON_MOUSE_LEAVE_ACTION_ID);
        when(mockRenderable.colorShifts()).thenReturn(listOf(mockShift));
        when(mockRenderable.getRenderingDimensionsProvider()).thenReturn(mockAreaProvider);
        when(mockRenderable.getZ()).thenReturn(Z);
        when(mockRenderable.uuid()).thenReturn(UUID);
        when(mockRenderable.startTimestamp()).thenReturn(START);
        when(mockRenderable.pausedTimestamp()).thenReturn(PAUSE);

        var output = handler.write(mockRenderable);

        assertEquals(WRITTEN_VALUE, output);
        verify(mockRenderable, once()).animationId();
        verify(mockRenderable, once()).getBorderThicknessProvider();
        verify(mockProviderHandler, once()).write(mockBorderThicknessProvider);
        verify(mockRenderable, once()).getBorderColorProvider();
        verify(mockProviderHandler, once()).write(mockBorderColorProvider);
        verify(mockRenderable, once()).pressActionIds();
        verify(mockRenderable, once()).releaseActionIds();
        verify(mockRenderable, once()).mouseOverActionId();
        verify(mockRenderable, once()).mouseLeaveActionId();
        verify(mockRenderable, once()).colorShifts();
        verify(mockShiftHandler, once()).write(mockShift);
        verify(mockRenderable, once()).getRenderingDimensionsProvider();
        verify(mockProviderHandler, once()).write(mockAreaProvider);
        verify(mockRenderable, once()).getZ();
        verify(mockRenderable, once()).uuid();
        verify(mockRenderable, once()).startTimestamp();
        verify(mockRenderable, once()).pausedTimestamp();
    }

    @Test
    public void testWriteWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> handler.write(null));
    }

    @Test
    public void testRead() {
        when(mockFactory.make(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                anyInt(),
                any(),
                any(),
                anyLong(),
                any(),
                any()
        )).thenReturn(mockRenderable);

        var output = handler.read(WRITTEN_VALUE);

        assertSame(mockRenderable, output);
        verify(MOCK_GET_ANIMATION, once()).apply(ANIMATION_ID);
        verify(mockProviderHandler, once()).read(BORDER_THICKNESS);
        verify(mockProviderHandler, once()).read(BORDER_COLOR);
        verify(MOCK_GET_ACTION, once()).apply(ON_PRESS_ACTION_ID);
        verify(MOCK_GET_ACTION, once()).apply(ON_RELEASE_ACTION_ID);
        verify(MOCK_GET_ACTION, once()).apply(ON_MOUSE_OVER_ACTION_ID);
        verify(MOCK_GET_ACTION, once()).apply(ON_MOUSE_LEAVE_ACTION_ID);
        verify(mockShiftHandler, once()).read(COLOR_SHIFT);
        verify(mockProviderHandler, once()).read(AREA);
        verify(mockFactory, once()).make(
                same(MOCK_ANIMATION),
                same(mockBorderThicknessProvider),
                same(mockBorderColorProvider),
                eq(mapOf(pairOf(ON_PRESS_BUTTON, MOCK_ON_PRESS_ACTION))),
                eq(mapOf(pairOf(ON_RELEASE_BUTTON, MOCK_ON_RELEASE_ACTION))),
                same(MOCK_ON_MOUSE_OVER_ACTION),
                same(MOCK_ON_MOUSE_LEAVE_ACTION),
                eq(listOf(mockShift)),
                same(mockAreaProvider),
                eq(Z),
                eq(UUID),
                isNull(),
                eq(START),
                eq(PAUSE),
                isNull()
        );
    }

    @Test
    public void testReadWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> handler.read(null));
        assertThrows(IllegalArgumentException.class, () -> handler.read(""));
    }
}
