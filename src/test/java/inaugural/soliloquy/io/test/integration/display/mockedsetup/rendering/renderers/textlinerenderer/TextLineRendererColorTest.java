package inaugural.soliloquy.io.test.integration.display.mockedsetup.rendering.renderers.textlinerenderer;

import inaugural.soliloquy.io.graphics.assets.FontImpl;
import inaugural.soliloquy.io.graphics.rendering.renderers.TextLineRenderer;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.io.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.FontDefinition;
import soliloquy.specs.io.graphics.bootstrap.assetfactories.definitions.FontStyleDefinition;
import soliloquy.specs.io.graphics.renderables.TextLineRenderable;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.Map;
import java.util.Set;

import static inaugural.soliloquy.io.api.Constants.INTACT_COLOR;
import static inaugural.soliloquy.tools.collections.Collections.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.mockito.Mockito.when;
import static soliloquy.specs.common.valueobjects.Vertex.vertexOf;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a string of text, "Rainbow", aligned left, near the left edge
 * of the window, and in the vertical center of the window, for 4000ms. The message, "This
 * message is in the colors of the rainbow!", will be displayed, with each color having a
 * different color in order of the rainbow.
 * 2. The window will then close.
 */
class TextLineRendererColorTest extends TextLineRendererTest {
    private final static String LINE_TEXT = "Wow, this message is in the colors of the rainbow!";

    private static TextLineRenderable TextLineRenderable;

    public static void main(String[] args) {
        runTest(
                TextLineRendererColorTest::generateRenderablesAndRenderersWithMeshAndShader,
                () -> {
                    when(TextLineRenderable.getFont()).thenReturn(new FontImpl(FontDefinition));
                    FrameTimer.ShouldExecuteNextFrame = true;
                },
                TextLineRendererColorTest::closeAfterSomeTime
        );
    }

    /** @noinspection rawtypes */
    private static Set<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager) {
        GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT.put('j', 0.000625f);

        var plain = new FontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_OSWALD,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_OSWALD);
        var italic = new FontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_OSWALD,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_OSWALD);
        var bold = new FontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_OSWALD,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_OSWALD);
        var boldItalic = new FontStyleDefinition(
                ADDITIONAL_GLYPH_HORIZONTAL_TEXTURE_SPACING_OSWALD,
                GLYPHWISE_ADDITIONAL_HORIZONTAL_TEXTURE_SPACING,
                GLYPHWISE_ADDITIONAL_LEFT_BOUNDARY_SHIFT,
                ADDITIONAL_GLYPH_VERTICAL_TEXTURE_SPACING_OSWALD);
        FontDefinition = new FontDefinition("id", RELATIVE_LOCATION_OSWALD,
                MAX_LOSSLESS_FONT_SIZE_OSWALD, LEADING_ADJUSTMENT,
                plain, italic, bold, boldItalic);

        var renderingLocation = vertexOf(0.1f, 0.475f);

        var colorIndices = rainbowGradient(LINE_TEXT);

        TextLineRenderable =
                mockTextLineRenderable(staticProvider(0.05f), 0f, LINE_TEXT, staticNullProvider(),
                        staticNullProvider(), colorIndices, listOf(), listOf(),
                        staticProvider(renderingLocation));

        TextLineRenderer = new TextLineRenderer(RENDERING_BOUNDARIES, INTACT_COLOR,
                windowResolutionManager::windowWidthToHeightRatio, TimestampValidator);

        when(MockFirstChildComponent.contentsRepresentation()).thenReturn(setOf(TextLineRenderable));
        Renderers.put(TextLineRenderable.getClass(), TextLineRenderer);

        return setOf(TextLineRenderer);
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        CheckedExceptionWrapper.sleep(6000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }

    @SuppressWarnings("SameParameterValue")
    private static Map<Integer, ProviderAtTime<Color>> rainbowGradient(String lineText) {
        Map<Integer, ProviderAtTime<Color>> rainbowGradient = mapOf();

        var degreePerLetter = 360f / lineText.length();
        for (var i = 0; i < lineText.length(); i++) {
            rainbowGradient.put(i, staticProvider(colorAtDegree((float) i * degreePerLetter)));
        }
        return rainbowGradient;
    }

    private static Color colorAtDegree(float degree) {
        var red = getColorComponent(0f, degree);
        var green = getColorComponent(120f, degree);
        var blue = getColorComponent(240f, degree);

        return new Color(red, green, blue, 1f);
    }

    private static float getColorComponent(float componentCenter, float degree) {
        var degreesInCircle = 360f;
        var halfOfCircle = 180f;
        var sixthOfCircle = 60f;
        var degreeModulo = degree % degreesInCircle;
        var distance = componentCenter - degreeModulo;
        if (distance < -halfOfCircle) {
            distance += degreesInCircle;
        }
        var absVal = Math.abs(distance);
        if (absVal <= sixthOfCircle) {
            return 1f;
        }
        absVal -= sixthOfCircle;
        var absValWithCeiling = Math.min(sixthOfCircle, absVal);
        var amountOfSixthOfCircle = sixthOfCircle - absValWithCeiling;
        @SuppressWarnings("UnnecessaryLocalVariable")
        var colorComponent = amountOfSixthOfCircle / sixthOfCircle;
        return colorComponent;
    }
}
