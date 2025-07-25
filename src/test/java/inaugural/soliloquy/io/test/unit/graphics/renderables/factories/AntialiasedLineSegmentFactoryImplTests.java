package inaugural.soliloquy.io.test.unit.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.AntialiasedLineSegmentRenderableImpl;
import inaugural.soliloquy.io.graphics.renderables.factories.AntialiasedLineSegmentRenderableFactoryImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeProviderAtTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.Renderable;
import soliloquy.specs.io.graphics.renderables.factories.AntialiasedLineSegmentRenderableFactory;
import soliloquy.specs.ui.Component;

import java.awt.*;
import java.util.function.BiConsumer;

import static inaugural.soliloquy.tools.random.Random.randomInt;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    @Mock private BiConsumer<Component, Renderable> mockRemoveFromComponent;
    @Mock private Component mockContainingComponent;

    private AntialiasedLineSegmentRenderableFactory antialiasedLineSegmentRenderableFactory;

    @BeforeEach
    public void setUp() {
        antialiasedLineSegmentRenderableFactory =
                new AntialiasedLineSegmentRenderableFactoryImpl(mockRemoveFromComponent);
    }

    @Test
    public void testMake() {
        var antialiasedLineSegmentRenderable =
                antialiasedLineSegmentRenderableFactory.make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                        THICKNESS_PROVIDER, COLOR_PROVIDER, THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID, mockContainingComponent);

        assertNotNull(antialiasedLineSegmentRenderable);
        assertInstanceOf(AntialiasedLineSegmentRenderableImpl.class,
                antialiasedLineSegmentRenderable);

        var newZ = 456;
        antialiasedLineSegmentRenderable.setZ(newZ);

        assertEquals(newZ, antialiasedLineSegmentRenderable.getZ());
        verify(mockContainingComponent, once()).add(antialiasedLineSegmentRenderable);

        antialiasedLineSegmentRenderable.delete();

        verify(mockRemoveFromComponent, once())
                .accept(same(mockContainingComponent), same(antialiasedLineSegmentRenderable));
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderableFactory
                .make(null, VERTEX_2_PROVIDER,
                        THICKNESS_PROVIDER, COLOR_PROVIDER,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderableFactory
                .make(VERTEX_1_PROVIDER, null,
                        THICKNESS_PROVIDER, COLOR_PROVIDER,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                        null, COLOR_PROVIDER,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                        THICKNESS_PROVIDER, null,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                        THICKNESS_PROVIDER, COLOR_PROVIDER,
                        null,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                        THICKNESS_PROVIDER, COLOR_PROVIDER,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        null, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                        THICKNESS_PROVIDER, COLOR_PROVIDER,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, null, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> antialiasedLineSegmentRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                        THICKNESS_PROVIDER, COLOR_PROVIDER,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID, null));
    }
}
