package inaugural.soliloquy.io.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.RectangleRenderableImpl;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.entities.Action;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.RectangleRenderable;
import soliloquy.specs.io.graphics.renderables.Renderable;
import soliloquy.specs.io.graphics.renderables.factories.RectangleRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.ui.EventInputs;
import soliloquy.specs.ui.Component;

import java.awt.*;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

public class RectangleRenderableFactoryImpl extends AbstractRenderableFactory
        implements RectangleRenderableFactory {
    private final RenderingBoundaries RENDERING_BOUNDARIES;

    public RectangleRenderableFactoryImpl(RenderingBoundaries renderingBoundaries,
                                          BiConsumer<Component, Renderable> removeFromComponent) {
        super(removeFromComponent);
        RENDERING_BOUNDARIES = Check.ifNull(renderingBoundaries, "renderingBoundaries");
    }

    @Override
    public RectangleRenderable make(ProviderAtTime<Color> topLeftColorProvider,
                                    ProviderAtTime<Color> topRightColorProvider,
                                    ProviderAtTime<Color> bottomRightColorProvider,
                                    ProviderAtTime<Color> bottomLeftColorProvider,
                                    ProviderAtTime<Integer> backgroundTextureIdProvider,
                                    ProviderAtTime<Float> textureTileWidthProvider,
                                    ProviderAtTime<Float> textureTileHeightProvider,
                                    Map<Integer, Action<EventInputs>> onPress,
                                    Map<Integer, Action<EventInputs>> onRelease,
                                    Action<EventInputs> onMouseOver,
                                    Action<EventInputs> onMouseLeave,
                                    ProviderAtTime<FloatBox> renderingAreaProvider,
                                    int z,
                                    UUID uuid,
                                    Component component)
            throws IllegalArgumentException {
        return new RectangleRenderableImpl(topLeftColorProvider, topRightColorProvider,
                bottomRightColorProvider, bottomLeftColorProvider, backgroundTextureIdProvider,
                textureTileWidthProvider, textureTileHeightProvider, onPress, onRelease,
                onMouseOver, onMouseLeave, renderingAreaProvider, z, uuid, component,
                REMOVE_FROM_COMPONENT, RENDERING_BOUNDARIES);
    }
}
