package inaugural.soliloquy.io.test.display.rendering.renderers.textlinerenderer;

import inaugural.soliloquy.io.graphics.assets.FontImpl;
import inaugural.soliloquy.io.graphics.rendering.renderers.TextLineRendererImpl;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.io.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.FontDefinition;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.FontStyleDefinition;
import soliloquy.specs.io.graphics.renderables.TextJustification;
import soliloquy.specs.io.graphics.renderables.TextLineRenderable;
import soliloquy.specs.io.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.List;

import static inaugural.soliloquy.io.api.Constants.INTACT_COLOR;
import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

class TextLineRendererJustificationsTest extends TextLineRendererTest {
    private final static float LEADING_ADJUSTMENT = 0.0f;
    private final static String LINE_TEXT_LEFT = "This is left-aligned";
    private final static String LINE_TEXT_CENTER = "This is center-aligned";
    private final static String LINE_TEXT_RIGHT = "This is right-aligned";

    private static TextLineRenderable TextLineRenderableLeft;
    private static TextLineRenderable TextLineRenderableCenter;
    private static TextLineRenderable TextLineRenderableRight;

    public static void main(String[] args) {
        runTest(
                TextLineRendererJustificationsTest::
                        generateRenderablesAndRenderersWithMeshAndShader,
                () -> {
                    when(TextLineRenderableLeft.getFont()).thenReturn(new FontImpl(FontDefinition));
                    when(TextLineRenderableCenter.getFont()).thenReturn(new FontImpl(FontDefinition));
                    when(TextLineRenderableRight.getFont()).thenReturn(new FontImpl(FontDefinition));

                    FrameTimer.ShouldExecuteNextFrame = true;
                },
                TextLineRendererJustificationsTest::closeAfterSomeTime);
    }

    /** @noinspection rawtypes */
    private static List<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager) {
        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING.put('Q', 0.75f);
        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING.put('q', 0.75f);

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

        var renderingLocationLeft = vertexOf(0.05f, 0.225f);
        var renderingLocationCenter = vertexOf(0.5f, 0.475f);
        var renderingLocationRight = vertexOf(0.95f, 0.725f);

        TextLineRenderableLeft = mockTextLineRenderable(staticProvider(0.05f), 0f, LINE_TEXT_LEFT,
                staticNullProvider(0f), staticNullProvider(Color.BLACK), null, listOf(), listOf(),
                staticProvider(renderingLocationLeft));
        TextLineRenderableCenter =
                mockTextLineRenderable(staticProvider(0.05f), 0f, LINE_TEXT_CENTER,
                        staticNullProvider(0f), staticNullProvider(Color.BLACK), null, listOf(),
                        listOf(), staticProvider(renderingLocationCenter));
        TextLineRenderableRight = mockTextLineRenderable(staticProvider(0.05f), 0f, LINE_TEXT_RIGHT,
                staticNullProvider(0f), staticNullProvider(Color.BLACK), null, listOf(), listOf(),
                staticProvider(renderingLocationRight));

        when(TextLineRenderableLeft.getJustification()).thenReturn(TextJustification.LEFT);
        when(TextLineRenderableCenter.getJustification()).thenReturn(TextJustification.CENTER);
        when(TextLineRenderableRight.getJustification()).thenReturn(TextJustification.RIGHT);

        TextLineRenderer = new TextLineRendererImpl(RENDERING_BOUNDARIES,
                INTACT_COLOR, windowResolutionManager, null);

        FirstChildStack.add(TextLineRenderableLeft);
        FirstChildStack.add(TextLineRenderableCenter);
        FirstChildStack.add(TextLineRenderableRight);
        Renderers.registerRenderer(TextLineRenderableLeft.getClass(), TextLineRenderer);
        Renderers.registerRenderer(TextLineRenderableCenter.getClass(), TextLineRenderer);
        Renderers.registerRenderer(TextLineRenderableRight.getClass(), TextLineRenderer);

        return listOf(TextLineRenderer);
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(8000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
