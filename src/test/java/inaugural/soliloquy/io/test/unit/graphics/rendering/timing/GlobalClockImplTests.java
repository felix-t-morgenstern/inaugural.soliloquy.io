package inaugural.soliloquy.io.test.unit.graphics.rendering.timing;

import inaugural.soliloquy.io.graphics.rendering.GlobalClockImpl;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.io.graphics.rendering.timing.GlobalClock;

import java.util.Calendar;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GlobalClockImplTests {
    @SuppressWarnings("FieldCanBeLocal")
    private final String TIME_ZONE = "GMT";

    private GlobalClock globalClock;

    @BeforeEach
    public void setUp() {
        globalClock = new GlobalClockImpl();
    }

    // NB: Indeterminate tests are generally a testing antipattern, but it is unavoidable in this
    //     instance, so the test is run enough times to ensure that it is highly reliable and
    //     fairly accurate.
    @Test
    public void testStartAndGlobalTimestamp() {
        final var numberOfTestsToRun = 10;
        var timestampMsDifferenceTolerance = 20;

        for (var i = 0; i < numberOfTestsToRun; i++) {
            var actualGlobalTimestamp =
                    Calendar.getInstance(TimeZone.getTimeZone(TIME_ZONE)).getTimeInMillis();
            var globalTimestampFromGlobalClock = globalClock.globalTimestamp();

            assertTrue(Math.abs(actualGlobalTimestamp - globalTimestampFromGlobalClock)
                    < timestampMsDifferenceTolerance);

            CheckedExceptionWrapper.sleep(10);
        }
    }
}
