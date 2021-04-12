package inaugural.soliloquy.graphics.rendering;

import inaugural.soliloquy.graphics.api.Constants;
import soliloquy.specs.graphics.rendering.timing.GlobalClock;

import java.util.Calendar;
import java.util.TimeZone;

public class GlobalClockImpl implements GlobalClock {
    @Override
    public long globalTimestamp() {
        return Calendar.getInstance(TimeZone.getTimeZone(Constants.GMT)).getTimeInMillis();
    }

    @Override
    public String getInterfaceName() {
        return GlobalClock.class.getCanonicalName();
    }
}
