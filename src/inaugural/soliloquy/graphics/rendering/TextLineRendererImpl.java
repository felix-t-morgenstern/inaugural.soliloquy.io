package inaugural.soliloquy.graphics.rendering;

import inaugural.soliloquy.graphics.archetypes.TextLineRenderableArchetype;
import inaugural.soliloquy.tools.Check;
import soliloquy.specs.graphics.renderables.TextLineRenderable;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.graphics.rendering.TextLineRenderer;
import soliloquy.specs.graphics.rendering.factories.FloatBoxFactory;

import java.awt.*;
import java.util.Map;
import java.util.Set;

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

        boolean italic = false;
        boolean bold = false;
        int nextItalicIndex = 0;
        int nextBoldIndex = 0;
        float length = 0f;

        for (int i = 0; i < textLineRenderable.lineText().length(); i++) {
            if (textLineRenderable.italicIndices() != null &&
                    textLineRenderable.italicIndices().size() > nextItalicIndex &&
                    textLineRenderable.italicIndices().get(nextItalicIndex) == i) {
                italic = !italic;
                nextItalicIndex++;
            }
            if (textLineRenderable.boldIndices() != null &&
                    textLineRenderable.boldIndices().size() > nextBoldIndex &&
                    textLineRenderable.boldIndices().get(nextBoldIndex) == i) {
                bold = !bold;
                nextBoldIndex++;
            }

            char glyph = textLineRenderable.lineText().charAt(i);
            FloatBox glyphBox;

            if (italic) {
                if (bold) {
                    glyphBox = textLineRenderable.font().getUvCoordinatesForGlyphBoldItalic(glyph);
                }
                else {
                    glyphBox = textLineRenderable.font().getUvCoordinatesForGlyphItalic(glyph);
                }
            }
            else {
                if (bold) {
                    glyphBox = textLineRenderable.font().getUvCoordinatesForGlyphBold(glyph);
                }
                else {
                    glyphBox = textLineRenderable.font().getUvCoordinatesForGlyph(glyph);
                }
            }

            length += glyphBox.width() * (textLineRenderable.lineHeight() / glyphBox.height());
        }

        return length;
    }

    private void validateTextLineRenderable(TextLineRenderable textLineRenderable,
                                            String methodName) {
        Check.ifNull(textLineRenderable.font(), "textLineRenderable.font()");
        Check.throwOnLteZero(textLineRenderable.lineHeight(), "textLineRenderable.lineHeight()");
        if (textLineRenderable.colorIndices() != null) {
            Integer highestIndexThusFar = null;
            Set<Map.Entry<Integer, Color>> colorIndicesEntries =
                    textLineRenderable.colorIndices().entrySet();
            for (Map.Entry<Integer, Color> entry : colorIndicesEntries) {
                validateIndex(entry.getKey(), textLineRenderable.lineText().length(),
                        "textLineRenderable.colorIndices()", methodName, highestIndexThusFar);
                highestIndexThusFar = entry.getKey();
                if (entry.getValue() == null) {
                    throw new IllegalArgumentException("TextLineRendererImpl." + methodName + ": "
                            + "textLineRenderable.colorIndices cannot contain null value");
                }
            }
        }
        if (textLineRenderable.italicIndices() != null) {
            Integer highestIndexThusFar = null;
            for (Integer index : textLineRenderable.italicIndices()) {
                validateIndex(index, textLineRenderable.lineText().length(),
                        "textLineRenderable.italicIndices()", methodName,
                        highestIndexThusFar);
                highestIndexThusFar = index;
            }
        }
        if (textLineRenderable.boldIndices() != null) {
            Integer highestIndexThusFar = null;
            for (Integer index : textLineRenderable.boldIndices()) {
                validateIndex(index, textLineRenderable.lineText().length(),
                        "textLineRenderable.italicIndices()", methodName,
                        highestIndexThusFar);
                highestIndexThusFar = index;
            }
        }
    }

    private void validateIndex(Integer index, int textLineLength, String dataStructureName,
                               String methodName, Integer highestIndexThusFar) {
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
        if (highestIndexThusFar != null && index <= highestIndexThusFar) {
            throw new IllegalArgumentException("TextLineRendererImpl." + methodName + ": " +
                    dataStructureName + " cannot contain an index out of ascending order");
        }
    }
}
