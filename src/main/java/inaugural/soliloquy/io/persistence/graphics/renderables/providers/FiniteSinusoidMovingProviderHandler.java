package inaugural.soliloquy.io.persistence.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.persistence.AbstractTypeHandler;
import soliloquy.specs.common.persistence.PersistenceHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.graphics.renderables.providers.FiniteSinusoidMovingProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.FiniteSinusoidMovingProviderFactory;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;

@SuppressWarnings("rawtypes")
public class FiniteSinusoidMovingProviderHandler extends
        AbstractTypeHandler<FiniteSinusoidMovingProvider> {
    private final PersistenceHandler PERSISTENCE_HANDLER;
    private final FiniteSinusoidMovingProviderFactory FACTORY;

    public FiniteSinusoidMovingProviderHandler(PersistenceHandler persistenceHandler,
                                               FiniteSinusoidMovingProviderFactory factory) {
        PERSISTENCE_HANDLER = Check.ifNull(persistenceHandler, "persistenceHandler");
        FACTORY = Check.ifNull(factory, "factory");
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public FiniteSinusoidMovingProvider read(String writtenValue) throws IllegalArgumentException {
        var dto = JSON.fromJson(Check.ifNullOrEmpty(writtenValue, "writtenValue"),
                Dto.class);
        var typeHandler = PERSISTENCE_HANDLER.getTypeHandler(dto.type);
        Map<Long, Object> valuesAtTimestamps = mapOf();
        for (var valueDto : dto.vals) {
            valuesAtTimestamps.put(valueDto.time, typeHandler.read(valueDto.val));
        }
        return FACTORY.make(UUID.fromString(dto.uuid), valuesAtTimestamps, dto.sharpnesses, dto.pausedTimestamp);
    }

    @Override
    public String write(FiniteSinusoidMovingProvider provider) {
        Check.ifNull(provider, "provider");

        var dto = new Dto();

        dto.uuid = provider.uuid().toString();

        //noinspection unchecked
        Map<Long, Object> valuesAtTimestamps = provider.valuesAtTimestampsRepresentation();
        var firstNonNullValue = valuesAtTimestamps.values().stream().filter(Objects::nonNull).findFirst();

        @SuppressWarnings("rawtypes") TypeHandler typeHandler = null;
        if (firstNonNullValue.isPresent()) {
            var valueType = dto.type = firstNonNullValue.get().getClass().getCanonicalName();
            typeHandler = PERSISTENCE_HANDLER.getTypeHandler(valueType);
        }

        var valuesSize = valuesAtTimestamps.size();
        dto.vals = new ValueAtTimestampDTO[valuesSize];
        var index = 0;
        for (var valueAtTimestamp : valuesAtTimestamps.entrySet()) {
            var valueDto = new ValueAtTimestampDTO();

            valueDto.time = valueAtTimestamp.getKey();
            if (valueAtTimestamp.getValue() == null) {
                valueDto.val = null;
            }
            else {
                //noinspection unchecked
                valueDto.val = typeHandler.write(valueAtTimestamp.getValue());
            }
            dto.vals[index++] = valueDto;
        }

        dto.sharpnesses = provider.transitionSharpnesses();

        dto.pausedTimestamp = provider.pausedTimestamp();

        return JSON.toJson(dto);
    }

    private static class Dto {
        String uuid;
        String type;
        ValueAtTimestampDTO[] vals;
        float[] sharpnesses;
        Long pausedTimestamp;
    }

    private static class ValueAtTimestampDTO {
        long time;
        String val;
    }
}
