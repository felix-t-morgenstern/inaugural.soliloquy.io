package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.rendering.StackRenderer;

import java.util.ArrayList;

public class FakeStackRenderer implements StackRenderer {
    public int NumberOfTimesRenderCalled;
    public ArrayList<Long> Timestamps = new ArrayList<>();
    public Runnable RenderAction;

    @Override
    public void render(long timestamp) {
        NumberOfTimesRenderCalled++;
        Timestamps.add(timestamp);
        
        if (RenderAction != null) {
            RenderAction.run();
        }
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
