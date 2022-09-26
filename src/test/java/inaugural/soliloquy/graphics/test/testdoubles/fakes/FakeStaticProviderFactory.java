package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.renderables.providers.StaticProvider;
import soliloquy.specs.graphics.renderables.providers.factories.StaticProviderFactory;

import java.util.ArrayList;
import java.util.UUID;

public class FakeStaticProviderFactory implements StaticProviderFactory {
    public ArrayList<Object> Inputs = new ArrayList<>();
    /** @noinspection rawtypes */
    public ArrayList<StaticProvider> Outputs = new ArrayList<>();

    @Override
    public <T> StaticProvider<T> make(UUID uuid, T value, T archetype,
                                      Long mostRecentTimestamp)
            throws IllegalArgumentException {
        Inputs.add(value);
        FakeStaticProvider<T> output = new FakeStaticProvider<>(value, uuid);
        output.MostRecentTimestamp = mostRecentTimestamp;
        Outputs.add(output);
        return output;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
