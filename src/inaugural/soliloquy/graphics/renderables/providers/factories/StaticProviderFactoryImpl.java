package inaugural.soliloquy.graphics.renderables.providers.factories;

import inaugural.soliloquy.graphics.renderables.providers.StaticProviderImpl;
import soliloquy.specs.graphics.renderables.providers.StaticProvider;
import soliloquy.specs.graphics.renderables.providers.factories.StaticProviderFactory;

public class StaticProviderFactoryImpl implements StaticProviderFactory {
    @Override
    public <T> StaticProvider<T> make(T value, T archetype) throws IllegalArgumentException {
        return new StaticProviderImpl<>(value, archetype);
    }

    @Override
    public String getInterfaceName() {
        return StaticProviderFactory.class.getCanonicalName();
    }
}
