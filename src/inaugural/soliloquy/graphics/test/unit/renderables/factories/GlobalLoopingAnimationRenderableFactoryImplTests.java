package inaugural.soliloquy.graphics.test.unit.renderables.factories;

import inaugural.soliloquy.graphics.renderables.GlobalLoopingAnimationRenderableImpl;
import inaugural.soliloquy.graphics.renderables.factories.GlobalLoopingAnimationRenderableFactoryImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeAction;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeEntityUuid;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeGlobalLoopingAnimation;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeProviderAtTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.graphics.renderables.GlobalLoopingAnimationRenderable;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.factories.GlobalLoopingAnimationRenderableFactory;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class GlobalLoopingAnimationRenderableFactoryImplTests {
    private final FakeGlobalLoopingAnimation GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS =
            new FakeGlobalLoopingAnimation(true);
    private final FakeGlobalLoopingAnimation GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS =
            new FakeGlobalLoopingAnimation(false);
    private final FakeProviderAtTime<Float> BORDER_THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Color> BORDER_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final FakeAction<Long> ON_PRESS_ACTION = new FakeAction<>();
    private final HashMap<Integer, Action<Long>> ON_PRESS_ACTIONS = new HashMap<>();
    private final FakeAction<Long> ON_MOUSE_OVER = new FakeAction<>();
    private final FakeAction<Long> ON_MOUSE_LEAVE = new FakeAction<>();
    private final ArrayList<ColorShift> COLOR_SHIFTS = new ArrayList<>();
    private final FakeProviderAtTime<FloatBox> RENDERING_AREA_PROVIDER =
            new FakeProviderAtTime<>();
    private final int Z = 123;
    private final FakeEntityUuid UUID = new FakeEntityUuid();
    private final Consumer<Renderable>
            GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER =
            renderable -> {};
    private final Consumer<Renderable>
            GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER =
            renderable -> {};
    private final Consumer<Renderable>
            GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER =
            renderable -> {};
    private final Consumer<Renderable>
            GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER =
            renderable -> {};
    
    private GlobalLoopingAnimationRenderableFactory _globalLoopingAnimationRenderableFactory;

    @BeforeEach
    void setUp() {
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
                        GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, null,
                        BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                        ON_MOUSE_LEAVE, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                        GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                        GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
                );

        GlobalLoopingAnimationRenderable globalLoopingAnimationRenderableNotSupportingMouseEvents =
                _globalLoopingAnimationRenderableFactory.make(
                        GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, null,
                        BORDER_COLOR_PROVIDER, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                        GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                        GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
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
                ON_MOUSE_LEAVE, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        ));
        // NB: The following two constructors should _not_ throw exceptions
        _globalLoopingAnimationRenderableFactory.make(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, null,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        );
        _globalLoopingAnimationRenderableFactory.make(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, null,
                null, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        );
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                null, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, null, RENDERING_AREA_PROVIDER, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFTS, null, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, null,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                null,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_PRESS_ACTIONS, null, ON_MOUSE_OVER,
                ON_MOUSE_LEAVE, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                null
        ));

        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                null, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        ));
        // NB: The following two constructors should _not_ throw exceptions
        _globalLoopingAnimationRenderableFactory.make(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, null,
                BORDER_COLOR_PROVIDER, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        );
        _globalLoopingAnimationRenderableFactory.make(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, null,
                null, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        );
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                null, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, null, RENDERING_AREA_PROVIDER, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFTS, null, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, null,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                null
        ));
        assertThrows(IllegalArgumentException.class, () -> _globalLoopingAnimationRenderableFactory
                .make(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                null,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        ));
    }
}
