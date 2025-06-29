package inaugural.soliloquy.io.graphics.renderables.providers.factories;

import inaugural.soliloquy.io.graphics.renderables.providers.LoopingLinearMovingColorProviderImpl;
import soliloquy.specs.io.graphics.renderables.providers.LoopingLinearMovingColorProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.LoopingLinearMovingColorProviderFactory;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LoopingLinearMovingColorProviderFactoryImpl
        implements LoopingLinearMovingColorProviderFactory {
    @Override
    public LoopingLinearMovingColorProvider make(UUID uuid,
                                                 Map<Integer, Color> valuesWithinPeriod,
                                                 List<Boolean> hueMovementIsClockwise,
                                                 int periodDuration, int periodModuloOffset,
                                                 Long pausedTimestamp, Long mostRecentTimestamp)
            throws IllegalArgumentException {
        return new LoopingLinearMovingColorProviderImpl(uuid, valuesWithinPeriod,
                hueMovementIsClockwise, periodDuration, periodModuloOffset, pausedTimestamp,
                mostRecentTimestamp);
    }
}
