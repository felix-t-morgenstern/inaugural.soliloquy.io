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
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RasterizedLineSegmentRenderableImplTests {
    private final ProviderAtTime<Float> THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final short STIPPLE_PATTERN = 456;
    private final short STIPPLE_FACTOR = 123;
    private final ProviderAtTime<Color> COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Vertex> VERTEX_1_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Vertex> VERTEX_2_PROVIDER = new FakeProviderAtTime<>();
    private final int Z = randomInt();

    private static final UUID UUID = java.util.UUID.randomUUID();

    @Mock private RenderableStack mockContainingStack;

    private RasterizedLineSegmentRenderable rasterizedLineSegmentRenderable;

    @BeforeEach
    public void setUp() {
        mockContainingStack = mock(RenderableStack.class);

        rasterizedLineSegmentRenderable = new RasterizedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, THICKNESS_PROVIDER, STIPPLE_PATTERN,
                STIPPLE_FACTOR, COLOR_PROVIDER, Z, UUID, mockContainingStack);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                null, VERTEX_2_PROVIDER, THICKNESS_PROVIDER, STIPPLE_PATTERN,
                STIPPLE_FACTOR, COLOR_PROVIDER, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER, null, THICKNESS_PROVIDER, STIPPLE_PATTERN,
                STIPPLE_FACTOR, COLOR_PROVIDER, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, null, STIPPLE_PATTERN,
                STIPPLE_FACTOR, COLOR_PROVIDER, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, THICKNESS_PROVIDER,
                (short) 0, STIPPLE_FACTOR, COLOR_PROVIDER, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, THICKNESS_PROVIDER,
                STIPPLE_PATTERN, (short) 0, COLOR_PROVIDER, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, THICKNESS_PROVIDER,
                STIPPLE_PATTERN, (short) 257, COLOR_PROVIDER, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, THICKNESS_PROVIDER,
                STIPPLE_PATTERN, STIPPLE_FACTOR, null, Z, UUID, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, THICKNESS_PROVIDER,
                STIPPLE_PATTERN, STIPPLE_FACTOR, COLOR_PROVIDER, Z, null, mockContainingStack
        ));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, THICKNESS_PROVIDER,
                STIPPLE_PATTERN, STIPPLE_FACTOR, COLOR_PROVIDER, Z, UUID, null
        ));
    }

    @Test
    public void testGetAndSetThicknessProvider() {
        assertSame(THICKNESS_PROVIDER, rasterizedLineSegmentRenderable.getThicknessProvider());

        FakeProviderAtTime<Float> newThicknessProvider = new FakeProviderAtTime<>();

        rasterizedLineSegmentRenderable.setThicknessProvider(newThicknessProvider);

        assertSame(newThicknessProvider, rasterizedLineSegmentRenderable.getThicknessProvider());
    }

    @Test
    public void testSetThicknessProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                rasterizedLineSegmentRenderable.setThicknessProvider(null));
    }

    @Test
    public void testGetAndSetStipplePattern() {
        assertEquals(STIPPLE_PATTERN, rasterizedLineSegmentRenderable.getStipplePattern());

        short newStipplePattern = 789;
        rasterizedLineSegmentRenderable.setStipplePattern(newStipplePattern);

        assertEquals(newStipplePattern, rasterizedLineSegmentRenderable.getStipplePattern());
    }

    @Test
    public void testSetStipplePatternWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                rasterizedLineSegmentRenderable.setStipplePattern((short) 0));
    }

    @Test
    public void testGetAndSetStippleFactor() {
        assertEquals(STIPPLE_FACTOR, rasterizedLineSegmentRenderable.getStippleFactor());

        short newStippleFactor = 234;
        rasterizedLineSegmentRenderable.setStippleFactor(newStippleFactor);

        assertEquals(newStippleFactor, rasterizedLineSegmentRenderable.getStippleFactor());
    }

    @Test
    public void testSetStippleFactorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                rasterizedLineSegmentRenderable.setStippleFactor((short) 0));
        assertThrows(IllegalArgumentException.class, () ->
                rasterizedLineSegmentRenderable.setStippleFactor((short) 257));
    }

    @Test
    public void testGetAndSetColorProvider() {
        assertSame(COLOR_PROVIDER, rasterizedLineSegmentRenderable.getColorProvider());

        FakeProviderAtTime<Color> newColorProvider = new FakeProviderAtTime<>();
        rasterizedLineSegmentRenderable.setColorProvider(newColorProvider);

        assertSame(newColorProvider, rasterizedLineSegmentRenderable.getColorProvider());
    }

    @Test
    public void testSetColorProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                rasterizedLineSegmentRenderable.setColorProvider(null));
    }

    @Test
    public void testSetAndGetVertexProviders() {
        assertSame(VERTEX_1_PROVIDER, rasterizedLineSegmentRenderable.getVertex1Provider());
        assertSame(VERTEX_2_PROVIDER, rasterizedLineSegmentRenderable.getVertex2Provider());

        FakeProviderAtTime<Vertex> newVertex1Provider =
                new FakeProviderAtTime<>();
        FakeProviderAtTime<Vertex> newVertex2Provider =
                new FakeProviderAtTime<>();
        rasterizedLineSegmentRenderable.setVertex1Provider(newVertex1Provider);
        rasterizedLineSegmentRenderable.setVertex2Provider(newVertex2Provider);

        assertSame(newVertex1Provider, rasterizedLineSegmentRenderable.getVertex1Provider());
        assertSame(newVertex2Provider, rasterizedLineSegmentRenderable.getVertex2Provider());
    }

    @Test
    public void testSetVertexProvidersWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                rasterizedLineSegmentRenderable.setVertex1Provider(null));
        assertThrows(IllegalArgumentException.class, () ->
                rasterizedLineSegmentRenderable.setVertex2Provider(null));
    }

    @Test
    public void testGetAndSetZ() {
        assertEquals(Z, rasterizedLineSegmentRenderable.getZ());

        int newZ = 456;
        rasterizedLineSegmentRenderable.setZ(newZ);

        assertEquals(newZ, rasterizedLineSegmentRenderable.getZ());
        verify(mockContainingStack, once()).add(rasterizedLineSegmentRenderable);
    }

    @Test
    public void testDelete() {
        rasterizedLineSegmentRenderable.delete();

        verify(mockContainingStack, once()).remove(rasterizedLineSegmentRenderable);
    }

    @Test
    public void testUuid() {
        assertSame(UUID, rasterizedLineSegmentRenderable.uuid());
    }
}
