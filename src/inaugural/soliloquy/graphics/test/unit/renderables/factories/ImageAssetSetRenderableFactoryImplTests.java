package inaugural.soliloquy.graphics.test.unit.renderables.factories;

import inaugural.soliloquy.graphics.renderables.ImageAssetSetRenderableImpl;
import inaugural.soliloquy.graphics.renderables.factories.ImageAssetSetRenderableFactoryImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeAction;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeEntityUuid;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeImageAssetSet;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeProviderAtTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.assets.ImageAssetSet;
import soliloquy.specs.graphics.renderables.ImageAssetSetRenderable;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.factories.ImageAssetSetRenderableFactory;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.util.ArrayList;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class ImageAssetSetRenderableFactoryImplTests {
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
    private final Consumer<Renderable> REMOVE_FROM_CONTAINER = renderable -> {};

    private ImageAssetSetRenderableFactory _imageAssetSetRenderableFactory;

    @BeforeEach
    void setUp() {
        _imageAssetSetRenderableFactory = new ImageAssetSetRenderableFactoryImpl();
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(ImageAssetSetRenderableFactory.class.getCanonicalName(),
                _imageAssetSetRenderableFactory.getInterfaceName());
    }

    @Test
    void testMakeWithMouseEventCapturing() {
        ImageAssetSetRenderable imageAssetSetRenderableWithMouseEvents =
                _imageAssetSetRenderableFactory.make(IMAGE_ASSET_SET, TYPE, DIRECTION,
                        CLICK_ACTION, MOUSE_OVER_ACTION, MOUSE_LEAVE_ACTION, COLOR_SHIFTS,
                        RENDERING_AREA_PROVIDER, Z, UUID, REMOVE_FROM_CONTAINER);

        assertNotNull(imageAssetSetRenderableWithMouseEvents);
        assertTrue(imageAssetSetRenderableWithMouseEvents instanceof ImageAssetSetRenderableImpl);
        assertTrue(imageAssetSetRenderableWithMouseEvents.capturesMouseEvents());

        ImageAssetSetRenderable imageAssetSetRenderableWithoutMouseEvents =
                _imageAssetSetRenderableFactory.make(IMAGE_ASSET_SET, TYPE, DIRECTION,
                        COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID, REMOVE_FROM_CONTAINER);

        assertNotNull(imageAssetSetRenderableWithoutMouseEvents);
        assertTrue(imageAssetSetRenderableWithoutMouseEvents
                instanceof ImageAssetSetRenderableImpl);
        assertFalse(imageAssetSetRenderableWithoutMouseEvents.capturesMouseEvents());
    }

    @Test
    void testMakeWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                null, TYPE, DIRECTION, CLICK_ACTION, MOUSE_OVER_ACTION,
                MOUSE_LEAVE_ACTION, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET, null, null, CLICK_ACTION, MOUSE_OVER_ACTION,
                MOUSE_LEAVE_ACTION, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET, null, "", CLICK_ACTION, MOUSE_OVER_ACTION,
                MOUSE_LEAVE_ACTION, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET, "", null, CLICK_ACTION, MOUSE_OVER_ACTION,
                MOUSE_LEAVE_ACTION, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET, "", "", CLICK_ACTION, MOUSE_OVER_ACTION,
                MOUSE_LEAVE_ACTION, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET, TYPE, DIRECTION, CLICK_ACTION, MOUSE_OVER_ACTION,
                MOUSE_LEAVE_ACTION, null, RENDERING_AREA_PROVIDER, Z, UUID,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET, TYPE, DIRECTION, CLICK_ACTION, MOUSE_OVER_ACTION,
                MOUSE_LEAVE_ACTION, COLOR_SHIFTS, null, Z, UUID,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET, TYPE, DIRECTION, CLICK_ACTION, MOUSE_OVER_ACTION,
                MOUSE_LEAVE_ACTION, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, null,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET, TYPE, DIRECTION, CLICK_ACTION, MOUSE_OVER_ACTION,
                MOUSE_LEAVE_ACTION, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                null
        ));

        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                null, TYPE, DIRECTION, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET, null, null, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET, null, "", COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET, "", null, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET, "", "", COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET, TYPE, DIRECTION, null, RENDERING_AREA_PROVIDER, Z, UUID,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET, TYPE, DIRECTION, COLOR_SHIFTS, null, Z, UUID,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET, TYPE, DIRECTION, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, null,
                REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET, TYPE, DIRECTION, COLOR_SHIFTS, RENDERING_AREA_PROVIDER, Z, UUID,
                null
        ));
    }
}
