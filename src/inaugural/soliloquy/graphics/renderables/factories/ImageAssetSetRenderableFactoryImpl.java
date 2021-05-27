package inaugural.soliloquy.graphics.renderables.factories;

import inaugural.soliloquy.graphics.renderables.ImageAssetSetRenderableImpl;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.assets.ImageAssetSet;
import soliloquy.specs.graphics.renderables.ImageAssetSetRenderable;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.factories.ImageAssetSetRenderableFactory;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class ImageAssetSetRenderableFactoryImpl implements ImageAssetSetRenderableFactory {
    @Override
    public ImageAssetSetRenderable make(ImageAssetSet imageAssetSet, String type, String direction,
                                        List<ColorShift> colorShifts,
                                        ProviderAtTime<Float> borderThicknessProvider,
                                        ProviderAtTime<Color> borderColorProvider,
                                        ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                        EntityUuid uuid,
                                        Consumer<Renderable> updateZIndexInContainer,
                                        Consumer<Renderable> removeFromContainer)
            throws IllegalArgumentException {
        return new ImageAssetSetRenderableImpl(imageAssetSet, type, direction, colorShifts,
                borderThicknessProvider, borderColorProvider, renderingAreaProvider, z, uuid,
                updateZIndexInContainer, removeFromContainer);
    }

    /** @noinspection rawtypes*/
    @Override
    public ImageAssetSetRenderable make(ImageAssetSet imageAssetSet, String type, String direction,
                                        Action onClick, Action onMouseOver, Action onMouseLeave,
                                        List<ColorShift> colorShifts,
                                        ProviderAtTime<Float> borderThicknessProvider,
                                        ProviderAtTime<Color> borderColorProvider,
                                        ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                        EntityUuid uuid,
                                        Consumer<Renderable> updateZIndexInContainer,
                                        Consumer<Renderable> removeFromContainer)
            throws IllegalArgumentException {
        return new ImageAssetSetRenderableImpl(imageAssetSet, type, direction, onClick,
                onMouseOver, onMouseLeave, colorShifts, borderThicknessProvider,
                borderColorProvider, renderingAreaProvider, z, uuid, updateZIndexInContainer,
                removeFromContainer);
    }

    @Override
    public String getInterfaceName() {
        return ImageAssetSetRenderableFactory.class.getCanonicalName();
    }
}
