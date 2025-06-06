package inaugural.soliloquy.graphics.rendering;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.CheckedExceptionWrapper;
import soliloquy.specs.graphics.rendering.timing.FrameRateReporter;
import soliloquy.specs.graphics.rendering.timing.FrameTimer;
import soliloquy.specs.graphics.rendering.timing.GlobalClock;

import static inaugural.soliloquy.graphics.api.Constants.MS_PER_SECOND;

public class FrameTimerImpl implements FrameTimer {
    private final GlobalClock GLOBAL_CLOCK;
    private final FrameRateReporter FRAME_RATE_REPORTER;

    private boolean started;
    private boolean stopped;
    private Float targetFps;

    private Long currentPeriodStartTimestamp;
    private Long currentPeriodEndTimestamp;
    private int framesExecutedInCurrentPeriod;

    public FrameTimerImpl(GlobalClock globalClock, FrameRateReporter frameRateReporter) {
        GLOBAL_CLOCK = Check.ifNull(globalClock, "globalClock");
        FRAME_RATE_REPORTER = Check.ifNull(frameRateReporter, "frameRateReporter");
    }

    @Override
    public void setTargetFps(Float targetFps) throws IllegalArgumentException {
        if (targetFps == null) {
            this.targetFps = null;
        }
        else {
            this.targetFps = Check.throwOnLteZero(targetFps, "targetFps");
        }
    }

    @Override
    public void start() throws UnsupportedOperationException {
        if (started) {
            throw new UnsupportedOperationException("FrameTimerImpl: cannot be started twice");
        }
        new Thread(this::startNewPeriodLoopIteration).start();
        started = true;

        while (!stopped) {
            CheckedExceptionWrapper.sleep(MS_PER_SECOND / 2);
        }
    }

    @Override
    public void stop() throws UnsupportedOperationException {
        if (!started) {
            throw new UnsupportedOperationException(
                    "FrameTimerImpl: cannot be stopped before started");
        }
        if (stopped) {
            throw new UnsupportedOperationException(
                    "FrameTimerImpl: can only be stopped while running");
        }
        stopped = true;
    }

    @Override
    public void registerFrameExecution() throws UnsupportedOperationException {
        synchronized (this) {
            framesExecutedInCurrentPeriod++;
        }
    }

    private void startNewPeriodLoopIteration() {
        if (stopped) {
            return;
        }

        if (currentPeriodStartTimestamp != null) {
            synchronized (this) {
                reportFrameInformation(currentPeriodStartTimestamp, targetFps,
                        framesExecutedInCurrentPeriod);
                framesExecutedInCurrentPeriod = 0;
            }
            currentPeriodStartTimestamp = currentPeriodEndTimestamp;
        }
        else {
            currentPeriodStartTimestamp = GLOBAL_CLOCK.globalTimestamp();
        }
        currentPeriodEndTimestamp = currentPeriodStartTimestamp + MS_PER_SECOND;

        long currentTimestamp = GLOBAL_CLOCK.globalTimestamp();

        CheckedExceptionWrapper.sleep(currentPeriodEndTimestamp - currentTimestamp);

        startNewPeriodLoopIteration();
    }

    private void reportFrameInformation(long gmtTimestamp, Float targetFps, float actualFps) {
        FRAME_RATE_REPORTER.reportFrameRate(gmtTimestamp, targetFps, actualFps);
    }

    @Override
    public boolean shouldExecuteNextFrame() throws UnsupportedOperationException {
        if (stopped) {
            return false;
        }
        if (targetFps == null) {
            return true;
        }

        long msThroughCurrentPeriod;
        int framesExecutedInCurrentPeriod;
        float targetFps;

        synchronized (this) {
            msThroughCurrentPeriod = GLOBAL_CLOCK.globalTimestamp() - currentPeriodStartTimestamp;
            framesExecutedInCurrentPeriod = this.framesExecutedInCurrentPeriod;
            targetFps = this.targetFps;
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
}
