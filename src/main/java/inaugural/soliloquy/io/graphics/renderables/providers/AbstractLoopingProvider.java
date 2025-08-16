package inaugural.soliloquy.io.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.AbstractLoopingPausableAtTime;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.io.graphics.renderables.providers.LoopingProvider;

import java.util.Objects;
import java.util.UUID;

public abstract class AbstractLoopingProvider<T> extends AbstractLoopingPausableAtTime
        implements LoopingProvider<T> {
    private final UUID UUID;

    private long timestampSentToProviderMostRecently;
    private int periodModuloOffsetAtMostRecentProvision;
    private T mostRecentlyProvidedValue;

    protected AbstractLoopingProvider(UUID uuid,
                                      int periodDuration,
                                      int periodModuloOffset,
                                      Long pauseTimestamp,
                                      TimestampValidator timestampValidator) {
        super(periodDuration, periodModuloOffset, pauseTimestamp, timestampValidator);
        UUID = Check.ifNull(uuid, "uuid");
    }

    @Override
    public T provide(long timestamp) throws IllegalArgumentException {
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);
        long timestampForProvider = Objects.requireNonNullElse(pausedTimestamp, timestamp);
        if (timestampSentToProviderMostRecently == timestampForProvider &&
                periodModuloOffset == periodModuloOffsetAtMostRecentProvision) {
            return mostRecentlyProvidedValue;
        }
        timestampSentToProviderMostRecently = timestampForProvider;
        periodModuloOffsetAtMostRecentProvision = periodModuloOffset;
        var msForProvider = (int) ((timestampForProvider + periodModuloOffset) % PERIOD_DURATION);

        return mostRecentlyProvidedValue = provideValueAtMsWithinPeriod(msForProvider);
    }

    protected abstract T provideValueAtMsWithinPeriod(int msWithinPeriod);

    @Override
    public UUID uuid() {
        return UUID;
    }

    @Override
    public void reset(long timestamp) throws IllegalArgumentException {
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);

        periodModuloOffset = PERIOD_DURATION - (int) (timestamp % PERIOD_DURATION);
    }
}
