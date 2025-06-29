package inaugural.soliloquy.io.persistence.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.persistence.AbstractTypeHandler;
import soliloquy.specs.common.persistence.PersistenceHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.graphics.renderables.providers.FiniteLinearMovingProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.FiniteLinearMovingProviderFactory;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;

/** @noinspection rawtypes */
public class FiniteLinearMovingProviderHandler extends
        AbstractTypeHandler<FiniteLinearMovingProvider> {
    private final PersistenceHandler PERSISTENCE_HANDLER;
    private final FiniteLinearMovingProviderFactory FACTORY;

    public FiniteLinearMovingProviderHandler(PersistenceHandler persistenceHandler,
                                             FiniteLinearMovingProviderFactory factory) {
        PERSISTENCE_HANDLER = Check.ifNull(persistenceHandler, "persistenceHandler");
        FACTORY = Check.ifNull(factory, "factory");
    }

    @SuppressWarnings("unchecked")
    @Override
    public FiniteLinearMovingProvider read(String writtenValue) throws IllegalArgumentException {
        var dto = JSON.fromJson(Check.ifNullOrEmpty(writtenValue, "writtenValue"),
                FiniteLinearMovingProviderDTO.class);
        var typeHandler = PERSISTENCE_HANDLER.getTypeHandler(dto.valueType);
        Map<Long, Object> valuesAtTimestamps = mapOf();
        for (var valueDto : dto.values) {
            valuesAtTimestamps.put(valueDto.timestamp, typeHandler.read(valueDto.value));
        }
        return FACTORY.make(UUID.fromString(dto.uuid), valuesAtTimestamps, dto.pausedTimestamp,
                dto.mostRecentTimestamp);
    }

    @Override
    public String typeHandled() {
        return null;
    }

    @Override
    public String write(FiniteLinearMovingProvider finiteLinearMovingProvider) {
        Check.ifNull(finiteLinearMovingProvider, "finiteLinearMovingProvider");

        var dto = new FiniteLinearMovingProviderDTO();

        dto.uuid = finiteLinearMovingProvider.uuid().toString();

        //noinspection unchecked
        Map<Long, Object> valuesAtTimestamps =
                finiteLinearMovingProvider.valuesAtTimestampsRepresentation();
        var firstNonNullValue =
                valuesAtTimestamps.values().stream().filter(Objects::nonNull).findFirst();

        TypeHandler typeHandler = null;
        if (firstNonNullValue.isPresent()) {
            var valueType = dto.valueType = firstNonNullValue.get().getClass().getCanonicalName();
            typeHandler = PERSISTENCE_HANDLER.getTypeHandler(valueType);
        }

        var valuesSize = valuesAtTimestamps.size();
        dto.values = new FiniteLinearMovingProviderValueAtTimestampDTO[valuesSize];
        var index = 0;
        for (var valueAtTimestamp : valuesAtTimestamps.entrySet()) {
            var valueDto = new FiniteLinearMovingProviderValueAtTimestampDTO();

            valueDto.timestamp = valueAtTimestamp.getKey();
            if (valueAtTimestamp.getValue() == null) {
                valueDto.value = null;
            }
            else {
                //noinspection unchecked
                valueDto.value = typeHandler.write(valueAtTimestamp.getValue());
            }
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
