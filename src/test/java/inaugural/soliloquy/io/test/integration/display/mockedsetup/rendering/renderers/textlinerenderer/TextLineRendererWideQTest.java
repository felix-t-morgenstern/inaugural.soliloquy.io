package inaugural.soliloquy.io.test.integration.display.mockedsetup.rendering.renderers.textlinerenderer;

import inaugural.soliloquy.io.graphics.assets.FontImpl;
import inaugural.soliloquy.io.graphics.rendering.renderers.TextLineRendererImpl;
import inaugural.soliloquy.tools.exception.CheckedExceptionWrapper;
import soliloquy.specs.io.bootstrap.CoreLoop;
import soliloquy.specs.io.bootstrap.assetfactories.definitions.FontDefinition;
import soliloquy.specs.io.bootstrap.assetfactories.definitions.FontStyleDefinition;
import soliloquy.specs.io.graphics.renderables.TextLineRenderable;
import soliloquy.specs.io.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.Set;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.setOf;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a string of text, "Quickly Quizzing Quokkas", white, aligned left,
 * near the left edge of the window, vertically centered, for 8000ms. The 'Q' glyphs will have
 * trails which extend to the right, underneath the subsequent glyphs, without pushing those
 * glyphs to the right.
 * 2. The window will then close.
 */
class TextLineRendererWideQTest extends TextLineRendererTest {
    private final static String LINE_TEXT = "Quickly Quizzing Quokkas";

    private static TextLineRenderable TextLineRenderable;

    public static void main(String[] args) {
        runTest(TextLineRendererWideQTest::generateRenderablesAndRenderersWithMeshAndShader,
                () -> {
                    when(TextLineRenderable.getFont()).thenReturn(new FontImpl(FontDefinition));
                    FrameTimer.ShouldExecuteNextFrame = true;
                },
                TextLineRendererWideQTest::closeAfterSomeTime);
    }

    /** @noinspection rawtypes */
    private static Set<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager) {
        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING.put('Q', 0.75f);
        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING.put('q', 0.75f);

        var plain = new FontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_TRAJAN,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                GLYPHWISE_WIDTH_FACTORS,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_TRAJAN);
        var italic = new FontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_TRAJAN,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                GLYPHWISE_WIDTH_FACTORS,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_TRAJAN);
        var bold = new FontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_TRAJAN,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                GLYPHWISE_WIDTH_FACTORS,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_TRAJAN);
        var boldItalic = new FontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_TRAJAN,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                GLYPHWISE_WIDTH_FACTORS,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_TRAJAN);
        FontDefinition = new FontDefinition("id", RELATIVE_LOCATION_TRAJAN,
                MAX_LOSSLESS_FONT_SIZE_TRAJAN, LEADING_ADJUSTMENT,
                plain, italic, bold, boldItalic);

        var renderingLocation = vertexOf(0.1f, 0.475f);

        TextLineRenderable = mockTextLineRenderable(
                staticProvider(0.05f), 0f, LINE_TEXT,
                staticNullProvider(), staticNullProvider(), null, listOf(), listOf(),
                staticProvider(renderingLocation));

        TextLineRenderer = new TextLineRendererImpl(RENDERING_BOUNDARIES, Color.WHITE,
                windowResolutionManager::windowWidthToHeightRatio, null);

        when(MockFirstChildComponent.contentsRepresentation()).thenReturn(setOf(TextLineRenderable));
        Renderers.put(TextLineRenderable.getClass(), TextLineRenderer);

        return setOf(TextLineRenderer);
    }

    public static void closeAfterSomeTime(CoreLoop coreLoop) {
        CheckedExceptionWrapper.sleep(8000);

        glfwSetWindowShouldClose(coreLoop.windowId(), true);
    }
}
