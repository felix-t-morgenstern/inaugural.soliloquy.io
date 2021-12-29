package inaugural.soliloquy.graphics.renderables.factories;

import inaugural.soliloquy.graphics.renderables.RectangleRenderableImpl;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.RectangleRenderable;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.colorshifting.ColorShift;
import soliloquy.specs.graphics.renderables.factories.RectangleRenderableFactory;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class RectangleRenderableFactoryImpl implements RectangleRenderableFactory {
    @Override
    public RectangleRenderable make(ProviderAtTime<Color> topLeftColorProvider,
                                    ProviderAtTime<Color> topRightColorProvider,
                                    ProviderAtTime<Color> bottomRightColorProvider,
                                    ProviderAtTime<Color> bottomLeftColorProvider,
                                    ProviderAtTime<Integer> backgroundTextureIdProvider,
                                    float backgroundTextureTileWidth,
                                    float backgroundTextureTileHeight,
                                    Map<Integer, Action<Long>> onPress,
                                    Map<Integer, Action<Long>> onRelease,
                                    Action<Long> onMouseOver,
                                    Action<Long> onMouseLeave,
                                    List<ProviderAtTime<ColorShift>> colorShiftProviders,
                                    ProviderAtTime<FloatBox> renderingAreaProvider,
                                    int z,
                                    EntityUuid uuid,
                                    Consumer<Renderable> updateZIndexInContainer,
                                    Consumer<Renderable> removeFromContainer)
            throws IllegalArgumentException {
        return new RectangleRenderableImpl(topLeftColorProvider, topRightColorProvider,
                bottomRightColorProvider, bottomLeftColorProvider, backgroundTextureIdProvider,
                backgroundTextureTileWidth, backgroundTextureTileHeight, onPress, onRelease,
                onMouseOver, onMouseLeave, colorShiftProviders, renderingAreaProvider, z, uuid,
                updateZIndexInContainer, removeFromContainer);
    }

    @Override
    public String getInterfaceName() {
        return RectangleRenderableFactory.class.getCanonicalName();
    }
}
