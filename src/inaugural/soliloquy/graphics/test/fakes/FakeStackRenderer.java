package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.rendering.StackRenderer;

public class FakeStackRenderer implements StackRenderer {
    public int NumberOfTimesRenderCalled;

    @Override
    public void render() {
        NumberOfTimesRenderCalled++;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
