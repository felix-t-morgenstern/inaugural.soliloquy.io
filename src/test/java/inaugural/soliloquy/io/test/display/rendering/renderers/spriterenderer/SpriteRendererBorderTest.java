package inaugural.soliloquy.io.test.display.rendering.renderers.spriterenderer;

import inaugural.soliloquy.io.test.display.DisplayTest;

import java.awt.*;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 1920x1080 pixels in the middle of the screen for 3000ms
 * with a titlebar reading "My title bar". The window will contain a picture of a shield,
 * centered in the window, taking up half of the width and three-fourths of the height of the
 * window. This sprite will have a purple border with a thickness of 1% of the height of the
 * screen.
 * 2. The window will then close.
 */
public class SpriteRendererBorderTest extends SpriteRendererTest {
    protected static final Float BORDER_THICKNESS = 0.005f;
    protected static final Color BORDER_COLOR = Color.getHSBColor(0.75f, 1f, 1f);

    public static void main(String[] args) {
        runTest(
                windowResolutionManager -> SpriteRendererTest
                        .generateRenderablesAndRenderersWithMeshAndShader(
                                BORDER_THICKNESS,
                                BORDER_COLOR,
                                null,
                                windowResolutionManager),
                SpriteRendererTest::graphicsPreloaderLoadAction,
                DisplayTest::closeAfterSomeTime);
    }
}
