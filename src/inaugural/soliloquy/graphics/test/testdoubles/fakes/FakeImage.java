package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.assets.Image;

public class FakeImage implements Image {
    public String RelativeLocation;

    public int Width;
    public int Height;

    public boolean SupportsMouseEventCapturing;

    public FakeImage(String relativeLocation) {
        RelativeLocation = relativeLocation;
    }

    public FakeImage(String relativeLocation, int width, int height) {
        RelativeLocation = relativeLocation;
        Width = width;
        Height = height;
    }

    public FakeImage(String relativeLocation, int width, int height, boolean supportsMouseEventCapturing) {
        RelativeLocation = relativeLocation;
        Width = width;
        Height = height;
        SupportsMouseEventCapturing = supportsMouseEventCapturing;
    }

    public FakeImage(boolean supportsMouseEventCapturing) {
        SupportsMouseEventCapturing = supportsMouseEventCapturing;
    }

    @Override
    public int textureId() {
        return 0;
    }

    @Override
    public String relativeLocation() {
        return RelativeLocation;
    }

    @Override
    public int width() {
        return Width;
    }

    @Override
    public int height() {
        return Height;
    }

    @Override
    public boolean supportsMouseEventCapturing() {
        return SupportsMouseEventCapturing;
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
