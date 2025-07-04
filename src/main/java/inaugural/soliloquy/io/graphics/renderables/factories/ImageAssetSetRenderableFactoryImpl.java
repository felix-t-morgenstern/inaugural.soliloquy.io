package inaugural.soliloquy.io.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.ImageAssetSetRenderableImpl;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.assets.ImageAssetSet;
import soliloquy.specs.io.graphics.renderables.ImageAssetSetRenderable;
import soliloquy.specs.io.graphics.renderables.RenderableWithMouseEvents.MouseEventInputs;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.factories.ImageAssetSetRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderableStack;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ImageAssetSetRenderableFactoryImpl implements ImageAssetSetRenderableFactory {
    private final RenderingBoundaries RENDERING_BOUNDARIES;

    public ImageAssetSetRenderableFactoryImpl(RenderingBoundaries renderingBoundaries) {
        RENDERING_BOUNDARIES = Check.ifNull(renderingBoundaries, "renderingBoundaries");
    }

    @Override
    public ImageAssetSetRenderable make(ImageAssetSet imageAssetSet,
                                        Map<String, String> displayParams,
                                        List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                        ProviderAtTime<Float> borderThicknessProvider,
                                        ProviderAtTime<Color> borderColorProvider,
                                        ProviderAtTime<FloatBox> renderingAreaProvider,
                                        int z,
                                        UUID uuid,
                                        RenderableStack containingStack)
            throws IllegalArgumentException {
        return new ImageAssetSetRenderableImpl(imageAssetSet, displayParams, colorShiftProviders,
                borderThicknessProvider, borderColorProvider, renderingAreaProvider, z, uuid,
                containingStack, RENDERING_BOUNDARIES);
    }

    @Override
    public ImageAssetSetRenderable make(ImageAssetSet imageAssetSet,
                                        Map<String, String> displayParams,
                                        Map<Integer, Action<MouseEventInputs>> onPress,
                                        Map<Integer, Action<MouseEventInputs>> onRelease,
                                        Action<MouseEventInputs> onMouseOver,
                                        Action<MouseEventInputs> onMouseLeave,
                                        List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                        ProviderAtTime<Float> borderThicknessProvider,
                                        ProviderAtTime<Color> borderColorProvider,
                                        ProviderAtTime<FloatBox> renderingAreaProvider,
                                        int z,
                                        UUID uuid, RenderableStack containingStack)
            throws IllegalArgumentException {
        return new ImageAssetSetRenderableImpl(imageAssetSet, displayParams, onPress, onRelease,
                onMouseOver, onMouseLeave, colorShiftProviders, borderThicknessProvider,
                borderColorProvider, renderingAreaProvider, z, uuid, containingStack,
                RENDERING_BOUNDARIES);
    }
}
