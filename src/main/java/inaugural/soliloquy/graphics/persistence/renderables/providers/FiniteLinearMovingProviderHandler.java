package inaugural.soliloquy.graphics.persistence.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.generic.CanGetInterfaceName;
import inaugural.soliloquy.tools.persistence.AbstractTypeWithOneGenericParamHandler;
import soliloquy.specs.common.persistence.PersistentValuesHandler;
import soliloquy.specs.graphics.renderables.providers.FiniteLinearMovingProvider;
import soliloquy.specs.graphics.renderables.providers.factories.FiniteLinearMovingProviderFactory;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.generic.Archetypes.generateArchetypeWithOneGenericParam;

/** @noinspection rawtypes */
public class FiniteLinearMovingProviderHandler
        extends AbstractTypeWithOneGenericParamHandler<FiniteLinearMovingProvider> {
    private final FiniteLinearMovingProviderFactory FACTORY;

    private static final CanGetInterfaceName CAN_GET_INTERFACE_NAME = new CanGetInterfaceName();

    public FiniteLinearMovingProviderHandler(PersistentValuesHandler persistentValuesHandler,
                                             FiniteLinearMovingProviderFactory factory) {
        //noinspection unchecked
        super(generateArchetypeWithOneGenericParam(FiniteLinearMovingProvider.class, 0,
                        FiniteLinearMovingProvider.class.getCanonicalName()),
                persistentValuesHandler,
                archetype -> generateArchetypeWithOneGenericParam(FiniteLinearMovingProvider.class,
                        archetype));
        FACTORY = Check.ifNull(factory, "factory");
    }

    @Override
    public FiniteLinearMovingProvider read(String writtenValue) throws IllegalArgumentException {
        var dto = JSON.fromJson(Check.ifNullOrEmpty(writtenValue, "writtenValue"),
                FiniteLinearMovingProviderDTO.class);
        var typeHandler = PERSISTENT_VALUES_HANDLER.getTypeHandler(dto.valueType);
        Map<Long, Object> valuesAtTimestamps = mapOf();
        for (var valueDto : dto.values) {
            valuesAtTimestamps.put(valueDto.timestamp, typeHandler.read(valueDto.value));
        }
        return FACTORY.make(UUID.fromString(dto.uuid), valuesAtTimestamps, dto.pausedTimestamp,
                dto.mostRecentTimestamp);
    }

    @Override
    public String write(FiniteLinearMovingProvider finiteLinearMovingProvider) {
        Check.ifNull(finiteLinearMovingProvider, "finiteLinearMovingProvider");

        var dto = new FiniteLinearMovingProviderDTO();

        dto.uuid = finiteLinearMovingProvider.uuid().toString();

        var valueType = dto.valueType =
                CAN_GET_INTERFACE_NAME.getProperTypeName(finiteLinearMovingProvider.getArchetype());

        var typeHandler = PERSISTENT_VALUES_HANDLER.getTypeHandler(valueType);
        //noinspection unchecked
        Map<Long, Object> valuesAtTimestamps =
                finiteLinearMovingProvider.valuesAtTimestampsRepresentation();
        var valuesSize = valuesAtTimestamps.size();
        dto.values = new FiniteLinearMovingProviderValueAtTimestampDTO[valuesSize];
        var index = 0;
        for (var valueAtTimestamp : valuesAtTimestamps.entrySet()) {
            var valueDto = new FiniteLinearMovingProviderValueAtTimestampDTO();

            valueDto.timestamp = valueAtTimestamp.getKey();
            valueDto.value = typeHandler.write(valueAtTimestamp.getValue());
            dto.values[index++] = valueDto;
        }

        dto.pausedTimestamp = finiteLinearMovingProvider.pausedTimestamp();

        dto.mostRecentTimestamp = finiteLinearMovingProvider.mostRecentTimestamp();

        return JSON.toJson(dto);
    }

    private static class FiniteLinearMovingProviderDTO {
        String uuid;
        String valueType;
        FiniteLinearMovingProviderValueAtTimestampDTO[] values;
        Long pausedTimestamp;
        Long mostRecentTimestamp;
    }

    private static class FiniteLinearMovingProviderValueAtTimestampDTO {
        long timestamp;
        String value;
    }
}
