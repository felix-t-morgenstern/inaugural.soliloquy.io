package inaugural.soliloquy.graphics.test.unit.rendering;

import inaugural.soliloquy.graphics.api.Settings;
import inaugural.soliloquy.graphics.rendering.PIDController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.gamestate.entities.Setting;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PIDControllerTests {
    private final double P = 0.25f;
    private final double I = 0.01f;
    private final double D = 0.4f;
    @SuppressWarnings("rawtypes")
    @Mock private Function<String, Setting> mockGetSetting;
    @Mock private Setting<Double> mockSetting_P;
    @Mock private Setting<Double> mockSetting_I;
    @Mock private Setting<Double> mockSetting_D;

    private PIDController pidController;

    @BeforeEach
    public void setUp() {
        when(mockSetting_P.getValue()).thenReturn(P);
        when(mockGetSetting.apply(Settings.PID_CONTROLLER_P_SETTING_ID)).thenReturn(mockSetting_P);

        when(mockSetting_I.getValue()).thenReturn(I);
        when(mockGetSetting.apply(Settings.PID_CONTROLLER_I_SETTING_ID)).thenReturn(mockSetting_I);

        when(mockSetting_D.getValue()).thenReturn(D);
        when(mockGetSetting.apply(Settings.PID_CONTROLLER_D_SETTING_ID)).thenReturn(mockSetting_D);

        pidController = new PIDController(mockGetSetting);
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> new PIDController(null));

        when(mockGetSetting.apply(Settings.PID_CONTROLLER_P_SETTING_ID)).thenReturn(null);
        assertThrows(IllegalArgumentException.class,
                () -> new PIDController(mockGetSetting));

        when(mockGetSetting.apply(Settings.PID_CONTROLLER_P_SETTING_ID)).thenReturn(mockSetting_P);
        when(mockSetting_P.getValue()).thenReturn(null);
        assertThrows(IllegalArgumentException.class,
                () -> new PIDController(mockGetSetting));
        when(mockSetting_P.getValue()).thenReturn(P);

        when(mockGetSetting.apply(Settings.PID_CONTROLLER_I_SETTING_ID)).thenReturn(mockSetting_I);
        when(mockSetting_I.getValue()).thenReturn(null);
        assertThrows(IllegalArgumentException.class,
                () -> new PIDController(mockGetSetting));
        when(mockSetting_I.getValue()).thenReturn(I);

        when(mockGetSetting.apply(Settings.PID_CONTROLLER_D_SETTING_ID)).thenReturn(mockSetting_D);
        when(mockSetting_D.getValue()).thenReturn(null);
        assertThrows(IllegalArgumentException.class,
                () -> new PIDController(mockGetSetting));
    }

    // NB: This class depends on an external library, so I am not testing its functionality in
    // detail
    @Test
    public void testBasicFunctionality() {
        double output;
        double actual = 0;
        double target = 100;

        double actual1 = 0;
        double actual5 = 0;
        double actual10 = 0;

        for (var i = 0; i < 10; i++) {
            output = pidController.getOutput(actual, target);
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
}
