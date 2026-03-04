package inaugural.soliloquy.io.test.unit.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.TriangleRenderableImpl;
import inaugural.soliloquy.io.graphics.renderables.factories.TriangleRenderableFactoryImpl;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.factories.TriangleRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;

import java.awt.*;
import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.randomInt;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TriangleRenderableFactoryImplTests {
    private final int Z = randomInt();
    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private ProviderAtTime<Vertex> mockVertex1Provider;
    @Mock private ProviderAtTime<Color> mockVertex1ColorProvider;
    @Mock private ProviderAtTime<Vertex> mockVertex2Provider;
    @Mock private ProviderAtTime<Color> mockVertex2ColorProvider;
    @Mock private ProviderAtTime<Vertex> mockVertex3Provider;
    @Mock private ProviderAtTime<Color> mockVertex3ColorProvider;
    @Mock private ProviderAtTime<Integer> mockBackgroundTextureIdProvider;
    @Mock private ProviderAtTime<Float> mockTextureTilesPerWidthProvider;
    @Mock private ProviderAtTime<Float> mockTextureXOffsetProvider;
    @Mock private ProviderAtTime<Float> mockTextureTilesPerHeightProvider;
    @Mock private ProviderAtTime<Float> mockTextureYOffsetProvider;
    @Mock private Component mockContainingComponent;
    @Mock private RenderingBoundaries mockRenderingBoundaries;
    @Mock private TimestampValidator mockTimestampValidator;

    private TriangleRenderableFactory factory;

    @BeforeEach
    public void setUp() {
        factory =
                new TriangleRenderableFactoryImpl(mockRenderingBoundaries, mockTimestampValidator);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new TriangleRenderableFactoryImpl(null, mockTimestampValidator));
        assertThrows(IllegalArgumentException.class,
                () -> new TriangleRenderableFactoryImpl(mockRenderingBoundaries, null));
    }

    @Test
    public void testMake() {
        var renderable = factory
                .make(mockVertex1Provider, mockVertex1ColorProvider, mockVertex2Provider,
                        mockVertex2ColorProvider, mockVertex3Provider, mockVertex3ColorProvider,
                        mockBackgroundTextureIdProvider, mockTextureTilesPerWidthProvider,
                        mockTextureXOffsetProvider, mockTextureTilesPerHeightProvider,
                        mockTextureYOffsetProvider, null, null, null, null, Z, UUID,
                        mockContainingComponent);

        assertNotNull(renderable);
        assertInstanceOf(TriangleRenderableImpl.class, renderable);
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(null, mockVertex1ColorProvider, mockVertex2Provider,
                        mockVertex2ColorProvider, mockVertex3Provider, mockVertex3ColorProvider,
                        mockBackgroundTextureIdProvider, mockTextureTilesPerWidthProvider,
                        mockTextureXOffsetProvider, mockTextureTilesPerHeightProvider,
                        mockTextureYOffsetProvider, null, null, null, null, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(mockVertex1Provider, null, mockVertex2Provider,
                        mockVertex2ColorProvider, mockVertex3Provider, mockVertex3ColorProvider,
                        mockBackgroundTextureIdProvider, mockTextureTilesPerWidthProvider,
                        mockTextureXOffsetProvider, mockTextureTilesPerHeightProvider,
                        mockTextureYOffsetProvider, null, null, null, null, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(mockVertex1Provider, mockVertex1ColorProvider, null,
                        mockVertex2ColorProvider, mockVertex3Provider, mockVertex3ColorProvider,
                        mockBackgroundTextureIdProvider, mockTextureTilesPerWidthProvider,
                        mockTextureXOffsetProvider, mockTextureTilesPerHeightProvider,
                        mockTextureYOffsetProvider, null, null, null, null, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(mockVertex1Provider, mockVertex1ColorProvider,
                        mockVertex2Provider, null, mockVertex3Provider, mockVertex3ColorProvider,
                        mockBackgroundTextureIdProvider, mockTextureTilesPerWidthProvider,
                        mockTextureXOffsetProvider, mockTextureTilesPerHeightProvider,
                        mockTextureYOffsetProvider, null, null, null, null, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(mockVertex1Provider, mockVertex1ColorProvider,
                        mockVertex2Provider, mockVertex2ColorProvider, null,
                        mockVertex3ColorProvider, mockBackgroundTextureIdProvider,
                        mockTextureTilesPerWidthProvider, mockTextureXOffsetProvider,
                        mockTextureTilesPerHeightProvider, mockTextureYOffsetProvider, null, null, null,
                        null, Z, UUID, mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(mockVertex1Provider, mockVertex1ColorProvider,
                        mockVertex2Provider, mockVertex2ColorProvider, mockVertex3Provider, null,
                        mockBackgroundTextureIdProvider, mockTextureTilesPerWidthProvider,
                        mockTextureXOffsetProvider, mockTextureTilesPerHeightProvider,
                        mockTextureYOffsetProvider, null, null, null, null, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(mockVertex1Provider, mockVertex1ColorProvider,
                        mockVertex2Provider, mockVertex2ColorProvider, mockVertex3Provider,
                        mockVertex3ColorProvider, null, mockTextureTilesPerWidthProvider,
                        mockTextureXOffsetProvider, mockTextureTilesPerHeightProvider,
                        mockTextureYOffsetProvider, null, null, null, null, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(mockVertex1Provider, mockVertex1ColorProvider,
                        mockVertex2Provider, mockVertex2ColorProvider, mockVertex3Provider,
                        mockVertex3ColorProvider, mockBackgroundTextureIdProvider, null,
                        mockTextureXOffsetProvider, mockTextureTilesPerHeightProvider,
                        mockTextureYOffsetProvider, null, null, null, null, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(mockVertex1Provider, mockVertex1ColorProvider,
                        mockVertex2Provider, mockVertex2ColorProvider, mockVertex3Provider,
                        mockVertex3ColorProvider, mockBackgroundTextureIdProvider,
                        mockTextureTilesPerWidthProvider, null, mockTextureTilesPerHeightProvider,
                        mockTextureYOffsetProvider, null, null, null, null, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(mockVertex1Provider, mockVertex1ColorProvider,
                        mockVertex2Provider, mockVertex2ColorProvider, mockVertex3Provider,
                        mockVertex3ColorProvider, mockBackgroundTextureIdProvider,
                        mockTextureTilesPerWidthProvider, mockTextureXOffsetProvider, null,
                        mockTextureYOffsetProvider, null, null, null, null, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(mockVertex1Provider, mockVertex1ColorProvider,
                        mockVertex2Provider, mockVertex2ColorProvider, mockVertex3Provider,
                        mockVertex3ColorProvider, mockBackgroundTextureIdProvider,
                        mockTextureTilesPerWidthProvider, mockTextureXOffsetProvider,
                        mockTextureTilesPerHeightProvider, null, null, null, null, null, Z, UUID,
                        mockContainingComponent));
        assertThrows(IllegalArgumentException.class,
                () -> factory.make(mockVertex1Provider, mockVertex1ColorProvider,
                        mockVertex2Provider, mockVertex2ColorProvider, mockVertex3Provider,
                        mockVertex3ColorProvider, mockBackgroundTextureIdProvider,
                        mockTextureTilesPerWidthProvider, mockTextureXOffsetProvider,
                        mockTextureTilesPerHeightProvider, mockTextureYOffsetProvider, null, null, null,
                        null, Z, null, mockContainingComponent));
    }
}
