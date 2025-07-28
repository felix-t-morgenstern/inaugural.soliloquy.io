package inaugural.soliloquy.io.test.unit.graphics.renderables;

import inaugural.soliloquy.io.graphics.renderables.RasterizedLineSegmentRenderableImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeProviderAtTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.ui.Component;

import java.awt.*;
import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.randomInt;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RasterizedLineSegmentRenderableImplTests {
    private final ProviderAtTime<Float> THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final short STIPPLE_PATTERN = 456;
    private final short STIPPLE_FACTOR = 123;
    private final ProviderAtTime<Color> COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Vertex> VERTEX_1_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Vertex> VERTEX_2_PROVIDER = new FakeProviderAtTime<>();
    private final int Z = randomInt();

    private static final UUID UUID = java.util.UUID.randomUUID();

    @Mock private Component mockContainingComponent;

    private RasterizedLineSegmentRenderable renderable;

    @BeforeEach
    public void setUp() {
        renderable = new RasterizedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, THICKNESS_PROVIDER, STIPPLE_PATTERN,
                STIPPLE_FACTOR, COLOR_PROVIDER, Z, UUID, mockContainingComponent);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                null, VERTEX_2_PROVIDER, THICKNESS_PROVIDER, STIPPLE_PATTERN,
                STIPPLE_FACTOR, COLOR_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER, null, THICKNESS_PROVIDER, STIPPLE_PATTERN,
                STIPPLE_FACTOR, COLOR_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, null, STIPPLE_PATTERN,
                STIPPLE_FACTOR, COLOR_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, THICKNESS_PROVIDER,
                (short) 0, STIPPLE_FACTOR, COLOR_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, THICKNESS_PROVIDER,
                STIPPLE_PATTERN, (short) 0, COLOR_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, THICKNESS_PROVIDER,
                STIPPLE_PATTERN, (short) 257, COLOR_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, THICKNESS_PROVIDER,
                STIPPLE_PATTERN, STIPPLE_FACTOR, null, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, THICKNESS_PROVIDER,
                STIPPLE_PATTERN, STIPPLE_FACTOR, COLOR_PROVIDER, Z, null, mockContainingComponent));
    }

    @Test
    public void testGetAndSetThicknessProvider() {
        assertSame(THICKNESS_PROVIDER, renderable.getThicknessProvider());

        FakeProviderAtTime<Float> newThicknessProvider = new FakeProviderAtTime<>();

        renderable.setThicknessProvider(newThicknessProvider);

        assertSame(newThicknessProvider, renderable.getThicknessProvider());
    }

    @Test
    public void testSetThicknessProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setThicknessProvider(null));
    }

    @Test
    public void testGetAndSetStipplePattern() {
        assertEquals(STIPPLE_PATTERN, renderable.getStipplePattern());

        short newStipplePattern = 789;
        renderable.setStipplePattern(newStipplePattern);

        assertEquals(newStipplePattern, renderable.getStipplePattern());
    }

    @Test
    public void testSetStipplePatternWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setStipplePattern((short) 0));
    }

    @Test
    public void testGetAndSetStippleFactor() {
        assertEquals(STIPPLE_FACTOR, renderable.getStippleFactor());

        short newStippleFactor = 234;
        renderable.setStippleFactor(newStippleFactor);

        assertEquals(newStippleFactor, renderable.getStippleFactor());
    }

    @Test
    public void testSetStippleFactorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setStippleFactor((short) 0));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setStippleFactor((short) 257));
    }

    @Test
    public void testGetAndSetColorProvider() {
        assertSame(COLOR_PROVIDER, renderable.getColorProvider());

        FakeProviderAtTime<Color> newColorProvider = new FakeProviderAtTime<>();
        renderable.setColorProvider(newColorProvider);

        assertSame(newColorProvider, renderable.getColorProvider());
    }

    @Test
    public void testSetColorProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setColorProvider(null));
    }

    @Test
    public void testSetAndGetVertexProviders() {
        assertSame(VERTEX_1_PROVIDER, renderable.getVertex1Provider());
        assertSame(VERTEX_2_PROVIDER, renderable.getVertex2Provider());

        FakeProviderAtTime<Vertex> newVertex1Provider =
                new FakeProviderAtTime<>();
        FakeProviderAtTime<Vertex> newVertex2Provider =
                new FakeProviderAtTime<>();
        renderable.setVertex1Provider(newVertex1Provider);
        renderable.setVertex2Provider(newVertex2Provider);

        assertSame(newVertex1Provider, renderable.getVertex1Provider());
        assertSame(newVertex2Provider, renderable.getVertex2Provider());
    }

    @Test
    public void testSetVertexProvidersWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setVertex1Provider(null));
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setVertex2Provider(null));
    }

    @Test
    public void testGetAndSetZ() {
        assertEquals(Z, renderable.getZ());

        int newZ = 456;
        renderable.setZ(newZ);

        assertEquals(newZ, renderable.getZ());
        verify(mockContainingComponent, once()).add(renderable);
    }

    @Test
    public void testDelete() {
        renderable.delete();

        assertNull(renderable.component());
        assertTrue(renderable.isDeleted());
    }

    @Test
    public void testUuid() {
        assertSame(UUID, renderable.uuid());
    }

    @Test
    public void testComponent() {
        assertSame(mockContainingComponent, renderable.component());
    }

    @Test
    public void testSetComponent() {
        ((RasterizedLineSegmentRenderableImpl) renderable).setComponent(null);

        assertNull(renderable.component());
    }
}
