package inaugural.soliloquy.graphics.test.unit.renderables.factories;

import inaugural.soliloquy.graphics.renderables.RasterizedLineSegmentRenderableImpl;
import inaugural.soliloquy.graphics.renderables.factories.RasterizedLineSegmentRenderableFactoryImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeProviderAtTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.graphics.renderables.factories.RasterizedLineSegmentRenderableFactory;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.awt.*;
import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.randomInt;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class RasterizedLineSegmentRenderableFactoryImplTests {
    private final ProviderAtTime<Float> THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final short STIPPLE_PATTERN = 456;
    private final short STIPPLE_FACTOR = 123;
    private final ProviderAtTime<Color> COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Vertex> VERTEX_1_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Vertex> VERTEX_2_PROVIDER = new FakeProviderAtTime<>();
    private final int Z = randomInt();

    private static final UUID UUID = java.util.UUID.randomUUID();

    @Mock private RenderableStack _mockContainingStack;

    private RasterizedLineSegmentRenderableFactory _rasterizedLineSegmentRenderableFactory;

    @BeforeEach
    void setUp() {
        _mockContainingStack = mock(RenderableStack.class);
        _rasterizedLineSegmentRenderableFactory = new RasterizedLineSegmentRenderableFactoryImpl();
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(RasterizedLineSegmentRenderableFactory.class.getCanonicalName(),
                _rasterizedLineSegmentRenderableFactory.getInterfaceName());
    }

    @Test
    void testMake() {
        RasterizedLineSegmentRenderable rasterizedLineSegmentRenderable =
                _rasterizedLineSegmentRenderableFactory
                        .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                                THICKNESS_PROVIDER, STIPPLE_PATTERN, STIPPLE_FACTOR, COLOR_PROVIDER,
                                Z, UUID, _mockContainingStack);

        assertNotNull(rasterizedLineSegmentRenderable);
        assertTrue(rasterizedLineSegmentRenderable instanceof RasterizedLineSegmentRenderableImpl);
    }

    @Test
    void testMakeWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _rasterizedLineSegmentRenderableFactory
                        .make(null, VERTEX_2_PROVIDER, THICKNESS_PROVIDER, STIPPLE_PATTERN,
                                STIPPLE_FACTOR, COLOR_PROVIDER, Z, UUID,
                                _mockContainingStack));
        assertThrows(IllegalArgumentException.class, () ->
                _rasterizedLineSegmentRenderableFactory
                        .make(VERTEX_1_PROVIDER, null, THICKNESS_PROVIDER, STIPPLE_PATTERN,
                                STIPPLE_FACTOR, COLOR_PROVIDER, Z, UUID,
                                _mockContainingStack));
        assertThrows(IllegalArgumentException.class, () ->
                _rasterizedLineSegmentRenderableFactory
                        .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, null,
                                STIPPLE_PATTERN, STIPPLE_FACTOR, COLOR_PROVIDER, Z, UUID,
                                _mockContainingStack));
        assertThrows(IllegalArgumentException.class, () ->
                _rasterizedLineSegmentRenderableFactory
                        .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                                THICKNESS_PROVIDER, (short) 0, STIPPLE_FACTOR, COLOR_PROVIDER, Z,
                                UUID, _mockContainingStack));
        assertThrows(IllegalArgumentException.class, () ->
                _rasterizedLineSegmentRenderableFactory
                        .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                                THICKNESS_PROVIDER, STIPPLE_PATTERN, (short) 0, COLOR_PROVIDER, Z,
                                UUID, _mockContainingStack));
        assertThrows(IllegalArgumentException.class, () ->
                _rasterizedLineSegmentRenderableFactory
                        .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                                THICKNESS_PROVIDER, STIPPLE_PATTERN, (short) 257, COLOR_PROVIDER, Z,
                                UUID, _mockContainingStack));
        assertThrows(IllegalArgumentException.class, () ->
                _rasterizedLineSegmentRenderableFactory
                        .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                                THICKNESS_PROVIDER, STIPPLE_PATTERN, STIPPLE_FACTOR, null, Z, UUID,
                                _mockContainingStack));
        assertThrows(IllegalArgumentException.class, () ->
                _rasterizedLineSegmentRenderableFactory
                        .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                                THICKNESS_PROVIDER, STIPPLE_PATTERN, STIPPLE_FACTOR, COLOR_PROVIDER,
                                Z, null, _mockContainingStack));
        assertThrows(IllegalArgumentException.class, () ->
                _rasterizedLineSegmentRenderableFactory
                        .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                                THICKNESS_PROVIDER, STIPPLE_PATTERN, STIPPLE_FACTOR, COLOR_PROVIDER,
                                Z, UUID, null));
    }
}
