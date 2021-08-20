package inaugural.soliloquy.graphics.test.display.rendering.renderers.spriterenderer;

import inaugural.soliloquy.common.test.fakes.FakeCoordinateFactory;
import inaugural.soliloquy.graphics.api.WindowResolution;
import inaugural.soliloquy.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.graphics.renderables.providers.StaticProviderImpl;
import inaugural.soliloquy.graphics.rendering.MeshImpl;
import inaugural.soliloquy.graphics.rendering.WindowResolutionManagerImpl;
import inaugural.soliloquy.graphics.rendering.factories.ShaderFactoryImpl;
import inaugural.soliloquy.graphics.rendering.renderers.SpriteRenderer;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.rendering.Mesh;
import soliloquy.specs.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

import static inaugural.soliloquy.graphics.api.Constants.INTACT_COLOR;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 1000ms
 *    with a titlebar reading "My title bar". The window will contain a picture of a shield,
 *    centered in the window, taking up half of the width and three-fourths of the height of the
 *    window.
 * 2. The image displayed will be clipped so that only the portions of the image within the
 *    left-most and top-most 62.5% of the window are displayed. This will last for 1000ms.
 * 3. The image displayed will be clipped so that only the portions of the image within the
 *    right-most and top-most 62.5% of the window are displayed. This will last for 1000ms.
 * 4. The image displayed will be clipped so that only the portions of the image within the
 *    right-most and bottom-most 62.5% of the window are displayed. This will last for 1000ms.
 * 5. The image displayed will be clipped so that only the portions of the image within the
 *    left-most and bottom-most 62.5% of the window are displayed. This will last for 1000ms.
 * 6. The entirety of the image will be displayed again for 1000ms.
 * 7. The window will then close.
 *
 */
public class SpriteRendererRenderingBoundariesTest extends SpriteRendererTest {
    public static void main(String[] args) {
        runTest(
                windowResolutionManager -> SpriteRendererTest
                        .generateRenderablesAndRenderersWithMeshAndShader(
                                0f,
                                INTACT_COLOR,
                                null,
                                windowResolutionManager),
                SpriteRendererTest::stackRendererAction,
                SpriteRendererTest::graphicsPreloaderLoadAction,
                SpriteRendererRenderingBoundariesTest::closeAfterSomeTime);
    }

    public static void closeAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        int msPerPeriod = 1000;
        
        CheckedExceptionWrapper.sleep(msPerPeriod);

        RENDERING_BOUNDARIES.CurrentBoundaries = new FakeFloatBox(0.0f, 0.0f, 0.625f, 0.625f);

        CheckedExceptionWrapper.sleep(msPerPeriod);

        RENDERING_BOUNDARIES.CurrentBoundaries = new FakeFloatBox(0.375f, 0.0f, 1.0f, 0.625f);

        CheckedExceptionWrapper.sleep(msPerPeriod);

        RENDERING_BOUNDARIES.CurrentBoundaries = new FakeFloatBox(0.375f, 0.375f, 1.0f, 1.0f);

        CheckedExceptionWrapper.sleep(msPerPeriod);

        RENDERING_BOUNDARIES.CurrentBoundaries = new FakeFloatBox(0.0f, 0.375f, 0.625f, 1.0f);

        CheckedExceptionWrapper.sleep(msPerPeriod);

        RENDERING_BOUNDARIES.CurrentBoundaries = new FakeFloatBox(0.0f, 0.0f, 1.0f, 1.0f);

        CheckedExceptionWrapper.sleep(msPerPeriod);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
