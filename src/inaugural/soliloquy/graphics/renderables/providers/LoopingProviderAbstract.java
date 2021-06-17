package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.tools.timing.LoopingPausableAtTimeAbstract;
import soliloquy.specs.graphics.renderables.providers.LoopingProvider;

public abstract class LoopingProviderAbstract<T>
        extends LoopingPausableAtTimeAbstract
        implements LoopingProvider<T> {
    private long _timestampSentToProviderMostRecently;
    private int _periodModuloOffsetAtMostRecentProvision;
    private T _mostRecentlyProvidedValue;

    public LoopingProviderAbstract(int periodDuration, int periodModuloOffset) {
        // TODO: Draft a constructor with a pausedTimestamp
        super(periodDuration, periodModuloOffset, null);
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
}
