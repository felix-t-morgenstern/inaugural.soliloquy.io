package inaugural.soliloquy.io.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.io.graphics.renderables.Component;
import soliloquy.specs.io.graphics.renderables.Renderable;
import soliloquy.specs.io.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.io.graphics.rendering.renderers.ComponentRenderer;
import soliloquy.specs.io.graphics.rendering.renderers.Renderer;

import java.util.Comparator;
import java.util.Map;
import java.util.function.BiConsumer;

public class ComponentRendererImpl implements ComponentRenderer {
    private final Map<Class<?>, Renderer<? extends Renderable>> CONTENT_RENDERERS;
    private final BiConsumer<Component, Long> PRERENDER_COMPONENT;
    private final RenderingBoundaries RENDERING_BOUNDARIES;
    private final TimestampValidator TIMESTAMP_VALIDATOR;

    public ComponentRendererImpl(Map<Class<?>, Renderer<? extends Renderable>> contentRenderers,
                                 BiConsumer<Component, Long> prerenderComponent,
                                 RenderingBoundaries renderingBoundaries,
                                 TimestampValidator timestampValidator) {
        CONTENT_RENDERERS = Check.ifNull(contentRenderers, "contentRenderers");
        PRERENDER_COMPONENT = Check.ifNull(prerenderComponent, "prerenderComponent");
        RENDERING_BOUNDARIES = Check.ifNull(renderingBoundaries, "renderingBoundaries");
        TIMESTAMP_VALIDATOR = Check.ifNull(timestampValidator, "timestampValidator");
    }

    @Override
    public void render(Component component, long timestamp) {
        TIMESTAMP_VALIDATOR.validateTimestamp(timestamp);

        PRERENDER_COMPONENT.accept(component, timestamp);

        var boundaries = component.getRenderingBoundariesProvider().provide(timestamp);
        RENDERING_BOUNDARIES.pushNewBoundaries(boundaries);

        var toRender = component.contentsRepresentation().stream()
                .sorted(Comparator.comparingInt(Renderable::getZ));

        toRender.forEach(r -> {
            if (r instanceof Component c) {
                render(c, timestamp);
            }
            else {
                @SuppressWarnings("rawtypes") var renderer =
                        (Renderer) CONTENT_RENDERERS.get(r.getClass());
                //noinspection unchecked
                renderer.render(r, timestamp);
            }
        });

        RENDERING_BOUNDARIES.popMostRecentBoundaries();
    }
}
