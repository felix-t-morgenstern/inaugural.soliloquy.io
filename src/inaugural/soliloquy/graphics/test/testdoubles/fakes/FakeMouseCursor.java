package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.io.MouseCursor;

public class FakeMouseCursor implements MouseCursor {
    public int NumberOfTimesUpdateCursorCalled;
    public long WindowId;

    @Override
    public void setMouseCursor(String s) throws IllegalArgumentException {

    }

    @Override
    public void updateCursor(long windowId) {
        NumberOfTimesUpdateCursorCalled++;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
