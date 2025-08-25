package inaugural.soliloquy.io.persistence.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.collections.Collections;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.persistence.TypeHandler;
import soliloquy.specs.common.valueobjects.Pair;
import soliloquy.specs.io.graphics.assets.ImageAssetSet;
import soliloquy.specs.io.graphics.renderables.ImageAssetSetRenderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.factories.ImageAssetSetRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;

import java.util.Arrays;
import java.util.UUID;
import java.util.function.Function;

import static soliloquy.specs.common.valueobjects.Pair.pairOf;

public class ImageAssetSetRenderableHandler
        extends AbstractImageAssetRenderableHandler<ImageAssetSet, ImageAssetSetRenderable> {
    private final ImageAssetSetRenderableFactory FACTORY;

    public ImageAssetSetRenderableHandler(
            Function<String, ImageAssetSet> getImageAssetSet,
            @SuppressWarnings("rawtypes") Function<String, Action> getAction,
            @SuppressWarnings("rawtypes") TypeHandler<ProviderAtTime> providerHandler,
            TypeHandler<ColorShift> shiftHandler,
            ImageAssetSetRenderableFactory factory) {
        super(getImageAssetSet, getAction, providerHandler, shiftHandler);
        FACTORY = Check.ifNull(factory, "factory");
    }

    @SuppressWarnings("unchecked")
    @Override
    public ImageAssetSetRenderable read(String writtenVal)
            throws IllegalArgumentException {
        var dto = JSON.fromJson(Check.ifNullOrEmpty(writtenVal, "writtenVal"), Dto.class);

        var displayParams = Collections.<String, String>mapOf(
                Arrays.stream(dto.displayParams).map(p -> pairOf(p.key, p.val))
                        .toArray(Pair[]::new));

        var readProps = readFromDto(dto);

        return FACTORY.make(
                readProps.asset,
                displayParams,
                readProps.borderThickness,
                readProps.borderColor,
                readProps.onPress,
                readProps.onRelease,
                readProps.onMouseOver,
                readProps.onMouseLeave,
                readProps.shifts,
                readProps.area,
                dto.z,
                UUID.fromString(dto.uuid),
                null
        );
    }

    @Override
    public String write(ImageAssetSetRenderable renderable) {
        Check.ifNull(renderable, "renderable");

        var dto = new Dto();

        hydrateDto(dto, renderable, renderable.getImageAssetSet().id());

        var displayParams = renderable.displayParams();
        dto.displayParams = new Dto.DisplayParam[displayParams.size()];
        var index = 0;
        for (var displayParam : displayParams.entrySet()) {
            var displayParamDto = new Dto.DisplayParam();
            displayParamDto.key = displayParam.getKey();
            displayParamDto.val = displayParam.getValue();
            dto.displayParams[index++] = displayParamDto;
        }

        return JSON.toJson(dto);
    }

    private static class Dto extends ImageAssetRenderableDto {
        DisplayParam[] displayParams;

        private static class DisplayParam {
            String key;
            String val;
        }
    }
}
