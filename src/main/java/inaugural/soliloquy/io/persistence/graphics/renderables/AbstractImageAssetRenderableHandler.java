package inaugural.soliloquy.io.persistence.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.assets.Asset;
import soliloquy.specs.io.graphics.renderables.ImageAssetRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

abstract class AbstractImageAssetRenderableHandler<TAsset extends Asset,
        TRenderable extends ImageAssetRenderable>
        extends AbstractMouseEventsRenderableHandler<TRenderable> {
    protected final Function<String, TAsset> GET_ASSET;
    @SuppressWarnings("rawtypes") protected final TypeHandler<ProviderAtTime> PROVIDER_HANDLER;
    protected final TypeHandler<ColorShift> SHIFT_HANDLER;

    protected AbstractImageAssetRenderableHandler(
            Function<String, TAsset> getAsset,
            @SuppressWarnings("rawtypes") Function<String, Action> getAction,
            @SuppressWarnings("rawtypes") TypeHandler<ProviderAtTime> providerHandler,
            TypeHandler<ColorShift> shiftHandler) {
        super(getAction);
        GET_ASSET = Check.ifNull(getAsset, "getAsset");
        PROVIDER_HANDLER = Check.ifNull(providerHandler, "providerHandler");
        SHIFT_HANDLER = Check.ifNull(shiftHandler, "shiftHandler");
    }

    protected void hydrateDto(Dto dto, TRenderable renderable, String assetId) {
        super.hydrateDto(dto, renderable);

        var borderThickness = PROVIDER_HANDLER.write(renderable.getBorderThicknessProvider());
        var borderColor = PROVIDER_HANDLER.write(renderable.getBorderColorProvider());

        var shifts = renderable.colorShifts();
        var writtenShifts = new String[shifts.size()];
        for (var i = 0; i < shifts.size(); i++) {
            writtenShifts[i] = SHIFT_HANDLER.write(shifts.get(i));
        }

        var area = PROVIDER_HANDLER.write(renderable.getRenderingDimensionsProvider());

        dto.assetId = assetId;
        dto.borderThickness = borderThickness;
        dto.borderColor = borderColor;
        dto.colorShifts = writtenShifts;
        dto.area = area;
    }

    protected void hydrateReadProps(ReadProps<TAsset> readProps, Dto dto) {
        super.hydrateReadProps(readProps, dto);

        readProps.asset = GET_ASSET.apply(dto.assetId);

        readProps.borderThickness = PROVIDER_HANDLER.read(dto.borderThickness);
        readProps.borderColor = PROVIDER_HANDLER.read(dto.borderColor);

        readProps.shifts =
                Arrays.stream(dto.colorShifts).map(s -> (ColorShift) SHIFT_HANDLER.read(s))
                        .toList();

        readProps.area = PROVIDER_HANDLER.read(dto.area);
    }

    static class ReadProps<TAsset> extends AbstractMouseEventsRenderableHandler.ReadProps {
        TAsset asset;
        ProviderAtTime<Float> borderThickness;
        ProviderAtTime<Color> borderColor;
        List<ColorShift> shifts;
        ProviderAtTime<FloatBox> area;
    }

    static class Dto extends AbstractMouseEventsRenderableHandler.Dto {
        public String assetId;
        public String borderThickness;
        public String borderColor;
        public String[] colorShifts;
        public String area;
    }
}
