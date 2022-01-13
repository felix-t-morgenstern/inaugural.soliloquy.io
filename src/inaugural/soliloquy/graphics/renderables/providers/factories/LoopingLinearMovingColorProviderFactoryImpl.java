package inaugural.soliloquy.graphics.renderables.providers.factories;

import inaugural.soliloquy.graphics.renderables.providers.LoopingLinearMovingColorProviderImpl;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.providers.LoopingLinearMovingColorProvider;
import soliloquy.specs.graphics.renderables.providers.factories.LoopingLinearMovingColorProviderFactory;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class LoopingLinearMovingColorProviderFactoryImpl
        implements LoopingLinearMovingColorProviderFactory {
    @Override
    public LoopingLinearMovingColorProvider make(EntityUuid uuid,
                                                 Map<Integer, Color> valuesWithinPeriod,
                                                 List<Boolean> hueMovementIsClockwise,
                                                 int periodDuration, int periodModuloOffset,
                                                 Long pausedTimestamp, Long mostRecentTimestamp)
            throws IllegalArgumentException {
        return new LoopingLinearMovingColorProviderImpl(uuid, valuesWithinPeriod,
                hueMovementIsClockwise, periodDuration, periodModuloOffset, pausedTimestamp,
                mostRecentTimestamp);
    }

    @Override
    public String getInterfaceName() {
        return LoopingLinearMovingColorProviderFactory.class.getCanonicalName();
    }
}
