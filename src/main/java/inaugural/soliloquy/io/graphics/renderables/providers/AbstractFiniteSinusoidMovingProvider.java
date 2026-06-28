package inaugural.soliloquy.io.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.io.graphics.renderables.providers.FiniteSinusoidMovingProvider;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.arrayFloats;

public abstract class AbstractFiniteSinusoidMovingProvider<T>
        extends AbstractFiniteLinearMovingProvider<T>
        implements FiniteSinusoidMovingProvider<T> {
    private final float[] TRANSITION_SHARPNESSES;

    protected AbstractFiniteSinusoidMovingProvider(UUID uuid,
                                                   Map<Long, T> valuesAtTimes,
                                                   float[] transitionSharpnesses,
                                                   Long pausedTimestamp,
                                                   TimestampValidator timestampValidator) {
        super(uuid, valuesAtTimes, pausedTimestamp, timestampValidator);
        Check.ifNull(transitionSharpnesses, "transitionSharpnesses");
        if (transitionSharpnesses.length != valuesAtTimes.size() - 1) {
            throw new IllegalArgumentException(String.format(
                    "%s: transitionSharpnesses (size = %s) must have number of entries equal to " +
                            "one fewer than valuesAtTimes (size = %s)",
                    className(), transitionSharpnesses.length, valuesAtTimes.size()));
        }
        for (var transitionSharpness : transitionSharpnesses) {
            if (transitionSharpness < 0f) {
                throw new IllegalArgumentException(
                        className() + ": Cannot have transition sharpness less than 0");
            }
        }
        TRANSITION_SHARPNESSES = transitionSharpnesses;
    }

    @Override
    protected T interpolate(T value1, float weight1, T value2, float weight2,
                            int transitionNumber) {
        var weightSine = (-Math.PI / 2f) + (Math.PI * weight2);
        var sineValue = Math.sin(weightSine);
        float sharpness = TRANSITION_SHARPNESSES[transitionNumber];
        double sharpenedSineValue;
        if (sharpness == 1f) {
            sharpenedSineValue = sineValue;
        }
        else {
            var distFromTarget = (sineValue > 0 ? 1 : -1) - sineValue;
            sharpenedSineValue = sineValue + (distFromTarget * sharpness);
        }

        var sineAdjustedWeight2 = (float) ((sharpenedSineValue + 1f) / 2f);

        return interpolateFromSineWeights(value1, value2, sineAdjustedWeight2);
    }

    protected abstract T interpolateFromSineWeights(T value1, T value2, float value2PercentToAdd);

    @Override
    public float[] transitionSharpnesses() {
        return arrayFloats(TRANSITION_SHARPNESSES);
    }

    protected abstract String className();
}
