package inaugural.soliloquy.graphics.rendering;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.infrastructure.ReadableCollection;
import soliloquy.specs.common.infrastructure.ReadableMap;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.rendering.RenderableStack;
import soliloquy.specs.graphics.rendering.RendererType;
import soliloquy.specs.graphics.rendering.StackRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StackRendererImpl implements StackRenderer {
    private final RenderableStack RENDERABLE_STACK;
    private final RendererType<Renderable> RENDERER;

    public StackRendererImpl(RenderableStack renderableStack, RendererType<Renderable> renderer) {
        RENDERABLE_STACK = Check.ifNull(renderableStack, "renderableStack");
        RENDERER = Check.ifNull(renderer, "renderer");
    }

    // TODO: Refactor how keys are obtained and sorted after having refactored Collection to either extend List, or implement a method which exposes a properly-typed List or Array
    @Override
    public void render() {
        ReadableMap<Integer, ReadableCollection<Renderable>> toRender =
                RENDERABLE_STACK.snapshot();

        List<Integer> keys = new ArrayList<>();

        toRender.getKeys().forEach(keys::add);

        keys.sort(Collections.reverseOrder());

        keys.forEach(z -> toRender.get(z).forEach(RENDERER::render));
    }

    @Override
    public String getInterfaceName() {
        return StackRenderer.class.getCanonicalName();
    }
}
