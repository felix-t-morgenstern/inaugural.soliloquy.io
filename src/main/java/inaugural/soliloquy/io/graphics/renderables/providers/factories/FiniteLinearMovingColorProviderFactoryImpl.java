package inaugural.soliloquy.io.graphics.renderables.providers.factories;

import inaugural.soliloquy.io.graphics.renderables.providers.FiniteLinearMovingColorProviderImpl;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.io.graphics.renderables.providers.FiniteLinearMovingColorProvider;
import soliloquy.specs.io.graphics.renderables.providers.factories.FiniteLinearMovingColorProviderFactory;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FiniteLinearMovingColorProviderFactoryImpl
        implements FiniteLinearMovingColorProviderFactory {
    private final TimestampValidator TIMESTAMP_VALIDATOR;

    public FiniteLinearMovingColorProviderFactoryImpl(TimestampValidator timestampValidator) {
        TIMESTAMP_VALIDATOR = Check.ifNull(timestampValidator, "timestampValidator");
    }

    @Override
    public FiniteLinearMovingColorProvider make(UUID uuid,
                                                Map<Long, Color> valuesAtTimestamps,
                                                List<Boolean> hueMovementIsClockwise,
                                                Long pausedTimestamp)
            throws IllegalArgumentException {
        return new FiniteLinearMovingColorProviderImpl(uuid, valuesAtTimestamps,
                hueMovementIsClockwise, pausedTimestamp, TIMESTAMP_VALIDATOR);
    }
}
