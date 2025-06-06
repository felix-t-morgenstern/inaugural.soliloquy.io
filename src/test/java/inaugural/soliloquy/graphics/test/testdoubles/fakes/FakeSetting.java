package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.infrastructure.ImmutableMap;
import soliloquy.specs.gamestate.entities.Setting;

public class FakeSetting<T> implements Setting<T> {
    private String id;
    private T value;

    public FakeSetting(String id, T value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(T t) throws IllegalArgumentException {
        value = t;
    }

    @Override
    public ImmutableMap<String, Object> controlParams() {
        return null;
    }

    @Override
    public String id() throws IllegalStateException {
        return id;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String s) {

    }
}
