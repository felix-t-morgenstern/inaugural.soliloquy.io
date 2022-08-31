package inaugural.soliloquy.graphics.test.unit.renderables.factories;

import inaugural.soliloquy.graphics.renderables.AntialiasedLineSegmentRenderableImpl;
import inaugural.soliloquy.graphics.renderables.factories.AntialiasedLineSegmentRenderableFactoryImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeProviderAtTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.renderables.AntialiasedLineSegmentRenderable;
import soliloquy.specs.graphics.renderables.factories.AntialiasedLineSegmentRenderableFactory;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.awt.*;

import static inaugural.soliloquy.tools.random.Random.randomInt;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AntialiasedLineSegmentFactoryImplTests {
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

    @Mock private RenderableStack _mockContainingStack;

    private AntialiasedLineSegmentRenderableFactory _antialiasedLineSegmentRenderableFactory;

    @BeforeEach
    void setUp() {
        _mockContainingStack = mock(RenderableStack.class);

        _antialiasedLineSegmentRenderableFactory =
                new AntialiasedLineSegmentRenderableFactoryImpl();
    }

    @Test
    void testMake() {
        AntialiasedLineSegmentRenderable antialiasedLineSegmentRenderable =
                _antialiasedLineSegmentRenderableFactory
                        .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                                THICKNESS_PROVIDER, COLOR_PROVIDER,
                                THICKNESS_GRADIENT_PERCENT_PROVIDER,
                                LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID, _mockContainingStack);

        assertNotNull(antialiasedLineSegmentRenderable);
        assertTrue(
                antialiasedLineSegmentRenderable instanceof AntialiasedLineSegmentRenderableImpl);

        int newZ = 456;
        antialiasedLineSegmentRenderable.setZ(newZ);

        assertEquals(newZ, antialiasedLineSegmentRenderable.getZ());
        verify(_mockContainingStack, times(1)).add(antialiasedLineSegmentRenderable);

        antialiasedLineSegmentRenderable.delete();

        verify(_mockContainingStack, times(1)).remove(antialiasedLineSegmentRenderable);
    }

    @Test
    void testMakeWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderableFactory
                .make(null, VERTEX_2_PROVIDER,
                        THICKNESS_PROVIDER, COLOR_PROVIDER,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID, _mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderableFactory
                .make(VERTEX_1_PROVIDER, null,
                        THICKNESS_PROVIDER, COLOR_PROVIDER,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID, _mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                        null, COLOR_PROVIDER,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID, _mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                        THICKNESS_PROVIDER, null,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID, _mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                        THICKNESS_PROVIDER, COLOR_PROVIDER,
                        null,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID, _mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                        THICKNESS_PROVIDER, COLOR_PROVIDER,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        null, Z, UUID, _mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                        THICKNESS_PROVIDER, COLOR_PROVIDER,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, null, _mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> _antialiasedLineSegmentRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                        THICKNESS_PROVIDER, COLOR_PROVIDER,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID, null));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(AntialiasedLineSegmentRenderableFactory.class.getCanonicalName(),
                _antialiasedLineSegmentRenderableFactory.getInterfaceName());
    }
}
