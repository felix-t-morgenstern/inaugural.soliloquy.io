package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.graphics.rendering.timing.GlobalClock;

import java.util.Calendar;
import java.util.TimeZone;

public class FakeGlobalClock implements GlobalClock {
    @SuppressWarnings("FieldCanBeLocal")
    private final String TIME_ZONE = "GMT";

    @Override
    public long globalTimestamp() throws UnsupportedOperationException {
        return Calendar.getInstance(TimeZone.getTimeZone(TIME_ZONE)).getTimeInMillis();
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
