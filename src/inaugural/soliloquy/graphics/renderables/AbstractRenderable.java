package inaugural.soliloquy.graphics.renderables;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.rendering.RenderableStack;

import java.util.UUID;

abstract class AbstractRenderable implements Renderable {
    private RenderableStack _containingStack;
    private final UUID UUID;

    private int _z;

    protected AbstractRenderable(int z, UUID uuid, RenderableStack containingStack) {
        _z = z;
        UUID = Check.ifNull(uuid, "uuid");
        _containingStack = Check.ifNull(containingStack, "containingStack");
    }

    @Override
    public int getZ() {
        return _z;
    }

    @Override
    public void setZ(int z) {
        _z = z;
        _containingStack.add(this);
    }

    @Override
    public RenderableStack containingStack() {
        return _containingStack;
    }

    // NB: deleted SpriteRenderables should _NOT_ make other calls unsupported, unlike
    //     TileEntities, since it might be deleted in the middle of rendering a frame which
    //     contains it, causing a breaking race condition.
    @Override
    public void delete() {
        RenderableStack stack = _containingStack;
        _containingStack = null;
        stack.remove(this);
    }

    @Override
    public UUID uuid() {
        return UUID;
    }
}
