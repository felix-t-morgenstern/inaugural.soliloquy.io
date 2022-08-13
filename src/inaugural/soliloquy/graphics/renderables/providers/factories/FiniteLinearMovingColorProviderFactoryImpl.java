package inaugural.soliloquy.graphics.renderables.providers.factories;

import inaugural.soliloquy.graphics.renderables.providers.FiniteLinearMovingColorProviderImpl;
import soliloquy.specs.graphics.renderables.providers.FiniteLinearMovingColorProvider;
import soliloquy.specs.graphics.renderables.providers.factories.FiniteLinearMovingColorProviderFactory;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FiniteLinearMovingColorProviderFactoryImpl
        implements FiniteLinearMovingColorProviderFactory {
    @Override
    public FiniteLinearMovingColorProvider make(UUID uuid,
                                                Map<Long, Color> valuesAtTimestamps,
                                                List<Boolean> hueMovementIsClockwise,
                                                Long pausedTimestamp, Long mostRecentTimestamp)
            throws IllegalArgumentException {
        return new FiniteLinearMovingColorProviderImpl(uuid, valuesAtTimestamps,
                hueMovementIsClockwise, pausedTimestamp, mostRecentTimestamp);
    }

    @Override
    public String getInterfaceName() {
        return FiniteLinearMovingColorProviderFactoryImpl.class.getCanonicalName();
    }
}
