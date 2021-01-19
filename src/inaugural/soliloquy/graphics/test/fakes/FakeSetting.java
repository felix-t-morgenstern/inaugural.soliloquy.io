package inaugural.soliloquy.graphics.test.fakes;

import soliloquy.specs.common.infrastructure.Setting;
import soliloquy.specs.common.infrastructure.VariableCache;

public class FakeSetting<T> implements Setting<T> {
    private String _id;
    private T _value;

    public FakeSetting(String id, T value) {
        _id = id;
        _value = value;
    }

    @Override
    public T getValue() {
        return _value;
    }

    @Override
    public void setValue(T t) throws IllegalArgumentException {
        _value = t;
    }

    @Override
    public VariableCache controlParams() {
        return null;
    }

    @Override
    public String id() throws IllegalStateException {
        return _id;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String s) {

    }

    @Override
    public T getArchetype() {
        return null;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
