package inaugural.soliloquy.io.test.unit.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.RectangleRenderableImpl;
import inaugural.soliloquy.io.graphics.renderables.factories.RectangleRenderableFactoryImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeProviderAtTime;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeStaticProvider;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.entities.Action;
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
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RectangleRenderableFactoryImplTests {
    private final ProviderAtTime<Color> TOP_LEFT_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> TOP_RIGHT_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> BOTTOM_RIGHT_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> BOTTOM_LEFT_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Integer> BACKGROUND_TEXTURE_ID_PROVIDER =
            new FakeProviderAtTime<>();
    private final Map<Integer, Action<EventInputs>> ON_PRESS_ACTIONS = mapOf();
    private final FakeStaticProvider<FloatBox> RENDERING_AREA_PROVIDER =
            new FakeStaticProvider<>(null);
    private final int Z = randomInt();

    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private Action<EventInputs> mockOnMouseOver;
    @Mock private Action<EventInputs> mockOnMouseLeave;
    @Mock private ProviderAtTime<Float> mockTextureTileWidthProvider;
    @Mock private ProviderAtTime<Float> mockTextureTileHeightProvider;
    @Mock private Component mockContainingComponent;
    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private TimestampValidator mockTimestampValidator;

    private RectangleRenderableFactory rectangleRenderableFactory;

    @BeforeEach
    public void setUp() {
        rectangleRenderableFactory = new RectangleRenderableFactoryImpl(mockRenderingBoundaries, mockTimestampValidator);
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
        RectangleRenderable rectangleRenderable = rectangleRenderableFactory.make(
                TOP_LEFT_COLOR_PROVIDER, TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                ON_PRESS_ACTIONS, null, mockOnMouseOver, mockOnMouseLeave,
                RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent);

        assertNotNull(rectangleRenderable);
        assertInstanceOf(RectangleRenderableImpl.class, rectangleRenderable);
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderableFactory.make(null,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                        ON_PRESS_ACTIONS, null, mockOnMouseOver, mockOnMouseLeave,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderableFactory.make(TOP_LEFT_COLOR_PROVIDER,
                        null, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                        ON_PRESS_ACTIONS, null, mockOnMouseOver, mockOnMouseLeave,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderableFactory.make(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, null,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                        ON_PRESS_ACTIONS, null, mockOnMouseOver, mockOnMouseLeave,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderableFactory.make(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        null, BACKGROUND_TEXTURE_ID_PROVIDER,
                        mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                        ON_PRESS_ACTIONS, null, mockOnMouseOver, mockOnMouseLeave,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderableFactory.make(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, null,
                        mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                        ON_PRESS_ACTIONS, null, mockOnMouseOver, mockOnMouseLeave,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderableFactory.make(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        null, mockTextureTileHeightProvider,
                        ON_PRESS_ACTIONS, null, mockOnMouseOver, mockOnMouseLeave,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderableFactory.make(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        mockTextureTileWidthProvider, null,
                        ON_PRESS_ACTIONS, null, mockOnMouseOver, mockOnMouseLeave,
                        RENDERING_AREA_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderableFactory.make(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                        ON_PRESS_ACTIONS, null, mockOnMouseOver, mockOnMouseLeave,
                        null, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () ->
                rectangleRenderableFactory.make(TOP_LEFT_COLOR_PROVIDER,
                        TOP_RIGHT_COLOR_PROVIDER, BOTTOM_RIGHT_COLOR_PROVIDER,
                        BOTTOM_LEFT_COLOR_PROVIDER, BACKGROUND_TEXTURE_ID_PROVIDER,
                        mockTextureTileWidthProvider, mockTextureTileHeightProvider,
                        ON_PRESS_ACTIONS, null, mockOnMouseOver, mockOnMouseLeave,
                        RENDERING_AREA_PROVIDER, Z, null, mockContainingComponent));
    }
}
