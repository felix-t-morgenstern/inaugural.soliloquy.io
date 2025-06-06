package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.rendering.timing.GlobalClock;

import java.util.Calendar;
import java.util.TimeZone;

import static inaugural.soliloquy.graphics.api.Constants.GMT;

public class FakeGlobalClock implements GlobalClock {
    @Override
    public long globalTimestamp() throws UnsupportedOperationException {
        return Calendar.getInstance(TimeZone.getTimeZone(GMT)).getTimeInMillis();
    }
}
