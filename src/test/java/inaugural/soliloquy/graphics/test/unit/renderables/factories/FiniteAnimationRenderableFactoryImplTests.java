package inaugural.soliloquy.graphics.test.unit.renderables.factories;

import inaugural.soliloquy.graphics.renderables.FiniteAnimationRenderableImpl;
import inaugural.soliloquy.graphics.renderables.factories.FiniteAnimationRenderableFactoryImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeAction;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeAnimation;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeProviderAtTime;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeStaticProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.graphics.renderables.FiniteAnimationRenderable;
import soliloquy.specs.graphics.renderables.RenderableWithMouseEvents.MouseEventInputs;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.factories.FiniteAnimationRenderableFactory;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.randomInt;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class FiniteAnimationRenderableFactoryImplTests {
    private final String ANIMATION_SUPPORTING_ID = "animationSupportingId";
    private final FakeAnimation ANIMATION_SUPPORTING_MOUSE_EVENTS =
            new FakeAnimation(ANIMATION_SUPPORTING_ID, true);
    private final String ANIMATION_NOT_SUPPORTING_ID = "animationNotSupportingId";
    private final FakeAnimation ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS =
            new FakeAnimation(ANIMATION_NOT_SUPPORTING_ID, false);
    private final FakeProviderAtTime<Float> BORDER_THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Color> BORDER_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final HashMap<Integer, Action<MouseEventInputs>> ON_PRESS_ACTIONS = new HashMap<>();
    private final FakeAction<MouseEventInputs> ON_MOUSE_OVER = new FakeAction<>();
    private final FakeAction<MouseEventInputs> ON_MOUSE_LEAVE = new FakeAction<>();
    private final ArrayList<ProviderAtTime<ColorShift>> COLOR_SHIFT_PROVIDERS = new ArrayList<>();
    private final FakeStaticProvider<FloatBox> RENDERING_AREA_PROVIDER =
            new FakeStaticProvider<>(null);
    private final int Z = randomInt();

    private final long START_TIMESTAMP = 111L;
    private final Long PAUSED_TIMESTAMP = -456L;
    private final Long MOST_RECENT_TIMESTAMP = -123L;

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private RenderableStack mockContainingStack;
    @Mock private RenderingBoundaries mockRenderingBoundaries;

    private FiniteAnimationRenderableFactory finiteAnimationRenderableFactory;

    @BeforeEach
    void setUp() {
        mockContainingStack = mock(RenderableStack.class);
        mockRenderingBoundaries = mock(RenderingBoundaries.class);

        finiteAnimationRenderableFactory =
                new FiniteAnimationRenderableFactoryImpl(mockRenderingBoundaries);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new FiniteAnimationRenderableFactoryImpl(null));
    }

    @Test
    void testMake() {
        FiniteAnimationRenderable finiteAnimationRenderableWithMouseEvents =
                finiteAnimationRenderableFactory.make(
                        ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                        ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingStack, START_TIMESTAMP, PAUSED_TIMESTAMP,
                        MOST_RECENT_TIMESTAMP
                );
        FiniteAnimationRenderable finiteAnimationRenderableWithoutMouseEvents =
                finiteAnimationRenderableFactory.make(
                        ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z,
                        UUID, mockContainingStack, START_TIMESTAMP, PAUSED_TIMESTAMP,
                        MOST_RECENT_TIMESTAMP
                );

        assertNotNull(finiteAnimationRenderableWithMouseEvents);
        assertNotNull(finiteAnimationRenderableWithoutMouseEvents);
        assertTrue(finiteAnimationRenderableWithMouseEvents
                instanceof FiniteAnimationRenderableImpl);
        assertTrue(finiteAnimationRenderableWithoutMouseEvents
                instanceof FiniteAnimationRenderableImpl);
    }

    @Test
    void testMakeWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> finiteAnimationRenderableFactory.make(
                null, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingStack, START_TIMESTAMP, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> finiteAnimationRenderableFactory.make(
                ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, null,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingStack, START_TIMESTAMP, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> finiteAnimationRenderableFactory.make(
                ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                null, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingStack, START_TIMESTAMP, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> finiteAnimationRenderableFactory.make(
                ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, null, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingStack, START_TIMESTAMP, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> finiteAnimationRenderableFactory.make(
                ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, null, Z, UUID,
                mockContainingStack, START_TIMESTAMP, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> finiteAnimationRenderableFactory.make(
                ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, null,
                mockContainingStack, START_TIMESTAMP, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> finiteAnimationRenderableFactory.make(
                ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                null, START_TIMESTAMP, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> finiteAnimationRenderableFactory.make(
                ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingStack, START_TIMESTAMP, PAUSED_TIMESTAMP, null
        ));
        assertThrows(IllegalArgumentException.class, () -> finiteAnimationRenderableFactory.make(
                ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingStack, START_TIMESTAMP, MOST_RECENT_TIMESTAMP + 1,
                MOST_RECENT_TIMESTAMP
        ));


        assertThrows(IllegalArgumentException.class, () -> finiteAnimationRenderableFactory.make(
                null, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingStack, START_TIMESTAMP, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> finiteAnimationRenderableFactory.make(
                ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, null,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingStack, START_TIMESTAMP, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> finiteAnimationRenderableFactory.make(
                ANIMATION_SUPPORTING_MOUSE_EVENTS, null,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingStack, START_TIMESTAMP, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> finiteAnimationRenderableFactory.make(
                ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                null, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingStack, START_TIMESTAMP, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> finiteAnimationRenderableFactory.make(
                ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, null, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingStack, START_TIMESTAMP, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> finiteAnimationRenderableFactory.make(
                ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, null, Z, UUID,
                mockContainingStack, START_TIMESTAMP, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> finiteAnimationRenderableFactory.make(
                ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, null,
                mockContainingStack, START_TIMESTAMP, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> finiteAnimationRenderableFactory.make(
                ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                null, START_TIMESTAMP, PAUSED_TIMESTAMP, MOST_RECENT_TIMESTAMP
        ));
        assertThrows(IllegalArgumentException.class, () -> finiteAnimationRenderableFactory.make(
                ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingStack, START_TIMESTAMP, PAUSED_TIMESTAMP, null
        ));
        assertThrows(IllegalArgumentException.class, () -> finiteAnimationRenderableFactory.make(
                ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                mockContainingStack, START_TIMESTAMP, MOST_RECENT_TIMESTAMP + 1,
                MOST_RECENT_TIMESTAMP
        ));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(FiniteAnimationRenderableFactory.class.getCanonicalName(),
                finiteAnimationRenderableFactory.getInterfaceName());
    }
}
