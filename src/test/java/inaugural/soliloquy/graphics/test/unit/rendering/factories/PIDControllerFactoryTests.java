package inaugural.soliloquy.graphics.test.unit.rendering.factories;

import inaugural.soliloquy.graphics.api.Settings;
import inaugural.soliloquy.graphics.rendering.PIDController;
import inaugural.soliloquy.graphics.rendering.factories.PIDControllerFactory;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeSettingsRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.rendering.OutputController;
import soliloquy.specs.graphics.rendering.factories.OutputControllerFactory;

import static org.junit.jupiter.api.Assertions.*;

class PIDControllerFactoryTests {
    private final double P = 0.25f;
    private final double I = 0.01f;
    private final double D = 0.4f;
    private final FakeSettingsRepo SETTINGS_REPO = new FakeSettingsRepo();

    private OutputControllerFactory _pidControllerFactory;

    @BeforeEach
    void setUp() {
        SETTINGS_REPO.setSetting(Settings.PID_CONTROLLER_P_SETTING_ID, P);
        SETTINGS_REPO.setSetting(Settings.PID_CONTROLLER_I_SETTING_ID, I);
        SETTINGS_REPO.setSetting(Settings.PID_CONTROLLER_D_SETTING_ID, D);

        _pidControllerFactory = new PIDControllerFactory();
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> _pidControllerFactory.make(null));

        SETTINGS_REPO.removeItem(Settings.PID_CONTROLLER_P_SETTING_ID);
        assertThrows(IllegalArgumentException.class,
                () -> _pidControllerFactory.make(SETTINGS_REPO));
        SETTINGS_REPO.setSetting(Settings.PID_CONTROLLER_P_SETTING_ID, null);
        assertThrows(IllegalArgumentException.class,
                () -> _pidControllerFactory.make(SETTINGS_REPO));
        SETTINGS_REPO.setSetting(Settings.PID_CONTROLLER_P_SETTING_ID, P);

        SETTINGS_REPO.removeItem(Settings.PID_CONTROLLER_I_SETTING_ID);
        assertThrows(IllegalArgumentException.class,
                () -> _pidControllerFactory.make(SETTINGS_REPO));
        SETTINGS_REPO.setSetting(Settings.PID_CONTROLLER_I_SETTING_ID, null);
        assertThrows(IllegalArgumentException.class,
                () -> _pidControllerFactory.make(SETTINGS_REPO));
        SETTINGS_REPO.setSetting(Settings.PID_CONTROLLER_I_SETTING_ID, I);

        SETTINGS_REPO.removeItem(Settings.PID_CONTROLLER_D_SETTING_ID);
        assertThrows(IllegalArgumentException.class,
                () -> _pidControllerFactory.make(SETTINGS_REPO));
        SETTINGS_REPO.setSetting(Settings.PID_CONTROLLER_D_SETTING_ID, null);
        assertThrows(IllegalArgumentException.class,
                () -> _pidControllerFactory.make(SETTINGS_REPO));
        SETTINGS_REPO.setSetting(Settings.PID_CONTROLLER_D_SETTING_ID, D);
    }

    @Test
    void testMake() {
        OutputController outputController = _pidControllerFactory.make(SETTINGS_REPO);
        assertNotNull(outputController);
        assertTrue(outputController instanceof PIDController);
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(OutputControllerFactory.class.getCanonicalName(),
                _pidControllerFactory.getInterfaceName());
    }
}
