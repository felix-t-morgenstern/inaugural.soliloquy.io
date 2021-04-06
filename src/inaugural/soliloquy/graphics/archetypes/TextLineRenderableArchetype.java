package inaugural.soliloquy.graphics.archetypes;

import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.renderables.Renderable;
import soliloquy.specs.graphics.renderables.TextLineRenderable;
import soliloquy.specs.graphics.rendering.FloatBox;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class TextLineRenderableArchetype implements TextLineRenderable {
    @Override
    public Font font() {
        return null;
    }

    @Override
    public float lineHeight() {
        return 0;
    }

    @Override
    public String lineText() {
        return null;
    }

    @Override
    public Map<Integer, Color> colorIndices() {
        return null;
    }

    @Override
    public List<Integer> italicIndices() {
        return null;
    }

    @Override
    public List<Integer> boldIndices() {
        return null;
    }

    @Override
    public FloatBox renderingArea() {
        return null;
    }

    @Override
    public int z() {
        return 0;
    }

    @Override
    public Renderable<TextLineRenderable> makeClone() {
        return null;
    }

    @Override
    public String getInterfaceName() {
        return TextLineRenderable.class.getCanonicalName();
    }
}
