package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.renderers.StackRenderer;

import java.util.List;
import java.util.function.Consumer;

import static inaugural.soliloquy.tools.collections.Collections.listOf;

public class FakeStackRenderer implements StackRenderer {
    public int NumberOfTimesRenderCalled;
    public List<Long> Timestamps = listOf();
    public Consumer<Long> RenderAction;

    @Override
    public void render(RenderableStack renderableStack, long timestamp) {
        NumberOfTimesRenderCalled++;
        Timestamps.add(timestamp);

        if (RenderAction != null) {
            RenderAction.accept(timestamp);
        }
    }
}
