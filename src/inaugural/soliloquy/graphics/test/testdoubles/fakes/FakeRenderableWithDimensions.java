package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.renderables.RenderableWithDimensions;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.util.UUID;

public class FakeRenderableWithDimensions implements RenderableWithDimensions {
    public int Z;
    public ProviderAtTime<FloatBox> RenderingDimensionsProvider;

    public FakeRenderableWithDimensions(int z) {
        Z = z;
    }

    @Override
    public ProviderAtTime<FloatBox> getRenderingDimensionsProvider() {
        return RenderingDimensionsProvider;
    }

    @Override
    public void setRenderingDimensionsProvider(ProviderAtTime<FloatBox>
                                                       renderingDimensionsProvider)
            throws IllegalArgumentException {

    }

    @Override
    public int getZ() {
        return Z;
    }

    @Override
    public void setZ(int i) {

    }

    @Override
    public void delete() {

    }

    @Override
    public String getInterfaceName() {
        return null;
    }

    @Override
    public UUID uuid() {
        return null;
    }
}
