package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.generic.AbstractHasOneGenericParam;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.rendering.Mesh;
import soliloquy.specs.graphics.rendering.Shader;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

public abstract class AbstractRenderer<TRenderable extends Renderable>
        extends AbstractHasOneGenericParam<TRenderable>
        implements Renderer<TRenderable> {
    protected Mesh _mesh;
    protected Shader _shader;

    protected final TimestampValidator TIMESTAMP_VALIDATOR;

    protected AbstractRenderer(TRenderable archetype, Long mostRecentTimestamp) {
        super(archetype);
        TIMESTAMP_VALIDATOR = new TimestampValidator(mostRecentTimestamp);
    }

    @Override
    public void setMesh(Mesh mesh) throws IllegalArgumentException {
        _mesh = Check.ifNull(mesh, "mesh");
    }

    @Override
    public void setShader(Shader shader) throws IllegalArgumentException {
        _shader = Check.ifNull(shader, "shader");
    }

    @Override
    protected String getUnparameterizedInterfaceName() {
        return Renderer.class.getCanonicalName();
    }

    @Override
    public Long mostRecentTimestamp() {
        return TIMESTAMP_VALIDATOR.mostRecentTimestamp();
    }
}
