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
import soliloquy.specs.io.graphics.renderables.factories.AntialiasedLineSegmentRenderableFactory;
import soliloquy.specs.ui.Component;

import java.awt.*;

import static inaugural.soliloquy.tools.random.Random.randomInt;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AntialiasedLineSegmentRenderableFactoryImplTests {
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

    @Mock private Component mockContainingComponent;

    private AntialiasedLineSegmentRenderableFactory factory;

    @BeforeEach
    public void setUp() {
        factory = new AntialiasedLineSegmentRenderableFactoryImpl();
    }

    @Test
    public void testMake() {
        var output = factory.make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER, COLOR_PROVIDER,
                THICKNESS_PROVIDER, THICKNESS_GRADIENT_PERCENT_PROVIDER,
                LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID, mockContainingComponent);

        assertNotNull(output);
        assertInstanceOf(AntialiasedLineSegmentRenderableImpl.class,
                output);

        var newZ = 456;
        output.setZ(newZ);

        assertEquals(newZ, output.getZ());
        verify(mockContainingComponent, once()).add(output);

        output.delete();

        assertTrue(output.isDeleted());
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> factory
                .make(null, VERTEX_2_PROVIDER,
                        COLOR_PROVIDER, THICKNESS_PROVIDER,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> factory
                .make(VERTEX_1_PROVIDER, null,
                        COLOR_PROVIDER, THICKNESS_PROVIDER,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> factory
                .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                        null, THICKNESS_PROVIDER,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> factory
                .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                        COLOR_PROVIDER, null,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> factory
                .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                        COLOR_PROVIDER, THICKNESS_PROVIDER,
                        null,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> factory
                .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                        COLOR_PROVIDER, THICKNESS_PROVIDER,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        null, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> factory
                .make(VERTEX_1_PROVIDER, VERTEX_2_PROVIDER,
                        COLOR_PROVIDER, THICKNESS_PROVIDER,
                        THICKNESS_GRADIENT_PERCENT_PROVIDER,
                        LENGTH_GRADIENT_PERCENT_PROVIDER, Z, null, mockContainingComponent));
    }
}
