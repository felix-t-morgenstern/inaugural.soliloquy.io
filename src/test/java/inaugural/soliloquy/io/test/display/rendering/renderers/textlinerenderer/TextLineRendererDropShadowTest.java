package inaugural.soliloquy.io.test.display.rendering.renderers.textlinerenderer;

import inaugural.soliloquy.io.graphics.assets.FontImpl;
import inaugural.soliloquy.io.graphics.rendering.renderers.TextLineRendererImpl;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.io.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.FontDefinition;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.FontStyleDefinition;
import soliloquy.specs.io.graphics.renderables.TextLineRenderable;
import soliloquy.specs.io.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.List;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a string of text, "Quick Message!", white, aligned left, near the left
 * edge of the window, vertically centered, for 8000ms. This text will have a red drop shadow.
 * This drop shadow will be slightly larger than the text, and will be slightly offset below and
 * to the right of the text.
 * 2. The window will then close.
 */
class TextLineRendererDropShadowTest extends TextLineRendererTest {
    private final static String LINE_TEXT = "This text has a drop shadow";

    private static TextLineRenderable TextLineRenderable;

    public static void main(String[] args) {
        runTest(TextLineRendererDropShadowTest::generateRenderablesAndRenderersWithMeshAndShader,
                () -> {
                    when(TextLineRenderable.getFont()).thenReturn(new FontImpl(FontDefinition));
                    FrameTimer.ShouldExecuteNextFrame = true;
                },
                TextLineRendererSimpleTest::closeAfterSomeTime);
    }

    /** @noinspection rawtypes */
    private static List<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
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

        var renderingLocation = vertexOf(0.1f, 0.475f);

        TextLineRenderable = mockTextLineRenderable(
                staticProvider(0.05f), 0f, staticProvider(LINE_TEXT),
                staticNullProvider(0f), staticNullProvider(Color.BLACK), null,
                listOf(), listOf(),
                staticProvider(renderingLocation),
                staticProvider(0.055f),
                staticProvider(vertexOf(0.00125f, 0.00125f)),
                staticProvider(Color.RED));

        TextLineRenderer = new TextLineRendererImpl(RENDERING_BOUNDARIES, Color.WHITE, windowResolutionManager, null);

        FirstChildStack.add(TextLineRenderable);
        Renderers.registerRenderer(TextLineRenderable.getClass(), TextLineRenderer);

        return listOf(TextLineRenderer);
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(8000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
