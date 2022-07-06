package inaugural.soliloquy.graphics.persistence.renderables.providers;

import com.google.gson.Gson;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.generic.AbstractHasOneGenericParam;
import inaugural.soliloquy.tools.generic.CanGetInterfaceName;
import inaugural.soliloquy.tools.persistence.AbstractTypeWithOneGenericParamHandler;
import soliloquy.specs.common.persistence.PersistentValuesHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.graphics.renderables.providers.StaticProvider;
import soliloquy.specs.graphics.renderables.providers.factories.StaticProviderFactory;

import java.util.UUID;

/** @noinspection rawtypes*/
public class StaticProviderHandler
        extends AbstractTypeWithOneGenericParamHandler<StaticProvider> {
    private final TypeHandler<UUID> UUID_HANDLER;
    private final StaticProviderFactory STATIC_PROVIDER_FACTORY;

    private static final CanGetInterfaceName CAN_GET_INTERFACE_NAME = new CanGetInterfaceName();
    private static final Gson GSON = new Gson();

    private final static StaticProviderArchetype ARCHETYPE = new StaticProviderArchetype();

    @SuppressWarnings("ConstantConditions")
    public StaticProviderHandler(TypeHandler<UUID> uuidHandler,
                                 PersistentValuesHandler persistentValuesHandler,
                                 StaticProviderFactory staticProviderFactory) {
        super(ARCHETYPE, persistentValuesHandler, QualifiedStaticProviderArchetype::new);
        UUID_HANDLER = Check.ifNull(uuidHandler, "uuidHandler");
        STATIC_PROVIDER_FACTORY = Check.ifNull(staticProviderFactory, "staticProviderFactory");
    }

    @Override
    public StaticProvider read(String writtenValue) throws IllegalArgumentException {
        StaticProviderDTO dto = GSON.fromJson(
                Check.ifNullOrEmpty(writtenValue, "writtenValue"),
                StaticProviderDTO.class);
        UUID uuid = UUID_HANDLER.read(dto.uuid);
        //noinspection rawtypes
        TypeHandler typeHandler = PERSISTENT_VALUES_HANDLER.getTypeHandler(dto.innerType);
        return STATIC_PROVIDER_FACTORY.make(uuid,
                typeHandler.read(dto.val),
                PERSISTENT_VALUES_HANDLER.generateArchetype(dto.innerType),
                dto.mostRecentTimestamp);
    }

    @Override
    public String write(StaticProvider staticProvider) {
        String innerType = CAN_GET_INTERFACE_NAME.getProperTypeName(
                Check.ifNull(staticProvider, "staticProvider").getArchetype());
        //noinspection rawtypes
        TypeHandler typeHandler = PERSISTENT_VALUES_HANDLER.getTypeHandler(innerType);
        StaticProviderDTO staticProviderDTO = new StaticProviderDTO();
        staticProviderDTO.uuid = UUID_HANDLER.write(staticProvider.uuid());
        staticProviderDTO.innerType = innerType;
        //noinspection unchecked
        staticProviderDTO.val = typeHandler
                .write(staticProvider.provide(staticProvider.mostRecentTimestamp()));
        staticProviderDTO.mostRecentTimestamp = staticProvider.mostRecentTimestamp();
        return GSON.toJson(staticProviderDTO);
    }

    private static class StaticProviderDTO {
        String uuid;
        String innerType;
        String val;
        Long mostRecentTimestamp;
    }

    /** @noinspection rawtypes*/
    private static class StaticProviderArchetype implements StaticProvider {

        @Override
        public Object provide(long l) throws IllegalArgumentException {
            return null;
        }

        @Override
        public Object getArchetype() {
            return 0;
        }

        @Override
        public UUID uuid() {
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
            return StaticProvider.class.getCanonicalName();
        }
    }

    private static class QualifiedStaticProviderArchetype<T>
            extends AbstractHasOneGenericParam<T>
            implements StaticProvider<T> {
        private QualifiedStaticProviderArchetype(T archetype) {
            super(archetype);
        }

        @Override
        protected String getUnparameterizedInterfaceName() {
            return StaticProvider.class.getCanonicalName();
        }

        @Override
        public T provide(long l) throws IllegalArgumentException {
            return null;
        }

        @Override
        public UUID uuid() {
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
