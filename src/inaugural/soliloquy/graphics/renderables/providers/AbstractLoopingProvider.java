package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.AbstractLoopingPausableAtTime;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.providers.LoopingProvider;

public abstract class AbstractLoopingProvider<T> extends AbstractLoopingPausableAtTime
        implements LoopingProvider<T> {
    private final EntityUuid ID;

    private long _timestampSentToProviderMostRecently;
    private int _periodModuloOffsetAtMostRecentProvision;
    private T _mostRecentlyProvidedValue;

    public AbstractLoopingProvider(EntityUuid uuid, int periodDuration, int periodModuloOffset,
                                   Long pauseTimestamp, Long mostRecentTimestamp) {
        super(periodDuration, periodModuloOffset, pauseTimestamp, mostRecentTimestamp);
        ID = Check.ifNull(uuid, "uuid");
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
    public EntityUuid uuid() {
        return ID;
    }

    @Override
    public Long mostRecentTimestamp() {
        return TIMESTAMP_VALIDATOR.mostRecentTimestamp();
    }
}
