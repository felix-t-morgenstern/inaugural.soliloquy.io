package inaugural.soliloquy.io.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.io.graphics.renderables.providers.FiniteSinusoidMovingProvider;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static inaugural.soliloquy.tools.collections.Collections.listOf;

abstract class AbstractFiniteSinusoidMovingProvider<T>
        extends AbstractFiniteLinearMovingProvider<T>
        implements FiniteSinusoidMovingProvider<T> {
    private final List<Float> TRANSITION_SHARPNESSES;

    protected AbstractFiniteSinusoidMovingProvider(UUID uuid,
                                                   Map<Long, T> valuesAtTimes,
                                                   List<Float> transitionSharpnesses,
                                                   Long pausedTimestamp,
                                                   TimestampValidator timestampValidator) {
        super(uuid, valuesAtTimes, pausedTimestamp, timestampValidator);
        Check.ifNull(transitionSharpnesses, "transitionSharpnesses");
        if (transitionSharpnesses.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException(
                    className() + ": cannot have null value in transitionSharpnesses");
        }
        if (transitionSharpnesses.size() != valuesAtTimes.size() - 1) {
            throw new IllegalArgumentException(
                    className() + ": transitionSharpnesses must have number of" +
                            " entries equal to one fewer than valuesAtTimes (" +
                            valuesAtTimes.size() + ")");
        }
        if (transitionSharpnesses.stream().anyMatch(s -> s < 0f)) {
            throw new IllegalArgumentException(
                    className() + ": Cannot have transition sharpness less " +
                            "than 0");
        }
        TRANSITION_SHARPNESSES = transitionSharpnesses;
    }

    @Override
    protected T interpolate(T value1, float weight1, T value2, float weight2,
                            int transitionNumber) {
        var weightSine = (-Math.PI / 2f) + (Math.PI * weight2);
        var sineValue = Math.sin(weightSine);
        float sharpness = TRANSITION_SHARPNESSES.get(transitionNumber);
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
    public List<Float> transitionSharpnesses() {
        return listOf(TRANSITION_SHARPNESSES);
    }

    protected abstract String className();
}
