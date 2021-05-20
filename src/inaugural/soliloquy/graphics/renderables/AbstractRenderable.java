package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.util.function.Consumer;

abstract class AbstractRenderable implements Renderable {
    private final Consumer<Renderable> REMOVE_FROM_CONTAINER;
    private final ProviderAtTime<FloatBox> RENDERING_AREA_PROVIDER;
    private final int Z;
    private final EntityUuid UUID;

    public AbstractRenderable(ProviderAtTime<FloatBox> renderingAreaProvider, int z,
                              EntityUuid uuid,  Consumer<Renderable> removeFromContainer) {
        RENDERING_AREA_PROVIDER = Check.ifNull(renderingAreaProvider, "renderingAreaProvider");
        Z = z;
        UUID = Check.ifNull(uuid, "uuid");
        REMOVE_FROM_CONTAINER = Check.ifNull(removeFromContainer, "removeFromContainer");
    }

    @Override
    public ProviderAtTime<FloatBox> renderingAreaProvider() {
        return RENDERING_AREA_PROVIDER;
    }

    @Override
    public int z() {
        return Z;
    }

    // NB: deleted SpriteRenderables should _NOT_ make other calls unsupported, unlike
    //     TileEntities, since it might be deleted in the middle of rendering a frame which
    //     contains it, causing a breaking race condition.
    @Override
    public void delete() {
        REMOVE_FROM_CONTAINER.accept(this);
    }

    @Override
    public EntityUuid uuid() {
        return UUID;
    }
}
