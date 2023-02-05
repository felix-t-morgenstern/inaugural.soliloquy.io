package inaugural.soliloquy.graphics.persistence.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.generic.CanGetInterfaceName;
import inaugural.soliloquy.tools.persistence.AbstractTypeWithOneGenericParamHandler;
import soliloquy.specs.common.persistence.PersistentValuesHandler;
import soliloquy.specs.graphics.renderables.providers.LoopingLinearMovingProvider;
import soliloquy.specs.graphics.renderables.providers.factories.LoopingLinearMovingProviderFactory;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static inaugural.soliloquy.tools.generic.Archetypes.generateArchetypeWithOneGenericParam;

/** @noinspection rawtypes */
public class LoopingLinearMovingProviderHandler
        extends AbstractTypeWithOneGenericParamHandler<LoopingLinearMovingProvider> {
    private final LoopingLinearMovingProviderFactory LOOPING_LINEAR_MOVING_PROVIDER_FACTORY;

    private static final CanGetInterfaceName CAN_GET_INTERFACE_NAME = new CanGetInterfaceName();

    public LoopingLinearMovingProviderHandler(PersistentValuesHandler persistentValuesHandler,
                                              LoopingLinearMovingProviderFactory
                                                      loopingLinearMovingProviderFactory) {
        //noinspection unchecked
        super(generateArchetypeWithOneGenericParam(LoopingLinearMovingProvider.class, 0,
                        LoopingLinearMovingProvider.class.getCanonicalName()),
                persistentValuesHandler,
                archetype -> generateArchetypeWithOneGenericParam(LoopingLinearMovingProvider.class,
                        archetype));
        LOOPING_LINEAR_MOVING_PROVIDER_FACTORY = Check.ifNull(loopingLinearMovingProviderFactory,
                "loopingLinearMovingProviderFactory");
    }

    @Override
    public LoopingLinearMovingProvider read(String writtenValue) throws IllegalArgumentException {
        var dto = JSON.fromJson(Check.ifNullOrEmpty(writtenValue, "writtenValue"),
                LoopingLinearMovingProviderDto.class);

        var innerTypeHandler = PERSISTENT_VALUES_HANDLER.getTypeHandler(dto.type);
        Map<Integer, Object> valuesWithinPeriod = mapOf();
        for (var valueWithinPeriodDto : dto.valueAtTimes) {
            valuesWithinPeriod.put(valueWithinPeriodDto.time,
                    innerTypeHandler.read(valueWithinPeriodDto.value));
        }

        return LOOPING_LINEAR_MOVING_PROVIDER_FACTORY.make(UUID.fromString(dto.id), dto.duration,
                dto.offset, valuesWithinPeriod, dto.pausedTimestamp, dto.mostRecentTimestamp,
                PERSISTENT_VALUES_HANDLER.generateArchetype(dto.type));
    }

    @Override
    public String write(LoopingLinearMovingProvider loopingLinearMovingProvider) {
        Check.ifNull(loopingLinearMovingProvider, "loopingLinearMovingProvider");

        var dto = new LoopingLinearMovingProviderDto();

        dto.id = loopingLinearMovingProvider.uuid().toString();

        dto.duration = loopingLinearMovingProvider.periodDuration();

        dto.offset = loopingLinearMovingProvider.periodModuloOffset();

        dto.type = CAN_GET_INTERFACE_NAME
                .getProperTypeName(loopingLinearMovingProvider.getArchetype());

        var innerTypeHandler = PERSISTENT_VALUES_HANDLER.getTypeHandler(dto.type);
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
