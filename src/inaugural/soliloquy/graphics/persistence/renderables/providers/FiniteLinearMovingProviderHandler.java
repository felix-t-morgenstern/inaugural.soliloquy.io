package inaugural.soliloquy.graphics.persistence.renderables.providers;

import com.google.gson.Gson;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.generic.AbstractHasOneGenericParam;
import inaugural.soliloquy.tools.generic.CanGetInterfaceName;
import inaugural.soliloquy.tools.persistence.AbstractTypeWithOneGenericParamHandler;
import soliloquy.specs.common.persistence.PersistentValuesHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.providers.FiniteLinearMovingProvider;
import soliloquy.specs.graphics.renderables.providers.factories.FiniteLinearMovingProviderFactory;

import java.util.HashMap;
import java.util.Map;

/** @noinspection rawtypes*/
public class FiniteLinearMovingProviderHandler
        extends AbstractTypeWithOneGenericParamHandler<FiniteLinearMovingProvider> {
    private final TypeHandler<EntityUuid> UUID_HANDLER;
    private final FiniteLinearMovingProviderFactory FINITE_LINEAR_MOVING_PROVIDER_FACTORY;

    private static final CanGetInterfaceName CAN_GET_INTERFACE_NAME = new CanGetInterfaceName();
    private static final Gson GSON = new Gson();

    private final static FiniteLinearMovingProviderArchetype ARCHETYPE =
            new FiniteLinearMovingProviderArchetype();

    public FiniteLinearMovingProviderHandler(TypeHandler<EntityUuid> uuidHandler,
                                             PersistentValuesHandler
                                                               persistentValuesHandler,
                                             FiniteLinearMovingProviderFactory
                                                               finiteLinearMovingProviderFactory) {
        super(ARCHETYPE, persistentValuesHandler,
                QualifiedFiniteLinearMovingProviderArchetype::new);
        UUID_HANDLER = Check.ifNull(uuidHandler, "uuidHandler");
        FINITE_LINEAR_MOVING_PROVIDER_FACTORY = Check.ifNull(finiteLinearMovingProviderFactory,
                "finiteLinearMovingProviderFactory");
    }

    @Override
    public FiniteLinearMovingProvider read(String writtenValue) throws IllegalArgumentException {
        FiniteLinearMovingProviderDTO dto =
                GSON.fromJson(writtenValue, FiniteLinearMovingProviderDTO.class);
        EntityUuid uuid = UUID_HANDLER.read(dto.uuid);
        TypeHandler typeHandler = PERSISTENT_VALUES_HANDLER.getTypeHandler(dto.valueType);
        Map<Long, Object> valuesAtTimestamps = new HashMap<>();
        for (FiniteLinearMovingProviderValueAtTimestampDTO valueDto : dto.values) {
            valuesAtTimestamps.put(valueDto.timestamp, typeHandler.read(valueDto.value));
        }
        return FINITE_LINEAR_MOVING_PROVIDER_FACTORY
                .make(uuid, valuesAtTimestamps, dto.pausedTimestamp, dto.mostRecentTimestamp);
    }

    @Override
    public String write(FiniteLinearMovingProvider finiteLinearMovingProvider) {
        FiniteLinearMovingProviderDTO dto = new FiniteLinearMovingProviderDTO();

        dto.uuid = UUID_HANDLER.write(finiteLinearMovingProvider.uuid());

        String valueType = dto.valueType = CAN_GET_INTERFACE_NAME
                .getProperTypeName(finiteLinearMovingProvider.getArchetype());

        TypeHandler typeHandler = PERSISTENT_VALUES_HANDLER.getTypeHandler(valueType);
        //noinspection unchecked
        Map<Long, Object> valuesAtTimestamps =
                finiteLinearMovingProvider.valuesAtTimestampsRepresentation();
        int valuesSize = valuesAtTimestamps.size();
        dto.values = new FiniteLinearMovingProviderValueAtTimestampDTO[valuesSize];
        int index = 0;
        for(Map.Entry<Long, Object> valueAtTimestamp : valuesAtTimestamps.entrySet()) {
            FiniteLinearMovingProviderValueAtTimestampDTO valueDto =
                    new FiniteLinearMovingProviderValueAtTimestampDTO();

            valueDto.timestamp = valueAtTimestamp.getKey();
            //noinspection unchecked
            valueDto.value = typeHandler.write(valueAtTimestamp.getValue());
            dto.values[index++] = valueDto;
        }

        dto.pausedTimestamp = finiteLinearMovingProvider.pausedTimestamp();

        dto.mostRecentTimestamp = finiteLinearMovingProvider.mostRecentTimestamp();

        return GSON.toJson(dto);
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

    /** @noinspection rawtypes*/
    private static class FiniteLinearMovingProviderArchetype implements FiniteLinearMovingProvider {

        @Override
        public String getInterfaceName() {
            return FiniteLinearMovingProvider.class.getCanonicalName();
        }

        @Override
        public Map valuesAtTimestampsRepresentation() {
            return null;
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
    }

    private static class QualifiedFiniteLinearMovingProviderArchetype<T>
            extends AbstractHasOneGenericParam<T>
            implements FiniteLinearMovingProvider<T> {
        private QualifiedFiniteLinearMovingProviderArchetype(T archetype) {
            super(archetype);
        }

        @Override
        protected String getUnparameterizedInterfaceName() {
            return FiniteLinearMovingProvider.class.getCanonicalName();
        }

        @Override
        public Map<Long, T> valuesAtTimestampsRepresentation() {
            return null;
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
