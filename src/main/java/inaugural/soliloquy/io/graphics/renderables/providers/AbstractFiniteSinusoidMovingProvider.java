package inaugural.soliloquy.io.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.io.graphics.renderables.providers.FiniteSinusoidMovingProvider;

import java.util.Map;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.arrayFloats;

abstract class AbstractFiniteSinusoidMovingProvider<T>
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
            throw new IllegalArgumentException(
                    className() + ": transitionSharpnesses must have number of" +
                            " entries equal to one fewer than valuesAtTimes (" +
                            valuesAtTimes.size() + ")");
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
            sharpenedSineValue =
                    (sineValue < 0f ? -1f : 1f) * Math.pow(Math.abs(sineValue), sharpness);
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
