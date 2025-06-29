package inaugural.soliloquy.io.persistence.graphics.renderables.providers;

import inaugural.soliloquy.io.graphics.renderables.providers.ValuesAtTimestampType;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.persistence.AbstractTypeHandler;
import soliloquy.specs.common.persistence.PersistenceHandler;
import soliloquy.specs.io.graphics.renderables.providers.LoopingLinearMovingProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.LoopingLinearMovingProviderFactory;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;

/** @noinspection rawtypes */
public class LoopingLinearMovingProviderHandler extends AbstractTypeHandler<LoopingLinearMovingProvider> {
    private final LoopingLinearMovingProviderFactory LOOPING_LINEAR_MOVING_PROVIDER_FACTORY;
    private final PersistenceHandler PERSISTENCE_HANDLER;

    public LoopingLinearMovingProviderHandler(PersistenceHandler persistenceHandler,
                                              LoopingLinearMovingProviderFactory
                                                      loopingLinearMovingProviderFactory) {
        PERSISTENCE_HANDLER = Check.ifNull(persistenceHandler, "persistenceHandler");
        LOOPING_LINEAR_MOVING_PROVIDER_FACTORY = Check.ifNull(loopingLinearMovingProviderFactory,
                "loopingLinearMovingProviderFactory");
    }

    @Override
    public String typeHandled() {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public LoopingLinearMovingProvider read(String writtenValue) throws IllegalArgumentException {
        var dto = JSON.fromJson(Check.ifNullOrEmpty(writtenValue, "writtenValue"),
                LoopingLinearMovingProviderDto.class);

        var innerTypeHandler = PERSISTENCE_HANDLER.getTypeHandler(dto.type);
        Map<Integer, Object> valuesWithinPeriod = mapOf();
        for (var valueWithinPeriodDto : dto.valueAtTimes) {
            valuesWithinPeriod.put(valueWithinPeriodDto.time,
                    innerTypeHandler.read(valueWithinPeriodDto.value));
        }

        return LOOPING_LINEAR_MOVING_PROVIDER_FACTORY.make(UUID.fromString(dto.id), dto.duration,
                dto.offset, valuesWithinPeriod, dto.pausedTimestamp, dto.mostRecentTimestamp);
    }

    @Override
    public String write(LoopingLinearMovingProvider loopingLinearMovingProvider) {
        Check.ifNull(loopingLinearMovingProvider, "loopingLinearMovingProvider");

        var dto = new LoopingLinearMovingProviderDto();

        dto.id = loopingLinearMovingProvider.uuid().toString();

        dto.duration = loopingLinearMovingProvider.periodDuration();

        dto.offset = loopingLinearMovingProvider.periodModuloOffset();

        //noinspection unchecked
        dto.type = ValuesAtTimestampType.get(loopingLinearMovingProvider.valuesWithinPeriod());

        var innerTypeHandler = PERSISTENCE_HANDLER.getTypeHandler(dto.type);
        //noinspection unchecked
        Map<Integer, Object> valuesWithinPeriod = loopingLinearMovingProvider.valuesWithinPeriod();
        var valuesWithinPeriodSize = valuesWithinPeriod.size();
        dto.valueAtTimes = new LoopingLinearMovingProviderValueAtTimeDto[valuesWithinPeriodSize];
        var index = 0;
        for (Map.Entry<Integer, Object> valueWithinPeriod : valuesWithinPeriod.entrySet()) {
            var valueWithinPeriodDto = new LoopingLinearMovingProviderValueAtTimeDto();
            valueWithinPeriodDto.time = valueWithinPeriod.getKey();
            valueWithinPeriodDto.value = innerTypeHandler.write(valueWithinPeriod.getValue());

            dto.valueAtTimes[index++] = valueWithinPeriodDto;
        }

        dto.pausedTimestamp = loopingLinearMovingProvider.pausedTimestamp();

        dto.mostRecentTimestamp = loopingLinearMovingProvider.mostRecentTimestamp();

        return JSON.toJson(dto);
    }

    private static class LoopingLinearMovingProviderDto {
        String id;
        int duration;
        int offset;
        LoopingLinearMovingProviderValueAtTimeDto[] valueAtTimes;
        Long pausedTimestamp;
        Long mostRecentTimestamp;
        String type;
    }

    private static class LoopingLinearMovingProviderValueAtTimeDto {
        int time;
        String value;
    }
}
