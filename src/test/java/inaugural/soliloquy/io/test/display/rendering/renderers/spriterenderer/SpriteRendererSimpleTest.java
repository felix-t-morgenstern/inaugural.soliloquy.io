package inaugural.soliloquy.io.test.display.rendering.renderers.spriterenderer;

import inaugural.soliloquy.io.test.display.DisplayTest;

import static inaugural.soliloquy.io.api.Constants.INTACT_COLOR;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 3000ms
 * with a titlebar reading "My title bar". The window will contain a picture of a shield,
 * centered in the window, taking up half of the width and three-fourths of the height of the
 * window.
 * 2. The window will then close.
 */
class SpriteRendererSimpleTest extends SpriteRendererTest {
    public static void main(String[] args) {
        runTest(
                windowResolutionManager -> generateRenderablesAndRenderersWithMeshAndShader(
                                0f,
                                INTACT_COLOR,
                                null,
                                windowResolutionManager),
                SpriteRendererTest::graphicsPreloaderLoadAction,
                DisplayTest::closeAfterSomeTime);
    }
}
