package inaugural.soliloquy.io.test.unit.graphics.rendering.factories;

import inaugural.soliloquy.io.api.Settings;
import inaugural.soliloquy.io.graphics.rendering.PIDController;
import inaugural.soliloquy.io.graphics.rendering.factories.PIDControllerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soliloquy.specs.gamestate.entities.Setting;
import soliloquy.specs.io.graphics.rendering.factories.OutputControllerFactory;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PIDControllerFactoryTests {
    private final double P = 0.25f;
    private final double I = 0.01f;
    private final double D = 0.4f;
    @SuppressWarnings("rawtypes")
    @Mock private Function<String, Setting> mockGetSetting;
    @Mock private Setting<Double> mockSetting_P;
    @Mock private Setting<Double> mockSetting_I;
    @Mock private Setting<Double> mockSetting_D;

    private OutputControllerFactory pidControllerFactory;

    @BeforeEach
    public void setUp() {
        when(mockSetting_P.getValue()).thenReturn(P);
        when(mockGetSetting.apply(Settings.PID_CONTROLLER_P_SETTING_ID)).thenReturn(
                mockSetting_P);

        when(mockSetting_I.getValue()).thenReturn(I);
        when(mockGetSetting.apply(Settings.PID_CONTROLLER_I_SETTING_ID)).thenReturn(
                mockSetting_I);

        when(mockSetting_D.getValue()).thenReturn(D);
        when(mockGetSetting.apply(Settings.PID_CONTROLLER_D_SETTING_ID)).thenReturn(
                mockSetting_D);

        pidControllerFactory = new PIDControllerFactory();
    }

    @Test
    public void testConstructorWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class, () -> pidControllerFactory.make(null));

        when(mockGetSetting.apply(Settings.PID_CONTROLLER_P_SETTING_ID)).thenReturn(null);
        assertThrows(IllegalArgumentException.class,
                () -> pidControllerFactory.make(mockGetSetting));

        when(mockGetSetting.apply(Settings.PID_CONTROLLER_P_SETTING_ID)).thenReturn(mockSetting_P);
        when(mockSetting_P.getValue()).thenReturn(null);
        assertThrows(IllegalArgumentException.class,
                () -> pidControllerFactory.make(mockGetSetting));
        when(mockSetting_P.getValue()).thenReturn(P);

        when(mockGetSetting.apply(Settings.PID_CONTROLLER_I_SETTING_ID)).thenReturn(mockSetting_I);
        when(mockSetting_I.getValue()).thenReturn(null);
        assertThrows(IllegalArgumentException.class,
                () -> pidControllerFactory.make(mockGetSetting));
        when(mockSetting_I.getValue()).thenReturn(I);

        when(mockGetSetting.apply(Settings.PID_CONTROLLER_D_SETTING_ID)).thenReturn(mockSetting_D);
        when(mockSetting_D.getValue()).thenReturn(null);
        assertThrows(IllegalArgumentException.class,
                () -> pidControllerFactory.make(mockGetSetting));
    }

    @Test
    public void testMake() {
        var outputController = pidControllerFactory.make(mockGetSetting);

        assertNotNull(outputController);
        assertInstanceOf(PIDController.class, outputController);
    }
}
