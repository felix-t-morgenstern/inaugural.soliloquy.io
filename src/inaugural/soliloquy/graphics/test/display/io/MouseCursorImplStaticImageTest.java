package inaugural.soliloquy.graphics.test.display.io;

import inaugural.soliloquy.graphics.api.dto.MouseCursorImageDTO;
import inaugural.soliloquy.graphics.bootstrap.tasks.MouseCursorImagePreloaderTask;
import inaugural.soliloquy.graphics.renderables.providers.StaticProviderImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeEntityUuid;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.bootstrap.GraphicsCoreLoop;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 800x600 pixels for 4000ms with a titlebar reading "New
 *    Window". The mouse cursor will be a 90s-looking teal and light blue cursor.
 * 2. The window will then close
 *
 */
class MouseCursorImplStaticImageTest extends MouseCursorImplTest {
    private static final String MOUSE_CURSOR_IMAGE_RELATIVE_LOCATION =
            "./res/images/mouse_cursors/cursor_green_default.png";

    public static void main(String[] args) {
        runTest(MouseCursorImplTest::generateRenderablesAndRenderersWithMeshAndShader,
                timestamp -> {},
                MouseCursorImplStaticImageTest::graphicsPreloaderLoadAction,
                MouseCursorImplStaticImageTest::actAndCloseAfterSomeTime);
    }

    protected static void graphicsPreloaderLoadAction() {
        new MouseCursorImagePreloaderTask(
                new MouseCursorImageDTO(MOUSE_CURSOR_IMAGE_RELATIVE_LOCATION, 0, 0),
                relativeLocation -> mouseCursorImage ->
                        _mouseCursorProviders.put(relativeLocation,
                                new StaticProviderImpl<>(new FakeEntityUuid(), mouseCursorImage,
                                        null)))
                .run();
    }

    private static void actAndCloseAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        MouseCursor.setMouseCursor(MOUSE_CURSOR_IMAGE_RELATIVE_LOCATION);

        CheckedExceptionWrapper.sleep(4000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
