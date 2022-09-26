package inaugural.soliloquy.graphics.renderables.providers.factories;

import inaugural.soliloquy.graphics.renderables.providers.StaticProviderImpl;
import soliloquy.specs.graphics.renderables.providers.StaticProvider;
import soliloquy.specs.graphics.renderables.providers.factories.StaticProviderFactory;

import java.util.UUID;

public class StaticProviderFactoryImpl implements StaticProviderFactory {
    @Override
    public <T> StaticProvider<T> make(UUID id, T value, T archetype,
                                      Long mostRecentTimestamp)
            throws IllegalArgumentException {
        return new StaticProviderImpl<>(id, value, archetype, mostRecentTimestamp);
    }

    @Override
    public String getInterfaceName() {
        return StaticProviderFactory.class.getCanonicalName();
    }
}
