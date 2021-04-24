package inaugural.soliloquy.graphics.test.display;

import inaugural.soliloquy.common.test.fakes.*;
import inaugural.soliloquy.graphics.api.WindowResolution;
import inaugural.soliloquy.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.graphics.renderables.providers.StaticProvider;
import inaugural.soliloquy.graphics.rendering.MeshImpl;
import inaugural.soliloquy.graphics.rendering.WindowResolutionManagerImpl;
import inaugural.soliloquy.graphics.rendering.factories.ShaderFactoryImpl;
import inaugural.soliloquy.graphics.rendering.renderers.TextLineRendererImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.rendering.Mesh;
import soliloquy.specs.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a string of text, "Quickly Quizzing Quokkas", white, aligned left, 
 *    near the left edge of the window, vertically centered, for 2000ms. The 'Q' glyphs will have 
 *    trails which extend to the right, underneath the subsequent glyphs, without pushing those 
 *    glyphs to the right.
 * 2. The text displayed will be clipped so that only the portions of the text within the left-most
 *    and top-most 62.5% of the window are displayed. This will last for 2000ms.
 * 3. The text displayed will be clipped so that only the portions of the text within the
 *    right-most and top-most 62.5% of the window are displayed. This will last for 2000ms.
 * 4. The text displayed will be clipped so that only the portions of the text within the
 *    right-most and bottom-most 62.5% of the window are displayed. This will last for 2000ms.
 * 5. The text displayed will be clipped so that only the portions of the text within the left-most
 *    and bottom-most 62.5% of the window are displayed. This will last for 2000ms.
 * 6. The entirety of the text will be displayed again for 2000ms.
 * 7. The window will then close.
 *
 */
class TextLineRendererRenderingBoundariesTest {
    private final static FakeCoordinateFactory COORDINATE_FACTORY = new FakeCoordinateFactory();
    private final static float[] MESH_DATA =
            new float[] {0f, 1f, 1f, 1f, 1f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};
    private final static FakeRenderingBoundaries RENDERING_BOUNDARIES =
            new FakeRenderingBoundaries();
    private final static String RELATIVE_LOCATION = "./res/fonts/Trajan Pro Regular.ttf";
    private final static float MAX_LOSSLESS_FONT_SIZE = 100f;
    private final static float ADDITIONAL_GLYPH_HORIZONTAL_PADDING = 0.5f;
    private final static float ADDITIONAL_GLYPH_VERTICAL_PADDING = 0.05f;
    private final static float LEADING_ADJUSTMENT = 0.0f;
    private final static FakeFloatBoxFactory FLOAT_BOX_FACTORY = new FakeFloatBoxFactory();
    private final static String LINE_TEXT = "Quickly Quizzing Quokkas";
    private static final String SHADER_FILENAME_PREFIX = "./res/shaders/defaultShader";

    private static FakeTextLineRenderable TextLineRenderable;

    public static void main(String[] args) {
        WindowResolutionManagerImpl windowManager =
                new WindowResolutionManagerImpl(WindowDisplayMode.WINDOWED,
                        WindowResolution.RES_1920x1080, COORDINATE_FACTORY);

        FakeFrameTimer frameTimer = new FakeFrameTimer();
        Function<float[], Function<float[], Mesh>> meshFactory = f1 -> f2 -> new MeshImpl(f1, f2);

        FakeStackRenderer stackRenderer = new FakeStackRenderer();

        RENDERING_BOUNDARIES.CurrentBoundaries = new FakeFloatBox(0.0f, 0.0f, 1.0f, 1.0f);

        Map<Character, Float> glyphwiseAdditionalHorizontalPadding = new HashMap<>();
        glyphwiseAdditionalHorizontalPadding.put('Q', 0.75f);

        FakeFontLoadable font = new FakeFontLoadable(RELATIVE_LOCATION, MAX_LOSSLESS_FONT_SIZE,
                ADDITIONAL_GLYPH_HORIZONTAL_PADDING, glyphwiseAdditionalHorizontalPadding,
                ADDITIONAL_GLYPH_VERTICAL_PADDING, LEADING_ADJUSTMENT, FLOAT_BOX_FACTORY,
                COORDINATE_FACTORY);

        FakeFloatBox renderingArea = new FakeFloatBox(0.1f, 0.475f, 1f, 1f);

        TextLineRenderable = new FakeTextLineRenderable(font, 0.05f, LINE_TEXT, null, null, null,
                new StaticProvider<>(renderingArea));

        FakeGraphicsPreloader graphicsPreloader = new FakeGraphicsPreloader();

        Renderer<soliloquy.specs.graphics.renderables.TextLineRenderable> textLineRenderer =
                new TextLineRendererImpl(RENDERING_BOUNDARIES, FLOAT_BOX_FACTORY, Color.WHITE);

        @SuppressWarnings("rawtypes") Collection<Renderer> renderersWithMesh =
                new ArrayList<Renderer>() {{
                    add(textLineRenderer);
                }};
        @SuppressWarnings("rawtypes") Collection<Renderer> renderersWithShader =
                new ArrayList<Renderer>() {{
                    add(textLineRenderer);
                }};

        stackRenderer.RenderAction = timestamp ->
                textLineRenderer.render(TextLineRenderable, timestamp);

        GraphicsCoreLoop graphicsCoreLoop = new GraphicsCoreLoopImpl("My title bar",
                new FakeGLFWMouseButtonCallback(), frameTimer, 20, windowManager, stackRenderer,
                new ShaderFactoryImpl(), renderersWithShader, SHADER_FILENAME_PREFIX, meshFactory,
                renderersWithMesh, MESH_DATA, MESH_DATA, graphicsPreloader);

        graphicsPreloader.LoadAction = () -> {
            font.load();
            frameTimer.ShouldExecuteNextFrame = true;
        };

        graphicsCoreLoop.startup(() -> closeAfterSomeTime(graphicsCoreLoop));
    }

    private static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        int msPerPeriod = 2000;

        CheckedExceptionWrapper.sleep(msPerPeriod);

        RENDERING_BOUNDARIES.CurrentBoundaries = new FakeFloatBox(0.0f, 0.0f, 0.5f, 0.5f);

        CheckedExceptionWrapper.sleep(msPerPeriod);

        RENDERING_BOUNDARIES.CurrentBoundaries = new FakeFloatBox(0.5f, 0.0f, 1.0f, 0.5f);

        CheckedExceptionWrapper.sleep(msPerPeriod);

        RENDERING_BOUNDARIES.CurrentBoundaries = new FakeFloatBox(0.5f, 0.5f, 1.0f, 1.0f);

        CheckedExceptionWrapper.sleep(msPerPeriod);

        RENDERING_BOUNDARIES.CurrentBoundaries = new FakeFloatBox(0.0f, 0.5f, 0.5f, 1.0f);

        CheckedExceptionWrapper.sleep(msPerPeriod);

        RENDERING_BOUNDARIES.CurrentBoundaries = new FakeFloatBox(0.0f, 0.0f, 1.0f, 1.0f);

        CheckedExceptionWrapper.sleep(msPerPeriod);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
