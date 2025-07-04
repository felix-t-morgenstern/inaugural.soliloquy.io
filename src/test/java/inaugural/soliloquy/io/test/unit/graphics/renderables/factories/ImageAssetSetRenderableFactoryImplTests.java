package inaugural.soliloquy.io.test.unit.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.ImageAssetSetRenderableImpl;
import inaugural.soliloquy.io.graphics.renderables.factories.ImageAssetSetRenderableFactoryImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeAction;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeProviderAtTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.assets.ImageAssetSet;
import soliloquy.specs.io.graphics.renderables.RenderableWithMouseEvents.MouseEventInputs;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.factories.ImageAssetSetRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderableStack;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.io.api.Constants.WHOLE_SCREEN;
import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomInt;
import static inaugural.soliloquy.tools.random.Random.randomString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

@ExtendWith(MockitoExtension.class)
public class ImageAssetSetRenderableFactoryImplTests {
    private final String STANCE_PARAM = randomString();
    private final String STANCE = randomString();
    private final String DIRECTION_PARAM = randomString();
    private final String DIRECTION = randomString();
    private final Map<String, String> DISPLAY_PARAMS = mapOf(
            pairOf(STANCE_PARAM, STANCE),
            pairOf(DIRECTION_PARAM, DIRECTION)
    );
    private final FakeAction<MouseEventInputs> ON_MOUSE_OVER = new FakeAction<>();
    private final FakeAction<MouseEventInputs> ON_MOUSE_LEAVE = new FakeAction<>();
    private final List<ProviderAtTime<ColorShift>> COLOR_SHIFT_PROVIDERS = listOf();
    private final FakeProviderAtTime<Float> BORDER_THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Color> BORDER_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<FloatBox> RENDERING_DIMENSIONS_PROVIDER =
            new FakeProviderAtTime<>();
    private final int Z = randomInt();

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private ImageAssetSet imageAssetSetSupportsMouseEvents;
    @Mock private ImageAssetSet imageAssetSetNotSupportsMouseEvents;
    @Mock private RenderableStack mockContainingStack;
    @Mock private RenderingBoundaries mockRenderingBoundaries;

    private ImageAssetSetRenderableFactory factory;

    @BeforeEach
    public void setUp() {
        lenient().when(imageAssetSetSupportsMouseEvents.supportsMouseEventCapturing()).thenReturn(true);
        lenient().when(imageAssetSetNotSupportsMouseEvents.supportsMouseEventCapturing()).thenReturn(false);

        lenient().when(mockRenderingBoundaries.currentBoundaries()).thenReturn(WHOLE_SCREEN);

        factory = new ImageAssetSetRenderableFactoryImpl(mockRenderingBoundaries);
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
                factory.make(imageAssetSetSupportsMouseEvents, DISPLAY_PARAMS, mapOf(), mapOf(),
                        ON_MOUSE_OVER, ON_MOUSE_LEAVE,
                        COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                        RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingStack);

        assertNotNull(imageAssetSetRenderableWithMouseEvents);
        assertInstanceOf(ImageAssetSetRenderableImpl.class, imageAssetSetRenderableWithMouseEvents);
        assertTrue(imageAssetSetRenderableWithMouseEvents.getCapturesMouseEvents());

        var imageAssetSetRenderableWithoutMouseEvents =
                factory.make(imageAssetSetNotSupportsMouseEvents,
                        DISPLAY_PARAMS, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                        mockContainingStack);

        assertNotNull(imageAssetSetRenderableWithoutMouseEvents);
        assertInstanceOf(ImageAssetSetRenderableImpl.class,
                imageAssetSetRenderableWithoutMouseEvents);
        assertFalse(imageAssetSetRenderableWithoutMouseEvents.getCapturesMouseEvents());
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> factory.make(
                null, DISPLAY_PARAMS, null, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> factory.make(
                imageAssetSetSupportsMouseEvents, DISPLAY_PARAMS, null, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, null, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> factory.make(
                imageAssetSetSupportsMouseEvents, DISPLAY_PARAMS, null, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, null, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> factory.make(
                imageAssetSetSupportsMouseEvents, DISPLAY_PARAMS, null, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, null,
                BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> factory.make(
                imageAssetSetSupportsMouseEvents, DISPLAY_PARAMS, null, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                null, RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> factory.make(
                imageAssetSetSupportsMouseEvents, DISPLAY_PARAMS, null, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                null, RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> factory.make(
                imageAssetSetSupportsMouseEvents, DISPLAY_PARAMS, null, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z, null, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> factory.make(
                imageAssetSetSupportsMouseEvents, DISPLAY_PARAMS, null, null,
                ON_MOUSE_OVER, ON_MOUSE_LEAVE, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z, UUID, null
        ));

        assertThrows(IllegalArgumentException.class, () -> factory.make(
                null, DISPLAY_PARAMS, COLOR_SHIFT_PROVIDERS, BORDER_THICKNESS_PROVIDER,
                BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> factory.make(
                imageAssetSetNotSupportsMouseEvents, DISPLAY_PARAMS, null,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z,
                UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> factory.make(
                imageAssetSetNotSupportsMouseEvents, DISPLAY_PARAMS, COLOR_SHIFT_PROVIDERS,
                null, BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> factory.make(
                imageAssetSetNotSupportsMouseEvents, DISPLAY_PARAMS, COLOR_SHIFT_PROVIDERS,
                BORDER_THICKNESS_PROVIDER, null, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> factory.make(
                imageAssetSetNotSupportsMouseEvents, DISPLAY_PARAMS, COLOR_SHIFT_PROVIDERS,
                BORDER_THICKNESS_PROVIDER, null, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> factory.make(
                imageAssetSetNotSupportsMouseEvents, DISPLAY_PARAMS, COLOR_SHIFT_PROVIDERS,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, null, Z, UUID,
                mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> factory.make(
                imageAssetSetNotSupportsMouseEvents, DISPLAY_PARAMS, COLOR_SHIFT_PROVIDERS,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z,
                null, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> factory.make(
                imageAssetSetNotSupportsMouseEvents, DISPLAY_PARAMS, COLOR_SHIFT_PROVIDERS,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z,
                UUID, null
        ));
    }
}
