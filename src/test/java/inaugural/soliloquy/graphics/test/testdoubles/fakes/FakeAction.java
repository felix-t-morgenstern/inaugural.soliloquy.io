package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.entities.Action;

import java.util.ArrayList;

public class FakeAction<T> implements Action<T> {
    public ArrayList<T> Inputs = new ArrayList<>();
    public int NumberOfTimesCalled;
    public String Id;

    public FakeAction() {

    }

    public FakeAction(String id) {
        Id = id;
    }

    @Override
    public void run(T t) throws IllegalArgumentException {
        Inputs.add(t);
        NumberOfTimesCalled++;
    }

    @Override
    public String id() throws IllegalStateException {
        return Id;
    }

    @Override
    public T archetype() {
        return null;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }
}
