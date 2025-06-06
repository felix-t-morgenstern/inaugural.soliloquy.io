package inaugural.soliloquy.graphics.persistence.renderables.providers;

import inaugural.soliloquy.graphics.renderables.providers.StaticProviderImpl;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.persistence.AbstractTypeHandler;
import soliloquy.specs.common.persistence.PersistenceHandler;
import soliloquy.specs.graphics.renderables.providers.StaticProvider;
import soliloquy.specs.graphics.renderables.providers.factories.StaticProviderFactory;

import java.util.UUID;

/** @noinspection rawtypes */
public class StaticProviderHandler extends AbstractTypeHandler<StaticProvider> {
    private final StaticProviderFactory FACTORY;
    private final PersistenceHandler PERSISTENCE_HANDLER;

    public StaticProviderHandler(PersistenceHandler persistenceHandler,
                                 StaticProviderFactory factory) {
        PERSISTENCE_HANDLER = Check.ifNull(persistenceHandler, "persistenceHandler");
        FACTORY = Check.ifNull(factory, "factory");
    }

    @Override
    public String typeHandled() {
        return StaticProviderImpl.class.getCanonicalName();
    }

    @SuppressWarnings("unchecked")
    @Override
    public StaticProvider read(String writtenValue) throws IllegalArgumentException {
        Check.ifNullOrEmpty(writtenValue, "writtenValue");
        var dto = JSON.fromJson(writtenValue, StaticProviderDTO.class);
        var uuid = UUID.fromString(dto.uuid);
        var typeHandler = PERSISTENCE_HANDLER.getTypeHandler(dto.innerType);
        return FACTORY.make(uuid,
                typeHandler.read(dto.val),
                dto.mostRecentTimestamp);
    }

    @Override
    public String write(StaticProvider staticProvider) {
        Check.ifNull(staticProvider, "staticProvider");

        var staticProviderDTO = new StaticProviderDTO();

        var staticValue = staticProvider.provide(staticProvider.mostRecentTimestamp());
        if (staticValue != null) {
            var type = staticValue.getClass().getCanonicalName();
            var typeHandler = PERSISTENCE_HANDLER.getTypeHandler(type);
            staticProviderDTO.uuid = staticProvider.uuid().toString();
            staticProviderDTO.innerType = type;
            staticProviderDTO.val = typeHandler
                    .write(staticProvider.provide(staticProvider.mostRecentTimestamp()));
        }

        staticProviderDTO.mostRecentTimestamp = staticProvider.mostRecentTimestamp();
        return JSON.toJson(staticProviderDTO);
    }

    private static class StaticProviderDTO {
        String uuid;
        String innerType;
        String val;
        Long mostRecentTimestamp;
    }
}
