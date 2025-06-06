package inaugural.soliloquy.graphics.renderables.providers.factories;

import inaugural.soliloquy.graphics.renderables.providers.ProgressiveStringProvider;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.renderables.providers.factories.ProgressiveStringProviderFactory;

import java.util.UUID;

public class ProgressiveStringProviderFactoryImpl implements ProgressiveStringProviderFactory {
    @Override
    public ProviderAtTime<String> make(UUID uuid, String string, long startTimestamp,
                                       long timeToComplete, Long pausedTimestamp,
                                       Long mostRecentTimestamp) throws IllegalArgumentException {
        return new ProgressiveStringProvider(uuid, string, startTimestamp, timeToComplete,
                pausedTimestamp, mostRecentTimestamp);
    }
}
