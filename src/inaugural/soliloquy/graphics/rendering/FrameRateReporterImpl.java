package inaugural.soliloquy.graphics.rendering;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.rendering.timing.FrameRateReporter;
import soliloquy.specs.graphics.rendering.timing.FrameRateReporterAggregateOutput;

import java.util.*;

import static inaugural.soliloquy.graphics.api.Constants.MS_PER_SECOND;

public class FrameRateReporterImpl implements FrameRateReporter {
    private final int PERIODS_PER_AGGREGATE;
    private final Collection<FrameRateReporterAggregateOutput> AGGREGATE_OUTPUTS;
    private final Map<String, Boolean> AGGREGATE_OUTPUTS_ACTIVATION_STATUSES;
    private final boolean AGGREGATE_OUTPUT_DEFAULT_ACTIVE_STATUS = true;

    private Long _lastReportedDate;
    private Float _currentActualFps;
    private long _currentAggregateStartDate;
    private Float[] _targetFpsInCurrentAggregate;
    private float[] _actualFpsInCurrentAggregate;
    private int _periodWithinCurrentAggregate;
    private float _aggregateTargetFpsDivisor;
    private Long _pauseStart;
    private int[] _msPausedPerPeriodInCurrentAggregate;
    private int _msPausedWithinCurrentAggregate;

    @SuppressWarnings("ConstantConditions")
    public FrameRateReporterImpl(int periodsPerAggregate,
                                 Collection<FrameRateReporterAggregateOutput> aggregateOutputs) {
        PERIODS_PER_AGGREGATE = Check.throwOnLteZero(periodsPerAggregate, "periodsPerAggregate");
        AGGREGATE_OUTPUTS = aggregateOutputs;
        AGGREGATE_OUTPUTS_ACTIVATION_STATUSES= new HashMap<>();
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
        _targetFpsInCurrentAggregate = new Float[PERIODS_PER_AGGREGATE];
        _actualFpsInCurrentAggregate = new float[PERIODS_PER_AGGREGATE];
        _periodWithinCurrentAggregate = 0;
        _aggregateTargetFpsDivisor = 0;
        _msPausedPerPeriodInCurrentAggregate = new int[PERIODS_PER_AGGREGATE];
        _msPausedWithinCurrentAggregate = 0;
    }

    @Override
    public void reportFrameRate(long datetime, Float targetFps, float actualFps)
            throws IllegalArgumentException {
        synchronized (this) {
            if (_lastReportedDate != null && datetime - _lastReportedDate != MS_PER_SECOND) {
                throw new IllegalArgumentException("FrameRateReporterImpl.reportFrameRate: " +
                        "datetime must be precisely 1000ms after previously reported datetime");
            }
            if (targetFps != null) {
                Check.throwOnLtValue(targetFps, 0f, "targetFps");
            }
            Check.throwOnLtValue(actualFps, 0f, "actualFps");

            if (_periodWithinCurrentAggregate == 0) {
                _currentAggregateStartDate = datetime;
            }

            if (targetFps != null) {
                _targetFpsInCurrentAggregate[_periodWithinCurrentAggregate] = targetFps;
            }
            _actualFpsInCurrentAggregate[_periodWithinCurrentAggregate] = actualFps;

            if (_pauseStart != null) {
                _msPausedPerPeriodInCurrentAggregate[_periodWithinCurrentAggregate] +=
                        Math.min(datetime - _pauseStart + MS_PER_SECOND, MS_PER_SECOND);
                _msPausedPerPeriodInCurrentAggregate[_periodWithinCurrentAggregate] = Math.min(
                        _msPausedPerPeriodInCurrentAggregate[_periodWithinCurrentAggregate],
                        MS_PER_SECOND
                );
            }
            if (targetFps != null) {
                _aggregateTargetFpsDivisor += (MS_PER_SECOND -
                        _msPausedPerPeriodInCurrentAggregate[_periodWithinCurrentAggregate]);
            }
            _msPausedWithinCurrentAggregate +=
                    _msPausedPerPeriodInCurrentAggregate[_periodWithinCurrentAggregate];

            if (_periodWithinCurrentAggregate == PERIODS_PER_AGGREGATE - 1) {
                Float aggregateTargetFps = null;
                float aggregateActualFps = 0f;
                float aggregateActualFpsDivisor = (float) PERIODS_PER_AGGREGATE -
                        (_msPausedWithinCurrentAggregate / (float) MS_PER_SECOND);
                boolean entireAggregatePaused = _msPausedWithinCurrentAggregate ==
                        MS_PER_SECOND * PERIODS_PER_AGGREGATE;
                for (int period = 0; period < PERIODS_PER_AGGREGATE; period++) {
                    if (!entireAggregatePaused) {
                        float percentageOfPeriodPausedAdj =
                                1f - (_msPausedPerPeriodInCurrentAggregate[period]
                                        / (float) MS_PER_SECOND);
                        if (_targetFpsInCurrentAggregate[period] != null) {
                            // I hate this statement. It works, but I hate it.
                            float toAddToAggregateTargetFps =
                                    (_targetFpsInCurrentAggregate[period] *
                                            percentageOfPeriodPausedAdj);
                            if (aggregateTargetFps == null) {
                                aggregateTargetFps = toAddToAggregateTargetFps;
                            } else {
                                aggregateTargetFps += toAddToAggregateTargetFps;
                            }
                        }
                        aggregateActualFps += (_actualFpsInCurrentAggregate[period] *
                                percentageOfPeriodPausedAdj);
                    }

                    _targetFpsInCurrentAggregate[period] = null;
                    _actualFpsInCurrentAggregate[period] = 0f;
                    _msPausedPerPeriodInCurrentAggregate[period] = 0;
                }

                if (aggregateTargetFps != null) {
                    aggregateTargetFps /= (_aggregateTargetFpsDivisor / (float) MS_PER_SECOND);
                }
                aggregateActualFps /= aggregateActualFpsDivisor;

                for (FrameRateReporterAggregateOutput aggregateOutput : AGGREGATE_OUTPUTS) {
                    if (AGGREGATE_OUTPUTS_ACTIVATION_STATUSES.get(aggregateOutput.id())) {
                        aggregateOutput.outputAggregateFrameRateData(
                                new Date(_currentAggregateStartDate),
                                entireAggregatePaused ? null : aggregateTargetFps,
                                entireAggregatePaused ? null : aggregateActualFps);
                    }
                }

                _msPausedWithinCurrentAggregate = 0;
                _periodWithinCurrentAggregate = 0;
                _aggregateTargetFpsDivisor = 0f;
            } else {
                _periodWithinCurrentAggregate++;
            }

            _lastReportedDate = datetime;

            if (_pauseStart == null) {
                _currentActualFps = actualFps;
            }
        }
    }

    @Override
    public Float currentActualFps() {
        return _currentActualFps;
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
            if (_lastReportedDate != null) {
                Check.throwOnLtValue(timestamp, nextReportingDate(), "timestamp");
                Check.throwOnGteValue(timestamp, nextReportingDate() + MS_PER_SECOND, "timestamp");
            }

            _currentActualFps = null;
            _pauseStart = timestamp;
        }
    }

    @Override
    public void reportUnpause(long timestamp) throws IllegalArgumentException {
        synchronized (this) {
            if (_lastReportedDate != null) {
                Check.throwOnLtValue(timestamp, nextReportingDate(), "timestamp");
                Check.throwOnGteValue(timestamp, nextReportingDate() + MS_PER_SECOND, "timestamp");
            }

            if (_lastReportedDate != null && _pauseStart != null) {
                _msPausedPerPeriodInCurrentAggregate[_periodWithinCurrentAggregate] +=
                        timestamp - Math.max(nextReportingDate(), _pauseStart);
            }
            _pauseStart = null;
        }
    }

    @Override
    public String getInterfaceName() {
        return FrameRateReporter.class.getCanonicalName();
    }

    private long nextReportingDate() {
        return _lastReportedDate + MS_PER_SECOND;
    }
}
