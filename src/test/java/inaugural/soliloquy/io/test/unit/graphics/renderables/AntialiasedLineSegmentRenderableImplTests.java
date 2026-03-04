package inaugural.soliloquy.io.test.unit.graphics.renderables;

import inaugural.soliloquy.io.graphics.renderables.AntialiasedLineSegmentRenderableImpl;
import inaugural.soliloquy.io.graphics.renderables.ComponentImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.AntialiasedLineSegmentRenderable;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.randomInt;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AntialiasedLineSegmentRenderableImplTests {
    private final int Z = randomInt();
    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private ProviderAtTime<Vertex> mockVertex1Provider;
    @Mock private ProviderAtTime<Vertex> mockVertex2Provider;
    @Mock private ProviderAtTime<Float> mockThicknessProvider;
    @Mock private ProviderAtTime<Color> mockColorProvider;
    @Mock private ProviderAtTime<Float> mockThicknessGradientPercentProvider;
    @Mock private ProviderAtTime<Float> mockLengthGradientPercentProvider;

    @Mock private Component mockComponent;

    private AntialiasedLineSegmentRenderable renderable;

    @BeforeEach
    public void setUp() {
        mockComponent = mock(ComponentImpl.class);

        renderable = new AntialiasedLineSegmentRenderableImpl(
                mockVertex1Provider,
                mockVertex2Provider,
                mockColorProvider,
                mockThicknessProvider,
                mockThicknessGradientPercentProvider,
                mockLengthGradientPercentProvider,
                Z,
                UUID,
                mockComponent
        );
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new AntialiasedLineSegmentRenderableImpl(
                null,
                mockVertex2Provider,
                mockColorProvider,
                mockThicknessProvider,
                mockThicknessGradientPercentProvider,
                mockLengthGradientPercentProvider,
                Z,
                UUID,
                mockComponent
        ));
        assertThrows(IllegalArgumentException.class, () -> new AntialiasedLineSegmentRenderableImpl(
                mockVertex1Provider,
                null,
                mockColorProvider,
                mockThicknessProvider,
                mockThicknessGradientPercentProvider,
                mockLengthGradientPercentProvider,
                Z,
                UUID,
                mockComponent
        ));
        assertThrows(IllegalArgumentException.class, () -> new AntialiasedLineSegmentRenderableImpl(
                mockVertex1Provider,
                mockVertex2Provider,
                null,
                mockThicknessProvider,
                mockThicknessGradientPercentProvider,
                mockLengthGradientPercentProvider,
                Z,
                UUID,
                mockComponent
        ));
        assertThrows(IllegalArgumentException.class, () -> new AntialiasedLineSegmentRenderableImpl(
                mockVertex1Provider,
                mockVertex2Provider,
                mockColorProvider,
                null,
                mockThicknessGradientPercentProvider,
                mockLengthGradientPercentProvider,
                Z,
                UUID,
                mockComponent
        ));
        assertThrows(IllegalArgumentException.class, () -> new AntialiasedLineSegmentRenderableImpl(
                mockVertex1Provider,
                mockVertex2Provider,
                mockColorProvider,
                mockThicknessProvider,
                null,
                mockLengthGradientPercentProvider,
                Z,
                UUID,
                mockComponent
        ));
        assertThrows(IllegalArgumentException.class, () -> new AntialiasedLineSegmentRenderableImpl(
                mockVertex1Provider,
                mockVertex2Provider,
                mockColorProvider,
                mockThicknessProvider,
                mockThicknessGradientPercentProvider,
                null,
                Z,
                UUID,
                mockComponent
        ));
        assertThrows(IllegalArgumentException.class, () -> new AntialiasedLineSegmentRenderableImpl(
                mockVertex1Provider,
                mockVertex2Provider,
                mockColorProvider,
                mockThicknessProvider,
                mockThicknessGradientPercentProvider,
                mockLengthGradientPercentProvider,
                Z,
                null,
                mockComponent
        ));
    }

    @Test
    public void testConstructorDoesNotAddSelfToContainingComponent() {
        verify(mockComponent, never()).add(renderable);
    }

    @Test
    public void testSetAndGetVertexProviders() {
        assertSame(mockVertex1Provider, renderable.getVertex1Provider());
        assertSame(mockVertex2Provider, renderable.getVertex2Provider());

        @SuppressWarnings("unchecked") var newVertex1Provider =
                (ProviderAtTime<Vertex>) mock(ProviderAtTime.class);
        @SuppressWarnings("unchecked") var newVertex2Provider =
                (ProviderAtTime<Vertex>) mock(ProviderAtTime.class);

        renderable.setVertex1Provider(newVertex1Provider);
        renderable.setVertex2Provider(newVertex2Provider);

        assertSame(newVertex1Provider, renderable.getVertex1Provider());
        assertSame(newVertex2Provider, renderable.getVertex2Provider());
    }

    @Test
    public void testGetAndSetThicknessProvider() {
        assertSame(mockThicknessProvider, renderable.getThicknessProvider());

        @SuppressWarnings("unchecked") var newThicknessProvider =
                (ProviderAtTime<Float>) mock(ProviderAtTime.class);

        renderable.setThicknessProvider(newThicknessProvider);

        assertSame(newThicknessProvider, renderable.getThicknessProvider());
    }

    @Test
    public void testSetThicknessProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setThicknessProvider(null));
    }

    @Test
    public void testGetAndSetColorProvider() {
        assertSame(mockColorProvider, renderable.getColorProvider());

        @SuppressWarnings("unchecked") var newColorProvider =
                (ProviderAtTime<Color>) mock(ProviderAtTime.class);

        renderable.setColorProvider(newColorProvider);

        assertSame(newColorProvider, renderable.getColorProvider());
    }

    @Test
    public void testGetAndSetThicknessGradientPercentProvider() {
        assertSame(mockThicknessGradientPercentProvider,
                renderable.getThicknessGradientPercentProvider());

        @SuppressWarnings("unchecked") var newProvider =
                (ProviderAtTime<Float>) mock(ProviderAtTime.class);

        renderable.setThicknessGradientPercentProvider(newProvider);

        assertSame(newProvider, renderable.getThicknessGradientPercentProvider());
    }

    @Test
    public void testGetAndSetLengthGradientPercentProvider() {
        assertSame(mockLengthGradientPercentProvider,
                renderable.getLengthGradientPercentProvider());

        @SuppressWarnings("unchecked") var newLengthGradientPercentProvider =
                (ProviderAtTime<Float>) mock(ProviderAtTime.class);

        renderable
                .setLengthGradientPercentProvider(newLengthGradientPercentProvider);

        assertSame(newLengthGradientPercentProvider,
                renderable.getLengthGradientPercentProvider());
    }

    @Test
    public void testSetColorProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                renderable.setColorProvider(null));
    }

    @Test
    public void testSetVertexProvidersWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> renderable.setVertex1Provider(null));
        assertThrows(IllegalArgumentException.class, () -> renderable.setVertex2Provider(null));
    }

    @Test
    public void testGetAndSetZ() {
        assertEquals(Z, renderable.getZ());

        var newZ = randomInt();
        renderable.setZ(newZ);

        assertEquals(newZ, renderable.getZ());
    }

    @Test
    public void testComponent() {
        assertSame(mockComponent, renderable.containingComponent());
    }

    @Test
    public void testSetComponent() {
        ((AntialiasedLineSegmentRenderableImpl) renderable).setContainingComponent(null);

        assertNull(renderable.containingComponent());
    }

    @Test
    public void testDelete() {
        renderable.delete();

        assertTrue(renderable.isDeleted());
        verify(mockComponent, once()).remove(renderable);
    }
}
