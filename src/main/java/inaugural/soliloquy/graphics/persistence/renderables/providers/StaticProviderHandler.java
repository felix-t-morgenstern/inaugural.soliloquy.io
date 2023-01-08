package inaugural.soliloquy.graphics.persistence.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.generic.CanGetInterfaceName;
import inaugural.soliloquy.tools.persistence.AbstractTypeWithOneGenericParamHandler;
import soliloquy.specs.common.persistence.PersistentValuesHandler;
import soliloquy.specs.graphics.renderables.providers.StaticProvider;
import soliloquy.specs.graphics.renderables.providers.factories.StaticProviderFactory;

import java.util.UUID;

import static inaugural.soliloquy.tools.generic.Archetypes.generateArchetypeWithOneGenericParam;

/** @noinspection rawtypes */
public class StaticProviderHandler
        extends AbstractTypeWithOneGenericParamHandler<StaticProvider> {
    private final StaticProviderFactory FACTORY;

    private static final CanGetInterfaceName CAN_GET_INTERFACE_NAME = new CanGetInterfaceName();

    public StaticProviderHandler(PersistentValuesHandler persistentValuesHandler,
                                 StaticProviderFactory factory) {
        //noinspection unchecked
        super(generateArchetypeWithOneGenericParam(StaticProvider.class, 0,
                        StaticProvider.class.getCanonicalName()), persistentValuesHandler,
                archetype -> generateArchetypeWithOneGenericParam(StaticProvider.class, archetype));
        FACTORY = Check.ifNull(factory, "factory");
    }

    @Override
    public StaticProvider read(String writtenValue) throws IllegalArgumentException {
        Check.ifNullOrEmpty(writtenValue, "writtenValue");
        var dto = JSON.fromJson(writtenValue, StaticProviderDTO.class);
        var uuid = UUID.fromString(dto.uuid);
        var typeHandler = PERSISTENT_VALUES_HANDLER.getTypeHandler(dto.innerType);
        return FACTORY.make(uuid,
                typeHandler.read(dto.val),
                PERSISTENT_VALUES_HANDLER.generateArchetype(dto.innerType),
                dto.mostRecentTimestamp);
    }

    @Override
    public String write(StaticProvider staticProvider) {
        Check.ifNull(staticProvider, "staticProvider");
        var innerType = CAN_GET_INTERFACE_NAME.getProperTypeName(staticProvider.getArchetype());
        var typeHandler = PERSISTENT_VALUES_HANDLER.getTypeHandler(innerType);
        StaticProviderDTO staticProviderDTO = new StaticProviderDTO();
        staticProviderDTO.uuid = staticProvider.uuid().toString();
        staticProviderDTO.innerType = innerType;
        staticProviderDTO.val = typeHandler
                .write(staticProvider.provide(staticProvider.mostRecentTimestamp()));
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
