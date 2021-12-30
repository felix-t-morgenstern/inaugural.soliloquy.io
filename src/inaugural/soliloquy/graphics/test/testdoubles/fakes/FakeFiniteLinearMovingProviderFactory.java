package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.providers.FiniteLinearMovingProvider;
import soliloquy.specs.graphics.renderables.providers.factories.FiniteLinearMovingProviderFactory;

import java.util.ArrayList;
import java.util.Map;

public class FakeFiniteLinearMovingProviderFactory implements FiniteLinearMovingProviderFactory {
    public ArrayList<EntityUuid> InputUuids = new ArrayList<>();
    @SuppressWarnings("rawtypes")
    public ArrayList<Map> InputValuesAtTimestamps = new ArrayList<>();
    public ArrayList<Long> InputPausedTimestamps = new ArrayList<>();
    public ArrayList<Long> InputMostRecentTimestamps = new ArrayList<>();
    @SuppressWarnings("rawtypes")
    public ArrayList<FiniteLinearMovingProvider> Outputs = new ArrayList<>();

    @Override
    public <T> FiniteLinearMovingProvider<T> make(EntityUuid uuid, Map<Long, T> valuesAtTimestamps,
                                                  Long pausedTimestamp, Long mostRecentTimestamp)
            throws IllegalArgumentException {
        InputUuids.add(uuid);
        InputValuesAtTimestamps.add(valuesAtTimestamps);
        InputPausedTimestamps.add(pausedTimestamp);
        InputMostRecentTimestamps.add(mostRecentTimestamp);
        FakeFiniteLinearMovingProvider<T> output = new FakeFiniteLinearMovingProvider<>();
        Outputs.add(output);
        return output;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
