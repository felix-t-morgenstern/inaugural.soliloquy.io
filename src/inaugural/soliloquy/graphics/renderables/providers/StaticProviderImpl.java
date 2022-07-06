package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.generic.AbstractHasOneGenericParam;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.graphics.renderables.providers.StaticProvider;

import java.util.UUID;

public class StaticProviderImpl<T>
        extends AbstractHasOneGenericParam<T>
        implements StaticProvider<T> {
    private final UUID UUID;
    private final T VALUE;
    private final TimestampValidator TIMESTAMP_VALIDATOR;

    public StaticProviderImpl(UUID uuid, T value, Long mostRecentTimestamp) {
        this(uuid, value, value, mostRecentTimestamp);
    }

    @SuppressWarnings("ConstantConditions")
    public StaticProviderImpl(UUID uuid, T value, T archetype, Long mostRecentTimestamp) {
        super(archetype);
        UUID = Check.ifNull(uuid, "uuid");
        VALUE = value;
        TIMESTAMP_VALIDATOR = new TimestampValidator(mostRecentTimestamp);
    }

    @Override
    protected String getUnparameterizedInterfaceName() {
        return StaticProvider.class.getCanonicalName();
    }

    @Override
    public T provide(long timestamp) throws IllegalArgumentException {
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);
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

    @Override
    public Long mostRecentTimestamp() {
        return TIMESTAMP_VALIDATOR.mostRecentTimestamp();
    }
}
