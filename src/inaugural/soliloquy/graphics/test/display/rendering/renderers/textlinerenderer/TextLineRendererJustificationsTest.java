package inaugural.soliloquy.graphics.test.display.rendering.renderers.textlinerenderer;

import inaugural.soliloquy.graphics.assets.FontImpl;
import inaugural.soliloquy.graphics.rendering.renderers.TextLineRendererImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeFloatBox;
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

import java.util.ArrayList;
import java.util.List;

import static inaugural.soliloquy.graphics.api.Constants.INTACT_COLOR;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

class TextLineRendererJustificationsTest extends TextLineRendererTest {
    private final static float LEADING_ADJUSTMENT = 0.0f;
    private final static String LINE_TEXT_LEFT = "This is left-aligned";
    private final static String LINE_TEXT_CENTER = "This is center-aligned";
    private final static String LINE_TEXT_RIGHT = "This is right-aligned";

    private static FakeTextLineRenderable TextLineRenderableLeft;
    private static FakeTextLineRenderable TextLineRenderableCenter;
    private static FakeTextLineRenderable TextLineRenderableRight;

    public static void main(String[] args) {
        runTest(
                TextLineRendererJustificationsTest::
                        generateRenderablesAndRenderersWithMeshAndShader,
                timestamp -> {
                    TextLineRenderer.render(TextLineRenderableLeft, timestamp);
                    TextLineRenderer.render(TextLineRenderableCenter, timestamp);
                    TextLineRenderer.render(TextLineRenderableRight, timestamp);
                },
                () -> {
                    TextLineRenderableLeft.Font =
                            TextLineRenderableCenter.Font =
                                    TextLineRenderableRight.Font =
                                            new FontImpl(FontDefinition, FLOAT_BOX_FACTORY,
                                                    COORDINATE_FACTORY);

                    FrameTimer.ShouldExecuteNextFrame = true;
                },
                TextLineRendererJustificationsTest::closeAfterSomeTime);
    }

    /** @noinspection rawtypes */
    private static List<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager) {
        RENDERING_BOUNDARIES.CurrentBoundaries = new FakeFloatBox(0.0f, 0.0f, 1.0f, 1.0f);

        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING.put('Q', 0.75f);
        GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING.put('q', 0.75f);

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

        Pair<Float, Float> renderingLocationLeft = new Pair<>(0.05f, 0.225f);
        Pair<Float, Float> renderingLocationCenter = new Pair<>(0.5f, 0.475f);
        Pair<Float, Float> renderingLocationRight = new Pair<>(0.95f, 0.725f);

        TextLineRenderableLeft = new FakeTextLineRenderable(null,
                new FakeStaticProvider<>(0.05f), 0f, LINE_TEXT_LEFT,
                new FakeStaticProvider<>(null), new FakeStaticProvider<>(null), null,
                null, null,
                new FakeStaticProvider<>(renderingLocationLeft),
                java.util.UUID.randomUUID());
        TextLineRenderableCenter = new FakeTextLineRenderable(null,
                new FakeStaticProvider<>(0.05f), 0f, LINE_TEXT_CENTER,
                new FakeStaticProvider<>(null), new FakeStaticProvider<>(null), null,
                null, null,
                new FakeStaticProvider<>(renderingLocationCenter),
                java.util.UUID.randomUUID());
        TextLineRenderableRight = new FakeTextLineRenderable(null,
                new FakeStaticProvider<>(0.05f), 0f, LINE_TEXT_RIGHT,
                new FakeStaticProvider<>(null), new FakeStaticProvider<>(null), null,
                null, null,
                new FakeStaticProvider<>(renderingLocationRight),
                java.util.UUID.randomUUID());

        TextLineRenderableLeft.Justification = TextJustification.LEFT;
        TextLineRenderableCenter.Justification = TextJustification.CENTER;
        TextLineRenderableRight.Justification = TextJustification.RIGHT;

        TextLineRenderer = new TextLineRendererImpl(RENDERING_BOUNDARIES, FLOAT_BOX_FACTORY,
                INTACT_COLOR, windowResolutionManager, null);

        return new ArrayList<Renderer>() {{
            add(TextLineRenderer);
        }};
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(8000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
