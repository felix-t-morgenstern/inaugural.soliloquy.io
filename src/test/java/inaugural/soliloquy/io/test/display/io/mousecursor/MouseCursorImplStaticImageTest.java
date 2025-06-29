package inaugural.soliloquy.io.test.display.io.mousecursor;

import inaugural.soliloquy.io.api.dto.MouseCursorImageDefinitionDTO;
import inaugural.soliloquy.io.graphics.bootstrap.assetfactories.MouseCursorImageFactoryImpl;
import inaugural.soliloquy.io.graphics.bootstrap.tasks.MouseCursorImagePreloaderTask;
import inaugural.soliloquy.io.graphics.renderables.providers.StaticProviderImpl;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.io.graphics.bootstrap.GraphicsCoreLoop;


import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static java.util.UUID.randomUUID;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Test acceptance criteria:
 *
 * 1. This test will display a window of 800x600 pixels for 4000ms with a titlebar reading "New
 * Window". The mouse cursor will be a 90s-looking teal and light blue cursor.
 * 2. The window will then close
 */
class MouseCursorImplStaticImageTest extends MouseCursorImplTest {
    private static final String MOUSE_CURSOR_IMAGE_RELATIVE_LOCATION =
            "./src/test/resources/images/mouse_cursors/cursor_green_default.png";

    public static void main(String[] args) {
        runTest(MouseCursorImplTest::generateRenderablesAndRenderersWithMeshAndShader,
                MouseCursorImplStaticImageTest::graphicsPreloaderLoadAction,
                MouseCursorImplStaticImageTest::actAndCloseAfterSomeTime);
    }

    protected static void graphicsPreloaderLoadAction() {
        new MouseCursorImagePreloaderTask(listOf(new MouseCursorImageDefinitionDTO(
                            MOUSE_CURSOR_IMAGE_RELATIVE_LOCATION,
                            0, 0
                    )),
                new MouseCursorImageFactoryImpl(),
                output -> MouseCursorProviders.put(
                        output.relativeLocation(),
                        new StaticProviderImpl<>(
                                randomUUID(),
                                output.id(),
                                null
                        )
                ))
                .run();
    }

    private static void actAndCloseAfterSomeTime(GraphicsCoreLoop graphicsCoreLoop) {
        MouseCursor.setMouseCursor(MOUSE_CURSOR_IMAGE_RELATIVE_LOCATION);

        CheckedExceptionWrapper.sleep(4000);

        glfwSetWindowShouldClose(graphicsCoreLoop.windowId(), true);
    }
}
