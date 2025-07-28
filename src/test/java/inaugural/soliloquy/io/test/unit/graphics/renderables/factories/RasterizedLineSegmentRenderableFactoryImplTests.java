package inaugural.soliloquy.io.test.unit.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.RasterizedLineSegmentRenderableImpl;
import inaugural.soliloquy.io.graphics.renderables.factories.RasterizedLineSegmentRenderableFactoryImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeProviderAtTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.factories.RasterizedLineSegmentRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.ui.Component;

import java.awt.*;
import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.randomInt;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RasterizedLineSegmentRenderableFactoryImplTests {
    private final ProviderAtTime<Float> THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final short STIPPLE_PATTERN = 456;
    private final short STIPPLE_FACTOR = 123;
    private final ProviderAtTime<Color> COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Vertex> VERTEX_1_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Vertex> VERTEX_2_PROVIDER = new FakeProviderAtTime<>();
    private final int Z = randomInt();

    private static final UUID UUID = java.util.UUID.randomUUID();

    @Mock private Component mockContainingComponent;

    private RasterizedLineSegmentRenderableFactory factory;

    @BeforeEach
    public void setUp() {
        factory = new RasterizedLineSegmentRenderableFactoryImpl();
    }

    @Test
    public void testMake() {
        var rasterizedLineSegmentRenderable =
                factory.make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, THICKNESS_PROVIDER,
                        STIPPLE_PATTERN, STIPPLE_FACTOR, COLOR_PROVIDER, Z, UUID,
                        mockContainingComponent);

        assertNotNull(rasterizedLineSegmentRenderable);
        assertInstanceOf(RasterizedLineSegmentRenderableImpl.class,
                rasterizedLineSegmentRenderable);
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(null, VERTEX_2_PROVIDER, THICKNESS_PROVIDER, STIPPLE_PATTERN,
                        STIPPLE_FACTOR, COLOR_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(VERTEX_1_PROVIDER, null, THICKNESS_PROVIDER, STIPPLE_PATTERN,
                        STIPPLE_FACTOR, COLOR_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, null, STIPPLE_PATTERN,
                        STIPPLE_FACTOR, COLOR_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, THICKNESS_PROVIDER,
                        (short) 0, STIPPLE_FACTOR, COLOR_PROVIDER, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, THICKNESS_PROVIDER,
                        STIPPLE_PATTERN, (short) 0, COLOR_PROVIDER, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, THICKNESS_PROVIDER,
                        STIPPLE_PATTERN, (short) 257, COLOR_PROVIDER, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, THICKNESS_PROVIDER,
                        STIPPLE_PATTERN, STIPPLE_FACTOR, null, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, THICKNESS_PROVIDER,
                        STIPPLE_PATTERN, STIPPLE_FACTOR, COLOR_PROVIDER, Z, null,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, THICKNESS_PROVIDER,
                        STIPPLE_PATTERN, STIPPLE_FACTOR, COLOR_PROVIDER, Z, UUID, null));
    }
}
