package inaugural.soliloquy.io.graphics.rendering;

import inaugural.soliloquy.io.api.Constants;
import soliloquy.specs.io.graphics.rendering.timing.GlobalClock;

import java.util.Calendar;
import java.util.TimeZone;

public class GlobalClockImpl implements GlobalClock {
    @Override
    public long globalTimestamp() {
        return Calendar.getInstance(TimeZone.getTimeZone(Constants.GMT)).getTimeInMillis();
    }
}
