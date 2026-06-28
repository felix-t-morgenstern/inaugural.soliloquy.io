package inaugural.soliloquy.io.test.unit.graphics.renderables;

import inaugural.soliloquy.io.graphics.renderables.RasterizedLineSegmentRenderableImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.randomInt;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RasterizedLineSegmentRenderableImplTests {
    private final short STIPPLE_PATTERN = 456;
    private final short STIPPLE_FACTOR = 123;
    private final int Z = randomInt();
    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private ProviderAtTime<Float> mockThicknessProvider;
    @Mock private ProviderAtTime<Color> mockColorProvider;
    @Mock private ProviderAtTime<Vertex> mockVertex1Provider;
    @Mock private ProviderAtTime<Vertex> mockVertex2Provider;

    @Mock private Component mockContainingComponent;

    private RasterizedLineSegmentRenderable renderable;

    @BeforeEach
    public void setUp() {
        renderable = new RasterizedLineSegmentRenderableImpl(
                mockVertex1Provider, mockVertex2Provider, mockThicknessProvider, STIPPLE_PATTERN,
                STIPPLE_FACTOR, mockColorProvider, Z, UUID, mockContainingComponent);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                null, mockVertex2Provider, mockThicknessProvider, STIPPLE_PATTERN,
                STIPPLE_FACTOR, mockColorProvider, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                mockVertex1Provider, null, mockThicknessProvider, STIPPLE_PATTERN,
                STIPPLE_FACTOR, mockColorProvider, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                mockVertex1Provider, mockVertex2Provider, null, STIPPLE_PATTERN,
                STIPPLE_FACTOR, mockColorProvider, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                mockVertex1Provider, mockVertex2Provider, mockThicknessProvider,
                (short) 0, STIPPLE_FACTOR, mockColorProvider, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                mockVertex1Provider, mockVertex2Provider, mockThicknessProvider,
                STIPPLE_PATTERN, (short) 0, mockColorProvider, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                mockVertex1Provider, mockVertex2Provider, mockThicknessProvider,
                STIPPLE_PATTERN, (short) 257, mockColorProvider, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                mockVertex1Provider, mockVertex2Provider, mockThicknessProvider,
                STIPPLE_PATTERN, STIPPLE_FACTOR, null, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> new RasterizedLineSegmentRenderableImpl(
                mockVertex1Provider, mockVertex2Provider, mockThicknessProvider,
                STIPPLE_PATTERN, STIPPLE_FACTOR, mockColorProvider, Z, null, mockContainingComponent));
    }

    @Test
    public void testConstructorDoesNotAddSelfToContainingComponent() {
        verify(mockContainingComponent, never()).add(renderable);
    }

    @Test
    public void testGetAndSetThicknessProvider() {
        assertSame(mockThicknessProvider, renderable.getThicknessProvider());

        @SuppressWarnings("unchecked") var newThicknessProvider = (ProviderAtTime<Float>) mock(ProviderAtTime.class);

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
        assertSame(mockColorProvider, renderable.getColorProvider());

        @SuppressWarnings("unchecked") var newColorProvider = (ProviderAtTime<Color>) mock(ProviderAtTime.class);

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
        assertSame(mockVertex1Provider, renderable.getVertex1Provider());
        assertSame(mockVertex2Provider, renderable.getVertex2Provider());

        @SuppressWarnings("unchecked") var newVertex1Provider = (ProviderAtTime<Vertex>) mock(ProviderAtTime.class);
        @SuppressWarnings("unchecked") var newVertex2Provider = (ProviderAtTime<Vertex>) mock(ProviderAtTime.class);

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

        var newZ = randomInt();
        renderable.setZ(newZ);

        assertEquals(newZ, renderable.getZ());
    }

    @Test
    public void testDelete() {
        renderable.delete();

        assertTrue(renderable.isDeleted());
        verify(mockContainingComponent, once()).remove(renderable);
    }

    @Test
    public void testUuid() {
        assertSame(UUID, renderable.uuid());
    }

    @Test
    public void testComponent() {
        assertSame(mockContainingComponent, renderable.getContainingComponent());
    }

    @Test
    public void testSetComponent() {
        ((RasterizedLineSegmentRenderableImpl) renderable).setContainingComponent(null);

        assertNull(renderable.getContainingComponent());
    }
}
