package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.assets.ImageAssetSet;
import soliloquy.specs.graphics.renderables.ImageAssetSetRenderable;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.util.List;
import java.util.function.Consumer;

import static inaugural.soliloquy.tools.Tools.nullIfEmpty;

public class ImageAssetSetRenderableImpl extends AbstractRenderableWithArea implements ImageAssetSetRenderable {
    private final ImageAssetSet IMAGE_ASSET_SET;
    private final String TYPE;
    private final String DIRECTION;

    public ImageAssetSetRenderableImpl(ImageAssetSet imageAssetSet, String type, String direction,
                                       List<ColorShift> colorShifts,
                                       ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                       EntityUuid uuid, Consumer<Renderable> deleteConsumer) {
        super(colorShifts, renderingAreaProvider, z, uuid, deleteConsumer);
        IMAGE_ASSET_SET = Check.ifNull(imageAssetSet, "imageAssetSet");
        TYPE = nullIfEmpty(type);
        DIRECTION = nullIfEmpty(direction);
    }

    /** @noinspection rawtypes*/
    public ImageAssetSetRenderableImpl(ImageAssetSet imageAssetSet, String type, String direction,
                                       Action onClick, Action onMouseOver, Action onMouseLeave,
                                       List<ColorShift> colorShifts,
                                       ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                       EntityUuid uuid, Consumer<Renderable> deleteConsumer) {
        super(onClick, onMouseOver, onMouseLeave, colorShifts, renderingAreaProvider,
                z, uuid, deleteConsumer);
        IMAGE_ASSET_SET = Check.ifNull(imageAssetSet, "imageAssetSet");
        TYPE = nullIfEmpty(type);
        DIRECTION = nullIfEmpty(direction);
    }

    @Override
    public ImageAssetSet imageAssetSet() {
        return IMAGE_ASSET_SET;
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public String direction() {
        return DIRECTION;
    }

    @Override
    protected String className() {
        return "ImageAssetSetRenderableImpl";
    }

    @Override
    public String getInterfaceName() {
        return ImageAssetSetRenderable.class.getCanonicalName();
    }
}
