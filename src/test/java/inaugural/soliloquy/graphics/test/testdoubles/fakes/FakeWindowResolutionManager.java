package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.graphics.rendering.WindowResolutionManager;

import java.util.concurrent.Callable;

public class FakeWindowResolutionManager implements WindowResolutionManager {
    WindowDisplayMode _windowDisplayMode;
    int _width;
    int _height;

    public boolean CallUpdateWindowSizeAndLocationOnlyOnce;
    private boolean UpdateWindowSizeAndLocationCalled;

    public Callable<Long> UpdateWindowSizeAndLocationAction;
    public int NumberOfTimesUpdateWindowSizeAndLocationActionCalled;

    public Integer WindowIdOutput = null;

    @Override
    public WindowDisplayMode getWindowDisplayMode() {
        return _windowDisplayMode;
    }

    @Override
    public void setWindowDisplayMode(WindowDisplayMode windowDisplayMode)
            throws IllegalArgumentException {
        _windowDisplayMode = windowDisplayMode;
    }

    @Override
    public void updateDimensions(int i, int i1)
            throws IllegalArgumentException, UnsupportedOperationException {
        _width = i;
        _height = i1;
    }

    @Override
    public Pair<Integer, Integer> getWindowDimensions() {
        return null;
    }

    @Override
    public float windowWidthToHeightRatio() {
        return 0;
    }

    @Override
    public long updateWindowSizeAndLocation(long windowId, String titlebar) {
        NumberOfTimesUpdateWindowSizeAndLocationActionCalled++;
        if (CallUpdateWindowSizeAndLocationOnlyOnce && UpdateWindowSizeAndLocationCalled) {
            return WindowIdOutput != null ? WindowIdOutput : windowId;
        }
        UpdateWindowSizeAndLocationCalled = true;
        if (UpdateWindowSizeAndLocationAction != null) {
            try {
                return UpdateWindowSizeAndLocationAction.call();
            }
            catch (Exception e) {
                throw new RuntimeException();
            }
        }
        return WindowIdOutput != null ? WindowIdOutput : windowId;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
