package inaugural.soliloquy.io.test.display.rendering.renderers.textlinerenderer;

import inaugural.soliloquy.io.test.display.DisplayTest;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.FontDefinition;
import soliloquy.specs.io.graphics.renderables.TextJustification;
import soliloquy.specs.io.graphics.renderables.TextLineRenderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.List;
import java.util.Map;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

public class TextLineRendererTest extends DisplayTest {
    protected final static String RELATIVE_LOCATION_TRAJAN =
            "./src/test/resources/fonts/Trajan Pro Regular.ttf";
    protected final static float MAX_LOSSLESS_FONT_SIZE_TRAJAN = 100f;
    protected final static float ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_TRAJAN = 0.5f;
    protected final static float ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_TRAJAN = 0.2f;
    protected final static float LEADING_ADJUSTMENT = 0f;
    protected final static String RELATIVE_LOCATION_OSWALD =
            "./src/test/resources/fonts/Oswald-VariableFont_wght.ttf";
    protected final static float MAX_LOSSLESS_FONT_SIZE_OSWALD = 200f;
    protected final static float ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_OSWALD = 0.25f;
    protected final static float ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC_OSWALD = 0.5f;
    protected final static float ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC_OSWALD =
            0.5f;
    protected final static float ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_OSWALD = 0.1f;
    protected final static Map<Character, Float> GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING =
            mapOf();
    protected final static Map<Character, Float> GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT =
            mapOf();

    protected static FontDefinition FontDefinition;
    protected static Renderer<TextLineRenderable> TextLineRenderer;

    protected static TextLineRenderable mockTextLineRenderable(
            ProviderAtTime<Float> lineHeightProvider,
            float paddingBetweenGlyphs,
            String lineText,
            ProviderAtTime<Float> borderThicknessProvider,
            ProviderAtTime<Color> borderColorProvider,
            Map<Integer, ProviderAtTime<Color>> colorProviderIndices,
            List<Integer> italicIndices,
            List<Integer> boldIndices,
            ProviderAtTime<Vertex> renderingLocationProvider) {
        return mockTextLineRenderable(lineHeightProvider, paddingBetweenGlyphs,
                staticProvider(lineText), borderThicknessProvider, borderColorProvider,
                colorProviderIndices, italicIndices, boldIndices, renderingLocationProvider);
    }

    protected static TextLineRenderable mockTextLineRenderable(
            ProviderAtTime<Float> lineHeightProvider,
            float paddingBetweenGlyphs,
            ProviderAtTime<String> lineTextProvider,
            ProviderAtTime<Float> borderThicknessProvider,
            ProviderAtTime<Color> borderColorProvider,
            Map<Integer, ProviderAtTime<Color>> colorProviderIndices,
            List<Integer> italicIndices,
            List<Integer> boldIndices,
            ProviderAtTime<Vertex> renderingLocationProvider) {
        var mockTextLineRenderable = mock(TextLineRenderable.class);

        when(mockTextLineRenderable.lineHeightProvider()).thenReturn(lineHeightProvider);
        when(mockTextLineRenderable.getPaddingBetweenGlyphs()).thenReturn(paddingBetweenGlyphs);
        when(mockTextLineRenderable.getLineTextProvider()).thenReturn(lineTextProvider);
        when(mockTextLineRenderable.getBorderThicknessProvider()).thenReturn(
                borderThicknessProvider);
        when(mockTextLineRenderable.getBorderColorProvider()).thenReturn(borderColorProvider);
        when(mockTextLineRenderable.colorProviderIndices()).thenReturn(colorProviderIndices);
        when(mockTextLineRenderable.italicIndices()).thenReturn(italicIndices);
        when(mockTextLineRenderable.boldIndices()).thenReturn(boldIndices);
        when(mockTextLineRenderable.getRenderingLocationProvider()).thenReturn(
                renderingLocationProvider);
        when(mockTextLineRenderable.uuid()).thenReturn(java.util.UUID.randomUUID());
        when(mockTextLineRenderable.getJustification()).thenReturn(TextJustification.LEFT);
        when(mockTextLineRenderable.dropShadowSizeProvider()).thenReturn(staticNullProvider(0f));
        when(mockTextLineRenderable.dropShadowOffsetProvider()).thenReturn(
                staticNullProvider(vertexOf(0f, 0f)));
        when(mockTextLineRenderable.dropShadowColorProvider()).thenReturn(
                staticNullProvider(Color.BLACK));

        return mockTextLineRenderable(lineHeightProvider, paddingBetweenGlyphs, lineTextProvider,
                borderThicknessProvider, borderColorProvider, colorProviderIndices, italicIndices,
                boldIndices, renderingLocationProvider, staticNullProvider(0f),
                staticNullProvider(vertexOf(0f, 0f)), staticNullProvider(Color.BLACK));
    }

    protected static TextLineRenderable mockTextLineRenderable(
            ProviderAtTime<Float> lineHeightProvider,
            float paddingBetweenGlyphs,
            ProviderAtTime<String> lineTextProvider,
            ProviderAtTime<Float> borderThicknessProvider,
            ProviderAtTime<Color> borderColorProvider,
            Map<Integer, ProviderAtTime<Color>> colorProviderIndices,
            List<Integer> italicIndices,
            List<Integer> boldIndices,
            ProviderAtTime<Vertex> renderingLocationProvider,
            ProviderAtTime<Float> dropShadowSizeProvider,
            ProviderAtTime<Vertex> dropShadowOffsetProvider,
            ProviderAtTime<Color> dropShadowColorProvider) {
        var mockTextLineRenderable = mock(TextLineRenderable.class);

        when(mockTextLineRenderable.lineHeightProvider()).thenReturn(lineHeightProvider);
        when(mockTextLineRenderable.getPaddingBetweenGlyphs()).thenReturn(paddingBetweenGlyphs);
        when(mockTextLineRenderable.getLineTextProvider()).thenReturn(lineTextProvider);
        when(mockTextLineRenderable.getBorderThicknessProvider()).thenReturn(
                borderThicknessProvider);
        when(mockTextLineRenderable.getBorderColorProvider()).thenReturn(borderColorProvider);
        when(mockTextLineRenderable.colorProviderIndices()).thenReturn(colorProviderIndices);
        when(mockTextLineRenderable.italicIndices()).thenReturn(italicIndices);
        when(mockTextLineRenderable.boldIndices()).thenReturn(boldIndices);
        when(mockTextLineRenderable.getRenderingLocationProvider()).thenReturn(
                renderingLocationProvider);
        when(mockTextLineRenderable.uuid()).thenReturn(java.util.UUID.randomUUID());
        when(mockTextLineRenderable.getJustification()).thenReturn(TextJustification.LEFT);
        when(mockTextLineRenderable.dropShadowSizeProvider()).thenReturn(dropShadowSizeProvider);
        when(mockTextLineRenderable.dropShadowOffsetProvider()).thenReturn(
                dropShadowOffsetProvider);
        when(mockTextLineRenderable.dropShadowColorProvider()).thenReturn(dropShadowColorProvider);

        return mockTextLineRenderable;
    }
}
