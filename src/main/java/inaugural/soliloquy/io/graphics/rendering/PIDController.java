package inaugural.soliloquy.io.graphics.rendering;

import com.stormbots.MiniPID;
import inaugural.soliloquy.io.api.Settings;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.gamestate.entities.Setting;
import soliloquy.specs.io.graphics.rendering.OutputController;

import java.util.function.Function;

public class PIDController implements OutputController {
    private final MiniPID PID_CONTROLLER;

    public PIDController(@SuppressWarnings("rawtypes") Function<String, Setting> getSetting) {
        Check.ifNull(getSetting, "getSetting");
        double p = getDoubleSettingValue(getSetting, Settings.PID_CONTROLLER_P_SETTING_ID);
        double i = getDoubleSettingValue(getSetting, Settings.PID_CONTROLLER_I_SETTING_ID);
        double d = getDoubleSettingValue(getSetting, Settings.PID_CONTROLLER_D_SETTING_ID);

        PID_CONTROLLER = new MiniPID(p, i, d);
    }

    private double getDoubleSettingValue(
            @SuppressWarnings("rawtypes") Function<String, Setting> getSetting, String settingId) {
        @SuppressWarnings("rawtypes") Setting setting = getSetting.apply(settingId);
        if (setting == null) {
            throw new IllegalArgumentException("PIDController: setting not found: " + settingId);
        }
        try {
            @SuppressWarnings("unchecked") Setting<Double> doubleSetting =
                    (Setting<Double>) setting;
            if (doubleSetting.getValue() == null) {
                throw new IllegalArgumentException("PIDController: setting is null: " + settingId);
            }
            return doubleSetting.getValue();
        }
        catch (ClassCastException e) {
            throw new IllegalArgumentException("PIDController: setting not a double: " + settingId);
        }
    }

    @Override
    public void reset() {
        PID_CONTROLLER.reset();
    }

    @Override
    public double getOutput(double actual, double target) {
        return PID_CONTROLLER.getOutput(actual, target);
    }
}
