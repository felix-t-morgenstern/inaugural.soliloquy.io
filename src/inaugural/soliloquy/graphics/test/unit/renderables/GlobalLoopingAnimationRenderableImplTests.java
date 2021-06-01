package inaugural.soliloquy.graphics.test.unit.renderables;

import inaugural.soliloquy.graphics.renderables.GlobalLoopingAnimationRenderableImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeAction;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeEntityUuid;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeGlobalLoopingAnimation;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeProviderAtTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.renderables.GlobalLoopingAnimationRenderable;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.ArrayList;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class GlobalLoopingAnimationRenderableImplTests {
    private final FakeGlobalLoopingAnimation GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS =
            new FakeGlobalLoopingAnimation(true);
    private final FakeGlobalLoopingAnimation GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS =
            new FakeGlobalLoopingAnimation(false);
    private final FakeProviderAtTime<Float> BORDER_THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Color> BORDER_COLOR_PROVIDER = new FakeProviderAtTime<>();
    /** @noinspection rawtypes*/
    private final FakeAction ON_CLICK = new FakeAction();
    /** @noinspection rawtypes*/
    private final FakeAction ON_MOUSE_OVER = new FakeAction();
    /** @noinspection rawtypes*/
    private final FakeAction ON_MOUSE_LEAVE = new FakeAction();
    private final ArrayList<ColorShift> COLOR_SHIFTS = new ArrayList<>();
    private final FakeProviderAtTime<FloatBox> RENDERING_AREA_PROVIDER =
            new FakeProviderAtTime<>();
    private final int Z = 123;
    private final FakeEntityUuid UUID = new FakeEntityUuid();
    private final Consumer<Renderable>
            GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER =
            renderable ->
                    _globalLoopingAnimationRenderableWithMouseEventsUpdateZIndexInContainerInput =
                            renderable;
    private final Consumer<Renderable>
            GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER =
            renderable ->
                    _globalLoopingAnimationRenderableWithoutMouseEventsUpdateZIndexInContainerInput =
                            renderable;
    private final Consumer<Renderable>
            GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER =
            renderable ->
                    _globalLoopingAnimationRenderableWithMouseEventsRemoveFromContainerInput =
                            renderable;
    private final Consumer<Renderable>
            GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER =
            renderable ->
                    _globalLoopingAnimationRenderableWithoutMouseEventsRemoveFromContainerInput =
                            renderable;

    private static Renderable
            _globalLoopingAnimationRenderableWithMouseEventsRemoveFromContainerInput;
    private static Renderable
            _globalLoopingAnimationRenderableWithoutMouseEventsRemoveFromContainerInput;
    private static Renderable
            _globalLoopingAnimationRenderableWithMouseEventsUpdateZIndexInContainerInput;
    private static Renderable
            _globalLoopingAnimationRenderableWithoutMouseEventsUpdateZIndexInContainerInput;

    private GlobalLoopingAnimationRenderable _globalLoopingAnimationRenderableWithMouseEvents;
    private GlobalLoopingAnimationRenderable _globalLoopingAnimationRenderableWithoutMouseEvents;

    @BeforeEach
    void setUp() {
        _globalLoopingAnimationRenderableWithMouseEvents = new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_CLICK, ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER);
        _globalLoopingAnimationRenderableWithoutMouseEvents = new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER);
    }

    @Test
    void testConstructorWithInvalidParameters() {
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                null, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_CLICK, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFTS,
                RENDERING_AREA_PROVIDER, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_CLICK, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFTS,
                RENDERING_AREA_PROVIDER, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        ));
        // NB: The following two constructors should _not_ throw exceptions
        new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, null,
                BORDER_COLOR_PROVIDER, ON_CLICK, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFTS,
                RENDERING_AREA_PROVIDER, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        );
        new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, null,
                null, ON_CLICK, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFTS,
                RENDERING_AREA_PROVIDER, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        );
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                null, ON_CLICK, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFTS,
                RENDERING_AREA_PROVIDER, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_CLICK, ON_MOUSE_OVER, ON_MOUSE_LEAVE, null,
                RENDERING_AREA_PROVIDER, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_CLICK, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFTS,
                null, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_CLICK, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFTS,
                RENDERING_AREA_PROVIDER, Z, null,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_CLICK, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFTS,
                RENDERING_AREA_PROVIDER, Z, UUID,
                null,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, ON_CLICK, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFTS,
                RENDERING_AREA_PROVIDER, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITH_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                null
        ));

        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                null, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        ));
        // NB: The following two constructors should _not_ throw exceptions
        new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, null,
                BORDER_COLOR_PROVIDER, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        );
        new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, null,
                null, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        );
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                null, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, null, RENDERING_AREA_PROVIDER, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFTS, null, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, null,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_UPDATE_Z_INDEX_IN_CONTAINER,
                null
        ));
        assertThrows(IllegalArgumentException.class, () -> new GlobalLoopingAnimationRenderableImpl(
                GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                null,
                GLOBAL_LOOPING_ANIMATION_RENDERABLE_WITHOUT_MOUSE_EVENTS_REMOVE_FROM_CONTAINER
        ));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(GlobalLoopingAnimationRenderable.class.getCanonicalName(),
                _globalLoopingAnimationRenderableWithMouseEvents.getInterfaceName());
    }

    @Test
    void testGetAndSetGlobalLoopingAnimation() {
        assertSame(GLOBAL_LOOPING_ANIMATION_SUPPORTING_MOUSE_EVENTS,
                _globalLoopingAnimationRenderableWithMouseEvents.getGlobalLoopingAnimation());
        assertSame(GLOBAL_LOOPING_ANIMATION_NOT_SUPPORTING_MOUSE_EVENTS,
                _globalLoopingAnimationRenderableWithoutMouseEvents.getGlobalLoopingAnimation());

        FakeGlobalLoopingAnimation newGlobalLoopingAnimation =
                new FakeGlobalLoopingAnimation(true);

        _globalLoopingAnimationRenderableWithMouseEvents
                .setGlobalLoopingAnimation(newGlobalLoopingAnimation);
        _globalLoopingAnimationRenderableWithoutMouseEvents
                .setGlobalLoopingAnimation(newGlobalLoopingAnimation);

        assertSame(newGlobalLoopingAnimation,
                _globalLoopingAnimationRenderableWithMouseEvents.getGlobalLoopingAnimation());
        assertSame(newGlobalLoopingAnimation,
                _globalLoopingAnimationRenderableWithoutMouseEvents.getGlobalLoopingAnimation());
    }

    @Test
    void testSetGlobalLoopingAnimationWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.setGlobalLoopingAnimation(null));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithoutMouseEvents.setGlobalLoopingAnimation(null));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.setGlobalLoopingAnimation(
                        new FakeGlobalLoopingAnimation(false)));
    }

    @Test
    void testGetAndSetBorderThicknessProvider() {
        assertSame(BORDER_THICKNESS_PROVIDER,
                _globalLoopingAnimationRenderableWithMouseEvents.getBorderThicknessProvider());
        assertSame(BORDER_THICKNESS_PROVIDER,
                _globalLoopingAnimationRenderableWithoutMouseEvents.getBorderThicknessProvider());

        FakeProviderAtTime<Float> newBorderThicknessProvider = new FakeProviderAtTime<>();

        _globalLoopingAnimationRenderableWithMouseEvents.setBorderThicknessProvider(newBorderThicknessProvider);
        _globalLoopingAnimationRenderableWithoutMouseEvents.setBorderThicknessProvider(newBorderThicknessProvider);

        assertSame(newBorderThicknessProvider,
                _globalLoopingAnimationRenderableWithMouseEvents.getBorderThicknessProvider());
        assertSame(newBorderThicknessProvider,
                _globalLoopingAnimationRenderableWithoutMouseEvents.getBorderThicknessProvider());
    }

    @Test
    void testSetBorderThicknessProviderWithInvalidParams() {
        _globalLoopingAnimationRenderableWithMouseEvents.setBorderThicknessProvider(null);
        _globalLoopingAnimationRenderableWithoutMouseEvents.setBorderThicknessProvider(null);
        _globalLoopingAnimationRenderableWithMouseEvents.setBorderColorProvider(null);
        _globalLoopingAnimationRenderableWithoutMouseEvents.setBorderColorProvider(null);

        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents
                        .setBorderThicknessProvider(BORDER_THICKNESS_PROVIDER));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithoutMouseEvents
                        .setBorderThicknessProvider(BORDER_THICKNESS_PROVIDER));
    }

    @Test
    void testGetAndSetBorderColorProvider() {
        assertSame(BORDER_COLOR_PROVIDER,
                _globalLoopingAnimationRenderableWithMouseEvents.getBorderColorProvider());
        assertSame(BORDER_COLOR_PROVIDER,
                _globalLoopingAnimationRenderableWithoutMouseEvents.getBorderColorProvider());

        FakeProviderAtTime<Color> newBorderColorProvider = new FakeProviderAtTime<>();

        _globalLoopingAnimationRenderableWithMouseEvents.setBorderColorProvider(newBorderColorProvider);
        _globalLoopingAnimationRenderableWithoutMouseEvents.setBorderColorProvider(newBorderColorProvider);

        assertSame(newBorderColorProvider,
                _globalLoopingAnimationRenderableWithMouseEvents.getBorderColorProvider());
        assertSame(newBorderColorProvider,
                _globalLoopingAnimationRenderableWithoutMouseEvents.getBorderColorProvider());
    }

    @Test
    void testSetBorderColorProviderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithMouseEvents.setBorderColorProvider(null));
        assertThrows(IllegalArgumentException.class, () ->
                _globalLoopingAnimationRenderableWithoutMouseEvents.setBorderColorProvider(null));
    }

    @Test
    void testGetAndSetCapturesMouseEvents() {
        assertTrue(_globalLoopingAnimationRenderableWithMouseEvents.getCapturesMouseEvents());
        assertFalse(_globalLoopingAnimationRenderableWithoutMouseEvents.getCapturesMouseEvents());

        _globalLoopingAnimationRenderableWithMouseEvents.setCapturesMouseEvents(false);
        assertThrows(UnsupportedOperationException.class, () ->
                _globalLoopingAnimationRenderableWithoutMouseEvents.setCapturesMouseEvents(false));

        assertFalse(_globalLoopingAnimationRenderableWithMouseEvents.getCapturesMouseEvents());
    }

    @Test
    void testClickAndSetOnClick() {
        assertThrows(UnsupportedOperationException.class,
                _globalLoopingAnimationRenderableWithoutMouseEvents::click);
        assertThrows(UnsupportedOperationException.class, () ->
                _globalLoopingAnimationRenderableWithoutMouseEvents.setOnClick(ON_CLICK));

        _globalLoopingAnimationRenderableWithMouseEvents.click();
        assertEquals(1, ON_CLICK.NumberOfTimesCalled);
        assertEquals(1, ON_CLICK.Inputs.size());
        assertNull(ON_CLICK.Inputs.get(0));

        //noinspection rawtypes
        FakeAction newOnClick = new FakeAction();
        _globalLoopingAnimationRenderableWithMouseEvents.setOnClick(newOnClick);

        _globalLoopingAnimationRenderableWithMouseEvents.click();
        assertEquals(1, newOnClick.NumberOfTimesCalled);
        assertEquals(1, newOnClick.Inputs.size());
        assertNull(newOnClick.Inputs.get(0));
    }

    @Test
    void testMouseOverAndSetOnMouseOver() {
        assertThrows(UnsupportedOperationException.class,
                _globalLoopingAnimationRenderableWithoutMouseEvents::mouseOver);
        assertThrows(UnsupportedOperationException.class, () ->
                _globalLoopingAnimationRenderableWithoutMouseEvents.setOnMouseOver(ON_MOUSE_OVER));

        _globalLoopingAnimationRenderableWithMouseEvents.mouseOver();
        assertEquals(1, ON_MOUSE_OVER.NumberOfTimesCalled);
        assertEquals(1, ON_MOUSE_OVER.Inputs.size());
        assertNull(ON_MOUSE_OVER.Inputs.get(0));

        //noinspection rawtypes
        FakeAction newOnMouseOver = new FakeAction();
        _globalLoopingAnimationRenderableWithMouseEvents.setOnMouseOver(newOnMouseOver);

        _globalLoopingAnimationRenderableWithMouseEvents.mouseOver();
        assertEquals(1, newOnMouseOver.NumberOfTimesCalled);
        assertEquals(1, newOnMouseOver.Inputs.size());
        assertNull(newOnMouseOver.Inputs.get(0));
    }

    @Test
    void testMouseLeaveAndSetOnMouseLeave() {
        assertThrows(UnsupportedOperationException.class,
                _globalLoopingAnimationRenderableWithoutMouseEvents::mouseLeave);
        assertThrows(UnsupportedOperationException.class, () ->
                _globalLoopingAnimationRenderableWithoutMouseEvents.setOnMouseLeave(ON_MOUSE_LEAVE));

        _globalLoopingAnimationRenderableWithMouseEvents.mouseLeave();
        assertEquals(1, ON_MOUSE_LEAVE.NumberOfTimesCalled);
        assertEquals(1, ON_MOUSE_LEAVE.Inputs.size());
        assertNull(ON_MOUSE_LEAVE.Inputs.get(0));

        //noinspection rawtypes
        FakeAction newOnMouseLeave = new FakeAction();
        _globalLoopingAnimationRenderableWithMouseEvents.setOnMouseLeave(newOnMouseLeave);

        _globalLoopingAnimationRenderableWithMouseEvents.mouseLeave();
        assertEquals(1, newOnMouseLeave.NumberOfTimesCalled);
        assertEquals(1, newOnMouseLeave.Inputs.size());
        assertNull(newOnMouseLeave.Inputs.get(0));
    }

    @Test
    void testColorShifts() {
        assertSame(COLOR_SHIFTS, _globalLoopingAnimationRenderableWithMouseEvents.colorShifts());
        assertSame(COLOR_SHIFTS, _globalLoopingAnimationRenderableWithoutMouseEvents.colorShifts());
    }

    @Test
    void testGetAndSetRenderingAreaProvider() {
        assertSame(RENDERING_AREA_PROVIDER,
                _globalLoopingAnimationRenderableWithMouseEvents.getRenderingDimensionsProvider());
        assertSame(RENDERING_AREA_PROVIDER,
                _globalLoopingAnimationRenderableWithoutMouseEvents.getRenderingDimensionsProvider());

        FakeProviderAtTime<FloatBox> newRenderingDimensionsProvider = new FakeProviderAtTime<>();

        _globalLoopingAnimationRenderableWithMouseEvents
                .setRenderingDimensionsProvider(newRenderingDimensionsProvider);
        _globalLoopingAnimationRenderableWithoutMouseEvents
                .setRenderingDimensionsProvider(newRenderingDimensionsProvider);

        assertSame(newRenderingDimensionsProvider,
                _globalLoopingAnimationRenderableWithMouseEvents.getRenderingDimensionsProvider());
        assertSame(newRenderingDimensionsProvider,
                _globalLoopingAnimationRenderableWithoutMouseEvents.getRenderingDimensionsProvider());
    }

    @Test
    void testGetAndSetZ() {
        assertSame(Z, _globalLoopingAnimationRenderableWithMouseEvents.getZ());
        assertSame(Z, _globalLoopingAnimationRenderableWithoutMouseEvents.getZ());

        int newZ = 456;

        _globalLoopingAnimationRenderableWithMouseEvents.setZ(newZ);
        _globalLoopingAnimationRenderableWithoutMouseEvents.setZ(newZ);

        assertEquals(newZ, _globalLoopingAnimationRenderableWithMouseEvents.getZ());
        assertEquals(newZ, _globalLoopingAnimationRenderableWithoutMouseEvents.getZ());

        assertSame(_globalLoopingAnimationRenderableWithMouseEvents,
                _globalLoopingAnimationRenderableWithMouseEventsUpdateZIndexInContainerInput);
        assertSame(_globalLoopingAnimationRenderableWithoutMouseEvents,
                _globalLoopingAnimationRenderableWithoutMouseEventsUpdateZIndexInContainerInput);
    }

    @Test
    void testDelete() {
        _globalLoopingAnimationRenderableWithMouseEvents.delete();
        assertSame(_globalLoopingAnimationRenderableWithMouseEvents,
                _globalLoopingAnimationRenderableWithMouseEventsRemoveFromContainerInput);

        _globalLoopingAnimationRenderableWithoutMouseEvents.delete();
        assertSame(_globalLoopingAnimationRenderableWithoutMouseEvents,
                _globalLoopingAnimationRenderableWithoutMouseEventsRemoveFromContainerInput);
    }

    @Test
    void testUuid() {
        assertSame(UUID, _globalLoopingAnimationRenderableWithMouseEvents.uuid());
        assertSame(UUID, _globalLoopingAnimationRenderableWithoutMouseEvents.uuid());
    }
}
