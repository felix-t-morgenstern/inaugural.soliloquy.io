package inaugural.soliloquy.graphics.test.display.io.mousecursor;

import inaugural.soliloquy.graphics.io.MouseCursorImpl;
import inaugural.soliloquy.graphics.test.display.DisplayTest;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.util.ArrayList;
import java.util.HashMap;

public class MouseCursorImplTest extends DisplayTest {
    protected static HashMap<String, ProviderAtTime<Long>> _mouseCursorProviders = new HashMap<>();

    /** @noinspection rawtypes, unused */
    public static java.util.List<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager) {
        MouseCursor = new MouseCursorImpl(_mouseCursorProviders, GLOBAL_CLOCK);
        FrameTimer.ShouldExecuteNextFrame = true;

        return new ArrayList<>();
    }
}
