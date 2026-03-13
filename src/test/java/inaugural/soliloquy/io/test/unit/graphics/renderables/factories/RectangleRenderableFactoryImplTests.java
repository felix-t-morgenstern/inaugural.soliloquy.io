package inaugural.soliloquy.io.test.unit.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.RectangleRenderableImpl;
import inaugural.soliloquy.io.graphics.renderables.factories.RectangleRenderableFactoryImpl;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.entities.Consumer;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.RectangleRenderable;
import soliloquy.specs.io.graphics.renderables.factories.RectangleRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.ui.EventInputs;

import java.awt.*;
import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomInt;
import static inaugural.soliloquy.tools.testing.Mock.generateMockStaticProvider;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RectangleRenderableFactoryImplTests {
    private final Map<Integer, Consumer<EventInputs>> ON_PRESS_CONSUMERS = mapOf();
    private final ProviderAtTime<FloatBox> RENDERING_AREA_PROVIDER =
            generateMockStaticProvider(null);
    private final int Z = randomInt();

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private ProviderAtTime<Color> mockTopLeftColorProvider;
    @Mock private ProviderAtTime<Color> mockTopRightColorProvider;
    @Mock private ProviderAtTime<Color> mockBottomRightColorProvider;
    @Mock private ProviderAtTime<Color> mockBottomLeftColorProvider;
    @Mock private ProviderAtTime<Integer> mockBackgroundTextureIdProvider;
    @Mock private ProviderAtTime<Float> mockTextureTilesPerWidthProvider;
    @Mock private ProviderAtTime<Float> mockTextureXOffsetProvider;
    @Mock private ProviderAtTime<Float> mockTextureTilesPerHeightProvider;
    @Mock private ProviderAtTime<Float> mockTextureYOffsetProvider;
    @Mock private Consumer<EventInputs> mockOnMouseOver;
    @Mock private Consumer<EventInputs> mockOnMouseLeave;
    @Mock private Component mockContainingComponent;
    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private TimestampValidator mockTimestampValidator;

    private RectangleRenderableFactory factory;

    @BeforeEach
    public void setUp() {
        factory =
                new RectangleRenderableFactoryImpl(mockRenderingBoundaries, mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new RectangleRenderableFactoryImpl(null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new RectangleRenderableFactoryImpl(mockRenderingBoundaries, null));
    }

    @Test
    public void testMake() {
        RectangleRenderable rectangleRenderable = factory.make(
                mockTopLeftColorProvider, mockTopRightColorProvider, mockBottomRightColorProvider,
                mockBottomLeftColorProvider, mockBackgroundTextureIdProvider,
                mockTextureTilesPerWidthProvider, mockTextureXOffsetProvider,
                mockTextureTilesPerHeightProvider, mockTextureYOffsetProvider,
                ON_PRESS_CONSUMERS, null, mockOnMouseOver, mockOnMouseLeave,
                RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent);

        assertNotNull(rectangleRenderable);
        assertInstanceOf(RectangleRenderableImpl.class, rectangleRenderable);
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(null, mockTopRightColorProvider, mockBottomRightColorProvider,
                        mockBottomLeftColorProvider, mockBackgroundTextureIdProvider,
                        mockTextureTilesPerWidthProvider, mockTextureXOffsetProvider,
                        mockTextureTilesPerHeightProvider, mockTextureYOffsetProvider,
                        ON_PRESS_CONSUMERS, null, mockOnMouseOver, mockOnMouseLeave,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(mockTopLeftColorProvider, null, mockBottomRightColorProvider,
                        mockBottomLeftColorProvider, mockBackgroundTextureIdProvider,
                        mockTextureTilesPerWidthProvider, mockTextureXOffsetProvider,
                        mockTextureTilesPerHeightProvider, mockTextureYOffsetProvider,
                        ON_PRESS_CONSUMERS, null, mockOnMouseOver, mockOnMouseLeave,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(mockTopLeftColorProvider, mockTopRightColorProvider, null,
                        mockBottomLeftColorProvider, mockBackgroundTextureIdProvider,
                        mockTextureTilesPerWidthProvider, mockTextureXOffsetProvider,
                        mockTextureTilesPerHeightProvider, mockTextureYOffsetProvider,
                        ON_PRESS_CONSUMERS, null, mockOnMouseOver, mockOnMouseLeave,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(mockTopLeftColorProvider, mockTopRightColorProvider,
                        mockBottomRightColorProvider, null, mockBackgroundTextureIdProvider,
                        mockTextureTilesPerWidthProvider, mockTextureXOffsetProvider,
                        mockTextureTilesPerHeightProvider, mockTextureYOffsetProvider,
                        ON_PRESS_CONSUMERS, null, mockOnMouseOver, mockOnMouseLeave,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(mockTopLeftColorProvider, mockTopRightColorProvider,
                        mockBottomRightColorProvider, mockBottomLeftColorProvider, null,
                        mockTextureTilesPerWidthProvider, mockTextureXOffsetProvider,
                        mockTextureTilesPerHeightProvider, mockTextureYOffsetProvider,
                        ON_PRESS_CONSUMERS, null, mockOnMouseOver, mockOnMouseLeave,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(mockTopLeftColorProvider, mockTopRightColorProvider,
                        mockBottomRightColorProvider, mockBottomLeftColorProvider, null,
                        mockTextureTilesPerWidthProvider, mockTextureXOffsetProvider,
                        mockTextureTilesPerHeightProvider, mockTextureYOffsetProvider,
                        ON_PRESS_CONSUMERS, null, mockOnMouseOver, mockOnMouseLeave,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(mockTopLeftColorProvider, mockTopRightColorProvider,
                        mockBottomRightColorProvider, mockBottomLeftColorProvider,
                        mockBackgroundTextureIdProvider, mockTextureTilesPerWidthProvider,
                        mockTextureXOffsetProvider, mockTextureTilesPerHeightProvider,
                        mockTextureYOffsetProvider, ON_PRESS_CONSUMERS, null, mockOnMouseOver,
                        mockOnMouseLeave, null, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(mockTopLeftColorProvider, mockTopRightColorProvider,
                        mockBottomRightColorProvider, mockBottomLeftColorProvider,
                        mockBackgroundTextureIdProvider, mockTextureTilesPerWidthProvider,
                        mockTextureXOffsetProvider, mockTextureTilesPerHeightProvider,
                        mockTextureYOffsetProvider, ON_PRESS_CONSUMERS, null, mockOnMouseOver,
                        mockOnMouseLeave, RENDERING_AREA_PROVIDER, Z, null,
                        mockContainingComponent));
    }
}
