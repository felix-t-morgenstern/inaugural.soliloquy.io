package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import inaugural.soliloquy.tools.timing.TimestampValidator;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.rendering.Mesh;
import soliloquy.specs.graphics.rendering.Shader;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

public abstract class AbstractRenderer<TRenderable extends Renderable>
        implements Renderer<TRenderable> {
    protected Mesh mesh;
    protected Shader shader;

    protected final TimestampValidator TIMESTAMP_VALIDATOR;

    protected AbstractRenderer(Long mostRecentTimestamp) {
        TIMESTAMP_VALIDATOR = new TimestampValidator(mostRecentTimestamp);
    }

    @Override
    public void setMesh(Mesh mesh) throws IllegalArgumentException {
        this.mesh = Check.ifNull(mesh, "mesh");
    }

    @Override
    public void setShader(Shader shader) throws IllegalArgumentException {
        this.shader = Check.ifNull(shader, "shader");
    }

    @Override
    public Long mostRecentTimestamp() {
        return TIMESTAMP_VALIDATOR.mostRecentTimestamp();
    }
}
