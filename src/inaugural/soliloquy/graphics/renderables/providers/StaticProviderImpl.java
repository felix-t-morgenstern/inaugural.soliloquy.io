package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.graphics.shared.TimestampValidator;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.generic.HasOneGenericParam;
import soliloquy.specs.graphics.renderables.providers.StaticProvider;

public class StaticProviderImpl<T> extends HasOneGenericParam<T> implements StaticProvider<T> {
    private final T VALUE;
    private final TimestampValidator TIMESTAMP_VALIDATOR;

    public StaticProviderImpl(T value) {
        this(value, value);
    }

    public StaticProviderImpl(T value, T archetype) {
        super(archetype);
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
}
