package inaugural.soliloquy.io.test.unit.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.RasterizedLineSegmentRenderableImpl;
import inaugural.soliloquy.io.graphics.renderables.factories.RasterizedLineSegmentRenderableFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.factories.RasterizedLineSegmentRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.randomInt;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RasterizedLineSegmentRenderableFactoryImplTests {
    private final short STIPPLE_PATTERN = 456;
    private final short STIPPLE_FACTOR = 123;
    private final int Z = randomInt();
    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private ProviderAtTime<Float> mockThicknessProvider;
    @Mock private ProviderAtTime<Color> mockColorProvider;
    @Mock private ProviderAtTime<Vertex> mockVertex1Provider;
    @Mock private ProviderAtTime<Vertex> mockVertex2Provider;

    @Mock private Component mockContainingComponent;

    private RasterizedLineSegmentRenderableFactory factory;

    @BeforeEach
    public void setUp() {
        factory = new RasterizedLineSegmentRenderableFactoryImpl();
    }

    @Test
    public void testMake() {
        var rasterizedLineSegmentRenderable =
                factory.make(mockVertex1Provider, mockVertex2Provider, mockThicknessProvider,
                        STIPPLE_PATTERN, STIPPLE_FACTOR, mockColorProvider, Z, UUID,
                        mockContainingComponent);

        assertNotNull(rasterizedLineSegmentRenderable);
        assertInstanceOf(RasterizedLineSegmentRenderableImpl.class,
                rasterizedLineSegmentRenderable);
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(null, mockVertex2Provider, mockThicknessProvider, STIPPLE_PATTERN,
                        STIPPLE_FACTOR, mockColorProvider, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(mockVertex1Provider, null, mockThicknessProvider, STIPPLE_PATTERN,
                        STIPPLE_FACTOR, mockColorProvider, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(mockVertex1Provider, mockVertex2Provider, null, STIPPLE_PATTERN,
                        STIPPLE_FACTOR, mockColorProvider, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(mockVertex1Provider, mockVertex2Provider, mockThicknessProvider,
                        (short) 0, STIPPLE_FACTOR, mockColorProvider, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(mockVertex1Provider, mockVertex2Provider, mockThicknessProvider,
                        STIPPLE_PATTERN, (short) 0, mockColorProvider, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(mockVertex1Provider, mockVertex2Provider, mockThicknessProvider,
                        STIPPLE_PATTERN, (short) 257, mockColorProvider, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(mockVertex1Provider, mockVertex2Provider, mockThicknessProvider,
                        STIPPLE_PATTERN, STIPPLE_FACTOR, null, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(mockVertex1Provider, mockVertex2Provider, mockThicknessProvider,
                        STIPPLE_PATTERN, STIPPLE_FACTOR, mockColorProvider, Z, null,
                        mockContainingComponent));
    }
}
