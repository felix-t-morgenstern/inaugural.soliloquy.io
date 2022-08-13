package inaugural.soliloquy.graphics.test.unit.rendering.timing;

import inaugural.soliloquy.graphics.rendering.GlobalClockImpl;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.rendering.timing.GlobalClock;

import java.util.Calendar;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlobalClockImplTests {
    @SuppressWarnings("FieldCanBeLocal")
    private final String TIME_ZONE = "GMT";

    private GlobalClock _globalClock;

    @BeforeEach
    void setUp() {
        _globalClock = new GlobalClockImpl();
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(GlobalClock.class.getCanonicalName(), _globalClock.getInterfaceName());
    }

    // NB: Indeterminate tests are generally a testing antipattern, but it is unavoidable in this
    //     instance, so the test is run enough times to ensure that it is highly reliable and
    //     fairly accurate.
    @Test
    void testStartAndGlobalTimestamp() {
        final int numberOfTestsToRun = 10;
        int timestampMsDifferenceTolerance = 20;

        for (int i = 0; i < numberOfTestsToRun; i++) {
            long actualGlobalTimestamp =
                    Calendar.getInstance(TimeZone.getTimeZone(TIME_ZONE)).getTimeInMillis();
            long globalTimestampFromGlobalClock = _globalClock.globalTimestamp();

            assertTrue(Math.abs(actualGlobalTimestamp - globalTimestampFromGlobalClock)
                    < timestampMsDifferenceTolerance);

            CheckedExceptionWrapper.sleep(10);
        }
    }
}
