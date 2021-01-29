package inaugural.soliloquy.graphics.rendering;

import soliloquy.specs.graphics.rendering.GlobalClock;

import java.util.Calendar;
import java.util.TimeZone;

public class GlobalClockImpl implements GlobalClock {
    @SuppressWarnings("FieldCanBeLocal")
    private final String TIME_ZONE = "GMT";

    private boolean _clockStarted;
    private long _startTimestamp;

    @Override
    public void start() throws UnsupportedOperationException {
        if (_clockStarted) {
            throw new UnsupportedOperationException("GlobalClockImpl.start: clock already started");
        }
        _clockStarted = true;

        _startTimestamp = getCurrentGmtMilliseconds();
    }

    @Override
    public long globalTimestamp() throws UnsupportedOperationException {
        if (!_clockStarted) {
            throw new UnsupportedOperationException("GlobalClockImpl.start: clock not yet started");
        }

        return getCurrentGmtMilliseconds() - _startTimestamp;
    }

    private long getCurrentGmtMilliseconds() {
        return Calendar.getInstance(TimeZone.getTimeZone(TIME_ZONE)).getTimeInMillis();
    }

    @Override
    public String getInterfaceName() {
        return GlobalClock.class.getCanonicalName();
    }
}
