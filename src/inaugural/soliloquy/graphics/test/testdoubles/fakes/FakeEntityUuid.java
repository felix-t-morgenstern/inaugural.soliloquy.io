package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.valueobjects.EntityUuid;

public class FakeEntityUuid implements EntityUuid {
    @Override
    public long getMostSignificantBits() {
        return 0;
    }

    @Override
    public long getLeastSignificantBits() {
        return 0;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
