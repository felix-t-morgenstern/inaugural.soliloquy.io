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
import inaugural.soliloquy.graphics.test.display.DisplayTest;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;
import soliloquy.specs.graphics.renderables.SpriteRenderable;
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
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 3000ms
 *    with a titlebar reading "My title bar". The window will contain a picture of a shield,
 *    centered in the window, taking up half of the width and three-fourths of the height of the
 *    window. This sprite will have its colors shifted by 50% of the color wheel, i.e., red (0
 *    degrees) will be cyan (180 degrees), yellow-green (60 degrees) will be blue (240 degrees),
 *    etc.
 * 2. The window will then close.
 *
 */
public class SpriteRendererColorRotationShiftTest extends SpriteRendererTest {
    public static void main(String[] args) {
        FakeNetColorShifts netColorShifts = new FakeNetColorShifts();
        // NB: This should be brought up to 0.5f
        netColorShifts.NetColorRotationShift = 30.5f;
        FakeColorShiftStackAggregator colorShiftStackAggregator =
                new FakeColorShiftStackAggregator(netColorShifts);

        runTest(
                windowResolutionManager -> SpriteRendererTest
                        .generateRenderablesAndRenderersWithMeshAndShader(
                                0f,
                                INTACT_COLOR,
                                colorShiftStackAggregator,
                                windowResolutionManager),
                SpriteRendererTest::stackRendererAction,
                SpriteRendererTest::graphicsPreloaderLoadAction,
                DisplayTest::closeAfterSomeTime);
    }
}
