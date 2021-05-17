package inaugural.soliloquy.graphics.test.unit.renderables;

import inaugural.soliloquy.graphics.renderables.ImageAssetSetRenderableImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.assets.ImageAssetSet;
import soliloquy.specs.graphics.renderables.ImageAssetSetRenderable;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.util.ArrayList;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class ImageAssetSetRenderableImplTests {
    private final ImageAssetSet IMAGE_ASSET_SET = new FakeImageAssetSet();
    private final String TYPE = "type";
    private final String DIRECTION = "direction";
    /** @noinspection rawtypes*/
    private final FakeAction CLICK_ACTION = new FakeAction();
    /** @noinspection rawtypes*/
    private final FakeAction MOUSE_OVER_ACTION = new FakeAction();
    /** @noinspection rawtypes*/
    private final FakeAction MOUSE_LEAVE_ACTION = new FakeAction();
    private final ArrayList<ColorShift> COLOR_SHIFTS = new ArrayList<>();
    private final FakeProviderAtTime<FloatBox> RENDERING_AREA_PROVIDER =
            new FakeProviderAtTime<>();
    private final int Z = 123;
    private final FakeEntityUuid UUID = new FakeEntityUuid();
    private final Consumer<Renderable>
            IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_DELETE_CONSUMER =
            renderable ->
                    _imageAssetSetRenderableWithMouseEventsDeleteConsumerInput = renderable;
    private final Consumer<Renderable>
            IMAGE_ASSET_SET_RENDERABLE_WITHOUT_MOUSE_EVENTS_DELETE_CONSUMER =
            renderable ->
                    _imageAssetSetRenderableWithoutMouseEventsDeleteConsumerInput = renderable;

    private static Renderable _imageAssetSetRenderableWithMouseEventsDeleteConsumerInput;
    private static Renderable _imageAssetSetRenderableWithoutMouseEventsDeleteConsumerInput;

    private ImageAssetSetRenderable _imageAssetSetRenderableWithMouseEvents;
    private ImageAssetSetRenderable _imageAssetSetRenderableWithoutMouseEvents;

    @BeforeEach
    void setUp() {
        _imageAssetSetRenderableWithMouseEvents = new ImageAssetSetRenderableImpl(IMAGE_ASSET_SET,
                TYPE, DIRECTION, CLICK_ACTION, MOUSE_OVER_ACTION, MOUSE_LEAVE_ACTION, COLOR_SHIFTS,
                RENDERING_AREA_PROVIDER, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_DELETE_CONSUMER);

        _imageAssetSetRenderableWithoutMouseEvents = new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET, TYPE, DIRECTION, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITHOUT_MOUSE_EVENTS_DELETE_CONSUMER);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                null, TYPE, DIRECTION, CLICK_ACTION, MOUSE_OVER_ACTION,
                MOUSE_LEAVE_ACTION, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET, null, null, CLICK_ACTION, MOUSE_OVER_ACTION,
                MOUSE_LEAVE_ACTION, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET, null, "", CLICK_ACTION, MOUSE_OVER_ACTION,
                MOUSE_LEAVE_ACTION, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET, "", null, CLICK_ACTION, MOUSE_OVER_ACTION,
                MOUSE_LEAVE_ACTION, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET, "", "", CLICK_ACTION, MOUSE_OVER_ACTION,
                MOUSE_LEAVE_ACTION, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET, TYPE, DIRECTION, CLICK_ACTION, MOUSE_OVER_ACTION,
                MOUSE_LEAVE_ACTION, null, RENDERING_AREA_PROVIDER, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET, TYPE, DIRECTION, CLICK_ACTION, MOUSE_OVER_ACTION,
                MOUSE_LEAVE_ACTION, COLOR_SHIFTS, null, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET, TYPE, DIRECTION, CLICK_ACTION, MOUSE_OVER_ACTION,
                MOUSE_LEAVE_ACTION, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, null,
                IMAGE_ASSET_SET_RENDERABLE_WITH_MOUSE_EVENTS_DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET, TYPE, DIRECTION, CLICK_ACTION, MOUSE_OVER_ACTION,
                MOUSE_LEAVE_ACTION, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                null
        ));

        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                null, TYPE, DIRECTION, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITHOUT_MOUSE_EVENTS_DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET, null, null, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITHOUT_MOUSE_EVENTS_DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET, null, "", COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITHOUT_MOUSE_EVENTS_DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET, "", null, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITHOUT_MOUSE_EVENTS_DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET, "", "", COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITHOUT_MOUSE_EVENTS_DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET, TYPE, DIRECTION, null, RENDERING_AREA_PROVIDER, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITHOUT_MOUSE_EVENTS_DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET, TYPE, DIRECTION, COLOR_SHIFTS, null, Z, UUID,
                IMAGE_ASSET_SET_RENDERABLE_WITHOUT_MOUSE_EVENTS_DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET, TYPE, DIRECTION, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, null,
                IMAGE_ASSET_SET_RENDERABLE_WITHOUT_MOUSE_EVENTS_DELETE_CONSUMER
        ));
        assertThrows(IllegalArgumentException.class, () -> new ImageAssetSetRenderableImpl(
                IMAGE_ASSET_SET, TYPE, DIRECTION, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                null
        ));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(ImageAssetSetRenderable.class.getCanonicalName(),
                _imageAssetSetRenderableWithMouseEvents.getInterfaceName());
        assertEquals(ImageAssetSetRenderable.class.getCanonicalName(),
                _imageAssetSetRenderableWithoutMouseEvents.getInterfaceName());
    }

    @Test
    void testImageAssetSet() {
        assertSame(IMAGE_ASSET_SET, _imageAssetSetRenderableWithMouseEvents.imageAssetSet());
        assertSame(IMAGE_ASSET_SET, _imageAssetSetRenderableWithoutMouseEvents.imageAssetSet());
    }

    @Test
    void testType() {
        assertEquals(TYPE, _imageAssetSetRenderableWithMouseEvents.type());
        assertEquals(TYPE, _imageAssetSetRenderableWithoutMouseEvents.type());
    }

    @Test
    void testDirection() {
        assertEquals(DIRECTION, _imageAssetSetRenderableWithMouseEvents.direction());
        assertEquals(DIRECTION, _imageAssetSetRenderableWithoutMouseEvents.direction());
    }

    @Test
    void testCapturesMouseEvents() {
        assertTrue(_imageAssetSetRenderableWithMouseEvents.capturesMouseEvents());
        assertFalse(_imageAssetSetRenderableWithoutMouseEvents.capturesMouseEvents());
    }

    @Test
    void testClick() {
        assertThrows(UnsupportedOperationException.class,
                _imageAssetSetRenderableWithoutMouseEvents::click);

        _imageAssetSetRenderableWithMouseEvents.click();
        assertEquals(1, CLICK_ACTION.NumberOfTimesCalled);
        assertEquals(1, CLICK_ACTION.Inputs.size());
        assertNull(CLICK_ACTION.Inputs.get(0));
    }

    @Test
    void testMouseOver() {
        assertThrows(UnsupportedOperationException.class,
                _imageAssetSetRenderableWithoutMouseEvents::mouseOver);

        _imageAssetSetRenderableWithMouseEvents.mouseOver();
        assertEquals(1, MOUSE_OVER_ACTION.NumberOfTimesCalled);
        assertEquals(1, MOUSE_OVER_ACTION.Inputs.size());
        assertNull(MOUSE_OVER_ACTION.Inputs.get(0));
    }

    @Test
    void testMouseLeave() {
        assertThrows(UnsupportedOperationException.class,
                _imageAssetSetRenderableWithoutMouseEvents::mouseLeave);

        _imageAssetSetRenderableWithMouseEvents.mouseLeave();
        assertEquals(1, MOUSE_LEAVE_ACTION.NumberOfTimesCalled);
        assertEquals(1, MOUSE_LEAVE_ACTION.Inputs.size());
        assertNull(MOUSE_LEAVE_ACTION.Inputs.get(0));
    }

    @Test
    void testColorShifts() {
        assertSame(COLOR_SHIFTS, _imageAssetSetRenderableWithMouseEvents.colorShifts());
        assertSame(COLOR_SHIFTS, _imageAssetSetRenderableWithoutMouseEvents.colorShifts());
    }

    @Test
    void testRenderingAreaProvider() {
        assertSame(RENDERING_AREA_PROVIDER,
                _imageAssetSetRenderableWithMouseEvents.renderingAreaProvider());
        assertSame(RENDERING_AREA_PROVIDER,
                _imageAssetSetRenderableWithoutMouseEvents.renderingAreaProvider());
    }

    @Test
    void testZ() {
        assertSame(Z, _imageAssetSetRenderableWithMouseEvents.z());
        assertSame(Z, _imageAssetSetRenderableWithoutMouseEvents.z());
    }

    @Test
    void testDelete() {
        _imageAssetSetRenderableWithMouseEvents.delete();
        assertSame(_imageAssetSetRenderableWithMouseEventsDeleteConsumerInput,
                _imageAssetSetRenderableWithMouseEvents);

        _imageAssetSetRenderableWithoutMouseEvents.delete();
        assertSame(_imageAssetSetRenderableWithoutMouseEventsDeleteConsumerInput,
                _imageAssetSetRenderableWithoutMouseEvents);
    }

    @Test
    void testUuid() {
        assertSame(UUID, _imageAssetSetRenderableWithMouseEvents.uuid());
        assertSame(UUID, _imageAssetSetRenderableWithoutMouseEvents.uuid());
    }
}
