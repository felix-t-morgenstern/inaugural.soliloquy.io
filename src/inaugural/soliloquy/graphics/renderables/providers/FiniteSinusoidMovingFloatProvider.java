package inaugural.soliloquy.graphics.renderables.providers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.renderables.providers.FiniteSinusoidMovingProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FiniteSinusoidMovingFloatProvider extends AbstractFiniteLinearMovingProvider<Float>
        implements FiniteSinusoidMovingProvider<Float> {
    private final List<Float> TRANSITION_SHARPNESSES;

    /** @noinspection ConstantConditions */
    public FiniteSinusoidMovingFloatProvider(java.util.UUID uuid,
                                             Map<Long, Float> valuesAtTimes,
                                             List<Float> transitionSharpnesses,
                                             Long pausedTimestamp,
                                             Long mostRecentTimestamp) {
        super(uuid, valuesAtTimes, pausedTimestamp, mostRecentTimestamp);
        Check.ifNull(transitionSharpnesses, "transitionSharpnesses");
        if (transitionSharpnesses.size() != valuesAtTimes.size() - 1) {
            throw new IllegalArgumentException(
                    "FiniteSinusoidMovingFloatProvider: transitionSharpnesses must have number of" +
                            " entries equal to one fewer than valuesAtTimes (" +
                            valuesAtTimes.size() + ")");
        }
        if (transitionSharpnesses.stream().anyMatch(s -> s < 0f)) {
            throw new IllegalArgumentException(
                    "FiniteSinusoidMovingFloatProvider: Cannot have transition sharpness less " +
                            "than 0");
        }
        TRANSITION_SHARPNESSES = transitionSharpnesses;
    }

    @Override
    protected Float interpolate(Float value1, float weight1, Float value2, float weight2,
                                int transitionNumber) {
        float distanceBetweenValues = value2 - value1;
        double weightSine = (-Math.PI / 2f) + (Math.PI * weight2);
        double sineValue = Math.sin(weightSine);
        float sharpness = TRANSITION_SHARPNESSES.get(transitionNumber);
        double sharpenedSineValue;
        if (sharpness == 1f) {
            sharpenedSineValue = sineValue;
        }
        else {
            sharpenedSineValue =
                    (sineValue < 0f ? -1f : 1f) * Math.pow(Math.abs(sineValue), sharpness);
        }

        float percentToAdd = (float) ((sharpenedSineValue + 1f) / 2f);

        return value1 + (distanceBetweenValues * percentToAdd);
    }

    @Override
    public List<Float> transitionSharpnesses() {
        return new ArrayList<>(TRANSITION_SHARPNESSES);
    }

    @Override
    public Float getArchetype() {
        return 0f;
    }

    @Override
    public String getInterfaceName() {
        return FiniteSinusoidMovingProvider.class.getName() + "<" +
                CAN_GET_INTERFACE_NAME.getProperTypeName(getArchetype()) + ">";
    }
}
