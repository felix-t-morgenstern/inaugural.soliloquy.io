package inaugural.soliloquy.io.graphics.renderables.providers.factories;

import inaugural.soliloquy.io.graphics.renderables.providers.LoopingLinearMovingColorProviderImpl;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.io.graphics.renderables.providers.LoopingLinearMovingColorProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.LoopingLinearMovingColorProviderFactory;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LoopingLinearMovingColorProviderFactoryImpl
        implements LoopingLinearMovingColorProviderFactory {
    private final TimestampValidator TIMESTAMP_VALIDATOR;

    public LoopingLinearMovingColorProviderFactoryImpl(TimestampValidator timestampValidator) {
        TIMESTAMP_VALIDATOR = Check.ifNull(timestampValidator, "timestampValidator");
    }

    @Override
    public LoopingLinearMovingColorProvider make(UUID uuid,
                                                 Map<Integer, Color> valuesWithinPeriod,
                                                 List<Boolean> hueMovementIsClockwise,
                                                 int periodDuration, int periodModuloOffset,
                                                 Long pausedTimestamp)
            throws IllegalArgumentException {
        return new LoopingLinearMovingColorProviderImpl(uuid, valuesWithinPeriod,
                hueMovementIsClockwise, periodDuration, periodModuloOffset, pausedTimestamp,
                TIMESTAMP_VALIDATOR);
    }
}
