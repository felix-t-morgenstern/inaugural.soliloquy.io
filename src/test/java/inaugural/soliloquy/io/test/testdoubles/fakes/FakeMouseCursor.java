package inaugural.soliloquy.io.test.testdoubles.fakes;

import soliloquy.specs.io.mouse.MouseCursor;

public class FakeMouseCursor implements MouseCursor {
    public int NumberOfTimesUpdateCursorCalled;

    @Override
    public void setMouseCursor(String s) throws IllegalArgumentException {

    }

    @Override
    public void updateCursor(long windowId) {
        NumberOfTimesUpdateCursorCalled++;
    }
}
