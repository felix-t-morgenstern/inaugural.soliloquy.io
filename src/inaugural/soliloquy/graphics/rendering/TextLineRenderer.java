package inaugural.soliloquy.graphics.rendering;

import inaugural.soliloquy.graphics.archetypes.TextLineRenderableArchetype;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.renderables.TextLineRenderable;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.graphics.rendering.factories.FloatBoxFactory;

public class TextLineRenderer extends CanRenderSnippets<TextLineRenderable> {
    private static final TextLineRenderable ARCHETYPE = new TextLineRenderableArchetype();

    public TextLineRenderer(RenderingBoundaries renderingBoundaries,
                            FloatBoxFactory floatBoxFactory) {
        super(renderingBoundaries, floatBoxFactory, ARCHETYPE);
    }

    @Override
    public void render(TextLineRenderable textLineRenderable) throws IllegalArgumentException {
        Check.ifNull(textLineRenderable.font(), "textLineRenderable.font()");
        Check.throwOnLteZero(textLineRenderable.lineHeight(), "textLineRenderable.lineHeight()");
        textLineRenderable.colorIndices().forEach((index, color) -> {
            validateIndex(index, textLineRenderable.lineText().length(),
                    "textLineRenderable.colorIndices()");
            if (color == null) {
                throw new IllegalArgumentException("TextLineRenderer.render: " +
                        "textLineRenderable.colorIndices cannot contain null value");
            }
        });
        textLineRenderable.italicIndices().forEach(i ->
                validateIndex(i, textLineRenderable.lineText().length(),
                        "textLineRenderable.italicIndices()"));
        textLineRenderable.boldIndices().forEach(i ->
                validateIndex(i, textLineRenderable.lineText().length(),
                        "textLineRenderable.boldIndices()"));
    }

    private void validateIndex(Integer index, int textLineLength, String dataStructureName) {
        if (index == null) {
            throw new IllegalArgumentException("TextLineRenderer.render: " + dataStructureName +
                    " cannot contain null key");
        }
        if (index < 0) {
            throw new IllegalArgumentException("TextLineRenderer.render: " + dataStructureName +
                    " cannot contain negative key");
        }
        if (index >= textLineLength) {
            throw new IllegalArgumentException("TextLineRenderer.render: " + dataStructureName +
                    " cannot contain a key beyond lineText length");
        }
    }
}
