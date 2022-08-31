package inaugural.soliloquy.graphics.test.unit.renderables;

import inaugural.soliloquy.graphics.renderables.RasterizedLineSegmentRenderableImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeProviderAtTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.awt.*;
import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.randomInt;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RasterizedLineSegmentRenderableImplTests {
    private final ProviderAtTime<Float> THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final short STIPPLE_PATTERN = 456;
    private final short STIPPLE_FACTOR = 123;
    private final ProviderAtTime<Color> COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Vertex> VERTEX_1_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Vertex> VERTEX_2_PROVIDER = new FakeProviderAtTime<>();
    private final int Z = randomInt();

    private static final UUID UUID = java.util.UUID.randomUUID();

    @Mock private RenderableStack _mockContainingStack;

    private RasterizedLineSegmentRenderable _rasterizedLineSegmentRenderable;

    @BeforeEach
    void setUp() {
        _mockContainingStack = mock(RenderableStack.class);

        _rasterizedLineSegmentRenderable = new RasterizedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, THICKNESS_PROVIDER, STIPPLE_PATTERN,
                STIPPLE_FACTOR, COLOR_PROVIDER, Z, UUID, _mockContainingStack);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                null, VERTEX_2_PROVIDER, THICKNESS_PROVIDER, STIPPLE_PATTERN,
                STIPPLE_FACTOR, COLOR_PROVIDER, Z, UUID, _mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER, null, THICKNESS_PROVIDER, STIPPLE_PATTERN,
                STIPPLE_FACTOR, COLOR_PROVIDER, Z, UUID, _mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, null, STIPPLE_PATTERN,
                STIPPLE_FACTOR, COLOR_PROVIDER, Z, UUID, _mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, THICKNESS_PROVIDER,
                (short) 0, STIPPLE_FACTOR, COLOR_PROVIDER, Z, UUID, _mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, THICKNESS_PROVIDER,
                STIPPLE_PATTERN, (short) 0, COLOR_PROVIDER, Z, UUID, _mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, THICKNESS_PROVIDER,
                STIPPLE_PATTERN, (short) 257, COLOR_PROVIDER, Z, UUID, _mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, THICKNESS_PROVIDER,
                STIPPLE_PATTERN, STIPPLE_FACTOR, null, Z, UUID, _mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, THICKNESS_PROVIDER,
                STIPPLE_PATTERN, STIPPLE_FACTOR, COLOR_PROVIDER, Z, null, _mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, THICKNESS_PROVIDER,
                STIPPLE_PATTERN, STIPPLE_FACTOR, COLOR_PROVIDER, Z, UUID, null
        ));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(RasterizedLineSegmentRenderable.class.getCanonicalName(),
                _rasterizedLineSegmentRenderable.getInterfaceName());
    }

    @Test
    void testGetAndSetThicknessProvider() {
        assertSame(THICKNESS_PROVIDER, _rasterizedLineSegmentRenderable.getThicknessProvider());

        FakeProviderAtTime<Float> newThicknessProvider = new FakeProviderAtTime<>();

        _rasterizedLineSegmentRenderable.setThicknessProvider(newThicknessProvider);

        assertSame(newThicknessProvider, _rasterizedLineSegmentRenderable.getThicknessProvider());
    }

    @Test
    void testSetThicknessProviderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _rasterizedLineSegmentRenderable.setThicknessProvider(null));
    }

    @Test
    void testGetAndSetStipplePattern() {
        assertEquals(STIPPLE_PATTERN, _rasterizedLineSegmentRenderable.getStipplePattern());

        short newStipplePattern = 789;
        _rasterizedLineSegmentRenderable.setStipplePattern(newStipplePattern);

        assertEquals(newStipplePattern, _rasterizedLineSegmentRenderable.getStipplePattern());
    }

    @Test
    void testSetStipplePatternWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _rasterizedLineSegmentRenderable.setStipplePattern((short) 0));
    }

    @Test
    void testGetAndSetStippleFactor() {
        assertEquals(STIPPLE_FACTOR, _rasterizedLineSegmentRenderable.getStippleFactor());

        short newStippleFactor = 234;
        _rasterizedLineSegmentRenderable.setStippleFactor(newStippleFactor);

        assertEquals(newStippleFactor, _rasterizedLineSegmentRenderable.getStippleFactor());
    }

    @Test
    void testSetStippleFactorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _rasterizedLineSegmentRenderable.setStippleFactor((short) 0));
        assertThrows(IllegalArgumentException.class, () ->
                _rasterizedLineSegmentRenderable.setStippleFactor((short) 257));
    }

    @Test
    void testGetAndSetColorProvider() {
        assertSame(COLOR_PROVIDER, _rasterizedLineSegmentRenderable.getColorProvider());

        FakeProviderAtTime<Color> newColorProvider = new FakeProviderAtTime<>();
        _rasterizedLineSegmentRenderable.setColorProvider(newColorProvider);

        assertSame(newColorProvider, _rasterizedLineSegmentRenderable.getColorProvider());
    }

    @Test
    void testSetColorProviderWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _rasterizedLineSegmentRenderable.setColorProvider(null));
    }

    @Test
    void testSetAndGetVertexProviders() {
        assertSame(VERTEX_1_PROVIDER, _rasterizedLineSegmentRenderable.getVertex1Provider());
        assertSame(VERTEX_2_PROVIDER, _rasterizedLineSegmentRenderable.getVertex2Provider());

        FakeProviderAtTime<Vertex> newVertex1Provider =
                new FakeProviderAtTime<>();
        FakeProviderAtTime<Vertex> newVertex2Provider =
                new FakeProviderAtTime<>();
        _rasterizedLineSegmentRenderable.setVertex1Provider(newVertex1Provider);
        _rasterizedLineSegmentRenderable.setVertex2Provider(newVertex2Provider);

        assertSame(newVertex1Provider, _rasterizedLineSegmentRenderable.getVertex1Provider());
        assertSame(newVertex2Provider, _rasterizedLineSegmentRenderable.getVertex2Provider());
    }

    @Test
    void testSetVertexProvidersWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () ->
                _rasterizedLineSegmentRenderable.setVertex1Provider(null));
        assertThrows(IllegalArgumentException.class, () ->
                _rasterizedLineSegmentRenderable.setVertex2Provider(null));
    }

    @Test
    void testGetAndSetZ() {
        assertEquals(Z, _rasterizedLineSegmentRenderable.getZ());

        int newZ = 456;
        _rasterizedLineSegmentRenderable.setZ(newZ);

        assertEquals(newZ, _rasterizedLineSegmentRenderable.getZ());
        verify(_mockContainingStack, times(1)).add(_rasterizedLineSegmentRenderable);
    }

    @Test
    void testDelete() {
        _rasterizedLineSegmentRenderable.delete();

        verify(_mockContainingStack, times(1)).remove(_rasterizedLineSegmentRenderable);
    }

    @Test
    void testUuid() {
        assertSame(UUID, _rasterizedLineSegmentRenderable.uuid());
    }
}
