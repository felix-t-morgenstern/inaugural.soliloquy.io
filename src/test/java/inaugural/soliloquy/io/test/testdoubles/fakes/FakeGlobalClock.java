package inaugural.soliloquy.io.test.testdoubles.fakes;

import soliloquy.specs.io.graphics.rendering.timing.GlobalClock;

import java.util.Calendar;
import java.util.TimeZone;

import static inaugural.soliloquy.io.api.Constants.GMT;

public class FakeGlobalClock implements GlobalClock {
    @Override
    public long globalTimestamp() throws UnsupportedOperationException {
        return Calendar.getInstance(TimeZone.getTimeZone(GMT)).getTimeInMillis();
    }
}
