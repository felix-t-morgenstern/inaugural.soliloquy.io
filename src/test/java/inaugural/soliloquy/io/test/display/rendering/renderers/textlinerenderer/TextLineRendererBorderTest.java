package inaugural.soliloquy.io.test.display.rendering.renderers.textlinerenderer;

import inaugural.soliloquy.io.graphics.assets.FontImpl;
import inaugural.soliloquy.io.graphics.rendering.renderers.TextLineRendererImpl;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.io.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.FontDefinition;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.FontStyleDefinition;
import soliloquy.specs.io.graphics.renderables.TextLineRenderable;
import soliloquy.specs.io.graphics.renderables.TextJustification;
import soliloquy.specs.io.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.List;
import java.util.Map;

import static inaugural.soliloquy.io.api.Constants.INTACT_COLOR;
import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

class TextLineRendererBorderTest extends TextLineRendererTest {
    private final static String RELATIVE_LOCATION =
            "./src/test/resources/fonts/Oswald-VariableFont_wght.ttf";
    private final static float MAX_LOSSLESS_FONT_SIZE = 200f;
    private final static float ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING = 0.25f;
    private final static Map<Character, Float> GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING =
            mapOf();
    private final static Map<Character, Float> GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT =
            mapOf();
    private final static float ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING = 0.1f;
    private final static float LEADING_ADJUSTMENT = 0f;
    private final static String LINE_TEXT = "Wow, this message has a border!";

    private static TextLineRenderable TextLineRenderable;

    public static void main(String[] args) {
        runTest(
                TextLineRendererBorderTest::generateRenderablesAndRenderersWithMeshAndShader,
                () -> {
                    when(TextLineRenderable.getFont()).thenReturn(new FontImpl(FontDefinition));
                    FrameTimer.ShouldExecuteNextFrame = true;
                },
                TextLineRendererBorderTest::closeAfterSomeTime);
    }

    /** @noinspection rawtypes */
    private static List<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager) {
        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT.put('j', 0.000625f);

        var plain = new FontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING);
        var italic = new FontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING);
        var bold = new FontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING);
        var boldItalic = new FontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING);
        FontDefinition = new FontDefinition("id", RELATIVE_LOCATION,
                MAX_LOSSLESS_FONT_SIZE, LEADING_ADJUSTMENT,
                plain, italic, bold, boldItalic);

        var renderingLocation = vertexOf(0.5f, 0.45f);

        TextLineRenderable = mockTextLineRenderable(staticProvider(0.1f), 0f, LINE_TEXT,
                staticProvider(0.00125f), staticProvider(new Color(255, 25, 119)), null, listOf(),
                listOf(), staticProvider(renderingLocation));
        when(TextLineRenderable.getJustification()).thenReturn(TextJustification.CENTER);

        TextLineRenderer = new TextLineRendererImpl(RENDERING_BOUNDARIES,
                INTACT_COLOR, windowResolutionManager, null);

        FirstChildStack.add(TextLineRenderable);
        Renderers.registerRenderer(TextLineRenderable.getClass(), TextLineRenderer);

        return listOf(TextLineRenderer);
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(6000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
