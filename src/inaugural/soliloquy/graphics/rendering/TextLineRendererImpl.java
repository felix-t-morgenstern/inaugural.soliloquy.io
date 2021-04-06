package inaugural.soliloquy.graphics.rendering;

import inaugural.soliloquy.graphics.archetypes.TextLineRenderableArchetype;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.renderables.TextLineRenderable;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.graphics.rendering.TextLineRenderer;
import soliloquy.specs.graphics.rendering.factories.FloatBoxFactory;

public class TextLineRendererImpl extends CanRenderSnippets<TextLineRenderable>
        implements TextLineRenderer {
    private static final TextLineRenderable ARCHETYPE = new TextLineRenderableArchetype();

    public TextLineRendererImpl(RenderingBoundaries renderingBoundaries,
                                FloatBoxFactory floatBoxFactory) {
        super(renderingBoundaries, floatBoxFactory, ARCHETYPE);
    }

    @Override
    public void render(TextLineRenderable textLineRenderable) throws IllegalArgumentException {
        validateTextLineRenderable(textLineRenderable, "render");

        System.out.println("Rendering text line...");
    }

    @Override
    public float textLineLength(TextLineRenderable textLineRenderable)
            throws IllegalArgumentException {
        validateTextLineRenderable(textLineRenderable, "textLineLength");
        return 0;
    }

    private void validateTextLineRenderable(TextLineRenderable textLineRenderable,
                                            String methodName) {
        Check.ifNull(textLineRenderable.font(), "textLineRenderable.font()");
        Check.throwOnLteZero(textLineRenderable.lineHeight(), "textLineRenderable.lineHeight()");
        if (textLineRenderable.colorIndices() != null) {
            textLineRenderable.colorIndices().forEach((index, color) -> {
                validateIndex(index, textLineRenderable.lineText().length(),
                        "textLineRenderable.colorIndices()", methodName);
                if (color == null) {
                    throw new IllegalArgumentException("TextLineRendererImpl." + methodName + ": "
                            + "textLineRenderable.colorIndices cannot contain null value");
                }
            });
        }
        if (textLineRenderable.italicIndices() != null) {
            textLineRenderable.italicIndices().forEach(i ->
                    validateIndex(i, textLineRenderable.lineText().length(),
                            "textLineRenderable.italicIndices()", methodName));
        }
        if (textLineRenderable.boldIndices() != null) {
            textLineRenderable.boldIndices().forEach(i ->
                    validateIndex(i, textLineRenderable.lineText().length(),
                            "textLineRenderable.boldIndices()", methodName));
        }
    }

    private void validateIndex(Integer index, int textLineLength, String dataStructureName,
                               String methodName) {
        if (index == null) {
            throw new IllegalArgumentException("TextLineRendererImpl." + methodName + ": " +
                    dataStructureName + " cannot contain null key");
        }
        if (index < 0) {
            throw new IllegalArgumentException("TextLineRendererImpl." + methodName + ": " +
                    dataStructureName + " cannot contain negative key");
        }
        if (index >= textLineLength) {
            throw new IllegalArgumentException("TextLineRendererImpl." + methodName + ": " +
                    dataStructureName + " cannot contain a key beyond lineText length");
        }
    }
}
