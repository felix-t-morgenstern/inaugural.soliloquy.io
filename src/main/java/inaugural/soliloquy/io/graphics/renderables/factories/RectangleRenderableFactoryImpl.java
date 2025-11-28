package inaugural.soliloquy.io.graphics.renderables.factories;

import inaugural.soliloquy.io.graphics.renderables.RectangleRenderableImpl;
import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.common.entities.Consumer;
import soliloquy.specs.common.valueobjects.FloatBox;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.RectangleRenderable;
import soliloquy.specs.io.graphics.renderables.factories.RectangleRenderableFactory;
import soliloquy.specs.io.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.ui.EventInputs;

import java.awt.*;
import java.util.Map;
import java.util.UUID;

public class RectangleRenderableFactoryImpl implements RectangleRenderableFactory {
    private final RenderingBoundaries RENDERING_BOUNDARIES;
    private final TimestampValidator TIMESTAMP_VALIDATOR;

    public RectangleRenderableFactoryImpl(RenderingBoundaries renderingBoundaries,
                                          TimestampValidator timestampValidator) {
        RENDERING_BOUNDARIES = Check.ifNull(renderingBoundaries, "renderingBoundaries");
        TIMESTAMP_VALIDATOR = Check.ifNull(timestampValidator, "timestampValidator");
    }

    @Override
    public RectangleRenderable make(ProviderAtTime<Color> topLeftColorProvider,
                                    ProviderAtTime<Color> topRightColorProvider,
                                    ProviderAtTime<Color> bottomRightColorProvider,
                                    ProviderAtTime<Color> bottomLeftColorProvider,
                                    ProviderAtTime<Integer> backgroundTextureIdProvider,
                                    ProviderAtTime<Float> textureTileWidthProvider,
                                    ProviderAtTime<Float> textureTileHeightProvider,
                                    Map<Integer, Consumer<EventInputs>> onPress,
                                    Map<Integer, Consumer<EventInputs>> onRelease,
                                    Consumer<EventInputs> onMouseOver,
                                    Consumer<EventInputs> onMouseLeave,
                                    ProviderAtTime<FloatBox> renderingAreaProvider,
                                    int z,
                                    UUID uuid,
                                    Component component)
            throws IllegalArgumentException {
        return new RectangleRenderableImpl(topLeftColorProvider, topRightColorProvider,
                bottomRightColorProvider, bottomLeftColorProvider, backgroundTextureIdProvider,
                textureTileWidthProvider, textureTileHeightProvider, onPress, onRelease,
                onMouseOver, onMouseLeave, renderingAreaProvider, z, uuid, component,
                RENDERING_BOUNDARIES, TIMESTAMP_VALIDATOR);
    }
}
