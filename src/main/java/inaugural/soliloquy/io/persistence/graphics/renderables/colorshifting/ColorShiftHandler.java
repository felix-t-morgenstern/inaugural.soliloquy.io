package inaugural.soliloquy.io.persistence.graphics.renderables.colorshifting;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.persistence.AbstractTypeHandler;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.io.graphics.renderables.colorshifting.*;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import static soliloquy.specs.io.graphics.renderables.colorshifting.BrightnessShift.brightnessShift;
import static soliloquy.specs.io.graphics.renderables.colorshifting.ColorComponentIntensityShift.colorComponentShift;
import static soliloquy.specs.io.graphics.renderables.colorshifting.ColorRotationShift.rotationShift;

public class ColorShiftHandler extends AbstractTypeHandler<ColorShift> {
    private final String TYPE_BRIGHTNESS = "brightness";
    private final String TYPE_ROTATION = "rotation";
    private final String TYPE_COMPONENT_SHIFT = "compShift";

    @SuppressWarnings("rawtypes") private final TypeHandler<ProviderAtTime> PROVIDER_HANDLER;

    public ColorShiftHandler(
            @SuppressWarnings("rawtypes") TypeHandler<ProviderAtTime> providerHandler) {
        PROVIDER_HANDLER = Check.ifNull(providerHandler, "providerHandler");
    }

    @Override
    public <TInstance extends ColorShift> TInstance read(String writtenVal)
            throws IllegalArgumentException {
        Check.ifNullOrEmpty(writtenVal, "writtenVal");

        var dto = JSON.fromJson(writtenVal, Dto.class);

        var amountProvider = PROVIDER_HANDLER.read(dto.amt);

        //noinspection unchecked
        return (TInstance) switch (dto.type) {
            case TYPE_BRIGHTNESS -> //noinspection unchecked
                    brightnessShift(amountProvider, dto.overrides);
            case TYPE_ROTATION -> //noinspection unchecked
                    rotationShift(amountProvider, dto.overrides);
            case TYPE_COMPONENT_SHIFT -> //noinspection unchecked
                    colorComponentShift(amountProvider, dto.overrides,
                            ColorComponent.fromValue(dto.comp));
            default -> throw new IllegalArgumentException(
                    "ColorShiftHandler.read: invalid type (" + dto.type + ")");
        };
    }

    @Override
    public String write(ColorShift shift) {
        Check.ifNull(shift, "shift");

        var dto = new Dto();

        switch (shift) {
            case BrightnessShift _ -> dto.type = TYPE_BRIGHTNESS;
            case ColorRotationShift _ -> dto.type = TYPE_ROTATION;
            case ColorComponentIntensityShift c -> {
                dto.type = TYPE_COMPONENT_SHIFT;
                dto.comp = Check.ifNull(c.COLOR_COMPONENT, "COLOR_COMPONENT within shift")
                        .getValue();
            }
            default -> throw new IllegalArgumentException(
                    "ColorShiftHandler.write: unknown ColorShift received (" +
                            shift.getClass().getCanonicalName() + ")");
        }
        dto.amt = PROVIDER_HANDLER.write(
                Check.ifNull(shift.AMOUNT_PROVIDER, "shift.AMOUNT_PROVIDER"));
        dto.overrides = shift.OVERRIDES_PRIOR_SHIFTS_OF_SAME_TYPE;

        return JSON.toJson(dto);
    }

    private static class Dto {
        String type;
        String amt;
        boolean overrides;
        Integer comp;
    }
}
