package inaugural.soliloquy.graphics.test.unit.rendering;

import inaugural.soliloquy.graphics.rendering.FrameTimerImpl;
import inaugural.soliloquy.graphics.test.fakes.FakeFrameRateReporter;
import inaugural.soliloquy.graphics.test.fakes.FakeGlobalClock;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.rendering.FrameTimer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FrameTimerImplTests {
    private final int MS_PER_SECOND = 1000;

    private final FakeGlobalClock GLOBAL_CLOCK = new FakeGlobalClock();
    private final FakeFrameRateReporter FRAME_RATE_REPORTER = new FakeFrameRateReporter();

    private FrameTimer _frameTimer;

    @BeforeEach
    void setUp() {
        _frameTimer = new FrameTimerImpl(GLOBAL_CLOCK, FRAME_RATE_REPORTER);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class,
                () -> new FrameTimerImpl(null, FRAME_RATE_REPORTER));
        assertThrows(IllegalArgumentException.class,
                () -> new FrameTimerImpl(GLOBAL_CLOCK, null));
    }

    @Test
    void testCannotStartTwice() {
        new Thread(_frameTimer::start).start();
        CheckedExceptionWrapper.sleep(50);

        assertThrows(UnsupportedOperationException.class, _frameTimer::start);

        _frameTimer.stop();
    }

    @Test
    void testCannotStopBeforeStarted() {
        assertThrows(UnsupportedOperationException.class, _frameTimer::stop);
    }

    @Test
    void testCannotBeStoppedTwice() {
        new Thread(_frameTimer::start).start();
        CheckedExceptionWrapper.sleep(50);
        _frameTimer.stop();

        assertThrows(UnsupportedOperationException.class, _frameTimer::stop);
    }

    @Test
    void testSetTargetFpsWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _frameTimer.setTargetFps(0f));
    }

    @Test
    void testCorrectNumberOfPeriodsElapsed() {
        int numberOfPeriodsToElapse = 3;

        new Thread(_frameTimer::start).start();
        CheckedExceptionWrapper.sleep(
                (MS_PER_SECOND * numberOfPeriodsToElapse) + MS_PER_SECOND / 2);
        _frameTimer.stop();

        assertEquals(numberOfPeriodsToElapse, FRAME_RATE_REPORTER.Dates.size());
    }

    @Test
    void testEquidistantPeriods() {
        int numberOfPeriodsToElapse = 3;

        new Thread(_frameTimer::start).start();
        CheckedExceptionWrapper.sleep(
                (MS_PER_SECOND * numberOfPeriodsToElapse) + MS_PER_SECOND / 2);
        _frameTimer.stop();

        assertEquals(numberOfPeriodsToElapse, FRAME_RATE_REPORTER.Dates.size());

        for (int i = 1; i < FRAME_RATE_REPORTER.Dates.size(); i++) {
            assertEquals(1000,
                    FRAME_RATE_REPORTER.Dates.get(i).getTime() -
                            FRAME_RATE_REPORTER.Dates.get(i - 1).getTime());
        }
    }

    @Test
    void testReportedTargetFps() {
        int numberOfPeriodsToElapse = 3;
        float targetFps = 123f;

        _frameTimer.setTargetFps(targetFps);
        new Thread(_frameTimer::start).start();
        CheckedExceptionWrapper.sleep(
                (MS_PER_SECOND * numberOfPeriodsToElapse) + MS_PER_SECOND / 2);
        _frameTimer.stop();

        assertEquals(numberOfPeriodsToElapse, FRAME_RATE_REPORTER.TargetFps.size());

        FRAME_RATE_REPORTER.TargetFps.forEach(reportedTargetFps ->
                assertEquals(targetFps, (float)reportedTargetFps));
    }

    @Test
    void testRegisterFrameExecution() {
        int numberOfPeriodsToElapse = 3;
        float actualFpsToAchieve = 8f;

        new Thread(_frameTimer::start).start();
        CheckedExceptionWrapper.sleep(MS_PER_SECOND / 4);
        for (int i = 0; i < numberOfPeriodsToElapse; i++) {
            for (int j = 0; j < actualFpsToAchieve; j++) {
                _frameTimer.registerFrameExecution();
            }
            CheckedExceptionWrapper.sleep(MS_PER_SECOND);
        }
        CheckedExceptionWrapper.sleep(MS_PER_SECOND / 4);
        _frameTimer.stop();

        assertEquals(numberOfPeriodsToElapse, FRAME_RATE_REPORTER.ActualFps.size());

        FRAME_RATE_REPORTER.ActualFps.forEach(actualFps ->
                assertEquals(actualFpsToAchieve, (float)actualFps));
    }

    @Test
    void testShouldExecuteNextFrame() {
        float targetFps = 7f;

        _frameTimer.setTargetFps(targetFps);
        new Thread(_frameTimer::start).start();
        CheckedExceptionWrapper.sleep(50);
        for (int i = 0; i < 20; i++) {
            if (_frameTimer.shouldExecuteNextFrame()) {
                _frameTimer.registerFrameExecution();
            }
            CheckedExceptionWrapper.sleep(50);
        }
        _frameTimer.stop();

        assertEquals(1, FRAME_RATE_REPORTER.ActualFps.size());
        assertEquals(targetFps, (float)FRAME_RATE_REPORTER.ActualFps.get(0));
    }

    @Test
    void testNullTargetFps() {
        int numberOfFramesToExecute = 1234;
        _frameTimer.setTargetFps(null);
        new Thread(_frameTimer::start).start();
        CheckedExceptionWrapper.sleep(50);
        for (int i = 0; i < numberOfFramesToExecute; i++) {
            if (_frameTimer.shouldExecuteNextFrame()) {
                _frameTimer.registerFrameExecution();
            }
        }
        CheckedExceptionWrapper.sleep(950);
        _frameTimer.stop();

        assertEquals(1, FRAME_RATE_REPORTER.ActualFps.size());
        assertEquals(numberOfFramesToExecute, (float)FRAME_RATE_REPORTER.ActualFps.get(0));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(FrameTimer.class.getCanonicalName(), _frameTimer.getInterfaceName());
    }
}
