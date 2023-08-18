package inaugural.soliloquy.graphics.test.display.renderables.providers.looping.linear.location;

import inaugural.soliloquy.graphics.renderables.providers.LoopingLinearMovingVertexProvider;
import inaugural.soliloquy.graphics.rendering.renderers.TextLineRendererImpl;
import inaugural.soliloquy.graphics.test.display.rendering.renderers.textlinerenderer.TextLineRendererTest;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontStyleDefinition;
import soliloquy.specs.graphics.renderables.TextLineRenderable;
import soliloquy.specs.graphics.renderables.providers.ResettableProvider;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.List;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.valueobjects.Pair.pairOf;

public class LoopingLinearMovingLocationProviderTest extends TextLineRendererTest {
    private final static String LINE_TEXT = "Wheee!";

    protected static TextLineRenderable TextLineRenderable;

    protected static ResettableProvider<Vertex> LoopingLinearMovingLocationProvider;

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

        var startTimestamp = GLOBAL_CLOCK.globalTimestamp();
        var periodDuration = 4000;
        var periodModuloOffset = periodDuration - (int) (startTimestamp % (periodDuration));
        var valuesAtTimes = mapOf(
                pairOf(0, Vertex.of(0.125f, 0.125f)),
                pairOf(1000, Vertex.of(0.75f, 0.125f)),
                pairOf(2000, Vertex.of(0.75f, 0.75f)),
                pairOf(3000, Vertex.of(0.125f, 0.75f))
        );

        LoopingLinearMovingLocationProvider = new LoopingLinearMovingVertexProvider(
                UUID,
                valuesAtTimes,
                periodDuration,
                periodModuloOffset,
                null,
                null
        );

        TextLineRenderable =
                mockTextLineRenderable(staticProvider(0.05f), 0f, LINE_TEXT, staticNullProvider(0f),
                        staticNullProvider(Color.BLACK), null, listOf(), listOf(),
                        LoopingLinearMovingLocationProvider);

        TextLineRenderer =
                new TextLineRendererImpl(RENDERING_BOUNDARIES, FLOAT_BOX_FACTORY, Color.WHITE,
                        windowResolutionManager, null);

        FirstChildStack.add(TextLineRenderable);
        Renderers.registerRenderer(TextLineRenderable.class.getCanonicalName(), TextLineRenderer);

        return listOf(TextLineRenderer);
    }
}
