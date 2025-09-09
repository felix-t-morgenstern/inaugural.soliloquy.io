package inaugural.soliloquy.io.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.Renderable;

import java.util.UUID;

abstract class AbstractRenderable implements Renderable {
    private final UUID UUID;

    private int z;
    private Component containingComponent;
    private boolean isDeleted;

    protected AbstractRenderable(int z, UUID uuid,
                                 Component containingComponent) {
        this.containingComponent = containingComponent;
        containingComponent.add(this);
        this.z = z;
        UUID = Check.ifNull(uuid, "uuid");
    }

    @Override
    public Component component() {
        return isDeleted ? null : containingComponent;
    }

    public void setContainingComponent(Component containingComponent) {
        this.containingComponent = containingComponent;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public void setZ(int z) {
        this.z = z;
    }

    // NB: deleted SpriteRenderables should NOT_ make other calls unsupported, unlike
    //     TileEntities, since it might be deleted in the middle of rendering a frame which
    //     contains it, causing a breaking race condition.
    @Override
    public void delete() {
        isDeleted = true;
        containingComponent.remove(this);
    }

    @Override
    public boolean isDeleted() {
        return isDeleted;
    }

    @Override
    public UUID uuid() {
        return UUID;
    }
}
