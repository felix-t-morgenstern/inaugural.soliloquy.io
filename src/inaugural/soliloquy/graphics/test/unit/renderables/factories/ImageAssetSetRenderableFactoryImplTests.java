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
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.ArrayList;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class ImageAssetSetRenderableFactoryImplTests {
    private final ImageAssetSet IMAGE_ASSET_SET_SUPPORTS_MOUSE_EVENTS =
            new FakeImageAssetSet(true);
    private final ImageAssetSet IMAGE_ASSET_SET_NOT_SUPPORTS_MOUSE_EVENTS =
            new FakeImageAssetSet(false);
    private final String TYPE = "type";
    private final String DIRECTION = "direction";
    private final FakeAction<Long> ON_MOUSE_OVER = new FakeAction<>();
    private final FakeAction<Long> ON_MOUSE_LEAVE = new FakeAction<>();
    private final ArrayList<ProviderAtTime<ColorShift>> COLOR_SHIFT_PROVIDERS = new ArrayList<>();
    private final FakeProviderAtTime<Float> BORDER_THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Color> BORDER_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<FloatBox> RENDERING_DIMENSIONS_PROVIDER =
            new FakeProviderAtTime<>();
    private final int Z = 123;
    private final FakeEntityUuid UUID = new FakeEntityUuid();
    private final Consumer<Renderable> UPDATE_Z_INDEX_IN_CONTAINER = renderable -> {};
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
    void testMake() {
        ImageAssetSetRenderable imageAssetSetRenderableWithMouseEvents =
                _imageAssetSetRenderableFactory.make(IMAGE_ASSET_SET_SUPPORTS_MOUSE_EVENTS, TYPE,
                        DIRECTION, null, null, ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                        RENDERING_DIMENSIONS_PROVIDER, Z, UUID, UPDATE_Z_INDEX_IN_CONTAINER,
                        REMOVE_FROM_CONTAINER);

        assertNotNull(imageAssetSetRenderableWithMouseEvents);
        assertTrue(imageAssetSetRenderableWithMouseEvents instanceof ImageAssetSetRenderableImpl);
        assertTrue(imageAssetSetRenderableWithMouseEvents.getCapturesMouseEvents());

        ImageAssetSetRenderable imageAssetSetRenderableWithoutMouseEvents =
                _imageAssetSetRenderableFactory.make(IMAGE_ASSET_SET_NOT_SUPPORTS_MOUSE_EVENTS,
                        TYPE, DIRECTION, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                        UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER);

        assertNotNull(imageAssetSetRenderableWithoutMouseEvents);
        assertTrue(imageAssetSetRenderableWithoutMouseEvents
                instanceof ImageAssetSetRenderableImpl);
        assertFalse(imageAssetSetRenderableWithoutMouseEvents.getCapturesMouseEvents());
    }

    @Test
    void testMakeWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                null, TYPE, DIRECTION, null, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET_SUPPORTS_MOUSE_EVENTS, TYPE, DIRECTION, null, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, null, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET_SUPPORTS_MOUSE_EVENTS, TYPE, DIRECTION, null, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER,  null, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET_SUPPORTS_MOUSE_EVENTS, TYPE, DIRECTION, null, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, null,
                BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET_SUPPORTS_MOUSE_EVENTS, TYPE, DIRECTION, null, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                null, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET_SUPPORTS_MOUSE_EVENTS, TYPE, DIRECTION, null, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                null, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET_SUPPORTS_MOUSE_EVENTS, TYPE, DIRECTION, null, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z, null,
                UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET_SUPPORTS_MOUSE_EVENTS, TYPE, DIRECTION, null, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                UPDATE_Z_INDEX_IN_CONTAINER, null
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET_SUPPORTS_MOUSE_EVENTS, TYPE, DIRECTION, null, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                null, REMOVE_FROM_CONTAINER
        ));

        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                null, TYPE, DIRECTION, COLOR_SHIFT_PROVIDERS,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z,
                UUID, UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET_NOT_SUPPORTS_MOUSE_EVENTS, TYPE, DIRECTION, null,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z,
                UUID, UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET_NOT_SUPPORTS_MOUSE_EVENTS, TYPE, DIRECTION, COLOR_SHIFT_PROVIDERS,
                null, BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z,
                UUID, UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET_NOT_SUPPORTS_MOUSE_EVENTS, TYPE, DIRECTION, COLOR_SHIFT_PROVIDERS,
                BORDER_THICKNESS_PROVIDER, null, RENDERING_DIMENSIONS_PROVIDER, Z,
                UUID, UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET_NOT_SUPPORTS_MOUSE_EVENTS, TYPE, DIRECTION, COLOR_SHIFT_PROVIDERS,
                BORDER_THICKNESS_PROVIDER, null, RENDERING_DIMENSIONS_PROVIDER, Z,
                UUID, UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET_NOT_SUPPORTS_MOUSE_EVENTS, TYPE, DIRECTION, COLOR_SHIFT_PROVIDERS,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, null, Z,
                UUID, UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET_NOT_SUPPORTS_MOUSE_EVENTS, TYPE, DIRECTION, COLOR_SHIFT_PROVIDERS,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z,
                null, UPDATE_Z_INDEX_IN_CONTAINER, REMOVE_FROM_CONTAINER
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET_NOT_SUPPORTS_MOUSE_EVENTS, TYPE, DIRECTION, COLOR_SHIFT_PROVIDERS,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z,
                UUID, UPDATE_Z_INDEX_IN_CONTAINER, null
        ));
        assertThrows(IllegalArgumentException.class, () -> _imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET_NOT_SUPPORTS_MOUSE_EVENTS, TYPE, DIRECTION, COLOR_SHIFT_PROVIDERS,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z,
                UUID, null, REMOVE_FROM_CONTAINER
        ));
    }
}
