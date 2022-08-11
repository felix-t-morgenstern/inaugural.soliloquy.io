package inaugural.soliloquy.graphics.test.display.renderables.providers.string;

import inaugural.soliloquy.graphics.assets.FontImpl;
import inaugural.soliloquy.graphics.renderables.providers.ProgressiveStringProvider;
import inaugural.soliloquy.graphics.rendering.renderers.TextLineRendererImpl;
import inaugural.soliloquy.graphics.test.display.rendering.renderers.textlinerenderer.TextLineRendererTest;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.common.infrastructure.Pair;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontStyleDefinition;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a string of text, "I appear over time!", white, aligned left, near the
 *    left edge of the window, vertically centered, gradually, starting at 2000ms, and completing
 *    at 6000ms.
 * 2. The window will then close.
 *
 */
public class ProgressiveStringProviderDisplayTest extends TextLineRendererTest {
    private final static String LINE_TEXT = "I appear over time!";

    protected static ProviderAtTime<String> LineTextProvider;
    protected static FakeTextLineRenderable TextLineRenderable;

    public static void main(String[] args) {
        runTest(windowResolutionManager ->
                        ProgressiveStringProviderDisplayTest
                                .generateRenderablesAndRenderersWithMeshAndShader(
                                        windowResolutionManager,
                                        2000L,
                                        4000L
                                ),
                timestamp -> TextLineRenderer.render(TextLineRenderable, timestamp),
                () -> {
                    TextLineRenderable.Font =
                            new FontImpl(FontDefinition, FLOAT_BOX_FACTORY, COORDINATE_FACTORY);
                    FrameTimer.ShouldExecuteNextFrame = true;
                },
                ProgressiveStringProviderDisplayTest::closeAfterSomeTime);
    }

    /** @noinspection rawtypes*/
    protected static List<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager,
            long startOffset,
            long duration) {
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

        Pair<Float,Float> renderingLocation = new Pair<>(0.1f, 0.475f);

        long now = new FakeGlobalClock().globalTimestamp();
        LineTextProvider = new ProgressiveStringProvider(java.util.UUID.randomUUID(), LINE_TEXT,
                now + startOffset, duration, null, null);

        TextLineRenderable = new FakeTextLineRenderable(null,
                new FakeStaticProvider<>(0.05f), 0f,
                LineTextProvider,
                new FakeStaticProvider<>(null), new FakeStaticProvider<>(null), null,
                null, null,
                new FakeStaticProvider<>(renderingLocation),
                java.util.UUID.randomUUID());

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
