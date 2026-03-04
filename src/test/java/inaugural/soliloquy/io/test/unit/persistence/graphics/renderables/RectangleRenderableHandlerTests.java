package inaugural.soliloquy.io.test.unit.persistence.graphics.renderables;

import inaugural.soliloquy.io.persistence.graphics.renderables.RectangleRenderableHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.RectangleRenderable;
import soliloquy.specs.io.graphics.renderables.factories.RectangleRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.randomString;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

@ExtendWith(MockitoExtension.class)
public class RectangleRenderableHandlerTests extends AbstractPolygonRenderableHandlerTests {
    private final String TOP_LEFT_COLOR_WRITTEN = randomString();
    private final String TOP_RIGHT_COLOR_WRITTEN = randomString();
    private final String BOTTOM_LEFT_COLOR_WRITTEN = randomString();
    private final String BOTTOM_RIGHT_COLOR_WRITTEN = randomString();
    private final String DIMENSIONS_WRITTEN = randomString();

    @Mock private ProviderAtTime<Color> mockTopLeftColorProvider;
    @Mock private ProviderAtTime<Color> mockTopRightColorProvider;
    @Mock private ProviderAtTime<Color> mockBottomLeftColorProvider;
    @Mock private ProviderAtTime<Color> mockBottomRightColorProvider;
    @Mock private ProviderAtTime<FloatBox> mockDimensionsProvider;

    @Mock private RectangleRenderable mockRenderable;
    @Mock private RectangleRenderableFactory mockFactory;

    private String writtenValue;

    private TypeHandler<RectangleRenderable> handler;

    @BeforeEach
    public void setUp() {
        super.setUp();

        writtenValue = String.format(
                "{\"topLeftColor\":\"%s\"," +
                        "\"topRightColor\":\"%s\"," +
                        "\"bottomLeftColor\":\"%s\"," +
                        "\"bottomRightColor\":\"%s\"," +
                        "\"area\":\"%s\",\"texId\":\"%s\"," +
                        "\"texWidth\":\"%s\"," +
                        "\"texXOffset\":\"%s\"," +
                        "\"texHeight\":\"%s\"," +
                        "\"texYOffset\":\"%s\"," +
                        "\"onPress\":[{\"button\":%d," +
                        "\"actionId\":\"%s\"}]," +
                        "\"onRelease\":[{\"button\":%d," +
                        "\"actionId\":\"%s\"}]," +
                        "\"mouseOver\":\"%s\"," +
                        "\"mouseLeave\":\"%s\",\"z\":%d," +
                        "\"uuid\":\"%s\",\"type\":\"%s\"}",
                TOP_LEFT_COLOR_WRITTEN, TOP_RIGHT_COLOR_WRITTEN, BOTTOM_LEFT_COLOR_WRITTEN,
                BOTTOM_RIGHT_COLOR_WRITTEN, DIMENSIONS_WRITTEN, TEXTURE_ID_WRITTEN,
                TEXTURE_TILE_WIDTH_WRITTEN, TEXTURE_X_OFFSET_WRITTEN, TEXTURE_TILE_HEIGHT_WRITTEN,
                TEXTURE_Y_OFFSET_WRITTEN, ON_PRESS_BUTTON, ON_PRESS_CONSUMER_ID, ON_RELEASE_BUTTON,
                ON_RELEASE_CONSUMER_ID, ON_MOUSE_OVER_CONSUMER_ID, ON_MOUSE_LEAVE_CONSUMER_ID, Z,
                UUID, mockRenderable.getClass().getCanonicalName()
        );

        handler =
                new RectangleRenderableHandler(MOCK_GET_CONSUMER, mockProviderHandler, mockFactory);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new RectangleRenderableHandler(null, mockProviderHandler, mockFactory));
        assertThrows(IllegalArgumentException.class,
                () -> new RectangleRenderableHandler(MOCK_GET_CONSUMER, null, mockFactory));
        assertThrows(IllegalArgumentException.class,
                () -> new RectangleRenderableHandler(MOCK_GET_CONSUMER, mockProviderHandler, null));
    }

    @Test
    public void testWrite() {
        setUpMockRenderable(mockRenderable);

        when(mockRenderable.getTopLeftColorProvider()).thenReturn(mockTopLeftColorProvider);
        when(mockRenderable.getTopRightColorProvider()).thenReturn(mockTopRightColorProvider);
        when(mockRenderable.getBottomLeftColorProvider()).thenReturn(mockBottomLeftColorProvider);
        when(mockRenderable.getBottomRightColorProvider()).thenReturn(mockBottomRightColorProvider);
        when(mockRenderable.getRenderingDimensionsProvider()).thenReturn(mockDimensionsProvider);

        when(mockProviderHandler.write(mockTopLeftColorProvider)).thenReturn(
                TOP_LEFT_COLOR_WRITTEN);
        when(mockProviderHandler.write(mockTopRightColorProvider)).thenReturn(
                TOP_RIGHT_COLOR_WRITTEN);
        when(mockProviderHandler.write(mockBottomLeftColorProvider)).thenReturn(
                BOTTOM_LEFT_COLOR_WRITTEN);
        when(mockProviderHandler.write(mockBottomRightColorProvider)).thenReturn(
                BOTTOM_RIGHT_COLOR_WRITTEN);
        when(mockProviderHandler.write(mockDimensionsProvider)).thenReturn(DIMENSIONS_WRITTEN);

        var output = handler.write(mockRenderable);

        assertEquals(writtenValue, output);
        verifyWritten(mockRenderable);

        verify(mockRenderable, once()).getTopLeftColorProvider();
        verify(mockRenderable, once()).getTopRightColorProvider();
        verify(mockRenderable, once()).getBottomLeftColorProvider();
        verify(mockRenderable, once()).getBottomRightColorProvider();
        verify(mockRenderable, once()).getRenderingDimensionsProvider();

        verify(mockProviderHandler, once()).write(mockTopLeftColorProvider);
        verify(mockProviderHandler, once()).write(mockTopRightColorProvider);
        verify(mockProviderHandler, once()).write(mockBottomLeftColorProvider);
        verify(mockProviderHandler, once()).write(mockBottomRightColorProvider);
        verify(mockProviderHandler, once()).write(mockDimensionsProvider);
    }

    @Test
    public void testWriteWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> handler.write(null));
    }

    @Test
    public void testRead() {
        when(mockProviderHandler.read(TOP_LEFT_COLOR_WRITTEN)).thenReturn(mockTopLeftColorProvider);
        when(mockProviderHandler.read(TOP_RIGHT_COLOR_WRITTEN))
                .thenReturn(mockTopRightColorProvider);
        when(mockProviderHandler.read(BOTTOM_LEFT_COLOR_WRITTEN))
                .thenReturn(mockBottomLeftColorProvider);
        when(mockProviderHandler.read(BOTTOM_RIGHT_COLOR_WRITTEN))
                .thenReturn(mockBottomRightColorProvider);
        when(mockProviderHandler.read(DIMENSIONS_WRITTEN)).thenReturn(mockDimensionsProvider);
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
                any(),
                anyInt(),
                any(),
                any()
        )).thenReturn(mockRenderable);

        var output = handler.read(writtenValue);

        assertSame(mockRenderable, output);
        verifyRead();
        verify(mockProviderHandler, once()).read(TOP_LEFT_COLOR_WRITTEN);
        verify(mockProviderHandler, once()).read(TOP_RIGHT_COLOR_WRITTEN);
        verify(mockProviderHandler, once()).read(BOTTOM_LEFT_COLOR_WRITTEN);
        verify(mockProviderHandler, once()).read(BOTTOM_RIGHT_COLOR_WRITTEN);
        verify(mockProviderHandler, once()).read(DIMENSIONS_WRITTEN);
        verify(mockFactory, once()).make(
                same(mockTopLeftColorProvider),
                same(mockTopRightColorProvider),
                same(mockBottomLeftColorProvider),
                same(mockBottomRightColorProvider),
                same(mockTextureIdProvider),
                same(mockTextureTilesPerWidthProvider),
                same(mockTextureXOffsetProvider),
                same(mockTextureTilesPerHeightProvider),
                same(mockTextureYOffsetProvider),
                eq(mapOf(pairOf(ON_PRESS_BUTTON, MOCK_ON_PRESS_CONSUMER))),
                eq(mapOf(pairOf(ON_RELEASE_BUTTON, MOCK_ON_RELEASE_CONSUMER))),
                same(MOCK_ON_MOUSE_OVER_CONSUMER),
                same(MOCK_ON_MOUSE_LEAVE_CONSUMER),
                same(mockDimensionsProvider),
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
