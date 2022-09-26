package inaugural.soliloquy.graphics.rendering.factories;

import inaugural.soliloquy.graphics.rendering.PIDController;
import soliloquy.specs.common.infrastructure.SettingsRepo;
import soliloquy.specs.graphics.rendering.OutputController;
import soliloquy.specs.graphics.rendering.factories.OutputControllerFactory;

public class PIDControllerFactory implements OutputControllerFactory {
    @Override
    public OutputController make(SettingsRepo settingsRepo) throws IllegalArgumentException {
        return new PIDController(settingsRepo);
    }

    @Override
    public String getInterfaceName() {
        return OutputControllerFactory.class.getCanonicalName();
    }
}
