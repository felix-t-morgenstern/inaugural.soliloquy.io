package inaugural.soliloquy.graphics.renderables.factories;

import inaugural.soliloquy.graphics.renderables.ImageAssetSetRenderableImpl;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.shared.Direction;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.graphics.assets.ImageAssetSet;
import soliloquy.specs.graphics.renderables.ImageAssetSetRenderable;
import soliloquy.specs.graphics.renderables.RenderableWithMouseEvents.MouseEventInputs;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.factories.ImageAssetSetRenderableFactory;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;

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
                                        String type,
                                        Direction direction,
                                        List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                        ProviderAtTime<Float> borderThicknessProvider,
                                        ProviderAtTime<Color> borderColorProvider,
                                        ProviderAtTime<FloatBox> renderingAreaProvider,
                                        int z,
                                        UUID uuid,
                                        RenderableStack containingStack)
            throws IllegalArgumentException {
        return new ImageAssetSetRenderableImpl(imageAssetSet, type, direction, colorShiftProviders,
                borderThicknessProvider, borderColorProvider, renderingAreaProvider, z, uuid,
                containingStack, RENDERING_BOUNDARIES);
    }

    @Override
    public ImageAssetSetRenderable make(ImageAssetSet imageAssetSet,
                                        String type,
                                        Direction direction,
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
        return new ImageAssetSetRenderableImpl(imageAssetSet, type, direction, onPress, onRelease,
                onMouseOver, onMouseLeave, colorShiftProviders, borderThicknessProvider,
                borderColorProvider, renderingAreaProvider, z, uuid, containingStack,
                RENDERING_BOUNDARIES);
    }
}
