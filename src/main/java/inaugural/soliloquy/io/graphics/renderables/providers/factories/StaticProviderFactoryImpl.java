package inaugural.soliloquy.io.graphics.renderables.providers.factories;

import inaugural.soliloquy.io.graphics.renderables.providers.StaticProviderImpl;
import soliloquy.specs.io.graphics.renderables.providers.StaticProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.StaticProviderFactory;

import java.util.UUID;

public class StaticProviderFactoryImpl implements StaticProviderFactory {
    @Override
    public <T> StaticProvider<T> make(UUID id, T value, Long mostRecentTimestamp)
            throws IllegalArgumentException {
        return new StaticProviderImpl<>(id, value, mostRecentTimestamp);
    }
}
