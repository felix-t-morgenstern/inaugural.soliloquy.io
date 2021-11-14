package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.providers.StaticProvider;
import soliloquy.specs.graphics.renderables.providers.factories.StaticProviderFactory;

import java.util.ArrayList;

public class FakeStaticProviderFactory implements StaticProviderFactory {
    public ArrayList<Object> Inputs = new ArrayList<>();
    /** @noinspection rawtypes*/
    public ArrayList<StaticProvider> Outputs = new ArrayList<>();

    @Override
    public <T> StaticProvider<T> make(EntityUuid uuid, T value, T archetype,
                                      Long mostRecentTimestamp)
            throws IllegalArgumentException {
        Inputs.add(value);
        StaticProvider<T> output = new FakeStaticProviderAtTime<>(value, uuid);
        Outputs.add(output);
        return output;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
