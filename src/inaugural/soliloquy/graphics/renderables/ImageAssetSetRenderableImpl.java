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

public class ImageAssetSetRenderableImpl extends AbstractRenderableWithArea implements ImageAssetSetRenderable {
    private final ImageAssetSet IMAGE_ASSET_SET;
    private final String TYPE;
    private final String DIRECTION;

    public ImageAssetSetRenderableImpl(ImageAssetSet imageAssetSet, String type, String direction,
                                       List<ColorShift> colorShifts,
                                       ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                       EntityUuid uuid, Consumer<Renderable> deleteConsumer) {
        super(colorShifts, renderingAreaProvider, z, uuid, deleteConsumer);
        throwOnBothNullOrEmpty(type, direction);
        IMAGE_ASSET_SET = Check.ifNull(imageAssetSet, "imageAssetSet");
        TYPE = type;
        DIRECTION = direction;
    }

    /** @noinspection rawtypes*/
    public ImageAssetSetRenderableImpl(ImageAssetSet imageAssetSet, String type, String direction,
                                       Action clickAction, Action mouseOverAction,
                                       Action mouseLeaveAction, List<ColorShift> colorShifts,
                                       ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                       EntityUuid uuid, Consumer<Renderable> deleteConsumer) {
        super(clickAction, mouseOverAction, mouseLeaveAction, colorShifts, renderingAreaProvider,
                z, uuid, deleteConsumer);
        throwOnBothNullOrEmpty(type, direction);
        IMAGE_ASSET_SET = Check.ifNull(imageAssetSet, "imageAssetSet");
        TYPE = type;
        DIRECTION = direction;
    }

    private void throwOnBothNullOrEmpty(String type, String direction) {
        if ((type == null || "".equals(type)) && (direction == null || "".equals(direction))) {
            throw new IllegalArgumentException(
                    "ImageAssetSetRenderableImpl: both type and direction cannot be null or empty");
        }
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
