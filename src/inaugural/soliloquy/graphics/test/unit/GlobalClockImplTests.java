package inaugural.soliloquy.graphics.test.unit;

import inaugural.soliloquy.graphics.rendering.GlobalClockImpl;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.rendering.GlobalClock;

import static org.junit.jupiter.api.Assertions.*;

class GlobalClockImplTests {
    private GlobalClock _globalClock;

    @BeforeEach
    void setUp() {
        _globalClock = new GlobalClockImpl();
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(GlobalClock.class.getCanonicalName(), _globalClock.getInterfaceName());
    }

    @Test
    void testCannotStartTwice() {
        _globalClock.start();

        assertThrows(UnsupportedOperationException.class, _globalClock::start);
    }

    @Test
    void testGlobalTimestampBeforeStarted() {
        assertThrows(UnsupportedOperationException.class, _globalClock::globalTimestamp);
    }

    // NB: This method cannot be deterministic, because the execution of the test itself takes an
    //     indeterminate amount of time. Indeterminate testing is an anti-pattern, but it cannot be
    //     avoided here. (I have also had some issues running these tests in parallel; I could
    //     debug that, but for a test that takes 1-2s, it doesn't seem worth the effort.)
    @Test
    void testStartAndGlobalTimestamp() {
        int msToSleep = 50;

        int passingThresholdMs = 20;
        int numberOfTestsPassed = 0;
        int numberOfTestsToRun = 20;
        double passingPercentage = 0.9d;

        for (int i = 0; i < numberOfTestsToRun; i++) {
            GlobalClock globalClock = new GlobalClockImpl();

            globalClock.start();

            CheckedExceptionWrapper.Sleep(msToSleep);

            long globalTimestamp = globalClock.globalTimestamp();

            if (Math.abs(globalTimestamp - msToSleep) <= passingThresholdMs) {
                numberOfTestsPassed++;
            }
        }

        double percentagePassed = (double)numberOfTestsPassed / numberOfTestsToRun;

        if (percentagePassed < passingPercentage) {
            fail("Percentage passed (" + percentagePassed + ") less than threshold (" +
                    passingPercentage + ")");
        }
    }
}
