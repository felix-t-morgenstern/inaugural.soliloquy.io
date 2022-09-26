package inaugural.soliloquy.graphics.test.testdoubles.fakes;

import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.SpriteRenderable;
import soliloquy.specs.graphics.rendering.Mesh;
import soliloquy.specs.graphics.rendering.Shader;
import soliloquy.specs.graphics.rendering.renderers.Renderer;

import java.util.ArrayList;
import java.util.List;

public class FakeRenderer implements Renderer<Renderable> {
    public Mesh Mesh;
    public Shader Shader;
    public List<Renderable> Rendered = new ArrayList<>();
    public List<Long> Timestamps = new ArrayList<>();

    public Renderer<SpriteRenderable> SpriteRenderer;

    @Override
    public void setMesh(Mesh mesh) throws IllegalArgumentException {
        Mesh = mesh;
    }

    @Override
    public void setShader(Shader shader) throws IllegalArgumentException {
        Shader = shader;
    }

    @Override
    public void render(Renderable renderable, long timestamp) throws IllegalArgumentException {
        Rendered.add(renderable);
        Timestamps.add(timestamp);

        if (renderable instanceof SpriteRenderable) {
            if (SpriteRenderer != null) {
                SpriteRenderer.render((SpriteRenderable) renderable, timestamp);
            }
        }
    }

    @Override
    public String getInterfaceName() {
        return null;
    }

    @Override
    public Long mostRecentTimestamp() {
        return null;
    }
}
