package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.graphics.rendering.WindowManager;

public class FakeWindowManager implements WindowManager {
    WindowDisplayMode _windowDisplayMode;
    int _width;
    int _height;

    public Runnable UpdateWindowSizeAndLocationAction;
    public int NumberOfTimesUpdateWindowSizeAndLocationActionCalled;

    @Override
    public WindowDisplayMode getWindowDisplayMode() {
        return _windowDisplayMode;
    }

    @Override
    public void setWindowDisplayMode(WindowDisplayMode windowDisplayMode) throws IllegalArgumentException {
        _windowDisplayMode = windowDisplayMode;
    }

    @Override
    public void setDimensions(int i, int i1) throws IllegalArgumentException, UnsupportedOperationException {
        _width = i;
        _height = i1;
    }

    @Override
    public void updateWindowSizeAndLocation() {
        NumberOfTimesUpdateWindowSizeAndLocationActionCalled++;
        if (UpdateWindowSizeAndLocationAction != null) {
            UpdateWindowSizeAndLocationAction.run();
        }
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
