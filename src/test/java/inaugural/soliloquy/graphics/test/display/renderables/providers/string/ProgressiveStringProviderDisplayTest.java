package inaugural.soliloquy.graphics.test.display.renderables.providers.string;

import inaugural.soliloquy.graphics.assets.FontImpl;
import inaugural.soliloquy.graphics.renderables.TextLineRenderableImpl;
import inaugural.soliloquy.graphics.renderables.providers.ProgressiveStringProvider;
import inaugural.soliloquy.graphics.rendering.renderers.TextLineRendererImpl;
import inaugural.soliloquy.graphics.test.display.rendering.renderers.textlinerenderer.TextLineRendererTest;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeGlobalClock;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontStyleDefinition;
import soliloquy.specs.graphics.renderables.TextLineRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.List;

import static inaugural.soliloquy.tools.collections.Collections.listOf;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a string of text, "I appear over time!", white, aligned left, near the
 * left edge of the window, vertically centered, gradually, starting at 2000ms, and completing
 * at 6000ms.
 * 2. The window will then close.
 */
public class ProgressiveStringProviderDisplayTest extends TextLineRendererTest {
    private final static String LINE_TEXT = "I appear over time!";

    protected static ProviderAtTime<String> LineTextProvider;
    protected static TextLineRenderable TextLineRenderable;

    public static void main(String[] args) {
        runTest(windowResolutionManager ->
                        ProgressiveStringProviderDisplayTest
                                .generateRenderablesAndRenderersWithMeshAndShader(
                                        windowResolutionManager,
                                        2000L,
                                        4000L
                                ),
                () -> {
                    when(TextLineRenderable.getFont()).thenReturn(new FontImpl(FontDefinition));
                    FrameTimer.ShouldExecuteNextFrame = true;
                },
                ProgressiveStringProviderDisplayTest::closeAfterSomeTime);
    }

    /** @noinspection rawtypes */
    protected static List<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager,
            long startOffset,
            long duration) {
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

        var now = new FakeGlobalClock().globalTimestamp();
        LineTextProvider = new ProgressiveStringProvider(java.util.UUID.randomUUID(), LINE_TEXT,
                now + startOffset, duration, null, null);

        TextLineRenderable = mockTextLineRenderable(
                staticProvider(0.05f), 0f, LineTextProvider, staticNullProvider(0f),
                staticNullProvider(Color.BLACK), null, listOf(), listOf(),
                staticProvider(renderingLocation));

        TextLineRenderer =
                new TextLineRendererImpl(RENDERING_BOUNDARIES, Color.WHITE, windowResolutionManager,
                        null);

        FirstChildStack.add(TextLineRenderable);
        Renderers.registerRenderer(TextLineRenderable.getClass(), TextLineRenderer);

        return listOf(TextLineRenderer);
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(8000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
