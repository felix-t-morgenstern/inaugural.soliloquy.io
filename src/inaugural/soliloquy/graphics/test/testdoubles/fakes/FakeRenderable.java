package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

public class FakeRenderable implements Renderable {
    public int Z;
    public ProviderAtTime<FloatBox> RenderingAreaProvider;

    public FakeRenderable(int z) {
        Z = z;
    }

    @Override
    public ProviderAtTime<FloatBox> getRenderingAreaProvider() {
        return RenderingAreaProvider;
    }

    @Override
    public void setRenderingAreaProvider(ProviderAtTime<FloatBox> providerAtTime)
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
    public EntityUuid uuid() {
        return null;
    }
}
