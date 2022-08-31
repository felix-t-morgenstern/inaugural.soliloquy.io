package inaugural.soliloquy.graphics.test.unit.renderables.factories;

import inaugural.soliloquy.graphics.renderables.GlobalLoopingAnimationRenderableImpl;
import inaugural.soliloquy.graphics.renderables.factories.GlobalLoopingAnimationRenderableFactoryImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeAction;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeGlobalLoopingAnimation;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeProviderAtTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.graphics.renderables.GlobalLoopingAnimationRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.factories.GlobalLoopingAnimationRenderableFactory;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.randomInt;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GlobalLoopingAnimationRenderableFactoryImplTests {
    private final FakeGlobalLoopingAnimation GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS =
            new FakeGlobalLoopingAnimation(true);
    private final FakeGlobalLoopingAnimation GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS =
            new FakeGlobalLoopingAnimation(false);
    private final FakeProviderAtTime<Float> BORDER_THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Color> BORDER_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final HashMap<Integer, Action<Long>> ON_PRESS_ACTIONS = new HashMap<>();
    private final FakeAction<Long> ON_MOUSE_OVER = new FakeAction<>();
    private final FakeAction<Long> ON_MOUSE_LEAVE = new FakeAction<>();
    private final ArrayList<ProviderAtTime<ColorShift>> COLOR_SHIFT_PROVIDERS = new ArrayList<>();
    private final FakeProviderAtTime<FloatBox> RENDERING_AREA_PROVIDER =
            new FakeProviderAtTime<>();
    private final int Z = randomInt();

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private RenderableStack _mockContainingStack;

    private GlobalLoopingAnimationRenderableFactory _globalLoopingAnimationRenderableFactory;

    @BeforeEach
    void setUp() {
        _mockContainingStack = mock(RenderableStack.class);

        _globalLoopingAnimationRenderableFactory =
                new GlobalLoopingAnimationRenderableFactoryImpl();
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(GlobalLoopingAnimationRenderableFactory.class.getCanonicalName(),
                _globalLoopingAnimationRenderableFactory.getInterfaceName());
    }

    @Test
    void testMake() {
        GlobalLoopingAnimationRenderable globalLoopingAnimationRenderableSupportingMouseEvents =
                _globalLoopingAnimationRenderableFactory.make(
                        GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null,
                        ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS,
                        RENDERING_AREA_PROVIDER, Z, UUID, _mockContainingStack
                );

        GlobalLoopingAnimationRenderable globalLoopingAnimationRenderableNotSupportingMouseEvents =
                _globalLoopingAnimationRenderableFactory.make(
                        GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS,
                        RENDERING_AREA_PROVIDER, Z, UUID, _mockContainingStack
                );

        assertNotNull(globalLoopingAnimationRenderableSupportingMouseEvents);
        assertNotNull(globalLoopingAnimationRenderableNotSupportingMouseEvents);
        assertTrue(globalLoopingAnimationRenderableSupportingMouseEvents instanceof
                GlobalLoopingAnimationRenderableImpl);
        assertTrue(globalLoopingAnimationRenderableNotSupportingMouseEvents instanceof
                GlobalLoopingAnimationRenderableImpl);
    }

    @Test
    void testMakeWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                        null, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                        ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                        _mockContainingStack
                ));
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                        GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                        ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                        _mockContainingStack
                ));
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                        GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, null,
                        BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                        ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                        _mockContainingStack
                ));
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                        GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                        null, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                        ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                        _mockContainingStack
                ));
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                        GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                        null, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                        ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                        _mockContainingStack
                ));
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                        GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                        ON_MOUSE_LEAVE, null, RENDERING_AREA_PROVIDER, Z, UUID,
                        _mockContainingStack
                ));
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                        GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                        ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, null, Z, UUID,
                        _mockContainingStack
                ));
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                        GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                        ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, null,
                        _mockContainingStack
                ));
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                        GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                        ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                        null
                ));

        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                        null, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z,
                        UUID, _mockContainingStack
                ));
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                        GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, null,
                        BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z,
                        UUID, _mockContainingStack
                ));
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                        GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, null, COLOR_SHIFT_PROVIDERS,
                        RENDERING_AREA_PROVIDER, Z, UUID, _mockContainingStack
                ));
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                        GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, null, COLOR_SHIFT_PROVIDERS,
                        RENDERING_AREA_PROVIDER, Z, UUID, _mockContainingStack
                ));
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                        GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, null,
                        RENDERING_AREA_PROVIDER, Z, UUID, _mockContainingStack
                ));
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                        GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS,
                        null, Z, UUID, _mockContainingStack
                ));
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                        GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS,
                        RENDERING_AREA_PROVIDER, Z, null, _mockContainingStack
                ));
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                        GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS,
                        RENDERING_AREA_PROVIDER, Z, UUID, null
                ));
    }
}
