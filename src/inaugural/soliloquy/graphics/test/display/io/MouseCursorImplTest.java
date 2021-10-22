package inaugural.soliloquy.graphics.test.display.io;

import inaugural.soliloquy.graphics.io.MouseCursorImpl;
import inaugural.soliloquy.graphics.test.display.DisplayTest;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.*;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.util.ArrayList;
import java.util.HashMap;

class MouseCursorImplTest extends DisplayTest {
    protected static HashMap<String, ProviderAtTime<Long>> _mouseCursors = new HashMap<>();

    /** @noinspection rawtypes*/
    protected static java.util.List<Renderer> generateRenderablesAndRenderersWithMeshAndShader(
            WindowResolutionManager windowResolutionManager) {
        MouseCursor = new MouseCursorImpl(_mouseCursors, new FakeGlobalClock());
        FrameTimer.ShouldExecuteNextFrame = true;

        return new ArrayList<>();
    }
}
