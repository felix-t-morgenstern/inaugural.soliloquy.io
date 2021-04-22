package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.renderables.TextLineRenderable;
import soliloquy.specs.graphics.rendering.FloatBox;
import soliloquy.specs.graphics.rendering.RenderingBoundaries;
import soliloquy.specs.graphics.rendering.factories.FloatBoxFactory;
import soliloquy.specs.graphics.rendering.renderers.TextLineRenderer;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import static inaugural.soliloquy.graphics.api.Constants.MAX_CHANNEL_VAL;

public class TextLineRendererImpl extends CanRenderSnippets<TextLineRenderable>
        implements TextLineRenderer {
    private final Color DEFAULT_COLOR;

    public TextLineRendererImpl(RenderingBoundaries renderingBoundaries,
                                FloatBoxFactory floatBoxFactory,
                                Color defaultColor) {
        super(renderingBoundaries, floatBoxFactory, ARCHETYPE);
        DEFAULT_COLOR = Check.ifNull(defaultColor, "defaultColor");
    }

    @Override
    public void render(TextLineRenderable textLineRenderable, long timestamp)
            throws IllegalArgumentException {
        validateTextLineRenderable(textLineRenderable, "render");

        validateTimestamp(timestamp, "TextLineRendererImpl");

        iterateOverTextLine(textLineRenderable,
                textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> color -> {
                    float leftX = textLineRenderable.renderingArea().leftX() +
                            textLineLengthThusFar;
                    FloatBox renderingArea = FLOAT_BOX_FACTORY.make(
                            leftX,
                            textLineRenderable.renderingArea().topY(),
                            leftX + glyphLength,
                            textLineRenderable.renderingArea().topY() +
                                    textLineRenderable.lineHeight()
                    );

                    super.render(renderingArea,
                            glyphBox.leftX(), glyphBox.topY(),
                            glyphBox.rightX(), glyphBox.bottomY(),
                            textureId,
                            color.getRed() / MAX_CHANNEL_VAL,
                            color.getGreen() / MAX_CHANNEL_VAL,
                            color.getBlue() / MAX_CHANNEL_VAL,
                            color.getAlpha() / MAX_CHANNEL_VAL);
        });
    }

    @Override
    public float textLineLength(TextLineRenderable textLineRenderable)
            throws IllegalArgumentException {
        validateTextLineRenderable(textLineRenderable, "textLineLength");

        return iterateOverTextLine(textLineRenderable, null);
    }

    private float iterateOverTextLine(TextLineRenderable textLineRenderable,
                                      Function<Float, Function<Float, Function<Integer,
                                              Function<FloatBox, Consumer<Color>>>>>
                                              renderingAction) {
        boolean italic = false;
        boolean bold = false;
        int nextItalicIndex = 0;
        int nextBoldIndex = 0;
        float textLineLengthThusFar = 0f;
        int textureId;
        Color color = DEFAULT_COLOR;
        Map<Character, Float> glyphwiseAdditionalHorizontalPadding =
                textLineRenderable.font().glyphwiseAdditionalHorizontalPadding();

        for (int i = 0; i < textLineRenderable.lineText().length(); i++) {
            if (textLineRenderable.colorIndices() != null &&
                    textLineRenderable.colorIndices().containsKey(i)) {
                color = textLineRenderable.colorIndices().get(i);
            }
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
            float textureWidthToHeightRatio;

            if (italic) {
                if (bold) {
                    glyphBox = textLineRenderable.font().getUvCoordinatesForGlyphBoldItalic(glyph);
                    textureId = textLineRenderable.font().textureIdBoldItalic();
                    textureWidthToHeightRatio =
                            textLineRenderable.font().textureWidthToHeightRatioBoldItalic();
                }
                else {
                    glyphBox = textLineRenderable.font().getUvCoordinatesForGlyphItalic(glyph);
                    textureId = textLineRenderable.font().textureIdItalic();
                    textureWidthToHeightRatio =
                            textLineRenderable.font().textureWidthToHeightRatioItalic();
                }
            }
            else {
                if (bold) {
                    glyphBox = textLineRenderable.font().getUvCoordinatesForGlyphBold(glyph);
                    textureId = textLineRenderable.font().textureIdBold();
                    textureWidthToHeightRatio =
                            textLineRenderable.font().textureWidthToHeightRatioBold();
                }
                else {
                    glyphBox = textLineRenderable.font().getUvCoordinatesForGlyph(glyph);
                    textureId = textLineRenderable.font().textureId();
                    textureWidthToHeightRatio =
                            textLineRenderable.font().textureWidthToHeightRatio();
                }
            }

            float glyphLength =
                    glyphBox.width() * (textLineRenderable.lineHeight() / glyphBox.height())
                    * textureWidthToHeightRatio;

            if (renderingAction != null) {
                renderingAction.apply(textLineLengthThusFar).apply(glyphLength).apply(textureId)
                        .apply(glyphBox).accept(color);
            }

            float lengthThusFarAddition = glyphLength;
            if (glyphwiseAdditionalHorizontalPadding != null &&
                    glyphwiseAdditionalHorizontalPadding.containsKey(glyph)) {
                lengthThusFarAddition /= (1f + glyphwiseAdditionalHorizontalPadding.get(glyph));
            }

            textLineLengthThusFar += lengthThusFarAddition;
        }

        return textLineLengthThusFar;
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

    private static final TextLineRenderable ARCHETYPE = new TextLineRenderable() {
        @Override
        public String getInterfaceName() {
            return TextLineRenderable.class.getCanonicalName();
        }

        @Override
        public EntityUuid id() {
            return null;
        }

        @Override
        public Font font() {
            return null;
        }

        @Override
        public String lineText() {
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
        public void delete() {

        }

        @Override
        public float lineHeight() {
            return 0;
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
    };
}
