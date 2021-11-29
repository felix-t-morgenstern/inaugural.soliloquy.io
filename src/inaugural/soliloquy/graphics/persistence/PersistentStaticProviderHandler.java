package inaugural.soliloquy.graphics.persistence;

import com.google.gson.Gson;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.generic.CanGetInterfaceName;
import soliloquy.specs.common.persistence.PersistentValueTypeHandler;
import soliloquy.specs.common.persistence.PersistentValuesHandler;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.providers.StaticProvider;
import soliloquy.specs.graphics.renderables.providers.factories.StaticProviderFactory;

public class PersistentStaticProviderHandler
        implements PersistentValueTypeHandler<StaticProvider<?>> {
    private final PersistentValueTypeHandler<EntityUuid> PERSISTENT_UUID_HANDLER;
    private final PersistentValuesHandler PERSISTENT_VALUES_HANDLER;
    private final StaticProviderFactory STATIC_PROVIDER_FACTORY;

    private static final CanGetInterfaceName CAN_GET_INTERFACE_NAME = new CanGetInterfaceName();
    private static final Gson GSON = new Gson();

    public PersistentStaticProviderHandler(PersistentValueTypeHandler<EntityUuid>
                                                   persistentUuidHandler,
                                           PersistentValuesHandler persistentValuesHandler,
                                           StaticProviderFactory staticProviderFactory) {
        PERSISTENT_UUID_HANDLER = Check.ifNull(persistentUuidHandler, "persistentUuidHandler");
        PERSISTENT_VALUES_HANDLER = Check.ifNull(persistentValuesHandler,
                "persistentValuesHandler");
        STATIC_PROVIDER_FACTORY = Check.ifNull(staticProviderFactory, "staticProviderFactory");
    }

    private final static StaticProviderArchetype ARCHETYPE = new StaticProviderArchetype();

    @Override
    public StaticProvider<?> read(String writtenValue) throws IllegalArgumentException {
        StaticProviderDTO staticProviderDTO = GSON.fromJson(writtenValue, StaticProviderDTO.class);
        EntityUuid uuid = PERSISTENT_UUID_HANDLER.read(staticProviderDTO.uuid);
        //noinspection rawtypes
        PersistentValueTypeHandler persistentValueTypeHandler = PERSISTENT_VALUES_HANDLER
                .getPersistentValueTypeHandler(staticProviderDTO.innerType);
        StaticProvider staticProvider = STATIC_PROVIDER_FACTORY.make(uuid,
                persistentValueTypeHandler.read(staticProviderDTO.val),
                PERSISTENT_VALUES_HANDLER.generateArchetype(staticProviderDTO.innerType),
                staticProviderDTO.mostRecentTimestamp);
        return staticProvider;
    }

    @Override
    public String write(StaticProvider<?> staticProvider) {
        String innerType = CAN_GET_INTERFACE_NAME.getProperTypeName(staticProvider.getArchetype());
        //noinspection rawtypes
        PersistentValueTypeHandler persistentValueTypeHandler =
                PERSISTENT_VALUES_HANDLER.getPersistentValueTypeHandler(innerType);
        StaticProviderDTO staticProviderDTO = new StaticProviderDTO();
        staticProviderDTO.uuid = PERSISTENT_UUID_HANDLER.write(staticProvider.uuid());
        staticProviderDTO.innerType = innerType;
        //noinspection unchecked
        staticProviderDTO.val = persistentValueTypeHandler
                .write(staticProvider.provide(staticProvider.mostRecentTimestamp()));
        staticProviderDTO.mostRecentTimestamp = staticProvider.mostRecentTimestamp();
        return GSON.toJson(staticProviderDTO);
    }

    @Override
    public StaticProvider<?> getArchetype() {
        return ARCHETYPE;
    }

    @Override
    public String getInterfaceName() {
        return PersistentValueTypeHandler.class.getCanonicalName() + "<" +
                getArchetype().getInterfaceName() + ">";
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

        @Override
        public String getInterfaceName() {
            return StaticProvider.class.getCanonicalName();
        }
    }
}
