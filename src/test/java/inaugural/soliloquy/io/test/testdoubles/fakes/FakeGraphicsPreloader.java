package inaugural.soliloquy.io.test.testdoubles.fakes;

import soliloquy.specs.io.graphics.bootstrap.GraphicsPreloader;

import java.util.List;

public class FakeGraphicsPreloader implements GraphicsPreloader {
    public boolean LoadCalled;
    public Runnable LoadAction;
    public List<Object> AddThisWhenLoadIsCalled;

    @Override
    public void load() {
        LoadCalled = true;
        if (AddThisWhenLoadIsCalled != null) {
            AddThisWhenLoadIsCalled.add(this);
        }
        if (LoadAction != null) {
            LoadAction.run();
        }
    }
}
