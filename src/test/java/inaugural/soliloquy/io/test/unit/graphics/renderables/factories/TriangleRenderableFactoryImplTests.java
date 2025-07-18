package inaugural.soliloquy.io.test.unit.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.TriangleRenderableImpl;
import inaugural.soliloquy.io.graphics.renderables.factories.TriangleRenderableFactoryImpl;
import inaugural.soliloquy.io.test.testdoubles.fakes.FakeProviderAtTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.TriangleRenderable;
import soliloquy.specs.io.graphics.renderables.factories.TriangleRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderableStack;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;

import java.awt.*;
import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class TriangleRenderableFactoryImplTests {
    private final ProviderAtTime<Vertex> VERTEX_1_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> VERTEX_1_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Vertex> VERTEX_2_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> VERTEX_2_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Vertex> VERTEX_3_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Color> VERTEX_3_COLOR_PROVIDER = new FakeProviderAtTime<>();
    private final ProviderAtTime<Integer> BACKGROUND_TEXTURE_ID_PROVIDER =
            new FakeProviderAtTime<>();
    private final int Z = randomInt();
    private final UUID UUID = java.util.UUID.randomUUID();

    @Mock private ProviderAtTime<Float> mockTextureTileWidthProvider;
    @Mock private ProviderAtTime<Float> mockTextureTileHeightProvider;
    @Mock private RenderableStack mockContainingStack;
    @Mock private RenderingBoundaries mockRenderingBoundaries;

    private TriangleRenderableFactory triangleRenderableFactory;

    @BeforeEach
    public void setUp() {
        mockContainingStack = mock(RenderableStack.class);
        mockRenderingBoundaries = mock(RenderingBoundaries.class);

        triangleRenderableFactory = new TriangleRenderableFactoryImpl(mockRenderingBoundaries);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new TriangleRenderableFactoryImpl(null));
    }

    @Test
    public void testMake() {
        TriangleRenderable triangleRenderable = triangleRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                        VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                        VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, mockTextureTileWidthProvider,
                        mockTextureTileHeightProvider, null, null, null, null, Z, UUID,
                        mockContainingStack);

        assertNotNull(triangleRenderable);
        assertInstanceOf(TriangleRenderableImpl.class, triangleRenderable);
    }

    @Test
    public void testMakeWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> triangleRenderableFactory
                .make(null, VERTEX_1_COLOR_PROVIDER,
                        VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                        VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, mockTextureTileWidthProvider,
                        mockTextureTileHeightProvider, null, null, null, null, Z, UUID,
                        mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> triangleRenderableFactory
                .make(VERTEX_1_PROVIDER, null,
                        VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                        VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, mockTextureTileWidthProvider,
                        mockTextureTileHeightProvider, null, null, null, null, Z, UUID,
                        mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> triangleRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                        null, VERTEX_2_COLOR_PROVIDER,
                        VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, mockTextureTileWidthProvider,
                        mockTextureTileHeightProvider, null, null, null, null, Z, UUID,
                        mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> triangleRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                        VERTEX_2_PROVIDER, null,
                        VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, mockTextureTileWidthProvider,
                        mockTextureTileHeightProvider, null, null, null, null, Z, UUID,
                        mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> triangleRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                        VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                        null, VERTEX_3_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, mockTextureTileWidthProvider,
                        mockTextureTileHeightProvider, null, null, null, null, Z, UUID,
                        mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> triangleRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                        VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                        VERTEX_3_PROVIDER, null,
                        BACKGROUND_TEXTURE_ID_PROVIDER, mockTextureTileWidthProvider,
                        mockTextureTileHeightProvider, null, null, null, null, Z, UUID,
                        mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> triangleRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                        VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                        VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                        null, mockTextureTileWidthProvider,
                        mockTextureTileHeightProvider, null, null, null, null, Z, UUID,
                        mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> triangleRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                        VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                        VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, null,
                        mockTextureTileHeightProvider, null, null, null, null, Z, UUID,
                        mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> triangleRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                        VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                        VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, mockTextureTileWidthProvider,
                        null, null, null, null, null, Z, UUID,
                        mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> triangleRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                        VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                        VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, mockTextureTileWidthProvider,
                        mockTextureTileHeightProvider, null, null, null, null, Z, null,
                        mockContainingStack));
        assertThrows(IllegalArgumentException.class, () -> triangleRenderableFactory
                .make(VERTEX_1_PROVIDER, VERTEX_1_COLOR_PROVIDER,
                        VERTEX_2_PROVIDER, VERTEX_2_COLOR_PROVIDER,
                        VERTEX_3_PROVIDER, VERTEX_3_COLOR_PROVIDER,
                        BACKGROUND_TEXTURE_ID_PROVIDER, mockTextureTileWidthProvider,
                        mockTextureTileHeightProvider, null, null, null, null, Z, UUID, null));
    }
}
