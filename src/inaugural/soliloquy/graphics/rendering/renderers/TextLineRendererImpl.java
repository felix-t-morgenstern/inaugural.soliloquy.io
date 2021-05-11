package inaugural.soliloquy.graphics.rendering.renderers;

import inaugural.soliloquy.tools.Check;
import soliloquy.specs.common.valueobjects.EntityUuid;
import soliloquy.specs.graphics.assets.Font;
import soliloquy.specs.graphics.assets.FontStyleInfo;
import soliloquy.specs.graphics.renderables.TextLineRenderable;
import soliloquy.specs.graphics.renderables.providers.ProviderAtTime;
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

        FloatBox renderingAreaFromRenderable =
                textLineRenderable.renderingAreaProvider().provide(timestamp);

        iterateOverTextLine(textLineRenderable, timestamp,
                textLineLengthThusFar -> glyphLength -> textureId -> glyphBox -> color -> {
                    float leftX = renderingAreaFromRenderable.leftX() + textLineLengthThusFar;
                    FloatBox renderingArea = FLOAT_BOX_FACTORY.make(
                            leftX,
                            renderingAreaFromRenderable.topY(),
                            leftX + glyphLength,
                            renderingAreaFromRenderable.topY() + textLineRenderable.lineHeight()
                    );

                    super.render(renderingArea,
                            glyphBox.leftX(), glyphBox.topY(),
                            glyphBox.rightX(), glyphBox.bottomY(),
                            textureId,
                            color);
        });
    }

    @Override
    public float textLineLength(TextLineRenderable textLineRenderable)
            throws IllegalArgumentException {
        validateTextLineRenderable(textLineRenderable, "textLineLength");

        return iterateOverTextLine(textLineRenderable, null, null);
    }

    // NB: null timestamp implies that colorIndices should be ignored altogether. This isn't elegant, but this is not front-facing code.
    private float iterateOverTextLine(TextLineRenderable textLineRenderable, Long timestamp,
                                      Function<Float, Function<Float, Function<Integer,
                                              Function<FloatBox, Consumer<Color>>>>>
                                              renderingAction) {
        boolean italic = false;
        boolean bold = false;
        int nextItalicIndex = 0;
        int nextBoldIndex = 0;
        float textLineLengthThusFar = 0f;
        Color color = DEFAULT_COLOR;
        FontStyleInfo fontStyleInfo;

        for (int i = 0; i < textLineRenderable.lineText().length(); i++) {
            if (timestamp != null) {
                if (textLineRenderable.colorProviderIndices() != null &&
                        textLineRenderable.colorProviderIndices().containsKey(i)) {
                    color = textLineRenderable.colorProviderIndices().get(i).provide(timestamp);
                }
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


            if (italic) {
                if (bold) {
                    fontStyleInfo = textLineRenderable.font().boldItalic();
                }
                else {
                    fontStyleInfo = textLineRenderable.font().italic();
                }
            }
            else {
                if (bold) {
                    fontStyleInfo = textLineRenderable.font().bold();
                }
                else {
                    fontStyleInfo = textLineRenderable.font().plain();
                }
            }

            char character = textLineRenderable.lineText().charAt(i);
            FloatBox glyphBox = fontStyleInfo.getUvCoordinatesForGlyph(character);
            float glyphLength =
                    glyphBox.width() * (textLineRenderable.lineHeight() / glyphBox.height())
                    * fontStyleInfo.textureWidthToHeightRatio();

            if (renderingAction != null) {
                renderingAction.apply(textLineLengthThusFar).apply(glyphLength)
                        .apply(fontStyleInfo.textureId()).apply(glyphBox).accept(color);
            }

            float lengthThusFarAddition = glyphLength;
            float paddingPercentage = 1f + fontStyleInfo.additionalHorizontalPadding();
            if (fontStyleInfo.glyphwiseAdditionalHorizontalPadding() != null &&
                    fontStyleInfo.glyphwiseAdditionalHorizontalPadding().containsKey(character)) {
                paddingPercentage +=
                        fontStyleInfo.glyphwiseAdditionalHorizontalPadding().get(character);
            }
            lengthThusFarAddition /= paddingPercentage;

            textLineLengthThusFar += lengthThusFarAddition;
        }

        return textLineLengthThusFar;
    }

    private void validateTextLineRenderable(TextLineRenderable textLineRenderable,
                                            String methodName) {
        Check.ifNull(textLineRenderable, "textLineRenderable");
        Check.ifNull(textLineRenderable.font(), "textLineRenderable.font()");
        Check.throwOnLteZero(textLineRenderable.lineHeight(), "textLineRenderable.lineHeight()");
        Check.ifNull(textLineRenderable.renderingAreaProvider(),
                "textLineRenderable.renderingAreaProvider()");
        Check.ifNull(textLineRenderable.uuid(), "textLineRenderable.id()");
        if (textLineRenderable.colorProviderIndices() != null) {
            Integer highestIndexThusFar = null;
            Set<Map.Entry<Integer, ProviderAtTime<Color>>> colorProviderIndicesEntries =
                    textLineRenderable.colorProviderIndices().entrySet();
            for (Map.Entry<Integer, ProviderAtTime<Color>> entry : colorProviderIndicesEntries) {
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
        public EntityUuid uuid() {
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
        public ProviderAtTime<FloatBox> renderingAreaProvider() {
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
        public Map<Integer, ProviderAtTime<Color>> colorProviderIndices() {
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
        public String getInterfaceName() {
            return TextLineRenderable.class.getCanonicalName();
        }
    };
}
