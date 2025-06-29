package inaugural.soliloquy.io.test.unit.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.GlobalLoopingAnimationRenderableImpl;
import inaugural.soliloquy.io.graphics.renderables.factories.GlobalLoopingAnimationRenderableFactoryImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeAction;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeGlobalLoopingAnimation;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeProviderAtTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.GlobalLoopingAnimationRenderable;
import soliloquy.specs.io.graphics.renderables.RenderableWithMouseEvents.MouseEventInputs;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.factories.GlobalLoopingAnimationRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderableStack;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomInt;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class GlobalLoopingAnimationRenderableFactoryImplTests {
    private final FakeGlobalLoopingAnimation GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS =
            new FakeGlobalLoopingAnimation(true);
    private final FakeGlobalLoopingAnimation GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS =
            new FakeGlobalLoopingAnimation(false);
    private final FakeProviderAtTime<Float> BORDER_THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Color> BORDER_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final Map<Integer, Action<MouseEventInputs>> ON_PRESS_ACTIONS = mapOf();
    private final FakeAction<MouseEventInputs> ON_MOUSE_OVER = new FakeAction<>();
    private final FakeAction<MouseEventInputs> ON_MOUSE_LEAVE = new FakeAction<>();
    private final List<ProviderAtTime<ColorShift>> COLOR_SHIFT_PROVIDERS = listOf();
    private final FakeProviderAtTime<FloatBox> RENDERING_AREA_PROVIDER =
            new FakeProviderAtTime<>();
    private final int Z = randomInt();

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private RenderableStack mockContainingStack;
    @Mock private RenderingBoundaries mockRenderingBoundaries;

    private GlobalLoopingAnimationRenderableFactory globalLoopingAnimationRenderableFactory;

    @BeforeEach
    public void setUp() {
        mockContainingStack = mock(RenderableStack.class);
        mockRenderingBoundaries = mock(RenderingBoundaries.class);

        globalLoopingAnimationRenderableFactory =
                new GlobalLoopingAnimationRenderableFactoryImpl(mockRenderingBoundaries);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new GlobalLoopingAnimationRenderableFactoryImpl(null));
    }

    @Test
    public void testMake() {
        GlobalLoopingAnimationRenderable globalLoopingAnimationRenderableSupportingMouseEvents =
                globalLoopingAnimationRenderableFactory.make(
                        GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null,
                        ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack
                );

        GlobalLoopingAnimationRenderable globalLoopingAnimationRenderableNotSupportingMouseEvents =
                globalLoopingAnimationRenderableFactory.make(
                        GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack
                );

        assertNotNull(globalLoopingAnimationRenderableSupportingMouseEvents);
        assertNotNull(globalLoopingAnimationRenderableNotSupportingMouseEvents);
        assertInstanceOf(GlobalLoopingAnimationRenderableImpl.class,
                globalLoopingAnimationRenderableSupportingMouseEvents);
        assertInstanceOf(GlobalLoopingAnimationRenderableImpl.class,
                globalLoopingAnimationRenderableNotSupportingMouseEvents);
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> globalLoopingAnimationRenderableFactory
                .make(
                        null, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                        ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingStack
                ));
        assertThrows(IllegalArgumentException.class, () -> globalLoopingAnimationRenderableFactory
                .make(
                        GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                        ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingStack
                ));
        assertThrows(IllegalArgumentException.class, () -> globalLoopingAnimationRenderableFactory
                .make(
                        GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, null,
                        BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                        ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingStack
                ));
        assertThrows(IllegalArgumentException.class, () -> globalLoopingAnimationRenderableFactory
                .make(
                        GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                        null, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                        ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingStack
                ));
        assertThrows(IllegalArgumentException.class, () -> globalLoopingAnimationRenderableFactory
                .make(
                        GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                        null, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                        ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingStack
                ));
        assertThrows(IllegalArgumentException.class, () -> globalLoopingAnimationRenderableFactory
                .make(
                        GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                        ON_MOUSE_LEAVE, null, RENDERING_AREA_PROVIDER, Z, UUID,
                        mockContainingStack
                ));
        assertThrows(IllegalArgumentException.class, () -> globalLoopingAnimationRenderableFactory
                .make(
                        GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                        ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, null, Z, UUID,
                        mockContainingStack
                ));
        assertThrows(IllegalArgumentException.class, () -> globalLoopingAnimationRenderableFactory
                .make(
                        GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                        ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, null,
                        mockContainingStack
                ));
        assertThrows(IllegalArgumentException.class, () -> globalLoopingAnimationRenderableFactory
                .make(
                        GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                        ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z, UUID,
                        null
                ));

        assertThrows(IllegalArgumentException.class, () -> globalLoopingAnimationRenderableFactory
                .make(
                        null, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z,
                        UUID, mockContainingStack
                ));
        assertThrows(IllegalArgumentException.class, () -> globalLoopingAnimationRenderableFactory
                .make(
                        GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, null,
                        BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS, RENDERING_AREA_PROVIDER, Z,
                        UUID, mockContainingStack
                ));
        assertThrows(IllegalArgumentException.class, () -> globalLoopingAnimationRenderableFactory
                .make(
                        GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, null, COLOR_SHIFT_PROVIDERS,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack
                ));
        assertThrows(IllegalArgumentException.class, () -> globalLoopingAnimationRenderableFactory
                .make(
                        GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, null, COLOR_SHIFT_PROVIDERS,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack
                ));
        assertThrows(IllegalArgumentException.class, () -> globalLoopingAnimationRenderableFactory
                .make(
                        GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, null,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingStack
                ));
        assertThrows(IllegalArgumentException.class, () -> globalLoopingAnimationRenderableFactory
                .make(
                        GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS,
                        null, Z, UUID, mockContainingStack
                ));
        assertThrows(IllegalArgumentException.class, () -> globalLoopingAnimationRenderableFactory
                .make(
                        GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS,
                        RENDERING_AREA_PROVIDER, Z, null, mockContainingStack
                ));
        assertThrows(IllegalArgumentException.class, () -> globalLoopingAnimationRenderableFactory
                .make(
                        GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, COLOR_SHIFT_PROVIDERS,
                        RENDERING_AREA_PROVIDER, Z, UUID, null
                ));
    }
}
