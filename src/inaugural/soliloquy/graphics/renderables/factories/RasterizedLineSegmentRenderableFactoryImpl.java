package inaugural.soliloquy.graphics.renderables.factories;

import inaugural.soliloquy.graphics.renderables.RasterizedLineSegmentRenderableImpl;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.RasterizedLineSegmentRenderable;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.factories.RasterizedLineSegmentRenderableFactory;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.function.Consumer;

public class RasterizedLineSegmentRenderableFactoryImpl
        implements RasterizedLineSegmentRenderableFactory {
    @Override
    public RasterizedLineSegmentRenderable make(ProviderAtTime<Float> thicknessProvider,
                                                short stipplePattern, short stippleFactor,
                                                ProviderAtTime<Color> colorProvider,
                                                ProviderAtTime<FloatBox>
                                                            renderingDimensionsProvider,
                                                int z, EntityUuid uuid,
                                                Consumer<Renderable> updateZIndexInContainer,
                                                Consumer<Renderable> removeFromContainer)
            throws IllegalArgumentException {
        return new RasterizedLineSegmentRenderableImpl(thicknessProvider, stipplePattern,
                stippleFactor, colorProvider, renderingDimensionsProvider, z, uuid,
                updateZIndexInContainer, removeFromContainer);
    }

    @Override
    public String getInterfaceName() {
        return RasterizedLineSegmentRenderableFactory.class.getCanonicalName();
    }
}
