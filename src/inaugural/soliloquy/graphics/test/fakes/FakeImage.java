package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.assets.Image;

public class FakeImage implements Image {
    public String _relativeLocation;

    public int _width;
    public int _height;

    public FakeImage(String relativeLocation, int width, int height) {
        _relativeLocation = relativeLocation;
        _width = width;
        _height = height;
    }

    @Override
    public String relativeLocation() {
        return _relativeLocation;
    }

    @Override
    public int width() {
        return _width;
    }

    @Override
    public int height() {
        return _height;
    }

    @Override
    public boolean supportsMouseEventCapturing() {
        return false;
    }

    @Override
    public boolean capturesMouseEventsAtPixel(int i, int i1) throws UnsupportedOperationException, IllegalArgumentException {
        return false;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
