package inaugural.soliloquy.graphics.rendering;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.rendering.FrameRateReporter;
import soliloquy.specs.graphics.rendering.FrameTimer;
import soliloquy.specs.graphics.rendering.GlobalClock;

import java.util.Date;

public class FrameTimerImpl implements FrameTimer {
    @SuppressWarnings("FieldCanBeLocal")
    private final int MS_PER_SECOND = 1000;

    private final GlobalClock GLOBAL_CLOCK;
    private final FrameRateReporter FRAME_RATE_REPORTER;

    private boolean _started;
    private boolean _stopped;
    private Float _targetFps;

    private Long _currentPeriodStartTimestamp;
    private Long _currentPeriodEndTimestamp;
    private int _framesExecutedInCurrentPeriod;

    public FrameTimerImpl(GlobalClock globalClock, FrameRateReporter frameRateReporter) {
        GLOBAL_CLOCK = Check.ifNull(globalClock, "globalClock");
        FRAME_RATE_REPORTER = Check.ifNull(frameRateReporter, "frameRateReporter");
    }

    @Override
    public void setTargetFps(Float targetFps) throws IllegalArgumentException {
        if (targetFps == null) {
            _targetFps = null;
        }
        else {
            _targetFps = Check.throwOnLteZero(targetFps, "targetFps");
        }
    }

    @Override
    public void start() throws UnsupportedOperationException {
        if (_started) {
            throw new UnsupportedOperationException("FrameTimerImpl: cannot be started twice");
        }
        new Thread(this::startNewPeriodLoopIteration).start();
        _started = true;

        while (!_stopped) {
            CheckedExceptionWrapper.sleep(MS_PER_SECOND / 2);
        }
    }

    @Override
    public void stop() throws UnsupportedOperationException {
        if (!_started) {
            throw new UnsupportedOperationException(
                    "FrameTimerImpl: cannot be stopped before started");
        }
        if (_stopped) {
            throw new UnsupportedOperationException(
                    "FrameTimerImpl: can only be stopped while running");
        }
        _stopped = true;
    }

    @Override
    public void registerFrameExecution() throws UnsupportedOperationException {
        synchronized (this) {
            _framesExecutedInCurrentPeriod++;
        }
    }

    private void startNewPeriodLoopIteration() {
        if (_stopped) {
            return;
        }

        if (_currentPeriodStartTimestamp != null) {
            synchronized (this) {
                reportFrameInformation(_currentPeriodStartTimestamp, _targetFps,
                        _framesExecutedInCurrentPeriod);
                _framesExecutedInCurrentPeriod = 0;
            }
            _currentPeriodStartTimestamp = _currentPeriodEndTimestamp;
        }
        else {
            _currentPeriodStartTimestamp = GLOBAL_CLOCK.globalTimestamp();
        }
        _currentPeriodEndTimestamp = _currentPeriodStartTimestamp + MS_PER_SECOND;

        long currentTimestamp = GLOBAL_CLOCK.globalTimestamp();

        CheckedExceptionWrapper.sleep(_currentPeriodEndTimestamp - currentTimestamp);

        startNewPeriodLoopIteration();
    }

    private void reportFrameInformation(long gmtTimestamp, Float targetFps, float actualFps) {
        FRAME_RATE_REPORTER.reportFrameRate(new Date(gmtTimestamp), targetFps, actualFps);
    }

    @Override
    public boolean shouldExecuteNextFrame() throws UnsupportedOperationException {
        if (_targetFps == null) {
            return true;
        }

        long msThroughCurrentPeriod;
        int framesExecutedInCurrentPeriod;
        float targetFps;

        synchronized (this) {
            msThroughCurrentPeriod = GLOBAL_CLOCK.globalTimestamp() - _currentPeriodStartTimestamp;
            framesExecutedInCurrentPeriod = _framesExecutedInCurrentPeriod;
            targetFps = _targetFps;
        }

        if (framesExecutedInCurrentPeriod == 0) {
            return true;
        }
        if (framesExecutedInCurrentPeriod >= targetFps) {
            return false;
        }

        float msBetweenFrames = MS_PER_SECOND / targetFps;
        float msThresholdForNextFrame = framesExecutedInCurrentPeriod * msBetweenFrames;

        return msThroughCurrentPeriod >= msThresholdForNextFrame;
    }

    @Override
    public String getInterfaceName() {
        return FrameTimer.class.getCanonicalName();
    }
}
