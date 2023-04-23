package inaugural.soliloquy.graphics.test.display.io.mousecursor;

import inaugural.soliloquy.graphics.io.MouseCursorImpl;
import inaugural.soliloquy.graphics.test.display.DisplayTest;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

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
