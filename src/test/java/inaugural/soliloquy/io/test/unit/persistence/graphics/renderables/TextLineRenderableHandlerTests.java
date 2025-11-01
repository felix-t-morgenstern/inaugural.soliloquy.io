package inaugural.soliloquy.io.test.unit.persistence.graphics.renderables;

import inaugural.soliloquy.io.persistence.graphics.renderables.TextLineRenderableHandler;
import inaugural.soliloquy.io.persistence.graphics.renderables.providers.ProviderHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.assets.Font;
import soliloquy.specs.io.graphics.renderables.HorizontalAlignment;
import soliloquy.specs.io.graphics.renderables.TextLineRenderable;
import soliloquy.specs.io.graphics.renderables.factories.TextLineRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.UUID;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.random.Random.*;
import static inaugural.soliloquy.tools.testing.Assertions.once;
import static inaugural.soliloquy.tools.testing.Mock.generateMockLookupFunctionWithId;
import static inaugural.soliloquy.tools.testing.Mock.LookupAndEntitiesWithId;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

@ExtendWith(MockitoExtension.class)
public class TextLineRenderableHandlerTests {
    private final String FONT_ID = randomString();
    private final LookupAndEntitiesWithId<Font> MOCK_FONT_AND_LOOKUP =
            generateMockLookupFunctionWithId(Font.class, FONT_ID);
    private final Font MOCK_FONT = MOCK_FONT_AND_LOOKUP.entities.getFirst();
    private final Function<String, Font> MOCK_GET_FONT = MOCK_FONT_AND_LOOKUP.lookup;
    private final HorizontalAlignment ALIGNMENT =
            HorizontalAlignment.fromValue(randomIntInRange(0, 3));
    private final float GLYPH_PADDING = randomFloat();
    private final int COLOR_PROVIDER_INDEX = randomInt();
    private final int ITALIC_INDEX = randomInt();
    private final int BOLD_INDEX = randomInt();

    private final int Z = randomInt();
    private final UUID UUID = randomUUID();

    private final String TEXT_WRITTEN = randomString();
    private final String LOC_WRITTEN = randomString();
    private final String HEIGHT_WRITTEN = randomString();
    private final String COLOR_WRITTEN = randomString();
    private final String BORDER_THICKNESS_WRITTEN = randomString();
    private final String BORDER_COLOR_WRITTEN = randomString();
    private final String DROP_SHADOW_SIZE_WRITTEN = randomString();
    private final String DROP_SHADOW_OFFSET_WRITTEN = randomString();
    private final String DROP_SHADOW_COLOR_WRITTEN = randomString();
    @Mock private ProviderHandler mockProviderHandler;
    @Mock private ProviderAtTime<String> mockTextProvider;
    @Mock private ProviderAtTime<Vertex> mockLocProvider;
    @Mock private ProviderAtTime<Float> mockHeightProvider;
    @Mock private ProviderAtTime<Color> mockColorProvider;
    @Mock private ProviderAtTime<Float> mockBorderThicknessProvider;
    @Mock private ProviderAtTime<Color> mockBorderColorProvider;
    @Mock private ProviderAtTime<Float> mockDropShadowSizeProvider;
    @Mock private ProviderAtTime<Vertex> mockDropShadowOffsetProvider;
    @Mock private ProviderAtTime<Color> mockDropShadowColorProvider;

    @Mock private TextLineRenderable mockRenderable;
    @Mock private TextLineRenderableFactory mockFactory;

    private final String WRITTEN_VALUE = String.format(
            "{\"fontId\":\"%s\",\"text\":\"%s\",\"loc\":\"%s\",\"height\":\"%s\",\"just\":%d," +
                    "\"padding\":%s,\"colors\":[{\"index\":%d,\"color\":\"%s\"}],\"italic\":[%d]," +
                    "\"bold\":[%d],\"borderThickness\":\"%s\",\"borderColor\":\"%s\"," +
                    "\"shadowSize\":\"%s\",\"shadowOffset\":\"%s\",\"shadowColor\":\"%s\"," +
                    "\"z\":%d,\"uuid\":\"%s\"}",
            FONT_ID, TEXT_WRITTEN, LOC_WRITTEN, HEIGHT_WRITTEN, ALIGNMENT.getValue(),
            GLYPH_PADDING, COLOR_PROVIDER_INDEX, COLOR_WRITTEN, ITALIC_INDEX, BOLD_INDEX,
            BORDER_THICKNESS_WRITTEN, BORDER_COLOR_WRITTEN, DROP_SHADOW_SIZE_WRITTEN,
            DROP_SHADOW_OFFSET_WRITTEN, DROP_SHADOW_COLOR_WRITTEN, Z, UUID
    );

    private TypeHandler<TextLineRenderable> handler;

    @BeforeEach
    public void setUp() {
        handler = new TextLineRenderableHandler(MOCK_GET_FONT, mockProviderHandler, mockFactory);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new TextLineRenderableHandler(null, mockProviderHandler, mockFactory));
        assertThrows(IllegalArgumentException.class,
                () -> new TextLineRenderableHandler(MOCK_GET_FONT, null, mockFactory));
        assertThrows(IllegalArgumentException.class,
                () -> new TextLineRenderableHandler(MOCK_GET_FONT, mockProviderHandler, null));
    }

    @Test
    public void testWrite() {
        when(mockRenderable.getFont()).thenReturn(MOCK_FONT);
        when(mockRenderable.getLineTextProvider()).thenReturn(mockTextProvider);
        when(mockRenderable.getRenderingLocationProvider()).thenReturn(mockLocProvider);
        when(mockRenderable.lineHeightProvider()).thenReturn(mockHeightProvider);
        when(mockRenderable.getAlignment()).thenReturn(ALIGNMENT);
        when(mockRenderable.getPaddingBetweenGlyphs()).thenReturn(GLYPH_PADDING);
        when(mockRenderable.colorProviderIndices()).thenReturn(
                mapOf(pairOf(COLOR_PROVIDER_INDEX, mockColorProvider)));
        when(mockRenderable.italicIndices()).thenReturn(listOf(ITALIC_INDEX));
        when(mockRenderable.boldIndices()).thenReturn(listOf(BOLD_INDEX));
        when(mockRenderable.getBorderThicknessProvider()).thenReturn(mockBorderThicknessProvider);
        when(mockRenderable.getBorderColorProvider()).thenReturn(mockBorderColorProvider);
        when(mockRenderable.dropShadowSizeProvider()).thenReturn(mockDropShadowSizeProvider);
        when(mockRenderable.dropShadowOffsetProvider()).thenReturn(mockDropShadowOffsetProvider);
        when(mockRenderable.dropShadowColorProvider()).thenReturn(mockDropShadowColorProvider);
        when(mockRenderable.getZ()).thenReturn(Z);
        when(mockRenderable.uuid()).thenReturn(UUID);

        when(mockProviderHandler.write(mockTextProvider)).thenReturn(TEXT_WRITTEN);
        when(mockProviderHandler.write(mockLocProvider)).thenReturn(LOC_WRITTEN);
        when(mockProviderHandler.write(mockHeightProvider)).thenReturn(HEIGHT_WRITTEN);
        when(mockProviderHandler.write(mockColorProvider)).thenReturn(COLOR_WRITTEN);
        when(mockProviderHandler.write(mockBorderThicknessProvider))
                .thenReturn(BORDER_THICKNESS_WRITTEN);
        when(mockProviderHandler.write(mockBorderColorProvider)).thenReturn(BORDER_COLOR_WRITTEN);
        when(mockProviderHandler.write(mockDropShadowSizeProvider))
                .thenReturn(DROP_SHADOW_SIZE_WRITTEN);
        when(mockProviderHandler.write(mockDropShadowOffsetProvider))
                .thenReturn(DROP_SHADOW_OFFSET_WRITTEN);
        when(mockProviderHandler.write(mockDropShadowColorProvider))
                .thenReturn(DROP_SHADOW_COLOR_WRITTEN);

        var output = handler.write(mockRenderable);

        assertEquals(WRITTEN_VALUE, output);
        verify(mockRenderable, once()).getFont();
        verify(mockRenderable, once()).getLineTextProvider();
        verify(mockRenderable, once()).getRenderingLocationProvider();
        verify(mockRenderable, once()).lineHeightProvider();
        verify(mockRenderable, once()).getAlignment();
        verify(mockRenderable, once()).getPaddingBetweenGlyphs();
        verify(mockRenderable, once()).colorProviderIndices();
        verify(mockRenderable, once()).italicIndices();
        verify(mockRenderable, once()).boldIndices();
        verify(mockRenderable, once()).getBorderThicknessProvider();
        verify(mockRenderable, once()).getBorderColorProvider();
        verify(mockRenderable, once()).dropShadowSizeProvider();
        verify(mockRenderable, once()).dropShadowOffsetProvider();
        verify(mockRenderable, once()).dropShadowColorProvider();
        verify(mockRenderable, once()).getZ();
        verify(mockRenderable, once()).uuid();

        verify(mockProviderHandler, once()).write(mockTextProvider);
        verify(mockProviderHandler, once()).write(mockLocProvider);
        verify(mockProviderHandler, once()).write(mockHeightProvider);
        verify(mockProviderHandler, once()).write(mockColorProvider);
        verify(mockProviderHandler, once()).write(mockBorderThicknessProvider);
        verify(mockProviderHandler, once()).write(mockBorderColorProvider);
        verify(mockProviderHandler, once()).write(mockDropShadowSizeProvider);
        verify(mockProviderHandler, once()).write(mockDropShadowOffsetProvider);
        verify(mockProviderHandler, once()).write(mockDropShadowColorProvider);
    }

    @Test
    public void testWriteWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> handler.write(null));
    }

    @Test
    public void testRead() {
        when(mockProviderHandler.read(TEXT_WRITTEN)).thenReturn(mockTextProvider);
        when(mockProviderHandler.read(LOC_WRITTEN)).thenReturn(mockLocProvider);
        when(mockProviderHandler.read(HEIGHT_WRITTEN)).thenReturn(mockHeightProvider);
        when(mockProviderHandler.read(COLOR_WRITTEN)).thenReturn(mockColorProvider);
        when(mockProviderHandler.read(BORDER_THICKNESS_WRITTEN)).thenReturn(
                mockBorderThicknessProvider);
        when(mockProviderHandler.read(BORDER_COLOR_WRITTEN)).thenReturn(mockBorderColorProvider);
        when(mockProviderHandler.read(DROP_SHADOW_SIZE_WRITTEN)).thenReturn(
                mockDropShadowSizeProvider);
        when(mockProviderHandler.read(DROP_SHADOW_OFFSET_WRITTEN)).thenReturn(
                mockDropShadowOffsetProvider);
        when(mockProviderHandler.read(DROP_SHADOW_COLOR_WRITTEN)).thenReturn(
                mockDropShadowColorProvider);

        when(mockFactory.make(
                any(),
                any(),
                any(),
                any(),
                any(),
                anyFloat(),
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

        var output = handler.read(WRITTEN_VALUE);

        assertSame(mockRenderable, output);
        verify(MOCK_GET_FONT, once()).apply(FONT_ID);
        verify(mockProviderHandler, once()).read(TEXT_WRITTEN);
        verify(mockProviderHandler, once()).read(LOC_WRITTEN);
        verify(mockProviderHandler, once()).read(HEIGHT_WRITTEN);
        verify(mockProviderHandler, once()).read(COLOR_WRITTEN);
        verify(mockProviderHandler, once()).read(BORDER_THICKNESS_WRITTEN);
        verify(mockProviderHandler, once()).read(BORDER_COLOR_WRITTEN);
        verify(mockProviderHandler, once()).read(DROP_SHADOW_SIZE_WRITTEN);
        verify(mockProviderHandler, once()).read(DROP_SHADOW_OFFSET_WRITTEN);
        verify(mockProviderHandler, once()).read(DROP_SHADOW_COLOR_WRITTEN);
        verify(mockFactory, once()).make(
                same(MOCK_FONT),
                same(mockTextProvider),
                same(mockLocProvider),
                same(mockHeightProvider),
                eq(ALIGNMENT),
                anyFloat(),
                eq(mapOf(pairOf(COLOR_PROVIDER_INDEX, mockColorProvider))),
                eq(listOf(ITALIC_INDEX)),
                eq(listOf(BOLD_INDEX)),
                same(mockBorderThicknessProvider),
                same(mockBorderColorProvider),
                same(mockDropShadowSizeProvider),
                same(mockDropShadowOffsetProvider),
                same(mockDropShadowColorProvider),
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
