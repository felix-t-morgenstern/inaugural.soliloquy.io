package inaugural.soliloquy.io.persistence.graphics.renderables;

import inaugural.soliloquy.io.persistence.graphics.renderables.providers.ProviderHandler;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.persistence.AbstractTypeHandler;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.assets.ImageAsset;
import soliloquy.specs.io.graphics.renderables.ImageAssetRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.ui.EventInputs;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static inaugural.soliloquy.tools.collections.Collections.mapOf;
import static soliloquy.specs.common.valueobjects.Pair.pairOf;

abstract class AbstractImageAssetRenderableHandler<
        TAsset extends ImageAsset,
        TRenderable extends ImageAssetRenderable> extends AbstractTypeHandler<TRenderable> {
    protected final Function<String, TAsset> GET_ASSET;
    @SuppressWarnings("rawtypes") protected final Function<String, Action> GET_ACTION;
    @SuppressWarnings("rawtypes") protected final TypeHandler<ProviderAtTime> PROVIDER_HANDLER;
    protected final TypeHandler<ColorShift> SHIFT_HANDLER;

    protected AbstractImageAssetRenderableHandler(
            Function<String, TAsset> getAsset,
            @SuppressWarnings("rawtypes") Function<String, Action> getAction,
            @SuppressWarnings("rawtypes") TypeHandler<ProviderAtTime> providerHandler,
            TypeHandler<ColorShift> shiftHandler) {
        GET_ASSET = Check.ifNull(getAsset, "getAsset");
        GET_ACTION = Check.ifNull(getAction, "getAction");
        PROVIDER_HANDLER = Check.ifNull(providerHandler, "providerHandler");
        SHIFT_HANDLER = Check.ifNull(shiftHandler, "shiftHandler");
    }

    protected void hydrateDto(ImageAssetRenderableDto dto, TRenderable renderable, String assetId) {
        var borderThickness = PROVIDER_HANDLER.write(renderable.getBorderThicknessProvider());
        var borderColor = PROVIDER_HANDLER.write(renderable.getBorderColorProvider());

        var onPressIds = renderable.pressActionIds();
        var onPress =
                new FiniteAnimationRenderableHandler.FiniteAnimationRenderableDto.ButtonEvent[onPressIds.size()];
        int i = 0;
        for (var onPressId : onPressIds.entrySet()) {
            var buttonEvent =
                    new FiniteAnimationRenderableHandler.FiniteAnimationRenderableDto.ButtonEvent();
            buttonEvent.button = onPressId.getKey();
            buttonEvent.actionId = onPressId.getValue();
            onPress[i++] = buttonEvent;
        }

        var onReleaseIds = renderable.releaseActionIds();
        var onRelease =
                new FiniteAnimationRenderableHandler.FiniteAnimationRenderableDto.ButtonEvent[onReleaseIds.size()];
        i = 0;
        for (var onReleaseId : onReleaseIds.entrySet()) {
            var buttonEvent =
                    new FiniteAnimationRenderableHandler.FiniteAnimationRenderableDto.ButtonEvent();
            buttonEvent.button = onReleaseId.getKey();
            buttonEvent.actionId = onReleaseId.getValue();
            onRelease[i++] = buttonEvent;
        }

        var shifts = renderable.colorShifts();
        var writtenShifts = new String[shifts.size()];
        for (i = 0; i < shifts.size(); i++) {
            writtenShifts[i] = SHIFT_HANDLER.write(shifts.get(i));
        }

        var area = PROVIDER_HANDLER.write(renderable.getRenderingDimensionsProvider());

        dto.assetId = assetId;
        dto.borderThickness = borderThickness;
        dto.borderColor = borderColor;
        dto.onPress = onPress;
        dto.onRelease = onRelease;
        dto.mouseOver = renderable.mouseOverActionId();
        dto.mouseLeave = renderable.mouseLeaveActionId();
        dto.colorShifts = writtenShifts;
        dto.area = area;
        dto.z = renderable.getZ();
        dto.uuid = renderable.uuid().toString();
        dto.type = renderable.getClass().getCanonicalName();
    }

    protected ReadProps<TAsset> readFromDto(ImageAssetRenderableDto dto) {
        var readProps = new ReadProps<TAsset>();

        readProps.asset = GET_ASSET.apply(dto.assetId);

        readProps.borderThickness = PROVIDER_HANDLER.read(dto.borderThickness);
        readProps.borderColor = PROVIDER_HANDLER.read(dto.borderColor);

        //noinspection unchecked
        readProps.onPress = mapOf(Arrays.stream(dto.onPress)
                .map(p -> pairOf(p.button, (Action<EventInputs>) GET_ACTION.apply(p.actionId))));
        //noinspection unchecked
        readProps.onRelease = mapOf(Arrays.stream(dto.onRelease)
                .map(p -> pairOf(p.button, (Action<EventInputs>) GET_ACTION.apply(p.actionId))));
        //noinspection unchecked
        readProps.onMouseOver = GET_ACTION.apply(dto.mouseOver);
        //noinspection unchecked
        readProps.onMouseLeave = GET_ACTION.apply(dto.mouseLeave);

        readProps.shifts = Arrays.stream(dto.colorShifts).map(s -> (ColorShift) SHIFT_HANDLER.read(s))
                .toList();

        readProps.area = PROVIDER_HANDLER.read(dto.area);

        return readProps;
    }

    static class ReadProps<TAsset> {
        TAsset asset;
        ProviderAtTime<Float> borderThickness;
        ProviderAtTime<Color> borderColor;
        Map<Integer, Action<EventInputs>> onPress;
        Map<Integer, Action<EventInputs>> onRelease;
        Action<EventInputs> onMouseOver;
        Action<EventInputs> onMouseLeave;
        List<ColorShift> shifts;
        ProviderAtTime<FloatBox> area;
    }

    static class ImageAssetRenderableDto extends ProviderHandler.ProviderDTO {
        public String assetId;
        public String borderThickness;
        public String borderColor;
        public FiniteAnimationRenderableHandler.FiniteAnimationRenderableDto.ButtonEvent[] onPress;
        public FiniteAnimationRenderableHandler.FiniteAnimationRenderableDto.ButtonEvent[]
                onRelease;
        public String mouseOver;
        public String mouseLeave;
        public String[] colorShifts;
        public String area;
        public int z;
        public String uuid;

        static class ButtonEvent {
            public int button;
            public String actionId;
        }
    }
}
