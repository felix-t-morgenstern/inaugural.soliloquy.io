package inaugural.soliloquy.io.graphics.rendering.factories;

import inaugural.soliloquy.io.graphics.rendering.PIDController;
import soliloquy.specs.gamestate.entities.Setting;
import soliloquy.specs.io.graphics.rendering.OutputController;
import soliloquy.specs.io.graphics.rendering.factories.OutputControllerFactory;

import java.util.function.Function;

public class PIDControllerFactory implements OutputControllerFactory {
    @Override
    public OutputController make(Function<String, Setting> getSetting) throws IllegalArgumentException {
        return new PIDController(getSetting);
    }
}
