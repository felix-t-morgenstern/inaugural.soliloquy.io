package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.rendering.StackRenderer;

public class FakeStackRenderer implements StackRenderer {
    public int NumberOfTimesRenderCalled;
    public Runnable RenderAction;

    @Override
    public void render() {
        NumberOfTimesRenderCalled++;
        
        if (RenderAction != null) {
            RenderAction.run();
        }
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
