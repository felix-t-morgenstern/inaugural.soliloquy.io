package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.renderers.StackRenderer;

import java.util.ArrayList;
import java.util.function.Consumer;

public class FakeStackRenderer implements StackRenderer {
    public int NumberOfTimesRenderCalled;
    public ArrayList<Long> Timestamps = new ArrayList<>();
    public Consumer<Long> RenderAction;

    @Override
    public void render(RenderableStack renderableStack, long timestamp) {
        NumberOfTimesRenderCalled++;
        Timestamps.add(timestamp);

        if (RenderAction != null) {
            RenderAction.accept(timestamp);
        }
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
