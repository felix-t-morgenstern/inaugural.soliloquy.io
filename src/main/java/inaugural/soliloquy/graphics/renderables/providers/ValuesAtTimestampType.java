package inaugural.soliloquy.graphics.renderables.providers;

import java.util.Map;
import java.util.Objects;

public class ValuesAtTimestampType {
    public static <T> String get(Map<?, T> valuesAtTimestamps) {
        var firstNonNullValue = valuesAtTimestamps.values().stream().filter(Objects::nonNull).findFirst();
        if (firstNonNullValue.isEmpty()) {
            throw new IllegalArgumentException("FiniteLinearMovingProviderFactoryImpl.make: valuesAtTimestamps cannot all be null");
        }
        return firstNonNullValue.get().getClass().getCanonicalName();
    }
}
