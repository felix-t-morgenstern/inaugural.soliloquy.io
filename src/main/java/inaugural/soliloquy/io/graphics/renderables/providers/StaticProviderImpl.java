package inaugural.soliloquy.io.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.util.UUID;

public class StaticProviderImpl<T> implements ProviderAtTime<T> {
    private final UUID UUID;
    private final T VALUE;
    private final TimestampValidator TIMESTAMP_VALIDATOR;

    public StaticProviderImpl(UUID uuid, T value, TimestampValidator timestampValidator) {
        UUID = Check.ifNull(uuid, "uuid");
        VALUE = value;
        TIMESTAMP_VALIDATOR = Check.ifNull(timestampValidator, "timestampValidator");
    }

    @Override
    public T provide(long timestamp) throws IllegalArgumentException {
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);
        return VALUE;
    }

    @Override
    public Object representation() {
        return VALUE;
    }

    @Override
    public void reportPause(long timestamp) throws IllegalArgumentException {
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);
    }

    @Override
    public void reportUnpause(long timestamp) throws IllegalArgumentException {
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);
    }

    @Override
    public Long pausedTimestamp() {
        throw new UnsupportedOperationException();
    }

    @Override
    public UUID uuid() {
        return UUID;
    }
}
