package inaugural.soliloquy.io.graphics.rendering;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.io.graphics.rendering.timing.FrameRateReporter;
import soliloquy.specs.io.graphics.rendering.timing.FrameRateReporterAggregateOutput;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import static inaugural.soliloquy.io.api.Constants.MS_PER_SECOND;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;

public class FrameRateReporterImpl implements FrameRateReporter {
    private final int PERIODS_PER_AGGREGATE;
    private final Collection<FrameRateReporterAggregateOutput> AGGREGATE_OUTPUTS;
    private final Map<String, Boolean> AGGREGATE_OUTPUTS_ACTIVATION_STATUSES;
    private final boolean AGGREGATE_OUTPUT_DEFAULT_ACTIVE_STATUS = true;

    private Long lastReportedDate;
    private Float currentActualFps;
    private long currentAggregateStartDate;
    private Float[] targetFpsInCurrentAggregate;
    private float[] actualFpsInCurrentAggregate;
    private int periodWithinCurrentAggregate;
    private float aggregateTargetFpsDivisor;
    private Long pauseStart;
    private int[] msPausedPerPeriodInCurrentAggregate;
    private int msPausedWithinCurrentAggregate;

    @SuppressWarnings("ConstantConditions")
    public FrameRateReporterImpl(int periodsPerAggregate,
                                 Collection<FrameRateReporterAggregateOutput> aggregateOutputs) {
        PERIODS_PER_AGGREGATE = Check.throwOnLteZero(periodsPerAggregate, "periodsPerAggregate");
        AGGREGATE_OUTPUTS = aggregateOutputs;
        AGGREGATE_OUTPUTS_ACTIVATION_STATUSES = mapOf();
        Check.ifNull(aggregateOutputs, "aggregateOutputs").forEach(aggregateOutput -> {
            Check.ifNull(aggregateOutput, "aggregateOutput");
            Check.ifNullOrEmpty(aggregateOutput.id(), "aggregateOutput.id()");
            if (AGGREGATE_OUTPUTS_ACTIVATION_STATUSES.containsKey(aggregateOutput.id())) {
                throw new IllegalArgumentException(
                        "FrameRateReporterImpl: Provided two aggregate outputs with the same Id ("
                                + aggregateOutput.id() + ")");
            }
            AGGREGATE_OUTPUTS_ACTIVATION_STATUSES.put(aggregateOutput.id(),
                    AGGREGATE_OUTPUT_DEFAULT_ACTIVE_STATUS);
        });
        targetFpsInCurrentAggregate = new Float[PERIODS_PER_AGGREGATE];
        actualFpsInCurrentAggregate = new float[PERIODS_PER_AGGREGATE];
        periodWithinCurrentAggregate = 0;
        aggregateTargetFpsDivisor = 0;
        msPausedPerPeriodInCurrentAggregate = new int[PERIODS_PER_AGGREGATE];
        msPausedWithinCurrentAggregate = 0;
    }

    @Override
    public void reportFrameRate(long datetime, Float targetFps, float actualFps)
            throws IllegalArgumentException {
        synchronized (this) {
            if (lastReportedDate != null && datetime - lastReportedDate != MS_PER_SECOND) {
                throw new IllegalArgumentException("FrameRateReporterImpl.reportFrameRate: " +
                        "datetime must be precisely 1000ms after previously reported datetime");
            }
            if (targetFps != null) {
                Check.throwOnLtValue(targetFps, 0f, "targetFps");
            }
            Check.throwOnLtValue(actualFps, 0f, "actualFps");

            if (periodWithinCurrentAggregate == 0) {
                currentAggregateStartDate = datetime;
            }

            if (targetFps != null) {
                targetFpsInCurrentAggregate[periodWithinCurrentAggregate] = targetFps;
            }
            actualFpsInCurrentAggregate[periodWithinCurrentAggregate] = actualFps;

            if (pauseStart != null) {
                msPausedPerPeriodInCurrentAggregate[periodWithinCurrentAggregate] +=
                        (int) Math.min(datetime - pauseStart + MS_PER_SECOND, MS_PER_SECOND);
                msPausedPerPeriodInCurrentAggregate[periodWithinCurrentAggregate] = Math.min(
                        msPausedPerPeriodInCurrentAggregate[periodWithinCurrentAggregate],
                        MS_PER_SECOND
                );
            }
            if (targetFps != null) {
                aggregateTargetFpsDivisor += (MS_PER_SECOND -
                        msPausedPerPeriodInCurrentAggregate[periodWithinCurrentAggregate]);
            }
            msPausedWithinCurrentAggregate +=
                    msPausedPerPeriodInCurrentAggregate[periodWithinCurrentAggregate];

            if (periodWithinCurrentAggregate == PERIODS_PER_AGGREGATE - 1) {
                Float aggregateTargetFps = null;
                float aggregateActualFps = 0f;
                float aggregateActualFpsDivisor = (float) PERIODS_PER_AGGREGATE -
                        (msPausedWithinCurrentAggregate / (float) MS_PER_SECOND);
                boolean entireAggregatePaused = msPausedWithinCurrentAggregate ==
                        MS_PER_SECOND * PERIODS_PER_AGGREGATE;
                for (var period = 0; period < PERIODS_PER_AGGREGATE; period++) {
                    if (!entireAggregatePaused) {
                        float percentageOfPeriodPausedAdj =
                                1f - (msPausedPerPeriodInCurrentAggregate[period]
                                        / (float) MS_PER_SECOND);
                        if (targetFpsInCurrentAggregate[period] != null) {
                            // I hate this statement. It works, but I hate it.
                            float toAddToAggregateTargetFps =
                                    (targetFpsInCurrentAggregate[period] *
                                            percentageOfPeriodPausedAdj);
                            if (aggregateTargetFps == null) {
                                aggregateTargetFps = toAddToAggregateTargetFps;
                            }
                            else {
                                aggregateTargetFps += toAddToAggregateTargetFps;
                            }
                        }
                        aggregateActualFps += (actualFpsInCurrentAggregate[period] *
                                percentageOfPeriodPausedAdj);
                    }

                    targetFpsInCurrentAggregate[period] = null;
                    actualFpsInCurrentAggregate[period] = 0f;
                    msPausedPerPeriodInCurrentAggregate[period] = 0;
                }

                if (aggregateTargetFps != null) {
                    aggregateTargetFps /= (aggregateTargetFpsDivisor / (float) MS_PER_SECOND);
                }
                aggregateActualFps /= aggregateActualFpsDivisor;

                for (FrameRateReporterAggregateOutput aggregateOutput : AGGREGATE_OUTPUTS) {
                    if (AGGREGATE_OUTPUTS_ACTIVATION_STATUSES.get(aggregateOutput.id())) {
                        aggregateOutput.outputAggregateFrameRateData(
                                new Date(currentAggregateStartDate),
                                entireAggregatePaused ? null : aggregateTargetFps,
                                entireAggregatePaused ? null : aggregateActualFps);
                    }
                }

                msPausedWithinCurrentAggregate = 0;
                periodWithinCurrentAggregate = 0;
                aggregateTargetFpsDivisor = 0f;
            }
            else {
                periodWithinCurrentAggregate++;
            }

            lastReportedDate = datetime;

            if (pauseStart == null) {
                currentActualFps = actualFps;
            }
        }
    }

    @Override
    public Float currentActualFps() {
        return currentActualFps;
    }

    @Override
    public void activateAggregateOutput(String id) throws IllegalArgumentException {
        Check.ifNullOrEmpty(id, "id");
        if (!AGGREGATE_OUTPUTS_ACTIVATION_STATUSES.containsKey(id)) {
            throw new IllegalArgumentException("FrameRateReporterImpl.activateAggregateOutput: "
                    + "id does not correspond to a registered aggregate output");
        }
        AGGREGATE_OUTPUTS_ACTIVATION_STATUSES.put(id, true);
    }

    @Override
    public void deactivateAggregateOutput(String id) throws IllegalArgumentException {
        Check.ifNullOrEmpty(id, "id");
        if (!AGGREGATE_OUTPUTS_ACTIVATION_STATUSES.containsKey(id)) {
            throw new IllegalArgumentException("FrameRateReporterImpl.deactivateAggregateOutput: "
                    + "id does not correspond to a registered aggregate output");
        }
        AGGREGATE_OUTPUTS_ACTIVATION_STATUSES.put(id, false);
    }

    @Override
    public void reportPause(long timestamp) throws IllegalArgumentException {
        synchronized (this) {
            if (lastReportedDate != null) {
                Check.throwOnLtValue(timestamp, nextReportingDate(), "timestamp");
                Check.throwOnGteValue(timestamp, nextReportingDate() + MS_PER_SECOND, "timestamp");
            }

            currentActualFps = null;
            pauseStart = timestamp;
        }
    }

    @Override
    public void reportUnpause(long timestamp) throws IllegalArgumentException {
        synchronized (this) {
            if (lastReportedDate != null) {
                Check.throwOnLtValue(timestamp, nextReportingDate(), "timestamp");
                Check.throwOnGteValue(timestamp, nextReportingDate() + MS_PER_SECOND, "timestamp");
            }

            if (lastReportedDate != null && pauseStart != null) {
                msPausedPerPeriodInCurrentAggregate[periodWithinCurrentAggregate] +=
                        (int) (timestamp - Math.max(nextReportingDate(), pauseStart));
            }
            pauseStart = null;
        }
    }

    // NB: This is unsupported FOR NOW; it can easily be implemented later
    @Override
    public Long pausedTimestamp() {
        throw new UnsupportedOperationException();
    }

    private long nextReportingDate() {
        return lastReportedDate + MS_PER_SECOND;
    }
}
