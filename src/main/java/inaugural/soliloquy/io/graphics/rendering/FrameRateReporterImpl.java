package inaugural.soliloquy.io.graphics.rendering;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.io.graphics.rendering.timing.FrameRateReporter;

import java.util.Date;
import java.util.Map;
import java.util.function.Consumer;

import static inaugural.soliloquy.io.api.Constants.MS_PER_SECOND;
import static inaugural.soliloquy.tools.collections.Collections.mapOf;

public class FrameRateReporterImpl implements FrameRateReporter {
    private final int PERIODS_PER_AGGREGATE;
    private final Map<String, Consumer<Aggregate>> AGGREGATE_OUTPUTS;
    private final Map<String, Boolean> AGGREGATE_OUTPUTS_ACTIVATION_STATUSES;
    private final boolean AGGREGATE_OUTPUT_DEFAULT_ACTIVE_STATUS = true;
    private final Float[] TARGET_FPS_IN_CURRENT_AGGREGATE;
    private final float[] ACTUAL_FPS_IN_CURRENT_AGGREGATE;
    private final int[] MS_PER_PERIOD_IN_CURRENT_AGGREGATE;

    private Long lastReportedDate;
    private Float currentActualFps;
    private long currentAggregateStartDate;
    private int periodWithinCurrentAggregate;
    private float aggregateTargetFpsDivisor;
    private Long pauseStart;
    private int msPausedWithinCurrentAggregate;

    @SuppressWarnings("ConstantConditions")
    public FrameRateReporterImpl(int periodsPerAggregate,
                                 Map<String, Consumer<Aggregate>> aggregateOutputs) {
        PERIODS_PER_AGGREGATE = Check.throwOnLteZero(periodsPerAggregate, "periodsPerAggregate");
        AGGREGATE_OUTPUTS = Check.ifNull(aggregateOutputs, "aggregateOutputs");
        AGGREGATE_OUTPUTS_ACTIVATION_STATUSES = mapOf();
        Check.ifNull(aggregateOutputs, "aggregateOutputs").forEach((key, value) -> {
            Check.ifNullOrEmpty(key, "id within aggregateOutputs");
            Check.ifNull(value, "value within aggregateOutputs");
            AGGREGATE_OUTPUTS_ACTIVATION_STATUSES.put(key,
                    AGGREGATE_OUTPUT_DEFAULT_ACTIVE_STATUS);
        });
        TARGET_FPS_IN_CURRENT_AGGREGATE = new Float[PERIODS_PER_AGGREGATE];
        ACTUAL_FPS_IN_CURRENT_AGGREGATE = new float[PERIODS_PER_AGGREGATE];
        periodWithinCurrentAggregate = 0;
        aggregateTargetFpsDivisor = 0;
        MS_PER_PERIOD_IN_CURRENT_AGGREGATE = new int[PERIODS_PER_AGGREGATE];
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
                TARGET_FPS_IN_CURRENT_AGGREGATE[periodWithinCurrentAggregate] = targetFps;
            }
            ACTUAL_FPS_IN_CURRENT_AGGREGATE[periodWithinCurrentAggregate] = actualFps;

            if (pauseStart != null) {
                MS_PER_PERIOD_IN_CURRENT_AGGREGATE[periodWithinCurrentAggregate] +=
                        (int) Math.min(datetime - pauseStart + MS_PER_SECOND, MS_PER_SECOND);
                MS_PER_PERIOD_IN_CURRENT_AGGREGATE[periodWithinCurrentAggregate] = Math.min(
                        MS_PER_PERIOD_IN_CURRENT_AGGREGATE[periodWithinCurrentAggregate],
                        MS_PER_SECOND
                );
            }
            if (targetFps != null) {
                aggregateTargetFpsDivisor += (MS_PER_SECOND -
                        MS_PER_PERIOD_IN_CURRENT_AGGREGATE[periodWithinCurrentAggregate]);
            }
            msPausedWithinCurrentAggregate +=
                    MS_PER_PERIOD_IN_CURRENT_AGGREGATE[periodWithinCurrentAggregate];

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
                                1f - (MS_PER_PERIOD_IN_CURRENT_AGGREGATE[period]
                                        / (float) MS_PER_SECOND);
                        if (TARGET_FPS_IN_CURRENT_AGGREGATE[period] != null) {
                            // I hate this statement. It works, but I hate it.
                            float toAddToAggregateTargetFps =
                                    (TARGET_FPS_IN_CURRENT_AGGREGATE[period] *
                                            percentageOfPeriodPausedAdj);
                            if (aggregateTargetFps == null) {
                                aggregateTargetFps = toAddToAggregateTargetFps;
                            }
                            else {
                                aggregateTargetFps += toAddToAggregateTargetFps;
                            }
                        }
                        aggregateActualFps += (ACTUAL_FPS_IN_CURRENT_AGGREGATE[period] *
                                percentageOfPeriodPausedAdj);
                    }

                    TARGET_FPS_IN_CURRENT_AGGREGATE[period] = null;
                    ACTUAL_FPS_IN_CURRENT_AGGREGATE[period] = 0f;
                    MS_PER_PERIOD_IN_CURRENT_AGGREGATE[period] = 0;
                }

                if (aggregateTargetFps != null) {
                    aggregateTargetFps /= (aggregateTargetFpsDivisor / (float) MS_PER_SECOND);
                }
                aggregateActualFps /= aggregateActualFpsDivisor;

                for (var aggregateOutput : AGGREGATE_OUTPUTS.entrySet()) {
                    if (AGGREGATE_OUTPUTS_ACTIVATION_STATUSES.get(aggregateOutput.getKey())) {
                        aggregateOutput.getValue().accept(new Aggregate(
                                new Date(currentAggregateStartDate),
                                entireAggregatePaused ? null : aggregateTargetFps,
                                entireAggregatePaused ? null : aggregateActualFps
                        ));
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
                MS_PER_PERIOD_IN_CURRENT_AGGREGATE[periodWithinCurrentAggregate] +=
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
