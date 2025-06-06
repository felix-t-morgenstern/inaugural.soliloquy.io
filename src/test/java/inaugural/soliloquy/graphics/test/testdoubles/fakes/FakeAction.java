package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.entities.Action;

import java.util.List;

import static inaugural.soliloquy.tools.collections.Collections.listOf;


public class FakeAction<T> implements Action<T> {
    public List<T> Inputs = listOf();
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
}
