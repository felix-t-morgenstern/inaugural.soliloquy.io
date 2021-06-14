package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.graphics.shared.TimestampValidator;
import inaugural.soliloquy.tools.generic.HasOneGenericParam;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

public abstract class AbstractRenderer<TRenderable extends Renderable>
        extends HasOneGenericParam<TRenderable>
        implements Renderer<TRenderable> {
    protected final TimestampValidator TIMESTAMP_VALIDATOR;

    protected AbstractRenderer(TRenderable archetype) {
        super(archetype);
        TIMESTAMP_VALIDATOR = new TimestampValidator();
    }

    @Override
    protected String getUnparameterizedInterfaceName() {
        return Renderer.class.getCanonicalName();
    }
}
