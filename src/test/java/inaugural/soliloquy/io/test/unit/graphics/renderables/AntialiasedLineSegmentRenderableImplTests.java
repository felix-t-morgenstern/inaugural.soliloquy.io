package inaugural.soliloquy.io.test.unit.graphics.renderables;

import inaugural.soliloquy.io.graphics.renderables.AntialiasedLineSegmentRenderableImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeProviderAtTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.AntialiasedLineSegmentRenderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.ui.Component;

import java.awt.*;
import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.randomInt;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AntialiasedLineSegmentRenderableImplTests {
    private final FakeProviderAtTime<Vertex> VERTEX_1_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Vertex> VERTEX_2_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Float> THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Color> COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Float> THICKNESS_GRADIENT_PERCENT_PROVIDER =
            new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Float> LENGTH_GRADIENT_PERCENT_PROVIDER =
            new FakeProviderAtTime<>();
    private final int Z = randomInt();
    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private Component mockComponent;

    private AntialiasedLineSegmentRenderable renderable;

    @BeforeEach
    public void setUp() {
        mockComponent = mock(Component.class);

        renderable = new AntialiasedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER,
                VERTEX_2_PROVIDER,
                THICKNESS_PROVIDER,
                COLOR_PROVIDER,
                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                LENGTH_GRADIENT_PERCENT_PROVIDER,
                Z,
                UUID,
                mockComponent
        );
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new AntialiasedLineSegmentRenderableImpl(
                null,
                VERTEX_2_PROVIDER,
                THICKNESS_PROVIDER,
                COLOR_PROVIDER,
                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                LENGTH_GRADIENT_PERCENT_PROVIDER,
                Z,
                UUID,
                mockComponent
        ));
        assertThrows(IllegalArgumentException.class, () -> new AntialiasedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER,
                null,
                THICKNESS_PROVIDER,
                COLOR_PROVIDER,
                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                LENGTH_GRADIENT_PERCENT_PROVIDER,
                Z,
                UUID,
                mockComponent
        ));
        assertThrows(IllegalArgumentException.class, () -> new AntialiasedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER,
                VERTEX_2_PROVIDER,
                null,
                COLOR_PROVIDER,
                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                LENGTH_GRADIENT_PERCENT_PROVIDER,
                Z,
                UUID,
                mockComponent
        ));
        assertThrows(IllegalArgumentException.class, () -> new AntialiasedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER,
                VERTEX_2_PROVIDER,
                THICKNESS_PROVIDER,
                null,
                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                LENGTH_GRADIENT_PERCENT_PROVIDER,
                Z,
                UUID,
                mockComponent
        ));
        assertThrows(IllegalArgumentException.class, () -> new AntialiasedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER,
                VERTEX_2_PROVIDER,
                THICKNESS_PROVIDER,
                COLOR_PROVIDER,
                null,
                LENGTH_GRADIENT_PERCENT_PROVIDER,
                Z,
                UUID,
                mockComponent
        ));
        assertThrows(IllegalArgumentException.class, () -> new AntialiasedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER,
                VERTEX_2_PROVIDER,
                THICKNESS_PROVIDER,
                COLOR_PROVIDER,
                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                null,
                Z,
                UUID,
                mockComponent
        ));
        assertThrows(IllegalArgumentException.class, () -> new AntialiasedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER,
                VERTEX_2_PROVIDER,
                THICKNESS_PROVIDER,
                COLOR_PROVIDER,
                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                LENGTH_GRADIENT_PERCENT_PROVIDER,
                Z,
                null,
                mockComponent
        ));
        assertThrows(IllegalArgumentException.class, () -> new AntialiasedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER,
                VERTEX_2_PROVIDER,
                THICKNESS_PROVIDER,
                COLOR_PROVIDER,
                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                LENGTH_GRADIENT_PERCENT_PROVIDER,
                Z,
                UUID,
                null
        ));
    }

    @Test
    public void testSetAndGetVertexProviders() {
        assertSame(VERTEX_1_PROVIDER, renderable.getVertex1Provider());
        assertSame(VERTEX_2_PROVIDER, renderable.getVertex2Provider());

        var newVertex1Provider = new FakeProviderAtTime<Vertex>();
        var newVertex2Provider = new FakeProviderAtTime<Vertex>();

        renderable.setVertex1Provider(newVertex1Provider);
        renderable.setVertex2Provider(newVertex2Provider);

        assertSame(newVertex1Provider, renderable.getVertex1Provider());
        assertSame(newVertex2Provider, renderable.getVertex2Provider());
    }

    @Test
    public void testGetAndSetThicknessProvider() {
        assertSame(THICKNESS_PROVIDER, renderable.getThicknessProvider());

        var newThicknessProvider = new FakeProviderAtTime<Float>();

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
        assertSame(COLOR_PROVIDER, renderable.getColorProvider());

        FakeProviderAtTime<Color> newColorProvider = new FakeProviderAtTime<>();
        renderable.setColorProvider(newColorProvider);

        assertSame(newColorProvider, renderable.getColorProvider());
    }

    @Test
    public void testGetAndSetThicknessGradientPercentProvider() {
        assertSame(THICKNESS_GRADIENT_PERCENT_PROVIDER,
                renderable.getThicknessGradientPercentProvider());

        var newProvider = new FakeProviderAtTime<Float>();

        renderable.setThicknessGradientPercentProvider(newProvider);

        assertSame(newProvider, renderable.getThicknessGradientPercentProvider());
    }

    @Test
    public void testGetAndSetLengthGradientPercentProvider() {
        assertSame(LENGTH_GRADIENT_PERCENT_PROVIDER,
                renderable.getLengthGradientPercentProvider());

        ProviderAtTime<Float> newLengthGradientPercentProvider = new FakeProviderAtTime<>();

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

        int newZ = 456;
        renderable.setZ(newZ);

        assertEquals(newZ, renderable.getZ());
        verify(mockComponent, once()).add(renderable);
    }

    @Test
    public void testComponent() {
        assertSame(mockComponent, renderable.component());
    }

    @Test
    public void testDelete() {
        renderable.delete();

        assertTrue(renderable.isDeleted());
    }
}
