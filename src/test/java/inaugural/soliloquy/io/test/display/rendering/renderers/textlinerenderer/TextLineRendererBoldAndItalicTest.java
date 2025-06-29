package inaugural.soliloquy.io.test.display.rendering.renderers.textlinerenderer;

import inaugural.soliloquy.io.graphics.assets.FontImpl;
import inaugural.soliloquy.io.graphics.rendering.renderers.TextLineRendererImpl;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.io.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.FontDefinition;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.FontStyleDefinition;
import soliloquy.specs.io.graphics.renderables.TextLineRenderable;
import soliloquy.specs.io.graphics.renderables.providers.StaticProvider;
import soliloquy.specs.io.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.List;
import java.util.Map;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a string of text, "Regular, italic, bold, bold-italic", white, aligned
 * left, near the left edge of the window, vertically centered, with each of the words rendered
 * with the corresponding style.
 * 2. The window will then close.
 */
class TextLineRendererBoldAndItalicTest extends TextLineRendererTest {
    private final static Map<Character, Float> GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING =
            mapOf();
    private final static Map<Character, Float> GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT =
            mapOf();
    private final static float ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING = 0.1f;
    private final static float LEADING_ADJUSTMENT = 0f;
    private final static float LINE_HEIGHT = 0.05f;
    private final static StaticProvider<Float> LINE_HEIGHT_PROVIDER = staticProvider(LINE_HEIGHT);
    private final static String LINE_TEXT = "Regular, italic, bold, bold-italic";

    private static TextLineRenderable TextLineRenderable;

    public static void main(String[] args) {
        runTest(
                TextLineRendererBoldAndItalicTest::
                        generateRenderablesAndRenderersWithMeshAndShader,
                () -> {
                    when(TextLineRenderable.getFont()).thenReturn(new FontImpl(FontDefinition));
                    FrameTimer.ShouldExecuteNextFrame = true;
                },
                TextLineRendererBoldAndItalicTest::closeAfterSomeTime
        );
    }

    /** @noinspection rawtypes */
    private static List<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager) {
        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING.put('Q', 0.75f);
        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING.put('q', 0.75f);

        var plain = new FontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_OSWALD,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING);
        var italic = new FontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_ITALIC_OSWALD,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING);
        var bold = new FontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_OSWALD,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING);
        var boldItalic = new FontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_BOLD_ITALIC_OSWALD,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING);
        FontDefinition = new FontDefinition("id", RELATIVE_LOCATION_OSWALD,
                MAX_LOSSLESS_FONT_SIZE_OSWALD, LEADING_ADJUSTMENT,
                plain, italic, bold, boldItalic);

        var renderingLocation = vertexOf(0.0f, 0.5f - LINE_HEIGHT);

        var italicIndices = listOf(9, 17, 23);

        var boldIndices = listOf(17);

        TextLineRenderable =
                mockTextLineRenderable(LINE_HEIGHT_PROVIDER, 0f, LINE_TEXT, staticNullProvider(0f),
                        staticNullProvider(Color.BLACK), null, italicIndices, boldIndices,
                        staticProvider(renderingLocation));

        TextLineRenderer = new TextLineRendererImpl(RENDERING_BOUNDARIES,
                Color.WHITE, windowResolutionManager, null);

        FirstChildStack.add(TextLineRenderable);
        Renderers.registerRenderer(TextLineRenderable.getClass(), TextLineRenderer);

        return listOf(TextLineRenderer);
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(6000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
