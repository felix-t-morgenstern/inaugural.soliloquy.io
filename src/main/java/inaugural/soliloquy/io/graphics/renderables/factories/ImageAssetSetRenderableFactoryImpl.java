package inaugural.soliloquy.io.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.ImageAssetSetRenderableImpl;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.assets.ImageAssetSet;
import soliloquy.specs.io.graphics.renderables.ImageAssetSetRenderable;
import soliloquy.specs.io.graphics.renderables.Renderable;
import soliloquy.specs.io.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.io.graphics.renderables.factories.ImageAssetSetRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.ui.EventInputs;
import soliloquy.specs.ui.Component;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

public class ImageAssetSetRenderableFactoryImpl implements ImageAssetSetRenderableFactory {
    private final RenderingBoundaries RENDERING_BOUNDARIES;

    public ImageAssetSetRenderableFactoryImpl(RenderingBoundaries renderingBoundaries) {
        RENDERING_BOUNDARIES = Check.ifNull(renderingBoundaries, "renderingBoundaries");
    }

    @Override
    public ImageAssetSetRenderable make(ImageAssetSet imageAssetSet,
                                        Map<String, String> displayParams,
                                        List<ColorShift> colorShifts,
                                        ProviderAtTime<Float> borderThicknessProvider,
                                        ProviderAtTime<Color> borderColorProvider,
                                        ProviderAtTime<FloatBox> renderingAreaProvider,
                                        int z,
                                        UUID uuid,
                                        Component component)
            throws IllegalArgumentException {
        return new ImageAssetSetRenderableImpl(imageAssetSet, displayParams, colorShifts,
                borderThicknessProvider, borderColorProvider, renderingAreaProvider, z, uuid,
                component, RENDERING_BOUNDARIES);
    }

    @Override
    public ImageAssetSetRenderable make(ImageAssetSet imageAssetSet,
                                        Map<String, String> displayParams,
                                        ProviderAtTime<Float> borderThicknessProvider,
                                        ProviderAtTime<Color> borderColorProvider,
                                        Map<Integer, Action<EventInputs>> onPress,
                                        Map<Integer, Action<EventInputs>> onRelease,
                                        Action<EventInputs> onMouseOver,
                                        Action<EventInputs> onMouseLeave,
                                        List<ColorShift> colorShifts,
                                        ProviderAtTime<FloatBox> renderingAreaProvider,
                                        int z,
                                        UUID uuid,
                                        Component component)
            throws IllegalArgumentException {
        return new ImageAssetSetRenderableImpl(imageAssetSet, displayParams, onPress, onRelease,
                onMouseOver, onMouseLeave, colorShifts, borderThicknessProvider,
                borderColorProvider, renderingAreaProvider, z, uuid, component, RENDERING_BOUNDARIES);
    }
}
