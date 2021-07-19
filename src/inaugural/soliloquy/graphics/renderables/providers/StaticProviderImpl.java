package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.generic.HasOneGenericParam;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.providers.StaticProvider;

public class StaticProviderImpl<T> extends HasOneGenericParam<T> implements StaticProvider<T> {
    private final EntityUuid ID;
    private final T VALUE;
    private final TimestampValidator TIMESTAMP_VALIDATOR;

    public StaticProviderImpl(EntityUuid uuid, T value) {
        this(uuid, value, value);
    }

    public StaticProviderImpl(EntityUuid uuid, T value, T archetype) {
        super(archetype);
        ID = Check.ifNull(uuid, "uuid");
        VALUE = value;
        TIMESTAMP_VALIDATOR = new TimestampValidator();
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
    public EntityUuid uuid() {
        return ID;
    }
}
