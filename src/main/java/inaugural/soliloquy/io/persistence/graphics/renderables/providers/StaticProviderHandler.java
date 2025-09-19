package inaugural.soliloquy.io.persistence.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.persistence.AbstractTypeHandler;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.persistence.PersistenceHandler;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.util.UUID;
import java.util.function.BiFunction;

/** @noinspection rawtypes */
public class StaticProviderHandler extends AbstractTypeHandler<ProviderAtTime> {
    private final BiFunction<UUID, Object, ProviderAtTime> FACTORY;
    private final PersistenceHandler PERSISTENCE_HANDLER;
    private final TimestampValidator TIMESTAMP_VALIDATOR;

    public StaticProviderHandler(PersistenceHandler persistenceHandler,
                                 BiFunction<UUID, Object, ProviderAtTime> factory,
                                 TimestampValidator timestampValidator) {
        PERSISTENCE_HANDLER = Check.ifNull(persistenceHandler, "persistenceHandler");
        FACTORY = Check.ifNull(factory, "factory");
        TIMESTAMP_VALIDATOR = Check.ifNull(timestampValidator, "timestampValidator");
    }

    @SuppressWarnings("unchecked")
    @Override
    public ProviderAtTime read(String writtenValue) throws IllegalArgumentException {
        Check.ifNullOrEmpty(writtenValue, "writtenValue");
        var dto = JSON.fromJson(writtenValue, StaticProviderDTO.class);
        var uuid = UUID.fromString(dto.uuid);
        var typeHandler = PERSISTENCE_HANDLER.getTypeHandler(dto.innerType);
        return (ProviderAtTime) FACTORY.apply(uuid, typeHandler.read(dto.val));
    }

    @Override
    public String write(ProviderAtTime provider) {
        Check.ifNull(provider, "provider");

        var staticProviderDTO = new StaticProviderDTO();

        var mostRecentTimestamp = TIMESTAMP_VALIDATOR.mostRecentTimestamp();
        var staticValue = provider.provide(mostRecentTimestamp);
        if (staticValue != null) {
            var type = staticValue.getClass().getCanonicalName();
            var typeHandler = PERSISTENCE_HANDLER.getTypeHandler(type);
            staticProviderDTO.uuid = provider.uuid().toString();
            staticProviderDTO.innerType = type;
            staticProviderDTO.val = typeHandler
                    .write(provider.provide(mostRecentTimestamp));
        }

        return JSON.toJson(staticProviderDTO);
    }

    private static class StaticProviderDTO {
        String uuid;
        String innerType;
        String val;
    }
}
