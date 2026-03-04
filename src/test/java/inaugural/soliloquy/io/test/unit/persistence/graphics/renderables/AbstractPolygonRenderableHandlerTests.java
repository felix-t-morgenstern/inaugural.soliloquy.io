package inaugural.soliloquy.io.test.unit.persistence.graphics.renderables;

import inaugural.soliloquy.io.persistence.graphics.renderables.providers.ProviderHandler;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.io.graphics.renderables.PolygonRenderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import static inaugural.soliloquy.tools.random.Random.randomString;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static inaugural.soliloquy.tools.testing.Mock.hydrateMockHandler;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractPolygonRenderableHandlerTests
        extends AbstractRenderableWithMouseEventsHandlerTests {
    protected final String TEXTURE_ID_WRITTEN = randomString();
    protected final String TEXTURE_TILE_WIDTH_WRITTEN = randomString();
    protected final String TEXTURE_X_OFFSET_WRITTEN = randomString();
    protected final String TEXTURE_TILE_HEIGHT_WRITTEN = randomString();
    protected final String TEXTURE_Y_OFFSET_WRITTEN = randomString();

    @Mock protected ProviderHandler mockProviderHandler;
    @Mock protected ProviderAtTime<Integer> mockTextureIdProvider;
    @Mock protected ProviderAtTime<Float> mockTextureTilesPerWidthProvider;
    @Mock protected ProviderAtTime<Float> mockTextureXOffsetProvider;
    @Mock protected ProviderAtTime<Float> mockTextureTilesPerHeightProvider;
    @Mock protected ProviderAtTime<Float> mockTextureYOffsetProvider;

    protected void setUp() {
        super.setUp();

        hydrateMockHandler(mockProviderHandler,
                pairOf(mockTextureIdProvider,TEXTURE_ID_WRITTEN),
                pairOf(mockTextureTilesPerWidthProvider,TEXTURE_TILE_WIDTH_WRITTEN),
                pairOf(mockTextureXOffsetProvider, TEXTURE_X_OFFSET_WRITTEN),
                pairOf(mockTextureTilesPerHeightProvider,TEXTURE_TILE_HEIGHT_WRITTEN),
                pairOf(mockTextureYOffsetProvider, TEXTURE_Y_OFFSET_WRITTEN)
        );
    }

    protected void setUpMockRenderable(PolygonRenderable mockRenderable) {
        super.setUpMockRenderable(mockRenderable);

        when(mockRenderable.getTextureIdProvider()).thenReturn(mockTextureIdProvider);
        when(mockRenderable.getTextureTilesPerWidthProvider()).thenReturn(
                mockTextureTilesPerWidthProvider);
        when(mockRenderable.getTextureXOffsetProvider()).thenReturn(mockTextureXOffsetProvider);
        when(mockRenderable.getTextureTilesPerHeightProvider()).thenReturn(
                mockTextureTilesPerHeightProvider);
        when(mockRenderable.getTextureYOffsetProvider()).thenReturn(mockTextureYOffsetProvider);
    }

    protected void verifyWritten(PolygonRenderable mockRenderable) {
        super.verifyWritten(mockRenderable);

        verify(mockRenderable, once()).getTextureIdProvider();
        verify(mockRenderable, once()).getTextureTilesPerWidthProvider();
        verify(mockRenderable, once()).getTextureXOffsetProvider();
        verify(mockRenderable, once()).getTextureTilesPerHeightProvider();
        verify(mockRenderable, once()).getTextureYOffsetProvider();

        verify(mockProviderHandler, once()).write(mockTextureIdProvider);
        verify(mockProviderHandler, once()).write(mockTextureTilesPerWidthProvider);
        verify(mockProviderHandler, once()).write(mockTextureXOffsetProvider);
        verify(mockProviderHandler, once()).write(mockTextureTilesPerHeightProvider);
        verify(mockProviderHandler, once()).write(mockTextureYOffsetProvider);
    }

    protected void verifyRead() {
        super.verifyRead();

        verify(mockProviderHandler, once()).read(TEXTURE_ID_WRITTEN);
        verify(mockProviderHandler, once()).read(TEXTURE_TILE_WIDTH_WRITTEN);
        verify(mockProviderHandler, once()).read(TEXTURE_X_OFFSET_WRITTEN);
        verify(mockProviderHandler, once()).read(TEXTURE_TILE_HEIGHT_WRITTEN);
        verify(mockProviderHandler, once()).read(TEXTURE_Y_OFFSET_WRITTEN);
    }
}
