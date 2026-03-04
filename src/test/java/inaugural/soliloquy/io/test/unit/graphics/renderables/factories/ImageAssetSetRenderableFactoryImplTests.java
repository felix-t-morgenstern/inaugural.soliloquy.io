package inaugural.soliloquy.io.test.unit.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.ImageAssetSetRenderableImpl;
import inaugural.soliloquy.io.graphics.renderables.factories.ImageAssetSetRenderableFactoryImpl;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.entities.Consumer;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.assets.ImageAssetSet;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.factories.ImageAssetSetRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
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
    private final int Z = randomInt();

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private ProviderAtTime<Float> mockBorderThicknessProvider;
    @Mock private ProviderAtTime<Color> mockBorderColorProvider;
    @Mock private Consumer<EventInputs> mockOnMouseOver;
    @Mock private Consumer<EventInputs> mockOnMouseLeave;
    @Mock private ImageAssetSet imageAssetSetSupportsMouseEvents;
    @Mock private ImageAssetSet imageAssetSetNotSupportsMouseEvents;
    @Mock private ProviderAtTime<FloatBox> mockRenderingDimensionsProvider;
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
                mockBorderThicknessProvider, mockBorderColorProvider, mapOf(), mapOf(), mockOnMouseOver,
                mockOnMouseLeave, COLOR_SHIFTS, mockRenderingDimensionsProvider, Z, UUID,
                mockContainingComponent);

        assertNotNull(output);
        assertInstanceOf(ImageAssetSetRenderableImpl.class, output);
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(null, DISPLAY_PARAMS, mockBorderThicknessProvider,
                        mockBorderColorProvider, null, null, mockOnMouseOver, mockOnMouseLeave,
                        COLOR_SHIFTS, mockRenderingDimensionsProvider, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(imageAssetSetSupportsMouseEvents, DISPLAY_PARAMS,
                        mockBorderThicknessProvider, mockBorderColorProvider, null, null,
                        mockOnMouseOver,
                        mockOnMouseLeave, null, mockRenderingDimensionsProvider, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(imageAssetSetSupportsMouseEvents, DISPLAY_PARAMS,
                        mockBorderThicknessProvider, mockBorderColorProvider, null, null,
                        mockOnMouseOver,
                        mockOnMouseLeave, COLOR_SHIFTS, null, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(imageAssetSetSupportsMouseEvents, DISPLAY_PARAMS, null,
                        mockBorderColorProvider, null, null, mockOnMouseOver, mockOnMouseLeave,
                        COLOR_SHIFTS, mockRenderingDimensionsProvider, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(imageAssetSetSupportsMouseEvents, DISPLAY_PARAMS,
                        mockBorderThicknessProvider, null, null, null, mockOnMouseOver,
                        mockOnMouseLeave,
                        COLOR_SHIFTS, mockRenderingDimensionsProvider, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(imageAssetSetSupportsMouseEvents, DISPLAY_PARAMS,
                        mockBorderThicknessProvider, null, null, null, mockOnMouseOver,
                        mockOnMouseLeave,
                        COLOR_SHIFTS, mockRenderingDimensionsProvider, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(imageAssetSetSupportsMouseEvents, DISPLAY_PARAMS,
                        mockBorderThicknessProvider, mockBorderColorProvider, null, null,
                        mockOnMouseOver,
                        mockOnMouseLeave, COLOR_SHIFTS, mockRenderingDimensionsProvider, Z, null,
                        mockContainingComponent));

        assertThrows(IllegalArgumentException.class,
                () -> factory.make(null, DISPLAY_PARAMS, COLOR_SHIFTS, mockBorderThicknessProvider,
                        mockBorderColorProvider, mockRenderingDimensionsProvider, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(imageAssetSetNotSupportsMouseEvents, DISPLAY_PARAMS, null,
                        mockBorderThicknessProvider, mockBorderColorProvider,
                        mockRenderingDimensionsProvider, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(imageAssetSetNotSupportsMouseEvents, DISPLAY_PARAMS,
                        COLOR_SHIFTS, null, mockBorderColorProvider,
                        mockRenderingDimensionsProvider, Z,
                        UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(imageAssetSetNotSupportsMouseEvents, DISPLAY_PARAMS,
                        COLOR_SHIFTS, mockBorderThicknessProvider, null,
                        mockRenderingDimensionsProvider, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(imageAssetSetNotSupportsMouseEvents, DISPLAY_PARAMS,
                        COLOR_SHIFTS, mockBorderThicknessProvider, null,
                        mockRenderingDimensionsProvider, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(imageAssetSetNotSupportsMouseEvents, DISPLAY_PARAMS,
                        COLOR_SHIFTS, mockBorderThicknessProvider, mockBorderColorProvider, null, Z,
                        UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(imageAssetSetNotSupportsMouseEvents, DISPLAY_PARAMS,
                        COLOR_SHIFTS, mockBorderThicknessProvider, mockBorderColorProvider,
                        mockRenderingDimensionsProvider, Z, null, mockContainingComponent));
    }
}
