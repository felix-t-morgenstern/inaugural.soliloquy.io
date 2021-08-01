package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.generic.HasOneGenericParam;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

public abstract class AbstractRenderer<TRenderable extends Renderable>
        extends HasOneGenericParam<TRenderable>
        implements Renderer<TRenderable> {
    protected final TimestampValidator TIMESTAMP_VALIDATOR;

    protected AbstractRenderer(TRenderable archetype, Long mostRecentTimestamp) {
        super(archetype);
        TIMESTAMP_VALIDATOR = new TimestampValidator(mostRecentTimestamp);
    }

    @Override
    protected String getUnparameterizedInterfaceName() {
        return Renderer.class.getCanonicalName();
    }

    // TODO: Test and implement
    @Override
    public Long mostRecentTimestamp() {
        return TIMESTAMP_VALIDATOR.mostRecentTimestamp();
    }
}
