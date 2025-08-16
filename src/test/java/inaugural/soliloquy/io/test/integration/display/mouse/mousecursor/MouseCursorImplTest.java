package inaugural.soliloquy.io.test.integration.display.mouse.mousecursor;

import inaugural.soliloquy.io.mouse.MouseCursorImpl;
import inaugural.soliloquy.io.test.integration.display.DisplayTest;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.util.Map;
import java.util.Set;

import static inaugural.soliloquy.tools.collections.Collections.*;

public class MouseCursorImplTest extends DisplayTest {
    protected static Map<String, ProviderAtTime<Long>> MouseCursorProviders = mapOf();

    /** @noinspection rawtypes, unused */
    public static Set<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager) {
        MouseCursor = new MouseCursorImpl(MouseCursorProviders::get, GLOBAL_CLOCK);
        FrameTimer.ShouldExecuteNextFrame = true;

        return setOf();
    }
}
