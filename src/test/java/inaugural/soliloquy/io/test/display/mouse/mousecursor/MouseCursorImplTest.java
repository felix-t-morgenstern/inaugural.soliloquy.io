package inaugural.soliloquy.io.test.display.mouse.mousecursor;

import inaugural.soliloquy.io.mouse.MouseCursorImpl;
import inaugural.soliloquy.io.test.display.DisplayTest;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.util.List;
import java.util.Map;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;

public class MouseCursorImplTest extends DisplayTest {
    protected static Map<String, ProviderAtTime<Long>> MouseCursorProviders = mapOf();

    /** @noinspection rawtypes, unused */
    public static List<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager) {
        MouseCursor = new MouseCursorImpl(MouseCursorProviders, GLOBAL_CLOCK);
        FrameTimer.ShouldExecuteNextFrame = true;

        return listOf();
    }
}
