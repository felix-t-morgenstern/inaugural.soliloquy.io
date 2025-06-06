package inaugural.soliloquy.graphics.test.unit.renderables.factories;

import inaugural.soliloquy.graphics.renderables.AntialiasedLineSegmentRenderableImpl;
import inaugural.soliloquy.graphics.renderables.factories.AntialiasedLineSegmentRenderableFactoryImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeProviderAtTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.factories.AntialiasedLineSegmentRenderableFactory;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.awt.*;

import static inaugural.soliloquy.tools.random.Random.randomInt;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AntialiasedLineSegmentFactoryImplTests {
    private final FakeProviderAtTime<Vertex> VERTEX_1_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Vertex> VERTEX_2_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Float> THICKNESS_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Color> COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Float> THICKNESS_GRADIENT_PERCENT_PROVIDER =
            new FakeProviderAtTime<>();
    private final FakeProviderAtTime<Float> LENGTH_GRADIENT_PERCENT_PROVIDER =
            new FakeProviderAtTime<>();
    private final int Z = randomInt();
    private final java.util.UUID UUID = java.util.UUID.randomUUID();

    @Mock private RenderableStack mockContainingStack;

    private AntialiasedLineSegmentRenderableFactory antialiasedLineSegmentRenderableFactory;

    @BeforeEach
    public void setUp() {
        mockContainingStack = mock(RenderableStack.class);

        antialiasedLineSegmentRenderableFactory =
                new AntialiasedLineSegmentRenderableFactoryImpl();
    }

    @Test
    public void testMake() {
        var antialiasedLineSegmentRenderable =
                antialiasedLineSegmentRenderableFactory.make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                        THICKNESS_PROVIDER, COLOR_PROVIDER, THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID, mockContainingStack);

        assertNotNull(antialiasedLineSegmentRenderable);
        assertInstanceOf(AntialiasedLineSegmentRenderableImpl.class,
                antialiasedLineSegmentRenderable);

        var newZ = 456;
        antialiasedLineSegmentRenderable.setZ(newZ);

        assertEquals(newZ, antialiasedLineSegmentRenderable.getZ());
        verify(mockContainingStack, once()).add(antialiasedLineSegmentRenderable);

        antialiasedLineSegmentRenderable.delete();

        verify(mockContainingStack, once()).remove(antialiasedLineSegmentRenderable);
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderableFactory
                .make(null, VERTEX_2_PROVIDER,
                        THICKNESS_PROVIDER, COLOR_PROVIDER,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID, mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderableFactory
                .make(VERTEX_1_PROVIDER, null,
                        THICKNESS_PROVIDER, COLOR_PROVIDER,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID, mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                        null, COLOR_PROVIDER,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID, mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                        THICKNESS_PROVIDER, null,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID, mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                        THICKNESS_PROVIDER, COLOR_PROVIDER,
                        null,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID, mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                        THICKNESS_PROVIDER, COLOR_PROVIDER,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        null, Z, UUID, mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                        THICKNESS_PROVIDER, COLOR_PROVIDER,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, null, mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                        THICKNESS_PROVIDER, COLOR_PROVIDER,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID, null));
    }
}
