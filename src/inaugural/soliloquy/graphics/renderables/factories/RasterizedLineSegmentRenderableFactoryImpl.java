package inaugural.soliloquy.graphics.renderables.factories;

import inaugural.soliloquy.graphics.renderables.RasterizedLineSegmentRenderableImpl;
import soliloquy.specs.common.infrastructure.Pair;
import soliloquy.specs.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.factories.RasterizedLineSegmentRenderableFactory;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;

import java.awt.*;
import java.util.UUID;
import java.util.function.Consumer;

public class RasterizedLineSegmentRenderableFactoryImpl
        implements RasterizedLineSegmentRenderableFactory {
    @Override
    public RasterizedLineSegmentRenderable make(ProviderAtTime<Pair<Float, Float>>
                                                        vertex1LocationProvider,
                                                ProviderAtTime<Pair<Float, Float>>
                                                        vertex2LocationProvider,
                                                ProviderAtTime<Float> thicknessProvider,
                                                short stipplePattern,
                                                short stippleFactor,
                                                ProviderAtTime<Color> colorProvider,
                                                int z,
                                                UUID uuid,
                                                Consumer<Renderable> updateZIndexInContainer,
                                                Consumer<Renderable> removeFromContainer)
            throws IllegalArgumentException {
        return new RasterizedLineSegmentRenderableImpl(vertex1LocationProvider,
                vertex2LocationProvider, thicknessProvider, stipplePattern, stippleFactor,
                colorProvider, z, uuid, updateZIndexInContainer, removeFromContainer);
    }

    @Override
    public String getInterfaceName() {
        return RasterizedLineSegmentRenderableFactory.class.getCanonicalName();
    }
}
