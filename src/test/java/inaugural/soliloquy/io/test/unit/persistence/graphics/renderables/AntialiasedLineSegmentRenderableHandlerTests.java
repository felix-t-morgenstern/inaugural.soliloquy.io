package inaugural.soliloquy.io.test.unit.persistence.graphics.renderables;

import inaugural.soliloquy.io.persistence.graphics.renderables.AntialiasedLineSegmentRenderableHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.renderables.AntialiasedLineSegmentRenderable;
import soliloquy.specs.io.graphics.renderables.factories.AntialiasedLineSegmentRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.UUID;

import static inaugural.soliloquy.tools.random.Random.randomInt;
import static inaugural.soliloquy.tools.random.Random.randomString;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AntialiasedLineSegmentRenderableHandlerTests {
    private final String VERTEX_1_PROVIDER_WRITTEN = randomString();
    private final String VERTEX_2_PROVIDER_WRITTEN = randomString();
    private final String COLOR_PROVIDER_WRITTEN = randomString();
    private final String THICKNESS_PROVIDER_WRITTEN = randomString();
    private final String THICKNESS_GRADIENT_PERCENT_PROVIDER_WRITTEN = randomString();
    private final String LENGTH_GRADIENT_PERCENT_PROVIDER_WRITTEN = randomString();
    private final int Z = randomInt();
    private final UUID UUID = randomUUID();

    @Mock private ProviderAtTime<Vertex> mockVertex1Provider;
    @Mock private ProviderAtTime<Vertex> mockVertex2Provider;
    @Mock private ProviderAtTime<Color> mockColorProvider;
    @Mock private ProviderAtTime<Float> mockThicknessProvider;
    @Mock private ProviderAtTime<Float> mockThicknessGradientPercentProvider;
    @Mock private ProviderAtTime<Float> mockLengthGradientPercentProvider;
    @SuppressWarnings("rawtypes") @Mock private TypeHandler<ProviderAtTime> mockProviderHandler;

    @Mock private AntialiasedLineSegmentRenderable mockRenderable;
    @Mock private AntialiasedLineSegmentRenderableFactory mockFactory;

    private String writtenValue = "";

    private TypeHandler<AntialiasedLineSegmentRenderable> handler;

    @BeforeEach
    public void setUp() {
        writtenValue = String.format(
                "{\"vertex1\":\"%s\",\"vertex2\":\"%s\",\"color\":\"%s\",\"thickness\":\"%s\"," +
                        "\"thicknessGradientPercent\":\"%s\",\"lengthGradientPercent\":\"%s\"," +
                        "\"z\":%d,\"uuid\":\"%s\",\"type\":\"%s\"}",
                VERTEX_1_PROVIDER_WRITTEN, VERTEX_2_PROVIDER_WRITTEN, COLOR_PROVIDER_WRITTEN,
                THICKNESS_PROVIDER_WRITTEN, THICKNESS_GRADIENT_PERCENT_PROVIDER_WRITTEN,
                LENGTH_GRADIENT_PERCENT_PROVIDER_WRITTEN, Z, UUID,
                mockRenderable.getClass().getCanonicalName());

        handler = new AntialiasedLineSegmentRenderableHandler(mockProviderHandler, mockFactory);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new AntialiasedLineSegmentRenderableHandler(null, mockFactory));
        assertThrows(IllegalArgumentException.class,
                () -> new AntialiasedLineSegmentRenderableHandler(mockProviderHandler, null));
    }

    @Test
    public void testWrite() {
        when(mockRenderable.getVertex1Provider()).thenReturn(mockVertex1Provider);
        when(mockRenderable.getVertex2Provider()).thenReturn(mockVertex2Provider);
        when(mockRenderable.getColorProvider()).thenReturn(mockColorProvider);
        when(mockRenderable.getThicknessProvider()).thenReturn(mockThicknessProvider);
        when(mockRenderable.getThicknessGradientPercentProvider()).thenReturn(
                mockThicknessGradientPercentProvider);
        when(mockRenderable.getLengthGradientPercentProvider()).thenReturn(
                mockLengthGradientPercentProvider);
        when(mockRenderable.uuid()).thenReturn(UUID);
        when(mockRenderable.getZ()).thenReturn(Z);
        when(mockProviderHandler.write(mockVertex1Provider)).thenReturn(VERTEX_1_PROVIDER_WRITTEN);
        when(mockProviderHandler.write(mockVertex2Provider)).thenReturn(VERTEX_2_PROVIDER_WRITTEN);
        when(mockProviderHandler.write(mockColorProvider)).thenReturn(COLOR_PROVIDER_WRITTEN);
        when(mockProviderHandler.write(mockThicknessProvider)).thenReturn(
                THICKNESS_PROVIDER_WRITTEN);
        when(mockProviderHandler.write(mockThicknessGradientPercentProvider)).thenReturn(
                THICKNESS_GRADIENT_PERCENT_PROVIDER_WRITTEN);
        when(mockProviderHandler.write(mockLengthGradientPercentProvider)).thenReturn(
                LENGTH_GRADIENT_PERCENT_PROVIDER_WRITTEN);

        var output = handler.write(mockRenderable);

        assertEquals(writtenValue, output);
        verify(mockProviderHandler, times(6)).write(any());
        verify(mockProviderHandler, once()).write(mockVertex1Provider);
        verify(mockProviderHandler, once()).write(mockVertex2Provider);
        verify(mockProviderHandler, once()).write(mockColorProvider);
        verify(mockProviderHandler, once()).write(mockThicknessProvider);
        verify(mockProviderHandler, once()).write(mockThicknessGradientPercentProvider);
        verify(mockProviderHandler, once()).write(mockLengthGradientPercentProvider);
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
        when(mockProviderHandler.read(THICKNESS_GRADIENT_PERCENT_PROVIDER_WRITTEN)).thenReturn(
                mockThicknessGradientPercentProvider);
        when(mockProviderHandler.read(LENGTH_GRADIENT_PERCENT_PROVIDER_WRITTEN)).thenReturn(
                mockLengthGradientPercentProvider);
        when(mockFactory.make(
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

        var renderable = handler.read(writtenValue);

        assertSame(mockRenderable, renderable);
        verify(mockFactory, once()).make(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                anyInt(),
                any(),
                any()
        );
        verify(mockFactory, once()).make(
                same(mockVertex1Provider),
                same(mockVertex2Provider),
                same(mockColorProvider),
                same(mockThicknessProvider),
                same(mockThicknessGradientPercentProvider),
                same(mockLengthGradientPercentProvider),
                eq(Z),
                eq(UUID),
                isNull()
        );
    }
}
