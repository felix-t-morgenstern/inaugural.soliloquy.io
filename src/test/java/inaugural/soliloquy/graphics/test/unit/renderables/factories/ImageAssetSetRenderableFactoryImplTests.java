package inaugural.soliloquy.graphics.test.unit.renderables.factories;

import inaugural.soliloquy.graphics.renderables.ImageAssetSetRenderableImpl;
import inaugural.soliloquy.graphics.renderables.factories.ImageAssetSetRenderableFactoryImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeAction;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeImageAssetSet;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeProviderAtTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.common.shared.Direction;
import soliloquy.specs.graphics.assets.ImageAssetSet;
import soliloquy.specs.graphics.renderables.RenderableWithMouseEvents.MouseEventInputs;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.factories.ImageAssetSetRenderableFactory;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static inaugural.soliloquy.graphics.api.Constants.WHOLE_SCREEN;
import static inaugural.soliloquy.tools.random.Random.randomInt;
import static inaugural.soliloquy.tools.random.Random.randomString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.shared.Direction.SOUTHWEST;

class ImageAssetSetRenderableFactoryImplTests {
    private final ImageAssetSet IMAGE_ASSET_SET_SUPPORTS_MOUSE_EVENTS =
            new FakeImageAssetSet(true);
    private final ImageAssetSet IMAGE_ASSET_SET_NOT_SUPPORTS_MOUSE_EVENTS =
            new FakeImageAssetSet(false);
    private final String TYPE = randomString();
    private final Direction DIRECTION = SOUTHWEST;
    private final FakeAction<MouseEventInputs> ON_MOUSE_OVER = new FakeAction<>();
    private final FakeAction<MouseEventInputs> ON_MOUSE_LEAVE = new FakeAction<>();
    private final ArrayList<ProviderAtTime<ColorShift>> COLOR_SHIFT_PROVIDERS = new ArrayList<>();
    private final FakeProviderAtTime<Float> BORDER_THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Color> BORDER_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<FloatBox> RENDERING_DIMENSIONS_PROVIDER =
            new FakeProviderAtTime<>();
    private final int Z = randomInt();

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private RenderableStack mockContainingStack;
    @Mock private RenderingBoundaries mockRenderingBoundaries;

    private ImageAssetSetRenderableFactory imageAssetSetRenderableFactory;

    @BeforeEach
    void setUp() {
        mockContainingStack = mock(RenderableStack.class);
        mockRenderingBoundaries = mock(RenderingBoundaries.class);
        when(mockRenderingBoundaries.currentBoundaries()).thenReturn(WHOLE_SCREEN);

        imageAssetSetRenderableFactory =
                new ImageAssetSetRenderableFactoryImpl(mockRenderingBoundaries);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableFactoryImpl(null));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(ImageAssetSetRenderableFactory.class.getCanonicalName(),
                imageAssetSetRenderableFactory.getInterfaceName());
    }

    @Test
    void testMake() {
        // TODO: Create proper maps for press and release!
        var imageAssetSetRenderableWithMouseEvents =
                imageAssetSetRenderableFactory.make(IMAGE_ASSET_SET_SUPPORTS_MOUSE_EVENTS, TYPE,
                        DIRECTION, new HashMap<>(), new HashMap<>(), ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                        RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingStack);

        assertNotNull(imageAssetSetRenderableWithMouseEvents);
        assertTrue(imageAssetSetRenderableWithMouseEvents instanceof ImageAssetSetRenderableImpl);
        assertTrue(imageAssetSetRenderableWithMouseEvents.getCapturesMouseEvents());

        var imageAssetSetRenderableWithoutMouseEvents =
                imageAssetSetRenderableFactory.make(IMAGE_ASSET_SET_NOT_SUPPORTS_MOUSE_EVENTS,
                        TYPE, DIRECTION, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                        mockContainingStack);

        assertNotNull(imageAssetSetRenderableWithoutMouseEvents);
        assertTrue(imageAssetSetRenderableWithoutMouseEvents
                instanceof ImageAssetSetRenderableImpl);
        assertFalse(imageAssetSetRenderableWithoutMouseEvents.getCapturesMouseEvents());
    }

    @Test
    void testMakeWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> imageAssetSetRenderableFactory.make(
                null, TYPE, DIRECTION, null, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET_SUPPORTS_MOUSE_EVENTS, TYPE, DIRECTION, null, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, null, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET_SUPPORTS_MOUSE_EVENTS, TYPE, DIRECTION, null, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, null, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET_SUPPORTS_MOUSE_EVENTS, TYPE, DIRECTION, null, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, null,
                BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET_SUPPORTS_MOUSE_EVENTS, TYPE, DIRECTION, null, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                null, RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET_SUPPORTS_MOUSE_EVENTS, TYPE, DIRECTION, null, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                null, RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET_SUPPORTS_MOUSE_EVENTS, TYPE, DIRECTION, null, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z, null, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET_SUPPORTS_MOUSE_EVENTS, TYPE, DIRECTION, null, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z, UUID, null
        ));

        assertThrows(IllegalArgumentException.class, () -> imageAssetSetRenderableFactory.make(
                null, TYPE, DIRECTION, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET_NOT_SUPPORTS_MOUSE_EVENTS, TYPE, DIRECTION, null,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z,
                UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET_NOT_SUPPORTS_MOUSE_EVENTS, TYPE, DIRECTION, COLOR_SHIFT_PROVIDERS,
                null, BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET_NOT_SUPPORTS_MOUSE_EVENTS, TYPE, DIRECTION, COLOR_SHIFT_PROVIDERS,
                BORDER_THICKNESS_PROVIDER, null, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET_NOT_SUPPORTS_MOUSE_EVENTS, TYPE, DIRECTION, COLOR_SHIFT_PROVIDERS,
                BORDER_THICKNESS_PROVIDER, null, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET_NOT_SUPPORTS_MOUSE_EVENTS, TYPE, DIRECTION, COLOR_SHIFT_PROVIDERS,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, null, Z, UUID,
                mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET_NOT_SUPPORTS_MOUSE_EVENTS, TYPE, DIRECTION, COLOR_SHIFT_PROVIDERS,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z,
                null, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> imageAssetSetRenderableFactory.make(
                IMAGE_ASSET_SET_NOT_SUPPORTS_MOUSE_EVENTS, TYPE, DIRECTION, COLOR_SHIFT_PROVIDERS,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z,
                UUID, null
        ));
    }
}
