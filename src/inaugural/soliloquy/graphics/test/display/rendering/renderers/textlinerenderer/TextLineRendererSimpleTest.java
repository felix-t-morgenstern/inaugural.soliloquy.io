package inaugural.soliloquy.graphics.test.display.rendering.renderers.textlinerenderer;

import inaugural.soliloquy.common.test.fakes.FakePair;
import inaugural.soliloquy.graphics.assets.FontImpl;
import inaugural.soliloquy.graphics.rendering.renderers.TextLineRendererImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a string of text, "Quick Message!", white, aligned left, near the left
 *    edge of the window, vertically centered, for 8000ms.
 * 2. The window will then close.
 *
 */
class TextLineRendererSimpleTest extends TextLineRendererTest {
    private final static String LINE_TEXT = "Quick Message!";

    private static FakeTextLineRenderable TextLineRenderable;

    public static void main(String[] args) {
        runTest(TextLineRendererSimpleTest::generateRenderablesAndRenderersWithMeshAndShader,
                timestamp -> TextLineRenderer.render(TextLineRenderable, timestamp),
                () -> {
                    TextLineRenderable.Font =
                            new FontImpl(FontDefinition, FLOAT_BOX_FACTORY, COORDINATE_FACTORY);
                    FrameTimer.ShouldExecuteNextFrame = true;
                },
                TextLineRendererSimpleTest::closeAfterSomeTime);
    }

    /** @noinspection rawtypes*/
    private static List<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager) {
        FakeFontStyleDefinition plain = new FakeFontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_TRAJAN,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_TRAJAN);
        FakeFontStyleDefinition italic = new FakeFontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_TRAJAN,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_TRAJAN);
        FakeFontStyleDefinition bold = new FakeFontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_TRAJAN,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_TRAJAN);
        FakeFontStyleDefinition boldItalic = new FakeFontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_TRAJAN,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_TRAJAN);
        FontDefinition = new FakeFontDefinition("id", RELATIVE_LOCATION_TRAJAN,
                MAX_LOSSLESS_FONT_SIZE_TRAJAN,
                plain, italic, bold, boldItalic,
                LEADING_ADJUSTMENT);

        FakePair<Float,Float> renderingLocation = new FakePair<>(0.1f, 0.475f);

        TextLineRenderable = new FakeTextLineRenderable(null,
                new FakeStaticProvider<>(0.05f), 0f, LINE_TEXT,
                new FakeStaticProvider<>(null), new FakeStaticProvider<>(null), null,
                null, null,
                new FakeStaticProvider<>(renderingLocation),
                new FakeEntityUuid());

        TextLineRenderer =
                new TextLineRendererImpl(RENDERING_BOUNDARIES, FLOAT_BOX_FACTORY, Color.WHITE,
                        windowResolutionManager, null);

        return new ArrayList<Renderer>() {{
            add(TextLineRenderer);
        }};
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(8000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
