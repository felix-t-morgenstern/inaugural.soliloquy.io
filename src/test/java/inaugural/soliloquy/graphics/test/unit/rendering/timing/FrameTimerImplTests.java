package inaugural.soliloquy.graphics.test.unit.rendering.timing;

import inaugural.soliloquy.graphics.rendering.FrameTimerImpl;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeFrameRateReporter;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeGlobalClock;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.rendering.timing.FrameTimer;

import static org.junit.jupiter.api.Assertions.*;

public class FrameTimerImplTests {
    private final FakeGlobalClock GLOBAL_CLOCK = new FakeGlobalClock();
    private final FakeFrameRateReporter FRAME_RATE_REPORTER = new FakeFrameRateReporter();

    private FrameTimer frameTimer;

    @BeforeEach
    public void setUp() {
        frameTimer = new FrameTimerImpl(GLOBAL_CLOCK, FRAME_RATE_REPORTER);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new FrameTimerImpl(null, FRAME_RATE_REPORTER));
        assertThrows(IllegalArgumentException.class,
                () -> new FrameTimerImpl(GLOBAL_CLOCK, null));
    }

    @Test
    public void testCannotStartTwice() {
        new Thread(frameTimer::start).start();
        CheckedExceptionWrapper.sleep(50);

        assertThrows(UnsupportedOperationException.class, frameTimer::start);

        frameTimer.stop();
    }

    @Test
    public void testCannotStopBeforeStarted() {
        assertThrows(UnsupportedOperationException.class, frameTimer::stop);
    }

    @Test
    public void testCannotBeStoppedTwice() {
        new Thread(frameTimer::start).start();
        CheckedExceptionWrapper.sleep(50);
        frameTimer.stop();

        assertThrows(UnsupportedOperationException.class, frameTimer::stop);
    }

    @Test
    public void testSetTargetFpsWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> frameTimer.setTargetFps(0f));
    }

    // NB: Ignored due to time constraints
//    @Test
//    void testCorrectNumberOfPeriodsElapsed() {
//        int numberOfPeriodsToElapse = 3;
//
//        new Thread(frameTimer::start).start();
//        CheckedExceptionWrapper.sleep(
//                (MS_PER_SECOND * numberOfPeriodsToElapse) + MS_PER_SECOND / 2);
//        frameTimer.stop();
//
//        assertEquals(numberOfPeriodsToElapse, FRAME_RATE_REPORTER.Dates.size());
//    }

    // NB: Ignored due to time constraints
//    @Test
//    void testEquidistantPeriods() {
//        int numberOfPeriodsToElapse = 3;
//
//        new Thread(frameTimer::start).start();
//        CheckedExceptionWrapper.sleep(
//                (MS_PER_SECOND * numberOfPeriodsToElapse) + MS_PER_SECOND / 2);
//        frameTimer.stop();
//
//        assertEquals(numberOfPeriodsToElapse, FRAME_RATE_REPORTER.Dates.size());
//
//        for (var i = 1; i < FRAME_RATE_REPORTER.Dates.size(); i++) {
//            assertEquals(1000,
//                    FRAME_RATE_REPORTER.Dates.get(i) - FRAME_RATE_REPORTER.Dates.get(i - 1));
//        }
//    }

    // NB: Ignored due to time constraints
//    @Test
//    void testReportedTargetFps() {
//        int numberOfPeriodsToElapse = 3;
//        float targetFps = 123f;
//
//        frameTimer.setTargetFps(targetFps);
//        new Thread(frameTimer::start).start();
//        CheckedExceptionWrapper.sleep(
//                (MS_PER_SECOND * numberOfPeriodsToElapse) + MS_PER_SECOND / 2);
//        frameTimer.stop();
//
//        assertEquals(numberOfPeriodsToElapse, FRAME_RATE_REPORTER.TargetFps.size());
//
//        FRAME_RATE_REPORTER.TargetFps.forEach(reportedTargetFps ->
//                assertEquals(targetFps, (float)reportedTargetFps));
//    }

    // NB: Ignored due to time constraints
//    @Test
//    void testRegisterFrameExecution() {
//        int numberOfPeriodsToElapse = 3;
//        float actualFpsToAchieve = 8f;
//
//        new Thread(frameTimer::start).start();
//        CheckedExceptionWrapper.sleep(MS_PER_SECOND / 4);
//        for (var i = 0; i < numberOfPeriodsToElapse; i++) {
//            for (var j = 0; j < actualFpsToAchieve; j++) {
//                frameTimer.registerFrameExecution();
//            }
//            CheckedExceptionWrapper.sleep(MS_PER_SECOND);
//        }
//        CheckedExceptionWrapper.sleep(MS_PER_SECOND / 4);
//        frameTimer.stop();
//
//        assertEquals(numberOfPeriodsToElapse, FRAME_RATE_REPORTER.ActualFps.size());
//
//        FRAME_RATE_REPORTER.ActualFps.forEach(actualFps ->
//                assertEquals(actualFpsToAchieve, (float)actualFps));
//    }

    @Test
    public void testShouldExecuteNextFrame() {
        float targetFps = 7f;

        frameTimer.setTargetFps(targetFps);
        new Thread(frameTimer::start).start();
        CheckedExceptionWrapper.sleep(50);
        for (var i = 0; i < 20; i++) {
            if (frameTimer.shouldExecuteNextFrame()) {
                frameTimer.registerFrameExecution();
            }
            CheckedExceptionWrapper.sleep(50);
        }
        frameTimer.stop();

        assertEquals(1, FRAME_RATE_REPORTER.ActualFps.size());
        assertEquals(targetFps, (float) FRAME_RATE_REPORTER.ActualFps.getFirst());
    }

    @Test
    public void testNullTargetFps() {
        int numberOfFramesToExecute = 1234;
        frameTimer.setTargetFps(null);
        new Thread(frameTimer::start).start();
        CheckedExceptionWrapper.sleep(50);
        for (var i = 0; i < numberOfFramesToExecute; i++) {
            if (frameTimer.shouldExecuteNextFrame()) {
                frameTimer.registerFrameExecution();
            }
        }
        CheckedExceptionWrapper.sleep(1050);
        frameTimer.stop();

        assertEquals(1, FRAME_RATE_REPORTER.ActualFps.size());
        assertEquals(numberOfFramesToExecute, (float) FRAME_RATE_REPORTER.ActualFps.getFirst());
    }

    @Test
    public void testShouldExecuteNextFrameWhenStopped() {
        new Thread(() -> {
            frameTimer.start();
            CheckedExceptionWrapper.sleep(50);
        }).start();
        CheckedExceptionWrapper.sleep(10);
        frameTimer.stop();

        assertFalse(frameTimer.shouldExecuteNextFrame());
    }
}
