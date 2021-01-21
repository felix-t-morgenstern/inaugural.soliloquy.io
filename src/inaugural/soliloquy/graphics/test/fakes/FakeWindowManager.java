package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.rendering.WindowDisplayMode;
import soliloquy.specs.graphics.rendering.WindowManager;

public class FakeWindowManager implements WindowManager {
    WindowDisplayMode _windowDisplayMode;
    int _width;
    int _height;

    int _xPos;
    int _yPos;

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
    public int windowWidth() throws UnsupportedOperationException {
        return _width;
    }

    @Override
    public int windowHeight() throws UnsupportedOperationException {
        return _height;
    }

    @Override
    public void setLocation(int i, int i1) throws IllegalArgumentException, UnsupportedOperationException {
        _xPos = i;
        _yPos = i1;
    }

    @Override
    public int windowXPos() throws UnsupportedOperationException {
        return _xPos;
    }

    @Override
    public int windowYPos() throws UnsupportedOperationException {
        return _yPos;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
