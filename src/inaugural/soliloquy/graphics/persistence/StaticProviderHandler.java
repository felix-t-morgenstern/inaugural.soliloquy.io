package inaugural.soliloquy.graphics.persistence;

import com.google.gson.Gson;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.generic.AbstractHasOneGenericParam;
import inaugural.soliloquy.tools.generic.CanGetInterfaceName;
import inaugural.soliloquy.tools.persistence.AbstractTypeWithOneGenericParamHandler;
import soliloquy.specs.common.persistence.PersistentValuesHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.providers.StaticProvider;
import soliloquy.specs.graphics.renderables.providers.factories.StaticProviderFactory;

/** @noinspection rawtypes*/
public class StaticProviderHandler
        extends AbstractTypeWithOneGenericParamHandler<StaticProvider> {
    private final TypeHandler<EntityUuid> PERSISTENT_UUID_HANDLER;
    private final PersistentValuesHandler PERSISTENT_VALUES_HANDLER;
    private final StaticProviderFactory STATIC_PROVIDER_FACTORY;

    private static final CanGetInterfaceName CAN_GET_INTERFACE_NAME = new CanGetInterfaceName();
    private static final Gson GSON = new Gson();

    private final static StaticProviderArchetype ARCHETYPE = new StaticProviderArchetype();

    public StaticProviderHandler(TypeHandler<EntityUuid>
                                                   persistentUuidHandler,
                                 PersistentValuesHandler persistentValuesHandler,
                                 StaticProviderFactory staticProviderFactory) {
        super(ARCHETYPE, persistentValuesHandler, QualifiedStaticProviderArchetype::new);
        PERSISTENT_UUID_HANDLER = Check.ifNull(persistentUuidHandler, "persistentUuidHandler");
        PERSISTENT_VALUES_HANDLER = Check.ifNull(persistentValuesHandler,
                "persistentValuesHandler");
        STATIC_PROVIDER_FACTORY = Check.ifNull(staticProviderFactory, "staticProviderFactory");
    }

    @Override
    public StaticProvider read(String writtenValue) throws IllegalArgumentException {
        StaticProviderDTO staticProviderDTO = GSON.fromJson(writtenValue, StaticProviderDTO.class);
        EntityUuid uuid = PERSISTENT_UUID_HANDLER.read(staticProviderDTO.uuid);
        //noinspection rawtypes
        TypeHandler persistentValueTypeHandler = PERSISTENT_VALUES_HANDLER
                .getTypeHandler(staticProviderDTO.innerType);
        return STATIC_PROVIDER_FACTORY.make(uuid,
                persistentValueTypeHandler.read(staticProviderDTO.val),
                PERSISTENT_VALUES_HANDLER.generateArchetype(staticProviderDTO.innerType),
                staticProviderDTO.mostRecentTimestamp);
    }

    @Override
    public String write(StaticProvider staticProvider) {
        String innerType = CAN_GET_INTERFACE_NAME.getProperTypeName(staticProvider.getArchetype());
        //noinspection rawtypes
        TypeHandler persistentValueTypeHandler =
                PERSISTENT_VALUES_HANDLER.getTypeHandler(innerType);
        StaticProviderDTO staticProviderDTO = new StaticProviderDTO();
        staticProviderDTO.uuid = PERSISTENT_UUID_HANDLER.write(staticProvider.uuid());
        staticProviderDTO.innerType = innerType;
        //noinspection unchecked
        staticProviderDTO.val = persistentValueTypeHandler
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
