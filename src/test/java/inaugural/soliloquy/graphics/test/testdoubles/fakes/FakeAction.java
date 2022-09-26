package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.entities.Action;
import soliloquy.specs.game.Game;
import soliloquy.specs.logger.Logger;

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
    public Game game() {
        return null;
    }

    @Override
    public Logger logger() {
        return null;
    }

    @Override
    public String id() throws IllegalStateException {
        return Id;
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
