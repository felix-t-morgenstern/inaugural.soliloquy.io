package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.AbstractLoopingPausableAtTime;
import soliloquy.specs.graphics.renderables.providers.LoopingProvider;

import java.util.UUID;

public abstract class AbstractLoopingProvider<T> extends AbstractLoopingPausableAtTime
        implements LoopingProvider<T> {
    private final UUID UUID;
    private final T ARCHETYPE;

    private long _timestampSentToProviderMostRecently;
    private int _periodModuloOffsetAtMostRecentProvision;
    private T _mostRecentlyProvidedValue;

    protected AbstractLoopingProvider(UUID uuid, int periodDuration, int periodModuloOffset,
                                      Long pauseTimestamp, Long mostRecentTimestamp, T archetype) {
        super(periodDuration, periodModuloOffset, pauseTimestamp, mostRecentTimestamp);
        UUID = Check.ifNull(uuid, "uuid");
        ARCHETYPE = Check.ifNull(archetype, "archetype");
    }

    @Override
    public T provide(long timestamp) throws IllegalArgumentException {
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);
        long timestampForProvider;
        if (pausedTimestamp == null) {
            timestampForProvider = timestamp;
        }
        else {
            timestampForProvider = pausedTimestamp;
        }
        if (_timestampSentToProviderMostRecently == timestampForProvider &&
                periodModuloOffset == _periodModuloOffsetAtMostRecentProvision) {
            return _mostRecentlyProvidedValue;
        }
        _timestampSentToProviderMostRecently = timestampForProvider;
        _periodModuloOffsetAtMostRecentProvision = periodModuloOffset;
        int msForProvider = (int) ((timestampForProvider + periodModuloOffset) % PERIOD_DURATION);

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

        periodModuloOffset = PERIOD_DURATION - (int) (timestamp % PERIOD_DURATION);
    }

    @Override
    public T archetype() {
        return ARCHETYPE;
    }
}
