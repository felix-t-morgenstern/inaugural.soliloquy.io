package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.bootstrap.GraphicsPreloader;

public class FakeGraphicsPreloader implements GraphicsPreloader {
    public boolean LoadCalled;
    public Runnable LoadAction;
    public float PercentageComplete;

    @Override
    public void load() {
        LoadCalled = true;
        if (LoadAction != null) {
            LoadAction.run();
            PercentageComplete = 1f;
        }
    }

    @Override
    public float percentageComplete() {
        return PercentageComplete;
    }

    @Override
    public float percentageComplete(String s) throws IllegalArgumentException {
        return 0;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
