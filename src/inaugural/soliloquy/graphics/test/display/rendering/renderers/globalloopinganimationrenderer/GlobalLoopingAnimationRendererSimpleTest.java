package inaugural.soliloquy.graphics.test.display.rendering.renderers.globalloopinganimationrenderer;

import inaugural.soliloquy.graphics.api.WindowResolution;
import inaugural.soliloquy.graphics.bootstrap.GraphicsCoreLoopImpl;
import inaugural.soliloquy.graphics.bootstrap.assetfactories.ImageFactoryImpl;
import inaugural.soliloquy.graphics.renderables.GlobalLoopingAnimationRenderableImpl;
import inaugural.soliloquy.graphics.renderables.providers.GlobalLoopingAnimationImpl;
import inaugural.soliloquy.graphics.renderables.providers.StaticProviderImpl;
import inaugural.soliloquy.graphics.rendering.MeshImpl;
import inaugural.soliloquy.graphics.rendering.WindowResolutionManagerImpl;
import inaugural.soliloquy.graphics.rendering.factories.ShaderFactoryImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeEntityUuid;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.assets.Animation;
import soliloquy.specs.graphics.assets.Image;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.rendering.Mesh;
import soliloquy.specs.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 3000ms
 *    with a titlebar reading "My title bar". The window will contain a looping animation of a
 *    torch, centered in the screen, which will change its frames every 250ms. The animation will
 *    persist for 2250ms.
 * 2. The window will then close.
 *
 */
class GlobalLoopingAnimationRendererSimpleTest extends GlobalLoopingAnimationRendererTest {
    public static void main(String[] args) {
        runTest(
                windowResolutionManager -> GlobalLoopingAnimationRendererTest.
                        generateRenderablesAndRenderersWithMeshAndShader(
                                windowResolutionManager,
                                null
                        ),
                timestamp -> GlobalLoopingAnimationRenderer
                        .render(GlobalLoopingAnimationRenderable, timestamp),
                GlobalLoopingAnimationRendererTest::graphicsPreloaderLoadAction,
                graphicsCoreLoop -> closeAfterSomeTime(graphicsCoreLoop, TEST_DURATION_MS)
        );
    }
}
