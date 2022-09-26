package inaugural.soliloquy.graphics.test.display.renderables.providers.finite.linear.location;

import inaugural.soliloquy.graphics.renderables.providers.FiniteLinearMovingVertexProvider;
import inaugural.soliloquy.graphics.rendering.renderers.TextLineRendererImpl;
import inaugural.soliloquy.graphics.test.display.rendering.renderers.textlinerenderer.TextLineRendererTest;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeStaticProvider;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeTextLineRenderable;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontStyleDefinition;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class FiniteLinearMovingLocationProviderDisplayTest extends TextLineRendererTest {
    private static final String LINE_TEXT = "Whee!!!";

    protected static ProviderAtTime<Vertex> RenderingLocationProvider;
    protected static FakeTextLineRenderable TextLineRenderable;

    /** @noinspection rawtypes */
    protected static List<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager) {
        FontStyleDefinition plain = new FontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_TRAJAN,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_TRAJAN);
        FontStyleDefinition italic = new FontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_TRAJAN,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_TRAJAN);
        FontStyleDefinition bold = new FontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_TRAJAN,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_TRAJAN);
        FontStyleDefinition boldItalic = new FontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_TRAJAN,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_TRAJAN);
        FontDefinition = new FontDefinition("id", RELATIVE_LOCATION_TRAJAN,
                MAX_LOSSLESS_FONT_SIZE_TRAJAN, LEADING_ADJUSTMENT,
                plain, italic, bold, boldItalic);

        long now = GLOBAL_CLOCK.globalTimestamp();

        RenderingLocationProvider = new FiniteLinearMovingVertexProvider(
                java.util.UUID.randomUUID(),
                new HashMap<Long, Vertex>() {{
                    put(now + 1000, Vertex.of(-0.25f, -0.25f));
                    put(now + 2000, Vertex.of(0.75f, 0.5f));
                    put(now + 3000, Vertex.of(-0.25f, 1.25f));
                }},
                null, null);

        TextLineRenderable = new FakeTextLineRenderable(null,
                new FakeStaticProvider<>(0.05f), 0f, LINE_TEXT,
                new FakeStaticProvider<>(null), new FakeStaticProvider<>(null), null,
                null, null,
                RenderingLocationProvider,
                java.util.UUID.randomUUID());

        TextLineRenderer =
                new TextLineRendererImpl(RENDERING_BOUNDARIES, FLOAT_BOX_FACTORY, Color.WHITE,
                        windowResolutionManager, null);

        return new ArrayList<Renderer>() {{
            add(TextLineRenderer);
        }};
    }
}
