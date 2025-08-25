package inaugural.soliloquy.io.test.unit.persistence.graphics.renderables;

import inaugural.soliloquy.io.persistence.graphics.renderables.RasterizedLineSegmentRenderableHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.io.graphics.renderables.factories.RasterizedLineSegmentRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;

import static inaugural.soliloquy.tools.random.Random.randomShort;
import static inaugural.soliloquy.tools.random.Random.randomString;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RasterizedLineSegmentRenderableHandlerTests
        extends AbstractLineSegmentRenderableHandlerTests {
    private final String VERTEX_1_PROVIDER_WRITTEN = randomString();
    private final String VERTEX_2_PROVIDER_WRITTEN = randomString();
    private final String COLOR_PROVIDER_WRITTEN = randomString();
    private final String THICKNESS_PROVIDER_WRITTEN = randomString();
    private final short STIPPLE_PATTERN = randomShort();
    private final short STIPPLE_FACTOR = randomShort();

    @Mock private ProviderAtTime<Vertex> mockVertex1Provider;
    @Mock private ProviderAtTime<Vertex> mockVertex2Provider;
    @Mock private ProviderAtTime<Color> mockColorProvider;
    @Mock private ProviderAtTime<Float> mockThicknessProvider;

    @Mock private RasterizedLineSegmentRenderable mockRenderable;
    @Mock private RasterizedLineSegmentRenderableFactory mockFactory;

    private String writtenValue = "";

    private TypeHandler<RasterizedLineSegmentRenderable> handler;

    @BeforeEach
    public void setUp() {
        writtenValue = String.format(
                "{\"vertex1\":\"%s\",\"vertex2\":\"%s\",\"color\":\"%s\",\"thickness\":\"%s\"," +
                        "\"stipplePattern\":%d,\"stippleFactor\":%d,\"z\":%d,\"uuid\":\"%s\"," +
                        "\"type\":\"%s\"}",
                VERTEX_1_PROVIDER_WRITTEN, VERTEX_2_PROVIDER_WRITTEN, COLOR_PROVIDER_WRITTEN,
                THICKNESS_PROVIDER_WRITTEN, STIPPLE_PATTERN, STIPPLE_FACTOR, Z, UUID,
                mockRenderable.getClass().getCanonicalName());

        handler = new RasterizedLineSegmentRenderableHandler(mockProviderHandler, mockFactory);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new RasterizedLineSegmentRenderableHandler(null, mockFactory));
        assertThrows(IllegalArgumentException.class,
                () -> new RasterizedLineSegmentRenderableHandler(mockProviderHandler, null));
    }

    @Test
    public void testWrite() {
        when(mockRenderable.getVertex1Provider()).thenReturn(mockVertex1Provider);
        when(mockRenderable.getVertex2Provider()).thenReturn(mockVertex2Provider);
        when(mockRenderable.getColorProvider()).thenReturn(mockColorProvider);
        when(mockRenderable.getThicknessProvider()).thenReturn(mockThicknessProvider);
        when(mockRenderable.getStipplePattern()).thenReturn(STIPPLE_PATTERN);
        when(mockRenderable.getStippleFactor()).thenReturn(STIPPLE_FACTOR);
        when(mockRenderable.uuid()).thenReturn(UUID);
        when(mockRenderable.getZ()).thenReturn(Z);
        when(mockProviderHandler.write(mockVertex1Provider)).thenReturn(VERTEX_1_PROVIDER_WRITTEN);
        when(mockProviderHandler.write(mockVertex2Provider)).thenReturn(VERTEX_2_PROVIDER_WRITTEN);
        when(mockProviderHandler.write(mockColorProvider)).thenReturn(COLOR_PROVIDER_WRITTEN);
        when(mockProviderHandler.write(mockThicknessProvider)).thenReturn(
                THICKNESS_PROVIDER_WRITTEN);

        var output = handler.write(mockRenderable);

        assertEquals(writtenValue, output);
        verify(mockProviderHandler, times(4)).write(any());
        verify(mockProviderHandler, once()).write(mockVertex1Provider);
        verify(mockProviderHandler, once()).write(mockVertex2Provider);
        verify(mockProviderHandler, once()).write(mockColorProvider);
        verify(mockProviderHandler, once()).write(mockThicknessProvider);
    }

    @Test
    public void testWriteWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> handler.write(null));
    }

    @Test
    public void testRead() {
        when(mockProviderHandler.read(VERTEX_1_PROVIDER_WRITTEN)).thenReturn(mockVertex1Provider);
        when(mockProviderHandler.read(VERTEX_2_PROVIDER_WRITTEN)).thenReturn(mockVertex2Provider);
        when(mockProviderHandler.read(COLOR_PROVIDER_WRITTEN)).thenReturn(mockColorProvider);
        when(mockProviderHandler.read(THICKNESS_PROVIDER_WRITTEN)).thenReturn(
                mockThicknessProvider);
        when(mockFactory.make(
                any(),
                any(),
                any(),
                anyShort(),
                anyShort(),
                any(),
                anyInt(),
                any(),
                any()
        )).thenReturn(mockRenderable);

        var renderable = handler.read(writtenValue);

        assertSame(mockRenderable, renderable);
        verify(mockFactory, once()).make(
                any(),
                any(),
                any(),
                anyShort(),
                anyShort(),
                any(),
                anyInt(),
                any(),
                any()
        );
        verify(mockFactory, once()).make(
                same(mockVertex1Provider),
                same(mockVertex2Provider),
                same(mockThicknessProvider),
                eq(STIPPLE_PATTERN),
                eq(STIPPLE_FACTOR),
                same(mockColorProvider),
                eq(Z),
                eq(UUID),
                isNull()
        );
    }
}
