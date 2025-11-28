package inaugural.soliloquy.io.test.unit.persistence.graphics.renderables;

import inaugural.soliloquy.io.persistence.graphics.renderables.RectangleRenderableHandler;
import inaugural.soliloquy.io.persistence.graphics.renderables.TriangleRenderableHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.TriangleRenderable;
import soliloquy.specs.io.graphics.renderables.factories.TriangleRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomString;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static inaugural.soliloquy.tools.testing.Mock.hydrateMockHandler;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

@ExtendWith(MockitoExtension.class)
public class TriangleRenderableHandlerTests extends AbstractPolygonRenderableHandlerTests {
    private final String VERTEX_1_WRITTEN = randomString();
    private final String VERTEX_1_COLOR_WRITTEN = randomString();
    private final String VERTEX_2_WRITTEN = randomString();
    private final String VERTEX_2_COLOR_WRITTEN = randomString();
    private final String VERTEX_3_WRITTEN = randomString();
    private final String VERTEX_3_COLOR_WRITTEN = randomString();

    @Mock private ProviderAtTime<Vertex> mockVertex1Provider;
    @Mock private ProviderAtTime<Color> mockVertex1ColorProvider;
    @Mock private ProviderAtTime<Vertex> mockVertex2Provider;
    @Mock private ProviderAtTime<Color> mockVertex2ColorProvider;
    @Mock private ProviderAtTime<Vertex> mockVertex3Provider;
    @Mock private ProviderAtTime<Color> mockVertex3ColorProvider;

    @Mock private TriangleRenderable mockRenderable;
    @Mock private TriangleRenderableFactory mockFactory;

    private String writtenValue;

    private TypeHandler<TriangleRenderable> handler;

    @BeforeEach
    public void setUp() {
        super.setUp();

        hydrateMockHandler(mockProviderHandler,
                pairOf(mockVertex1Provider, VERTEX_1_WRITTEN),
                pairOf(mockVertex1ColorProvider, VERTEX_1_COLOR_WRITTEN),
                pairOf(mockVertex2Provider, VERTEX_2_WRITTEN),
                pairOf(mockVertex2ColorProvider, VERTEX_2_COLOR_WRITTEN),
                pairOf(mockVertex3Provider, VERTEX_3_WRITTEN),
                pairOf(mockVertex3ColorProvider, VERTEX_3_COLOR_WRITTEN)
        );

        writtenValue = String.format(
                "{\"vertex1\":\"%s\",\"vertex1Color\":\"%s\",\"vertex2\":\"%s\"," +
                        "\"vertex2Color\":\"%s\",\"vertex3\":\"%s\",\"vertex3Color\":\"%s\"," +
                        "\"texId\":\"%s\",\"texWidth\":\"%s\",\"texHeight\":\"%s\"," +
                        "\"onPress\":[{\"button\":%d,\"actionId\":\"%s\"}]," +
                        "\"onRelease\":[{\"button\":%d,\"actionId\":\"%s\"}]," +
                        "\"mouseOver\":\"%s\",\"mouseLeave\":\"%s\",\"z\":%d,\"uuid\":\"%s\"," +
                        "\"type\":\"%s\"}",
                VERTEX_1_WRITTEN, VERTEX_1_COLOR_WRITTEN, VERTEX_2_WRITTEN, VERTEX_2_COLOR_WRITTEN,
                VERTEX_3_WRITTEN, VERTEX_3_COLOR_WRITTEN, TEXTURE_ID_WRITTEN,
                TEXTURE_TILE_WIDTH_WRITTEN, TEXTURE_TILE_HEIGHT_WRITTEN, ON_PRESS_BUTTON,
                ON_PRESS_CONSUMER_ID, ON_RELEASE_BUTTON, ON_RELEASE_CONSUMER_ID,
                ON_MOUSE_OVER_CONSUMER_ID, ON_MOUSE_LEAVE_CONSUMER_ID, Z, UUID,
                mockRenderable.getClass().getCanonicalName()
        );

        handler = new TriangleRenderableHandler(MOCK_GET_CONSUMER, mockProviderHandler, mockFactory);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new TriangleRenderableHandler(null, mockProviderHandler, mockFactory));
        assertThrows(IllegalArgumentException.class,
                () -> new TriangleRenderableHandler(MOCK_GET_CONSUMER, null, mockFactory));
        assertThrows(IllegalArgumentException.class,
                () -> new TriangleRenderableHandler(MOCK_GET_CONSUMER, mockProviderHandler, null));
    }

    @Test
    public void testWrite() {
        setUpMockRenderable(mockRenderable);

        when(mockRenderable.getVertex1Provider()).thenReturn(mockVertex1Provider);
        when(mockRenderable.getVertex1ColorProvider()).thenReturn(mockVertex1ColorProvider);
        when(mockRenderable.getVertex2Provider()).thenReturn(mockVertex2Provider);
        when(mockRenderable.getVertex2ColorProvider()).thenReturn(mockVertex2ColorProvider);
        when(mockRenderable.getVertex3Provider()).thenReturn(mockVertex3Provider);
        when(mockRenderable.getVertex3ColorProvider()).thenReturn(mockVertex3ColorProvider);

        var output = handler.write(mockRenderable);

        assertEquals(writtenValue, output);
        verifyWritten(mockRenderable);

        verify(mockRenderable, once()).getVertex1Provider();
        verify(mockRenderable, once()).getVertex1ColorProvider();
        verify(mockRenderable, once()).getVertex2Provider();
        verify(mockRenderable, once()).getVertex2ColorProvider();
        verify(mockRenderable, once()).getVertex3Provider();
        verify(mockRenderable, once()).getVertex3ColorProvider();

        verify(mockProviderHandler, once()).write(mockVertex1Provider);
        verify(mockProviderHandler, once()).write(mockVertex1ColorProvider);
        verify(mockProviderHandler, once()).write(mockVertex2Provider);
        verify(mockProviderHandler, once()).write(mockVertex2ColorProvider);
        verify(mockProviderHandler, once()).write(mockVertex3Provider);
        verify(mockProviderHandler, once()).write(mockVertex3ColorProvider);
    }

    @Test
    public void testWriteWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> handler.write(null));
    }

    @Test
    public void testRead() {
        when(mockFactory.make(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                anyInt(),
                any(),
                any()
        )).thenReturn(mockRenderable);

        var output = handler.read(writtenValue);

        assertSame(mockRenderable, output);
        verifyRead();
        verify(mockProviderHandler, once()).read(VERTEX_1_WRITTEN);
        verify(mockProviderHandler, once()).read(VERTEX_1_COLOR_WRITTEN);
        verify(mockProviderHandler, once()).read(VERTEX_2_WRITTEN);
        verify(mockProviderHandler, once()).read(VERTEX_2_COLOR_WRITTEN);
        verify(mockProviderHandler, once()).read(VERTEX_3_WRITTEN);
        verify(mockProviderHandler, once()).read(VERTEX_3_COLOR_WRITTEN);
        verify(mockFactory, once()).make(
                same(mockVertex1Provider),
                same(mockVertex1ColorProvider),
                same(mockVertex2Provider),
                same(mockVertex2ColorProvider),
                same(mockVertex3Provider),
                same(mockVertex3ColorProvider),
                same(mockTextureIdProvider),
                same(mockTextureTileWidthProvider),
                same(mockTextureTileHeightProvider),
                eq(mapOf(pairOf(ON_PRESS_BUTTON, MOCK_ON_PRESS_CONSUMER))),
                eq(mapOf(pairOf(ON_RELEASE_BUTTON, MOCK_ON_RELEASE_CONSUMER))),
                same(MOCK_ON_MOUSE_OVER_CONSUMER),
                same(MOCK_ON_MOUSE_LEAVE_CONSUMER),
                eq(Z),
                eq(UUID),
                isNull()
        );
    }

    @Test
    public void testReadWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> handler.read(null));
        assertThrows(IllegalArgumentException.class, () -> handler.read(""));
    }
}
