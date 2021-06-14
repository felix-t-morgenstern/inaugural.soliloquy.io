package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.graphics.shared.TimestampValidator;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.generic.HasOneGenericParam;
import soliloquy.specs.graphics.renderables.providers.PausableProviderWithOffset;

public abstract class PausableProviderWithOffsetAbstract<T>
        extends HasOneGenericParam<T>
        implements PausableProviderWithOffset<T> {
    private final int PERIOD_DURATION;
    private final TimestampValidator TIMESTAMP_VALIDATOR;

    private int _periodModuloOffset;
    private Long _pausedTimestamp;
    private Long _mostRecentReportedTimestamp;

    private long _timestampSentToProviderMostRecently;
    private int _periodModuloOffsetAtMostRecentProvision;
    private T _mostRecentlyProvidedValue;

    // NB: There is no way to combine a call to HasOneGenericParam(T) and the alternate constructor
    //     for this class, so the constructor logic must be duplicated; ensure that it is the same
    //     at all times!
    public PausableProviderWithOffsetAbstract(int periodDuration, int periodModuloOffset,
                                              T archetype) {
        super(archetype);
        if (periodModuloOffset >= periodDuration) {
            throw new IllegalArgumentException("GlobalLoopingAnimationImpl: periodModuloOffset (" +
                    periodModuloOffset + ") cannot be greater than period duration (" +
                    periodDuration + ")");
        }
        PERIOD_DURATION = periodDuration;
        _periodModuloOffset = Check.throwOnLtValue(periodModuloOffset, 0, "periodModuloOffset");
        TIMESTAMP_VALIDATOR = new TimestampValidator();
    }

    // NB: There is no way to combine a call to HasOneGenericParam(T) and the alternate constructor
    //     for this class, so the constructor logic must be duplicated; ensure that it is the same
    //     at all times!
    public PausableProviderWithOffsetAbstract(int periodDuration, int periodModuloOffset) {
        if (periodModuloOffset >= periodDuration) {
            throw new IllegalArgumentException("GlobalLoopingAnimationImpl: periodModuloOffset (" +
                    periodModuloOffset + ") cannot be greater than period duration (" +
                    periodDuration + ")");
        }
        PERIOD_DURATION = periodDuration;
        _periodModuloOffset = Check.throwOnLtValue(periodModuloOffset, 0, "periodModuloOffset");
        TIMESTAMP_VALIDATOR = new TimestampValidator();
    }

    @Override
    public int periodModuloOffset() {
        return _periodModuloOffset;
    }

    @Override
    public Long pausedTimestamp() {
        return _pausedTimestamp;
    }

    @Override
    public T provide(long timestamp) throws IllegalArgumentException {
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);
        long timestampForProvider;
        if (_pausedTimestamp == null) {
            timestampForProvider = timestamp;
        }
        else {
            timestampForProvider = _pausedTimestamp;
        }
        if (_timestampSentToProviderMostRecently == timestampForProvider &&
                _periodModuloOffset == _periodModuloOffsetAtMostRecentProvision) {
            return _mostRecentlyProvidedValue;
        }
        _timestampSentToProviderMostRecently = timestampForProvider;
        _periodModuloOffsetAtMostRecentProvision = _periodModuloOffset;
        int msForProvider = (int) ((timestampForProvider + _periodModuloOffset) % PERIOD_DURATION);

        return _mostRecentlyProvidedValue = provideValueAtMsWithinPeriod(msForProvider);
    }

    protected abstract T provideValueAtMsWithinPeriod(int msWithinPeriod);

    @Override
    public void reportPause(long timestamp) throws IllegalArgumentException {
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);
        if (_pausedTimestamp != null) {
            throw new IllegalArgumentException("GlobalLoopingAnimationImpl.reportPause: " +
                    "cannot pause if already paused");
        }
        if (_mostRecentReportedTimestamp != null && timestamp < _mostRecentReportedTimestamp) {
            throw new IllegalArgumentException("GlobalLoopingAnimationImpl.reportPause: " +
                    "cannot pause at timestamp prior to most recent unpausing");
        }
        _mostRecentReportedTimestamp = _pausedTimestamp = timestamp;
    }

    @Override
    public void reportUnpause(long timestamp) throws IllegalArgumentException {
        if (_pausedTimestamp == null) {
            throw new IllegalArgumentException("GlobalLoopingAnimationImpl.reportUnpause: " +
                    "cannot unpause if already unpaused");
        }
        if (_mostRecentReportedTimestamp != null && timestamp < _mostRecentReportedTimestamp) {
            throw new IllegalArgumentException("GlobalLoopingAnimationImpl.reportUnpause: " +
                    "cannot unpause at timestamp prior to most recent pausing");
        }
        _periodModuloOffset = (int)((_periodModuloOffset - (timestamp - _pausedTimestamp)
                + PERIOD_DURATION) % PERIOD_DURATION);
        while (_periodModuloOffset < 0) {
            _periodModuloOffset += PERIOD_DURATION;
        }
        _mostRecentReportedTimestamp = timestamp;
        _pausedTimestamp = null;
    }
}
