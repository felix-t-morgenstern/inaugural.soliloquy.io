package inaugural.soliloquy.graphics.test.display.rendering.renderers.textlinerenderer;

import inaugural.soliloquy.graphics.assets.FontImpl;
import inaugural.soliloquy.graphics.rendering.FloatBoxImpl;
import inaugural.soliloquy.graphics.rendering.renderers.TextLineRendererImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeFloatBox;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeStaticProvider;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeTextLineRenderable;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.common.valueobjects.Vertex;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontStyleDefinition;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.util.ArrayList;
import java.util.List;

import static inaugural.soliloquy.graphics.api.Constants.INTACT_COLOR;
import static inaugural.soliloquy.graphics.api.Constants.WHOLE_SCREEN;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.mockito.Mockito.when;

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

    private static FakeTextLineRenderable TextLineRenderable;

    public static void main(String[] args) {
        runTest(
                TextLineRendererRenderingBoundariesTest::
                        generateRenderablesAndRenderersWithMeshAndShader,
                timestamp -> TextLineRenderer.render(TextLineRenderable, timestamp),
                () -> {
                    TextLineRenderable.Font = new FontImpl(FontDefinition, FLOAT_BOX_FACTORY);
                    FrameTimer.ShouldExecuteNextFrame = true;
                },
                TextLineRendererRenderingBoundariesTest::closeAfterSomeTime);
    }

    /** @noinspection rawtypes */
    private static List<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager) {
        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING.put('Q', 0.75f);

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

        Vertex renderingLocation = Vertex.of(0.1f, 0.475f);

        TextLineRenderable = new FakeTextLineRenderable(null,
                new FakeStaticProvider<>(0.05f), 0f, LINE_TEXT,
                new FakeStaticProvider<>(null), new FakeStaticProvider<>(null), null,
                null, null,
                new FakeStaticProvider<>(renderingLocation),
                java.util.UUID.randomUUID());

        TextLineRenderer = new TextLineRendererImpl(RENDERING_BOUNDARIES, FLOAT_BOX_FACTORY,
                INTACT_COLOR, windowResolutionManager, null);

        return new ArrayList<Renderer>() {{
            add(TextLineRenderer);
        }};
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        int msPerPeriod = 2000;

        CheckedExceptionWrapper.sleep(msPerPeriod);

        when(RENDERING_BOUNDARIES.currentBoundaries()).thenReturn(new FloatBoxImpl(0.0f, 0.0f, 0.5f, 0.5f));

        CheckedExceptionWrapper.sleep(msPerPeriod);

        when(RENDERING_BOUNDARIES.currentBoundaries()).thenReturn(new FloatBoxImpl(0.5f, 0.0f, 1.0f, 0.5f));

        CheckedExceptionWrapper.sleep(msPerPeriod);

        when(RENDERING_BOUNDARIES.currentBoundaries()).thenReturn(new FloatBoxImpl(0.5f, 0.5f, 1.0f, 1.0f));

        CheckedExceptionWrapper.sleep(msPerPeriod);

        when(RENDERING_BOUNDARIES.currentBoundaries()).thenReturn(new FloatBoxImpl(0.0f, 0.5f, 0.5f, 1.0f));

        CheckedExceptionWrapper.sleep(msPerPeriod);

        when(RENDERING_BOUNDARIES.currentBoundaries()).thenReturn(WHOLE_SCREEN);

        CheckedExceptionWrapper.sleep(msPerPeriod);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
