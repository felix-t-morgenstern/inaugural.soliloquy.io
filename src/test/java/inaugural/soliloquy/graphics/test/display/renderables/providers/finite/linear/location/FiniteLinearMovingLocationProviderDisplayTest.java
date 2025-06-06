package inaugural.soliloquy.graphics.test.display.renderables.providers.finite.linear.location;

import inaugural.soliloquy.graphics.renderables.TextLineRenderableImpl;
import inaugural.soliloquy.graphics.renderables.providers.FiniteLinearMovingVertexProvider;
import inaugural.soliloquy.graphics.rendering.renderers.TextLineRendererImpl;
import inaugural.soliloquy.graphics.test.display.rendering.renderers.textlinerenderer.TextLineRendererTest;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontStyleDefinition;
import soliloquy.specs.graphics.renderables.TextLineRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.List;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

class FiniteLinearMovingLocationProviderDisplayTest extends TextLineRendererTest {
    private static final String LINE_TEXT = "Whee!!!";

    protected static ProviderAtTime<Vertex> RenderingLocationProvider;
    protected static TextLineRenderable TextLineRenderable;

    /** @noinspection rawtypes */
    protected static List<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager) {
        var plain = new FontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_TRAJAN,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_TRAJAN);
        var italic = new FontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_TRAJAN,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_TRAJAN);
        var bold = new FontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_TRAJAN,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_TRAJAN);
        var boldItalic = new FontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_TRAJAN,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_TRAJAN);
        FontDefinition = new FontDefinition("id", RELATIVE_LOCATION_TRAJAN,
                MAX_LOSSLESS_FONT_SIZE_TRAJAN, LEADING_ADJUSTMENT,
                plain, italic, bold, boldItalic);

        var now = GLOBAL_CLOCK.globalTimestamp();

        RenderingLocationProvider = new FiniteLinearMovingVertexProvider(
                java.util.UUID.randomUUID(),
                mapOf(
                        pairOf(now + 1000, vertexOf(-0.25f, -0.25f)),
                        pairOf(now + 2000, vertexOf(0.75f, 0.5f)),
                        pairOf(now + 3000, vertexOf(-0.25f, 1.25f))
                ),
                null, null);

        TextLineRenderable =
                mockTextLineRenderable(staticProvider(0.05f), 0f, LINE_TEXT, staticNullProvider(0f),
                        staticNullProvider(Color.BLACK), null, listOf(), listOf(),
                        RenderingLocationProvider);

        TextLineRenderer =
                new TextLineRendererImpl(RENDERING_BOUNDARIES, Color.WHITE, windowResolutionManager,
                        null);

        FirstChildStack.add(TextLineRenderable);
        Renderers.registerRenderer(TextLineRenderable.getClass(), TextLineRenderer);

        return listOf(TextLineRenderer);
    }
}
