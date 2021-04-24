package inaugural.soliloquy.graphics.renderables.providers.factories;

import inaugural.soliloquy.graphics.renderables.providers.StaticProvider;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.renderables.providers.factories.StaticProviderFactory;

public class StaticProviderFactoryImpl implements StaticProviderFactory {
    @Override
    public <T> ProviderAtTime<T> make(T value, T archetype) throws IllegalArgumentException {
        return new StaticProvider<>(value, archetype);
    }

    @Override
    public String getInterfaceName() {
        return StaticProviderFactory.class.getCanonicalName();
    }
}
