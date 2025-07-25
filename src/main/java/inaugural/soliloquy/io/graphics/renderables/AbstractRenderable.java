package inaugural.soliloquy.io.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.io.graphics.renderables.Renderable;
import soliloquy.specs.ui.Component;

import java.util.UUID;
import java.util.function.BiConsumer;

abstract class AbstractRenderable implements Renderable {
    private final Component COMPONENT;
    private final BiConsumer<Component, Renderable> REMOVE_FROM_COMPONENT;
    private final UUID UUID;

    private int z;
    private boolean isDeleted;

    protected AbstractRenderable(int z, UUID uuid,
                                 Component component,
                                 BiConsumer<Component, Renderable> removeFromComponent) {
        COMPONENT = Check.ifNull(component, "component");
        REMOVE_FROM_COMPONENT = Check.ifNull(removeFromComponent, "removeFromComponent");
        this.z = z;
        UUID = Check.ifNull(uuid, "uuid");
    }

    @Override
    public Component component() {
        return isDeleted ? null : COMPONENT;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public void setZ(int z) {
        this.z = z;
        COMPONENT.add(this);
    }

    // NB: deleted SpriteRenderables should NOT_ make other calls unsupported, unlike
    //     TileEntities, since it might be deleted in the middle of rendering a frame which
    //     contains it, causing a breaking race condition.
    @Override
    public void delete() {
        isDeleted = true;
        REMOVE_FROM_COMPONENT.accept(COMPONENT, this);
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
