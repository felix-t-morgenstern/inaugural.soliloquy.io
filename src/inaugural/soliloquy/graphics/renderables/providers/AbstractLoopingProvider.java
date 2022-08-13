package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.AbstractLoopingPausableAtTime;
import soliloquy.specs.graphics.renderables.providers.LoopingProvider;

import java.util.UUID;

public abstract class AbstractLoopingProvider<T> extends AbstractLoopingPausableAtTime
        implements LoopingProvider<T> {
    private final UUID UUID;

    private long _timestampSentToProviderMostRecently;
    private int _periodModuloOffsetAtMostRecentProvision;
    private T _mostRecentlyProvidedValue;

    protected AbstractLoopingProvider(UUID uuid, int periodDuration, int periodModuloOffset,
                                      Long pauseTimestamp, Long mostRecentTimestamp) {
        super(periodDuration, periodModuloOffset, pauseTimestamp, mostRecentTimestamp);
        UUID = Check.ifNull(uuid, "uuid");
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
    public UUID uuid() {
        return UUID;
    }

    @Override
    public Long mostRecentTimestamp() {
        return TIMESTAMP_VALIDATOR.mostRecentTimestamp();
    }

    @Override
    public void reset(long timestamp) throws IllegalArgumentException {
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);

        _periodModuloOffset = PERIOD_DURATION - (int) (timestamp % PERIOD_DURATION);
    }
}
