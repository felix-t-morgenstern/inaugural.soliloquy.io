package inaugural.soliloquy.graphics.renderables.providers.factories;

import inaugural.soliloquy.graphics.renderables.providers.AnimatedMouseCursorProviderImpl;
import soliloquy.specs.graphics.renderables.providers.AnimatedMouseCursorProvider;
import soliloquy.specs.graphics.renderables.providers.factories.AnimatedMouseCursorProviderFactory;

import java.util.Map;

public class AnimatedMouseCursorProviderFactoryImpl implements AnimatedMouseCursorProviderFactory {
    @Override
    public AnimatedMouseCursorProvider make(String id, Map<Long, Long> cursorsAtMs, int msDuration,
                                            int periodModuloOffset, Long pausedTimestamp,
                                            Long mostRecentTimestamp)
            throws IllegalArgumentException {
        return new AnimatedMouseCursorProviderImpl(id, cursorsAtMs, msDuration, periodModuloOffset,
                pausedTimestamp, mostRecentTimestamp);
    }

    @Override
    public String getInterfaceName() {
        return AnimatedMouseCursorProviderFactory.class.getCanonicalName();
    }
}
