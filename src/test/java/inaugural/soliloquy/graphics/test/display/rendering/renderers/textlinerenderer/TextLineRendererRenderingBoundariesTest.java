package inaugural.soliloquy.graphics.test.display.rendering.renderers.textlinerenderer;

import inaugural.soliloquy.graphics.assets.FontImpl;
import inaugural.soliloquy.graphics.renderables.TextLineRenderableImpl;
import inaugural.soliloquy.graphics.rendering.renderers.TextLineRendererImpl;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontStyleDefinition;
import soliloquy.specs.graphics.renderables.TextLineRenderable;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.List;

import static inaugural.soliloquy.graphics.api.Constants.INTACT_COLOR;
import static inaugural.soliloquy.graphics.api.Constants.WHOLE_SCREEN;
import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.FloatBox.floatBoxOf;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a string of text, "Quickly Quizzing Quokkas", white, aligned left,
 * near the left edge of the window, vertically centered, for 2000ms. The 'Q' glyphs will have
 * trails which extend to the right, underneath the subsequent glyphs, without pushing those
 * glyphs to the right.
 * 2. The text displayed will be clipped so that only the portions of the text within the left-most
 * and top-most 62.5% of the window are displayed. This will last for 2000ms.
 * 3. The text displayed will be clipped so that only the portions of the text within the
 * right-most and top-most 62.5% of the window are displayed. This will last for 2000ms.
 * 4. The text displayed will be clipped so that only the portions of the text within the
 * right-most and bottom-most 62.5% of the window are displayed. This will last for 2000ms.
 * 5. The text displayed will be clipped so that only the portions of the text within the left-most
 * and bottom-most 62.5% of the window are displayed. This will last for 2000ms.
 * 6. The entirety of the text will be displayed again for 2000ms.
 * 7. The window will then close.
 */
class TextLineRendererRenderingBoundariesTest extends TextLineRendererTest {
    private final static String LINE_TEXT = "Quickly Quizzing Quokkas";

    private static TextLineRenderable TextLineRenderable;

    public static void main(String[] args) {
        runTest(
                TextLineRendererRenderingBoundariesTest::
                        generateRenderablesAndRenderersWithMeshAndShader,
                () -> {
                    when(TextLineRenderable.getFont()).thenReturn(new FontImpl(FontDefinition));
                    FrameTimer.ShouldExecuteNextFrame = true;
                },
                TextLineRendererRenderingBoundariesTest::closeAfterSomeTime);
    }

    /** @noinspection rawtypes */
    private static List<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager) {
        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING.put('Q', 0.75f);

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
                staticProvider(0.05f), 0f, LINE_TEXT, staticNullProvider(0f),
                staticNullProvider(Color.BLACK), null, listOf(), listOf(),
                staticProvider(renderingLocation));

        TextLineRenderer = new TextLineRendererImpl(RENDERING_BOUNDARIES,
                INTACT_COLOR, windowResolutionManager, null);

        FirstChildStack.add(TextLineRenderable);
        Renderers.registerRenderer(TextLineRenderable.getClass(), TextLineRenderer);

        return listOf(TextLineRenderer);
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        int msPerPeriod = 2000;

        CheckedExceptionWrapper.sleep(msPerPeriod);

        FirstChildStack.setRenderingBoundariesProvider(staticProvider(
                floatBoxOf(0.0f, 0.0f, 0.5f, 0.5f)));

        CheckedExceptionWrapper.sleep(msPerPeriod);

        FirstChildStack.setRenderingBoundariesProvider(staticProvider(
                floatBoxOf(0.5f, 0.0f, 1.0f, 0.5f)));

        CheckedExceptionWrapper.sleep(msPerPeriod);

        FirstChildStack.setRenderingBoundariesProvider(staticProvider(
                floatBoxOf(0.5f, 0.5f, 1.0f, 1.0f)));

        CheckedExceptionWrapper.sleep(msPerPeriod);

        FirstChildStack.setRenderingBoundariesProvider(staticProvider(
                floatBoxOf(0.0f, 0.5f, 0.5f, 1.0f)));

        CheckedExceptionWrapper.sleep(msPerPeriod);

        FirstChildStack.setRenderingBoundariesProvider(staticProvider(WHOLE_SCREEN));

        CheckedExceptionWrapper.sleep(msPerPeriod);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
