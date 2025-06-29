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
import soliloquy.specs.io.graphics.rendering.RenderableStack;

import java.awt.*;
import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.randomInt;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Mock private RenderableStack mockContainingStack;

    private AntialiasedLineSegmentRenderable antialiasedLineSegmentRenderable;

    @BeforeEach
    public void setUp() {
        mockContainingStack = mock(RenderableStack.class);

        antialiasedLineSegmentRenderable = new AntialiasedLineSegmentRenderableImpl(
                VERTEX_1_PROVIDER,
                VERTEX_2_PROVIDER,
                THICKNESS_PROVIDER,
                COLOR_PROVIDER,
                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                LENGTH_GRADIENT_PERCENT_PROVIDER,
                Z,
                UUID,
                mockContainingStack
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
                mockContainingStack
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
                mockContainingStack
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
                mockContainingStack
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
                mockContainingStack
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
                mockContainingStack
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
                mockContainingStack
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
                mockContainingStack
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
        assertSame(VERTEX_1_PROVIDER, antialiasedLineSegmentRenderable.getVertex1Provider());
        assertSame(VERTEX_2_PROVIDER, antialiasedLineSegmentRenderable.getVertex2Provider());

        var newVertex1Provider = new FakeProviderAtTime<Vertex>();
        var newVertex2Provider = new FakeProviderAtTime<Vertex>();

        antialiasedLineSegmentRenderable.setVertex1Provider(newVertex1Provider);
        antialiasedLineSegmentRenderable.setVertex2Provider(newVertex2Provider);

        assertSame(newVertex1Provider, antialiasedLineSegmentRenderable.getVertex1Provider());
        assertSame(newVertex2Provider, antialiasedLineSegmentRenderable.getVertex2Provider());
    }

    @Test
    public void testGetAndSetThicknessProvider() {
        assertSame(THICKNESS_PROVIDER, antialiasedLineSegmentRenderable.getThicknessProvider());

        var newThicknessProvider = new FakeProviderAtTime<Float>();

        antialiasedLineSegmentRenderable.setThicknessProvider(newThicknessProvider);

        assertSame(newThicknessProvider, antialiasedLineSegmentRenderable.getThicknessProvider());
    }

    @Test
    public void testSetThicknessProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                antialiasedLineSegmentRenderable.setThicknessProvider(null));
    }

    @Test
    public void testGetAndSetColorProvider() {
        assertSame(COLOR_PROVIDER, antialiasedLineSegmentRenderable.getColorProvider());

        FakeProviderAtTime<Color> newColorProvider = new FakeProviderAtTime<>();
        antialiasedLineSegmentRenderable.setColorProvider(newColorProvider);

        assertSame(newColorProvider, antialiasedLineSegmentRenderable.getColorProvider());
    }

    @Test
    public void testGetAndSetThicknessGradientPercentProvider() {
        assertSame(THICKNESS_GRADIENT_PERCENT_PROVIDER,
                antialiasedLineSegmentRenderable.getThicknessGradientPercentProvider());

        ProviderAtTime<Float> newThicknessGradientPercentProvider = new FakeProviderAtTime<>();

        antialiasedLineSegmentRenderable
                .setThicknessGradientPercentProvider(newThicknessGradientPercentProvider);

        assertSame(newThicknessGradientPercentProvider,
                antialiasedLineSegmentRenderable.getThicknessGradientPercentProvider());
    }

    @Test
    public void testGetAndSetLengthGradientPercentProvider() {
        assertSame(LENGTH_GRADIENT_PERCENT_PROVIDER,
                antialiasedLineSegmentRenderable.getLengthGradientPercentProvider());

        ProviderAtTime<Float> newLengthGradientPercentProvider = new FakeProviderAtTime<>();

        antialiasedLineSegmentRenderable
                .setLengthGradientPercentProvider(newLengthGradientPercentProvider);

        assertSame(newLengthGradientPercentProvider,
                antialiasedLineSegmentRenderable.getLengthGradientPercentProvider());
    }

    @Test
    public void testSetColorProviderWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                antialiasedLineSegmentRenderable.setColorProvider(null));
    }

    @Test
    public void testSetVertexProvidersWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () ->
                antialiasedLineSegmentRenderable.setVertex1Provider(null));
        assertThrows(IllegalArgumentException.class, () ->
                antialiasedLineSegmentRenderable.setVertex2Provider(null));
    }

    @Test
    public void testGetAndSetZ() {
        assertEquals(Z, antialiasedLineSegmentRenderable.getZ());

        int newZ = 456;
        antialiasedLineSegmentRenderable.setZ(newZ);

        assertEquals(newZ, antialiasedLineSegmentRenderable.getZ());
        verify(mockContainingStack, once()).add(antialiasedLineSegmentRenderable);
    }

    @Test
    public void testDelete() {
        antialiasedLineSegmentRenderable.delete();

        verify(mockContainingStack, once()).remove(antialiasedLineSegmentRenderable);
    }
}
