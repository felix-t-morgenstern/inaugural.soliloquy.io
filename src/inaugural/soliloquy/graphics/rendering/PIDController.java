package inaugural.soliloquy.graphics.rendering;

import com.stormbots.MiniPID;
import inaugural.soliloquy.graphics.api.Settings;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.infrastructure.Setting;
import soliloquy.specs.common.infrastructure.SettingsRepo;
import soliloquy.specs.graphics.rendering.OutputController;

public class PIDController implements OutputController {
    private MiniPID _pidController;

    public PIDController(SettingsRepo settingsRepo) {
        Check.ifNull(settingsRepo, "settingsRepo");
        double p = getDoubleSettingValue(settingsRepo, Settings.PID_CONTROLLER_P_SETTING_ID);
        double i = getDoubleSettingValue(settingsRepo, Settings.PID_CONTROLLER_I_SETTING_ID);
        double d = getDoubleSettingValue(settingsRepo, Settings.PID_CONTROLLER_D_SETTING_ID);

        _pidController = new MiniPID(p, i, d);
    }

    private double getDoubleSettingValue(SettingsRepo settingsRepo, String settingId) {
        @SuppressWarnings("rawtypes") Setting setting = settingsRepo.getSetting(settingId);
        if (setting == null) {
            throw new IllegalArgumentException("PIDController: setting not found: " + settingId);
        }
        try {
            @SuppressWarnings("unchecked") Setting<Double> doubleSetting = (Setting<Double>)setting;
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
        _pidController.reset();
    }

    @Override
    public double getOutput(double actual, double target) {
        return _pidController.getOutput(actual, target);
    }

    @Override
    public String getInterfaceName() {
        return OutputController.class.getCanonicalName();
    }
}
