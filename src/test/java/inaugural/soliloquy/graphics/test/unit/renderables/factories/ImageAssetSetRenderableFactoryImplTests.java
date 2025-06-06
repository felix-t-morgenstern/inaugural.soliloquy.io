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
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.graphics.assets.ImageAssetSet;
import soliloquy.specs.graphics.renderables.RenderableWithMouseEvents.MouseEventInputs;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.factories.ImageAssetSetRenderableFactory;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;

import java.awt.*;
import java.util.List;
import java.util.UUID;

import static inaugural.soliloquy.graphics.api.Constants.WHOLE_SCREEN;
import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomInt;
import static inaugural.soliloquy.tools.random.Random.randomString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.shared.Direction.SOUTHWEST;

public class ImageAssetSetRenderableFactoryImplTests {
    private final ImageAssetSet IMAGE_ASSET_SET_SUPPORTS_MOUSE_EVENTS =
            new FakeImageAssetSet(true);
    private final ImageAssetSet IMAGE_ASSET_SET_NOT_SUPPORTS_MOUSE_EVENTS =
            new FakeImageAssetSet(false);
    private final String TYPE = randomString();
    private final Direction DIRECTION = SOUTHWEST;
    private final FakeAction<MouseEventInputs> ON_MOUSE_OVER = new FakeAction<>();
    private final FakeAction<MouseEventInputs> ON_MOUSE_LEAVE = new FakeAction<>();
    private final List<ProviderAtTime<ColorShift>> COLOR_SHIFT_PROVIDERS = listOf();
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
    public void setUp() {
        mockContainingStack = mock(RenderableStack.class);
        mockRenderingBoundaries = mock(RenderingBoundaries.class);
        when(mockRenderingBoundaries.currentBoundaries()).thenReturn(WHOLE_SCREEN);

        imageAssetSetRenderableFactory =
                new ImageAssetSetRenderableFactoryImpl(mockRenderingBoundaries);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableFactoryImpl(null));
    }

    @Test
    public void testMake() {
        // TODO: Create proper maps for press and release!
        var imageAssetSetRenderableWithMouseEvents =
                imageAssetSetRenderableFactory.make(IMAGE_ASSET_SET_SUPPORTS_MOUSE_EVENTS, TYPE,
                        DIRECTION, mapOf(), mapOf(), ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                        RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingStack);

        assertNotNull(imageAssetSetRenderableWithMouseEvents);
        assertInstanceOf(ImageAssetSetRenderableImpl.class, imageAssetSetRenderableWithMouseEvents);
        assertTrue(imageAssetSetRenderableWithMouseEvents.getCapturesMouseEvents());

        var imageAssetSetRenderableWithoutMouseEvents =
                imageAssetSetRenderableFactory.make(IMAGE_ASSET_SET_NOT_SUPPORTS_MOUSE_EVENTS,
                        TYPE, DIRECTION, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                        mockContainingStack);

        assertNotNull(imageAssetSetRenderableWithoutMouseEvents);
        assertInstanceOf(ImageAssetSetRenderableImpl.class,
                imageAssetSetRenderableWithoutMouseEvents);
        assertFalse(imageAssetSetRenderableWithoutMouseEvents.getCapturesMouseEvents());
    }

    @Test
    public void testMakeWithInvalidArgs() {
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
