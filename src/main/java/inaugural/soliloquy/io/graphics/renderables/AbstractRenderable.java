package inaugural.soliloquy.io.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.io.graphics.renderables.Renderable;
import soliloquy.specs.io.graphics.rendering.RenderableStack;

import java.util.UUID;

abstract class AbstractRenderable implements Renderable {
    private RenderableStack containingStack;
    private final UUID UUID;

    private int z;

    protected AbstractRenderable(int z, UUID uuid, RenderableStack containingStack) {
        this.z = z;
        UUID = Check.ifNull(uuid, "uuid");
        this.containingStack = Check.ifNull(containingStack, "containingStack");
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public void setZ(int z) {
        this.z = z;
        containingStack.add(this);
    }

    @Override
    public RenderableStack containingStack() {
        return containingStack;
    }

    // NB: deleted SpriteRenderables should NOT_ make other calls unsupported, unlike
    //     TileEntities, since it might be deleted in the middle of rendering a frame which
    //     contains it, causing a breaking race condition.
    @Override
    public void delete() {
        RenderableStack stack = containingStack;
        containingStack = null;
        stack.remove(this);
    }

    @Override
    public UUID uuid() {
        return UUID;
    }
}
