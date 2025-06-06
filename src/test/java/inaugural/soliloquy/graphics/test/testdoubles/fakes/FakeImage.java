package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.graphics.assets.Image;

import java.util.List;

import static inaugural.soliloquy.tools.collections.Collections.listOf;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;



public class FakeImage implements Image {
    public String RelativeLocation;

    public int Width;
    public int Height;

    public boolean SupportsMouseEventCapturing;

    public List<Pair<Integer, Integer>> CapturesMouseEventsAtPixelInputs = listOf();

    public FakeImage(String relativeLocation) {
        RelativeLocation = relativeLocation;
    }

    public FakeImage(int width, int height) {
        Width = width;
        Height = height;
    }

    public FakeImage(String relativeLocation, int width, int height) {
        RelativeLocation = relativeLocation;
        Width = width;
        Height = height;
    }

    public FakeImage(String relativeLocation, int width, int height,
                     boolean supportsMouseEventCapturing) {
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
    public boolean capturesMouseEventsAtPixel(int x, int y)
            throws UnsupportedOperationException, IllegalArgumentException {
        CapturesMouseEventsAtPixelInputs.add(pairOf(x, y));
        return true;
    }
}
