package inaugural.soliloquy.graphics.test.unit.rendering;

import inaugural.soliloquy.graphics.api.Settings;
import inaugural.soliloquy.graphics.rendering.PIDController;
import inaugural.soliloquy.graphics.test.testdoubles.fakes.FakeSettingsRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soliloquy.specs.graphics.rendering.OutputController;

import static org.junit.jupiter.api.Assertions.*;

class PIDControllerTests {
    private final double P = 0.25f;
    private final double I = 0.01f;
    private final double D = 0.4f;
    private final FakeSettingsRepo SETTINGS_REPO = new FakeSettingsRepo();

    private PIDController _pidController;

    @BeforeEach
    void setUp() {
        SETTINGS_REPO.setSetting(Settings.PID_CONTROLLER_P_SETTING_ID, P);
        SETTINGS_REPO.setSetting(Settings.PID_CONTROLLER_I_SETTING_ID, I);
        SETTINGS_REPO.setSetting(Settings.PID_CONTROLLER_D_SETTING_ID, D);

        _pidController = new PIDController(SETTINGS_REPO);
    }

    @Test
    void testConstructorWithInvalidParams() {
        assertThrows(IllegalArgumentException.class, () -> new PIDController(null));

        SETTINGS_REPO.removeItem(Settings.PID_CONTROLLER_P_SETTING_ID);
        assertThrows(IllegalArgumentException.class, () -> new PIDController(SETTINGS_REPO));
        SETTINGS_REPO.setSetting(Settings.PID_CONTROLLER_P_SETTING_ID, null);
        assertThrows(IllegalArgumentException.class, () -> new PIDController(SETTINGS_REPO));
        SETTINGS_REPO.setSetting(Settings.PID_CONTROLLER_P_SETTING_ID, P);

        SETTINGS_REPO.removeItem(Settings.PID_CONTROLLER_I_SETTING_ID);
        assertThrows(IllegalArgumentException.class, () -> new PIDController(SETTINGS_REPO));
        SETTINGS_REPO.setSetting(Settings.PID_CONTROLLER_I_SETTING_ID, null);
        assertThrows(IllegalArgumentException.class, () -> new PIDController(SETTINGS_REPO));
        SETTINGS_REPO.setSetting(Settings.PID_CONTROLLER_I_SETTING_ID, I);

        SETTINGS_REPO.removeItem(Settings.PID_CONTROLLER_D_SETTING_ID);
        assertThrows(IllegalArgumentException.class, () -> new PIDController(SETTINGS_REPO));
        SETTINGS_REPO.setSetting(Settings.PID_CONTROLLER_D_SETTING_ID, null);
        assertThrows(IllegalArgumentException.class, () -> new PIDController(SETTINGS_REPO));
        SETTINGS_REPO.setSetting(Settings.PID_CONTROLLER_D_SETTING_ID, D);
    }

    // NB: This class depends on an external library, so I am not testing its functionality in
    // detail
    @Test
    void testBasicFunctionality() {
        double output;
        double actual = 0;
        double target = 100;

        double actual1 = 0;
        double actual5 = 0;
        double actual10 = 0;

        for (int i = 0; i < 10; i++) {
            output = _pidController.getOutput(actual, target);
            actual = actual + output;

            if (i == 0) {
                actual1 = actual;
            }
            if (i == 4) {
                actual5 = actual;
            }
            if (i == 9) {
                actual10 = actual;
            }
        }

        assertTrue(Math.abs(target - actual1) > Math.abs(target - actual5));
        assertTrue(Math.abs(target - actual5) > Math.abs(target - actual10));
    }

    @Test
    void testGetInterfaceName() {
        assertEquals(OutputController.class.getCanonicalName(), _pidController.getInterfaceName());
    }
}
