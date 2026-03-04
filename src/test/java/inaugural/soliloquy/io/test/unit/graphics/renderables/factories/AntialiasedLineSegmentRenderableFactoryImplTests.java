package inaugural.soliloquy.io.test.unit.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.AntialiasedLineSegmentRenderableImpl;
import inaugural.soliloquy.io.graphics.renderables.factories.AntialiasedLineSegmentRenderableFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.factories.AntialiasedLineSegmentRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;

import static inaugural.soliloquy.tools.random.Random.randomInt;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AntialiasedLineSegmentRenderableFactoryImplTests {
    private final int Z = randomInt();
    private final java.util.UUID UUID = java.util.UUID.randomUUID();

    @Mock private ProviderAtTime<Vertex> mockVertex1Provider;
    @Mock private ProviderAtTime<Vertex> mockVertex2Provider;
    @Mock private ProviderAtTime<Float> mockThicknessProvider;
    @Mock private ProviderAtTime<Color> mockColorProvider;
    @Mock private ProviderAtTime<Float> mockThicknessGradientPercentProvider;
    @Mock private ProviderAtTime<Float> mockLengthGradientPercentProvider;

    @Mock private Component mockContainingComponent;

    private AntialiasedLineSegmentRenderableFactory factory;

    @BeforeEach
    public void setUp() {
        factory = new AntialiasedLineSegmentRenderableFactoryImpl();
    }

    @Test
    public void testMake() {
        var output = factory.make(mockVertex1Provider, mockVertex2Provider, mockColorProvider,
                mockThicknessProvider, mockThicknessGradientPercentProvider,
                mockLengthGradientPercentProvider, Z, UUID, mockContainingComponent);

        assertNotNull(output);
        assertInstanceOf(AntialiasedLineSegmentRenderableImpl.class,
                output);

        var newZ = randomInt();
        output.setZ(newZ);

        assertEquals(newZ, output.getZ());

        output.delete();

        assertTrue(output.isDeleted());
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> factory
                .make(null, mockVertex2Provider,
                        mockColorProvider, mockThicknessProvider,
                        mockThicknessGradientPercentProvider,
                        mockLengthGradientPercentProvider, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> factory
                .make(mockVertex1Provider, null,
                        mockColorProvider, mockThicknessProvider,
                        mockThicknessGradientPercentProvider,
                        mockLengthGradientPercentProvider, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> factory
                .make(mockVertex1Provider, mockVertex2Provider,
                        null, mockThicknessProvider,
                        mockThicknessGradientPercentProvider,
                        mockLengthGradientPercentProvider, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> factory
                .make(mockVertex1Provider, mockVertex2Provider,
                        mockColorProvider, null,
                        mockThicknessGradientPercentProvider,
                        mockLengthGradientPercentProvider, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> factory
                .make(mockVertex1Provider, mockVertex2Provider,
                        mockColorProvider, mockThicknessProvider,
                        null,
                        mockLengthGradientPercentProvider, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> factory
                .make(mockVertex1Provider, mockVertex2Provider,
                        mockColorProvider, mockThicknessProvider,
                        mockThicknessGradientPercentProvider,
                        null, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class, () -> factory
                .make(mockVertex1Provider, mockVertex2Provider,
                        mockColorProvider, mockThicknessProvider,
                        mockThicknessGradientPercentProvider,
                        mockLengthGradientPercentProvider, Z, null, mockContainingComponent));
    }
}
