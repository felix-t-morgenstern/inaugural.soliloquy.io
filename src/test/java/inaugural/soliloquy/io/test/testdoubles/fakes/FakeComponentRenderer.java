package inaugural.soliloquy.io.test.testdoubles.fakes;

import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.rendering.renderers.ComponentRenderer;

import java.util.List;
import java.util.function.Consumer;

import static inaugural.soliloquy.tools.collections.Collections.listOf;

public class FakeComponentRenderer implements ComponentRenderer {
    public int NumberOfTimesRenderCalled;
    public List<Long> Timestamps = listOf();
    public Consumer<Long> RenderAction;

    @Override
    public void render(Component component, long timestamp) {
        NumberOfTimesRenderCalled++;
        Timestamps.add(timestamp);

        if (RenderAction != null) {
            RenderAction.accept(timestamp);
        }
    }
}
