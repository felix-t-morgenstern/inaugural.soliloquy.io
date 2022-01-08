package inaugural.soliloquy.graphics.persistence.renderables.providers;

import com.google.gson.Gson;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.generic.AbstractHasOneGenericParam;
import inaugural.soliloquy.tools.generic.CanGetInterfaceName;
import inaugural.soliloquy.tools.persistence.AbstractTypeWithOneGenericParamHandler;
import soliloquy.specs.common.persistence.PersistentValuesHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.providers.LoopingMovingProvider;
import soliloquy.specs.graphics.renderables.providers.factories.LoopingLinearMovingProviderFactory;

import java.util.HashMap;
import java.util.Map;

/** @noinspection rawtypes*/
public class LoopingLinearMovingProviderHandler
        extends AbstractTypeWithOneGenericParamHandler<LoopingMovingProvider> {
    private final TypeHandler<EntityUuid> UUID_HANDLER;
    private final LoopingLinearMovingProviderFactory LOOPING_LINEAR_MOVING_PROVIDER_FACTORY;

    private static final CanGetInterfaceName CAN_GET_INTERFACE_NAME = new CanGetInterfaceName();
    private static final Gson GSON = new Gson();

    private final static LoopingLinearMovingProviderArchetype ARCHETYPE =
            new LoopingLinearMovingProviderArchetype();

    public LoopingLinearMovingProviderHandler(TypeHandler<EntityUuid> uuidHandler,
                                              PersistentValuesHandler persistentValuesHandler,
                                              LoopingLinearMovingProviderFactory
                                                      loopingLinearMovingProviderFactory) {
        super(ARCHETYPE, persistentValuesHandler,
                QualifiedLinearLinearMovingProviderArchetype::new);
        UUID_HANDLER = Check.ifNull(uuidHandler, "uuidHandler");
        LOOPING_LINEAR_MOVING_PROVIDER_FACTORY = Check.ifNull(loopingLinearMovingProviderFactory,
                "loopingLinearMovingProviderFactory");
    }

    @Override
    public LoopingMovingProvider read(String writtenValue) throws IllegalArgumentException {
        LoopingLinearMovingProviderDto dto = GSON.fromJson(
                Check.ifNullOrEmpty(writtenValue, "writtenValue"),
                LoopingLinearMovingProviderDto.class);

        TypeHandler innerTypeHandler = PERSISTENT_VALUES_HANDLER.getTypeHandler(dto.type);
        HashMap<Integer,Object> valuesWithinPeriod = new HashMap<>();
        for (LoopingLinearMovingProviderValueAtTimeDto valueWithinPeriodDto : dto.valueAtTimes) {
            valuesWithinPeriod.put(valueWithinPeriodDto.time,
                    innerTypeHandler.read(valueWithinPeriodDto.value));
        }

        return LOOPING_LINEAR_MOVING_PROVIDER_FACTORY.make(UUID_HANDLER.read(dto.id), dto.duration,
                dto.offset, valuesWithinPeriod, dto.pausedTimestamp, dto.mostRecentTimestamp,
                PERSISTENT_VALUES_HANDLER.generateArchetype(dto.type));
    }

    @Override
    public String write(LoopingMovingProvider loopingLinearMovingProvider) {
        Check.ifNull(loopingLinearMovingProvider, "loopingLinearMovingProvider");

        LoopingLinearMovingProviderDto dto = new LoopingLinearMovingProviderDto();

        dto.id = UUID_HANDLER.write(loopingLinearMovingProvider.uuid());

        dto.duration = loopingLinearMovingProvider.periodDuration();

        dto.offset = loopingLinearMovingProvider.periodModuloOffset();

        dto.type = CAN_GET_INTERFACE_NAME
                .getProperTypeName(loopingLinearMovingProvider.getArchetype());

        TypeHandler innerTypeHandler = PERSISTENT_VALUES_HANDLER.getTypeHandler(dto.type);
        //noinspection unchecked
        Map<Integer, Object> valuesWithinPeriod = loopingLinearMovingProvider.valuesWithinPeriod();
        int valuesWithinPeriodSize = valuesWithinPeriod.size();
        dto.valueAtTimes = new LoopingLinearMovingProviderValueAtTimeDto[valuesWithinPeriodSize];
        int index = 0;
        for (Map.Entry<Integer, Object> valueWithinPeriod : valuesWithinPeriod.entrySet()) {
            LoopingLinearMovingProviderValueAtTimeDto valueWithinPeriodDto =
                    new LoopingLinearMovingProviderValueAtTimeDto();
            valueWithinPeriodDto.time = valueWithinPeriod.getKey();
            //noinspection unchecked
            valueWithinPeriodDto.value = innerTypeHandler.write(valueWithinPeriod.getValue());

            dto.valueAtTimes[index++] = valueWithinPeriodDto;
        }

        dto.pausedTimestamp = loopingLinearMovingProvider.pausedTimestamp();

        dto.mostRecentTimestamp = loopingLinearMovingProvider.mostRecentTimestamp();

        return GSON.toJson(dto);
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

    private static class LoopingLinearMovingProviderArchetype implements LoopingMovingProvider {

        @Override
        public Map valuesWithinPeriod() {
            return null;
        }

        @Override
        public int periodDuration() {
            return 0;
        }

        @Override
        public int periodModuloOffset() {
            return 0;
        }

        @Override
        public void reset(long l) throws IllegalArgumentException {

        }

        @Override
        public Object provide(long l) throws IllegalArgumentException {
            return null;
        }

        @Override
        public Object getArchetype() {
            return 0;
        }

        @Override
        public EntityUuid uuid() {
            return null;
        }

        @Override
        public void reportPause(long l) throws IllegalArgumentException {

        }

        @Override
        public void reportUnpause(long l) throws IllegalArgumentException {

        }

        @Override
        public Long pausedTimestamp() {
            return null;
        }

        @Override
        public Long mostRecentTimestamp() {
            return null;
        }

        @Override
        public String getInterfaceName() {
            return LoopingMovingProvider.class.getCanonicalName();
        }
    }

    private static class QualifiedLinearLinearMovingProviderArchetype<T>
            extends AbstractHasOneGenericParam<T>
            implements LoopingMovingProvider<T> {
        private QualifiedLinearLinearMovingProviderArchetype(T archetype) {
            super(archetype);
        }

        @Override
        protected String getUnparameterizedInterfaceName() {
            return null;
        }

        @Override
        public Map<Integer, T> valuesWithinPeriod() {
            return null;
        }

        @Override
        public int periodDuration() {
            return 0;
        }

        @Override
        public int periodModuloOffset() {
            return 0;
        }

        @Override
        public void reset(long l) throws IllegalArgumentException {

        }

        @Override
        public T provide(long l) throws IllegalArgumentException {
            return null;
        }

        @Override
        public EntityUuid uuid() {
            return null;
        }

        @Override
        public void reportPause(long l) throws IllegalArgumentException {

        }

        @Override
        public void reportUnpause(long l) throws IllegalArgumentException {

        }

        @Override
        public Long pausedTimestamp() {
            return null;
        }

        @Override
        public Long mostRecentTimestamp() {
            return null;
        }
    }
}
