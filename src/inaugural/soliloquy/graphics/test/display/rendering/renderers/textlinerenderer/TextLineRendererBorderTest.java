package inaugural.soliloquy.graphics.test.display.rendering.renderers.textlinerenderer;

import inaugural.soliloquy.graphics.assets.FontImpl;
import inaugural.soliloquy.graphics.rendering.renderers.TextLineRendererImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeStaticProvider;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeTextLineRenderable;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.common.infrastructure.Pair;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontDefinition;
import soliloquy.specs.graphics.bootstrap.assetfactories.definitions.FontStyleDefinition;
import soliloquy.specs.graphics.renderables.TextJustification;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static inaugural.soliloquy.graphics.api.Constants.INTACT_COLOR;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

class TextLineRendererBorderTest extends TextLineRendererTest {
    private final static String RELATIVE_LOCATION = "./res/fonts/Oswald-VariableFont_wght.ttf";
    private final static float MAX_LOSSLESS_FONT_SIZE = 200f;
    private final static float ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING = 0.25f;
    private final static Map<Character, Float> GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING =
            new HashMap<>();
    private final static Map<Character, Float> GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT =
            new HashMap<>();
    private final static float ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING = 0.1f;
    private final static float LEADING_ADJUSTMENT = 0f;
    private final static String LINE_TEXT = "Wow, this message has a border!";

    private static FakeTextLineRenderable TextLineRenderable;

    public static void main(String[] args) {
        runTest(
                TextLineRendererBorderTest::generateRenderablesAndRenderersWithMeshAndShader,
                timestamp -> TextLineRenderer.render(TextLineRenderable, timestamp),
                () -> {
                    TextLineRenderable.Font =
                            new FontImpl(FontDefinition, FLOAT_BOX_FACTORY, COORDINATE_FACTORY);
                    FrameTimer.ShouldExecuteNextFrame = true;
                },
                TextLineRendererBorderTest::closeAfterSomeTime);
    }

    /** @noinspection rawtypes */
    private static List<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager) {
        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT.put('j', 0.000625f);

        FontStyleDefinition plain = new FontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING);
        FontStyleDefinition italic = new FontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING);
        FontStyleDefinition bold = new FontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING);
        FontStyleDefinition boldItalic = new FontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING);
        FontDefinition = new FontDefinition("id", RELATIVE_LOCATION,
                MAX_LOSSLESS_FONT_SIZE, LEADING_ADJUSTMENT,
                plain, italic, bold, boldItalic);

        Pair<Float, Float> renderingLocation = new Pair<>(0.5f, 0.45f);

        TextLineRenderable = new FakeTextLineRenderable(null,
                new FakeStaticProvider<>(0.1f), 0f, LINE_TEXT,
                new FakeStaticProvider<>(0.00125f),
                new FakeStaticProvider<>(new Color(255, 25, 119)),
                null, null, null,
                new FakeStaticProvider<>(renderingLocation),
                java.util.UUID.randomUUID());
        TextLineRenderable.Justification = TextJustification.CENTER;

        TextLineRenderer = new TextLineRendererImpl(RENDERING_BOUNDARIES, FLOAT_BOX_FACTORY,
                INTACT_COLOR, windowResolutionManager, null);

        return new ArrayList<Renderer>() {{
            add(TextLineRenderer);
        }};
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(6000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
