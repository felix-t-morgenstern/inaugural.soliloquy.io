package inaugural.soliloquy.graphics.renderables.factories;

import inaugural.soliloquy.graphics.renderables.ImageAssetSetRenderableImpl;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.graphics.assets.ImageAssetSet;
import soliloquy.specs.graphics.renderables.ImageAssetSetRenderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.factories.ImageAssetSetRenderableFactory;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ImageAssetSetRenderableFactoryImpl implements ImageAssetSetRenderableFactory {
    @Override
    public ImageAssetSetRenderable make(ImageAssetSet imageAssetSet, String type, String direction,
                                        List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                        ProviderAtTime<Float> borderThicknessProvider,
                                        ProviderAtTime<Color> borderColorProvider,
                                        ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                        UUID uuid,
                                        RenderableStack containingStack)
            throws IllegalArgumentException {
        return new ImageAssetSetRenderableImpl(imageAssetSet, type, direction, colorShiftProviders,
                borderThicknessProvider, borderColorProvider, renderingAreaProvider, z, uuid,
                containingStack);
    }

    @Override
    public ImageAssetSetRenderable make(ImageAssetSet imageAssetSet, String type, String direction,
                                        Map<Integer, Action<Long>> onPress,
                                        Map<Integer, Action<Long>> onRelease,
                                        Action<Long> onMouseOver, Action<Long> onMouseLeave,
                                        List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                        ProviderAtTime<Float> borderThicknessProvider,
                                        ProviderAtTime<Color> borderColorProvider,
                                        ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                                        UUID uuid, RenderableStack containingStack)
            throws IllegalArgumentException {
        return new ImageAssetSetRenderableImpl(imageAssetSet, type, direction, onPress, onRelease,
                onMouseOver, onMouseLeave, colorShiftProviders, borderThicknessProvider,
                borderColorProvider, renderingAreaProvider, z, uuid, containingStack);
    }

    @Override
    public String getInterfaceName() {
        return ImageAssetSetRenderableFactory.class.getCanonicalName();
    }
}
