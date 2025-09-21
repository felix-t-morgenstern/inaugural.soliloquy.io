package inaugural.soliloquy.io.test.unit.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.ImageAssetSetRenderableImpl;
import inaugural.soliloquy.io.graphics.renderables.factories.ImageAssetSetRenderableFactoryImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeProviderAtTime;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.assets.ImageAssetSet;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.factories.ImageAssetSetRenderableFactory;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.ui.EventInputs;

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
    private final List<ColorShift> COLOR_SHIFTS = listOf();
    private final FakeProviderAtTime<Float> BORDER_THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Color> BORDER_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<FloatBox> RENDERING_DIMENSIONS_PROVIDER =
            new FakeProviderAtTime<>();
    private final int Z = randomInt();

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private Action<EventInputs> mockOnMouseOver;
    @Mock private Action<EventInputs> mockOnMouseLeave;
    @Mock private ImageAssetSet imageAssetSetSupportsMouseEvents;
    @Mock private ImageAssetSet imageAssetSetNotSupportsMouseEvents;
    @Mock private Component mockContainingComponent;
    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private TimestampValidator mockTimestampValidator;

    private ImageAssetSetRenderableFactory factory;

    @BeforeEach
    public void setUp() {
        lenient().when(imageAssetSetSupportsMouseEvents.supportsMouseEventCapturing())
                .thenReturn(true);
        lenient().when(imageAssetSetNotSupportsMouseEvents.supportsMouseEventCapturing())
                .thenReturn(false);

        lenient().when(mockRenderingBoundaries.currentBoundaries()).thenReturn(WHOLE_SCREEN);

        factory = new ImageAssetSetRenderableFactoryImpl(mockRenderingBoundaries,
                mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableFactoryImpl(null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new ImageAssetSetRenderableFactoryImpl(mockRenderingBoundaries, null));
    }

    @Test
    public void testMake() {
        // TODO: Create proper maps for press and release!
        var output = factory.make(imageAssetSetSupportsMouseEvents, DISPLAY_PARAMS,
                BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, mapOf(), mapOf(), mockOnMouseOver,
                mockOnMouseLeave, COLOR_SHIFTS, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                mockContainingComponent);

        assertNotNull(output);
        assertInstanceOf(ImageAssetSetRenderableImpl.class, output);
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(null, DISPLAY_PARAMS, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, null, null, mockOnMouseOver, mockOnMouseLeave,
                        COLOR_SHIFTS, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(imageAssetSetSupportsMouseEvents, DISPLAY_PARAMS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, null, null,
                        mockOnMouseOver,
                        mockOnMouseLeave, null, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(imageAssetSetSupportsMouseEvents, DISPLAY_PARAMS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, null, null,
                        mockOnMouseOver,
                        mockOnMouseLeave, COLOR_SHIFTS, null, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(imageAssetSetSupportsMouseEvents, DISPLAY_PARAMS, null,
                        BORDER_COLOR_PROVIDER, null, null, mockOnMouseOver, mockOnMouseLeave,
                        COLOR_SHIFTS, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(imageAssetSetSupportsMouseEvents, DISPLAY_PARAMS,
                        BORDER_THICKNESS_PROVIDER, null, null, null, mockOnMouseOver,
                        mockOnMouseLeave,
                        COLOR_SHIFTS, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(imageAssetSetSupportsMouseEvents, DISPLAY_PARAMS,
                        BORDER_THICKNESS_PROVIDER, null, null, null, mockOnMouseOver,
                        mockOnMouseLeave,
                        COLOR_SHIFTS, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(imageAssetSetSupportsMouseEvents, DISPLAY_PARAMS,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, null, null,
                        mockOnMouseOver,
                        mockOnMouseLeave, COLOR_SHIFTS, RENDERING_DIMENSIONS_PROVIDER, Z, null,
                        mockContainingComponent));

        assertThrows(IllegalArgumentException.class,
                () -> factory.make(null, DISPLAY_PARAMS, COLOR_SHIFTS, BORDER_THICKNESS_PROVIDER,
                        BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(imageAssetSetNotSupportsMouseEvents, DISPLAY_PARAMS, null,
                        BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                        RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(imageAssetSetNotSupportsMouseEvents, DISPLAY_PARAMS,
                        COLOR_SHIFTS, null, BORDER_COLOR_PROVIDER, RENDERING_DIMENSIONS_PROVIDER, Z,
                        UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(imageAssetSetNotSupportsMouseEvents, DISPLAY_PARAMS,
                        COLOR_SHIFTS, BORDER_THICKNESS_PROVIDER, null,
                        RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(imageAssetSetNotSupportsMouseEvents, DISPLAY_PARAMS,
                        COLOR_SHIFTS, BORDER_THICKNESS_PROVIDER, null,
                        RENDERING_DIMENSIONS_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(imageAssetSetNotSupportsMouseEvents, DISPLAY_PARAMS,
                        COLOR_SHIFTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER, null, Z,
                        UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(imageAssetSetNotSupportsMouseEvents, DISPLAY_PARAMS,
                        COLOR_SHIFTS, BORDER_THICKNESS_PROVIDER, BORDER_COLOR_PROVIDER,
                        RENDERING_DIMENSIONS_PROVIDER, Z, null, mockContainingComponent));
    }
}
