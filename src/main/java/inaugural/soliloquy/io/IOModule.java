package inaugural.soliloquy.io;

import soliloquy.specs.game.Module;

public class IOModule implements Module {
    @Override
    public <T> T provide(Class<T> aClass) throws IllegalArgumentException {
        return null;
    }

    @Override
    public <T> T provide(String s) throws IllegalArgumentException {
        return null;
    }
}
