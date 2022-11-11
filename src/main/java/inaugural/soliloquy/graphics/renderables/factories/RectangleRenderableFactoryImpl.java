package inaugural.soliloquy.graphics.renderables.factories;

import inaugural.soliloquy.graphics.renderables.RectangleRenderableImpl;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.graphics.renderables.RectangleRenderable;
import soliloquy.specs.graphics.renderables.RenderableWithMouseEvents.MouseEventInputs;
import soliloquy.specs.graphics.renderables.factories.RectangleRenderableFactory;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;

import java.awt.*;
import java.util.Map;
import java.util.UUID;

public class RectangleRenderableFactoryImpl implements RectangleRenderableFactory {
    private final RenderingBoundaries RENDERING_BOUNDARIES;

    public RectangleRenderableFactoryImpl(RenderingBoundaries renderingBoundaries) {
        RENDERING_BOUNDARIES = Check.ifNull(renderingBoundaries, "renderingBoundaries");
    }

    @Override
    public RectangleRenderable make(ProviderAtTime<Color> topLeftColorProvider,
                                    ProviderAtTime<Color> topRightColorProvider,
                                    ProviderAtTime<Color> bottomRightColorProvider,
                                    ProviderAtTime<Color> bottomLeftColorProvider,
                                    ProviderAtTime<Integer> backgroundTextureIdProvider,
                                    float backgroundTextureTileWidth,
                                    float backgroundTextureTileHeight,
                                    Map<Integer, Action<MouseEventInputs>> onPress,
                                    Map<Integer, Action<MouseEventInputs>> onRelease,
                                    Action<MouseEventInputs> onMouseOver,
                                    Action<MouseEventInputs> onMouseLeave,
                                    ProviderAtTime<FloatBox> renderingAreaProvider,
                                    int z,
                                    UUID uuid,
                                    RenderableStack containingStack)
            throws IllegalArgumentException {
        return new RectangleRenderableImpl(topLeftColorProvider, topRightColorProvider,
                bottomRightColorProvider, bottomLeftColorProvider, backgroundTextureIdProvider,
                backgroundTextureTileWidth, backgroundTextureTileHeight, onPress, onRelease,
                onMouseOver, onMouseLeave, renderingAreaProvider, z, uuid,
                containingStack, RENDERING_BOUNDARIES);
    }

    @Override
    public String getInterfaceName() {
        return RectangleRenderableFactory.class.getCanonicalName();
    }
}
